package home.genealogy.schema.all.extensions;

import home.genealogy.schema.all.Person;
import home.genealogy.schema.all.helpers.PersonHelper;

public class PersonSortableByName implements Comparable<PersonSortableByName>
{
	private Person m_person;
	
	public PersonSortableByName(Person person)
	{
		m_person = person;
	}
	
	public Person getPerson()
	{
		return m_person;
	}
	
	public int compareTo(PersonSortableByName that)
	{
		String strThisLastName = PersonHelper.getLastName(m_person);
		String strThatLastName = PersonHelper.getLastName(that.getPerson());
		int iReturn = strThisLastName.compareTo(strThatLastName);
		if (0 != iReturn)
		{	// Last names are not equal
			return iReturn;
		}
		String strThisFirstName = PersonHelper.getFirstName(m_person);
		String strThatFirstName = PersonHelper.getFirstName(that.getPerson());
		iReturn = strThisFirstName.compareTo(strThatFirstName);
		if (0 != iReturn)
		{	// First names are not equal
			return iReturn;
		}
		String strThisMiddleName = PersonHelper.getMiddleName(m_person);
		String strThatMiddleName = PersonHelper.getMiddleName(that.getPerson());
		return strThisMiddleName.compareTo(strThatMiddleName);
	}
}
