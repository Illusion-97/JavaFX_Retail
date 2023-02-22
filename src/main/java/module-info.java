module dwn.jfx.retail {
    requires javafx.controls;
    requires javafx.fxml;


    opens dwn.jfx.retail to javafx.fxml;
    exports dwn.jfx.retail;
}