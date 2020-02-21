package home.genealogy.indexes;

import home.genealogy.configuration.CFGFamily;
import home.genealogy.lists.PersonList;
import home.genealogy.schema.all.Person;
import home.genealogy.schema.all.helpers.MarriageIdHelper;
import home.genealogy.schema.all.helpers.PersonHelper;
import home.genealogy.schema.all.helpers.PersonIdHelper;

import java.util.Iterator;

public class IndexPersonToParentMarriage
{
	// Index into array = Person Id
	// Value of array entry = Marriage Ids for that person's parents
	private int[] m_arIndex;
	
	public IndexPersonToParentMarriage(CFGFamily family, PersonList personList)
	{
		m_arIndex = new int[family.getPersonListMaxSize()];
		for (int i=0; i<m_arIndex.length; i++)
		{	// Init entire array of marriage ids to invalid
			m_arIndex[i] = MarriageIdHelper.MARRIAGEID_INVALID;
		}
		Iterator<Person> iter = personList.getPersons();
		while (iter.hasNext())
		{
			Person person = iter.next();
			int iPersonId = PersonHelper.getPersonId(person);
			int iParentId = PersonHelper.getParentId(person);
			if ((PersonIdHelper.PERSONID_INVALID != iPersonId) &&
				(PersonIdHelper.PERSONID_INVALID != iParentId))
			{
				m_arIndex[iPersonId] = iParentId;
			}
		}
	}
	
	public int getParentMarriageId(int iPersonId)
	{
		if (iPersonId < m_arIndex.length)
		{
			return m_arIndex[iPersonId];
		}
		return MarriageIdHelper.MARRIAGEID_INVALID;
	}
	
}
