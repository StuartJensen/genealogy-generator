package home.genealogy.schema.all.helpers;

import home.genealogy.schema.all.Author;
import home.genealogy.schema.all.Authority;
import home.genealogy.schema.all.Location;
import home.genealogy.schema.all.Title;

import java.util.ArrayList;
import java.util.List;

public class AuthorityHelper
{
	public static String getTitle(Authority authority)
	{
		if (null != authority)
		{
			Title title = authority.getTitle();
			if (null != title)
			{
				return TitleHelper.getTitle(title);
			}
		}
		return "";
	}
	
	public static List<String> getPersonAuthorNames(Authority authority)
	{
		if (null != authority)
		{
			Author author = authority.getAuthor();
			if (null != author)
			{
				return AuthorHelper.getPersonAuthorNames(author);
			}
		}
		return new ArrayList<String>();
	}
	
	public static List<String> getGroupAuthorNames(Authority authority)
	{
		if (null != authority)
		{
			Author author = authority.getAuthor();
			if (null != author)
			{
				return AuthorHelper.getGroupAuthorNames(author);
			}
		}
		return new ArrayList<String>();
	}
	
	public static int getLocationCount(Authority authority)
	{
		if (null != authority)
		{
			List<Location> alLocations = authority.getLocation();
			if (null != alLocations)
			{
				return alLocations.size();
			}
		}
		return 0;
	}
	
	public static Location getLocation(Authority authority, int iNth)
	{
		if (null != authority)
		{
			List<Location> alLocations = authority.getLocation();
			if ((null != alLocations) && (iNth < alLocations.size()))
			{
				return alLocations.get(iNth);
			}
		}
		return null;
	}
	
}
