package dwn.jfx.retail.models;

import javafx.concurrent.Task;
import javafx.scene.control.Button;

import java.util.function.Function;


public class Command extends Task<Double> {
    private static Function<Command,Button> buttonCreator;
    private final Product product;
    private final int qte;
    private final Button button;
    private double taux;
    private double delay;

    public Command(Product product, int qte) {
        this.product = product;
        this.qte = qte;
        taux = 1.5;
        delay = 0;
        button = buttonCreator != null ? buttonCreator.apply(this) : null;
    }

    public static void setButtonCreator(Function<Command, Button> buttonCreator) {
        Command.buttonCreator = buttonCreator;
    }

    public Product getProduit() {
        return product;
    }

    public int getQte() {
        return qte;
    }

    public double getTaux() {
        return taux;
    }

    public double getDelay() {
        return delay;
    }

    public Double getGain() {
        return product.getPrice() * qte * taux;
    }

    @Override
    protected Double call() throws Exception {
        while (delay < 300) {
            delay += 0.01;
            taux -= 0.00003;
            this.updateProgress(300 - delay, 300);
            this.updateMessage(String.format("Taux : %.2f", taux));
            waitTen();
        }
        return 0D;
    }

    private void waitTen() throws InterruptedException {
        Thread.sleep(10);
    }

    @Override
    public String toString() {
        return product.getName() + " (" + qte + ")";
    }

    public Button getButton() {
        return button;
    }
}
