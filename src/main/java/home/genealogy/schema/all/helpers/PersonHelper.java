package home.genealogy.schema.all.helpers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import home.genealogy.GenealogyContext;
import home.genealogy.lists.PlaceList;
import home.genealogy.lists.RelationshipManager;
import home.genealogy.output.IOutputStream;
import home.genealogy.schema.all.BirthInfo;
import home.genealogy.schema.all.BurialInfo;
import home.genealogy.schema.all.ChrInfo;
import home.genealogy.schema.all.Date;
import home.genealogy.schema.all.DeathInfo;
import home.genealogy.schema.all.Event;
import home.genealogy.schema.all.EventGroup;
import home.genealogy.schema.all.Parents;
import home.genealogy.schema.all.Person;
import home.genealogy.schema.all.PersonName;
import home.genealogy.schema.all.Place;
import home.genealogy.util.StringUtil;

public class PersonHelper
{
	public static final String LIVING = "(Living)";
	
	private Person m_person;
	private boolean m_bIsPersonLiving;
	private boolean m_bSuppressLiving;
	private PlaceList m_placeList;
	
	public PersonHelper(Person person, boolean bSuppressLiving, PlaceList placeList)
	{
		m_person = person;
		m_placeList = placeList;
		m_bIsPersonLiving = PersonHelper.isLiving(person, m_placeList);
		m_bSuppressLiving = bSuppressLiving;
	}
	
	public boolean getIsPersonLiving()
	{
		return m_bIsPersonLiving;
	}

	// Getter Methods
	public String getPersonName()
	{
		String strPersonName = getPersonName(m_person);
		if (m_bSuppressLiving && m_bIsPersonLiving)
		{
			if (0 != strPersonName.length())
			{
				return LIVING;
			}
		}
		return strPersonName;
	}
	
	public int getPersonId()
	{
		return getPersonId(m_person);
	}
	
	public List<Parents> getParents()
	{
		return getParents(m_person);
	}

	public Parents getPreferredParents()
	{
		return getPreferredParents(m_person);
	}

	public Parents getBloodParents()
	{
		return getBloodParents(m_person);
	}
	
	public Parents getPreferredBloodThenAnyParents()
	{
		return ParentsHelper.getPreferredBloodThenAnyParents(m_person.getParents());
	}

	public String getAfn()
	{
		return getAfn(m_person);
	}
	
	public String getGender()
	{
		return getGender(m_person);
	}
	
	public String getRelationshipToPrimary()
	{
		String strRelationshipToPrimary = getRelationshipToPrimary(m_person);
		if (m_bSuppressLiving && m_bIsPersonLiving)
		{
			if (0 != strRelationshipToPrimary.length())
			{
				return LIVING;
			}
		}
		return strRelationshipToPrimary;
	}
	
	public String getFirstName()
	{
		String strFirstName = getFirstName(m_person);
		if (m_bSuppressLiving && m_bIsPersonLiving)
		{
			if (0 != strFirstName.length())
			{
				return LIVING;
			}
		}
		return strFirstName;
	}
	
	public String getMiddleName()
	{
		String strMiddleName = getMiddleName(m_person);
		if (m_bSuppressLiving && m_bIsPersonLiving)
		{
			if (0 != strMiddleName.length())
			{
				return LIVING;
			}
		}
		return strMiddleName;
	}
	
	public String getLastName()
	{
		String strLastName = getLastName(m_person);
		if (m_bSuppressLiving && m_bIsPersonLiving)
		{
			if (0 != strLastName.length())
			{
				return LIVING;
			}
		}
		return strLastName;
	}
	
	public List<String> getProfessions()
	{
		List<String> lProfessions = getProfessions(m_person);
		if (m_bSuppressLiving && m_bIsPersonLiving)
		{
			return Collections.singletonList(LIVING);
		}
		return lProfessions;
	}

	public List<String> getReligions()
	{
		List<String> lReligions = getReligions(m_person);
		if (m_bSuppressLiving && m_bIsPersonLiving)
		{
			return Collections.singletonList(LIVING);
		}
		return lReligions;
	}
	
