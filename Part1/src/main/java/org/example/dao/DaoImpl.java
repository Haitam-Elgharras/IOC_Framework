package org.example.dao;

import org.springframework.stereotype.Repository;

public class DaoImpl implements IDao {
    @Override
    public String getDate() {
        // Implémentation de la méthode pour récupérer la date
        return "2024-01-18";
    }
}

