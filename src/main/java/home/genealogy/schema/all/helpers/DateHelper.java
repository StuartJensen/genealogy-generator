package home.genealogy.schema.all.helpers;

import home.genealogy.schema.all.Date;
import home.genealogy.schema.all.DateOr;
import home.genealogy.schema.all.DateRange;

import java.util.List;

public class DateHelper
{
	public static String getDay(Date date)
	{
		if (null != date)
		{
			String strDay = date.getDay();
			if (null != strDay)
			{
				return strDay;
			}
		}
		return "";
	}
	
	public static String getMonth(Date date)
	{
		if (null != date)
		{
			String strMonth = date.getMonth();
			if (null != strMonth)
			{
				return strMonth;
			}
		}
		return "";
	}
	
	public static String getYear(Date date)
	{
		if (null != date)
		{
			String strYear = date.getYear();
			if (null != strYear)
			{
				return strYear;
			}
		}
		return "";
	}
	
	public static final int DATE_DAY_UNKNOWN = -1; 
	public static int getNumericDay(Date date)
	{
		String strDay = getDay(date);
		if ((null != strDay) && (0 != strDay.length()))
		{
			try
			{
				int iDay = Integer.parseInt(strDay);
				return iDay;
			}
			catch (NumberFormatException nfe)
			{
				System.out.println("Invalid Date \"Day\" Format: " + strDay);
			}
		}
		return DATE_DAY_UNKNOWN;
	}
	
	public static final int DATE_MONTH_UNKNOWN = -1; 
	public static int getNumericMonth(Date date)
	{
		String strMonth = getMonth(date);
		if ((null != strMonth) && (0 != strMonth.length()))
		{
			if (strMonth.equals("January"))
			{
				return 1;
			}
			if (strMonth.equals("February"))
			{
				return 2;
			}
			if (strMonth.equals("March"))
			{
				return 3;
			}
			if (strMonth.equals("April"))
			{
				return 4;
			}
			if (strMonth.equals("May"))
			{
				return 5;
			}
			if (strMonth.equals("June"))
			{
				return 6;
			}
			if (strMonth.equals("July"))
			{
				return 7;
			}
			if (strMonth.equals("August"))
			{
				return 8;
			}
			if (strMonth.equals("September"))
			{
				return 9;
			}
			if (strMonth.equals("October"))
			{
				return 10;
			}
			if (strMonth.equals("November"))
			{
				return 11;
			}
			if (strMonth.equals("December"))
			{
				return 12;
			}
			System.out.println("Invalid Date \"Month\" Format: " + strMonth);
		}
		return DATE_MONTH_UNKNOWN;
	}
	
	public static final int DATE_YEAR_UNKNOWN = -1; 
	public static int getNumericYear(Date date)
	{
		String strYear = getYear(date);
		if ((null != strYear) && (0 != strYear.length()))
		{
			try
			{
				int iYear = Integer.parseInt(strYear);
				return iYear;
			}
			catch (NumberFormatException nfe)
			{
				System.out.println("Invalid Date \"Year\" Format: " + strYear);
			}
		}
		return DATE_YEAR_UNKNOWN;
	}
	
	public static final int DAY_IDX = 0;
	public static final int MONTH_IDX = 1;
	public static final int YEAR_IDX = 2;
	public static int[] getNumericDate(Date date)
	{
		int[] arResult = new int[]{DateHelper.DATE_DAY_UNKNOWN,
				                   DateHelper.DATE_MONTH_UNKNOWN,
				                   DateHelper.DATE_YEAR_UNKNOWN};
		if (null != date)
		{
			arResult[DAY_IDX] = DateHelper.getNumericDay(date);
			arResult[MONTH_IDX] = DateHelper.getNumericMonth(date);
			arResult[YEAR_IDX] = DateHelper.getNumericYear(date);
		}
		return arResult;
	}
	
	public static String getDate(Date date)
	{
		if (null == date)
		{
			return "";
		}
		String strDate = date.getLdsModifiers();
		if (null == strDate)
		{
			strDate = "";
		}
		String strRelativeTime = date.getRelativeTime();
		String strYear = date.getYear();
		String strMonth = date.getMonth();
		String strDay = date.getDay();
		if ((null != strRelativeTime) && (0 != strRelativeTime.length()))
		{
			if (0 != strDate.length())
			{
				strDate += ", ";
			}
			strDate = strRelativeTime;
		}
		if ((null != strYear) && (0 != strYear.length()))
		{
			if (0 != strDate.length())
			{
				strDate += ", ";
			}
			strDate += strYear;
		}
		if ((null != strMonth) && (0 != strMonth.length()))
		{
			if (0 != strDate.length())
			{
				strDate += ", ";
			}
			strDate += strMonth;
		}
		if ((null != strDay) && (0 != strDay.length()))
		{
			if (0 != strDate.length())
			{
				strDate += " ";
			}
			strDate += strDay;
		}
		// Add Range
		DateRange dateRange = date.getDateRange();
		if ((null != dateRange) && (!DateRangeHelper.isEmpty(dateRange)))
		{
			if (0 != strDate.length())
			{
				strDate += ", ";
			}
			strDate += DateRangeHelper.getDateRange(dateRange);
		}
		// Add Or
		List<DateOr> lDateOr = date.getDateOr();
		if (null != lDateOr)
		{
			for (int i=0; i<lDateOr.size(); i++)
			{
				if (0 != strDate.length())
				{
					strDate += "; ";
				}
				strDate += DateOrHelper.getDateOr(lDateOr.get(i));
			}
		}
		return(strDate);
	}

