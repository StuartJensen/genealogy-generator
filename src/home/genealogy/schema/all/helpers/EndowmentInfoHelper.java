package home.genealogy.schema.all.helpers;

import home.genealogy.schema.all.EndowmentInfo;
import home.genealogy.schema.all.Info;
import home.genealogy.schema.all.Proxy;

public class EndowmentInfoHelper
{
	public static String getEndowmentDate(EndowmentInfo emdowmentInfo)
	{
		if (null != emdowmentInfo)
		{
			Info info = emdowmentInfo.getInfo();
			if (null != info)
			{
				return InfoHelper.getDate(info);
			}
		}
		return "";
	}
	
	public static int[] getNumericEndowmentDate(EndowmentInfo emdowmentInfo)
	{
		if (null != emdowmentInfo)
		{
			Info info = emdowmentInfo.getInfo();
			if (null != info)
			{
				return InfoHelper.getNumericDate(info);
			}
		}
		return InfoHelper.getNumericDate(null);
	}
	
	public static String getEndowmentPlace(EndowmentInfo emdowmentInfo)
	{
		if (null != emdowmentInfo)
		{
			Info info = emdowmentInfo.getInfo();
			if (null != info)
			{
				return InfoHelper.getPlace(info);
			}
		}
		return "";
	}
	
	public static String getEndowmentTemple(EndowmentInfo emdowmentInfo)
	{
		if (null != emdowmentInfo)
		{
			Info info = emdowmentInfo.getInfo();
			if (null != info)
			{
				return InfoHelper.getTemple(info);
			}
		}
		return "";
	}
	
	public static String getEndowmentProxyName(EndowmentInfo emdowmentInfo)
	{
		if (null != emdowmentInfo)
		{
			Proxy proxy = emdowmentInfo.getProxy();
			if (null != proxy)
			{
				return ProxyHelper.getProxyName(proxy);
			}
		}
		return "";
	}
	
	public static int getEndowmentProxyPersonId(EndowmentInfo emdowmentInfo)
	{
		if (null != emdowmentInfo)
		{
			Proxy proxy = emdowmentInfo.getProxy();
			if (null != proxy)
			{
				return ProxyHelper.getProxyPersonId(proxy);
			}
		}
		return PersonIdHelper.PERSONID_INVALID;
	}
}
