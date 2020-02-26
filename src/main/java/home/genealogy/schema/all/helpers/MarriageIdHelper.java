package home.genealogy.schema.all.helpers;

import home.genealogy.schema.all.MarriageId;

public class MarriageIdHelper
{
	public static final int MARRIAGEID_INVALID = -1;
	
	public static int getMarriageId(MarriageId marriageId)
	{
		String strId = marriageId.getId();
		if (null != strId)
		{
			try
			{
				return Integer.parseInt(strId);
			}
			catch (Exception e)
			{/* return invalid id */}
		}
		return MARRIAGEID_INVALID;
	}
}
