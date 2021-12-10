package by.k19.beans;

import by.k19.model.*;
import lombok.Data;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

@Data
@Named
@SessionScoped
public class Validator implements Serializable {

    public boolean valid(Object obj) {
        if (obj instanceof String) {
            return validField((String) obj);
        }
        else if (obj instanceof User) {
            User user = (User)obj;
            return validFields(user.getLogin(), user.getPassword(), user.getName(), user.getSurname());
        }
        else if (obj instanceof Product) {
            Product prod = (Product)obj;
            return validFields(prod.getName(), prod.getType(), prod.getPurchasePrice(), prod.getSellingPrice(),
                    prod.getManufactureDate(), prod.getManufacturerCountry());
        }
        else if (obj instanceof ProductProvider) {
            ProductProvider provider = (ProductProvider)obj;
            return validFields(provider.getName(), provider.getAddress(), provider.getCountry(), provider.getDeliveryDays());
        }
        else if (obj instanceof Outlet) {
            Outlet outlet = (Outlet)obj;
            return validFields(outlet.getName(), outlet.getAddress(), outlet.getCountry());
        }
        else if (obj instanceof Purchase) {
            Purchase purchase = (Purchase)obj;
            if (purchase.getProducts() == null || purchase.getProducts().size() == 0)
                return false;
            for (Product product : purchase.getProducts().keySet()) {
                if (!valid(product))
                    return false;
            }
            return true;
        }
        else if (obj instanceof Sale) {
            Sale sale = (Sale) obj;
            if (!validFields(sale.getProduct(), sale.getOutlet()))
                return false;
            return sale.getPercent() > 0 && sale.getPercent() < 100;
        }
        return false;
    }

    private boolean validFields(Object...objects) {
        for (Object obj : objects)
            if (!validField(obj))
                return false;
        return true;
    }

    private boolean validField(Object obj) {
        if (obj == null)
            return false;
        if (obj instanceof String && ((String) obj).isEmpty())
            return false;
        if (obj instanceof Integer && ((Integer) obj) == 0)
            return false;
        if (obj instanceof Double && ((Double) obj).compareTo(0d) == 0)
            return false;
        return true;
    }
}
