package by.k19.beans;

import by.k19.dao.ProductRequestDao;
import by.k19.dao.PurchasesDao;
import by.k19.model.Product;
import by.k19.model.ProductProvider;
import by.k19.model.ProductRequest;
import by.k19.model.Purchase;
import lombok.Data;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.*;

@Data
@Named
@SessionScoped
public class ManagerBean implements Serializable {
    @Inject
    private DatabaseBean db;
    @Inject
    private Validator validator;
    @Inject
    private UserBean currentUser;

    private Product currentProduct;
    private Purchase currentPurchase;
    private Integer amountProduct;
    private boolean enablePanelSA;
    private boolean enablePanelSS;
    private boolean enablePanelCS;
    private boolean enablePanelCA;
    private boolean enablePanelRequests;
    private boolean enablePanelPurchases;
    private boolean enableQueryToMainStorage;
    private boolean enableEditProductPanelSA;
    private boolean enableEditProductPanelSS;
    private boolean enableCreatePurchasePanel;
    private boolean addingNewProductSA;
    private boolean creatingProductToPurchase;
    private boolean addingNewProductToPurchase;

    private boolean addingNewProductSS;
    private boolean updatingProductSA;
    private boolean updatingProductSS;
    private boolean updatingProductPurchase;
    private Map<Product, Integer> newQueryMap;

    public List<Product> getNewQueryList() {
        newQueryMap = new HashMap<>();
        Map<Product, Integer> currentMap = new HashMap<>(getCA());
        for (Product product : getCS().keySet()) {
            if (currentMap.containsKey(product))
                currentMap.put(product, currentMap.get(product) + getCS().get(product));
            else currentMap.put(product, getCS().get(product));
        }
        Map<Product, Integer> standardMap = new HashMap<>(getSA());
        for (Product product : getSS().keySet()) {
            if (standardMap.containsKey(product))
                standardMap.put(product, standardMap.get(product) + getSS().get(product));
            else standardMap.put(product, getSS().get(product));
        }
        for (Product product : standardMap.keySet()) {
            if (!currentMap.containsKey(product))
                newQueryMap.put(product, standardMap.get(product));
            else if (currentMap.get(product) < standardMap.get(product))
                newQueryMap.put(product, standardMap.get(product) - currentMap.get(product));
        }
        return new ArrayList<>(newQueryMap.keySet());
    }

    public void saveQuery() {
        ProductRequest request = new ProductRequest(newQueryMap, new Date(), currentUser.getUser().getOutlet(), currentUser.getUser());
        db.save(request);
        //TODO: open available requests table
        closeAllTables();
    }

    public Map<Product, Integer> getSA() {
        return currentUser.getUser().getOutlet().getStandardAssortment();
    }

    public Map<Product, Integer> getSS() {
        return currentUser.getUser().getOutlet().getStandardStorage();
    }

    public Map<Product, Integer> getCA() {
        return currentUser.getUser().getOutlet().getCurrentAssortment();
    }

    public Map<Product, Integer> getCS() {
        return currentUser.getUser().getOutlet().getCurrentStorage();
    }

    public List<Product> getSAproducts() {
        return new ArrayList<>(getSA().keySet());
    }

    public List<Product> getPurchaseProducts() {
        if (currentPurchase == null)
            currentPurchase = new Purchase();
        if (currentPurchase.getProducts() == null)
            currentPurchase.setProducts(new HashMap<>());
        return new ArrayList<>(currentPurchase.getProducts().keySet());
    }

    public List<Purchase> getPurchasesHistory() {
        return ((PurchasesDao)db.getDao(Purchase.class)).findAllByOutletId(currentUser.getUser().getOutlet().getId());
    }

    public List<Product> getSSproducts() {
        return new ArrayList<>(getSS().keySet());
    }

    public List<Product> getCAproducts() {
        return new ArrayList<>(getCA().keySet());
    }

    public List<Product> getCSproducts() {
        return new ArrayList<>(getCS().keySet());
    }

    public List<ProductRequest> getRequestList() {
        return new ArrayList<>(((ProductRequestDao)db.getDao(ProductRequest.class)).findAllByOutletId(currentUser.getUser().getOutlet().getId()));
    }

