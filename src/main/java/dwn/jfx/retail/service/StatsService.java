package dwn.jfx.retail.service;

import dwn.jfx.retail.models.Command;
import dwn.jfx.retail.models.Delivery;
import dwn.jfx.retail.models.Product;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class StatsService {
    private final static Map<Product, DoubleProperty> commandesSum = Arrays.stream(Product.values())
            .collect(Collectors.toMap(value -> value, val -> new SimpleDoubleProperty(0)));
    private final static Map<Product, DoubleProperty> ventesSum = Arrays.stream(Product.values())
            .collect(Collectors.toMap(value -> value, val -> new SimpleDoubleProperty(0)));

    public static void bind(ObservableList<Delivery> endedLivs, ObservableList<Command> endedCommands) {
        endedLivs.addListener((ListChangeListener<? super Delivery>) change ->
                change.getList().stream()
                        .collect(Collectors.toMap(Delivery::getProduit, Delivery::getQte, Integer::sum))
                        .forEach((produit, integer) -> commandesSum.get(produit).set(integer)));

        endedCommands.addListener((ListChangeListener<? super Command>) change ->
                change.getList().stream()
                        .collect(Collectors.toMap(Command::getProduit, Command::getQte, Integer::sum))
                        .forEach((produit, integer) -> ventesSum.get(produit).set(integer)));

    }

    public static DoubleProperty getCommandesSum(Product p) {
        return commandesSum.get(p);
    }

    public static DoubleProperty getVentesSum(Product p) {
        return ventesSum.get(p);
    }
}
