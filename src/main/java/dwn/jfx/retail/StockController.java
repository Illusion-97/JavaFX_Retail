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
    private final DoubleProperty cout = new SimpleDoubleProperty(0);
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
    }

    private void bindQuantite() {
        // qteTF : TextProperty -> IntegerProperty
        // qteTF -> selectedQuantite
    }

    private void bindDuration() {
        //durationLAbel <- Delivery.getDuration(qte)
    }

    private void bindTableView() {
        // RetailService.getStockObservableList() : ObservableList<Stock>
        // Pour chaque colonne dire quoi afficher par rapport à un objet de type stock
        // La fonction à utiliser contiens 4 mots et attends une lambda<Stock>
    }

    public void bindChart() {
        // selectedProduit -> RetailService.getProductStats -> stPC
    }

    private void bindProduit() {
        // selectedProduit <- stockTV
        // productLabel <- selectedProduit
    }

    private void bindCout() {
        DoubleProperty produitCout = new SimpleDoubleProperty(0);
        // produitCout <- selectedProduit
        // cout <- produitCout multiply selectedQuantite
        // ctLb <- getMoneyString(cout)
    }

    private void bindCommanderButton() {
        // disableProperty -> produit.isNull() | selectedQuantite < | cout > RetailService.getMoney
        // action -> RetailService.newLivraison(selectedProduit, selectedQuantite)
    }
}
