//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.12.21 at 05:46:39 PM MST 
//


package home.genealogy.schema.all;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{}marriageInfo" minOccurs="0"/>
 *         &lt;element ref="{}termination" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="marriageId" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="husbandPersonId" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="wifePersonId" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="version" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="inLine">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *             &lt;enumeration value="true"/>
 *             &lt;enumeration value="false"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "marriageInfo",
    "termination"
})
@XmlRootElement(name = "marriage")
public class Marriage {

    protected MarriageInfo marriageInfo;
    protected Termination termination;
    @XmlAttribute(required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String marriageId;
    @XmlAttribute(required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String husbandPersonId;
    @XmlAttribute(required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String wifePersonId;
    @XmlAttribute(required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String version;
    @XmlAttribute
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String inLine;

    /**
     * Gets the value of the marriageInfo property.
     * 
     * @return
     *     possible object is
     *     {@link MarriageInfo }
     *     
     */
    public MarriageInfo getMarriageInfo() {
        return marriageInfo;
    }

    /**
     * Sets the value of the marriageInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link MarriageInfo }
     *     
     */
    public void setMarriageInfo(MarriageInfo value) {
        this.marriageInfo = value;
    }

    /**
     * Gets the value of the termination property.
     * 
     * @return
     *     possible object is
     *     {@link Termination }
     *     
     */
    public Termination getTermination() {
        return termination;
    }

    /**
     * Sets the value of the termination property.
     * 
     * @param value
     *     allowed object is
     *     {@link Termination }
     *     
     */
    public void setTermination(Termination value) {
        this.termination = value;
    }

    /**
     * Gets the value of the marriageId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMarriageId() {
        return marriageId;
    }

    /**
     * Sets the value of the marriageId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMarriageId(String value) {
        this.marriageId = value;
    }

    /**
     * Gets the value of the husbandPersonId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHusbandPersonId() {
        return husbandPersonId;
    }

    /**
     * Sets the value of the husbandPersonId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHusbandPersonId(String value) {
        this.husbandPersonId = value;
    }

    /**
     * Gets the value of the wifePersonId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWifePersonId() {
        return wifePersonId;
    }

    /**
     * Sets the value of the wifePersonId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWifePersonId(String value) {
        this.wifePersonId = value;
    }

    /**
     * Gets the value of the version property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVersion() {
        return version;
    }

    /**
     * Sets the value of the version property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVersion(String value) {
        this.version = value;
    }

    /**
     * Gets the value of the inLine property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInLine() {
        return inLine;
    }

    /**
     * Sets the value of the inLine property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInLine(String value) {
        this.inLine = value;
    }

}
