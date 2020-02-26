package home.genealogy.schema.all.helpers;

import home.genealogy.schema.all.MarriageTag;
import home.genealogy.schema.all.MarriageTagType;

import java.util.List;

public class MarriageTagHelper
{
	public static int getMarriageId(MarriageTag marriageTag)
	{
		if (null != marriageTag)
		{
			String strMarriageId = marriageTag.getMarriageId();
			if (null != strMarriageId)
			{
				try
				{
					return Integer.parseInt(strMarriageId);
				}
				catch (Exception e)
				{/* return invalid id */}
			}
		}
		return MarriageIdHelper.MARRIAGEID_INVALID;
	}
	
	public static int getMarriageTagTypeCount(MarriageTag marriageTag)
	{
		if (null != marriageTag)
		{
			List<MarriageTagType> lTags = marriageTag.getMarriageTagType();
			if (null != lTags)
			{
				return lTags.size();
			}
		}
		return 0;
	}
	
	public static MarriageTagType getMarriageTagType(MarriageTag marriageTag, int iNth)
	{
		if (null != marriageTag)
		{
			List<MarriageTagType> lTags = marriageTag.getMarriageTagType();
			if ((null != lTags) && (iNth < lTags.size()))
			{
				return lTags.get(iNth);
			}
		}
		return null;
	}

}