	public List<String> getNickNames()
	{
		List<String> lNickNames = getNickNames(m_person);
		if (m_bSuppressLiving && m_bIsPersonLiving)
		{
			return Collections.singletonList(LIVING);
		}
		return lNickNames;
	}
	
	public String getBirthDate()
	{
		String strBirthDate = getBirthDate(m_person);
		if (m_bSuppressLiving && m_bIsPersonLiving)
		{
			if (0 != strBirthDate.length())
			{
				return LIVING;
			}
		}
		return strBirthDate;
	}
	
	public String getBirthPlace()
	{
		String strBirthPlace = getBirthPlace(m_person, m_placeList);
		if (m_bSuppressLiving && m_bIsPersonLiving)
		{
			if (0 != strBirthPlace.length())
			{
				return LIVING;
			}
		}
		return strBirthPlace;
	}
	
	public String getChrDate()
	{
		String strChrDate = getChrDate(m_person);
		if (m_bSuppressLiving && m_bIsPersonLiving)
		{
			if (0 != strChrDate.length())
			{
				return LIVING;
			}
		}
		return strChrDate;
	}
	
	public String getChrPlace()
	{
		String strChrPlace = getChrPlace(m_person, m_placeList);
		if (m_bSuppressLiving && m_bIsPersonLiving)
		{
			if (0 != strChrPlace.length())
			{
				return LIVING;
			}
		}
		return strChrPlace;
	}

	public String getDeathDate()
	{
		String strDeathDate = getDeathDate(m_person);
		if (m_bSuppressLiving && m_bIsPersonLiving)
		{
			if (0 != strDeathDate.length())
			{
				return LIVING;
			}
		}
		return strDeathDate;
	}
	
	public String getDeathPlace()
	{
		String strDeathPlace = getDeathPlace(m_person, m_placeList);
		if (m_bSuppressLiving && m_bIsPersonLiving)
		{
			if (0 != strDeathPlace.length())
			{
				return LIVING;
			}
		}
		return strDeathPlace;
	}
	
	public String getBurialDate()
	{
		String strBurialDate = getBurialDate(m_person);
		if (m_bSuppressLiving && m_bIsPersonLiving)
		{
			if (0 != strBurialDate.length())
			{
				return LIVING;
			}
		}
		return strBurialDate;
	}
	
	public String getBurialPlace()
	{
		String strBurialPlace = getBurialPlace(m_person, m_placeList);
		if (m_bSuppressLiving && m_bIsPersonLiving)
		{
			if (0 != strBurialPlace.length())
			{
				return LIVING;
			}
		}
		return strBurialPlace;
	}
	
	public String getBurialCemeteryName()
	{
		String strBurialCemeteryName = getBurialCemeteryName(m_person);
		if (m_bSuppressLiving && m_bIsPersonLiving)
		{
			if (0 != strBurialCemeteryName.length())
			{
				return LIVING;
			}
		}
		return strBurialCemeteryName;
	}
	
	public String getBurialCemeteryPlotAddress()
	{
		String strBurialCemeteryPlotAddress = getBurialCemeteryPlotAddress(m_person);
		if (m_bSuppressLiving && m_bIsPersonLiving)
		{
			if (0 != strBurialCemeteryPlotAddress.length())
			{
				return LIVING;
			}
		}
		return strBurialCemeteryPlotAddress;
	}
	
	public List<String> getFirstNameAltSpellings()
	{
		List<String> lNames = getFirstNameAltSpellings(m_person);
		if (m_bSuppressLiving && m_bIsPersonLiving)
		{
			ArrayList<String> alNames = new ArrayList<String>();
			for (int i=0; i<lNames.size(); i++)
			{
				alNames.add(LIVING);
			}
			return alNames;
		}
		return lNames;
	}
	
