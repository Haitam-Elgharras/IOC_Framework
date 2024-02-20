package org.example.customIOCXML;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

public class CustomClassPathXmlApplicationContext implements CustomApplicationContext {
    private final String configXml;
    private final Map<String, Object> beanMap;

    public CustomClassPathXmlApplicationContext(String configXml) {
        this.configXml = configXml;
        this.beanMap = new HashMap<>();
        initializeBeans();
    }

    private void initializeBeans() {
        try {
            File file = new File("src/main/resources/" + configXml);
            JAXBContext jaxbContext = JAXBContext.newInstance(BeansList.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            BeansList beansList = (BeansList) unmarshaller.unmarshal(file);

            // First pass: Initialize beans without constructor arguments
            for (BeanDefinition beanDefinition : beansList.getBeansList()) {
                if (beanDefinition.getConstructorArg() == null) {
                    Class<?> beanClass = Class.forName(beanDefinition.getClassName());
                    Object beanInstance = beanClass.getDeclaredConstructor().newInstance();
                    beanMap.put(beanDefinition.getId(), beanInstance);
                }
            }

            // less dynamic way to initialize beans with constructor arguments because it relies on the order of the interface
//            for (BeanDefinition beanDefinition : beansList.getBeansList()) {
//                if (beanDefinition.getConstructorArg() != null) {
//                    Class<?> beanClass = Class.forName(beanDefinition.getClassName());
//
//                    Object constructorArgBean = beanMap.get(beanDefinition.getConstructorArg().getRef()); // Get the referenced bean
//
//                    Object beanInstance = beanClass.getConstructor(constructorArgBean.getClass().getInterfaces()[0])
//                                                                                      .newInstance(constructorArgBean);
//                    beanMap.put(beanDefinition.getId(), beanInstance);
//                }
//            }

                // more dynamic way to initialize beans with constructor arguments

            for (BeanDefinition beanDefinition : beansList.getBeansList()) {
                if (beanDefinition.getConstructorArg() != null) {
                    Class<?> beanClass = Class.forName(beanDefinition.getClassName());

                    Object constructorArgBean = beanMap.get(beanDefinition.getConstructorArg().getRef()); // Get the referenced bean

                    Constructor<?>[] constructors = beanClass.getConstructors();
                    Object beanInstance = null;

                    for (Constructor<?> constructor : constructors) {
                        Class<?>[] paramTypes = constructor.getParameterTypes();
                        for (Class<?> paramType : paramTypes) {
                            if (paramType.isInstance(constructorArgBean)) {
                                try {
                                    beanInstance = constructor.newInstance(constructorArgBean);
                                    break;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        if (beanInstance != null) {
                            break;
                        }
                    }

                    if (beanInstance == null) {
                        throw new RuntimeException("No suitable constructor found for bean class " + beanClass.getName());
                    }

                    beanMap.put(beanDefinition.getId(), beanInstance);
                }
            }

        } catch (JAXBException | ReflectiveOperationException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize beans from XML configuration.");
        }
    }

    @Override
    public <T> T getBean(Class<T> beanClass) {
        // Look for the bean instance based on its class name
        for (Object beanInstance : beanMap.values()) {
            if (beanClass.isInstance(beanInstance)) {
                // Cast the bean to the requested type and return
                return beanClass.cast(beanInstance);
            }
        }
        // If the bean is not found, return null or throw an exception as needed
        return null;
    }
}
