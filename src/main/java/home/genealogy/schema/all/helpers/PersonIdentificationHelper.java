package home.genealogy.schema.all.helpers;

import home.genealogy.schema.all.NickName;
import home.genealogy.schema.all.PersonIdentification;
import home.genealogy.schema.all.PersonName;

import java.util.ArrayList;
import java.util.List;

public class PersonIdentificationHelper
{
	public static String getPersonName(PersonIdentification personIdentification)
	{
		if (null == personIdentification)
		{
			return ("");
		}
		return PersonNameHelper.getPersonName(personIdentification.getPersonName());
	}
	
	public static String getFirstName(PersonIdentification personIdentification)
	{
		if (null == personIdentification)
		{
			return ("");
		}
		return PersonNameHelper.getFirstName(personIdentification.getPersonName());
	}
	
	public static String getMiddleName(PersonIdentification personIdentification)
	{
		if (null == personIdentification)
		{
			return ("");
		}
		return PersonNameHelper.getMiddleName(personIdentification.getPersonName());
	}
	
	public static String getLastName(PersonIdentification personIdentification)
	{
		if (null == personIdentification)
		{
			return ("");
		}
		return PersonNameHelper.getLastName(personIdentification.getPersonName());
	}
	
	public static List<String> getNickNames(PersonIdentification personIdentification)
	{
		if (null != personIdentification)
		{
			List<NickName> lNickName = personIdentification.getNickName();
			if (null != lNickName)
			{
				ArrayList<String> alNickNames = new ArrayList<String>();
				for (int i=0; i<lNickName.size(); i++)
				{
					String strNickName = lNickName.get(i).getName();
					if ((null != strNickName) && (0 != strNickName.length()))
					{
						alNickNames.add(strNickName);
					}
				}
				return alNickNames;
			}
		}
		return (new ArrayList<String>());
	}
	
	public static List<String> getMonkiers(PersonIdentification personIdentification)
	{
		if (null != personIdentification)
		{
			List<String> lMoniker = personIdentification.getMoniker();
			if (null != lMoniker)
			{
				return new ArrayList<String>(lMoniker);
			}
		}
		return (new ArrayList<String>());
	}
	
	public static List<String> getFirstNameAltSpellings(PersonIdentification personIdentification)
	{
		if (null != personIdentification)
		{
			PersonName personName = personIdentification.getPersonName();
			if (null != personName)
			{
				return PersonNameHelper.getFirstNameAltSpellings(personName);
			}
		}
		return (new ArrayList<String>());
	}
	
	public static List<String> getMiddleNameAltSpellings(PersonIdentification personIdentification)
	{
		if (null != personIdentification)
		{
			PersonName personName = personIdentification.getPersonName();
			if (null != personName)
			{
				return PersonNameHelper.getMiddleNameAltSpellings(personName);
			}
		}
		return (new ArrayList<String>());
	}
	
	public static List<String> getLastNameAltSpellings(PersonIdentification personIdentification)
	{
		if (null != personIdentification)
		{
			PersonName personName = personIdentification.getPersonName();
			if (null != personName)
			{
				return PersonNameHelper.getLastNameAltSpellings(personName);
			}
		}
		return (new ArrayList<String>());
	}
}
