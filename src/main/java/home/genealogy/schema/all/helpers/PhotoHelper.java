package home.genealogy.schema.all.helpers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import home.genealogy.lists.PlaceList;
import home.genealogy.output.IOutputStream;
import home.genealogy.schema.all.Description;
import home.genealogy.schema.all.Event;
import home.genealogy.schema.all.File;
import home.genealogy.schema.all.FileList;
import home.genealogy.schema.all.Marriage;
import home.genealogy.schema.all.MarriageTag;
import home.genealogy.schema.all.Paragraph;
import home.genealogy.schema.all.Person;
import home.genealogy.schema.all.PersonTag;
import home.genealogy.schema.all.Photo;
import home.genealogy.schema.all.Place;
import home.genealogy.schema.all.PublishedIn;
import home.genealogy.schema.all.SeeAlso;
import home.genealogy.schema.all.Singleton;
import home.genealogy.schema.all.Source;

public class PhotoHelper
{
	public static final String ACCESS_PUBLIC = "PUBLIC";
	public static final String ACCESS_PRIVATE = "PRIVATE";
	public static final String ACCESS_COPYRIGHTED = "COPYRIGHTED";
	
	public static int getPhotoId(Photo photo)
	{
		return photo.getPhotoId();
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
	
	public static Set<String> getAllPlaceIds(Photo photo)
	{
		Set<String> hResults = new HashSet<String>();
		if (null != photo)
		{
			if ((null != photo.getSource()) &&
				(null != photo.getSource().getSingleton()) &&
				(null != photo.getSource().getSingleton().getPlace()))
			{
				hResults.add(photo.getSource().getSingleton().getPlace().getIdRef());
			}
		}
		return hResults;
	}
	
	public static boolean usesPlace(Photo photo, PlaceList placeList, String strPlaceId)
	{
		Set<String> sAllPlaceIds = PhotoHelper.getAllPlaceIds(photo);
		return sAllPlaceIds.contains(strPlaceId);
	}
	
	public static int replacePlaceId(Photo photo,
									String strToBeReplaced,
									String strReplacement,
									String strLocale,
									String strStreet,
									String strSpot,
									IOutputStream outputStream)
	{
		int iCount = 0;
		if (null != photo)
		{
			if ((null != photo.getSource()) &&
				(null != photo.getSource().getSingleton()) &&
				(null != photo.getSource().getSingleton().getPlace()))
			{
				if (photo.getSource().getSingleton().getPlace().getIdRef().equals(strToBeReplaced))
				{
					outputStream.output("  PhotoList: Place Id Replace: Photo Id: " + photo.getPhotoId() + ", Replacing Photo Place: " + photo.getSource().getSingleton().getPlace().getIdRef() + " with " + strReplacement + "\n");
					PlaceHelper.setPlaceData(photo.getSource().getSingleton().getPlace(), strReplacement, strLocale, strStreet, strSpot);
					iCount++;
				}
			}
		}
		return iCount;
	}
}
