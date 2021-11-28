package by.k19.dao;

import by.k19.model.*;

import java.util.HashMap;
import java.util.Map;

public class DaoFactory {

    public static <T> ObjectDao<T> byClass(Class<T> clazz) {
        if (clazz.equals(User.class)) {
            return UserDao.getInstance();
        } else if (clazz.equals(Product.class)) {
            return ProductDao.getInstance();
        } else if (clazz.equals(ProductProvider.class)) {
            return ProvidersDao.getInstance();
        } else if (clazz.equals(Country.class)) {
            return CountriesDao.getInstance();
        } else if (clazz.equals(ProductType.class)) {
            return ProductTypesDao.getInstance();
        } else if (clazz.equals(Outlet.class)) {
            return OutletsDao.getInstance();
        } else if (clazz.equals(ProductRequest.class)) {
            return ProductRequestDao.getInstance();
        } else if (clazz.equals(Purchase.class)) {
            return PurchasesDao.getInstance();
        }
        else throw new IllegalArgumentException();
    }

    public static <T> ObjectDao<T> byObject(Object obj) {
        if (obj instanceof User) {
            return UserDao.getInstance();
        } else if (obj instanceof Product) {
            return ProductDao.getInstance();
        } else if (obj instanceof ProductProvider) {
            return ProvidersDao.getInstance();
        } else if (obj instanceof Country) {
            return CountriesDao.getInstance();
        } else if (obj instanceof ProductType) {
            return ProductTypesDao.getInstance();
        } else if (obj instanceof Outlet) {
            return OutletsDao.getInstance();
        } else if (obj instanceof ProductRequest) {
            return ProductRequestDao.getInstance();
        } else if (obj instanceof Purchase) {
            return PurchasesDao.getInstance();
        }
        else throw new IllegalArgumentException();
    }
}
