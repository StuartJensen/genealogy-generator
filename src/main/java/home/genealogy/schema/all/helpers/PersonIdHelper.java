package home.genealogy.schema.all.helpers;

import home.genealogy.schema.all.PersonId;

public class PersonIdHelper
{
	public static final int PERSONID_INVALID = -1;
	
	public static int getPersonId(PersonId personId)
	{
		String strId = personId.getId();
		if (null != strId)
		{
			try
			{
				return Integer.parseInt(strId);
			}
			catch (Exception e)
			{/* return invalid id */}
		}
		return PersonIdHelper.PERSONID_INVALID;
	}
}
