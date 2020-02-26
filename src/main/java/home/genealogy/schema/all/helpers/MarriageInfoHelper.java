package home.genealogy.schema.all.helpers;

import home.genealogy.schema.all.Info;
import home.genealogy.schema.all.MarriageInfo;
import home.genealogy.schema.all.Proxy;
import home.genealogy.schema.all.SealToSpouseInfo;

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
	
	public static String getPlace(MarriageInfo marrInfo)
	{
		if (null != marrInfo)
		{
			Info info = marrInfo.getInfo();
			if (null != info)
			{
				return InfoHelper.getPlace(info);
			}
		}
		return "";
	}
	
	public static String getSealToSpouseDate(MarriageInfo marrInfo)
	{
		if (null != marrInfo)
		{
			SealToSpouseInfo stosInfo = marrInfo.getSealToSpouseInfo();
			if (null != stosInfo)
			{
				return SealToSpouseInfoHelper.getSealToSpouseDate(stosInfo);
			}
		}
		return "";
	}
	
	public static String getSealToSpousePlace(MarriageInfo marrInfo)
	{
		if (null != marrInfo)
		{
			SealToSpouseInfo stosInfo = marrInfo.getSealToSpouseInfo();
			if (null != stosInfo)
			{
				return SealToSpouseInfoHelper.getSealToSpousePlace(stosInfo);
			}
		}
		return "";
	}
	
	public static List<String> getSealToSpouseProxies(MarriageInfo marrInfo)
	{
		if (null != marrInfo)
		{
			SealToSpouseInfo stosInfo = marrInfo.getSealToSpouseInfo();
			if (null != stosInfo)
			{
				return SealToSpouseInfoHelper.getSealToSpouseProxies(stosInfo);
			}
		}
		return new ArrayList<String>();
	}
}
