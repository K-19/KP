package by.k19.dao;

import by.k19.model.TechMessage;
import by.k19.model.UserAction;
import by.k19.utils.HibernateSessionFactoryUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class ActionDao implements ObjectDao<UserAction> {
    private static ActionDao actionDao;
    private ActionDao() {}

    @SuppressWarnings("unchecked")
    public static <T> ObjectDao<T> getInstance() {
        if (actionDao == null)
            actionDao = new ActionDao();
        return (ObjectDao<T>) actionDao;
    }

    public UserAction findById(long id) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        UserAction obj = session.get(UserAction.class, id);
        session.close();
        return obj;
    }

    public void save(UserAction userAction) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.save(userAction);
        tx1.commit();
        session.close();
    }

    public void update(UserAction userAction) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.update(userAction);
        tx1.commit();
        session.close();
    }

    public void delete(UserAction userAction) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.delete(userAction);
        tx1.commit();
        session.close();
    }

    @SuppressWarnings("unchecked")
    public List<UserAction> findAll() {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        List<UserAction> list = (List<UserAction>)session.createQuery("From UserAction").list();
        session.close();
        return list;
    }
}
