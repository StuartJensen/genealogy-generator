package home.genealogy.indexes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import home.genealogy.CommandLineParameters;
import home.genealogy.configuration.CFGFamily;
import home.genealogy.lists.PersonList;
import home.genealogy.schema.all.Parents;
import home.genealogy.schema.all.Person;
import home.genealogy.schema.all.helpers.MarriageIdHelper;
import home.genealogy.schema.all.helpers.PersonHelper;
import home.genealogy.schema.all.helpers.PersonIdHelper;
import home.genealogy.util.Sorters;

public class IndexMarriageToChildren
{
	// Key = Marriage Id of parents
	// Value = List of Person Ids representing children for that person
	private HashMap<Integer, ArrayList<Integer>> m_hmIndex;
	
	public IndexMarriageToChildren(CFGFamily family, CommandLineParameters commandLineParameters, PersonList personList)
	{
		m_hmIndex = new HashMap<Integer, ArrayList<Integer>>();
		Iterator<Person> iter = personList.getPersons();
		while (iter.hasNext())
		{
			Person person = iter.next();
			int iPersonId = PersonHelper.getPersonId(person);
			if (PersonIdHelper.PERSONID_INVALID != iPersonId)
			{
				List<Parents> lParents = PersonHelper.getParents(person);
				for (Parents parent : lParents)
				{
					int iParentMarriageId = parent.getMarriageId();
					if (MarriageIdHelper.MARRIAGEID_INVALID != iParentMarriageId)
					{
						ArrayList<Integer> al = m_hmIndex.get(new Integer(iParentMarriageId));
						if (null == al)
						{
							al = new ArrayList<Integer>();
							m_hmIndex.put(iParentMarriageId, al);
						}
						al.add(iPersonId);
					}
				}
			}
		}
		
		// Sort all children of each marriage by birth/chr date
		Iterator<Integer> iterMarriages = m_hmIndex.keySet().iterator();
		while (iterMarriages.hasNext())
		{
			Integer intMarId = iterMarriages.next();
			ArrayList<Integer> alChildIds = m_hmIndex.get(intMarId);
			Sorters.sortPeopleInArrayByBirth(alChildIds, personList);
		}
		
	}
	
	public int getMarriageCount()
	{
		return m_hmIndex.size();
	}

	public int getChildrenCount(int iMarriageId)
	{
		ArrayList<Integer> al = m_hmIndex.get(new Integer(iMarriageId));
		if (null != al)
		{
			return al.size();
		}
		return 0;
	}
	
	public ArrayList<Integer> getChildrenPersonIds(int iMarriageId)
	{
		ArrayList<Integer> alChildren = new ArrayList<Integer>();
		ArrayList<Integer> al = m_hmIndex.get(new Integer(iMarriageId));
		if (null != al)
		{
			alChildren = new ArrayList<Integer>(al);
		}
		return alChildren;
	}

}
