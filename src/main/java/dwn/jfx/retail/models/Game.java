package dwn.jfx.retail.models;

import dwn.jfx.retail.service.RetailService;
import dwn.jfx.retail.tools.Randomizer;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.concurrent.Task;

import java.util.Random;

public class Game extends Task<Integer> {

    private static final Random random = new Random();
    private static final Product[] productsArray = Product.values();
    private final double gameDuration;
    private final DoubleProperty gameTimer;
    private double commandInterval;

    public Game(double gameDuration) {
        this.gameDuration = gameDuration;
        gameTimer = new SimpleDoubleProperty(gameDuration);
        commandInterval = 0.01;
    }

    public DoubleProperty gameTimerProperty() {
        return gameTimer;
    }

    @Override
    protected Integer call() throws Exception {
        while (gameTimer.get() > 0) {
            double currentSec = gameTimer.get() - 0.01;
            Platform.runLater(() -> gameTimer.set(currentSec));
            commandInterval -= 0.01;
            if (commandInterval <= 0) {
                RetailService.newCommand(productsArray[random.nextInt(productsArray.length)], (int) Randomizer.Randomize(10));
                commandInterval = Randomizer.Randomize(15);
            }
            this.updateProgress(currentSec, gameDuration);
            waitTenMilliSec();
        }
        return null;
    }

    private void waitTenMilliSec() throws InterruptedException {
        Thread.sleep(10);
    }
}
