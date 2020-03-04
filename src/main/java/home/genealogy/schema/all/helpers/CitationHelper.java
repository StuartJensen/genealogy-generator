package home.genealogy.schema.all.helpers;

import home.genealogy.lists.PlaceList;
import home.genealogy.schema.all.Authority;
import home.genealogy.schema.all.Citation;
import home.genealogy.schema.all.Classification;
import home.genealogy.schema.all.DateRange;
import home.genealogy.schema.all.Location;
import home.genealogy.schema.all.Place;

import java.util.ArrayList;
import java.util.List;

public class CitationHelper
{
	public static String getTitle(Citation citation)
	{
		if (null != citation)
		{
			Authority authority = citation.getAuthority();
			if (null != authority)
			{
				return AuthorityHelper.getTitle(authority);
			}
		}
		return "";
	}
	
	public static String getPlace(Citation citation, PlaceList placeList)
	{
		if (null != citation)
		{
			Place place = citation.getPlace();
			if (null != place)
			{
				return PlaceHelper.getPlace(place, placeList);
			}
		}
		return "";
	}
	
	public static List<String> getPersonAuthorNames(Citation citation)
	{
		if (null != citation)
		{
			Authority authority = citation.getAuthority();
			if (null != authority)
			{
				return AuthorityHelper.getPersonAuthorNames(authority);
			}
		}
		return new ArrayList<String>();
	}
	
	public static List<String> getGroupAuthorNames(Citation citation)
	{
		if (null != citation)
		{
			Authority authority = citation.getAuthority();
			if (null != authority)
			{
				return AuthorityHelper.getGroupAuthorNames(authority);
			}
		}
		return new ArrayList<String>();
	}
	
	public static int getLocationCount(Citation citation)
	{
		if (null != citation)
		{
			Authority authority = citation.getAuthority();
			if (null != authority)
			{
				return AuthorityHelper.getLocationCount(authority);
			}
		}
		return 0;
	}
	
	public static Location getLocation(Citation citation, int iNth)
	{
		if (null != citation)
		{
			Authority authority = citation.getAuthority();
			if (null != authority)
			{
				return AuthorityHelper.getLocation(authority, iNth);
			}
		}
		return null;
	}
	
	public static int getClassificationCount(Citation citation)
	{
		if (null != citation)
		{
			List<Classification> lClassification = citation.getClassification();
			if (null != lClassification)
			{
				return lClassification.size();
			}
		}
		return 0;
	}
	
	public static Classification getClassification(Citation citation, int iNth)
	{
		if (null != citation)
		{
			List<Classification> lClassification = citation.getClassification();
			if ((null != lClassification) && (iNth < lClassification.size()))
			{
				return lClassification.get(iNth);
			}
		}
		return null;
	}
	
	public static int getDateRangeCount(Citation citation)
	{
		if (null != citation)
		{
			List<DateRange> lDateRange = citation.getDateRange();
			if (null != lDateRange)
			{
				return lDateRange.size();
			}
		}
		return 0;
	}
	
	public static DateRange getDateRange(Citation citation, int iNth)
	{
		if (null != citation)
		{
			List<DateRange> lDateRange = citation.getDateRange();
			if ((null != lDateRange) && (iNth < lDateRange.size()))
			{
				return lDateRange.get(iNth);
			}
		}
		return null;
	}
}
