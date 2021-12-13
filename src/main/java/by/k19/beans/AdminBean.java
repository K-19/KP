package by.k19.beans;

import by.k19.dao.TechMessagesDao;
import by.k19.dao.UserDao;
import by.k19.model.*;
import lombok.Data;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Named
@SessionScoped
public class AdminBean implements Serializable {
    @Inject
    private DatabaseBean db;
    @Inject
    private Validator validator;
    @Inject
    private UserBean currentAdmin;
    @Inject
    private ErrorBean errorBean;
    private String currentTechSupportAnswer;
    private boolean creatingNewUser;
    private boolean updatingUser;
    private boolean enableEditUserPanel;
    private boolean creatingNewOutlet;
    private boolean updatingOutlet;
    private boolean enableEditOutletPanel;
    private boolean enableTechSupport;
    private boolean enableProvidersPanel;
    private User manipulatedUser = new User();
    private Outlet manipulatedOutlet = new Outlet();
    private String stringCreatedUserPassword;

    public List<User> getAllUsers() {
        return db.findAll(User.class);
    }

    public List<Outlet> getAllOutlets() {
        return db.findAll(Outlet.class);
    }

    public String getOutletHrs(Outlet outlet) {
        if (outlet == null || outlet.getUserList() == null)
            return "Отсутствуют";
        return outlet.getUserList().stream().map(User::getSurname).collect(Collectors.joining(", "));
    }

    public void showProviders() {
        closeAllPaged();
        enableProvidersPanel = true;
    }

    public void showTechSupport() {
        closeAllPaged();
        enableTechSupport = true;
    }

    public void closeAllPaged() {
        creatingNewUser = false;
        updatingUser = false;
        enableEditUserPanel = false;
        creatingNewOutlet = false;
        updatingOutlet = false;
        enableEditOutletPanel = false;
        enableTechSupport = false;
        enableProvidersPanel = false;
    }

    public void enableCreateUserPanel() {
        manipulatedUser = new User();
        enableEditUserPanel = true;
        creatingNewUser = true;
        CashDataBean.initialCash();
    }
    public void enableUpdatePanel() {
        enableEditUserPanel = true;
        updatingUser = true;
        CashDataBean.initialCash();
    }

    public void enableCreateOutletPanel() {
        manipulatedOutlet = new Outlet();
        enableEditOutletPanel = true;
        creatingNewOutlet = true;
        CashDataBean.initialCash();
    }

    public void closeEditUserPanel() {
        manipulatedUser = null;
        updatingUser = false;
        creatingNewUser = false;
        enableEditUserPanel = false;
    }

    public void closeEditOutletPanel() {
        manipulatedOutlet = null;
        updatingOutlet = false;
        creatingNewOutlet = false;
        enableEditOutletPanel = false;
    }

    public void createUser() {
        manipulatedUser.setPassword(stringCreatedUserPassword.hashCode());
        if (manipulatedUser.getType() == UserType.MANAGER && manipulatedUser.getOutlet() == null) {
            errorBean.setMessage("Ошибка! Менеджеру должна быть указана торговая точка");
        }
        else if (validator.valid(manipulatedUser) && !userIsExist(manipulatedUser)) {
            if (manipulatedUser.getType() != UserType.MANAGER)
                manipulatedUser.setOutlet(null);
            db.save(manipulatedUser);
            db.save(new UserAction(currentAdmin.getUser(), "Создан новый пользователь " + manipulatedUser));
            closeEditUserPanel();
        }
    }

    public void updateUser() {
        if (validator.valid(manipulatedUser)) {
            if (manipulatedUser.getType() != UserType.MANAGER)
                manipulatedUser.setOutlet(null);
            db.update(manipulatedUser);
            if (manipulatedUser.getType() == UserType.MANAGER)
                manipulatedUser.getOutlet().getUserList().add(manipulatedUser);
            db.save(new UserAction(currentAdmin.getUser(), "Обновлён пользователь " + manipulatedUser));
            closeEditUserPanel();
        }
    }

    public void createOutlet() {
        if (validator.valid(manipulatedOutlet)) {
            db.save(manipulatedOutlet);
            db.save(new UserAction(currentAdmin.getUser(), "Создана новая торговая точка " + manipulatedOutlet));
            closeEditOutletPanel();
            CashDataBean.initialCash();
        }
    }

    public void updateOutlet() {
        if (validator.valid(manipulatedOutlet)) {
            db.update(manipulatedOutlet);
            db.save(new UserAction(currentAdmin.getUser(), "Обновлена торговая точка " + manipulatedOutlet));
            closeEditOutletPanel();
        }
    }

    public void startUpdateUser(User user) {
        manipulatedUser = user;
        enableEditUserPanel = true;
        enableUpdatePanel();
    }

    public void startUpdateOutlet(Outlet outlet) {
        manipulatedOutlet = outlet;
        enableEditOutletPanel = true;
        updatingOutlet = true;
    }

    public void deleteUser(User user) {
        db.delete(user);
        db.save(new UserAction(currentAdmin.getUser(), "Удалён пользователь " + user));
    }

    public void deleteOutlet(Outlet outlet) {
        for (User user : outlet.getUserList()) {
            user.setOutlet(null);
            db.update(user);
        }
        outlet.setUserList(null);
        db.delete(outlet);
        db.save(new UserAction(currentAdmin.getUser(), "Удалена торговая точка " + outlet));
    }

    private boolean userIsExist(User user) {
        return ((UserDao)db.getDao(User.class)).isExist(user);
    }

    public List<TechMessage> getSupportHistory() {
        return ((TechMessagesDao) db.getDao(TechMessage.class)).findAllClosed();
    }

    public List<TechMessage> getOpenedTechMessages() {
        return ((TechMessagesDao) db.getDao(TechMessage.class)).findAllOpened();
    }

    public void answerMessage(TechMessage message) {
        if (validator.valid(currentTechSupportAnswer)) {
            message.setAnswer(currentTechSupportAnswer);
            currentTechSupportAnswer = "";
            message.setAdmin(currentAdmin.getUser());
            message.setAnswerDate(new Date());
            db.update(message);
            db.save(new UserAction(currentAdmin.getUser(), "Ответ на вопрос пользователя " + message.getUser().getFio() + ": " + message.getQuestion() + " -> " + message.getAnswer()));
        }
        else {
            errorBean.setMessage("Ошибка! Невозможно отправить пустой сообщение");
        }
    }
}