	public static final int DATE_YEAR_INVALID = DATE_YEAR_UNKNOWN;
	public static final int DATE_MONTH_INVALID = DATE_MONTH_UNKNOWN;
	public static final int DATE_DAY_INVALID = DATE_DAY_UNKNOWN;
	public static final int DATE_COMPARE_PARAMETER_BEFORE = 1;
	public static final int DATE_COMPARE_PARAMETER_AFTER = 2;
	public static final int DATE_COMPARE_EQUAL = 4;
	public static final int DATE_COMPARE_PARAMETER_INVALID = 8;
	public static final int DATE_COMPARE_THIS_INVALID = 16;
	
	public static long compareDate(Date dateOne, Date dateTwo)
	{
		long lFlags = 0;
		if ((null == dateOne) && (null != dateTwo))
		{
			lFlags |= DATE_COMPARE_THIS_INVALID;
			return lFlags;
		}
		if ((null != dateOne) && (null == dateTwo))
		{
			lFlags |= DATE_COMPARE_PARAMETER_INVALID;
			return lFlags;
		}
		if ((null == dateOne) && (null == dateTwo))
		{
			lFlags |= DATE_COMPARE_THIS_INVALID;
			lFlags |= DATE_COMPARE_PARAMETER_INVALID;
			lFlags |= DATE_COMPARE_EQUAL;
			return lFlags;
		}
		
		long lTopYear = DateHelper.getNumericYear(dateOne);
		long lTopMonth = DateHelper.getNumericMonth(dateOne);
		long lTopDay = DateHelper.getNumericDay(dateOne);

		long lBottomYear = DateHelper.getNumericYear(dateTwo);
		long lBottomMonth = DateHelper.getNumericMonth(dateTwo);
		long lBottomDay = DateHelper.getNumericDay(dateTwo);

		if ((DATE_YEAR_INVALID == lTopYear) && (DATE_YEAR_INVALID == lBottomYear))
		{	// Neither date available, Equal! Do not swap!
			lFlags |= DATE_COMPARE_THIS_INVALID;
			lFlags |= DATE_COMPARE_PARAMETER_INVALID;
			lFlags |= DATE_COMPARE_EQUAL;
			return lFlags;
		}
		else if ((DATE_YEAR_INVALID != lTopYear) && (DATE_YEAR_INVALID == lBottomYear))
		{	// Top before bottom, Do not swap!
			lFlags |= DATE_COMPARE_PARAMETER_INVALID;
			lFlags |= DATE_COMPARE_PARAMETER_AFTER;
			return lFlags;
		}
		else if ((DATE_YEAR_INVALID == lTopYear) && (DATE_YEAR_INVALID != lBottomYear))
		{	// Bottom before top, Swap!
			lFlags |= DATE_COMPARE_THIS_INVALID;
			lFlags |= DATE_COMPARE_PARAMETER_BEFORE;
			return lFlags;
		}

		// The INVALID_* flags are only returned if the year is invalid.
		// So all of the following checks will not set the INVALID flags.

		// Both dates are valid, so compare the dates. Year first!
		if (lBottomYear < lTopYear)
		{	// Bottom before top, Swap!
			lFlags |= DATE_COMPARE_PARAMETER_BEFORE;
			return lFlags;
		}
		else if (lBottomYear > lTopYear)
		{	// Top before bottom, Do not swap!
			lFlags |= DATE_COMPARE_PARAMETER_AFTER;
			return lFlags;
		}

		// Years equal, compare months
		if ((DATE_MONTH_INVALID == lTopMonth) && (DATE_MONTH_INVALID == lBottomMonth))
		{	// Neither date available, Equal! Do not swap!
			lFlags |= DATE_COMPARE_EQUAL;
			return lFlags;
		}
		else if ((DATE_MONTH_INVALID != lTopMonth) && (DATE_MONTH_INVALID == lBottomMonth))
		{	// Top before bottom, Do not swap!
			lFlags |= DATE_COMPARE_PARAMETER_AFTER;
			return lFlags;
		}
		else if ((DATE_MONTH_INVALID == lTopMonth) && (DATE_MONTH_INVALID != lBottomMonth))
		{	// Bottom before top, Swap!
			lFlags |= DATE_COMPARE_PARAMETER_BEFORE;
			return lFlags;
		}
		else if (lBottomMonth < lTopMonth)
		{	// Bottom before top, Swap!
			lFlags |= DATE_COMPARE_PARAMETER_BEFORE;
			return lFlags;
		}
		else if (lBottomMonth > lTopMonth)
		{	// Top before bottom, Do not swap!
			lFlags |= DATE_COMPARE_PARAMETER_AFTER;
			return lFlags;
		}

		// Months equal, compare days
		if ((DATE_DAY_INVALID == lTopDay) && (DATE_DAY_INVALID == lBottomDay))
		{	// Neither date available, Equal! Do not swap!
			lFlags |= DATE_COMPARE_EQUAL;
			return lFlags;
		}
		else if ((DATE_DAY_INVALID != lTopDay) && (DATE_DAY_INVALID == lBottomDay))
		{	// Top before bottom, Do not swap!
			lFlags |= DATE_COMPARE_PARAMETER_AFTER;
			return lFlags;
		}
		else if ((DATE_DAY_INVALID == lTopDay) && (DATE_DAY_INVALID != lBottomDay))
		{	// Bottom before top, Swap!
			lFlags |= DATE_COMPARE_PARAMETER_BEFORE;
			return lFlags;
		}
		else if (lBottomDay < lTopDay)
		{	// Bottom before top, Swap!
			lFlags |= DATE_COMPARE_PARAMETER_BEFORE;
			return lFlags;
		}
		else if (lBottomDay > lTopDay)
		{	// Top before bottom, Do not swap!
			lFlags |= DATE_COMPARE_PARAMETER_AFTER;
			return lFlags;
		}

		// Dates Equal
		lFlags |= DATE_COMPARE_EQUAL;
		return lFlags;
	}
}
