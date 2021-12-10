package by.k19.dao;

import by.k19.model.Product;
import by.k19.model.TechMessage;
import by.k19.utils.HibernateSessionFactoryUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class TechMessagesDao implements ObjectDao<TechMessage> {
    private static TechMessagesDao techMessagesDao;
    private TechMessagesDao() {}

    @SuppressWarnings("unchecked")
    public static <T> ObjectDao<T> getInstance() {
        if (techMessagesDao == null)
            techMessagesDao = new TechMessagesDao();
        return (ObjectDao<T>) techMessagesDao;
    }

    public TechMessage findById(long id) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        TechMessage obj = session.get(TechMessage.class, id);
        session.close();
        return obj;
    }

    public void save(TechMessage techMessage) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.save(techMessage);
        tx1.commit();
        session.close();
    }

    public void update(TechMessage techMessage) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.update(techMessage);
        tx1.commit();
        session.close();
    }

    public void delete(TechMessage techMessage) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.delete(techMessage);
        tx1.commit();
        session.close();
    }

    public List<TechMessage> findAll() {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        List<TechMessage> list = (List<TechMessage>)session.createQuery("From TechMessage").list();
        session.close();
        return list;
    }

    public List<TechMessage> findAllByUserId(long user_id) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        List<TechMessage> list = (List<TechMessage>)session.createQuery("From TechMessage WHERE user_id = " + user_id + " ORDER BY questionDate DESC").list();
        session.close();
        return list;
    }
}
