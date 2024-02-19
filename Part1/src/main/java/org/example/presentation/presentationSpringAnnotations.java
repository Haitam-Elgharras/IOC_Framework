package org.example.presentation;

import org.example.service.IMetier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class presentationSpringAnnotations {
    public static void main(String[] args) {

        ApplicationContext springContext = new AnnotationConfigApplicationContext("org.example");
        IMetier metier = springContext.getBean(IMetier.class);
        System.out.println(metier.calcul());

    }
}
