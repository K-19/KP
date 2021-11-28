package by.k19.model.converters;

import by.k19.beans.CashDataBean;
import by.k19.beans.DatabaseBean;
import by.k19.model.Outlet;
import by.k19.model.Product;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@ApplicationScoped
@FacesConverter(forClass= Product.class)
public class ProductConverter implements Converter {
    @Inject
    private DatabaseBean db;

    public String getAsString(FacesContext context, UIComponent component, Object modelValue) {
        if (modelValue == null) {
            return "";
        }
        if (modelValue instanceof Product) {
            return ((Product) modelValue).toString();
        } else {
            throw new ConverterException(new FacesMessage(modelValue + " is not a valid Outlet"));
        }
    }

    public Object getAsObject(FacesContext context, UIComponent component, String submittedValue) {
        if (submittedValue == null || submittedValue.isEmpty()) {
            return null;
        }
        String fullStr = "";
        try {
            fullStr = (String) submittedValue;
            fullStr = fullStr.substring(fullStr.indexOf("#") + 1);
            fullStr = fullStr.substring(0, fullStr.indexOf(" "));
            int id = Integer.parseInt(fullStr);
            Product product = new CashDataBean().getAvailableProductsById(id);
            if (product == null)
                throw new Exception();
            return product;
        } catch (NumberFormatException e) {
            throw new ConverterException(new FacesMessage("Шото не то с цифрой " + fullStr), e);
        } catch (Exception e) {
            throw new ConverterException(new FacesMessage(submittedValue + " is not a valid Product ID"), e);
        }
    }
}
