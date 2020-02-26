package home.genealogy.schema.all.helpers;

import home.genealogy.schema.all.BaptismInfo;
import home.genealogy.schema.all.Info;
import home.genealogy.schema.all.Proxy;

public class BaptismInfoHelper
{
	public static String getBaptismDate(BaptismInfo baptismInfo)
	{
		if (null != baptismInfo)
		{
			Info info = baptismInfo.getInfo();
			if (null != info)
			{
				return InfoHelper.getDate(info);
			}
		}
		return "";
	}
	
	public static int[] getNumericBaptismDate(BaptismInfo baptismInfo)
	{
		if (null != baptismInfo)
		{
			Info info = baptismInfo.getInfo();
			if (null != info)
			{
				return InfoHelper.getNumericDate(info);
			}
		}
		return InfoHelper.getNumericDate(null);
	}
	
	public static String getBaptismPlace(BaptismInfo baptismInfo)
	{
		if (null != baptismInfo)
		{
			Info info = baptismInfo.getInfo();
			if (null != info)
			{
				return InfoHelper.getPlace(info);
			}
		}
		return "";
	}
	
	public static String getBaptismTemple(BaptismInfo baptismInfo)
	{
		if (null != baptismInfo)
		{
			Info info = baptismInfo.getInfo();
			if (null != info)
			{
				return InfoHelper.getTemple(info);
			}
		}
		return "";
	}
	
	public static String getProxyName(BaptismInfo baptismInfo)
	{
		if (null != baptismInfo)
		{
			Proxy proxy = baptismInfo.getProxy();
			if (null != proxy)
			{
				return ProxyHelper.getProxyName(proxy);
			}
		}
		return "";
	}
	
	public static int getProxyPersonId(BaptismInfo baptismInfo)
	{
		if (null != baptismInfo)
		{
			Proxy proxy = baptismInfo.getProxy();
			if (null != proxy)
			{
				return ProxyHelper.getProxyPersonId(proxy);
			}
		}
		return PersonIdHelper.PERSONID_INVALID;
	}

}
