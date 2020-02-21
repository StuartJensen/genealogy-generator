package home.genealogy.schema.all.helpers;

import home.genealogy.schema.all.MarriageTag;
import home.genealogy.schema.all.PersonTag;
import home.genealogy.schema.all.TagGroup;

import java.util.List;

public class TagGroupHelper
{
	public static List<PersonTag> getPersonTagList(TagGroup tagGroup)
	{
		if (null != tagGroup)
		{
			return tagGroup.getPersonTag();
		}
		return null;
	}
	
	public static List<MarriageTag> getMarriageTagList(TagGroup tagGroup)
	{
		if (null != tagGroup)
		{
			return tagGroup.getMarriageTag();
		}
		return null;
	}
}
