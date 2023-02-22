package dwn.jfx.retail;

import dwn.jfx.retail.models.Delivery;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;

public class LivraisonController {
    public ProgressBar LivPg;
    public Label livLb;
    public AnchorPane livPane;

    public AnchorPane bind(Delivery livraison) {
        livLb.setText(livraison.toString());
        LivPg.progressProperty().bind(livraison.progressProperty());
        return livPane;
    }
}