	public List<String> getMiddleNameAltSpellings()
	{
		List<String> lNames = getMiddleNameAltSpellings(m_person);
		if (m_bSuppressLiving && m_bIsPersonLiving)
		{
			ArrayList<String> alNames = new ArrayList<String>();
			for (int i=0; i<lNames.size(); i++)
			{
				alNames.add(LIVING);
			}
			return alNames;
		}
		return lNames;
	}
	
	public List<String> getLastNameAltSpellings()
	{
		List<String> lNames = getLastNameAltSpellings(m_person);
		if (m_bSuppressLiving && m_bIsPersonLiving)
		{
			ArrayList<String> alNames = new ArrayList<String>();
			for (int i=0; i<lNames.size(); i++)
			{
				alNames.add(LIVING);
			}
			return alNames;
		}
		return lNames;
	}

	public static int getPersonId(Person person)
	{
		return person.getPersonId();
	}
	
	public static List<Parents> getParents(Person person)
	{
		return person.getParents();
	}
	
	public static Parents getPreferredParents(Person person)
	{
		List<Parents> lParents = getParents(person);
		return ParentsHelper.getPreferredParents(lParents);
	}

	public static Parents getBloodParents(Person person)
	{
		List<Parents> lParents = getParents(person);
		return ParentsHelper.getBloodParents(lParents);
	}
	
	public static String getPersonName(Person person)
	{
		PersonName personName = person.getPersonName();
		if (null == personName)
		{
			return ("<No Name>");
		}
		return PersonNameHelper.getPersonName(personName);
	}
	
	public static String getAfn(Person person)
	{
		return person.getAfn();
	}
	
	public static String getGender(Person person)
	{
		return person.getGender();
	}
	
	
	public static String getFirstName(Person person)
	{
		PersonName pn = person.getPersonName();
		if (null != pn)
		{
			return PersonNameHelper.getFirstName(pn);
		}
		return "";
	}
	
	public static String getMiddleName(Person person)
	{
		PersonName pn = person.getPersonName();
		if (null != pn)
		{
			return PersonNameHelper.getMiddleName(pn);
		}
		return "";
	}
	
	public static String getLastName(Person person)
	{
		PersonName pn = person.getPersonName();
		if (null != pn)
		{
			return PersonNameHelper.getLastName(pn);
		}
		return "";
	}
	
	public static String getRelationshipToPrimary(Person person)
	{
		if (null != person)
		{
			if (null != person.getRelationshipToPrimary())
			{				
				return person.getRelationshipToPrimary();
			}
		}
		return "";
	}
	
	public static List<String> getProfessions(Person person)
	{
		if (null != person)
		{
			return person.getProfession();
		}
		return Collections.emptyList();
	}

	public static List<String> getReligions(Person person)
	{
		if (null != person)
		{
			List<String> lReligion = person.getReligion();
			if (null != lReligion)
			{
				return lReligion;
			}
		}
		return Collections.emptyList();
	}
	
	public static List<String> getNickNames(Person person)
	{
		if (null != person)
		{
			return person.getNickName();
		}
		return (new ArrayList<String>());
	}
	
	public static String getBirthDate(Person person)
	{
		if (null != person)
		{
			BirthInfo birthInfo = person.getBirthInfo();
			if (null != birthInfo)
			{
				return BirthInfoHelper.getBirthDate(birthInfo);
			}
		}
		return "";
	}
	
	public static int[] getNumericBirthDate(Person person)
	{
		if (null != person)
		{
			BirthInfo birthInfo = person.getBirthInfo();
			if (null != birthInfo)
			{
				return BirthInfoHelper.getNumericBirthDate(birthInfo);
			}
		}
		return BirthInfoHelper.getNumericBirthDate(null);
	}
	
	public static Date getObjectBirthDate(Person person)
	{
		if (null != person)
		{
			BirthInfo birthInfo = person.getBirthInfo();
			if (null != birthInfo)
			{
				return BirthInfoHelper.getObjectBirthDate(birthInfo);
			}
		}
		return null;
	}
	
