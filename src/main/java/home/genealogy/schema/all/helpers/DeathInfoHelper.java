package home.genealogy.schema.all.helpers;

import home.genealogy.schema.all.DeathInfo;
import home.genealogy.schema.all.Info;

public class DeathInfoHelper
{
	public static String getDeathDate(DeathInfo deathInfo)
	{
		if (null != deathInfo)
		{
			Info info = deathInfo.getInfo();
			if (null != info)
			{
				return InfoHelper.getDate(info);
			}
		}
		return "";
	}
	
	public static String getDeathPlace(DeathInfo deathInfo)
	{
		if (null != deathInfo)
		{
			Info info = deathInfo.getInfo();
			if (null != info)
			{
				return InfoHelper.getPlace(info);
			}
		}
		return "";
	}
	
	public static String getCause(DeathInfo deathInfo)
	{
		if (null != deathInfo)
		{
			String strCause = deathInfo.getCause();
			if (null != strCause)
			{
				return strCause;
			}
		}
		return "";
	}
}
