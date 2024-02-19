package org.example.customIOCXML;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class BeanDefinition {

    @XmlAttribute(name = "id")
    private String id;

    @XmlAttribute(name = "class")
    private String className;

    @XmlElement(name = "constructor-arg")
    private ConstructorArg constructorArg;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public ConstructorArg getConstructorArg() {
        return constructorArg;
    }

    public void setConstructorArg(ConstructorArg constructorArg) {
        this.constructorArg = constructorArg;
    }
}