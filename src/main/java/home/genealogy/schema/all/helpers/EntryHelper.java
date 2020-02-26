package home.genealogy.schema.all.helpers;

import home.genealogy.schema.all.Body;
import home.genealogy.schema.all.Comment;
import home.genealogy.schema.all.Entry;
import home.genealogy.schema.all.EntryTitle;
import home.genealogy.schema.all.Header;
import home.genealogy.schema.all.MarriageTag;
import home.genealogy.schema.all.Paragraph;
import home.genealogy.schema.all.PersonTag;
import home.genealogy.schema.all.SeeAlso;
import home.genealogy.schema.all.Tag;
import home.genealogy.schema.all.TagGroup;

import java.util.ArrayList;
import java.util.List;

public class EntryHelper
{
	public static final int REFERENCEID_INVALID = -1;
	
	public static int getEntryId(Entry entry)
	{
		String strEntryId = entry.getEntryId();
		if (null != strEntryId)
		{
			try
			{
				return Integer.parseInt(strEntryId);
			}
			catch (Exception e)
			{/* return invalid id */}
		}
		return REFERENCEID_INVALID;
	}
	
	public static boolean hasPrivateAccess(Entry entry)
	{
		String strAccess = entry.getAccess();
		if (null != strAccess)
		{
			return strAccess.equalsIgnoreCase("PRIVATE");
		}
		return false;
	}
	
	public static boolean hasCopyRightedAccess(Entry entry)
	{
		String strAccess = entry.getAccess();
		if (null != strAccess)
		{
			return strAccess.equalsIgnoreCase("COPYRIGHTED");
		}
		return false;
	}
	
	public static List<PersonTag> getPersonTagList(Entry entry)
	{
		if (null != entry)
		{
			TagGroup tagGroup = entry.getTagGroup();
			if (null != tagGroup)
			{
				return TagGroupHelper.getPersonTagList(tagGroup);
			}
		}
		return null;
	}
	
	public static List<Tag> getTagList(Entry entry)
	{
		ArrayList<Tag> alTags = new ArrayList<Tag>();
		if (null != entry)
		{
    		Body body = entry.getBody();
    		if (null != body)
    		{
    			BodyHelper.getTagList(body, alTags);
    		}
		}
		return alTags;
	}
	
	public static List<MarriageTag> getMarriageTagList(Entry entry)
	{
		if (null != entry)
		{
			TagGroup tagGroup = entry.getTagGroup();
			if (null != tagGroup)
			{
				return TagGroupHelper.getMarriageTagList(tagGroup);
			}
		}
		return null;
	}
	
	public static int getTitleParagraphCount(Entry entry)
	{
		if (null != entry)
		{
			EntryTitle entryTitle = entry.getEntryTitle();
			if (null != entryTitle)
			{
				return EntryTitleHelper.getParagraphCount(entryTitle);
			}
		}
		return 0;
	}
	
	public static Paragraph getTitleParagraph(Entry entry, int iNth)
	{
		if (null != entry)
		{
			EntryTitle entryTitle = entry.getEntryTitle();
			if (null != entryTitle)
			{
				return EntryTitleHelper.getParagraph(entryTitle, iNth);
			}
		}
		return null;
	}
	
    public static String getVolumeNumbers(Entry entry)
    {
    	if (null != entry)
    	{
    		Header header = entry.getHeader();
    		if (null != header)
    		{
    			return HeaderHelper.getVolumeNumbers(header);
    		}
    	}
    	return "";
    }
    
    public static String getChapterNumbers(Entry entry)
    {
    	if (null != entry)
    	{
    		Header header = entry.getHeader();
    		if (null != header)
    		{
    			return HeaderHelper.getChapterNumbers(header);
    		}
    	}
    	return "";
    }
    
    public static String getPageNumbers(Entry entry)
    {
    	if (null != entry)
    	{
    		Header header = entry.getHeader();
    		if (null != header)
    		{
    			return HeaderHelper.getPageNumbers(header);
    		}
    	}
    	return "";
    }
    
    public static String getEntryNumbers(Entry entry)
    {
    	if (null != entry)
    	{
    		Header header = entry.getHeader();
    		if (null != header)
    		{
    			return HeaderHelper.getEntryNumbers(header);
    		}
    	}
    	return "";
    }
    
	public static int getHeaderParagraphCount(Entry entry)
	{
    	if (null != entry)
    	{
    		Header header = entry.getHeader();
    		if (null != header)
    		{
    			return HeaderHelper.getParagraphCount(header);
    		}
		}
		return 0;
	}
	
	public static Paragraph getHeaderParagraph(Entry entry, int iNth)
	{
    	if (null != entry)
    	{
    		Header header = entry.getHeader();
    		if (null != header)
    		{
    			return HeaderHelper.getParagraph(header, iNth);
    		}
		}
		return null;
	}
	
	public static int getBodyObjectCount(Entry entry)
	{
    	if (null != entry)
    	{
    		Body body = entry.getBody();
    		if (null != body)
    		{
    			return BodyHelper.getObjectCount(body);
    		}
		}
		return 0;
	}
	
	public static Object getBodyObject(Entry entry, int iNth)
	{
    	if (null != entry)
    	{
    		Body body = entry.getBody();
    		if (null != body)
    		{
    			return BodyHelper.getObject(body, iNth);
    		}
		}
		return 0;
	}
	
	public static int getCommentParagraphCount(Entry entry)
	{
    	if (null != entry)
    	{
    		Comment comment = entry.getComment();
    		if (null != comment)
    		{
    			return CommentHelper.getParagraphCount(comment);
    		}
		}
		return 0;
	}
	
	public static Paragraph getCommentParagraph(Entry entry, int iNth)
	{
    	if (null != entry)
    	{
    		Comment comment = entry.getComment();
    		if (null != comment)
    		{
    			return CommentHelper.getParagraph(comment, iNth);
    		}
		}
		return null;
	}
	
	public static int getSeeAlsoObjectCount(Entry entry)
	{
    	if (null != entry)
    	{
    		SeeAlso seeAlso = entry.getSeeAlso();
    		if (null != seeAlso)
    		{
    			return SeeAlsoHelper.getObjectCount(seeAlso);
    		}
		}
		return 0;
	}
	
	public static Object getSeeAlsoObject(Entry entry, int iNth)
	{
    	if (null != entry)
    	{
    		SeeAlso seeAlso = entry.getSeeAlso();
    		if (null != seeAlso)
    		{
    			return SeeAlsoHelper.getObject(seeAlso, iNth);
    		}
		}
		return null;
	}
	
	public static SeeAlso getSeeAlso(Entry entry)
	{
    	if (null != entry)
    	{
    		return entry.getSeeAlso();
		}
		return null;
	}
	
}
