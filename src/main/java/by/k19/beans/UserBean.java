package by.k19.beans;


import by.k19.dao.CountriesDao;
import by.k19.dao.UserDao;
import by.k19.model.*;
import lombok.Data;

import javax.annotation.ManagedBean;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Data
@Named
@SessionScoped
public class UserBean implements Serializable {
    @Inject
    private DatabaseBean db;
    private User user;
    private String login;
    private String stringPassword;




    public void login() {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            if (validLoginForm()) {
                user = ((UserDao) db.getDao(User.class))
                        .findByLoginAndPassword(login, stringPassword.hashCode());
                if (user != null) {
                    String redirectUrl;
                    login = null;
                    stringPassword = null;
                    switch (user.getType()) {
                        case ADMIN:
                            redirectUrl = "admin/adminPanel.xhtml";
                            break;
                        case DIRECTOR:
                            redirectUrl = "director/directorPanel.xhtml";
                            break;
                        case MANAGER:
                            redirectUrl = "manager/managerPanel.xhtml";
                            break;
                        default:
                            redirectUrl = "joke.xhtml";
                            break;
                    }
                    CashDataBean.initialCash();
                    context.getExternalContext().redirect(redirectUrl);
                }
            }
        } catch (IOException e) {
            context.addMessage(null ,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Неправильный логин или пароль", null));
        }
    }
    public void setUser(User user) {
        this.user = user;
    }

    public void exitAccount() {
        user = null;
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            context.getExternalContext().redirect("../login.xhtml");
        } catch (IOException e) {
            context.addMessage(null ,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Непредвиденная ошибка выхода из аккаунта", null));
        }
    }

    private boolean validLoginForm() {
        return (stringPassword != null &&
                !stringPassword.isEmpty()
                && login != null && !login.isEmpty());
    }
}
