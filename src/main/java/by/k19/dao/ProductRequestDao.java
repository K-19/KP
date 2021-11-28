package by.k19.dao;

import by.k19.model.Outlet;
import by.k19.model.ProductRequest;
import by.k19.utils.HibernateSessionFactoryUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class ProductRequestDao implements ObjectDao<ProductRequest>{
    private static ProductRequestDao productRequestDao;
    private ProductRequestDao() {}

    @SuppressWarnings("unchecked")
    public static <T> ObjectDao<T> getInstance() {
        if (productRequestDao == null)
            productRequestDao = new ProductRequestDao();
        return (ObjectDao<T>) productRequestDao;
    }

    public ProductRequest findById(long id) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        ProductRequest obj = session.get(ProductRequest.class, id);
        session.close();
        return obj;
    }

    public void save(ProductRequest productRequest) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.save(productRequest);
        tx1.commit();
        session.close();
    }

    public void update(ProductRequest productRequest) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.update(productRequest);
        tx1.commit();
        session.close();
    }

    public void delete(ProductRequest productRequest) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.delete(productRequest);
        tx1.commit();
        session.close();
    }

    public List<ProductRequest> findAll() {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        List<ProductRequest> list = (List<ProductRequest>)session.createQuery("From ProductRequest").list();
        session.close();
        return list;
    }

    public List<ProductRequest> findAllByOutletId(long outletId) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        List<ProductRequest> list = (List<ProductRequest>)session.createQuery("FROM ProductRequest WHERE outlet_id = " + outletId + " ORDER BY accepttime, starttime").list();
        session.close();
        return list;
    }
}
