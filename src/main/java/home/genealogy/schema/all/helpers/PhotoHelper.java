package home.genealogy.schema.all.helpers;

import java.util.ArrayList;
import java.util.List;

import home.genealogy.schema.all.Description;
import home.genealogy.schema.all.Entry;
import home.genealogy.schema.all.File;
import home.genealogy.schema.all.FileList;
import home.genealogy.schema.all.MarriageTag;
import home.genealogy.schema.all.Paragraph;
import home.genealogy.schema.all.PersonTag;
import home.genealogy.schema.all.Photo;
import home.genealogy.schema.all.PublishedIn;
import home.genealogy.schema.all.SeeAlso;
import home.genealogy.schema.all.Singleton;
import home.genealogy.schema.all.Source;
import home.genealogy.schema.all.TagGroup;

public class PhotoHelper
{
	public static final String ACCESS_PUBLIC = "PUBLIC";
	public static final String ACCESS_PRIVATE = "PRIVATE";
	public static final String ACCESS_COPYRIGHTED = "COPYRIGHTED";
	
	public static int getPhotoId(Photo photo)
	{
		String strPhotoId = photo.getPhotoId();
		if (null != strPhotoId)
		{
			try
			{
				return Integer.parseInt(strPhotoId);
			}
			catch (Exception e)
			{/* return invalid id */}
		}
		return PhotoIdHelper.PHOTOID_INVALID;
	}
	
	public static String getAccess(Photo photo)
	{
		if (null != photo)
		{
			String strAccess = photo.getAccess();
			if (null != strAccess)
			{
				return strAccess;
			}
		}
		return "";
	}
	
	public static List<Paragraph> getDescription(Photo photo)
	{
		if (null != photo)
		{
			Description description = photo.getDescription();
			if (null != description)
			{
				return DescriptionHelper.getParagraphs(description);
			}
		}
		return new ArrayList<Paragraph>();
	}
	
	public static String getPackage(Photo photo)
	{
		if (null != photo)
		{
			FileList fileList = photo.getFileList();
			if (null != fileList)
			{
				return FileListHelper.getPackage(fileList);
			}
		}
		return "";
	}
	
	public static String getBaseFileName(Photo photo)
	{
		if (null != photo)
		{
			FileList fileList = photo.getFileList();
			if (null != fileList)
			{
				return FileListHelper.getBaseFileName(fileList);
			}
		}
		return "";
	}
	
	public static List<File> getFiles(Photo photo)
	{
		if (null != photo)
		{
			FileList fileList = photo.getFileList();
			if (null != fileList)
			{
				return FileListHelper.getFiles(fileList);
			}
		}
		return new ArrayList<File>();
	}

	public static String getSmallestWebbableFile(Photo photo)
	{
		if (null != photo)
		{
			FileList fileList = photo.getFileList();
			if (null != fileList)
			{
				File f = FileListHelper.getSmallestWebbableFile(fileList);
				if (null != f)
				{
					StringBuffer sb = new StringBuffer(128);
					sb.append(FileListHelper.getPackage(fileList)).append("\\").append(FileListHelper.getBaseFileName(fileList));
					sb.append(FileHelper.getUniqueId(f)).append(".").append(FileHelper.getTypeExtension(f));
					return sb.toString();
				}
			}
		}
		return "";
	}
	
	public static boolean containsPublishedIn(Photo photo)
	{
		if (null != photo)
		{
			Source source = photo.getSource();
			if (null != source)
			{
				return SourceHelper.containsPublishedIn(source);
			}
		}
		return false;
	}
	
	public static PublishedIn getPublishedIn(Photo photo)
	{
		if (null != photo)
		{
			Source source = photo.getSource();
			if (null != source)
			{
				return SourceHelper.getPublishedIn(source);
			}
		}
		return null;
	}
	
	public static boolean containsSingleton(Photo photo)
	{
		if (null != photo)
		{
			Source source = photo.getSource();
			if (null != source)
			{
				return SourceHelper.containsSingleton(source);
			}
		}
		return false;
	}
	
	public static Singleton getSingleton(Photo photo)
	{
		if (null != photo)
		{
			Source source = photo.getSource();
			if (null != source)
			{
				return SourceHelper.getSingleton(source);
			}
		}
		return null;
	}
	
	public static int getSeeAlsoObjectCount(Photo photo)
	{
    	if (null != photo)
    	{
    		SeeAlso seeAlso = photo.getSeeAlso();
    		if (null != seeAlso)
    		{
    			return SeeAlsoHelper.getObjectCount(seeAlso);
    		}
		}
		return 0;
	}
	
	public static Object getSeeAlsoObject(Photo photo, int iNth)
	{
    	if (null != photo)
    	{
    		SeeAlso seeAlso = photo.getSeeAlso();
    		if (null != seeAlso)
    		{
    			return SeeAlsoHelper.getObject(seeAlso, iNth);
    		}
		}
		return null;
	}
	
	public static SeeAlso getSeeAlso(Photo photo)
	{
    	if (null != photo)
    	{
    		return photo.getSeeAlso();
		}
		return null;
	}
	
	public static List<PersonTag> getPersonTagList(Photo photo)
	{
		if (null != photo)
		{
			Source source = photo.getSource();
			if (null != source)
			{
				return SourceHelper.getPersonTagList(source);
			}
		}
		return null;
	}
	
	public static List<MarriageTag> getMarriageTagList(Photo photo)
	{
		if (null != photo)
		{
			Source source = photo.getSource();
			if (null != source)
			{
				return SourceHelper.getMarriageTagList(source);
			}
		}
		return null;
	}
}
