package home.genealogy.schema.all.helpers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import home.genealogy.lists.PlaceList;
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
	
	public static String getDeathPlace(DeathInfo deathInfo, PlaceList placeList)
	{
		if (null != deathInfo)
		{
			Info info = deathInfo.getInfo();
			if (null != info)
			{
				return InfoHelper.getPlace(info, placeList);
			}
		}
		return "";
	}

	public static List<String> getCause(DeathInfo deathInfo)
	{
		if (null != deathInfo)
		{
			List<String> lCause = deathInfo.getCause();
			if (null != lCause)
			{
				return lCause;
			}
		}
		return Collections.emptyList();
	}
}
