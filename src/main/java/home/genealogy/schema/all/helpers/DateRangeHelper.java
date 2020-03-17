package home.genealogy.schema.all.helpers;

import java.util.List;

import home.genealogy.schema.all.Date;
import home.genealogy.schema.all.DateOr;
import home.genealogy.schema.all.DateRange;

public class DateRangeHelper
{
	public static boolean isEmpty(DateRange dateRange)
	{
		if ((null != dateRange.getDateBegin()) &&
			(null != dateRange.getDateBegin().getDate()))
		{
			if (!DateHelper.isEmpty(dateRange.getDateBegin().getDate()))
			{
				return false;
			}
		}
		if ((null != dateRange.getDateEnd()) &&
			(null != dateRange.getDateEnd().getDate()))
		{
			if (!DateHelper.isEmpty(dateRange.getDateEnd().getDate()))
			{
				return false;
			}
		}
		return true;
	}
	
	public static int getOldestBeginYear(DateRange dateRange, int iRelativeToYear)
	{
		int iYear = iRelativeToYear;
		if (null != dateRange)
		{
			if (null != dateRange.getDateBegin())
			{
				iYear = DateHelper.getOldestYear(dateRange.getDateBegin().getDate(), iYear);
			}
		}
		return iYear;
	}
	
	public static int getOldestEndYear(DateRange dateRange, int iRelativeToYear)
	{
		int iYear = iRelativeToYear;
		if (null != dateRange)
		{
			if (null != dateRange.getDateEnd())
			{
				iYear = DateHelper.getOldestYear(dateRange.getDateEnd().getDate(), iYear);
			}
		}
		return iYear;
	}
	
	public static int getOldestYear(DateRange dateRange)
	{
		int iYear = DateHelper.DATE_YEAR_UNKNOWN;
		iYear = getOldestBeginYear(dateRange, iYear);
		iYear = getOldestEndYear(dateRange, iYear);
		return iYear;
	}
	
	public static int getOldestYear(DateRange dateRange, int iRelativeToYear)
	{
		int iYear = iRelativeToYear;
		iYear = getOldestBeginYear(dateRange, iYear);
		iYear = getOldestEndYear(dateRange, iYear);
		return iYear;
	}

	public static String getDateRange(DateRange dateRange)
	{
		StringBuilder sb = new StringBuilder();
		if (null != dateRange)
		{
			sb.append("(BTWN");
			if ((null != dateRange.getDateBegin()) &&
				(null != dateRange.getDateBegin().getDate()))
			{
				sb.append(" ");
				sb.append(DateHelper.getDate(dateRange.getDateBegin().getDate()));
				sb.append(" ");
			}
			else
			{
				sb.append(" ~ ");
			}
			
			sb.append("and");
			if ((null != dateRange.getDateEnd()) &&
				(null != dateRange.getDateEnd().getDate()))
			{
				sb.append(" ");
				sb.append(DateHelper.getDate(dateRange.getDateEnd().getDate()));
			}
			else
			{
				sb.append(" ~");
			}
			sb.append(")");
		}
		return(sb.toString());
	}
}
