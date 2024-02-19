package org.example.customIOCXML;

public interface CustomApplicationContext {
    <T> T getBean(Class<T> aClass);
}
