package home.genealogy.schema.all.helpers;

import home.genealogy.schema.all.Description;
import home.genealogy.schema.all.Paragraph;

import java.util.ArrayList;
import java.util.List;

public class DescriptionHelper
{
	public static List<Paragraph> getParagraphs(Description description)
	{
		if (null != description)
		{
			List<Paragraph> lFile = description.getParagraph();
			return lFile;
		}
		return new ArrayList<Paragraph>();
	}
}
