package home.genealogy.schema.all.helpers;

import java.util.ArrayList;
import java.util.List;

import home.genealogy.schema.all.FirstName;
import home.genealogy.schema.all.LastName;
import home.genealogy.schema.all.MiddleName;
import home.genealogy.schema.all.PersonIdentification;
import home.genealogy.schema.all.PersonName;

public class PersonNameHelper
{
	public static String getPersonName(PersonName personName)
	{
		if (null == personName)
		{
			return ("");
		}
		String strLastName = LastNameHelper.getLastName(personName.getLastName());
		String strMiddleName = MiddleNameHelper.getMiddleName(personName.getMiddleName());
		String strFirstName = FirstNameHelper.getFirstName(personName.getFirstName());
		if (0 == (strLastName.length() + strMiddleName.length() + strFirstName.length()))
		{
			return "";
		}
		StringBuffer sb = new StringBuffer(125);
		if (0 != strLastName.length())
		{
			sb.append(strLastName.toUpperCase());
			sb.append(", ");
		}
		sb.append(strFirstName);
		if (0 != strMiddleName.length())
		{
			sb.append(" ");
			sb.append(strMiddleName);
		}
		return sb.toString();
	}
	
	public static String getFirstName(PersonName personName)
	{
		if (null == personName)
		{
			return ("");
		}
		return FirstNameHelper.getFirstName(personName.getFirstName());
	}
	
	public static String getMiddleName(PersonName personName)
	{
		if (null == personName)
		{
			return ("");
		}
		return MiddleNameHelper.getMiddleName(personName.getMiddleName());
	}
	
	public static String getLastName(PersonName personName)
	{
		if (null == personName)
		{
			return ("");
		}
		return LastNameHelper.getLastName(personName.getLastName());
	}
	
	public static List<String> getFirstNameAltSpellings(PersonName personName)
	{
		if (null != personName)
		{
			FirstName firstName = personName.getFirstName();
			if (null != firstName)
			{
				return FirstNameHelper.getAlternateSpellings(firstName);
			}
		}
		return (new ArrayList<String>());
	}
	
	public static List<String> getMiddleNameAltSpellings(PersonName personName)
	{
		if (null != personName)
		{
			MiddleName middleName = personName.getMiddleName();
			if (null != middleName)
			{
				return MiddleNameHelper.getAlternateSpellings(middleName);
			}
		}
		return (new ArrayList<String>());
	}
	
	public static List<String> getLastNameAltSpellings(PersonName personName)
	{
		if (null != personName)
		{
			LastName lastName = personName.getLastName();
			if (null != lastName)
			{
				return LastNameHelper.getAlternateSpellings(lastName);
			}
		}
		return (new ArrayList<String>());
	}
}
