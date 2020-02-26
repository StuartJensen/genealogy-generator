package home.genealogy.schema.all.helpers;

import home.genealogy.schema.all.Header;
import home.genealogy.schema.all.Paragraph;

import java.util.List;

public class HeaderHelper
{
    public static String getVolumeNumbers(Header header)
    {
    	if (null != header)
    	{
    		String strVolumeNumbers = header.getVolumeNumbers();
    		if (null != strVolumeNumbers)
    		{
    			return strVolumeNumbers;
    		}
    	}
    	return "";
    }
    
    public static String getChapterNumbers(Header header)
    {
    	if (null != header)
    	{
    		String strChapterNumbers = header.getChapterNumbers();
    		if (null != strChapterNumbers)
    		{
    			return strChapterNumbers;
    		}
    	}
    	return "";
    }
    
    public static String getPageNumbers(Header header)
    {
    	if (null != header)
    	{
    		String strPageNumbers = header.getPageNumbers();
    		if (null != strPageNumbers)
    		{
    			return strPageNumbers;
    		}
    	}
    	return "";
    }
    
    public static String getEntryNumbers(Header header)
    {
    	if (null != header)
    	{
    		String strEntryNumbers = header.getEntryNumbers();
    		if (null != strEntryNumbers)
    		{
    			return strEntryNumbers;
    		}
    	}
    	return "";
    }
    
	public static int getParagraphCount(Header header)
	{
		if (null != header)
		{
			List<Paragraph> lParagraphs = header.getParagraph();
			if (null != lParagraphs)
			{
				return lParagraphs.size();
			}
		}
		return 0;
	}
	
	public static Paragraph getParagraph(Header header, int iNth)
	{
		if (null != header)
		{
			List<Paragraph> lParagraphs = header.getParagraph();
			if ((null != lParagraphs) && (iNth < lParagraphs.size()))
			{
				return lParagraphs.get(iNth);
			}
		}
		return null;
	}
}
