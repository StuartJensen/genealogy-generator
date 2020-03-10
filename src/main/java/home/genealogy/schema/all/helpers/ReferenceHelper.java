package home.genealogy.schema.all.helpers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import home.genealogy.lists.PlaceList;
import home.genealogy.output.IOutputStream;
import home.genealogy.schema.all.Authority;
import home.genealogy.schema.all.Citation;
import home.genealogy.schema.all.Classification;
import home.genealogy.schema.all.DateRange;
import home.genealogy.schema.all.Entry;
import home.genealogy.schema.all.Event;
import home.genealogy.schema.all.Location;
import home.genealogy.schema.all.Marriage;
import home.genealogy.schema.all.Person;
import home.genealogy.schema.all.Photo;
import home.genealogy.schema.all.Place;
import home.genealogy.schema.all.Reference;
import home.genealogy.schema.all.Title;
import home.genealogy.util.StringUtil;

public class ReferenceHelper
{
	public static int getReferenceId(Reference reference)
	{
		return reference.getReferenceId();
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

	public static Set<String> getAllPlaceIds(Reference reference)
	{
		Set<String> hResults = new HashSet<String>();
		if (null != reference)
		{
			if ((null != reference.getCitation()) &&
				(null != reference.getCitation().getPlace()))
			{
				hResults.add(reference.getCitation().getPlace().getIdRef());
			}
		}
		int iEntryCount = getEntryCount(reference);
		for (int i=0; i<iEntryCount; i++)
		{
			Entry entryCandidate = ReferenceHelper.getEntry(reference, i);
			List<Place> lPlaces = entryCandidate.getPlace();
			if (null != lPlaces)
			{
				for (Place place : lPlaces)
				{
					hResults.add(place.getIdRef());
				}
			}
		}
		return hResults;
	}
	
	public static boolean usesPlace(Reference reference, PlaceList placeList, String strPlaceId)
	{
		Set<String> sAllPlaceIds = ReferenceHelper.getAllPlaceIds(reference);
		return sAllPlaceIds.contains(strPlaceId);
	}
	
	public static int replacePlaceId(Reference reference, String strToBeReplaced, String strReplacement, IOutputStream outputStream)
	{
		int iCount = 0;
		if (null != reference)
		{
			if ((null != reference.getCitation()) &&
				(null != reference.getCitation().getPlace()))
			{
				if (reference.getCitation().getPlace().getIdRef().equals(strToBeReplaced))
				{
					outputStream.output("  ReferenceList: Place Id Replace: Reference Id: " + reference.getReferenceId() + ", Replacing Reference Citation Place: " + reference.getCitation().getPlace().getIdRef() + " with " + strReplacement + "\n");
					reference.getCitation().getPlace().setIdRef(strReplacement);
					iCount++;
				}
			}
			
			int iEntryCount = getEntryCount(reference);
			for (int i=0; i<iEntryCount; i++)
			{
				Entry entryCandidate = ReferenceHelper.getEntry(reference, i);
				List<Place> lPlaces = entryCandidate.getPlace();
				if (null != lPlaces)
				{
					for (Place place : lPlaces)
					{
						if (place.getIdRef().equals(strToBeReplaced))
						{
							outputStream.output("  ReferenceList: Place Id Replace: Reference Id: " + reference.getReferenceId() + ", Entry: " + entryCandidate.getEntryId() + " Replacing Reference Entry Place: " + place.getIdRef() + " with " + strReplacement + "\n");
							place.setIdRef(strReplacement);
							iCount++;
						}
					}
				}
			}
		}
		return iCount;
	}
}
