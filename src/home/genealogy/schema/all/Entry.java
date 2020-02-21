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
import javax.xml.bind.annotation.XmlElement;
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
 *         &lt;element ref="{}entryTitle"/>
 *         &lt;element ref="{}place" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}header" minOccurs="0"/>
 *         &lt;element ref="{}body" minOccurs="0"/>
 *         &lt;element ref="{}comment" minOccurs="0"/>
 *         &lt;element ref="{}tagGroup" minOccurs="0"/>
 *         &lt;element ref="{}seeAlso" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="access" use="required">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *             &lt;enumeration value="PUBLIC"/>
 *             &lt;enumeration value="PRIVATE"/>
 *             &lt;enumeration value="COPYRIGHTED"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="entryId" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "entryTitle",
    "place",
    "header",
    "body",
    "comment",
    "tagGroup",
    "seeAlso"
})
@XmlRootElement(name = "entry")
public class Entry {

    @XmlElement(required = true)
    protected EntryTitle entryTitle;
    protected List<Place> place;
    protected Header header;
    protected Body body;
    protected Comment comment;
    protected TagGroup tagGroup;
    protected SeeAlso seeAlso;
    @XmlAttribute(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String access;
    @XmlAttribute(required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String entryId;

    /**
     * Gets the value of the entryTitle property.
     * 
     * @return
     *     possible object is
     *     {@link EntryTitle }
     *     
     */
    public EntryTitle getEntryTitle() {
        return entryTitle;
    }

    /**
     * Sets the value of the entryTitle property.
     * 
     * @param value
     *     allowed object is
     *     {@link EntryTitle }
     *     
     */
    public void setEntryTitle(EntryTitle value) {
        this.entryTitle = value;
    }

    /**
     * Gets the value of the place property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the place property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPlace().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Place }
     * 
     * 
     */
    public List<Place> getPlace() {
        if (place == null) {
            place = new ArrayList<Place>();
        }
        return this.place;
    }

    /**
     * Gets the value of the header property.
     * 
     * @return
     *     possible object is
     *     {@link Header }
     *     
     */
    public Header getHeader() {
        return header;
    }

    /**
     * Sets the value of the header property.
     * 
     * @param value
     *     allowed object is
     *     {@link Header }
     *     
     */
    public void setHeader(Header value) {
        this.header = value;
    }

    /**
     * Gets the value of the body property.
     * 
     * @return
     *     possible object is
     *     {@link Body }
     *     
     */
    public Body getBody() {
        return body;
    }

    /**
     * Sets the value of the body property.
     * 
     * @param value
     *     allowed object is
     *     {@link Body }
     *     
     */
    public void setBody(Body value) {
        this.body = value;
    }

    /**
     * Gets the value of the comment property.
     * 
     * @return
     *     possible object is
     *     {@link Comment }
     *     
     */
    public Comment getComment() {
        return comment;
    }

    /**
     * Sets the value of the comment property.
     * 
     * @param value
     *     allowed object is
     *     {@link Comment }
     *     
     */
    public void setComment(Comment value) {
        this.comment = value;
    }

    /**
     * Gets the value of the tagGroup property.
     * 
     * @return
     *     possible object is
     *     {@link TagGroup }
     *     
     */
    public TagGroup getTagGroup() {
        return tagGroup;
    }

    /**
     * Sets the value of the tagGroup property.
     * 
     * @param value
     *     allowed object is
     *     {@link TagGroup }
     *     
     */
    public void setTagGroup(TagGroup value) {
        this.tagGroup = value;
    }

    /**
     * Gets the value of the seeAlso property.
     * 
     * @return
     *     possible object is
     *     {@link SeeAlso }
     *     
     */
    public SeeAlso getSeeAlso() {
        return seeAlso;
    }

    /**
     * Sets the value of the seeAlso property.
     * 
     * @param value
     *     allowed object is
     *     {@link SeeAlso }
     *     
     */
    public void setSeeAlso(SeeAlso value) {
        this.seeAlso = value;
    }

    /**
     * Gets the value of the access property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAccess() {
        return access;
    }

    /**
     * Sets the value of the access property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccess(String value) {
        this.access = value;
    }

    /**
     * Gets the value of the entryId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEntryId() {
        return entryId;
    }

    /**
     * Sets the value of the entryId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEntryId(String value) {
        this.entryId = value;
    }

}