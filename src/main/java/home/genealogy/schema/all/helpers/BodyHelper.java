package home.genealogy.schema.all.helpers;

import home.genealogy.schema.all.Body;
import home.genealogy.schema.all.Paragraph;
import home.genealogy.schema.all.Table;
import home.genealogy.schema.all.Tag;

import java.util.ArrayList;
import java.util.List;

public class BodyHelper
{
	public static int getObjectCount(Body body)
	{
		if (null != body)
		{
			List<Object> lObjects = body.getContent();
			if (null != lObjects)
			{
				return lObjects.size();
			}
		}
		return 0;
	}
	
	public static Object getObject(Body body, int iNth)
	{
		if (null != body)
		{
			List<Object> lObjects = body.getContent();
			if ((null != lObjects) && (iNth < lObjects.size()))
			{
				return lObjects.get(iNth);
			}
		}
		return null;
	}
	
	public static List<Tag> getTagList(Body body, List<Tag> lTags)
	{
		if (null == lTags)
		{
			lTags = new ArrayList<Tag>();
		}
		if (null != body)
		{
			int iCount = getObjectCount(body);
			for (int i=0; i<iCount; i++)
			{
				Object oo = getObject(body, i);
				if (oo instanceof Paragraph)
				{
					ParagraphHelper.getTagList((Paragraph)oo, lTags);
				}
				else if (oo instanceof Table)
				{
					TableHelper.getTagList((Table)oo, lTags);
				}
			}
		}
		return lTags;
	}
}
