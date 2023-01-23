package com.javalab.tutorial.archunit.service.product;

import com.javalab.tutorial.archunit.api.product.ProductService;
import com.javalab.tutorial.archunit.repository.product.ProductRepository;

public class ProductServiceImpl implements ProductService {

    ProductRepository repository = new ProductRepository();

    @Override
    public void welcome(String name) {
        repository.welcome(name);
    }
}
