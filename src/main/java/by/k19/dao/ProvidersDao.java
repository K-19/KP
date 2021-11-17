package by.k19.dao;

import by.k19.model.Country;
import by.k19.model.Product;
import by.k19.model.ProductProvider;
import by.k19.utils.HibernateSessionFactoryUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class ProvidersDao implements ObjectDao<ProductProvider> {
    private static ProvidersDao providersDao;
    private ProvidersDao() {}

    @SuppressWarnings("unchecked")
    public static <T> ObjectDao<T> getInstance() {
        if (providersDao == null)
            providersDao = new ProvidersDao();
        return (ObjectDao<T>) providersDao;
    }

    public ProductProvider findById(long id) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        ProductProvider obj = session.get(ProductProvider.class, id);
        session.close();
        return obj;
    }

    public void save(ProductProvider provider) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.save(provider);
        tx1.commit();
        session.close();
    }

    public void update(ProductProvider provider) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.update(provider);
        tx1.commit();
        session.close();
    }

    public void delete(ProductProvider provider) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.delete(provider);
        tx1.commit();
        session.close();
    }

    public List<ProductProvider> findAll() {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        List<ProductProvider> list = (List<ProductProvider>)session.createQuery("From ProductProvider").list();
        for (ProductProvider provider : list) {
            int hash = provider.getProductList().hashCode();
        }
        session.close();
        return list;
    }
}
