package home.genealogy.schema.all.helpers;

import home.genealogy.schema.all.Comment;
import home.genealogy.schema.all.Paragraph;

import java.util.List;

public class CommentHelper
{
	public static int getParagraphCount(Comment comment)
	{
    	if (null != comment)
    	{
    		List<Paragraph> lParagraph = comment.getParagraph();
    		if (null != lParagraph)
    		{
    			return lParagraph.size();
    		}
		}
		return 0;
	}
	
	public static Paragraph getParagraph(Comment comment, int iNth)
	{
    	if (null != comment)
    	{
    		List<Paragraph> lParagraph = comment.getParagraph();
    		if ((null != lParagraph) && (iNth < lParagraph.size()))
    		{
    			return lParagraph.get(iNth);
    		}
		}
		return null;
	}
}
