package home.genealogy.schema.all.helpers;

import home.genealogy.schema.all.DateRange;

public class DateRangeHelper
{
	public static boolean isEmpty(DateRange dateRange)
	{
		String strYearBegin = dateRange.getYearBegin();
		String strMonthBegin = dateRange.getMonthBegin();
		String strDayBegin = dateRange.getDayBegin();
		String strRelativeTimeBegin = dateRange.getRelativeTimeBegin();
		String strYearEnd = dateRange.getYearEnd();
		String strMonthEnd = dateRange.getMonthEnd();
		String strDayEnd = dateRange.getDayEnd();
		String strRelativeTimeEnd = dateRange.getRelativeTimeEnd();
		if (((null != strYearBegin) && (0 != strYearBegin.length())) &&
		    ((null != strMonthBegin) && (0 != strMonthBegin.length())) &&
		    ((null != strDayBegin) && (0 != strDayBegin.length())) &&
		    ((null != strRelativeTimeBegin) && (0 != strRelativeTimeBegin.length())) &&
		    ((null != strYearEnd) && (0 != strYearEnd.length())) &&
		    ((null != strMonthEnd) && (0 != strMonthEnd.length())) &&
		    ((null != strDayEnd) && (0 != strDayEnd.length())) &&
		    ((null != strRelativeTimeEnd) && (0 != strRelativeTimeEnd.length())))
		{
			return false;
		}
		return true;
	}
	
	public static int getBeginYear(DateRange dateRange)
	{
		int iYear = DateHelper.DATE_YEAR_UNKNOWN;
		if (null != dateRange)
		{
			String strYear = dateRange.getYearBegin();
			if (null != strYear)
			{
				try
				{
					iYear = Integer.parseInt(strYear);
				}
				catch (NumberFormatException nfe)
				{
					// Leave as invalid
				}
			}
		}
		return iYear;
	}
	
	public static int getEndYear(DateRange dateRange)
	{
		int iYear = DateHelper.DATE_YEAR_UNKNOWN;
		if (null != dateRange)
		{
			String strYear = dateRange.getYearEnd();
			if (null != strYear)
			{
				try
				{
					iYear = Integer.parseInt(strYear);
				}
				catch (NumberFormatException nfe)
				{
					// Leave as invalid
				}
			}
		}
		return iYear;
	}

	public static String getDateRange(DateRange dateRange)
	{
		String strYearBegin = dateRange.getYearBegin();
		String strMonthBegin = dateRange.getMonthBegin();
		String strDayBegin = dateRange.getDayBegin();
		String strRelativeTimeBegin = dateRange.getRelativeTimeBegin();
		String strYearEnd = dateRange.getYearEnd();
		String strMonthEnd = dateRange.getMonthEnd();
		String strDayEnd = dateRange.getDayEnd();
		String strRelativeTimeEnd = dateRange.getRelativeTimeEnd();
		StringBuffer sb = new StringBuffer(256);
		sb.append("BTWN");
		if ((null != strRelativeTimeBegin) && (0 != strRelativeTimeBegin.length()))
		{
			sb.append(" ");
			sb.append(strRelativeTimeBegin);
		}
		if ((null != strYearBegin) && (0 != strYearBegin.length()))
		{
			sb.append(" ");
			sb.append(strYearBegin);
		}
		if ((null != strMonthBegin) && (0 != strMonthBegin.length()))
		{
			sb.append(" ");
			sb.append(strMonthBegin);
		}
		if ((null != strDayBegin) && (0 != strDayBegin.length()))
		{
			sb.append(" ");
			sb.append(strDayBegin);
		}

		sb.append(" and ");

		if ((null != strRelativeTimeEnd) && (0 != strRelativeTimeEnd.length()))
		{
			sb.append(" ");
			sb.append(strRelativeTimeEnd);
		}
		if ((null != strYearEnd) && (0 != strYearEnd.length()))
		{
			sb.append(" ");
			sb.append(strYearEnd);
		}
		if ((null != strMonthEnd) && (0 != strMonthEnd.length()))
		{
			sb.append(" ");
			sb.append(strMonthEnd);
		}
		if ((null != strDayEnd) && (0 != strDayEnd.length()))
		{
			sb.append(" ");
			sb.append(strDayEnd);
		}

		return(sb.toString());
	}
}
