package home.genealogy.schema.all.helpers;

import java.util.List;

import home.genealogy.schema.all.MarriageTag;
import home.genealogy.schema.all.PersonTag;
import home.genealogy.schema.all.PublishedIn;
import home.genealogy.schema.all.Singleton;
import home.genealogy.schema.all.Source;
import home.genealogy.schema.all.TagGroup;

public class SourceHelper
{
	public static boolean containsPublishedIn(Source source)
	{
		if (null != source)
		{
			PublishedIn pi = source.getPublishedIn();
			if (null != pi)
			{
				return true;
			}
		}
		return false;
	}
	
	public static PublishedIn getPublishedIn(Source source)
	{
		if (null != source)
		{
			return source.getPublishedIn();
		}
		return null;
	}
	
	public static boolean containsSingleton(Source source)
	{
		if (null != source)
		{
			Singleton si = source.getSingleton();
			if (null != si)
			{
				return true;
			}
		}
		return false;
	}
	
	public static Singleton getSingleton(Source source)
	{
		if (null != source)
		{
			return source.getSingleton();
		}
		return null;
	}
	
	public static List<PersonTag> getPersonTagList(Source source)
	{
		if (null != source)
		{
			Singleton singleton = source.getSingleton();
			if (null != singleton)
			{
				return SingletonHelper.getPersonTagList(singleton);
			}
		}
		return null;
	}
	
	public static List<MarriageTag> getMarriageTagList(Source source)
	{
		if (null != source)
		{
			Singleton singleton = source.getSingleton();
			if (null != singleton)
			{
				return SingletonHelper.getMarriageTagList(singleton);
			}
		}
		return null;
	}
	
}
