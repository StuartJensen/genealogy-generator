package home.genealogy.schema.all.helpers;

import home.genealogy.schema.all.Date;
import home.genealogy.schema.all.Info;
import home.genealogy.schema.all.Place;

public class InfoHelper
{
	public static String getDate(Info info)
	{
		if (null != info)
		{
			Date date = info.getDate();
			if (null != date)
			{
				return DateHelper.getDate(date);
			}
		}
		return "";
	}
	
	public static int[] getNumericDate(Info info)
	{
		if (null != info)
		{
			Date date = info.getDate();
			if (null != date)
			{
				return DateHelper.getNumericDate(date);
			}
		}
		return DateHelper.getNumericDate(null);
	}
	
	public static Date getObjectDate(Info info)
	{
		if (null != info)
		{
			return info.getDate();
		}
		return null;
	}
	
	public static String getPlace(Info info)
	{
		if (null != info)
		{
			Place place = info.getPlace();
			if (null != place)
			{
				return PlaceHelper.getPlace(place);
			}
		}
		return "";
	}
	
	public static String getTemple(Info info)
	{
		if (null != info)
		{
			Place place = info.getPlace();
			if (null != place)
			{
				return PlaceHelper.getTemple(place);
			}
		}
		return "";
	}
}
