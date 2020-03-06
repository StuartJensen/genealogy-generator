package home.genealogy.indexes;

import home.genealogy.configuration.CFGFamily;
import home.genealogy.lists.PersonList;
import home.genealogy.schema.all.Parents;
import home.genealogy.schema.all.Person;
import home.genealogy.schema.all.helpers.MarriageIdHelper;
import home.genealogy.schema.all.helpers.ParentsHelper;
import home.genealogy.schema.all.helpers.PersonHelper;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class IndexPersonToParentMarriage
{
	// Key is the Person Id
	// Value is the list of parents objects for that person
	private Map<Integer, List<Parents>> m_mPersonIdToParents;
	
	public IndexPersonToParentMarriage(CFGFamily family, PersonList personList)
	{
		m_mPersonIdToParents = new HashMap<Integer, List<Parents>>();
		Iterator<Person> iter = personList.getPersons();
		while (iter.hasNext())
		{
			Person person = iter.next();
			int iPersonId = PersonHelper.getPersonId(person);
			List<Parents> lParents = PersonHelper.getParents(person);
			if (!lParents.isEmpty())
			{
				m_mPersonIdToParents.put(new Integer(iPersonId), lParents);
			}
		}
	}
	
	public int getPreferredParentMarriageId(int iPersonId)
	{
		List<Parents> lParents = m_mPersonIdToParents.get(new Integer(iPersonId));
		if (null != lParents)
		{	// If parent list is in map, then it is not empty
			Parents preferredParents = ParentsHelper.getPreferredParents(lParents);
			if (null != preferredParents)
			{
				return preferredParents.getMarriageId();
			}
		}
		return MarriageIdHelper.MARRIAGEID_INVALID;
	}
/*	
	public int getParentMarriageId(int iPersonId)
	{
		if (iPersonId < m_arIndex.length)
		{
			return m_arIndex[iPersonId];
		}
		return MarriageIdHelper.MARRIAGEID_INVALID;
	}
*/	
}
