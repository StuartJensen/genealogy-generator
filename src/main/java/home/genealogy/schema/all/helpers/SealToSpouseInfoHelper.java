package home.genealogy.schema.all.helpers;

import home.genealogy.schema.all.Info;
import home.genealogy.schema.all.Proxy;
import home.genealogy.schema.all.SealToSpouseInfo;

import java.util.ArrayList;
import java.util.List;

public class SealToSpouseInfoHelper
{
	public static String getSealToSpouseDate(SealToSpouseInfo stosInfo)
	{
		if (null != stosInfo)
		{
			Info info = stosInfo.getInfo();
			if (null != info)
			{
				return InfoHelper.getDate(info);
			}
		}
		return "";
	}
	
	public static int[] getNumericSealToSpouseDate(SealToSpouseInfo stosInfo)
	{
		if (null != stosInfo)
		{
			Info info = stosInfo.getInfo();
			if (null != info)
			{
				return InfoHelper.getNumericDate(info);
			}
		}
		return InfoHelper.getNumericDate(null);
	}
	
	public static String getSealToSpousePlace(SealToSpouseInfo stosInfo)
	{
		if (null != stosInfo)
		{
			Info info = stosInfo.getInfo();
			if (null != info)
			{
				return InfoHelper.getPlace(info);
			}
		}
		return "";
	}
	
	public static List<String> getSealToSpouseProxies(SealToSpouseInfo stosInfo)
	{
		ArrayList<String> alResult = new ArrayList<String>();
		if (null != stosInfo)
		{
			List<Proxy> lProxies = stosInfo.getProxy();
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
