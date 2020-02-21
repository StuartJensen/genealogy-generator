package home.genealogy.schema.all.helpers;

import home.genealogy.schema.all.Religion;

public class ReligionHelper
{
	public static String getReligion(Religion religion)
	{
		String strReligion = religion.getName();
		if (null != strReligion)
		{
			return strReligion;
		}
		return "";
	}
}
