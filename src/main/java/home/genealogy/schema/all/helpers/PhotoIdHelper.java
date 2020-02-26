package home.genealogy.schema.all.helpers;

import home.genealogy.schema.all.PhotoId;

public class PhotoIdHelper
{
	public static final String PHOTOID_TYPE_LINKED = "LINKED";
	public static final String PHOTOID_TYPE_EMBEDDED = "EMBEDDED";	
	public static final int PHOTOID_INVALID = -1;
	
	public enum ePhotoIdType
	{
		 eEmbedded,
		 eLinked
	};
	
	public static int getPhotoId(PhotoId photoId)
	{
		String strId = photoId.getId();
		if (null != strId)
		{
			try
			{
				return Integer.parseInt(strId);
			}
			catch (Exception e)
			{/* return invalid id */}
		}
		return PHOTOID_INVALID;
	}
	
	public static ePhotoIdType getType(PhotoId photoId)
	{
		String strType = photoId.getType();
		if (null != strType)
		{
			if (strType.equalsIgnoreCase(PHOTOID_TYPE_EMBEDDED))
			{
				return ePhotoIdType.eEmbedded;
			}
		}
		return ePhotoIdType.eLinked;
	}
	
	public static boolean isLinked(PhotoId photoId)
	{
		return (getType(photoId) == ePhotoIdType.eLinked);
	}
	
	public static boolean isEmbedded(PhotoId photoId)
	{
		return (getType(photoId) == ePhotoIdType.eEmbedded);
	}
}
