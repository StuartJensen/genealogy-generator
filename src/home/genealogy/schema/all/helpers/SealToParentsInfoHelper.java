package home.genealogy.schema.all.helpers;

import java.util.ArrayList;
import java.util.List;

import home.genealogy.schema.all.Info;
import home.genealogy.schema.all.Proxy;
import home.genealogy.schema.all.SealToParentsInfo;

public class SealToParentsInfoHelper
{
	public static String getSealToParentsDate(SealToParentsInfo sealToParentsInfo)
	{
		if (null != sealToParentsInfo)
		{
			Info info = sealToParentsInfo.getInfo();
			if (null != info)
			{
				return InfoHelper.getDate(info);
			}
		}
		return "";
	}
	
	public static int[] getNumericSealToParentsDate(SealToParentsInfo sealToParentsInfo)
	{
		if (null != sealToParentsInfo)
		{
			Info info = sealToParentsInfo.getInfo();
			if (null != info)
			{
				return InfoHelper.getNumericDate(info);
			}
		}
		return InfoHelper.getNumericDate(null);
	}
	
	public static String getSealToParentsPlace(SealToParentsInfo sealToParentsInfo)
	{
		if (null != sealToParentsInfo)
		{
			Info info = sealToParentsInfo.getInfo();
			if (null != info)
			{
				return InfoHelper.getPlace(info);
			}
		}
		return "";
	}
	
	public static String getSealToParentsTemple(SealToParentsInfo sealToParentsInfo)
	{
		if (null != sealToParentsInfo)
		{
			Info info = sealToParentsInfo.getInfo();
			if (null != info)
			{
				return InfoHelper.getTemple(info);
			}
		}
		return "";
	}
	
	public static List<String> getSealToParentsProxies(SealToParentsInfo sealToParentsInfo)
	{
		ArrayList<String> alResult = new ArrayList<String>();
		if (null != sealToParentsInfo)
		{
			List<Proxy> lProxies = sealToParentsInfo.getProxy();
			if (null != lProxies)
			{
				for (int i=0; i<lProxies.size(); i++)
				{
					String strName = ProxyHelper.getProxyName(lProxies.get(i));
					if ((null != strName) && (0 != strName.length()))
					{
						alResult.add(strName);
					}
				}
			}
		}
		return alResult;
	}
}