	public static String getBirthPlace(Person person, PlaceList placeList)
	{
		if (null != person)
		{
			BirthInfo birthInfo = person.getBirthInfo();
			if (null != birthInfo)
			{
				return BirthInfoHelper.getBirthPlace(birthInfo, placeList);
			}
		}
		return "";
	}
	
	public static String getChrDate(Person person)
	{
		if (null != person)
		{
			ChrInfo chrInfo = person.getChrInfo();
			if (null != chrInfo)
			{
				return ChrInfoHelper.getChrDate(chrInfo);
			}
		}
		return "";
	}
	
	public static int[] getNumericChrDate(Person person)
	{
		if (null != person)
		{
			ChrInfo chrInfo = person.getChrInfo();
			if (null != chrInfo)
			{
				return ChrInfoHelper.getNumericChrDate(chrInfo);
			}
		}
		return ChrInfoHelper.getNumericChrDate(null);
	}
	
	public static Date getObjectChrDate(Person person)
	{
		if (null != person)
		{
			ChrInfo chrInfo = person.getChrInfo();
			if (null != chrInfo)
			{
				return ChrInfoHelper.getObjectChrDate(chrInfo);
			}
		}
		return null;
	}
	
	public static String getChrPlace(Person person, PlaceList placeList)
	{
		if (null != person)
		{
			ChrInfo chrInfo = person.getChrInfo();
			if (null != chrInfo)
			{
				return ChrInfoHelper.getChrPlace(chrInfo, placeList);
			}
		}
		return "";
	}
	
	public static String getDeathDate(Person person)
	{
		if (null != person)
		{
			DeathInfo deathInfo = person.getDeathInfo();
			if (null != deathInfo)
			{
				return DeathInfoHelper.getDeathDate(deathInfo);
			}
		}
		return "";
	}
	
	public static String getDeathPlace(Person person, PlaceList placeList)
	{
		if (null != person)
		{
			DeathInfo deathInfo = person.getDeathInfo();
			if (null != deathInfo)
			{
				return DeathInfoHelper.getDeathPlace(deathInfo, placeList);
			}
		}
		return "";
	}
	
	public static List<String> getDeathCause(Person person)
	{
		if (null != person)
		{
			DeathInfo deathInfo = person.getDeathInfo();
			if (null != deathInfo)
			{
				return DeathInfoHelper.getCause(deathInfo);
			}
		}
		return Collections.emptyList();
	}
	
	public static String getBurialDate(Person person)
	{
		if (null != person)
		{
			BurialInfo burialInfo = person.getBurialInfo();
			if (null != burialInfo)
			{
				return BurialInfoHelper.getBurialDate(burialInfo);
			}
		}
		return "";
	}
	
	public static String getBurialPlace(Person person, PlaceList placeList)
	{
		if (null != person)
		{
			BurialInfo burialInfo = person.getBurialInfo();
			if (null != burialInfo)
			{
				return BurialInfoHelper.getBurialPlace(burialInfo, placeList);
			}
		}
		return "";
	}	
	
	public static String getBurialCemeteryName(Person person)
	{
		if (null != person)
		{
			BurialInfo burialInfo = person.getBurialInfo();
			if (null != burialInfo)
			{
				return BurialInfoHelper.getCemeteryName(burialInfo);
			}
		}
		return "";
	}
	
	public static String getBurialCemeteryPlotAddress(Person person)
	{
		if (null != person)
		{
			BurialInfo burialInfo = person.getBurialInfo();
			if (null != burialInfo)
			{
				return BurialInfoHelper.getCemeteryPlotAddress(burialInfo);
			}
		}
		return "";
	}
	
	public static List<String> getFirstNameAltSpellings(Person person)
	{
		if (null != person)
		{
			PersonName personName = person.getPersonName();
			if (null != personName)
			{
				return PersonNameHelper.getFirstNameAltSpellings(personName);
			}
		}
		return (new ArrayList<String>());
	}
	
