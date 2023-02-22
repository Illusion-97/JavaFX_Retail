package dwn.jfx.retail.service;

import dwn.jfx.retail.models.*;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class RetailService {
    private static final ObservableList<Command> endedCommands = FXCollections.observableArrayList();
    private static final ObservableList<Delivery> endedDeliveries = FXCollections.observableArrayList();
    private static final ObjectProperty<Optional<Command>> selectedCommand = new SimpleObjectProperty<>(Optional.empty());
    public static void setOnCommandCreated(Consumer<Button> onCommandCreated) {
        RetailService.onCommandCreated = onCommandCreated;
    }
    public static void setOnCommandSelected(Consumer<Optional<Command>> onCommandSelected) {
        RetailService.onCommandSelected = onCommandSelected;
    }
    public static void setOnDeliveryCreated(Consumer<AnchorPane> onDeliveryCreated) {
        RetailService.onDeliveryCreated = onDeliveryCreated;
    }
    private static Consumer<Button> onCommandCreated;
    private static Consumer<Optional<Command>> onCommandSelected;
    private static Consumer<AnchorPane> onDeliveryCreated;
    private static Consumer<Button> onCommandEnded;
    private static Consumer<AnchorPane> onDeliveryEnded;

    private static final Map<Product, SimpleIntegerProperty> stock = Arrays.stream(Product.values())
            .collect(Collectors.toMap(value -> value, val -> new SimpleIntegerProperty(15)));

    private static DoubleProperty money;
    private static Game game;

    private static void setStartingMoney(double startingMoney) {
        money = new SimpleDoubleProperty(startingMoney);
    }

    public static void startGame(double startingMoney, double gameDuration) {
        init();
        setStartingMoney(startingMoney);
        startGameThread(gameDuration);
    }

    public static void startGame() {
        init();
        setStartingMoney(5000);
        startGameThread(600);
    }

    private static void init() {
        StatsService.bind(endedDeliveries, endedCommands);
        selectedCommand.addListener((observableValue, command, t1) -> {
            if(onCommandSelected != null) onCommandSelected.accept(t1);
        });
    }

    private static void startGameThread(double gameDuration) {
        game = new Game(gameDuration);
        Thread gameThread = new Thread(game);
        gameThread.setDaemon(true);
        gameThread.start();
    }

    public static DoubleProperty getMoney() {
        return money;
    }

    public static void newCommand(Product product, int qte) {
        Command c = new Command(product, qte);
        if(onCommandCreated != null) Platform.runLater(() -> onCommandCreated.accept(c.getButton()));
        if(c.getButton() != null) c.getButton().setOnAction(actionEvent -> selectedCommand.set(Optional.of(c)));
        c.setOnSucceeded(workerStateEvent -> endCommand(c, false));
        c.setOnCancelled(workerStateEvent -> endCommand(c, true));
        Thread thread = new Thread(c);
        thread.setDaemon(true);
        thread.start();
    }

    public static void newLivraison(Product product, int qte) throws IOException {
        Delivery l = new Delivery(product, qte);
        if (money.get() >= l.getCost()) {
            if(onDeliveryCreated != null) onDeliveryCreated.accept(l.getPane());
            l.setOnSucceeded(workerStateEvent -> stocker(l));
            Thread thread = new Thread(l);
            thread.setDaemon(true);
            thread.start();
            money.set(money.get() - l.getCost());
        }
    }

    public static void endCommand(Command c, boolean complete) {
        if (complete) {
            stock.get(c.getProduit()).set(getStock(c.getProduit()).get() - c.getQte());
            money.set(money.get() + c.getGain());
        }
        endedCommands.add(c);
        if(onCommandEnded != null) onCommandEnded.accept(c.getButton());
        selectedCommand.set(Optional.empty());
    }

    public static SimpleIntegerProperty getStock(Product p) {
        return stock.get(p);
    }

    public static void stocker(Delivery l) {
        stock.get(l.getProduit()).set(getStock(l.getProduit()).get() + l.getQte());
        endedDeliveries.add(l);
        if(onDeliveryEnded != null) onDeliveryEnded.accept(l.getPane());
    }

    public static ObservableList<Stock> getStockObservableList() {
        return FXCollections.observableList(stock.entrySet().stream()
                .map(Stock::new)
                .collect(Collectors.toList()));
    }

    public static ObservableList<PieChart.Data> getProductStats(Stock s) {
        PieChart.Data data1 = new PieChart.Data("Commandés", 0);
        data1.pieValueProperty().bind(StatsService.getCommandesSum(s.getProduit()));
        PieChart.Data data2 = new PieChart.Data("Vendus", 0);
        data2.pieValueProperty().bind(StatsService.getVentesSum(s.getProduit()));
        PieChart.Data data3 = new PieChart.Data("Stock", 0);
        data3.pieValueProperty().bind(RetailService.getStock(s.getProduit()));
        return FXCollections.observableArrayList(data1, data2, data3);
    }

    public static ObservableValue<String> getCurrentGameTime() {
        return game.gameTimerProperty().map(time -> new SimpleDateFormat("mm:ss").format(time.longValue() * 1000));
    }

    public static ReadOnlyDoubleProperty getGameProgress() {
        return game.progressProperty();
    }

    public static void setOnDeliveryEnded(Consumer<AnchorPane> onDeliveryEnded) {
        RetailService.onDeliveryEnded = onDeliveryEnded;
    }

    public static void setOnCommandEnded(Consumer<Button> onCommandEnded) {
        RetailService.onCommandEnded = onCommandEnded;
    }
}
