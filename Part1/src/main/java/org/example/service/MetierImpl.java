package org.example.service;

import org.example.dao.IDao;

public class MetierImpl implements IMetier {
    private IDao dao;

    // Injection de dépendance par le constructeur
    public MetierImpl(IDao dao) {
        this.dao = dao;
    }

    @Override
    public int calcul() {
        // Implémentation de la méthode de calcul en utilisant la méthode getDate() de l'interface IDao
        String date = dao.getDate();
        // Calcul basique pour l'exemple
        return date.length();
    }
}

