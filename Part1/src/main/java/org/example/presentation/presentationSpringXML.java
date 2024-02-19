package org.example.presentation;

import org.example.service.IMetier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class presentationSpringXML {
    public static void main(String[] args) {

        ApplicationContext springContext = new ClassPathXmlApplicationContext("config.xml");
        IMetier metier = springContext.getBean(IMetier.class);

        int result = metier.calcul();
        System.out.println("Résultat du calcul : " + result);

    }
}
