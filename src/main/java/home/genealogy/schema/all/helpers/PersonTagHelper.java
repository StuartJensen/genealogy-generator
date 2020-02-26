package home.genealogy.schema.all.helpers;

import java.util.List;

import home.genealogy.schema.all.PersonTag;
import home.genealogy.schema.all.PersonTagType;

public class PersonTagHelper
{
	public static int getPersonId(PersonTag personTag)
	{
		if (null != personTag)
		{
			String strPersonId = personTag.getPersonId();
			if (null != strPersonId)
			{
				try
				{
					return Integer.parseInt(strPersonId);
				}
				catch (Exception e)
				{/* return invalid id */}
			}
		}
		return PersonIdHelper.PERSONID_INVALID;
	}
	
	public static int getPersonTagTypeCount(PersonTag personTag)
	{
		if (null != personTag)
		{
			List<PersonTagType> lTags = personTag.getPersonTagType();
			if (null != lTags)
			{
				return lTags.size();
			}
		}
		return 0;
	}
	
	public static PersonTagType getPersonTagType(PersonTag personTag, int iNth)
	{
		if (null != personTag)
		{
			List<PersonTagType> lTags = personTag.getPersonTagType();
			if ((null != lTags) && (iNth < lTags.size()))
			{
				return lTags.get(iNth);
			}
		}
		return null;
	}
}
