package org.example.dao;

import org.example.CustomIOCAnnotation.interfaces.Repository;
import org.example.dao.DBSingletonDb;
import org.example.dao.ProductDao;
import org.example.dao.entities.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ProductDaoImpl implements ProductDao {
    private  Connection connection = DBSingletonDb.connection;
    private PreparedStatement pstm=null;
    @Override
    public void create(Product product) {
        try {
             pstm= connection.prepareStatement(
                    "INSERT INTO products (name,price,quantity) VALUES (?,?,?)"
            );
            pstm.setString(1,product.getName());
            pstm.setDouble(2,product.getPrice());
            pstm.setDouble(3,product.getQuantity());
            pstm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Product findById(Integer id) {
        Product product = null;
        try {
             pstm= connection.prepareStatement(
                    "SELECT * FROM products WHERE id=?"
            );
            pstm.setInt(1,id);
            ResultSet rs= pstm.executeQuery();

            if (rs.next()) {
                product = new Product(
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getDouble("quantity")
                );
                product.setId(rs.getLong("id"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return product;
    }

    @Override
    public List<Product> getAll() {
        List<Product> products = new ArrayList<>();
        try {
             pstm= connection.prepareStatement(
                    "SELECT * FROM products"
            );
            ResultSet rs= pstm.executeQuery();

            while (rs.next()) {
                Product product = new Product(
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getDouble("quantity")
                );
                product.setId(rs.getLong("id"));
                products.add(product);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        System.out.println("v1\n");
        return products;
    }

    @Override
    public void update(Product product) {
        try {
             pstm= connection.prepareStatement(
                    "UPDATE products SET name=?,price=?,quantity=? WHERE id=?"
            );
            pstm.setString(1,product.getName());
            pstm.setDouble(2,product.getPrice());
            pstm.setDouble(3,product.getQuantity());
            pstm.setLong(4,product.getId());
            pstm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Integer id) {
        try {
             pstm= connection.prepareStatement(
                    "DELETE FROM products WHERE id=?"
            );
            pstm.setInt(1,id);
            pstm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<Product> searchProductByQuery(String query) {
        try {
             pstm= connection.prepareStatement(
                    "SELECT * FROM products " +
                            "WHERE name LIKE ? OR price LIKE ? OR quantity LIKE ?"
            );
            pstm.setString(1,"%"+query+"%");
            pstm.setString(2,"%"+query+"%");
            pstm.setString(3,"%"+query+"%");

            ResultSet rs= pstm.executeQuery();
            List<Product> products = new ArrayList<>();
            while (rs.next()) {
                Product product = new Product(
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getDouble("quantity")
                );
                product.setId(rs.getLong("id"));
                products.add(product);
            }
            return products;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
