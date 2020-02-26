package home.genealogy.schema.all.helpers;

import home.genealogy.schema.all.File;

public class FileHelper
{
	public static final String FILE_TYPE_JPG = "JPG";
	public static final String FILE_TYPE_PNG = "PNG";
	public static final String FILE_TYPE_GIF = "GIF";
	public static final String FILE_TYPE_BMP = "BMP";
	public static final String FILE_TYPE_TIFF = "TIFF";
	
	public static final String FILE_QUALITY_THUMBNAIL = "Thumbnail";
	public static final String FILE_QUALITY_LOWEST = "Lowest";
	public static final String FILE_QUALITY_LOW = "Low";
	public static final String FILE_QUALITY_MEDIUM = "Medium";
	public static final String FILE_QUALITY_HIGH = "High";
	public static final String FILE_QUALITY_HIGHEST = "Highest";
	
	public enum eFileType
	{
		eUnknown,
		eJPG,
		eGIF,
		eBMP,
		eTIFF,
		ePNG
	};
	
	public enum eFileQuality
	{
		eUnknown,
		eThumbnail,
		eLowest,
		eLow,
		eMedium,
		eHigh,
		eHighest
	};
	
	public static eFileQuality getQuality(File file)
	{
		if (null != file)
		{
			String strQuality = file.getQuality();
			if (null != strQuality)
			{
				if (strQuality.equalsIgnoreCase(FILE_QUALITY_THUMBNAIL))
				{
					return eFileQuality.eThumbnail;
				}
				else if (strQuality.equalsIgnoreCase(FILE_QUALITY_LOWEST))
				{
					return eFileQuality.eLowest;
				}
				else if (strQuality.equalsIgnoreCase(FILE_QUALITY_MEDIUM))
				{
					return eFileQuality.eMedium;
				}
				else if (strQuality.equalsIgnoreCase(FILE_QUALITY_HIGH))
				{
					return eFileQuality.eHigh;
				}
				else if (strQuality.equalsIgnoreCase(FILE_QUALITY_HIGHEST))
				{
					return eFileQuality.eHighest;
				}
			}
		}
		return eFileQuality.eUnknown;
	}

	public static eFileType getType(File file)
	{
		if (null != file)
		{
			String strType = file.getType();
			if (null != strType)
			{
				if (strType.equalsIgnoreCase(FILE_TYPE_JPG))
				{
					return eFileType.eJPG;
				}
				else if (strType.equalsIgnoreCase(FILE_TYPE_PNG))
				{
					return eFileType.ePNG;
				}
				else if (strType.equalsIgnoreCase(FILE_TYPE_GIF))
				{
					return eFileType.eGIF;
				}
				else if (strType.equalsIgnoreCase(FILE_TYPE_BMP))
				{
					return eFileType.eBMP;
				}
				else if (strType.equalsIgnoreCase(FILE_TYPE_TIFF))
				{
					return eFileType.eTIFF;
				}
			}
		}
		return eFileType.eUnknown;
	}
	
	public static String getTypeExtension(File file)
	{
		if (null != file)
		{
			String strType = file.getType();
			if (null != strType)
			{
				if (strType.equalsIgnoreCase(FILE_TYPE_JPG))
				{
					return "jpg";
				}
				else if (strType.equalsIgnoreCase(FILE_TYPE_PNG))
				{
					return "png";
				}
				else if (strType.equalsIgnoreCase(FILE_TYPE_GIF))
				{
					return "gif";
				}
				else if (strType.equalsIgnoreCase(FILE_TYPE_BMP))
				{
					return "bmp";
				}
				else if (strType.equalsIgnoreCase(FILE_TYPE_TIFF))
				{
					return "tiff";
				}
			}
		}
		return "unknown";
	}
	
	public static String getUniqueId(File file)
	{
		if (null != file)
		{
			String strUniqueId = file.getUniqueId();
			if (null != strUniqueId)
			{
				return strUniqueId;
			}
		}
		return "";
	}
	
	public static int getSize(File file)
	{
		if (null != file)
		{
			String strSize = file.getSize();
			if (null != strSize)
			{
				try{return Integer.parseInt(strSize);}catch(Exception e){}
			}
		}
		return 0;
	}
}
