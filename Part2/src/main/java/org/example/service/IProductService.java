package org.example.service;
import org.example.dao.entities.Product;

import java.util.List;

public interface IProductService {
    void addProduct(Product product);
    void deleteProductById(int id);
    List<Product> getAllProducts();
    void updateProduct(Product product);
    List<Product> searchProductByQuery(String query);
}
