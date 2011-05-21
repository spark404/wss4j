//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.05.13 at 04:42:25 PM CEST 
//


package org.w3._2000._09.xmldsig_;

import org.swssf.ext.Constants;
import org.swssf.ext.ParseException;
import org.swssf.ext.Parseable;
import org.swssf.ext.Utils;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * <p>Java class for SignatureType complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="SignatureType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.w3.org/2000/09/xmldsig#}SignedInfo"/>
 *         &lt;element ref="{http://www.w3.org/2000/09/xmldsig#}SignatureValue"/>
 *         &lt;element ref="{http://www.w3.org/2000/09/xmldsig#}KeyInfo" minOccurs="0"/>
 *         &lt;element ref="{http://www.w3.org/2000/09/xmldsig#}Object" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="Id" type="{http://www.w3.org/2001/XMLSchema}ID" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SignatureType", propOrder = {
        "signedInfo",
        "signatureValue",
        "keyInfo",
        "object"
})
public class SignatureType implements Parseable {

    @XmlElement(name = "SignedInfo", required = true)
    protected SignedInfoType signedInfo;
    @XmlElement(name = "SignatureValue", required = true)
    protected SignatureValueType signatureValue;
    @XmlElement(name = "KeyInfo")
    protected KeyInfoType keyInfo;
    @XmlElement(name = "Object")
    protected List<ObjectType> object;
    @XmlAttribute(name = "Id")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected String id;

    private Parseable currentParseable;

    public SignatureType(StartElement startElement) {
        @SuppressWarnings("unchecked")
        Iterator<Attribute> attributeIterator = startElement.getAttributes();
        while (attributeIterator.hasNext()) {
            Attribute attribute = attributeIterator.next();
            if (attribute.getName().equals(Constants.ATT_NULL_Id)) {
                CollapsedStringAdapter collapsedStringAdapter = new CollapsedStringAdapter();
                this.id = collapsedStringAdapter.unmarshal(attribute.getValue());
            }
        }
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

                if (startElement.getName().equals(Constants.TAG_dsig_SignedInfo)) {
                    currentParseable = this.signedInfo = new SignedInfoType(startElement);
                } else if (startElement.getName().equals(Constants.TAG_dsig_SignatureValue)) {
                    currentParseable = this.signatureValue = new SignatureValueType(startElement);
                } else if (startElement.getName().equals(Constants.TAG_dsig_KeyInfo)) {
                    currentParseable = this.keyInfo = new KeyInfoType(startElement);
                } else {
                    throw new ParseException("Unsupported Element: " + startElement.getName());
                }

                break;
            case XMLStreamConstants.END_ELEMENT:
                currentParseable = null;
                EndElement endElement = xmlEvent.asEndElement();
                if (endElement.getName().equals(Constants.TAG_dsig_Signature)) {
                    return true;
                }
                break;
            //possible ignorable withespace and comments
            case XMLStreamConstants.CHARACTERS:
            case XMLStreamConstants.COMMENT:
                break;
            default:
                throw new ParseException("Unexpected event received " + Utils.getXMLEventAsString(xmlEvent));
        }
        return false;
    }

    public void validate() throws ParseException {
        if (signedInfo == null || signatureValue == null) {
            throw new ParseException("Element \"SignedInfo\"|\"SignatureValue\" is missing");
        }
    }

    /**
     * Gets the value of the signedInfo property.
     *
     * @return possible object is
     *         {@link SignedInfoType }
     */
    public SignedInfoType getSignedInfo() {
        return signedInfo;
    }

    /**
     * Sets the value of the signedInfo property.
     *
     * @param value allowed object is
     *              {@link SignedInfoType }
     */
    public void setSignedInfo(SignedInfoType value) {
        this.signedInfo = value;
    }

    /**
     * Gets the value of the signatureValue property.
     *
     * @return possible object is
     *         {@link SignatureValueType }
     */
    public SignatureValueType getSignatureValue() {
        return signatureValue;
    }

    /**
     * Sets the value of the signatureValue property.
     *
     * @param value allowed object is
     *              {@link SignatureValueType }
     */
    public void setSignatureValue(SignatureValueType value) {
        this.signatureValue = value;
    }

    /**
     * Gets the value of the keyInfo property.
     *
     * @return possible object is
     *         {@link KeyInfoType }
     */
    public KeyInfoType getKeyInfo() {
        return keyInfo;
    }

    /**
     * Sets the value of the keyInfo property.
     *
     * @param value allowed object is
     *              {@link KeyInfoType }
     */
    public void setKeyInfo(KeyInfoType value) {
        this.keyInfo = value;
    }

    /**
     * Gets the value of the object property.
     * <p/>
     * <p/>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the object property.
     * <p/>
     * <p/>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getObject().add(newItem);
     * </pre>
     * <p/>
     * <p/>
     * <p/>
     * Objects of the following type(s) are allowed in the list
     * {@link ObjectType }
     */
    public List<ObjectType> getObject() {
        if (object == null) {
            object = new ArrayList<ObjectType>();
        }
        return this.object;
    }

    /**
     * Gets the value of the id property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setId(String value) {
        this.id = value;
    }

}
