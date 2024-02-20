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

    public CustomAnnotationConfigApplicationContext(String basePackage) {
        try {
            scanPackage(basePackage);
            instantiateNoArgBeans();
            instantiateArgBeans();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void scanPackage(String basePackage) throws Exception {
        String basePath = basePackage.replace(".", "/");
        URL url = getClass().getClassLoader().getResource(basePath);
        if (url != null) {
            File baseDirectory = new File(url.getFile());
            if (baseDirectory.exists() && baseDirectory.isDirectory()) {
                processDirectory(baseDirectory, basePackage);
            }
        }
    }

    private void processDirectory(File directory, String basePackage) throws Exception{
        for (File file : Objects.requireNonNull(directory.listFiles())) {
            if (file.isDirectory()) {
                processDirectory(file, basePackage + "." + file.getName());
            } else if (file.getName().endsWith(".class")) {
                String className = basePackage + "." + file.getName().replace(".class", "");
                Class<?> classx = Class.forName(className);
                processClass(classx);
            }
        }
    }


    private void processClass(Class<?> classx) {
        if (classx.isAnnotationPresent(Service.class) || classx.isAnnotationPresent(Repository.class)) {
            beans.put(classx, null); // Store the class first and not the instance
        }
    }


    private void instantiateNoArgBeans() throws Exception {
        for (Class<?> classx : new ArrayList<>(beans.keySet())) {
            if (beans.get(classx) != null)
                continue;

            System.out.println(true);

            if (classx.getConstructors().length == 0) {
                beans.put(classx, classx.newInstance());
                // name of the class
                System.out.println(classx.getName());
            } else {
                for (Constructor<?> constructor : classx.getConstructors()) {
                    if (constructor.getParameterCount() == 0) {
                        beans.put(classx, constructor.newInstance());
                        // name of the class
                        System.out.println(classx.getName());
                        break;
                    }
                }
            }
        }
    }

    private void instantiateArgBeans() throws Exception {
        for (Class<?> classx : new ArrayList<>(beans.keySet())) {
            if (beans.get(classx) == null) { // If the bean is not yet instantiated
                for (Constructor<?> constructor : classx.getConstructors()) {
                    if (constructor.getParameterCount() > 0) {
                        Object[] args = new Object[constructor.getParameterCount()];
                        Class<?>[] paramTypes = constructor.getParameterTypes();
                        boolean allArgsFound = true;
                        for (int i = 0; i < paramTypes.length; i++) {
                            Object arg = findBeanOfType(paramTypes[i]);
                            if (arg == null) {
                                allArgsFound = false;
                                break;
                            }
                            args[i] = arg;
                        }
                        if (allArgsFound) {
                            beans.put(classx, constructor.newInstance(args));
                            // class name and the args
                            System.out.println(classx.getName());
                            Arrays.stream(args).forEach(System.out::println);
                            break;
                        }
                    }
                }
            }
        }
    }

    private Object findBeanOfType(Class<?> type) {
        for (Object bean : beans.values()) {
            if (bean != null && type.isInstance(bean)) {
                return bean;
            }
        }
        return null;
    }

    public <T> T searchInterfaceImplementation(Class<T> beanInterface) {
        for (Map.Entry<Class<?>, Object> entry : beans.entrySet()) {
            Class<?> beanClass = entry.getKey();
            Object beanInstance = entry.getValue();
            if (beanInterface.isAssignableFrom(beanClass)) {
                return beanInterface.cast(beanInstance);
            }
        }
        return null;
    }

    public <T> T getBean(Class<T> beanInterface) {
        return searchInterfaceImplementation(beanInterface);
    }
}
