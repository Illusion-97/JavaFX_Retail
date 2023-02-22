package dwn.jfx.retail.tools;

import javafx.beans.binding.StringBinding;
import javafx.beans.property.DoubleProperty;

public class Monetize {
    public static StringBinding getMoneyString(DoubleProperty moneyDouble) {
        return moneyDouble.asString("%.2f €");
    }
}
