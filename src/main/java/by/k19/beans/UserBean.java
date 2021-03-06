package by.k19.beans;


import by.k19.dao.CountriesDao;
import by.k19.dao.TechMessagesDao;
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
import java.util.Date;
import java.util.List;

@Data
@Named
@SessionScoped
public class UserBean implements Serializable {
    @Inject
    private DatabaseBean db;
    @Inject
    private Validator validator;
    @Inject
    private ErrorBean errorBean;

    private User user;
    private String login;
    private String stringPassword;
    private String techSupportQuestion;

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
                else {
                    errorBean.setMessage("????????????! ?????????? ?????? ???????????? ??????????????????????");
                }
            }
            else {
                errorBean.setMessage("????????????! ???????? ???????????? ???????? ?????????????????????? ??????????????????");
            }
        } catch (IOException e) {
            context.addMessage(null ,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "???????????????????????? ?????????? ?????? ????????????", null));
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
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "???????????????????????????? ???????????? ???????????? ???? ????????????????", null));
        }
    }

    private boolean validLoginForm() {
        return (stringPassword != null &&
                !stringPassword.isEmpty()
                && login != null && !login.isEmpty());
    }

    public List<TechMessage> getHistorySupportMessage() {
        return ((TechMessagesDao)db.getDao(TechMessage.class)).findAllByUserId(user.getId());
    }

    public void sendTechMessage() {
        if (validator.valid(techSupportQuestion)) {
            TechMessage newMessage = new TechMessage();
            newMessage.setUser(user);
            newMessage.setQuestionDate(new Date());
            newMessage.setQuestion(techSupportQuestion);
            db.save(newMessage);
        }
        else {
            errorBean.setMessage("????????????! ???????????? ?????????????????? ???????????? ??????????????????");
        }
    }
}
