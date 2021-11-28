package by.k19.dao;

import by.k19.model.Outlet;
import by.k19.model.Product;
import by.k19.utils.HibernateSessionFactoryUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class OutletsDao implements ObjectDao<Outlet>{
    private static OutletsDao outletsDao;
    private OutletsDao() {}

    @SuppressWarnings("unchecked")
    public static <T> ObjectDao<T> getInstance() {
        if (outletsDao == null)
            outletsDao = new OutletsDao();
        return (ObjectDao<T>) outletsDao;
    }

    public Outlet findById(long id) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Outlet obj = session.get(Outlet.class, id);
        session.close();
        return obj;
    }

    public void save(Outlet outlet) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.save(outlet);
        tx1.commit();
        session.close();
    }

    public void update(Outlet outlet) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.update(outlet);
        tx1.commit();
        session.close();
    }

    public void delete(Outlet outlet) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.delete(outlet);
        tx1.commit();
        session.close();
    }

    public List<Outlet> findAll() {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        List<Outlet> list = (List<Outlet>)session.createQuery("From Outlet").list();
        session.close();
        return list;
    }
}
