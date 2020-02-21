package home.genealogy.schema.all.helpers;

import home.genealogy.schema.all.BurialInfo;
import home.genealogy.schema.all.Info;

public class BurialInfoHelper
{
	public static String getBurialDate(BurialInfo burialInfo)
	{
		if (null != burialInfo)
		{
			Info info = burialInfo.getInfo();
			if (null != info)
			{
				return InfoHelper.getDate(info);
			}
		}
		return "";
	}
	
	public static String getBurialPlace(BurialInfo burialInfo)
	{
		if (null != burialInfo)
		{
			Info info = burialInfo.getInfo();
			if (null != info)
			{
				return InfoHelper.getPlace(info);
			}
		}
		return "";
	}
	
	public static String getCemeteryName(BurialInfo burialInfo)
	{
		if (null != burialInfo)
		{
			String strCemeteryName = burialInfo.getCemeteryName();
			if (null != strCemeteryName)
			{
				return strCemeteryName;
			}
		}
		return "";
	}
	
	public static String getCemeteryPlotAddress(BurialInfo burialInfo)
	{
		if (null != burialInfo)
		{
			String strCemeteryPlotAddress = burialInfo.getCemeteryPlotAddress();
			if (null != strCemeteryPlotAddress)
			{
				return strCemeteryPlotAddress;
			}
		}
		return "";
	}

}
