package home.genealogy.schema.all.helpers;

import home.genealogy.schema.all.FirstName;

import java.util.ArrayList;
import java.util.List;

public class FirstNameHelper
{
	public static String getFirstName(FirstName firstName)
	{
		if (null == firstName)
		{
			return "";
		}
		String strFirstName = firstName.getName();
		if (null == strFirstName)
		{
			return "";
		}
		return strFirstName;
	}
	
	public static List<String> getAlternateSpellings(FirstName firstName)
	{
		if (null != firstName)
		{
			List<String> lAltSpellings = firstName.getAltSpelling();
			if (null != lAltSpellings)
			{
				return new ArrayList<String>(lAltSpellings);
			}
		}
		return (new ArrayList<String>());
	}
}
