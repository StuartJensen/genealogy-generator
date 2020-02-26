package home.genealogy.schema.all.helpers;

import home.genealogy.schema.all.Classification;

public class ClassificationHelper
{
	public static String getType(Classification classification)
	{
		if (null != classification)
		{
			String strType = classification.getType();
			if (null != strType)
			{
				return strType;
			}
		}
		return "";
	}
	
	public static String getSubType(Classification classification)
	{
		if (null != classification)
		{
			String strSubType = classification.getSubType();
			if (null != strSubType)
			{
				return strSubType;
			}
		}
		return "";
	}
}
