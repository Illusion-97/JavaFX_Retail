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
    }

    private void bindGameProgressBar() {
    }

    private void bindGameTimerLabel() {
    }

    private void bindSelectedCommand() {
    }

    private void bindDeliveryToVBox() {
    }

    private void bindCommandsToVBox() {
    }

    private static void bindObjectToNode() {
    }

    private static void bindObjectToFXML() {
    }

    public void showStock() {
    }
}