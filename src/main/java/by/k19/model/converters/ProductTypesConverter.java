package by.k19.model.converters;

import by.k19.beans.DatabaseBean;
import by.k19.beans.UserBean;
import by.k19.dao.CountriesDao;
import by.k19.dao.ProductTypesDao;
import by.k19.model.Country;
import by.k19.model.ProductType;

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
@FacesConverter(forClass = ProductType.class)
public class ProductTypesConverter implements Converter {
    @Inject
    private DatabaseBean db;

    public String getAsString(FacesContext context, UIComponent component, Object modelValue) {
        if (modelValue == null) {
            return "";
        }
        if (modelValue instanceof ProductType) {
            return ((ProductType) modelValue).getName();
        } else {
            throw new ConverterException(new FacesMessage(modelValue + " is not a valid ProductType"));
        }
    }

    public Object getAsObject(FacesContext context, UIComponent component, String submittedValue) {
        if (submittedValue == null || submittedValue.isEmpty()) {
            return null;
        }
        try {
            for (ProductType productType : UserBean.cashProductTypes) {
                if (productType.getName().equals(submittedValue))
                    return productType;
            }
            throw new Exception();
        } catch (Exception e) {
            throw new ConverterException(new FacesMessage(submittedValue + " is not a valid ProductType ID"), e);
        }
    }
}
