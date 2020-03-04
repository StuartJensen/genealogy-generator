package home.genealogy.schema.all.helpers;

import home.genealogy.lists.PlaceList;
import home.genealogy.schema.all.Info;
import home.genealogy.schema.all.MarriageInfo;
import home.genealogy.schema.all.Proxy;

import java.util.ArrayList;
import java.util.List;

public class MarriageInfoHelper
{
	public static String getDate(MarriageInfo marrInfo)
	{
		if (null != marrInfo)
		{
			Info info = marrInfo.getInfo();
			if (null != info)
			{
				return InfoHelper.getDate(info);
			}
		}
		return "";
	}
	
	public static String getPlace(MarriageInfo marrInfo, PlaceList placeList)
	{
		if (null != marrInfo)
		{
			Info info = marrInfo.getInfo();
			if (null != info)
			{
				return InfoHelper.getPlace(info, placeList);
			}
		}
		return "";
	}
	
}
