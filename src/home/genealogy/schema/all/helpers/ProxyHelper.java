package home.genealogy.schema.all.helpers;

import home.genealogy.schema.all.PersonId;
import home.genealogy.schema.all.PersonName;
import home.genealogy.schema.all.Proxy;

public class ProxyHelper
{
	public static String getProxyName(Proxy proxy)
	{
		if (null != proxy)
		{
			PersonName personName = proxy.getPersonName();		
			if (null != personName)
			{
				return PersonNameHelper.getPersonName(personName);
			}
		}
		return "";
	}
	
	public static int getProxyPersonId(Proxy proxy)
	{
		if (null != proxy)
		{
			PersonId personId = proxy.getPersonId();		
			if (null != personId)
			{
				return PersonIdHelper.getPersonId(personId);
			}
		}
		return PersonIdHelper.PERSONID_INVALID;
	}

}
