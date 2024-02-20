package org.example.presentation;


import org.example.CustomIOCAnnotation.CustomAnnotationConfigApplicationContext;
import org.example.customIOCXML.CustomApplicationContext;
import org.example.service.IProductService;

public class presentationCustomIOCAnnotations {
     public static void main(String[] args) {
         // Test framework for annotation-based IOC
            CustomApplicationContext context = new CustomAnnotationConfigApplicationContext("org.example");
            IProductService productService =  context.getBean(IProductService.class);
            System.out.println(productService.getAllProducts());

    }


}
