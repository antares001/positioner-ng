//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.06.18 at 02:24:28 PM EEST 
//


package com.google.earth.kml._2;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for styleStateEnum.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="styleStateEnum">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="normal"/>
 *     &lt;enumeration value="highlight"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "styleStateEnum")
@XmlEnum
public enum StyleStateEnum {

    @XmlEnumValue("normal")
    NORMAL("normal"),
    @XmlEnumValue("highlight")
    HIGHLIGHT("highlight");
    private final String value;

    StyleStateEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static StyleStateEnum fromValue(String v) {
        for (StyleStateEnum c: StyleStateEnum.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
