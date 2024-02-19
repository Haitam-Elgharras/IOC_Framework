package org.example.presentation;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.Scanner;

import org.example.dao.IDao;
import org.example.service.IMetier;

public class presentationDynamic {
    public static void main(String[] args)  {

        try(Scanner scanner = new Scanner(new File("src/main/resources/"+"config.txt"))) {
        // read the the class that impl the dao interface
        String daoClass = scanner.nextLine();
        // instantiate the class
        IDao dao = (IDao) Class.forName(daoClass).getConstructor().newInstance();

        // read the the class that impl the metier interface
        String metierClass = scanner.nextLine();
        // instantiate the class and inject the dao
        IMetier metier = (IMetier) Class.forName(metierClass).getConstructor(IDao.class).newInstance(dao);
        System.out.println(metier.calcul());
        } catch ( Exception e) {
            e.printStackTrace();
        }
    }
}
