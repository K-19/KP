package by.k19.beans;

import by.k19.dao.DaoFactory;
import by.k19.dao.ObjectDao;
import by.k19.model.Country;
import by.k19.model.ProductType;
import by.k19.model.UserType;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@ApplicationScoped
public class DatabaseBean implements Serializable {

    public <T> T findById(Class<T> clazz, long id) {
        ObjectDao<T> dao = DaoFactory.byClass(clazz);
        return dao.findById(id);
    }

    public <T> void save(T obj) {
        ObjectDao<T> dao = DaoFactory.byObject(obj);
        dao.save(obj);
    }

    public <T> void delete(T obj) {
        ObjectDao<T> dao = DaoFactory.byObject(obj);
        dao.delete(obj);
    }

    public <T> void update(T obj) {
        ObjectDao<T> dao = DaoFactory.byObject(obj);
        dao.update(obj);
    }

    public <T> List<T> findAll(Class<T> clazz) {
        ObjectDao<T> dao = DaoFactory.byClass(clazz);
        return dao.findAll();
    }

    public <T> ObjectDao<T> getDao(Class<T> clazz) {
        return DaoFactory.byClass(clazz);
    }

    public List<Country> getCountryTypes() {
        return DaoFactory.byClass(Country.class).findAll();
    }

    public List<ProductType> getProductTypes() {
        return DaoFactory.byClass(ProductType.class).findAll();
    }

    public UserType[] getUserTypes() {
        return UserType.values();
    }
}
