package org.example.customIOCXML;


import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement(name = "beans")
@XmlAccessorType(XmlAccessType.FIELD)
public class BeansList {
    @XmlElement(name = "bean")
    private List<BeanDefinition> beanDefinitions;

    public List<BeanDefinition> getBeansList() {
        return beanDefinitions;
    }

    public void setBeanDefinitions(List<BeanDefinition> beanDefinitions) {
        this.beanDefinitions = beanDefinitions;
    }
}
