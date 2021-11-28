package by.k19.dao;

import by.k19.model.Outlet;
import by.k19.model.ProductRequest;
import by.k19.model.Purchase;
import by.k19.utils.HibernateSessionFactoryUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class PurchasesDao implements ObjectDao<Purchase> {
    private static PurchasesDao purchasesDao;
    private PurchasesDao() {}

    @SuppressWarnings("unchecked")
    public static <T> ObjectDao<T> getInstance() {
        if (purchasesDao == null)
            purchasesDao = new PurchasesDao();
        return (ObjectDao<T>) purchasesDao;
    }

    public Purchase findById(long id) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Purchase obj = session.get(Purchase.class, id);
        session.close();
        return obj;
    }

    public void save(Purchase purchase) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.save(purchase);
        tx1.commit();
        session.close();
    }

    public void update(Purchase purchase) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.update(purchase);
        tx1.commit();
        session.close();
    }

    public void delete(Purchase purchase) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.delete(purchase);
        tx1.commit();
        session.close();
    }

    public List<Purchase> findAll() {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        List<Purchase> list = (List<Purchase>)session.createQuery("From Purchase").list();
        session.close();
        return list;
    }

    public List<Purchase> findAllByOutletId(long outletId) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        List<Purchase> list = (List<Purchase>)session.createQuery("FROM Purchase WHERE outlet_id = " + outletId).list();
        session.close();
        return list;
    }
}
