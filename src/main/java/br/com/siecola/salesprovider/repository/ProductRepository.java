package br.com.siecola.salesprovider.repository;

import br.com.siecola.salesprovider.exception.NonValidProductException;
import br.com.siecola.salesprovider.exception.ProductAlreadyExistsException;
import br.com.siecola.salesprovider.exception.ProductNotFoundException;
import br.com.siecola.salesprovider.model.Product;
import com.google.appengine.api.datastore.*;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Repository
public class ProductRepository {

    private static final Logger log = Logger.getLogger("ProductRepository");

    private static final String PRODUCT_KIND = "Products";
    private static final String PRODUCT_KEY = "productKey";

    private static final String PROPERTY_NAME = "name";
    private static final String PROPERTY_DESCRIPTION = "description";
    private static final String PROPERTY_CODE = "code";
    private static final String PROPERTY_PRICE = "price";
    private static final String PROPERTY_QUANTITY = "quantity";
    private static final String PROPERTY_USER_EMAIL = "userEmail";

    public List<Product> getProducts(boolean isAdmin, String userEmail) {
        List<Product> products = new ArrayList<>();
        DatastoreService datastore = DatastoreServiceFactory
                .getDatastoreService();

        Query query;
        query = new Query(PRODUCT_KIND).addSort(PROPERTY_CODE,
                Query.SortDirection.ASCENDING);

        if (!isAdmin) {
            Query.Filter filter = new Query.FilterPredicate(PROPERTY_USER_EMAIL,
                    Query.FilterOperator.EQUAL, userEmail);
            query.setFilter(filter);
        }

        List<Entity> entities = datastore.prepare(query).asList(
                FetchOptions.Builder.withDefaults());

        for (Entity entity : entities) {
            Product product = entityToProduct(entity);

            products.add(product);
        }

        return products;
    }

    public Optional<Product> getByCode(String code, String userEmail) {
        DatastoreService datastore = DatastoreServiceFactory
                .getDatastoreService();

        Query.CompositeFilter filter = getCodeUserEmailFilter(code, userEmail);

        Query query = new Query(PRODUCT_KIND).setFilter(filter);

        Entity entity = datastore.prepare(query).asSingleEntity();

        if (entity != null) {
            return Optional.ofNullable(entityToProduct(entity));
        } else {
            return Optional.empty();
        }
    }

    public Product saveProduct(Product product, String userEmail) throws ProductAlreadyExistsException,
            NonValidProductException {
        DatastoreService datastore = DatastoreServiceFactory
                .getDatastoreService();

        if (!isValidProduct(product)) {
            throw new NonValidProductException("Non valid product");
        }

        product.setUserEmail(userEmail);
        if (!checkIfCodeExist(product)) {
            Key key = KeyFactory.createKey(PRODUCT_KIND, PRODUCT_KEY);
            Entity entity = new Entity(PRODUCT_KIND, key);

            productToEntity(product, entity);

            datastore.put(entity);

            product.setId(entity.getKey().getId());

            return product;
        } else {
            throw new ProductAlreadyExistsException("Product " + product.getCode()
                    + " already exists");
        }
    }

    public Product updateProduct(Product product, String code, String userEmail)
            throws ProductNotFoundException, ProductAlreadyExistsException, NonValidProductException {

        if (!isValidProduct(product)) {
            throw new NonValidProductException("Non valid product");
        }

        product.setUserEmail(userEmail);
        if (!checkIfCodeExist(product)) {
            DatastoreService datastore = DatastoreServiceFactory
                    .getDatastoreService();

            Query.CompositeFilter filter = getCodeUserEmailFilter(code, userEmail);

            Query query = new Query(PRODUCT_KIND).setFilter(filter);

            Entity entity = datastore.prepare(query).asSingleEntity();

            if (entity != null) {
                productToEntity(product, entity);

                datastore.put(entity);

                product.setId(entity.getKey().getId());

                return product;
            } else {
                throw new ProductNotFoundException("Product " + code
                        + " not found");
            }
        } else {
            throw new ProductAlreadyExistsException("Product " + product.getCode()
                    + " already exists");
        }
    }

    public Product deleteProduct(String code, boolean isAdmin, String userEmail) throws ProductNotFoundException {
        DatastoreService datastore = DatastoreServiceFactory
                .getDatastoreService();

        Query.CompositeFilter filter = getCodeUserEmailFilter(code, userEmail);

        Query query = new Query(PRODUCT_KIND).setFilter(filter);

        Entity entity = datastore.prepare(query).asSingleEntity();

        if (entity != null) {
            datastore.delete(entity.getKey());

            return entityToProduct(entity);
        } else {
            throw new ProductNotFoundException("Product " + code
                    + " not found");
        }
    }

    private Query.CompositeFilter getCodeUserEmailFilter(String code, String userEmail) {
        Query.Filter codeFilter = new Query.FilterPredicate(PROPERTY_CODE,
                Query.FilterOperator.EQUAL, code);

        Query.Filter userEmailFilter = new Query.FilterPredicate(PROPERTY_USER_EMAIL,
                Query.FilterOperator.EQUAL, userEmail);

        return Query.CompositeFilterOperator.and(codeFilter, userEmailFilter);
    }

    private boolean isValidProduct(Product product) {
        return StringUtils.hasText(product.getCode()) && StringUtils.hasText(product.getName());
    }

    private boolean checkIfCodeExist(Product product) {
        DatastoreService datastore = DatastoreServiceFactory
                .getDatastoreService();

        Query.CompositeFilter filter = getCodeUserEmailFilter(product.getCode(), product.getUserEmail());

        Query query = new Query(PRODUCT_KIND).setFilter(filter);
        Entity productEntity = datastore.prepare(query).asSingleEntity();

        if (productEntity == null) {
            return false;
        } else {
            if (productEntity.getKey().getId() == product.getId()) {
                //it's the same product
                return false;
            } else {
                return true;
            }
        }
    }

    private void productToEntity(Product product, Entity productEntity) {
        productEntity.setProperty(PROPERTY_NAME, product.getName());
        productEntity.setProperty(PROPERTY_DESCRIPTION, product.getDescription());
        productEntity.setProperty(PROPERTY_CODE, product.getCode());
        productEntity.setProperty(PROPERTY_PRICE, product.getPrice());
        productEntity.setProperty(PROPERTY_QUANTITY, product.getQuantity());
        productEntity.setProperty(PROPERTY_USER_EMAIL, product.getUserEmail());
    }

    private Product entityToProduct(Entity productEntity) {
        Product product = new Product();
        product.setId(productEntity.getKey().getId());
        product.setName((String) productEntity.getProperty(PROPERTY_NAME));
        product.setDescription((String) productEntity.getProperty(PROPERTY_DESCRIPTION));
        product.setCode((String) productEntity.getProperty(PROPERTY_CODE));
        product.setPrice((Double) productEntity.getProperty(PROPERTY_PRICE));
        product.setUserEmail((String) productEntity.getProperty(PROPERTY_USER_EMAIL));
        if (productEntity.hasProperty(PROPERTY_QUANTITY)) {
            product.setQuantity(((Number) productEntity.getProperty(PROPERTY_QUANTITY)).intValue());
        } else {
            product.setQuantity(0);
        }
        return product;
    }
}
