package org.example.presentation;

import org.example.dao.DaoImpl;
import org.example.dao.IDao;
import org.example.service.IMetier;
import org.example.service.MetierImpl;

public class presentationStatic {
    public static void main(String[] args) {
        IDao dao = new DaoImpl();
        // injection de dépendance par le constructeur
        IMetier metier = new MetierImpl(dao);
        int result = metier.calcul();
        System.out.println("Résultat du calcul : " + result);
    }
}
