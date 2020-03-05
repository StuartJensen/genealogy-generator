package home.genealogy.schema.all.helpers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import home.genealogy.lists.PlaceList;
import home.genealogy.lists.RelationshipManager;
import home.genealogy.schema.all.BirthInfo;
import home.genealogy.schema.all.BurialInfo;
import home.genealogy.schema.all.ChrInfo;
import home.genealogy.schema.all.Date;
import home.genealogy.schema.all.DeathInfo;
import home.genealogy.schema.all.Event;
import home.genealogy.schema.all.EventGroup;
import home.genealogy.schema.all.Person;
import home.genealogy.schema.all.PersonName;

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
	
	public int getParentId()
	{
		return getParentId(m_person);
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
		String strPersonId = person.getPersonId();
		if (null != strPersonId)
		{
			try
			{
				return Integer.parseInt(strPersonId);
			}
			catch (Exception e)
			{/* return invalid id */}
		}
		return PersonIdHelper.PERSONID_INVALID;
	}
	
	public static int getParentId(Person person)
	{
		String strParentId = person.getParentId();
		if (null != strParentId)
		{
			try
			{
				return Integer.parseInt(strParentId);
			}
			catch (Exception e)
			{/* return invalid id */}
		}
		return MarriageIdHelper.MARRIAGEID_INVALID;
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
		String strLiving = person.getLiving();
		if (null != strLiving)
		{
			if ((strLiving.equalsIgnoreCase("yes")) ||
				(strLiving.equalsIgnoreCase("true")))
			{
				return true;
			}
			if ((strLiving.equalsIgnoreCase("no")) ||
				(strLiving.equalsIgnoreCase("false")))
			{
				return false;
			}
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
		String strLiving = person.getLiving();
		if (null != strLiving)
		{
			if ((strLiving.equalsIgnoreCase("yes")) ||
				(strLiving.equalsIgnoreCase("true")))
			{
				return true;
			}
        }
		return false;
	}
	
	public static boolean isMarkedAsNotLiving(Person person)
	{
		String strLiving = person.getLiving();
		if (null != strLiving)
		{
			if ((strLiving.equalsIgnoreCase("no")) ||
				(strLiving.equalsIgnoreCase("false")))
			{
				return true;
			}
        }
		return false;
	}
	
	public static boolean isLivingIsNotSpecified(Person person)
	{
		String strLiving = person.getLiving();
		if ((null == strLiving) || (0 == strLiving.length()))
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
}
