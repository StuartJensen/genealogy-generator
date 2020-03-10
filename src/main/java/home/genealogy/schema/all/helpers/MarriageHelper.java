package home.genealogy.schema.all.helpers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import home.genealogy.lists.PersonList;
import home.genealogy.lists.PlaceList;
import home.genealogy.output.IOutputStream;
import home.genealogy.schema.all.Entry;
import home.genealogy.schema.all.Event;
import home.genealogy.schema.all.Info;
import home.genealogy.schema.all.Marriage;
import home.genealogy.schema.all.MarriageInfo;
import home.genealogy.schema.all.Person;
import home.genealogy.schema.all.Place;
import home.genealogy.schema.all.Reference;
import home.genealogy.schema.all.SealToSpouseInfo;
import home.genealogy.util.StringUtil;

public class MarriageHelper
{
	private Marriage m_marriage;
	private PersonHelper m_husband;
	private PersonHelper m_wife;
	private boolean m_bSuppressLiving;
	private PlaceList m_placeList;
	
	public MarriageHelper(Marriage marriage, PersonHelper husband, PersonHelper wife, boolean bSuppressLiving, PlaceList placeList)
	{
		m_marriage = marriage;
		m_husband = husband;
		m_wife = wife;
		m_bSuppressLiving = bSuppressLiving;
		m_placeList = placeList;
	}
	
	public String getMarriageName(PersonList personList)
	{
		return getHusbandName(personList) + " and " + getWifeName(personList);
	}
	
	public String getHusbandName(PersonList personList)
	{
		String strHusbandName = "[Unavailable]";
		if (null != m_marriage)
		{
			int iHusbandId = getHusbandPersonId(m_marriage);
			if (iHusbandId != MarriageIdHelper.MARRIAGEID_INVALID)
			{
				strHusbandName = PersonHelper.LIVING;
				Person husband = personList.get(iHusbandId);
				if (null != husband && !m_bSuppressLiving)
				{
					strHusbandName = PersonHelper.getPersonName(husband);
				}
			}			
		}
		return strHusbandName;
	}
	
	public String getWifeName(PersonList personList)
	{
		String strWifeName = "[Unavailable]";
		if (null != m_marriage)
		{
			int iWifeId = getWifePersonId(m_marriage);
			if (iWifeId != MarriageIdHelper.MARRIAGEID_INVALID)
			{
				strWifeName = PersonHelper.LIVING;
				Person wife = personList.get(iWifeId);
				if (null != wife && !m_bSuppressLiving)
				{
					strWifeName = PersonHelper.getPersonName(wife);
				}
			}			
		}
		return strWifeName;
	}
	
	public String getDate()
	{
		String strDate = getDate(m_marriage);
		if (m_bSuppressLiving && (m_husband.getIsPersonLiving() || m_wife.getIsPersonLiving()))
		{
			if (0 != strDate.length())
			{
				return PersonHelper.LIVING;
			}
		}
		return strDate;
	}
	
	public String getPlace()
	{
		String strPlace = getPlace(m_marriage, m_placeList);
		if (m_bSuppressLiving && (m_husband.getIsPersonLiving() || m_wife.getIsPersonLiving()))
		{
			if (0 != strPlace.length())
			{
				return PersonHelper.LIVING;
			}
		}
		return strPlace;
	}
	
	public static int getMarriageId(Marriage marriage)
	{
		return marriage.getMarriageId();
	}
	
	public static int getHusbandPersonId(Marriage marriage)
	{
		return marriage.getHusbandPersonId();
	}
	
	public static int getWifePersonId(Marriage marriage)
	{
		return marriage.getWifePersonId();
	}
	
	
	public static String getDate(Marriage marriage)
	{
		if (null != marriage)
		{
			MarriageInfo marriageInfo = marriage.getMarriageInfo();
			if (null != marriageInfo)
			{
				return MarriageInfoHelper.getDate(marriageInfo);
			}
		}
		return "";
	}
	
	public static String getPlace(Marriage marriage, PlaceList placeList)
	{
		if (null != marriage)
		{
			MarriageInfo marriageInfo = marriage.getMarriageInfo();
			if (null != marriageInfo)
			{
				return MarriageInfoHelper.getPlace(marriageInfo, placeList);
			}
		}
		return "";
	}
		
	public static boolean isInLine(Marriage marriage)
	{
		if (null != marriage)
		{
			String strInLine = marriage.getInLine();
			if (null != strInLine)
			{
				if (strInLine.equalsIgnoreCase("true"))
				{
					return true;
				}
			}
		}
		return false;
	}
	
	public static Set<String> getAllPlaceIds(Marriage marriage)
	{
		Set<String> hResults = new HashSet<String>();
		if (null != marriage)
		{
			if ((null != marriage.getMarriageInfo()) &&
				(null != marriage.getMarriageInfo().getInfo()) &&
				(null != marriage.getMarriageInfo().getInfo().getPlace()))
			{
				hResults.add(marriage.getMarriageInfo().getInfo().getPlace().getIdRef());
			}
		}
		return hResults;
	}

	public static boolean usesPlace(Marriage marriage, PlaceList placeList, String strPlaceId)
	{
		Set<String> sAllPlaceIds = MarriageHelper.getAllPlaceIds(marriage);
		return sAllPlaceIds.contains(strPlaceId);
	}
	
	public static int replacePlaceId(Marriage marriage, String strToBeReplaced, String strReplacement, IOutputStream outputStream)
	{
		int iCount = 0;
		if (null != marriage)
		{
			if ((null != marriage.getMarriageInfo()) &&
				(null != marriage.getMarriageInfo().getInfo()) &&
				(null != marriage.getMarriageInfo().getInfo().getPlace()))
			{
				if (marriage.getMarriageInfo().getInfo().getPlace().getIdRef().equals(strToBeReplaced))
				{
					outputStream.output("  MarriageList: Place Id Replace: Marriage Id: " + marriage.getMarriageId() + ", Replacing Marriage Place: " + marriage.getMarriageInfo().getInfo().getPlace().getIdRef() + " with " + strReplacement + "\n");
					marriage.getMarriageInfo().getInfo().getPlace().setIdRef(strReplacement);
					iCount++;
				}
			}
		}
		return iCount;
	}

}
