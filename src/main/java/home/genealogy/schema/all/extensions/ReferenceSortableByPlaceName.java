package home.genealogy.schema.all.extensions;

import home.genealogy.lists.PlaceList;
import home.genealogy.schema.all.DateRange;
import home.genealogy.schema.all.Reference;
import home.genealogy.schema.all.helpers.DateHelper;
import home.genealogy.schema.all.helpers.DateRangeHelper;
import home.genealogy.schema.all.helpers.ReferenceHelper;

public class ReferenceSortableByPlaceName implements Comparable<ReferenceSortableByPlaceName>
{
	private Reference m_reference;
	private PlaceList m_placeList;
	
	public ReferenceSortableByPlaceName(Reference reference, PlaceList placeList)
	{
		m_reference = reference;
		m_placeList = placeList;
	}
	
	public Reference getReference()
	{
		return m_reference;
	}
	
	public int compareTo(ReferenceSortableByPlaceName that)
	{
		String strThisPlace = ReferenceHelper.getCitationPlace(m_reference, m_placeList);
		String strThatPlace = ReferenceHelper.getCitationPlace(that.getReference(), m_placeList);
		int iReturn = strThisPlace.compareTo(strThatPlace);
		if (0 != iReturn)
		{	// Place names are not equal
			return iReturn;
		}
		// If place names are equal, then sort by data oldest to youngest
		//
		// Find oldest date for this reference
		int iThisEndYear = DateHelper.DATE_YEAR_UNKNOWN;
		int iCount = ReferenceHelper.getCitationDateRangeCount(m_reference);
		for (int i=0; i<iCount; i++)
		{
			DateRange dateRange = ReferenceHelper.getCitationDateRange(m_reference, i);
			int iEndYear = DateRangeHelper.getEndYear(dateRange);
			if ((DateHelper.DATE_YEAR_UNKNOWN == iThisEndYear) ||
				(iEndYear < iThisEndYear))
			{
				iThisEndYear = iEndYear;
			}
		}
		// Find oldest date for that reference
		int iThatEndYear = DateHelper.DATE_YEAR_UNKNOWN;
		iCount = ReferenceHelper.getCitationDateRangeCount(that.getReference());
		for (int i=0; i<iCount; i++)
		{
			DateRange dateRange = ReferenceHelper.getCitationDateRange(that.getReference(), i);
			int iEndYear = DateRangeHelper.getEndYear(dateRange);
			if ((DateHelper.DATE_YEAR_UNKNOWN == iThatEndYear) ||
				(iEndYear < iThatEndYear))
			{
				iThatEndYear = iEndYear;
			}
		}
		
		// Now compare the end years, oldest sorts first
		if (iThatEndYear == iThisEndYear)
		{
			return 0;
		}
		if (iThatEndYear == DateHelper.DATE_YEAR_UNKNOWN)
		{
			return 1;
		}
		if (iThisEndYear == DateHelper.DATE_YEAR_UNKNOWN)
		{
			return -1;
		}
		// Both are valid years
		if (iThatEndYear < iThisEndYear)
		{
			return 1;
		}
		// iThatEndYear must be > iThisEndYear
		return -1;
	}
}