	public static List<String> getMiddleNameAltSpellings(Person person)
	{
		if (null != person)
		{
			PersonName personName = person.getPersonName();
			if (null != personName)
			{
				return PersonNameHelper.getMiddleNameAltSpellings(personName);
			}
		}
		return (new ArrayList<String>());
	}
	
	public static List<String> getLastNameAltSpellings(Person person)
	{
		if (null != person)
		{
			PersonName personName = person.getPersonName();
			if (null != personName)
			{
				return PersonNameHelper.getLastNameAltSpellings(personName);
			}
		}
		return (new ArrayList<String>());
	}

	public static int getEventCount(Person person)
	{
		if (null != person)
		{
			EventGroup eventGroup = person.getEventGroup();
			if (null != eventGroup)
			{
				return EventGroupHelper.getEventCount(eventGroup);
			}
		}
		return 0;
	}
	
	public static Event getEvent(Person person, int iNth)
	{
		if (null != person)
		{
			EventGroup eventGroup = person.getEventGroup();
			if (null != eventGroup)
			{
				return EventGroupHelper.getEvent(eventGroup, iNth);
			}
		}
		return null;
	}
	
	public static final int GENERATION_UNKNOWN = Integer.MIN_VALUE;
	public static final int GENERATION_OF_PRIMARY = 0;
	public static int getGenerationFromPrimary(Person person)
	{
		if (null != person)
		{
			String strRelationship = person.getRelationshipToPrimary();
			if ((null != strRelationship) && (0 != strRelationship.length()))
			{
				int iGeneration = GENERATION_OF_PRIMARY;
				for (int i=0; i<strRelationship.length(); i++)
				{
					String strLetter = new Character(strRelationship.charAt(i)).toString();
					if ((strLetter.equals(RelationshipManager.FATHER)) ||
						(strLetter.equals(RelationshipManager.MOTHER)))
					{
						iGeneration++;
					}
					else if (strLetter.equals(RelationshipManager.CHILD))
					{
						iGeneration--;
					}
					else if (strLetter.equals(RelationshipManager.UNKNOWN))
					{
						return GENERATION_UNKNOWN;
					}
				}
				return iGeneration;
			}
		}
		return GENERATION_UNKNOWN;
	}
	
	public static boolean isLiving(Person person, PlaceList placeList)
	{
		Boolean bIsLiving = person.isLiving();
		if (null != bIsLiving)
		{
			return bIsLiving.booleanValue();
		}
		// "living" attribute not found, look for death information
		if (hasAnyDeathInfo(person, placeList))
		{	// Has a death date, must be dead
			return false;
		}
		if (hasAnyBurialInfo(person, placeList))
		{	// Has a burial date, must be dead
			return false;
		}
		// No burial info, look at age.
		int iAge = getApproximateAge(person);
		if (iAge >= 110)
		{	// We assume all people older than 110 years old are dead
			return false;
		}
		// Not explicitly marked living. No death or burial info. Younger than 110 or
		// no birth/chr date information. Must be alive.
		return true;
	}
	
	public static boolean isMarkedAsLiving(Person person)
	{
		Boolean bIsLiving = person.isLiving();
		if (null != bIsLiving)
		{
			return bIsLiving.booleanValue();
        }
		return false;
	}
	
	public static boolean isMarkedAsNotLiving(Person person)
	{
		Boolean bIsLiving = person.isLiving();
		if (null != bIsLiving)
		{
			return !bIsLiving.booleanValue();
        }
		return false;
	}
	
	public static boolean isLivingIsNotSpecified(Person person)
	{
		Boolean bIsLiving = person.isLiving();
		if (null == bIsLiving)
		{
			return true;
        }
		return false;
	}
	
	public static boolean hasAnyDeathInfo(Person person, PlaceList placeList)
	{
		String strDeathDate = PersonHelper.getDeathDate(person);
		if (0 != strDeathDate.length())
		{	// Has a death date
			return true;
		}
		String strDeathPlace = PersonHelper.getDeathPlace(person, placeList);
		if (0 != strDeathPlace.length())
		{	// Has a death place
			return true;
		}
		List<String> lDeathCause = PersonHelper.getDeathCause(person);
		if (!lDeathCause.isEmpty())
		{	// Has a death cause
			return true;
		}
		return false;
	}
	
