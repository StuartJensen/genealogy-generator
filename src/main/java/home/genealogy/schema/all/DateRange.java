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
import javax.xml.bind.annotation.XmlValue;
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
 *       &lt;attribute name="yearBegin" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="monthBegin">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *             &lt;enumeration value="January"/>
 *             &lt;enumeration value="February"/>
 *             &lt;enumeration value="March"/>
 *             &lt;enumeration value="April"/>
 *             &lt;enumeration value="May"/>
 *             &lt;enumeration value="June"/>
 *             &lt;enumeration value="July"/>
 *             &lt;enumeration value="August"/>
 *             &lt;enumeration value="September"/>
 *             &lt;enumeration value="October"/>
 *             &lt;enumeration value="November"/>
 *             &lt;enumeration value="December"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="dayBegin" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="relativeTimeBegin">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *             &lt;enumeration value="ABOUT"/>
 *             &lt;enumeration value="CALC"/>
 *             &lt;enumeration value="PROBABLY"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="yearEnd" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="monthEnd">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *             &lt;enumeration value="January"/>
 *             &lt;enumeration value="February"/>
 *             &lt;enumeration value="March"/>
 *             &lt;enumeration value="April"/>
 *             &lt;enumeration value="May"/>
 *             &lt;enumeration value="June"/>
 *             &lt;enumeration value="July"/>
 *             &lt;enumeration value="August"/>
 *             &lt;enumeration value="September"/>
 *             &lt;enumeration value="October"/>
 *             &lt;enumeration value="November"/>
 *             &lt;enumeration value="December"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="dayEnd" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="relativeTimeEnd">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *             &lt;enumeration value="ABOUT"/>
 *             &lt;enumeration value="CALC"/>
 *             &lt;enumeration value="PROBABLY"/>
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
    "content"
})
@XmlRootElement(name = "dateRange")
public class DateRange {

    @XmlValue
    protected String content;
    @XmlAttribute
    @XmlSchemaType(name = "anySimpleType")
    protected String yearBegin;
    @XmlAttribute
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String monthBegin;
    @XmlAttribute
    @XmlSchemaType(name = "anySimpleType")
    protected String dayBegin;
    @XmlAttribute
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String relativeTimeBegin;
    @XmlAttribute
    @XmlSchemaType(name = "anySimpleType")
    protected String yearEnd;
    @XmlAttribute
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String monthEnd;
    @XmlAttribute
    @XmlSchemaType(name = "anySimpleType")
    protected String dayEnd;
    @XmlAttribute
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String relativeTimeEnd;

    /**
     * Gets the value of the content property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getContent() {
        return content;
    }

    /**
     * Sets the value of the content property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setContent(String value) {
        this.content = value;
    }

    /**
     * Gets the value of the yearBegin property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getYearBegin() {
        return yearBegin;
    }

    /**
     * Sets the value of the yearBegin property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setYearBegin(String value) {
        this.yearBegin = value;
    }

    /**
     * Gets the value of the monthBegin property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMonthBegin() {
        return monthBegin;
    }

    /**
     * Sets the value of the monthBegin property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMonthBegin(String value) {
        this.monthBegin = value;
    }

    /**
     * Gets the value of the dayBegin property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDayBegin() {
        return dayBegin;
    }

    /**
     * Sets the value of the dayBegin property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDayBegin(String value) {
        this.dayBegin = value;
    }

    /**
     * Gets the value of the relativeTimeBegin property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRelativeTimeBegin() {
        return relativeTimeBegin;
    }

    /**
     * Sets the value of the relativeTimeBegin property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRelativeTimeBegin(String value) {
        this.relativeTimeBegin = value;
    }

    /**
     * Gets the value of the yearEnd property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getYearEnd() {
        return yearEnd;
    }

    /**
     * Sets the value of the yearEnd property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setYearEnd(String value) {
        this.yearEnd = value;
    }

    /**
     * Gets the value of the monthEnd property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMonthEnd() {
        return monthEnd;
    }

    /**
     * Sets the value of the monthEnd property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMonthEnd(String value) {
        this.monthEnd = value;
    }

    /**
     * Gets the value of the dayEnd property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDayEnd() {
        return dayEnd;
    }

    /**
     * Sets the value of the dayEnd property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDayEnd(String value) {
        this.dayEnd = value;
    }

    /**
     * Gets the value of the relativeTimeEnd property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRelativeTimeEnd() {
        return relativeTimeEnd;
    }

    /**
     * Sets the value of the relativeTimeEnd property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRelativeTimeEnd(String value) {
        this.relativeTimeEnd = value;
    }

}
