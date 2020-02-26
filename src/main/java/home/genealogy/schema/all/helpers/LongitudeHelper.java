package home.genealogy.schema.all.helpers;

import home.genealogy.schema.all.Longitude;

public class LongitudeHelper
{
	public static String getHemisphere(Longitude lng)
	{
		String strHemisphere = lng.getHemisphere();
		if (null != strHemisphere)
		{
			return strHemisphere;
		}
		return "";
	}
	
	public static String getDegrees(Longitude lng)
	{
		String strDegrees = lng.getDegrees();
		if (null != strDegrees)
		{
			return strDegrees;
		}
		return "";
	}
	
	public static String getMinutes(Longitude lng)
	{
		String strMinutes = lng.getMinutes();
		if (null != strMinutes)
		{
			return strMinutes;
		}
		return "";
	}
	
	public static boolean isEmpty(Longitude lng)
	{
		return (null == lng.getHemisphere()) &&
		       (null == lng.getDegrees()) &&
		       (null == lng.getMinutes());
	}
}