	public static boolean hasAnyBurialInfo(Person person, PlaceList placeList)
	{
		String strBurialDate = PersonHelper.getBurialDate(person);
		if (0 != strBurialDate.length())
		{	// Has a burial date
			return true;
		}
		String strBurialPlace = PersonHelper.getBurialPlace(person, placeList);
		if (0 != strBurialPlace.length())
		{	// Has a burial place
			return true;
		}
		String strCemeteryName = PersonHelper.getBurialCemeteryName(person);
		if (0 != strCemeteryName.length())
		{	// Has a burial cemetery
			return true;
		}
		String getCemeteryPlotAddress = PersonHelper.getBurialCemeteryPlotAddress(person);
		if (0 != getCemeteryPlotAddress.length())
		{	// Has a burial cemetery plot address
			return true;
		}
		return false;
	}
	
	// Returns a negative value if the age cannot be determined
	public static int getApproximateAge(Person person)
	{
		// Try to get age from birth date
		Calendar currentTime = Calendar.getInstance();
		int iCurrentYear = currentTime.get(Calendar.YEAR);
		int[] arBirthDate = PersonHelper.getNumericBirthDate(person);
		if (arBirthDate[DateHelper.YEAR_IDX] != DateHelper.DATE_YEAR_UNKNOWN)
		{
			return (iCurrentYear - arBirthDate[DateHelper.YEAR_IDX]);
		}
		int[] arChrDate = PersonHelper.getNumericChrDate(person);
		if (arChrDate[DateHelper.YEAR_IDX] != DateHelper.DATE_YEAR_UNKNOWN)
		{
			return(iCurrentYear - arChrDate[DateHelper.YEAR_IDX]);
		}
		return -1;
	}
	
	public static Set<String> getAllPlaceIds(Person person)
	{
		Set<String> hResults = new HashSet<String>();
		if (null != person)
		{
			if ((null != person.getBirthInfo()) &&
				(null != person.getBirthInfo().getInfo()) &&
				(null != person.getBirthInfo().getInfo().getPlace()))
			{
				hResults.add(person.getBirthInfo().getInfo().getPlace().getIdRef());
			}
			if ((null != person.getChrInfo()) &&
				(null != person.getChrInfo().getInfo()) &&
				(null != person.getChrInfo().getInfo().getPlace()))
			{
				hResults.add(person.getChrInfo().getInfo().getPlace().getIdRef());
			}
			if ((null != person.getDeathInfo()) &&
				(null != person.getDeathInfo().getInfo()) &&
				(null != person.getDeathInfo().getInfo().getPlace()))
			{
				hResults.add(person.getDeathInfo().getInfo().getPlace().getIdRef());
			}
			if ((null != person.getBurialInfo()) &&
				(null != person.getBurialInfo().getInfo()) &&
				(null != person.getBurialInfo().getInfo().getPlace()))
			{
				hResults.add(person.getBurialInfo().getInfo().getPlace().getIdRef());
			}
			int iEventCount = getEventCount(person);
			for (int i=0; i<iEventCount; i++)
			{
				Event eventCandidate = PersonHelper.getEvent(person, i);
				Place placeCandidate = eventCandidate.getPlace();
				if (null != placeCandidate)
				{
					hResults.add(placeCandidate.getIdRef());
				}
			}
		}
		return hResults;
	}
	
	public static boolean usesPlace(Person person, PlaceList placeList, String strPlaceId)
	{
		Set<String> sAllPlacedIds = PersonHelper.getAllPlaceIds(person);
		return sAllPlacedIds.contains(strPlaceId);
	}

	public static int replacePlaceId(Person person,
			String strToBeReplaced,
			String strReplacement,
			IOutputStream outputStream)
	{
		return replacePlaceId(person,
							strToBeReplaced,
							strReplacement,
							(String)null,
							(String)null,
							(String)null,
							outputStream);
	}
	
