package home.genealogy.schema.all.helpers;

import home.genealogy.schema.all.BirthInfo;
import home.genealogy.schema.all.ChrInfo;
import home.genealogy.schema.all.Date;
import home.genealogy.schema.all.Info;

public class ChrInfoHelper
{
	public static String getChrDate(ChrInfo chrInfo)
	{
		if (null != chrInfo)
		{
			Info info = chrInfo.getInfo();
			if (null != info)
			{
				return InfoHelper.getDate(info);
			}
		}
		return "";
	}
	
	public static int[] getNumericChrDate(ChrInfo chrInfo)
	{
		if (null != chrInfo)
		{
			Info info = chrInfo.getInfo();
			if (null != info)
			{
				return InfoHelper.getNumericDate(info);
			}
		}
		return InfoHelper.getNumericDate(null);
	}
	
	public static Date getObjectChrDate(ChrInfo chrInfo)
	{
		if (null != chrInfo)
		{
			Info info = chrInfo.getInfo();
			if (null != info)
			{
				return InfoHelper.getObjectDate(info);
			}
		}
		return null;
	}
	
	public static String getChrPlace(ChrInfo chrInfo)
	{
		if (null != chrInfo)
		{
			Info info = chrInfo.getInfo();
			if (null != info)
			{
				return InfoHelper.getPlace(info);
			}
		}
		return "";
	}
}
