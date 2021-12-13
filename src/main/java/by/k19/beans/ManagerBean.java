package by.k19.beans;

import by.k19.dao.ProductRequestDao;
import by.k19.dao.PurchasesDao;
import by.k19.dao.SalesDao;
import by.k19.model.*;
import lombok.Data;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.TimeUnit;

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
    private Sale currentSale;
    private Integer amountProduct;
    private Map<String, String> statistics;
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
    private boolean enableQualityReportPanel;
    private boolean enableSales;
    private boolean enableTechSupport;
    private boolean enableStatistics;
    private boolean addingNewProductSA;
    private boolean creatingProductToPurchase;
    private boolean addingNewProductToPurchase;
    private boolean creatingSale;

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
        db.save(new UserAction(currentUser.getUser(), "Создано запрос на склад " + request));
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

    public List<Sale> getSales() {
        return db.findAll(Sale.class);
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
        db.save(new UserAction(currentUser.getUser(), "Реализован запрос на склад " + request));
    }

    public void deleteRequest(ProductRequest request) {
        db.delete(request);
        db.save(new UserAction(currentUser.getUser(), "Удалён запрос на склад " + request));
    }

    public void enableAddProductPanelSA() {
        currentProduct = new Product();
        enableEditProductPanelSA = true;
        addingNewProductSA = true;
    }

    public void enableAddSalePanel() {
        currentSale = new Sale();
        creatingSale = true;
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
        currentPurchase.setProducts(new HashMap<>());
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
        enableQualityReportPanel = false;
        enableSales = false;
        enableTechSupport = false;
        enableStatistics = false;
        enableCreatePurchasePanel = false;
        enableEditProductPanelSS = false;
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

    public void showSalesPanel() {
        closeAllTables();
        enableSales = true;
    }

    public void showStatistics() {
        closeAllTables();
        enableStatistics = true;
    }

    public void showTechSupport() {
        closeAllTables();
        enableTechSupport = true;
    }

    public void startQueryToMainStorage() {
        closeAllTables();
        enableQueryToMainStorage = true;
    }

    public void deleteProductSA(Product product) {
        getSA().remove(product);
        db.update(currentUser.getUser().getOutlet());
        db.save(new UserAction(currentUser.getUser(), "Из стандартного ассортимента удалён продукт " + product));
    }

    public void deleteProductPurchase(Product product) {
        currentPurchase.getProducts().remove(product);
    }

    public void deleteProductSS(Product product) {
        getSS().remove(product);
        db.save(new UserAction(currentUser.getUser(), "Из стандартного склада удалн продукт " + product));
        db.update(currentUser.getUser().getOutlet());
    }

    public void deleteSale(Sale sale) {
        db.delete(sale);
        db.save(new UserAction(currentUser.getUser(), "Удалена скидка  " + sale));
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
    //TODO: исправить фронт покупки

    public void disableAddProductPanelSA() {
        currentProduct = null;
        amountProduct = 0;
        enableEditProductPanelSA = false;
        addingNewProductSA = false;
        updatingProductSA = false;
    }

    public void disableSaleCreating() {
        currentSale = null;
        enableSales = true;
        creatingSale = false;
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
            db.save(new UserAction(currentUser.getUser(), "Создан продукт в стандартном ассортименте " + currentProduct));
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
            db.save(new UserAction(currentUser.getUser(), "Создан продукт в стандартном складе " + currentProduct));
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
            db.save(new UserAction(currentUser.getUser(), "Обновлён продукт в стандартном ассортименте " + currentProduct));
            disableAddProductPanelSA();
        }
    }

    public void createSale() {
        currentSale.setOutlet(currentUser.getUser().getOutlet());
        if (validator.valid(currentSale)) {
            db.save(currentSale);
            db.save(new UserAction(currentUser.getUser(), "Создана скидка " + currentSale));
            disableSaleCreating();
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
            db.save(new UserAction(currentUser.getUser(), "Оформлена покупка " + currentPurchase));
            disableAddProductPanelPurchase();
            disableCreatePanel();
        }
    }

    public void disableCreatePanel() {
        currentPurchase = null;
        enableCreatePurchasePanel = false;
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
            db.save(new UserAction(currentUser.getUser(), "Обновлён продукт в стандартном складе " + currentProduct));
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

    public List<Product> getAvailableProductsToSale() {
        List<Product> result = new ArrayList<>(getCA().keySet());
        for (Sale sale : ((SalesDao)db.getDao(Sale.class)).findFromOutletById(currentUser.getUser().getOutlet().getId())) {
            result.remove(sale.getProduct());
        }
        return result;
    }

    public void createQualityReport() {
        closeAllTables();
        enableQualityReportPanel = true;
    }

    public int checkQualityProduct(Product product) {
        if (product.getShelfLife() == null)
            return 0;
        long diff = product.getShelfLife().getTime() - new Date().getTime();
        long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        if (days > 7)
            return 1;
        if (days > 0)
            return 2;
        if (days < 0)
            return 3;
        return 0;
    }

    public String checkQualityProductToString(Product product) {
        long diff = product.getShelfLife().getTime() - new Date().getTime();
        long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        switch(checkQualityProduct(product)) {
            case 1: return "Свежий (" + days + " дней)";
            case 2: return "Срок годности истекает (" + days + " дней)";
            case 3: return "Срок годности ИСТЕК (" + Math.abs(days) + " дней назад)";
            default: return "Нет данных";
        }
    }

    public boolean checkReplacementProduct(Product product) {
        for (Product csProduct : getCS().keySet()) {
            if (csProduct.getName().equals(product.getName()) &&
                csProduct.getShelfLife().getTime() > new Date().getTime())
                return true;
        }
        return false;
    }

    public void replaceProductFromStorage(Product product) {
        int amount = getCA().get(product);
        getCA().remove(product);
        Product csProduct = null;
        for (Product csProd : getCS().keySet()) {
            if (csProd.getName().equals(product.getName()) &&
                    csProd.getShelfLife().getTime() > new Date().getTime())
                csProduct = csProd;
        }
        if (csProduct != null) {
            if (getCS().get(csProduct) > amount) {
                getCS().put(csProduct, getCS().get(csProduct) - amount);
                getCA().put(csProduct, amount);
            }
            else if (getCS().get(csProduct) == amount) {
                getCS().remove(csProduct);
                getCA().put(csProduct, amount);
            }
            else if (getCS().get(csProduct) < amount) {
                getCA().put(csProduct, getCS().get(csProduct));
                getCS().remove(csProduct);
            }
            db.update(currentUser.getUser().getOutlet());
        }
    }

    public long getAmountProblemsProducts() {
        long problems = 0;
        for (Product product : getCA().keySet()) {
            if (product.getShelfLife().getTime() < new Date().getTime()) {
                problems += getCA().get(product);
                problems += getCS().get(product);
            }
        }
        return problems;
    }

    public boolean commonStatusIsOk() {
        return getAmountProblemsProducts() == 0;
    }

    public void utilProduct(Product product) {
        if (product.getShelfLife().getTime() < new Date().getTime()) {
            getCA().remove(product);
            getCS().remove(product);
            db.update(currentUser.getUser().getOutlet());
            db.save(new UserAction(currentUser.getUser(), "Утилизирован продукт " + product));
        }
    }

    public List<UserAction> getAnnounces() {
        List<UserAction> resultList = new ArrayList<>();
        for (UserAction action : db.findAll(UserAction.class)) {
            if (action.getUser().getType() == UserType.DIRECTOR)
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

    public String getStatistic(String stat) {
        return statistics.get(stat);
    }

    public List<String> getMyStatistics() {
        return getFullStatistics(currentUser.getUser().getOutlet());
    }

    public List<String> getFullStatistics(Outlet outlet) {
        if (outlet == null)
            return new ArrayList<>();
        statistics = new HashMap<>();

        //Товаров в ассортименете
        statistics.put("Товаров в ассортименете", outlet.getStandardAssortment().keySet().size() + " видов");

        //Количество товаров в ассортименте на данный момент
        int sum1 = 0;
        for (Product product : outlet.getCurrentAssortment().keySet()) {
            sum1 += outlet.getCurrentAssortment().get(product);
        }
        statistics.put("Количество товаров в ассортименте на данный момент", sum1 + " шт.");

        //Количество товаров в ассортименте стандартно
        int sum2 = 0;
        for (Product product : outlet.getStandardAssortment().keySet()) {
            sum2 += outlet.getStandardAssortment().get(product);
        }
        statistics.put("Количество товаров в ассортименте стандартно", sum2 + " шт.");

        //Товаров на складе
        statistics.put("Товаров на складе", outlet.getStandardStorage().keySet().size() + " видов");

        //Количество товаров на складе на данный момент
        int sum3 = 0;
        for (Product product : outlet.getCurrentStorage().keySet()) {
            sum3 += outlet.getCurrentStorage().get(product);
        }
        statistics.put("Количество товаров на складе на данный момент", sum3 + " шт.");


        //КОличество товаров на складе стандартно
        int sum4 = 0;
        for (Product product : outlet.getStandardStorage().keySet()) {
            sum4 += outlet.getStandardStorage().get(product);
        }
        statistics.put("Количество товаров на складе на данный момент", sum4 + " шт.");

        List<ProductRequest> requests = ((ProductRequestDao)db.getDao(ProductRequest.class)).findAllByOutletId(outlet.getId());

        //Количество запросов
        statistics.put("Количество запросов", Integer.toString(requests.size()));

        //Количество удовлетворенных запросов
        Integer sum5 = 0;
        for (ProductRequest request : requests) {
            if (request.getAcceptTime() != null)
                sum5++;
        }
        statistics.put("Количество удовлетворенных запросов", sum5.toString());

        List<Sale> sales = ((SalesDao)db.getDao(Sale.class)).findFromOutletById(outlet.getId());

        //Скидок активно
        statistics.put("Скидок активно", Integer.toString(sales.size()));

        //Средний процент скидки
        double avrPercent = 0;
        for (Sale sale : sales) {
            avrPercent += sale.getPercent();
        }
        avrPercent /= sales.size();
        statistics.put("Средний процент скидки", Double.toString(avrPercent));


        //Средняя скидка на всю продукцию
        double avrAllPercent = 0;
        for (Sale sale : sales) {
            avrPercent += sale.getPercent();
        }
        avrPercent /= outlet.getStandardAssortment().size();
        statistics.put("Средняя скидка на всю продукцию", Double.toString(avrAllPercent));

        // Количество покупок
        Integer sumSellProds = 0;
        for (Purchase purchase : ((PurchasesDao)db.getDao(Purchase.class)).findAllByOutletId(outlet.getId())) {
            for (Product product : purchase.getProducts().keySet()) {
                sumSellProds += purchase.getProducts().get(product);
            }
        }
        statistics.put("Количество проданных товаров", sumSellProds.toString());

        //Количество завезённых продуктов со склада
        Integer amountProdsFromStorage = 0;
        for (ProductRequest request : ((ProductRequestDao)db.getDao(ProductRequest.class)).findAllByOutletId(outlet.getId())) {
            for (Product product : request.getProductMap().keySet()) {
                amountProdsFromStorage += request.getProductMap().get(product);
            }
        }
        statistics.put("Количество завезённых со склада товаров", amountProdsFromStorage.toString());
        return new ArrayList<>(statistics.keySet());
    }

    public List<Product> getAvailableToPurchaseProducts() {
        List<Product> result = new ArrayList<>(getCA().keySet());
        for (Product product : currentPurchase.getProducts().keySet()) {
            result.remove(product);
        }
        return result;
    }
}
