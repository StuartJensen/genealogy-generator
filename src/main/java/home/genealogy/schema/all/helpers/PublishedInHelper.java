package home.genealogy.schema.all.helpers;

import home.genealogy.schema.all.PublishedIn;
import home.genealogy.schema.all.ReferenceEntryId;

import java.util.List;

public class PublishedInHelper
{
	public static int getReferenceEntryIdCount(PublishedIn pi)
	{
		if (null != pi)
		{
			 List<ReferenceEntryId> lEntries = pi.getReferenceEntryId();
			 return lEntries.size();
		}
		return 0;
	}
	
	public static ReferenceEntryId getReferenceEntryId(PublishedIn pi, int iNth)
	{
		if (null != pi)
		{
			 List<ReferenceEntryId> lEntries = pi.getReferenceEntryId();
			 if (lEntries.size() > iNth)
			 {
				 return lEntries.get(iNth);
			 }
		}
		return null;
	}
}
