package org.example.presentation;

import org.example.customIOCXML.CustomApplicationContext;
import org.example.customIOCXML.CustomClassPathXmlApplicationContext;
import org.example.service.IProductService;

public class presentationCustomIOCXML {
    public static void main(String[] args) {
        // Test framework for XML-based IOC
        CustomApplicationContext Context = new CustomClassPathXmlApplicationContext("config.xml");
        IProductService productService =  Context.getBean(IProductService.class);
        System.out.println(productService.getAllProducts());
    }
}
