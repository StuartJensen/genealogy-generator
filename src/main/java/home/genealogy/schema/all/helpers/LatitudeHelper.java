package home.genealogy.schema.all.helpers;

import home.genealogy.schema.all.Latitude;

public class LatitudeHelper
{
	public static String getHemisphere(Latitude lat)
	{
		String strHemisphere = lat.getHemisphere();
		if (null != strHemisphere)
		{
			return strHemisphere;
		}
		return "";
	}
	
	public static String getDegrees(Latitude lat)
	{
		String strDegrees = lat.getDegrees();
		if (null != strDegrees)
		{
			return strDegrees;
		}
		return "";
	}
	
	public static String getMinutes(Latitude lat)
	{
		String strMinutes = lat.getMinutes();
		if (null != strMinutes)
		{
			return strMinutes;
		}
		return "";
	}
	
	public static boolean isEmpty(Latitude lat)
	{
		return (null == lat.getHemisphere()) &&
		       (null == lat.getDegrees()) &&
		       (null == lat.getMinutes());
	}
}
