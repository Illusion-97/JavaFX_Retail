package dwn.jfx.retail;

import dwn.jfx.retail.models.Command;
import dwn.jfx.retail.service.RetailService;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static dwn.jfx.retail.tools.Monetize.getMoneyString;


public class GameController extends Stage implements Initializable {

    public TitledPane detailCommandeTPane;
    public VBox commandesVBox;
    public VBox livraisonsVBox;
    public Label tauxCommandeLabel;
    public ProgressBar commandeProgressBar;
    public Label stockLabel;
    public Label qteCommandeLabel;
    public Label produitCommandeLabel;
    public BorderPane commandePanel;
    public Button validerCommandeButton;
    public TextField soldeTextField;
    public ProgressBar gameProgressBar;
    public Label gameTimerText;

    private static void showExceptionAlert(IOException e) {
        Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.CLOSE);
        alert.setHeaderText(e.getClass().getSimpleName());
        alert.showAndWait();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bindCommandsToVBox();
        bindSelectedCommand();
        bindDeliveryToVBox();
        bindObjectToNode();
        bindObjectToFXML();
        RetailService.startGame();
        bindSolde();
        bindGameTimerLabel();
        bindGameProgressBar();
    }

    private void bindSolde() {
        // bind
        // RetailService.getMoney() -> DoubleProperty
        // getMoneyString(DoubleProperty) -> StringProperty
        // soldeTextField <- StringProperty
        soldeTextField.textProperty().bind(getMoneyString(RetailService.getMoney()));
    }

    private void bindGameProgressBar() {
        gameProgressBar.progressProperty().bind(RetailService.getGameProgress());
    }

    private void bindGameTimerLabel() {
        gameTimerText.textProperty().bind(RetailService.getCurrentGameTime());
    }

    private void bindSelectedCommand() {
        // On souhaite ici mettre à jour tous les champs du panel Commande
        // avec les informations d'une commande selectionnée
        // RetailService.setOnCommandSelected(lambda)
        RetailService.setOnCommandSelected(optionalCommand -> {
            commandePanel.setOpacity(0);
            optionalCommand.ifPresent(command -> {
                commandePanel.setOpacity(1);
                detailCommandeTPane.setExpanded(true);
                produitCommandeLabel.setText(command.getProduit().getName());
                commandeProgressBar.progressProperty().bind(command.progressProperty());
                tauxCommandeLabel.textProperty().bind(command.messageProperty());
                qteCommandeLabel.setText(String.valueOf(command.getQte()));
                SimpleIntegerProperty stock = RetailService.getStock(command.getProduit());
                stockLabel.textProperty().bind(stock.asString());
                validerCommandeButton.disableProperty().bind(stock.lessThan(command.getQte()));
                validerCommandeButton.setOnAction(actionEvent -> command.cancel());
            });
        });
    }

    private void bindDeliveryToVBox() {
        // RetailService.setOnDeliveryCreated(lambda)
        // RetailService.setOnDeliveryEnded(lambda)

        ObservableList<Node> deliveries = livraisonsVBox.getChildren();
        RetailService.setOnDeliveryCreated(deliveries::add);
        RetailService.setOnDeliveryEnded(deliveries::remove);
    }

    private void bindCommandsToVBox() {
        // RetailService.setOnCommandCreated(lambda)
        // RetailService.setOnCommandEnded(lambda)
        /*RetailService.setOnCommandCreated(button -> commandesVBox.getChildren().add(button));
        RetailService.setOnCommandCreated(button -> commandesVBox.getChildren().add(button));*/
        ObservableList<Node> commands = commandesVBox.getChildren();
        RetailService.setOnCommandCreated(commands::add);
        RetailService.setOnCommandEnded(commands::remove);

    }

    private static void bindObjectToNode() {
        // Il faut ajouter un bouton à la creation d'une commande
        // Command.setButtonCreator(lambda)
        // Comment créer le bouton à la création d'une commande
        Command.setButtonCreator(command -> {
            Button commandButton = new Button(command.toString());
            commandButton.setMaxWidth(Double.MAX_VALUE);
            return commandButton;
        });
    }

    private static void bindObjectToFXML() {
        // Il faut ajouter un Panel existant (livraison.fxml)
        // Delivery.setAnchorPaneCreator(lambda)
        // Comment récupérer un template et mettre à jour ses valeurs
    }

    public void showStock() {
        // Ouvrir une nouvelle fenêtre : stock.fxml
        try {
            Stage stage = new Stage();
            stage.setTitle("Stock");
            FXMLLoader fxmlLoader = new FXMLLoader(GameController.class.getResource("stock.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showExceptionAlert(e);
        }
    }
}