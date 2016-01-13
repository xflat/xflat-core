package cn.xflat.core.spring;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repo;

    public List<Product> getAllProducts(String productId) {
        System.out.println("productid : " + productId);
        return repo.findAll();
    }

    public void getProduct(String productId) {
        System.out.println("productid : " + productId);
    }

}