	public static int replacePlaceId(Person person,
									String strToBeReplaced,
									String strReplacement,
									String strLocale,
									String strStreet,
									String strSpot,
									IOutputStream outputStream)
	{
		int iCount = 0;
		if (null != person)
		{
			if ((null != person.getBirthInfo()) &&
				(null != person.getBirthInfo().getInfo()) &&
				(null != person.getBirthInfo().getInfo().getPlace()))
			{
				if (person.getBirthInfo().getInfo().getPlace().getIdRef().equals(strToBeReplaced))
				{
					outputStream.output("  PersonList: Place Id Replace: Person Id: " + person.getPersonId() + ", Replacing Birth Place: " + person.getBirthInfo().getInfo().getPlace().getIdRef() + " with " + strReplacement + "\n");
					PlaceHelper.setPlaceData(person.getBirthInfo().getInfo().getPlace(), strReplacement, strLocale, strStreet, strSpot);
					iCount++;
				}
			}
			if ((null != person.getChrInfo()) &&
				(null != person.getChrInfo().getInfo()) &&
				(null != person.getChrInfo().getInfo().getPlace()))
			{
				if (person.getChrInfo().getInfo().getPlace().getIdRef().equals(strToBeReplaced))
				{
					outputStream.output("  PersonList: Place Id Replace: Person Id: " + person.getPersonId() + ", Replacing Chr Place: " + person.getChrInfo().getInfo().getPlace().getIdRef() + " with " + strReplacement + "\n");
					PlaceHelper.setPlaceData(person.getChrInfo().getInfo().getPlace(), strReplacement, strLocale, strStreet, strSpot);
					iCount++;
				}
			}
			if ((null != person.getDeathInfo()) &&
				(null != person.getDeathInfo().getInfo()) &&
				(null != person.getDeathInfo().getInfo().getPlace()))
			{
				if (person.getDeathInfo().getInfo().getPlace().getIdRef().equals(strToBeReplaced))
				{
					outputStream.output("  PersonList: Place Id Replace: Person Id: " + person.getPersonId() + ", Replacing Death Place: " + person.getDeathInfo().getInfo().getPlace().getIdRef() + " with " + strReplacement + "\n");
					PlaceHelper.setPlaceData(person.getDeathInfo().getInfo().getPlace(), strReplacement, strLocale, strStreet, strSpot);
					iCount++;
				}
			}
			if ((null != person.getBurialInfo()) &&
				(null != person.getBurialInfo().getInfo()) &&
				(null != person.getBurialInfo().getInfo().getPlace()))
			{
				if (person.getBurialInfo().getInfo().getPlace().getIdRef().equals(strToBeReplaced))
				{
					outputStream.output("  PersonList: Place Id Replace: Person Id: " + person.getPersonId() + ", Replacing Burial Place: " + person.getBurialInfo().getInfo().getPlace().getIdRef() + " with " + strReplacement + "\n");
					PlaceHelper.setPlaceData(person.getBurialInfo().getInfo().getPlace(), strReplacement, strLocale, strStreet, strSpot);
					iCount++;
				}
			}
			int iEventCount = getEventCount(person);
			for (int i=0; i<iEventCount; i++)
			{
				Event eventCandidate = PersonHelper.getEvent(person, i);
				Place placeCandidate = eventCandidate.getPlace();
				if (null != placeCandidate)
				{
					if (placeCandidate.getIdRef().equals(strToBeReplaced))
					{
						outputStream.output("  PersonList: Place Id Replace: Person Id: " + person.getPersonId() + ", Replacing Event Place: " + placeCandidate.getIdRef() + " with " + strReplacement + "\n");
						PlaceHelper.setPlaceData(placeCandidate, strReplacement, strLocale, strStreet, strSpot);
						iCount++;
					}
				}
			}
		}
		return iCount;
	}
	

}
