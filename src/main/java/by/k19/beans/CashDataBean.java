package by.k19.beans;

import by.k19.model.*;
import lombok.Data;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class CashDataBean implements Serializable {
    private static DatabaseBean db = new DatabaseBean();

    public static List<Country> cashCountries;
    public static List<ProductType> cashProductTypes;
    public static List<Outlet> cashOutlets;

    public static void initialCash() {
        cashCountries = db.findAll(Country.class);
        cashProductTypes = db.findAll(ProductType.class);
        cashOutlets = db.findAll(Outlet.class);
    }

    public Product getAvailableProductsById(int id) {
        List<Product> products = new ArrayList<>();
        for (ProductProvider provider : db.getDao(ProductProvider.class).findAll()) {
            products.addAll(provider.getProductMap().keySet());
        }
        for (Product prod : products) {
            if (prod.getId() == id)
                return prod;
        }
        return null;
    }
}
