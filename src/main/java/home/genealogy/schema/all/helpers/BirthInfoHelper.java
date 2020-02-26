package home.genealogy.schema.all.helpers;

import home.genealogy.schema.all.BirthInfo;
import home.genealogy.schema.all.Date;
import home.genealogy.schema.all.Info;

public class BirthInfoHelper
{
	public static String getBirthDate(BirthInfo birthInfo)
	{
		if (null != birthInfo)
		{
			Info info = birthInfo.getInfo();
			if (null != info)
			{
				return InfoHelper.getDate(info);
			}
		}
		return "";
	}
	
	public static int[] getNumericBirthDate(BirthInfo birthInfo)
	{
		if (null != birthInfo)
		{
			Info info = birthInfo.getInfo();
			if (null != info)
			{
				return InfoHelper.getNumericDate(info);
			}
		}
		return InfoHelper.getNumericDate(null);
	}
	
	public static Date getObjectBirthDate(BirthInfo birthInfo)
	{
		if (null != birthInfo)
		{
			Info info = birthInfo.getInfo();
			if (null != info)
			{
				return InfoHelper.getObjectDate(info);
			}
		}
		return null;
	}
	
	public static String getBirthPlace(BirthInfo birthInfo)
	{
		if (null != birthInfo)
		{
			Info info = birthInfo.getInfo();
			if (null != info)
			{
				return InfoHelper.getPlace(info);
			}
		}
		return "";
	}
	
	public static String getHospital(BirthInfo birthInfo)
	{
		if (null != birthInfo)
		{
			String strHospital = birthInfo.getHospital();
			if (null != strHospital)
			{
				return strHospital;
			}
		}
		return "";
	}
}
