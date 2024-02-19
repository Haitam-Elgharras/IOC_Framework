package org.example.CustomIOCAnnotation;

import org.example.CustomIOCAnnotation.interfaces.Repository;
import org.example.CustomIOCAnnotation.interfaces.Service;
import org.example.customIOCXML.CustomApplicationContext;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.*;

public class CustomAnnotationConfigApplicationContext implements CustomApplicationContext {
    private Map<Class<?>, Object> beans = new HashMap<>();
    private Map<Class<?>, Set<Class<?>>> dependencies = new HashMap<>();

    public CustomAnnotationConfigApplicationContext(String basePackage) {
        try {
            scanPackage(basePackage);
            identifyDependencies();
            instantiateBeans();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void scanPackage(String basePackage) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        String basePath = basePackage.replace(".", "/");
        URL url = getClass().getClassLoader().getResource(basePath);
        if (url != null) {
            File baseDirectory = new File(url.getFile());
            if (baseDirectory.exists() && baseDirectory.isDirectory()) {
                processDirectory(baseDirectory, basePackage);
            }
        }
    }

    private void processDirectory(File directory, String basePackage) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        for (File file : Objects.requireNonNull(directory.listFiles())) {
            System.out.println(file.getName());
            if (file.isDirectory()) {
                processDirectory(file, basePackage + "." + file.getName());
            } else if (file.getName().endsWith(".class")) {
                String className = basePackage + "." + file.getName().replace(".class", "");
                Class<?> clazz = Class.forName(className);
                processClass(clazz);
            }
        }
    }


    private void processClass(Class<?> clazz) throws IllegalAccessException, InstantiationException {
        if (clazz.isAnnotationPresent(Service.class) || clazz.isAnnotationPresent(Repository.class)) {
            System.out.println(clazz.getName() + " is annotated");
            beans.put(clazz, null); // Store the class first
        }
    }

    private void identifyDependencies() {
        for (Class<?> clazz : beans.keySet()) {
            Constructor<?>[] constructors = clazz.getConstructors();
            if (constructors.length > 0) {
                Class<?>[] parameterTypes = constructors[0].getParameterTypes();
                Set<Class<?>> classDependencies = new HashSet<>(Arrays.asList(parameterTypes));
                dependencies.put(clazz, classDependencies);
            }
        }
    }

    private void instantiateBeans() throws IllegalAccessException, InstantiationException {
        for (Class<?> clazz : beans.keySet()) {
            instantiateBean(clazz);
        }
    }

    private void instantiateBean(Class<?> clazz) throws IllegalAccessException, InstantiationException {
        if (beans.get(clazz) == null) {
            Set<Class<?>> classDependencies = dependencies.get(clazz);
            if (classDependencies != null) {
                List<Object> dependencyInstances = new ArrayList<>();
                for (Class<?> dependency : classDependencies) {
                    Object dependencyInstance = beans.get(dependency);
                    if (dependencyInstance == null) {
                        instantiateBean(dependency); // Instantiate dependency first
                        dependencyInstance = beans.get(dependency);
                    }
                    dependencyInstances.add(dependencyInstance);
                }
                Constructor<?>[] constructors = clazz.getConstructors();
                if (constructors.length > 0) {
                    Constructor<?> constructor = constructors[0];
                    Object instance = null;
                    try {
                        instance = constructor.newInstance(dependencyInstances.toArray());
                    } catch (InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                    beans.put(clazz, instance);
                }
            }
        }
    }

    public <T> T getBean(Class<T> beanClass) {
        return beanClass.cast(beans.get(beanClass));
    }
}
