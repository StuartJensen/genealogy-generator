package home.genealogy.schema.all.helpers;

import java.util.List;

import home.genealogy.lists.PlaceList;
import home.genealogy.schema.all.Caption;
import home.genealogy.schema.all.Commentary;
import home.genealogy.schema.all.Date;
import home.genealogy.schema.all.MarriageTag;
import home.genealogy.schema.all.PersonTag;
import home.genealogy.schema.all.Place;
import home.genealogy.schema.all.Singleton;
import home.genealogy.schema.all.TagGroup;

public class SingletonHelper
{
	public static String getDate(Singleton singleton)
	{
		if (null != singleton)
		{
			Date date = singleton.getDate();
			if (null != date)
			{
				return DateHelper.getDate(date);
			}
		}
		return "";
	}
	
	public static String getPlace(Singleton singleton, PlaceList placeList)
	{
		if (null != singleton)
		{
			Place place = singleton.getPlace();
			if (null != place)
			{
				return PlaceHelper.getPlace(place, placeList);
			}
		}
		return "";
	}
	
	public static Caption getCaption(Singleton singleton)
	{
		if (null != singleton)
		{
			return singleton.getCaption();
		}
		return null;
	}
	
	public static Commentary getCommentary(Singleton singleton)
	{
		if (null != singleton)
		{
			return singleton.getCommentary();
		}
		return null;
	}
	
	public static List<PersonTag> getPersonTagList(Singleton singleton)
	{
		if (null != singleton)
		{
			TagGroup tagGroup = singleton.getTagGroup();
			if (null != tagGroup)
			{
				return TagGroupHelper.getPersonTagList(tagGroup);
			}
		}
		return null;
	}
	
	public static List<MarriageTag> getMarriageTagList(Singleton singleton)
	{
		if (null != singleton)
		{
			TagGroup tagGroup = singleton.getTagGroup();
			if (null != tagGroup)
			{
				return TagGroupHelper.getMarriageTagList(tagGroup);
			}
		}
		return null;
	}
}
