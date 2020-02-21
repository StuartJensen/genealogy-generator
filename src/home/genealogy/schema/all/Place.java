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
 *         &lt;element ref="{}place1Level" minOccurs="0"/>
 *         &lt;element ref="{}globalCoordinates" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="modifier">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *             &lt;enumeration value="NEAR"/>
 *             &lt;enumeration value="OF"/>
 *             &lt;enumeration value="PROBABLY"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="country" use="required">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *             &lt;enumeration value="USA"/>
 *             &lt;enumeration value="England"/>
 *             &lt;enumeration value="Scotland"/>
 *             &lt;enumeration value="Ireland"/>
 *             &lt;enumeration value="Wales"/>
 *             &lt;enumeration value="Denmark"/>
 *             &lt;enumeration value="Norway"/>
 *             &lt;enumeration value="Finland"/>
 *             &lt;enumeration value="Germany"/>
 *             &lt;enumeration value="France"/>
 *             &lt;enumeration value="Holland"/>
 *             &lt;enumeration value="Ukraine"/>
 *             &lt;enumeration value="Italy"/>
 *             &lt;enumeration value="Austria"/>
 *             &lt;enumeration value="Switzerland"/>
 *             &lt;enumeration value="Hungary"/>
 *             &lt;enumeration value="Spain"/>
 *             &lt;enumeration value="Canada"/>
 *             &lt;enumeration value="Mexico"/>
 *             &lt;enumeration value="Greece"/>
 *             &lt;enumeration value="Russia"/>
 *             &lt;enumeration value="Sweden"/>
 *             &lt;enumeration value="Turkey"/>
 *             &lt;enumeration value="Belgium"/>
 *             &lt;enumeration value="Tonga"/>
 *             &lt;enumeration value="Prussia"/>
 *             &lt;enumeration value="Columbia"/>
 *             &lt;enumeration value="Venezuela"/>
 *             &lt;enumeration value="Brazil"/>
 *             &lt;enumeration value="Iceland"/>
 *             &lt;enumeration value="Israel"/>
 *             &lt;enumeration value="Egypt"/>
 *             &lt;enumeration value="India"/>
 *             &lt;enumeration value="Iran"/>
 *             &lt;enumeration value="Iraq"/>
 *             &lt;enumeration value="Pakistan"/>
 *             &lt;enumeration value="Japan"/>
 *             &lt;enumeration value="Korea"/>
 *             &lt;enumeration value="VietNam"/>
 *             &lt;enumeration value="Portugal"/>
 *             &lt;enumeration value="NewZealand"/>
 *             &lt;enumeration value="Australia"/>
 *             &lt;enumeration value="Scandinavia"/>
 *             &lt;enumeration value="Tunisia"/>
 *             &lt;enumeration value="AtSea"/>
 *             &lt;enumeration value="PacificOcean"/>
 *             &lt;enumeration value="AtlanticOcean"/>
 *             &lt;enumeration value="Austrasia"/>
 *             &lt;enumeration value="Bavaria"/>
 *             &lt;enumeration value="Neustria"/>
 *             &lt;enumeration value="Netherlands"/>
 *             &lt;enumeration value="UNKNOWN"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="temple">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *             &lt;enumeration value="ABA"/>
 *             &lt;enumeration value="ACCRA"/>
 *             &lt;enumeration value="ADELA"/>
 *             &lt;enumeration value="ALBER"/>
 *             &lt;enumeration value="ALBUQ"/>
 *             &lt;enumeration value="ANCHO"/>
 *             &lt;enumeration value="ANTON"/>
 *             &lt;enumeration value="APIA"/>
 *             &lt;enumeration value="ARIZO"/>
 *             &lt;enumeration value="ASUNC"/>
 *             &lt;enumeration value="ATLAN"/>
 *             &lt;enumeration value="BAIRE"/>
 *             &lt;enumeration value="BILLI"/>
 *             &lt;enumeration value="BIRMI"/>
 *             &lt;enumeration value="BISMA"/>
 *             &lt;enumeration value="BOGOT"/>
 *             &lt;enumeration value="BOISE"/>
 *             &lt;enumeration value="BOSTO"/>
 *             &lt;enumeration value="BOUNT"/>
 *             &lt;enumeration value="BRISB"/>
 *             &lt;enumeration value="BROUG"/>
 *             &lt;enumeration value="CAMPI"/>
 *             &lt;enumeration value="CARAC"/>
 *             &lt;enumeration value="CHICA"/>
 *             &lt;enumeration value="CIUJU"/>
 *             &lt;enumeration value="COCHA"/>
 *             &lt;enumeration value="COLJU"/>
 *             &lt;enumeration value="COLSC"/>
 *             &lt;enumeration value="COLUM"/>
 *             &lt;enumeration value="COPEN"/>
 *             &lt;enumeration value="CRIVE"/>
 *             &lt;enumeration value="CURIT"/>
 *             &lt;enumeration value="DALLA"/>
 *             &lt;enumeration value="DENVE"/>
 *             &lt;enumeration value="DETRO"/>
 *             &lt;enumeration value="EDMON"/>
 *             &lt;enumeration value="EHOUS"/>
 *             &lt;enumeration value="FRANK"/>
 *             &lt;enumeration value="FREIB"/>
 *             &lt;enumeration value="FRESN"/>
 *             &lt;enumeration value="FUKUO"/>
 *             &lt;enumeration value="GUADA"/>
 *             &lt;enumeration value="GUATE"/>
 *             &lt;enumeration value="GUAYA"/>
 *             &lt;enumeration value="HAGUE"/>
 *             &lt;enumeration value="HALIF"/>
 *             &lt;enumeration value="HARRI"/>
 *             &lt;enumeration value="HAWAI"/>
 *             &lt;enumeration value="HELSI"/>
 *             &lt;enumeration value="HERMO"/>
 *             &lt;enumeration value="HKONG"/>
 *             &lt;enumeration value="HOUST"/>
 *             &lt;enumeration value="IFALL"/>
 *             &lt;enumeration value="JOHAN"/>
 *             &lt;enumeration value="JRIVE"/>
 *             &lt;enumeration value="KIEV"/>
 *             &lt;enumeration value="KIRTL"/>
 *             &lt;enumeration value="KONA"/>
 *             &lt;enumeration value="LANGE"/>
 *             &lt;enumeration value="LIMA"/>
 *             &lt;enumeration value="LOGAN"/>
 *             &lt;enumeration value="LONDO"/>
 *             &lt;enumeration value="LOUIS"/>
 *             &lt;enumeration value="LUBBO"/>
 *             &lt;enumeration value="LVEGA"/>
 *             &lt;enumeration value="MADRI"/>
 *             &lt;enumeration value="MANIL"/>
 *             &lt;enumeration value="MANHA"/>
 *             &lt;enumeration value="MANTI"/>
 *             &lt;enumeration value="MEDFO"/>
 *             &lt;enumeration value="MELBO"/>
 *             &lt;enumeration value="MEMPH"/>
 *             &lt;enumeration value="MERID"/>
 *             &lt;enumeration value="MEXIC"/>
 *             &lt;enumeration value="MNTVD"/>
 *             &lt;enumeration value="MONTE"/>
 *             &lt;enumeration value="MONTI"/>
 *             &lt;enumeration value="MONTR"/>
 *             &lt;enumeration value="MTIMP"/>
 *             &lt;enumeration value="NASHV"/>
 *             &lt;enumeration value="NAUV2"/>
 *             &lt;enumeration value="NAUVO"/>
 *             &lt;enumeration value="NBEAC"/>
 *             &lt;enumeration value="NUKUA"/>
 *             &lt;enumeration value="NYORK"/>
 *             &lt;enumeration value="NZEAL"/>
 *             &lt;enumeration value="OAKLA"/>
 *             &lt;enumeration value="OAXAC"/>
 *             &lt;enumeration value="OGDEN"/>
 *             &lt;enumeration value="OKLAH"/>
 *             &lt;enumeration value="ORLAN"/>
 *             &lt;enumeration value="PALEG"/>
 *             &lt;enumeration value="PALMY"/>
 *             &lt;enumeration value="PAPEE"/>
 *             &lt;enumeration value="PCITY"/>
 *             &lt;enumeration value="PERTH"/>
 *             &lt;enumeration value="POFFI"/>
 *             &lt;enumeration value="PORTL"/>
 *             &lt;enumeration value="PREST"/>
 *             &lt;enumeration value="PROVO"/>
 *             &lt;enumeration value="RALEI"/>
 *             &lt;enumeration value="RECIF"/>
 *             &lt;enumeration value="REDLA"/>
 *             &lt;enumeration value="REGIN"/>
 *             &lt;enumeration value="RENO"/>
 *             &lt;enumeration value="REXBU"/>
 *             &lt;enumeration value="SACRA"/>
 *             &lt;enumeration value="SAMOA"/>
 *             &lt;enumeration value="SANTI"/>
 *             &lt;enumeration value="SANTO"/>
 *             &lt;enumeration value="SDIEG"/>
 *             &lt;enumeration value="SDOMI"/>
 *             &lt;enumeration value="SEATT"/>
 *             &lt;enumeration value="SEOUL"/>
 *             &lt;enumeration value="SGEOR"/>
 *             &lt;enumeration value="SJOSE"/>
 *             &lt;enumeration value="SLAKE"/>
 *             &lt;enumeration value="SLOUI"/>
 *             &lt;enumeration value="SNOWF"/>
 *             &lt;enumeration value="SPAUL"/>
 *             &lt;enumeration value="SPMIN"/>
 *             &lt;enumeration value="SPOKA"/>
 *             &lt;enumeration value="STOCK"/>
 *             &lt;enumeration value="SUVA"/>
 *             &lt;enumeration value="SWISS"/>
 *             &lt;enumeration value="SYDNE"/>
 *             &lt;enumeration value="TAIPE"/>
 *             &lt;enumeration value="TAMPI"/>
 *             &lt;enumeration value="TGUTI"/>
 *             &lt;enumeration value="TOKYO"/>
 *             &lt;enumeration value="TORON"/>
 *             &lt;enumeration value="TRICI"/>
 *             &lt;enumeration value="VERAC"/>
 *             &lt;enumeration value="VERNA"/>
 *             &lt;enumeration value="VILLA"/>
 *             &lt;enumeration value="WASHI"/>
 *             &lt;enumeration value="WINTE"/>
 *             &lt;enumeration value="WQUAR"/>
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
    "place1Level",
    "globalCoordinates"
})
@XmlRootElement(name = "place")
public class Place {

