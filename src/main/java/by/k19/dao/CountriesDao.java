package by.k19.dao;

import by.k19.model.Country;
import by.k19.model.Product;
import by.k19.model.User;
import by.k19.utils.HibernateSessionFactoryUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class CountriesDao implements ObjectDao<Country> {
    private static CountriesDao countriesDao;
    private CountriesDao() {}

    @SuppressWarnings("unchecked")
    public static <T> ObjectDao<T> getInstance() {
        if (countriesDao == null)
            countriesDao = new CountriesDao();
        return (ObjectDao<T>) countriesDao;
    }

    public Country findById(long id) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Country obj = session.get(Country.class, id);
        session.close();
        return obj;
    }

    public Country findByName(String name) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Country obj = (Country) session.createQuery("From Country WHERE name = ?")
                .setString(0, name)
                .uniqueResult();
        session.close();
        return obj;
    }

    public void save(Country country) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.save(country);
        tx1.commit();
        session.close();
    }

    public void update(Country country) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.update(country);
        tx1.commit();
        session.close();
    }

    public void delete(Country country) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.delete(country);
        tx1.commit();
        session.close();
    }

    public List<Country> findAll() {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        List<Country> list = (List<Country>)session.createQuery("From Country").list();
        session.close();
        return list;
    }
}
