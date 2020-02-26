package home.genealogy.schema.all.helpers;

import java.util.ArrayList;
import java.util.List;

import home.genealogy.schema.all.LastName;

public class LastNameHelper
{
	public static String getLastName(LastName lastName)
	{
		if (null == lastName)
		{
			return "";
		}
		String strLastName = lastName.getName();
		if (null == strLastName)
		{
			return "";
		}
		return strLastName;
	}
	
	public static List<String> getAlternateSpellings(LastName lastName)
	{
		if (null != lastName)
		{
			List<String> lAltSpellings = lastName.getAltSpelling();
			if (null != lAltSpellings)
			{
				return new ArrayList<String>(lAltSpellings);
			}
		}
		return (new ArrayList<String>());
	}
}
