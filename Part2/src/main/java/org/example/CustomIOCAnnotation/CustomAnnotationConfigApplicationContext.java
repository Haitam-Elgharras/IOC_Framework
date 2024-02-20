package org.example.CustomIOCAnnotation;

import org.example.CustomIOCAnnotation.interfaces.Autowired;
import org.example.CustomIOCAnnotation.interfaces.Repository;
import org.example.CustomIOCAnnotation.interfaces.Service;
import org.example.customIOCXML.CustomApplicationContext;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

public class CustomAnnotationConfigApplicationContext implements CustomApplicationContext {
    private Map<Class<?>, Object> beans = new HashMap<>();
    private String basePackage;

    public CustomAnnotationConfigApplicationContext(String basePackage) {
        this.basePackage = basePackage;
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


    private void instantiateNoConstructorArgBeans() throws Exception {
        for (Class<?> classx : new ArrayList<>(beans.keySet())) {
            if (beans.get(classx) != null)
                continue;


            if (classx.getConstructors().length == 0) {
                beans.put(classx, classx.newInstance());
            } else {
                for (Constructor<?> constructor : classx.getConstructors()) {
                    if (constructor.getParameterCount() == 0) {
                        beans.put(classx, constructor.newInstance());
                        break;
                    }
                }
            }
        }
    }

    private void instantiateCostructorArgBeans() throws Exception {
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
                            break;
                        }
                    }
                }
            }
        }
    }

    private Object findBeanOfType(Class<?> type) {
        for (Object bean : beans.values()) {
            if (type.isInstance(bean)) {
                return bean;
            }
        }
        return null;
    }

    private void instantiateAutowiredBeans() throws Exception {
        for (Class<?> classx : new ArrayList<>(beans.keySet())) {
                for (Method method : classx.getMethods()) {
                    if (method.isAnnotationPresent(Autowired.class) && method.getParameterCount() > 0) {
                        Object[] args = new Object[method.getParameterCount()];
                        Class<?>[] paramTypes = method.getParameterTypes();
                        boolean allArgsFound = true;
                        for (int i = 0; i < paramTypes.length; i++) {
                            Object arg = findBeanOfType(paramTypes[i]);
                            if (arg == null) {
                                allArgsFound = false;
                                break;
                            }
                            args[i] = arg;
                            System.out.println("here");
                        }
                        if (allArgsFound) {
                            Object instance = classx.newInstance();
                            method.invoke(instance, args);
                            System.out.println(method.getName());
                            beans.put(classx, instance);
                            break;
                        }
                    }
                }
        }
    }

    public <T> T searchIntrImplConstrctor(Class<T> beanInterface) {
        try{
            scanPackage(basePackage);
            instantiateNoConstructorArgBeans();
            instantiateCostructorArgBeans();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        if(beans != null)
            for (Map.Entry<Class<?>, Object> entry : beans.entrySet()) {
                Class<?> beanClass = entry.getKey();
                Object beanInstance = entry.getValue();
                if (beanInterface.isAssignableFrom(beanClass)) {
                    return beanInterface.cast(beanInstance);
                }
            }
        return null;
    }

    private <T> T searchIntrImplSetter(Class<T> beanInterface) {
        try{
            scanPackage(basePackage);
            instantiateNoConstructorArgBeans();
            instantiateAutowiredBeans();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        if(beans != null)
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
        T bean = searchIntrImplConstrctor(beanInterface);
        if (bean != null) {
            for (Constructor<?> constructor : bean.getClass().getConstructors()) {
                for (Class<?> paramType : constructor.getParameterTypes()) {
                    if (paramType.isInstance(bean)) {
                        return bean;
                    }
                }
            }
        }
        T beanS= searchIntrImplSetter(beanInterface);
        System.out.println(beanS.getClass().getName());
        return  beanS;
    }


}
