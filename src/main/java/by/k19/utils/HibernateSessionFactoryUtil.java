package by.k19.utils;

import by.k19.model.*;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class HibernateSessionFactoryUtil {
    private static SessionFactory sessionFactory;

    private HibernateSessionFactoryUtil() {}

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration().configure();
                configuration.addAnnotatedClass(User.class);
                configuration.addAnnotatedClass(Product.class);
                configuration.addAnnotatedClass(ProductProvider.class);
                configuration.addAnnotatedClass(Country.class);
                configuration.addAnnotatedClass(ProductType.class);
                configuration.addAnnotatedClass(Outlet.class);
                configuration.addAnnotatedClass(ProductRequest.class);
                configuration.addAnnotatedClass(Purchase.class);
                configuration.addAnnotatedClass(Sale.class);
                configuration.addAnnotatedClass(TechMessage.class);
                configuration.addAnnotatedClass(UserAction.class);
                StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
                sessionFactory = configuration.buildSessionFactory(builder.build());

            } catch (Exception e) {
                System.out.println("Исключение!" + e);
            }
        }
        return sessionFactory;
    }
}
