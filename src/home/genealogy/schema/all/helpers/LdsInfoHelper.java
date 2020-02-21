package home.genealogy.schema.all.helpers;

import java.util.ArrayList;
import java.util.List;

import home.genealogy.schema.all.BaptismInfo;
import home.genealogy.schema.all.EndowmentInfo;
import home.genealogy.schema.all.LdsInfo;
import home.genealogy.schema.all.Proxy;
import home.genealogy.schema.all.SealToParentsInfo;

public class LdsInfoHelper
{
	public static String getBaptismDate(LdsInfo ldsInfo)
	{
		if (null != ldsInfo)
		{
			BaptismInfo baptismInfo = ldsInfo.getBaptismInfo();
			if (null != baptismInfo)
			{
				return BaptismInfoHelper.getBaptismDate(baptismInfo);
			}
		}
		return "";
	}
	
	public static int[] getNumericBaptismDate(LdsInfo ldsInfo)
	{
		if (null != ldsInfo)
		{
			BaptismInfo baptismInfo = ldsInfo.getBaptismInfo();
			if (null != baptismInfo)
			{
				return BaptismInfoHelper.getNumericBaptismDate(baptismInfo);
			}
		}
		return InfoHelper.getNumericDate(null);
	}
	
	public static String getBaptismPlace(LdsInfo ldsInfo)
	{
		if (null != ldsInfo)
		{
			BaptismInfo baptismInfo = ldsInfo.getBaptismInfo();
			if (null != baptismInfo)
			{
				return BaptismInfoHelper.getBaptismPlace(baptismInfo);
			}
		}
		return "";
	}
	
	public static String getBaptismTemple(LdsInfo ldsInfo)
	{
		if (null != ldsInfo)
		{
			BaptismInfo baptismInfo = ldsInfo.getBaptismInfo();
			if (null != baptismInfo)
			{
				return BaptismInfoHelper.getBaptismTemple(baptismInfo);
			}
		}
		return "";
	}
	
	public static String getBaptismProxyName(LdsInfo ldsInfo)
	{
		if (null != ldsInfo)
		{
			BaptismInfo baptismInfo = ldsInfo.getBaptismInfo();
			if (null != baptismInfo)
			{
				return BaptismInfoHelper.getProxyName(baptismInfo);
			}
		}
		return "";
	}
	
	public static int getBaptismProxyPersonId(LdsInfo ldsInfo)
	{
		if (null != ldsInfo)
		{
			BaptismInfo baptismInfo = ldsInfo.getBaptismInfo();
			if (null != baptismInfo)
			{
				return BaptismInfoHelper.getProxyPersonId(baptismInfo);
			}
		}
		return PersonIdHelper.PERSONID_INVALID;
	}
		
	public static String getEndowmentDate(LdsInfo ldsInfo)
	{
		if (null != ldsInfo)
		{
			EndowmentInfo endowmentInfo = ldsInfo.getEndowmentInfo();
			if (null != endowmentInfo)
			{
				return EndowmentInfoHelper.getEndowmentDate(endowmentInfo);
			}
		}
		return "";
	}
	
	public static int[] getNumericEndowmentDate(LdsInfo ldsInfo)
	{
		if (null != ldsInfo)
		{
			EndowmentInfo endowmentInfo = ldsInfo.getEndowmentInfo();
			if (null != endowmentInfo)
			{
				return EndowmentInfoHelper.getNumericEndowmentDate(endowmentInfo);
			}
		}
		return InfoHelper.getNumericDate(null);
	}
	
	public static String getEndowmentPlace(LdsInfo ldsInfo)
	{
		if (null != ldsInfo)
		{
			EndowmentInfo endowmentInfo = ldsInfo.getEndowmentInfo();
			if (null != endowmentInfo)
			{
				return EndowmentInfoHelper.getEndowmentPlace(endowmentInfo);
			}
		}
		return "";
	}
	
	public static String getEndowmentTemple(LdsInfo ldsInfo)
	{
		if (null != ldsInfo)
		{
			EndowmentInfo endowmentInfo = ldsInfo.getEndowmentInfo();
			if (null != endowmentInfo)
			{
				return EndowmentInfoHelper.getEndowmentTemple(endowmentInfo);
			}
		}
		return "";
	}
	
	public static String getEndowmentProxyName(LdsInfo ldsInfo)
	{
		if (null != ldsInfo)
		{
			EndowmentInfo endowmentInfo = ldsInfo.getEndowmentInfo();
			if (null != endowmentInfo)
			{
				return EndowmentInfoHelper.getEndowmentProxyName(endowmentInfo);
			}
		}
		return "";
	}
	
	public static int getEndowmentProxyPersonId(LdsInfo ldsInfo)
	{
		if (null != ldsInfo)
		{
			EndowmentInfo endowmentInfo = ldsInfo.getEndowmentInfo();
			if (null != endowmentInfo)
			{
				return EndowmentInfoHelper.getEndowmentProxyPersonId(endowmentInfo);
			}
		}
		return PersonIdHelper.PERSONID_INVALID;
	}
	
	public static String getSealToParentsDate(LdsInfo ldsInfo)
	{
		if (null != ldsInfo)
		{
			SealToParentsInfo sealToParentsInfo = ldsInfo.getSealToParentsInfo();
			if (null != sealToParentsInfo)
			{
				return SealToParentsInfoHelper.getSealToParentsDate(sealToParentsInfo);
			}
		}
		return "";
	}
	
	public static int[] getNumericSealToParentsDate(LdsInfo ldsInfo)
	{
		if (null != ldsInfo)
		{
			SealToParentsInfo sealToParentsInfo = ldsInfo.getSealToParentsInfo();
			if (null != sealToParentsInfo)
			{
				return SealToParentsInfoHelper.getNumericSealToParentsDate(sealToParentsInfo);
			}
		}
		return InfoHelper.getNumericDate(null);
	}
	
	public static String getSealToParentsPlace(LdsInfo ldsInfo)
	{
		if (null != ldsInfo)
		{
			SealToParentsInfo sealToParentsInfo = ldsInfo.getSealToParentsInfo();
			if (null != sealToParentsInfo)
			{
				return SealToParentsInfoHelper.getSealToParentsPlace(sealToParentsInfo);
			}
		}
		return "";
	}
	
	public static String getSealToParentsTemple(LdsInfo ldsInfo)
	{
		if (null != ldsInfo)
		{
			SealToParentsInfo sealToParentsInfo = ldsInfo.getSealToParentsInfo();
			if (null != sealToParentsInfo)
			{
				return SealToParentsInfoHelper.getSealToParentsTemple(sealToParentsInfo);
			}
		}
		return "";
	}
	
	public static List<String> getSealToParentsProxies(LdsInfo ldsInfo)
	{
		if (null != ldsInfo)
		{
			SealToParentsInfo sealToParentsInfo = ldsInfo.getSealToParentsInfo();
			if (null != sealToParentsInfo)
			{
				return SealToParentsInfoHelper.getSealToParentsProxies(sealToParentsInfo);
			}
		}
		return new ArrayList<String>();
	}
}
