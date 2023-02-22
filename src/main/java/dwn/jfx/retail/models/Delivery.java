package dwn.jfx.retail.models;

import dwn.jfx.retail.tools.Randomizer;
import javafx.concurrent.Task;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.function.Function;

public class Delivery extends Task<Integer> {
    private static final double productionDelay = 2;
    private static final double deliveryDelay = 5;

    private static Function<Delivery, AnchorPane> anchorPaneCreator;
    private final Product product;
    private final int qte;
    private final double maxSec;
    private final AnchorPane pane;
    private double currentSec;

    public Delivery(Product product, int qte) throws IOException {
        this.product = product;
        this.qte = qte;
        maxSec = getDelay(qte);
        pane = anchorPaneCreator != null ? anchorPaneCreator.apply(this) : null;
    }

    public static double getDelay(int qte) {
        return qte * Randomizer.Randomize(productionDelay) + Randomizer.Randomize(deliveryDelay);
    }

    public static void setAnchorPaneCreator(Function<Delivery, AnchorPane> anchorPaneCreator) {
        Delivery.anchorPaneCreator = anchorPaneCreator;
    }

    public double getMaxSec() {
        return maxSec;
    }

    public double getCost() {
        return qte * product.getPrice();
    }

    public Product getProduit() {
        return product;
    }

    public int getQte() {
        return qte;
    }

    @Override
    protected Integer call() throws Exception {
        while (currentSec < maxSec) {
            currentSec += 0.01;
            this.updateProgress(maxSec - currentSec, maxSec);
            waitTenMilliSec();
        }
        return qte;
    }

    private void waitTenMilliSec() throws InterruptedException {
        Thread.sleep(10);
    }

    @Override
    public String toString() {
        return product.getName() + " (" + qte + ")";
    }

    public AnchorPane getPane() {
        return pane;
    }
}
