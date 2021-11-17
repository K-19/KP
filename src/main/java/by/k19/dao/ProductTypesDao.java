package by.k19.dao;

import by.k19.model.Country;
import by.k19.model.ProductType;
import by.k19.model.User;
import by.k19.utils.HibernateSessionFactoryUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class ProductTypesDao implements ObjectDao<ProductType> {
    private static ProductTypesDao productTypesDao;
    private ProductTypesDao() {}

    @SuppressWarnings("unchecked")
    public static <T> ObjectDao<T> getInstance() {
        if (productTypesDao == null)
            productTypesDao = new ProductTypesDao();
        return (ObjectDao<T>) productTypesDao;
    }

    public ProductType findById(long id) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        ProductType obj = session.get(ProductType.class, id);
        session.close();
        return obj;
    }

    public ProductType findByName(String name) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        ProductType obj = (ProductType) session.createQuery("From ProductType WHERE name = ?")
                .setString(0, name)
                .uniqueResult();
        session.close();
        return obj;
    }

    public void save(ProductType productType) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.save(productType);
        tx1.commit();
        session.close();
    }

    public void update(ProductType productType) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.update(productType);
        tx1.commit();
        session.close();
    }

    public void delete(ProductType productType) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.delete(productType);
        tx1.commit();
        session.close();
    }

    public List<ProductType> findAll() {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        List<ProductType> list = (List<ProductType>)session.createQuery("From ProductType").list();
        session.close();
        return list;
    }
}
