//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.12.21 at 05:46:39 PM MST 
//


package home.genealogy.schema.all;

import java.util.ArrayList;
import java.util.List;
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
 *         &lt;element ref="{}dateRange" minOccurs="0"/>
 *         &lt;element ref="{}dateOr" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="year" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="month">
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
 *       &lt;attribute name="day">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *             &lt;enumeration value="1"/>
 *             &lt;enumeration value="2"/>
 *             &lt;enumeration value="3"/>
 *             &lt;enumeration value="4"/>
 *             &lt;enumeration value="5"/>
 *             &lt;enumeration value="6"/>
 *             &lt;enumeration value="7"/>
 *             &lt;enumeration value="8"/>
 *             &lt;enumeration value="9"/>
 *             &lt;enumeration value="10"/>
 *             &lt;enumeration value="11"/>
 *             &lt;enumeration value="12"/>
 *             &lt;enumeration value="13"/>
 *             &lt;enumeration value="14"/>
 *             &lt;enumeration value="15"/>
 *             &lt;enumeration value="16"/>
 *             &lt;enumeration value="17"/>
 *             &lt;enumeration value="18"/>
 *             &lt;enumeration value="19"/>
 *             &lt;enumeration value="20"/>
 *             &lt;enumeration value="21"/>
 *             &lt;enumeration value="22"/>
 *             &lt;enumeration value="23"/>
 *             &lt;enumeration value="24"/>
 *             &lt;enumeration value="25"/>
 *             &lt;enumeration value="26"/>
 *             &lt;enumeration value="27"/>
 *             &lt;enumeration value="28"/>
 *             &lt;enumeration value="29"/>
 *             &lt;enumeration value="30"/>
 *             &lt;enumeration value="31"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="relativeTime">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *             &lt;enumeration value="ABOUT"/>
 *             &lt;enumeration value="AFTER"/>
 *             &lt;enumeration value="BEFORE"/>
 *             &lt;enumeration value="CALC"/>
 *             &lt;enumeration value="PROBABLY"/>
 *             &lt;enumeration value="CHRISTENING"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="ldsModifiers">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *             &lt;enumeration value="CHILD"/>
 *             &lt;enumeration value="BIC"/>
 *             &lt;enumeration value="CLEARED"/>
 *             &lt;enumeration value="UNCLEARED"/>
 *             &lt;enumeration value="SUBMITTED"/>
 *             &lt;enumeration value="SEALEDFORTIME"/>
 *             &lt;enumeration value="DONOTSEAL"/>
 *             &lt;enumeration value="NEVER"/>
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
    "dateRange",
    "dateOr"
})
@XmlRootElement(name = "date")
public class Date {

    protected DateRange dateRange;
    protected List<DateOr> dateOr;
    @XmlAttribute
    @XmlSchemaType(name = "anySimpleType")
    protected String year;
    @XmlAttribute
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String month;
    @XmlAttribute
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String day;
    @XmlAttribute
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String relativeTime;
    @XmlAttribute
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String ldsModifiers;

    /**
     * Gets the value of the dateRange property.
     * 
     * @return
     *     possible object is
     *     {@link DateRange }
     *     
     */
    public DateRange getDateRange() {
        return dateRange;
    }

    /**
     * Sets the value of the dateRange property.
     * 
     * @param value
     *     allowed object is
     *     {@link DateRange }
     *     
     */
    public void setDateRange(DateRange value) {
        this.dateRange = value;
    }

    /**
     * Gets the value of the dateOr property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the dateOr property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDateOr().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DateOr }
     * 
     * 
     */
    public List<DateOr> getDateOr() {
        if (dateOr == null) {
            dateOr = new ArrayList<DateOr>();
        }
        return this.dateOr;
    }

    /**
     * Gets the value of the year property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getYear() {
        return year;
    }

    /**
     * Sets the value of the year property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setYear(String value) {
        this.year = value;
    }

    /**
     * Gets the value of the month property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMonth() {
        return month;
    }

    /**
     * Sets the value of the month property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMonth(String value) {
        this.month = value;
    }

    /**
     * Gets the value of the day property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDay() {
        return day;
    }

    /**
     * Sets the value of the day property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDay(String value) {
        this.day = value;
    }

    /**
     * Gets the value of the relativeTime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRelativeTime() {
        return relativeTime;
    }

    /**
     * Sets the value of the relativeTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRelativeTime(String value) {
        this.relativeTime = value;
    }

    /**
     * Gets the value of the ldsModifiers property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLdsModifiers() {
        return ldsModifiers;
    }

    /**
     * Sets the value of the ldsModifiers property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLdsModifiers(String value) {
        this.ldsModifiers = value;
    }

}