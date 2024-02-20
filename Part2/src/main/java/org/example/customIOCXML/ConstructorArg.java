package org.example.customIOCXML;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.FIELD)
public class ConstructorArg {

    @XmlAttribute(name = "ref")
    private String ref;

    public String getRef() {
        return ref;
    }
}