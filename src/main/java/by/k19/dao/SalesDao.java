package by.k19.dao;

import by.k19.model.Product;
import by.k19.model.Sale;
import by.k19.utils.HibernateSessionFactoryUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class SalesDao implements ObjectDao<Sale>{
    private static SalesDao salesDao;
    private SalesDao() {}

    @SuppressWarnings("unchecked")
    public static <T> ObjectDao<T> getInstance() {
        if (salesDao == null)
            salesDao = new SalesDao();
        return (ObjectDao<T>) salesDao;
    }

    public Sale findById(long id) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Sale obj = session.get(Sale.class, id);
        session.close();
        return obj;
    }

    public List<Sale> findFromOutletById(long outletId) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        List<Sale> list = (List<Sale>)session.createQuery("From Sale WHERE outlet_id = " + outletId).list();
        session.close();
        return list;
    }

    public void save(Sale sale) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.save(sale);
        tx1.commit();
        session.close();
    }

    public void update(Sale sale) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.update(sale);
        tx1.commit();
        session.close();
    }

    public void delete(Sale sale) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.delete(sale);
        tx1.commit();
        session.close();
    }

    public List<Sale> findAll() {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        List<Sale> list = (List<Sale>)session.createQuery("From Sale").list();
        session.close();
        return list;
    }
}
