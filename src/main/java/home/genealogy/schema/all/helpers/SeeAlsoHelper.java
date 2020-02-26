package home.genealogy.schema.all.helpers;

import home.genealogy.schema.all.SeeAlso;

import java.util.List;

public class SeeAlsoHelper
{	
	public static int getObjectCount(SeeAlso seeAlso)
	{
		List<Object> lObject = seeAlso.getPersonIdOrMarriageIdOrReferenceId();
		if (null != lObject)
		{
			return lObject.size();
		}
		return 0;
	}
	
	public static Object getObject(SeeAlso seeAlso, int iNth)
	{
		List<Object> lObject = seeAlso.getPersonIdOrMarriageIdOrReferenceId();
		if ((null != lObject) && (iNth < lObject.size()))
		{
			return lObject.get(iNth);
		}
		return null;
	}
}
