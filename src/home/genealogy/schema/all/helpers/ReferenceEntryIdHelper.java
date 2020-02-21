package home.genealogy.schema.all.helpers;

import home.genealogy.schema.all.ReferenceEntryId;

public class ReferenceEntryIdHelper
{
	public static final int REFERENCEENTRYID_INVALID = -1;
	
	public static int getReferenceId(ReferenceEntryId referenceEntryId)
	{
		String strId = referenceEntryId.getId();
		if (null != strId)
		{
			try
			{
				return Integer.parseInt(strId);
			}
			catch (Exception e)
			{/* return invalid id */}
		}
		return ReferenceIdHelper.REFERENCEID_INVALID;
	}
	
	public static int getReferenceEntryId(ReferenceEntryId referenceEntryId)
	{
		String strId = referenceEntryId.getEntryId();
		if (null != strId)
		{
			try
			{
				return Integer.parseInt(strId);
			}
			catch (Exception e)
			{/* return invalid id */}
		}
		return REFERENCEENTRYID_INVALID;
	}
}
