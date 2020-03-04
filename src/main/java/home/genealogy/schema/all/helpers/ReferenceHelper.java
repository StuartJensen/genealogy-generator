package home.genealogy.schema.all.helpers;

import java.util.ArrayList;
import java.util.List;

import home.genealogy.lists.PlaceList;
import home.genealogy.schema.all.Authority;
import home.genealogy.schema.all.Citation;
import home.genealogy.schema.all.Classification;
import home.genealogy.schema.all.DateRange;
import home.genealogy.schema.all.Entry;
import home.genealogy.schema.all.Location;
import home.genealogy.schema.all.Place;
import home.genealogy.schema.all.Reference;
import home.genealogy.schema.all.Title;

public class ReferenceHelper
{
	public static int getReferenceId(Reference reference)
	{
		String strReferenceId = reference.getReferenceId();
		if (null != strReferenceId)
		{
			try
			{
				return Integer.parseInt(strReferenceId);
			}
			catch (Exception e)
			{/* return invalid id */}
		}
		return ReferenceIdHelper.REFERENCEID_INVALID;
	}
	
	public static String getTitle(Reference reference)
	{
		if (null != reference)
		{
			Citation citation = reference.getCitation();
			if (null != citation)
			{
				return CitationHelper.getTitle(citation);
			}
		}
		return "";
	}
	
	public static String getCitationPlace(Reference reference, PlaceList placeList)
	{
		if (null != reference)
		{
			Citation citation = reference.getCitation();
			if (null != citation)
			{
				return CitationHelper.getPlace(citation, placeList);
			}
		}
		return "";
	}
	
	public static int getCitationDateRangeCount(Reference reference)
	{
		if (null != reference)
		{
			Citation citation = reference.getCitation();
			if (null != citation)
			{
				return CitationHelper.getDateRangeCount(citation);
			}
		}
		return 0;
	}
	
	public static DateRange getCitationDateRange(Reference reference, int iNth)
	{
		if (null != reference)
		{
			Citation citation = reference.getCitation();
			if (null != citation)
			{
				return CitationHelper.getDateRange(citation, iNth);
			}
		}
		return null;
	}
	
	public static List<String> getPersonAuthorNames(Reference reference)
	{
		if (null != reference)
		{
			Citation citation = reference.getCitation();
			if (null != citation)
			{
				return CitationHelper.getPersonAuthorNames(citation);
			}
		}
		return new ArrayList<String>();
	}
	
	public static List<String> getGroupAuthorNames(Reference reference)
	{
		if (null != reference)
		{
			Citation citation = reference.getCitation();
			if (null != citation)
			{
				return CitationHelper.getGroupAuthorNames(citation);
			}
		}
		return new ArrayList<String>();
	}
	
	public static int getLocationCount(Reference reference)
	{
		if (null != reference)
		{
			Citation citation = reference.getCitation();
			if (null != citation)
			{
				return CitationHelper.getLocationCount(citation);
			}
		}
		return 0;
	}
	
	public static Location getLocation(Reference reference, int iNth)
	{
		if (null != reference)
		{
			Citation citation = reference.getCitation();
			if (null != citation)
			{
				return CitationHelper.getLocation(citation, iNth);
			}
		}
		return null;
	}
	
	public static int getClassificationCount(Reference reference)
	{
		if (null != reference)
		{
			Citation citation = reference.getCitation();
			if (null != citation)
			{
				return CitationHelper.getClassificationCount(citation);
			}
		}
		return 0;
	}
	
	public static Classification getClassification(Reference reference, int iNth)
	{
		if (null != reference)
		{
			Citation citation = reference.getCitation();
			if (null != citation)
			{
				return CitationHelper.getClassification(citation, iNth);
			}
		}
		return null;
	}
	
	public static int getEntryCount(Reference reference)
	{
		if (null != reference)
		{
			List<Entry> lEntries = reference.getEntry();
			if (null != lEntries)
			{
				return lEntries.size();
			}
		}
		return 0;
	}
	
    public static Entry getEntry(Reference reference, int iNth)
    {
		if (null != reference)
		{
			List<Entry> lEntries = reference.getEntry();
			if (null != lEntries)
			{			
				return lEntries.get(iNth);
			}
		}
		return null;
    }
}
