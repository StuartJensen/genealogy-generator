package home.genealogy.schema.all.helpers;

import home.genealogy.schema.all.MiddleName;

import java.util.ArrayList;
import java.util.List;

public class MiddleNameHelper
{
	public static String getMiddleName(MiddleName middleName)
	{
		if (null == middleName)
		{
			return "";
		}
		String strMiddleName = middleName.getName();
		if (null == strMiddleName)
		{
			return "";
		}
		return strMiddleName;
	}
	
	public static List<String> getAlternateSpellings(MiddleName middleName)
	{
		if (null != middleName)
		{
			List<String> lAltSpellings = middleName.getAltSpelling();
			if (null != lAltSpellings)
			{
				return new ArrayList<String>(lAltSpellings);
			}
		}
		return (new ArrayList<String>());
	}
}
