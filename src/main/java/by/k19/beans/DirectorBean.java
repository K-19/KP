package by.k19.beans;

import by.k19.dao.DaoFactory;
import by.k19.model.*;
import lombok.Data;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
    private boolean enableEditProviderPanel;
    private boolean creatingProvider;
    private boolean updatingProvider;
    private boolean enableEditProductPanel;
    private boolean addingNewProduct;
    private boolean updatingProduct;
    private ProductProvider currentProvider = new ProductProvider();
    private Product currentProduct = new Product();

    public List<Country> getCountryTypes() {
        return DaoFactory.byClass(Country.class).findAll();
    }

    public List<ProductType> getProductTypes() {
        return DaoFactory.byClass(ProductType.class).findAll();
    }

    public List<ProductProvider> getAllProviders() {
        return db.findAll(ProductProvider.class);
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
    }

    public void createProvider() {
        if (validator.valid(currentProvider)) {
            db.save(currentProvider);
            disableAddProductPanel();
            disableCreatePanel();
        }
    }

    public void startUpdateProvider(ProductProvider provider) {
        currentProvider = provider;
        enableEditProviderPanel = true;
        updatingProvider = true;
    }


    public void enableUpdateProviderPanel() {
        updatingProvider = true;
    }

    public void updateProvider() {
        if (validator.valid(currentProvider)) {
            db.update(currentProvider);
            disableAddProductPanel();
            disableCreatePanel();
        }
    }

    public void createProduct() {
        if (currentProvider.getProductList() == null)
            currentProvider.setProductList(new ArrayList<>());
        currentProduct.setProvider(currentProvider);
        if (updatingProvider) {
            db.save(currentProduct);
            currentProduct = db.findById(Product.class, currentProduct.getId());
        }
        if (validator.valid(currentProduct)) {
            currentProvider.getProductList().add(currentProduct);
            disableAddProductPanel();
        }
    }

    public void updateProduct() {
        if (validator.valid(currentProduct)) {
            if (creatingProvider) {
                List<Product> newList = new ArrayList<>();
                for (Product product : currentProvider.getProductList()) {
                    if (product.getId() == currentProduct.getId())
                        newList.add(currentProduct);
                    else newList.add(product);
                }
                currentProvider.setProductList(newList);
            }
            else
                db.update(currentProduct);
            disableAddProductPanel();
        }
    }



    public void startUpdateProduct(Product product) {
        currentProduct = product;
        enableEditProductPanel = true;
        updatingProduct = true;
    }

    public void deleteProvider(ProductProvider provider) {
        db.delete(provider);
    }

    public void deleteProduct(Product product) {
        if (updatingProvider) {
            db.delete(product);
        }
        currentProvider.getProductList().remove(product);
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
    }

    public void disableAddProductPanel() {
        currentProduct = null;
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
}