    public void acceptRequest(ProductRequest request) {
        Map<Product, Integer> standardAMap = getSA();
        Map<Product, Integer> currentAMap = getCA();
        Map<Product, Integer> requestMap = request.getProductMap();
        Map<Product, Integer> neededAssortMap = new HashMap<>();
        for (Product product : standardAMap.keySet()) {
            if (!currentAMap.containsKey(product))
                neededAssortMap.put(product, standardAMap.get(product));
            else if (currentAMap.get(product) < standardAMap.get(product))
                neededAssortMap.put(product, standardAMap.get(product) - currentAMap.get(product));
        }
        for (Product product : neededAssortMap.keySet()) {
            if (requestMap.containsKey(product)) {
                if (requestMap.get(product) > neededAssortMap.get(product)) {
                    if (!currentAMap.containsKey(product))
                        currentAMap.put(product, neededAssortMap.get(product));
                    else currentAMap.put(product, currentAMap.get(product) + neededAssortMap.get(product));
                    requestMap.put(product, requestMap.get(product) - neededAssortMap.get(product));
                }
                else if (Objects.equals(requestMap.get(product), neededAssortMap.get(product))) {
                    currentAMap.put(product, currentAMap.get(product) + neededAssortMap.get(product));
                    requestMap.remove(product);
                }
                else if (requestMap.get(product) < neededAssortMap.get(product)) {
                    currentAMap.put(product, currentAMap.get(product) + requestMap.get(product));
                    requestMap.remove(product);
                }
            }
        }
        currentUser.getUser().getOutlet().setCurrentAssortment(currentAMap);
        Map<Product, Integer> currentSMap = getCS();
        for (Product product : requestMap.keySet()) {
            if (currentSMap.containsKey(product)) {
                currentSMap.put(product, currentSMap.get(product) + requestMap.get(product));
            }
            else currentSMap.put(product, requestMap.get(product));
        }
        currentUser.getUser().getOutlet().setCurrentStorage(currentSMap);
        db.update(currentUser.getUser().getOutlet());

        request.setAcceptTime(new Date());
        db.update(request);
    }

    public void deleteRequest(ProductRequest request) {
        db.delete(request);
    }

    public void enableAddProductPanelSA() {
        currentProduct = new Product();
        enableEditProductPanelSA = true;
        addingNewProductSA = true;
    }

    public void enableProductToPurchase() {
        currentProduct = new Product();
        creatingProductToPurchase = true;
        addingNewProductToPurchase = true;
    }

    public void enableAddProductPanelSS() {
        currentProduct = new Product();
        enableEditProductPanelSS = true;
        addingNewProductSS = true;
    }

    public void showCreatePurchasePanel() {
        currentPurchase = new Purchase();
        enableCreatePurchasePanel = true;
    }

    private void closeAllTables() {
        enablePanelPurchases = false;
        enablePanelRequests = false;
        enableQueryToMainStorage = false;
        enablePanelSA = false;
        enablePanelSS = false;
        enablePanelCA = false;
        enablePanelCS = false;
    }

    public void showPanelSA() {
        closeAllTables();
        enablePanelSA = true;
    }

    public void showPanelSS() {
        closeAllTables();
        enablePanelSS = true;
    }

    public void showPanelCA() {
        closeAllTables();
        enablePanelCA = true;
    }

    public void showPanelCS() {
        closeAllTables();
        enablePanelCS = true;
    }

    public void showPanelRequests() {
        closeAllTables();
        enablePanelRequests = true;
    }

    public void showPanelPurchase() {
        closeAllTables();
        enablePanelPurchases = true;
    }

    public void startQueryToMainStorage() {
        closeAllTables();
        enableQueryToMainStorage = true;
    }

    public void deleteProductSA(Product product) {
        getSA().remove(product);
        db.update(currentUser.getUser().getOutlet());
    }

    public void deleteProductPurchase(Product product) {
        currentPurchase.getProducts().remove(product);
    }

    public void deleteProductSS(Product product) {
        getSS().remove(product);
        db.update(currentUser.getUser().getOutlet());
    }

    public void startUpdateProductPurchase(Product product, Integer amount) {
        currentProduct = product;
        amountProduct = amount;
        creatingProductToPurchase = true;
        updatingProductPurchase = true;
    }

    ////
    public void startUpdateProductSA(Product product, Integer amount) {
        currentProduct = product;
        amountProduct = amount;
        enableEditProductPanelSA = true;
        updatingProductSA = true;
    }

    public void startUpdateProductSS(Product product, Integer amount) {
        currentProduct = product;
        amountProduct = amount;
        enableEditProductPanelSS = true;
        updatingProductSS = true;
    }

    public void disableAddProductPanelSA() {
        currentProduct = null;
        amountProduct = 0;
        enableEditProductPanelSA = false;
        addingNewProductSA = false;
        updatingProductSA = false;
    }

    public void disableAddProductPanelPurchase() {
        currentProduct = null;
        amountProduct = 0;
        creatingProductToPurchase = false;
        addingNewProductToPurchase = false;
        updatingProductPurchase = false;
    }

    public void disableAddProductPanelSS() {
        currentProduct = null;
        amountProduct = 0;
        enableEditProductPanelSS = false;
        addingNewProductSS = false;
        updatingProductSS = false;
    }

