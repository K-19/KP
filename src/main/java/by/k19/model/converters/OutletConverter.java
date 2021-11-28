package by.k19.model.converters;

import by.k19.beans.AdminBean;
import by.k19.beans.CashDataBean;
import by.k19.beans.UserBean;
import by.k19.model.Country;
import by.k19.model.Outlet;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import javax.inject.Named;

@Named
@ApplicationScoped
@FacesConverter(forClass=Outlet.class)
public class OutletConverter implements Converter {
    public String getAsString(FacesContext context, UIComponent component, Object modelValue) {
        if (modelValue == null) {
            return "";
        }
        if (modelValue instanceof Outlet) {
            return ((Outlet) modelValue).toString();
        } else {
            throw new ConverterException(new FacesMessage(modelValue + " is not a valid Outlet"));
        }
    }

    public Object getAsObject(FacesContext context, UIComponent component, String submittedValue) {
        if (submittedValue == null || submittedValue.isEmpty()) {
            return null;
        }
        try {
            for (Outlet outlet : CashDataBean.cashOutlets) {
                if (outlet.toString().equals(submittedValue))
                    return outlet;
            }
            throw new Exception();
        } catch (Exception e) {
            throw new ConverterException(new FacesMessage(submittedValue + " is not a valid Outlet ID"), e);
        }
    }
}
