package dwn.jfx.retail.models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Map;

public class Stock {

    private final Product product;
    private final StringProperty name;
    private final IntegerProperty qte;
    private final StringProperty prix;

    public Stock(Map.Entry<Product, SimpleIntegerProperty> entry) {
        product = entry.getKey();
        name = new SimpleStringProperty(product.getName());
        prix = new SimpleStringProperty(String.format("%.2f €", product.getPrice()));
        qte = entry.getValue();
    }

    public Product getProduit() {
        return product;
    }

    public StringProperty getName() {
        return name;
    }

    public IntegerProperty getQte() {
        return qte;
    }

    public StringProperty getPrix() {
        return prix;
    }
}
