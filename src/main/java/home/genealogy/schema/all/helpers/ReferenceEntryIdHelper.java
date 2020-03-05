package home.genealogy.schema.all.helpers;

import home.genealogy.schema.all.ReferenceEntryId;

public class ReferenceEntryIdHelper
{
	public static final int REFERENCEENTRYID_INVALID = -1;
	
	public static int getReferenceId(ReferenceEntryId referenceEntryId)
	{
		return referenceEntryId.getId();
	}
	
	public static int getReferenceEntryId(ReferenceEntryId referenceEntryId)
	{
		return referenceEntryId.getEntryId();
	}
}
