package by.k19.dao;

import by.k19.model.Country;
import by.k19.model.Product;
import by.k19.model.User;
import by.k19.utils.HibernateSessionFactoryUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class ProductDao implements ObjectDao<Product>{
    private static ProductDao productDao;
    private ProductDao() {}

    @SuppressWarnings("unchecked")
    public static <T> ObjectDao<T> getInstance() {
        if (productDao == null)
            productDao = new ProductDao();
        return (ObjectDao<T>) productDao;
    }

    public Product findById(long id) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Product obj = session.get(Product.class, id);
        session.close();
        return obj;
    }

    public void save(Product product) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.save(product);
        tx1.commit();
        session.close();
    }

    public void update(Product product) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.update(product);
        tx1.commit();
        session.close();
    }

    public void delete(Product product) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.delete(product);
        tx1.commit();
        session.close();
    }

    public List<Product> findAll() {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        List<Product> list = (List<Product>)session.createQuery("From Product").list();
        session.close();
        return list;
    }
}
