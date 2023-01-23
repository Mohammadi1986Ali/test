package com.javalab.tutorial.archunit.web.product;

import com.javalab.tutorial.archunit.api.product.ProductService;
import com.javalab.tutorial.archunit.service.product.ProductServiceImpl;

public class ProductResource {
    ProductService service = new ProductServiceImpl();

    public void welcome(String name) {
        service.welcome(name);
    }
}
