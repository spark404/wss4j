/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.w3._2000._09.xmldsig_;

import org.swssf.xmlsec.ext.ParseException;
import org.swssf.xmlsec.ext.Parseable;
import org.swssf.xmlsec.ext.XMLSecurityConstants;
import org.swssf.xmlsec.ext.XMLSecurityUtils;
import org.w3c.dom.Element;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.*;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for X509DataType complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="X509DataType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence maxOccurs="unbounded">
 *         &lt;choice>
 *           &lt;element name="X509IssuerSerial" type="{http://www.w3.org/2000/09/xmldsig#}X509IssuerSerialType"/>
 *           &lt;element name="X509SKI" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/>
 *           &lt;element name="X509SubjectName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *           &lt;element name="X509Certificate" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/>
 *           &lt;element name="X509CRL" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/>
 *           &lt;any processContents='lax' namespace='##other'/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "X509DataType", propOrder = {
        "x509IssuerSerialOrX509SKIOrX509SubjectName"
})
public class X509DataType implements Parseable {

    private Parseable currentParseable;

    @XmlElementRefs({
            @XmlElementRef(name = "X509Certificate", namespace = "http://www.w3.org/2000/09/xmldsig#", type = JAXBElement.class),
            @XmlElementRef(name = "X509IssuerSerial", namespace = "http://www.w3.org/2000/09/xmldsig#", type = JAXBElement.class),
            @XmlElementRef(name = "X509SubjectName", namespace = "http://www.w3.org/2000/09/xmldsig#", type = JAXBElement.class),
            @XmlElementRef(name = "X509SKI", namespace = "http://www.w3.org/2000/09/xmldsig#", type = JAXBElement.class),
            @XmlElementRef(name = "X509CRL", namespace = "http://www.w3.org/2000/09/xmldsig#", type = JAXBElement.class)
    })
    @XmlAnyElement(lax = true)
    protected List<Object> x509IssuerSerialOrX509SKIOrX509SubjectName;
    @XmlElement(name = "X509IssuerSerial")
    protected X509IssuerSerialType x509IssuerSerialType;

    public X509DataType(StartElement startElement) {
    }

    public boolean parseXMLEvent(XMLEvent xmlEvent) throws ParseException {

        if (currentParseable != null) {
            boolean finished = currentParseable.parseXMLEvent(xmlEvent);
            if (finished) {
                currentParseable.validate();
                currentParseable = null;
            }
            return false;
        }

        switch (xmlEvent.getEventType()) {
            case XMLStreamConstants.START_ELEMENT:
                StartElement startElement = xmlEvent.asStartElement();

                if (startElement.getName().equals(XMLSecurityConstants.TAG_dsig_X509IssuerSerial)) {
                    currentParseable = this.x509IssuerSerialType = new X509IssuerSerialType(startElement);
                } else {
                    throw new ParseException("Unsupported Element: " + startElement.getName());
                }
                break;
            case XMLStreamConstants.END_ELEMENT:
                currentParseable = null;
                EndElement endElement = xmlEvent.asEndElement();
                if (endElement.getName().equals(XMLSecurityConstants.TAG_dsig_X509Data)) {
                    return true;
                }
                break;
            //possible ignorable withespace and comments
            case XMLStreamConstants.CHARACTERS:
            case XMLStreamConstants.COMMENT:
                break;
            default:
                throw new ParseException("Unexpected event received " + XMLSecurityUtils.getXMLEventAsString(xmlEvent));
        }
        return false;
    }

    public void validate() throws ParseException {
        if (x509IssuerSerialType == null) {
            throw new ParseException("Element \"X509IssuerSerialType\" is missing");
        }
    }

    /**
     * Gets the value of the x509IssuerSerialOrX509SKIOrX509SubjectName property.
     * <p/>
     * <p/>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the x509IssuerSerialOrX509SKIOrX509SubjectName property.
     * <p/>
     * <p/>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getX509IssuerSerialOrX509SKIOrX509SubjectName().add(newItem);
     * </pre>
     * <p/>
     * <p/>
     * <p/>
     * Objects of the following type(s) are allowed in the list
     * {@link Element }
     * {@link Object }
     * {@link JAXBElement }{@code <}{@link byte[]}{@code >}
     * {@link JAXBElement }{@code <}{@link X509IssuerSerialType }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link byte[]}{@code >}
     * {@link JAXBElement }{@code <}{@link byte[]}{@code >}
     */
    public List<Object> getX509IssuerSerialOrX509SKIOrX509SubjectName() {
        if (x509IssuerSerialOrX509SKIOrX509SubjectName == null) {
            x509IssuerSerialOrX509SKIOrX509SubjectName = new ArrayList<Object>();
        }
        return this.x509IssuerSerialOrX509SKIOrX509SubjectName;
    }

    public X509IssuerSerialType getX509IssuerSerialType() {
        return x509IssuerSerialType;
    }

    public void setX509IssuerSerialType(X509IssuerSerialType x509IssuerSerialType) {
        this.x509IssuerSerialType = x509IssuerSerialType;
    }
}