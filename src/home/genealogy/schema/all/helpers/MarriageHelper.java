package home.genealogy.schema.all.helpers;

import java.util.ArrayList;
import java.util.List;

import home.genealogy.lists.PersonList;
import home.genealogy.schema.all.Info;
import home.genealogy.schema.all.Marriage;
import home.genealogy.schema.all.MarriageInfo;
import home.genealogy.schema.all.Person;
import home.genealogy.schema.all.SealToSpouseInfo;

public class MarriageHelper
{
	private Marriage m_marriage;
	private PersonHelper m_husband;
	private PersonHelper m_wife;
	private boolean m_bSuppressLiving;
	
	public MarriageHelper(Marriage marriage, PersonHelper husband, PersonHelper wife, boolean bSuppressLiving)
	{
		m_marriage = marriage;
		m_husband = husband;
		m_wife = wife;
		m_bSuppressLiving = bSuppressLiving;
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
		String strPlace = getPlace(m_marriage);
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
		String strMarriageId = marriage.getMarriageId();
		if (null != strMarriageId)
		{
			try
			{
				return Integer.parseInt(strMarriageId);
			}
			catch (Exception e)
			{/* return invalid id */}
		}
		return MarriageIdHelper.MARRIAGEID_INVALID;
	}
	
	public static int getHusbandPersonId(Marriage marriage)
	{
		String strPersonId = marriage.getHusbandPersonId();
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
	
	public static int getWifePersonId(Marriage marriage)
	{
		String strPersonId = marriage.getWifePersonId();
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
	
	public static String getPlace(Marriage marriage)
	{
		if (null != marriage)
		{
			MarriageInfo marriageInfo = marriage.getMarriageInfo();
			if (null != marriageInfo)
			{
				return MarriageInfoHelper.getPlace(marriageInfo);
			}
		}
		return "";
	}
	
	public static String getSealToSpouseDate(Marriage marriage)
	{
		if (null != marriage)
		{
			MarriageInfo marriageInfo = marriage.getMarriageInfo();
			if (null != marriageInfo)
			{
				return MarriageInfoHelper.getSealToSpouseDate(marriageInfo);
			}
		}
		return "";
	}
	
	public static String getSealToSpousePlace(Marriage marriage)
	{
		if (null != marriage)
		{
			MarriageInfo marriageInfo = marriage.getMarriageInfo();
			if (null != marriageInfo)
			{
				return MarriageInfoHelper.getSealToSpousePlace(marriageInfo);
			}
		}
		return "";
	}
	
	public static List<String> getSealToSpouseProxies(Marriage marriage)
	{
		if (null != marriage)
		{
			MarriageInfo marriageInfo = marriage.getMarriageInfo();
			if (null != marriageInfo)
			{
				return MarriageInfoHelper.getSealToSpouseProxies(marriageInfo);
			}
		}
		return new ArrayList<String>();
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
	
}
