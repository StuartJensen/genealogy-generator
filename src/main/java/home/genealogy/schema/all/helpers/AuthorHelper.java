package home.genealogy.schema.all.helpers;

import home.genealogy.schema.all.Author;
import home.genealogy.schema.all.GroupName;
import home.genealogy.schema.all.PersonName;

import java.util.ArrayList;
import java.util.List;

public class AuthorHelper
{
	public static List<String> getPersonAuthorNames(Author author)
	{
		ArrayList<String> alNames = new ArrayList<String>();
		if (null != author)
		{
			List<PersonName> lPersonNames = author.getPersonName();
			if (null != lPersonNames)
			{
				for (int i=0; i<lPersonNames.size(); i++)
				{
					String strPersonName = PersonNameHelper.getPersonName(lPersonNames.get(i));
					if ((null != strPersonName) && (0 != strPersonName.length()))
					{
						alNames.add(strPersonName);
					}
				}
			}
		}
		return alNames;
	}
	
	public static List<String> getGroupAuthorNames(Author author)
	{
		ArrayList<String> alNames = new ArrayList<String>();
		if (null != author)
		{
			List<GroupName> lGroupNames = author.getGroupName();
			if (null != lGroupNames)
			{
				for (int i=0; i<lGroupNames.size(); i++)
				{
					GroupName groupName = lGroupNames.get(i);
					if (null != groupName)
					{
						String strGroupName = groupName.getName();
						if ((null != strGroupName) || (0 != strGroupName.length()))
						{
							alNames.add(strGroupName);
						}
					}
				}
			}
		}
		return alNames;
	}
}
