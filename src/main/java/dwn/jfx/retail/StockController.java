package dwn.jfx.retail;

import dwn.jfx.retail.models.Delivery;
import dwn.jfx.retail.models.Product;
import dwn.jfx.retail.models.Stock;
import dwn.jfx.retail.service.RetailService;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

import static dwn.jfx.retail.tools.Monetize.getMoneyString;

public class StockController implements Initializable {

    // region PROPERTIES
    private final DoubleProperty coutLivraison = new SimpleDoubleProperty(0);
    private final ObjectProperty<Product> selectedProduit = new SimpleObjectProperty<>(null);
    private final IntegerProperty selectedQuantite = new SimpleIntegerProperty(0);
    // endregion

    // region CONTROLS
    public TableView<Stock> stockTableView;
    public TableColumn<Stock, String> stockProduitTC;
    public TableColumn<Stock, Number> stockQteTC;
    public TableColumn<Stock, String> stockPriceTC;
    public PieChart stockPieChart;
    public TextField qteTF;
    public Label ctLB;
    public Label soldeLabel;
    public Button livraisonBtn;
    public Label productLabel;
    public Label duration;
    // endregion

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        formatTextToNumber();
        bindBanque();
        bindTableView();
        bindProduit();
        bindChart();
        bindQuantite();
        bindCout();
        bindDuration();
        bindCommanderButton();
    }

    private void formatTextToNumber() {
        qteTF.setTextFormatter(new TextFormatter<Number>(change -> {
            if (change.getText().matches("[0-9]*")) {
                if (change.getControlNewText().isBlank()) {
                    change.setText("0");
                }
                if (change.isContentChange() && change.getControlText().equals("0")) {
                    change.setRange(0, 1);
                }
                return change;
            }
            return null;
        }));
    }

    private void bindBanque() {
        // soldeLabel
        soldeLabel.textProperty().bind(getMoneyString(RetailService.getMoney()));
    }

    private void bindQuantite() {
        // qteTF.setText("0");
        // qteTF : TextProperty -> IntegerProperty
        // qteTF -> selectedQuantite
        selectedQuantite.bind(qteTF.textProperty().map(text -> Integer.valueOf(text)));
        // selectedQuantite.bind(qteTF.textProperty().map(Integer::valueOf));
        // qteTF.textProperty().bind(Bindings.convert(selectedQuantite)); (relation inverse)
        /*selectedQuantite.bind(Bindings.createIntegerBinding(() -> {
            if(qteTF.getText().isEmpty()) {
                return 0;
            } else {
             return Integer.parseInt(qteTF.getText());
            }
        }, qteTF.textProperty()));*/
    }

    private void bindDuration() {
        //durationLabel <- Delivery.getDuration(qte)
        duration.textProperty().bind(selectedQuantite.map(qte -> new SimpleDateFormat("mm:ss")
                .format(Delivery.getDelay(qte.intValue()) * 1000)));
    }

    private void bindTableView() {
        // RetailService.getStockObservableList() : ObservableList<Stock>
        // Pour chaque colonne dire quoi afficher par rapport à un objet de type stock
        // La fonction à utiliser contiens 4 mots et attends une lambda<Stock>
        stockTableView.setItems(RetailService.getStockObservableList());
        stockProduitTC.setCellValueFactory(cellData -> cellData.getValue().getName());
        stockQteTC.setCellValueFactory(p -> p.getValue().getQte());
        stockPriceTC.setCellValueFactory(stockData -> stockData.getValue().getPrix());
    }

    public void bindChart() {
        // selectedProduit -> RetailService.getProductStats -> stPC
        stockPieChart.dataProperty().bind(selectedProduit.map(product -> RetailService.getProductStats(product)));
        // stockPieChart.dataProperty().bind(selectedProduit.map(RetailService::getProductStats));
    }

    private void bindProduit() {
        // selectedProduit <- stockTV
        // productLabel <- selectedProduit
        selectedProduit.bind(stockTableView.getSelectionModel().selectedItemProperty().map(stock -> stock.getProduit()));
        // selectedProduit.bind(stockTableView.getSelectionModel().selectedItemProperty().map(Stock::getProduit));
        productLabel.textProperty().bind(selectedProduit.map(produit -> produit.getName()).orElse("Produit"));
        // productLabel.textProperty().bind(selectedProduit.map(Merch::getName).orElse("Produit"));
    }

    private void bindCout() {
        DoubleProperty produitCout = new SimpleDoubleProperty(0);
        // produitCout <- selectedProduit
        produitCout.bind(selectedProduit.map(product -> product.getPrice()));
        // produitCout.bind(selectedProduit.map(Merch::getPrice));

        // coutLivraison <- produitCout multiply selectedQuantite
        coutLivraison.bind(produitCout.multiply(selectedQuantite));
        // coutLivraison.bind(Bindings.multiply(produitCout,selectedQuantite));

        // ctLb <- getMoneyString(coutLivraison)
        ctLB.textProperty().bind(getMoneyString(coutLivraison));
    }

    private void bindCommanderButton() {
        // disableProperty -> produit.isNull() | selectedQuantite < 1 | coutLivraison > RetailService.getMoney
        livraisonBtn.disableProperty().bind(selectedProduit.isNull()
                .or(selectedQuantite.lessThan(1)).or(coutLivraison.greaterThan(RetailService.getMoney())));
        // action -> RetailService.newLivraison(selectedProduit, selectedQuantite)
        livraisonBtn.setOnAction(actionEvent -> {
            try {
                RetailService.newLivraison(selectedProduit.getValue(),selectedQuantite.get());
            } catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.CLOSE);
                alert.setHeaderText(e.getClass().getSimpleName());
                alert.showAndWait();
            }
        });
    }
}
