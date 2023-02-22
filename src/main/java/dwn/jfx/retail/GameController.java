package dwn.jfx.retail;

import dwn.jfx.retail.service.RetailService;
import javafx.fxml.Initializable;
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
        // soldeLabel <- StringProperty
    }

    private void bindGameProgressBar() {
        // RetailService.getGameProgress()
    }

    private void bindGameTimerLabel() {
        // RetailService.getGameTimer()
    }

    private void bindSelectedCommand() {
        // On souhaite ici mettre à jour tous les champs du panel Commande
        // avec les informations d'une commande selectionnée
        // RetailService.setOnCommandSelected(lambda)
    }

    private void bindDeliveryToVBox() {
        // RetailService.setOnDeliveryCreated(lambda)
        // RetailService.setOnDeliveryEnded(lambda)
    }

    private void bindCommandsToVBox() {
        // RetailService.setOnCommandCreated(lambda)
        // RetailService.setOnCommandEnded(lambda)
    }

    private static void bindObjectToNode() {
        // Il faut ajouter un bouton à la creation d'une commande
        // Command.setButtonCreator(lambda)
        // Comment créer le bouton à la création d'une commande
    }

    private static void bindObjectToFXML() {
        // Il faut ajouter un Panel existant (livraison.fxml)
        // Delivery.setAnchorPaneCreator(lambda)
        // Comment récupérer un template et mettre à jour ses valeurs
    }

    public void showStock() {
        // Ouvrir une nouvelle fenêtre : stock.fxml
    }
}