package home.genealogy.schema.all.helpers;

import home.genealogy.schema.all.ReferenceId;

public class ReferenceIdHelper
{
	public static final int REFERENCEID_INVALID = -1;
	
	public static int getReferenceId(ReferenceId referenceId)
	{
		return referenceId.getId();
	}
}
