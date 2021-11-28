package by.k19.beans;

import by.k19.dao.DaoFactory;
import by.k19.dao.UserDao;
import by.k19.model.Country;
import by.k19.model.Outlet;
import by.k19.model.User;
import by.k19.model.UserType;
import lombok.Data;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
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
    private boolean creatingNewUser;
    private boolean updatingUser;
    private boolean enableEditUserPanel;
    private boolean creatingNewOutlet;
    private boolean updatingOutlet;
    private boolean enableEditOutletPanel;
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

    public void enableCreateUserPanel() {
        manipulatedUser = new User();
        enableEditUserPanel = true;
        creatingNewUser = true;
    }
    public void enableUpdatePanel() {
        enableEditUserPanel = true;
        updatingUser = true;
    }

    public void enableCreateOutletPanel() {
        manipulatedOutlet = new Outlet();
        enableEditOutletPanel = true;
        creatingNewOutlet = true;
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
        if (validator.valid(manipulatedUser) && !userIsExist(manipulatedUser)) {
            if (manipulatedUser.getType() != UserType.MANAGER)
                manipulatedUser.setOutlet(null);
            db.save(manipulatedUser);
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
            closeEditUserPanel();
        }
    }

    public void createOutlet() {
        if (validator.valid(manipulatedOutlet)) {
            db.save(manipulatedOutlet);
            closeEditOutletPanel();
            CashDataBean.initialCash();
        }
    }

    public void updateOutlet() {
        if (validator.valid(manipulatedOutlet)) {
            db.update(manipulatedOutlet);
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
    }

    public void deleteOutlet(Outlet outlet) {
        for (User user : outlet.getUserList()) {
            user.setOutlet(null);
            db.update(user);
        }
        outlet.setUserList(null);
        db.delete(outlet);
    }

    private boolean userIsExist(User user) {
        return ((UserDao)db.getDao(User.class)).isExist(user);
    }


}