    protected Place1Level place1Level;
    protected GlobalCoordinates globalCoordinates;
    @XmlAttribute
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String modifier;
    @XmlAttribute(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String country;
    @XmlAttribute
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String temple;

    /**
     * Gets the value of the place1Level property.
     * 
     * @return
     *     possible object is
     *     {@link Place1Level }
     *     
     */
    public Place1Level getPlace1Level() {
        return place1Level;
    }

    /**
     * Sets the value of the place1Level property.
     * 
     * @param value
     *     allowed object is
     *     {@link Place1Level }
     *     
     */
    public void setPlace1Level(Place1Level value) {
        this.place1Level = value;
    }

    /**
     * Gets the value of the globalCoordinates property.
     * 
     * @return
     *     possible object is
     *     {@link GlobalCoordinates }
     *     
     */
    public GlobalCoordinates getGlobalCoordinates() {
        return globalCoordinates;
    }

    /**
     * Sets the value of the globalCoordinates property.
     * 
     * @param value
     *     allowed object is
     *     {@link GlobalCoordinates }
     *     
     */
    public void setGlobalCoordinates(GlobalCoordinates value) {
        this.globalCoordinates = value;
    }

    /**
     * Gets the value of the modifier property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getModifier() {
        return modifier;
    }

    /**
     * Sets the value of the modifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setModifier(String value) {
        this.modifier = value;
    }

    /**
     * Gets the value of the country property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCountry() {
        return country;
    }

    /**
     * Sets the value of the country property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCountry(String value) {
        this.country = value;
    }

    /**
     * Gets the value of the temple property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTemple() {
        return temple;
    }

    /**
     * Sets the value of the temple property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTemple(String value) {
        this.temple = value;
    }

}
