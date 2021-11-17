package by.k19.beans;

import by.k19.dao.UserDao;
import by.k19.model.User;
import by.k19.model.UserType;
import lombok.Data;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

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
    private User createdUser = new User();
    private User updatedUser = new User();
    private String stringCreatedUserPassword;

    public List<User> getAllUsers() {
        return db.findAll(User.class);
    }

    public UserType[] getUserTypes() {
        return UserType.values();
    }

    public void enableCreatePanel() {
        creatingNewUser = true;
    }
    public void enableUpdatePanel() {
        updatingUser = true;
    }

    public void disableCreatePanel() {
        createdUser = null;
        creatingNewUser = false;
    }

    public void disableUpdatePanel() {
        updatedUser = null;
        updatingUser = false;
    }

    public void createUser() {
        createdUser.setPassword(stringCreatedUserPassword.hashCode());
        if (validator.valid(createdUser) && !userIsExist(createdUser)) {
            db.save(createdUser);
            disableCreatePanel();
        }
    }

    public void updateUser() {
        if (validator.valid(updatedUser)) {
            db.update(updatedUser);
            disableUpdatePanel();
        }
    }

    public void startUpdateUser(User user) {
        updatedUser = user;
        enableUpdatePanel();
    }

    public void deleteUser(User user) {
        db.delete(user);
    }

    private boolean userIsExist(User user) {
        return ((UserDao)db.getDao(User.class)).isExist(user);
    }


}
