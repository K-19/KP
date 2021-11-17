package by.k19.dao;

import by.k19.model.Country;
import by.k19.model.Product;
import by.k19.model.User;
import by.k19.utils.HibernateSessionFactoryUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class UserDao implements ObjectDao<User>{
    private static UserDao userDao;
    private UserDao() {}

    @SuppressWarnings("unchecked")
    public static <T> ObjectDao<T> getInstance() {
        if (userDao == null)
            userDao = new UserDao();
        return (ObjectDao<T>) userDao;
    }

    public User findById(long id) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        User obj = session.get(User.class, id);
        session.close();
        return obj;
    }

    public User findByLoginAndPassword(String login, int password) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        User user = (User) session.createQuery("From User where login = ? and password = ?")
                .setString(0, login)
                .setInteger(1, password)
                .uniqueResult();
        session.close();
        return user;
    }

    public boolean isExist(User user) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        boolean exist = session.createQuery("From User where login = ?")
                .setString(0, user.getLogin())
                .uniqueResult() != null;
        session.close();
        return exist;
    }

    public void save(User user) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.save(user);
        tx1.commit();
        session.close();
    }

    public void update(User user) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.update(user);
        tx1.commit();
        session.close();
    }

    public void delete(User user) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.delete(user);
        tx1.commit();
        session.close();
    }

    public List<User> findAll() {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        List<User> list = (List<User>)session.createQuery("From User").list();
        session.close();
        return list;
    }
}