    public void createProductSA() {
        if (getSA() == null)
            currentUser.getUser().getOutlet().setStandardAssortment(new HashMap<>());
        if (validator.valid(currentProduct)) {
            getSA().put(currentProduct, amountProduct);
            db.update(currentUser.getUser().getOutlet());
            disableAddProductPanelSA();
        }
    }

    public void createProductPurchase() {
        if (currentPurchase.getProducts() == null)
            currentPurchase.setProducts(new HashMap<>());
        if (validator.valid(currentProduct)) {
            currentPurchase.getProducts().put(currentProduct, amountProduct);
//            db.update(currentUser.getUser().getOutlet());
            disableAddProductPanelPurchase();
        }
    }

    public void createProductSS() {
        if (getSS() == null)
            currentUser.getUser().getOutlet().setStandardStorage(new HashMap<>());
        if (validator.valid(currentProduct)) {
            getSS().put(currentProduct, amountProduct);
            db.update(currentUser.getUser().getOutlet());
            disableAddProductPanelSS();
        }
    }

    public void updateProductSA() {
        if (validator.valid(currentProduct)) {
            Map<Product, Integer> newSA = new HashMap<>();
            for (Product product : getSA().keySet()) {
                if (product.getId() == currentProduct.getId())
                    newSA.put(currentProduct, amountProduct);
                else newSA.put(product, getSA().get(product));
            }
            currentUser.getUser().getOutlet().setStandardAssortment(newSA);
            db.update(currentUser.getUser().getOutlet());
            disableAddProductPanelSA();
        }
    }

    public void updateProductPurchase() {
        if (validator.valid(currentProduct)) {
            Map<Product, Integer> newProducts = new HashMap<>();
            for (Product product : currentPurchase.getProducts().keySet()) {
                if (product.getId() == currentProduct.getId())
                    newProducts.put(currentProduct, amountProduct);
                else newProducts.put(product, currentPurchase.getProducts().get(product));
            }
            currentPurchase.setProducts(newProducts);
            disableAddProductPanelPurchase();
        }
    }

    public void createPurchase() {
        if (validator.valid(currentPurchase)) {
            currentPurchase.setTime(new Date());
            currentPurchase.setUser(currentUser.getUser());
            currentPurchase.setOutlet(currentUser.getUser().getOutlet());
            for (Product product : currentPurchase.getProducts().keySet()) {
                if (!getCA().containsKey(product)) {
                    currentPurchase.getProducts().remove(product);
                }
                else if (getCA().get(product) < currentPurchase.getProducts().get(product)) {
                    currentPurchase.getProducts().put(product, getCA().get(product));
                    getCA().remove(product);
                }
                else if (Objects.equals(getCA().get(product), currentPurchase.getProducts().get(product))) {
                    getCA().remove(product);
                }
                else if (getCA().get(product) > currentPurchase.getProducts().get(product)) {
                    getCA().put(product, getCA().get(product) - currentPurchase.getProducts().get(product));
                }
            }
            db.update(currentUser.getUser().getOutlet());
            db.save(currentPurchase);
            disableAddProductPanelPurchase();
            disableCreatePanel();
        }
    }

    public void disableCreatePanel() {
        currentPurchase = null;
        enablePanelPurchases = false;
    }

    public void updateProductSS() {
        if (validator.valid(currentProduct)) {
            Map<Product, Integer> newSS = new HashMap<>();
            for (Product product : getSS().keySet()) {
                if (product.getId() == currentProduct.getId())
                    newSS.put(currentProduct, amountProduct);
                else newSS.put(product, getSS().get(product));
            }
            currentUser.getUser().getOutlet().setStandardStorage(newSS);
            db.update(currentUser.getUser().getOutlet());
            disableAddProductPanelSS();
        }
    }

    private void updateUser() {
        db.update(currentUser.getUser());
    }

    public List<Product> getAvailableProducts(boolean isAssortment) {
        List<Product> products = new ArrayList<>();
        for (ProductProvider provider : db.getDao(ProductProvider.class).findAll()) {
            for (Product product : provider.getProductMap().keySet()) {
                if (isAssortment) {
                    if (!getSA().containsKey(product))
                        products.add(product);
                }
                else {
                    if (!getSS().containsKey(product))
                        products.add(product);
                }
            }
        }
        return products;
    }

    public List<Product> getAvailableProductsCACS(boolean isAssortment) {
        List<Product> products = new ArrayList<>();
        for (ProductProvider provider : db.getDao(ProductProvider.class).findAll()) {
            for (Product product : provider.getProductMap().keySet()) {
                if (isAssortment) {
                    if (getCA().containsKey(product) && !currentPurchase.getProducts().containsKey(product))
                        products.add(product);
                }
                else {
                    if (getCS().containsKey(product) && !currentPurchase.getProducts().containsKey(product))
                        products.add(product);
                }
            }
        }
        return products;
    }
}
