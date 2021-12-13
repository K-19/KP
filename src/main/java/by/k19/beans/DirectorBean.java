package by.k19.beans;

import by.k19.dao.DaoFactory;
import by.k19.model.*;
import lombok.Data;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.*;

@Data
@Named
@SessionScoped
public class DirectorBean implements Serializable {
    @Inject
    private DatabaseBean db;
    @Inject
    private Validator validator;
    @Inject
    private UserBean currentUser;
    @Inject
    private ManagerBean managerBean;
    @Inject
    private ErrorBean errorBean;
    private String newAnnounce;
    private Outlet statisticOutlet;
    private boolean enableEditProviderPanel;
    private boolean creatingProvider;
    private boolean updatingProvider;
    private boolean enableEditProductPanel;
    private boolean addingNewProduct;
    private boolean updatingProduct;
    private boolean enableTechSupport;
    private boolean enableStatistics;
    private boolean enableProvidersPanel;
    private boolean enableAnnounces;
    private ProductProvider currentProvider = new ProductProvider();
    private Product currentProduct = new Product();
    private Integer amountProduct;

    public List<ProductProvider> getAllProviders() {
        return db.findAll(ProductProvider.class);
    }

    public void closeAllPaged() {
        enableEditProviderPanel = false;
        creatingProvider = false;
        updatingProvider = false;
        enableEditProductPanel = false;
        addingNewProduct = false;
        updatingProduct = false;
        enableTechSupport = false;
        enableProvidersPanel = false;
        enableAnnounces = false;
        enableStatistics = false;
    }

    public void showProviders() {
        closeAllPaged();
        enableProvidersPanel = true;
    }

    public void showTechSupport() {
        closeAllPaged();
        enableTechSupport = true;
    }

    public void showAnnounces() {
        closeAllPaged();
        enableAnnounces = true;
    }

    public void showStatistics() {
        closeAllPaged();
        enableStatistics = true;
    }

    public ProductProvider getCurrentProvider() {
        if (currentProvider == null)
            currentProvider = new ProductProvider();
        return currentProvider;
    }

    public void startCreateProvider() {
        currentProvider = new ProductProvider();
        enableEditProviderPanel = true;
        creatingProvider = true;
        CashDataBean.initialCash();
    }

    public void createProvider() {
        if (validator.valid(currentProvider)) {
            db.save(currentProvider);
            db.save(new UserAction(currentUser.getUser(), "Создан новый поставщик " + currentProvider));
            disableAddProductPanel();
            disableCreatePanel();
        }
        else {
            errorBean.setMessage("Ошибка! Проверьте правильность ввода данных");
        }
    }

    public void startUpdateProvider(ProductProvider provider) {
        currentProvider = provider;
        enableEditProviderPanel = true;
        updatingProvider = true;
        CashDataBean.initialCash();
    }


    public void enableUpdateProviderPanel() {
        updatingProvider = true;
    }

    public void updateProvider() {
        if (validator.valid(currentProvider)) {
            db.update(currentProvider);
            db.save(new UserAction(currentUser.getUser(), "Обновлён поставщик " + currentProvider));
            disableAddProductPanel();
            disableCreatePanel();
        }
    }

    public void createProduct() {
        if (currentProvider.getProductMap() == null)
            currentProvider.setProductMap(new HashMap<>());
        currentProduct.setProvider(currentProvider);
//        if (updatingProvider) {
//            db.save(currentProduct);
//            currentProduct = db.findById(Product.class, currentProduct.getId());
//        }
        if (validator.valid(currentProduct)) {
            currentProvider.getProductMap().put(currentProduct, amountProduct);
            disableAddProductPanel();
        }
    }

    public void updateProduct() {
        if (validator.valid(currentProduct)) {
            Map<Product, Integer> newMap = new HashMap<>();
            for (Product product : currentProvider.getProductMap().keySet()) {
                if (product.getId() == currentProduct.getId())
                    newMap.put(currentProduct, amountProduct);
                else newMap.put(product, currentProvider.getProductMap().get(product));
            }
            currentProvider.setProductMap(newMap);
            if (updatingProduct)
                db.update(currentProvider);
            db.save(new UserAction(currentUser.getUser(), "Обновлён поставщик " + currentProvider));
            disableAddProductPanel();
        }
    }



    public void startUpdateProduct(Product product, Integer amount) {
        currentProduct = product;
        amountProduct = amount;
        enableEditProductPanel = true;
        updatingProduct = true;
        CashDataBean.initialCash();
    }

    public void deleteProvider(ProductProvider provider) {
        db.delete(provider);
        db.save(new UserAction(currentUser.getUser(), "Удалён поставщик " + provider));
    }

    public void deleteProduct(Product product) {
        if (updatingProvider) {
            db.delete(product);
            db.save(new UserAction(currentUser.getUser(), "Удалён продукт " + product));
        }
        currentProvider.getProductMap().remove(product);
    }

    public void disableCreatePanel() {
        currentProvider = null;
        enableEditProviderPanel = false;
        creatingProvider = false;
        updatingProvider = false;
    }

    public void enableAddProductPanel() {
        currentProduct = new Product();
        enableEditProductPanel = true;
        addingNewProduct = true;
        CashDataBean.initialCash();
    }

    public void disableAddProductPanel() {
        currentProduct = null;
        amountProduct = 0;
        enableEditProductPanel = false;
        addingNewProduct = false;
        updatingProduct = false;
    }

    public void disableUpdateProviderPanel() {
        currentProvider = null;
        currentProduct = null;
        enableEditProductPanel = false;
        enableEditProviderPanel = false;
        updatingProvider = false;
        addingNewProduct = false;
        updatingProduct = false;
    }

    public List<Product> getCurrentProviderProducts() {
        if (currentProvider != null && currentProvider.getProductMap() != null)
            return new ArrayList<>(currentProvider.getProductMap().keySet());
        else return new ArrayList<>();
    }

    public List<UserAction> getAdminsActions() {
        List<UserAction> resultList = new ArrayList<>();
        for (UserAction action : db.findAll(UserAction.class)) {
            if (action.getUser().getType() == UserType.ADMIN)
                resultList.add(action);
        }
        resultList.sort(new Comparator<UserAction>() {
            @Override
            public int compare(UserAction o1, UserAction o2) {
                return (int) (o2.getTime().getTime() - o1.getTime().getTime());
            }
        });
        return resultList;
    }

    public void sendNewAnnounce() {
        if (validator.valid(newAnnounce)) {
            UserAction action = new UserAction(currentUser.getUser(), newAnnounce);
            db.save(action);
        } else {
            errorBean.setMessage("Ошибка! Нельзя отправить пустое объявление");
        }
    }
}
