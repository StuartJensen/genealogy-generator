package home.genealogy.schema.all.helpers;

import home.genealogy.schema.all.Tag;

public class TagHelper
{
	public static final String TAG_QUALITY_HIGH = "HIGH";
	public static final String TAG_QUALITY_MEDIUM = "MEDIUM";
	public static final String TAG_QUALITY_LOW = "LOW";
	
	public enum eTagQuality
	{
		eHigh,
		eMedium,
		eLow
	};	
	
	public static int getPersonId(Tag tag)
	{
		String strId = tag.getPersonId();
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
	
	public static int getMarriageId(Tag tag)
	{
		String strId = tag.getMarriageId();
		if (null != strId)
		{
			try
			{
				return Integer.parseInt(strId);
			}
			catch (Exception e)
			{/* return invalid id */}
		}
		return MarriageIdHelper.MARRIAGEID_INVALID;
	}
	
	public static eTagQuality getQuality(Tag tag)
	{
		String strQuality = tag.getQuality();
		if (null != strQuality)
		{
			if (strQuality.equalsIgnoreCase(TAG_QUALITY_HIGH))
			{
				return eTagQuality.eHigh;
			}
			else if (strQuality.equalsIgnoreCase(TAG_QUALITY_MEDIUM))
			{
				return eTagQuality.eMedium;
			}
		}
		return eTagQuality.eLow;
	}
	
	public static String getType(Tag tag)
	{
		String strType = tag.getType();
		if (null != strType)
		{
			return strType;
		}
		return "";
	}
	
	public static boolean isPersonTag(Tag tag)
	{
		int iPersonId = getPersonId(tag);
		int iMarriageId = getMarriageId(tag);
		return ((PersonIdHelper.PERSONID_INVALID != iPersonId) && (MarriageIdHelper.MARRIAGEID_INVALID == iMarriageId));
	}
	
	public static boolean isMarriageTag(Tag tag)
	{
		int iPersonId = getPersonId(tag);
		int iMarriageId = getMarriageId(tag);
		return ((PersonIdHelper.PERSONID_INVALID == iPersonId) && (MarriageIdHelper.MARRIAGEID_INVALID != iMarriageId));
	}
	
	public static boolean isInvalidTag(Tag tag)
	{
		if (isPersonTag(tag) && isMarriageTag(tag))
		{	// Tag cannot be both person and marriage tag
			return true;
		}
		// Also, a tag must be at least one of person or marriage tag
		return (!isPersonTag(tag) && !isMarriageTag(tag));
	}
}
