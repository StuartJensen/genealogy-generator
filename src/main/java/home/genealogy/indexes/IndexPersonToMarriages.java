package home.genealogy.indexes;

import home.genealogy.configuration.CFGFamily;
import home.genealogy.lists.MarriageList;
import home.genealogy.schema.all.Marriage;
import home.genealogy.schema.all.helpers.MarriageHelper;
import home.genealogy.schema.all.helpers.MarriageIdHelper;
import home.genealogy.schema.all.helpers.PersonIdHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class IndexPersonToMarriages
{
	// Key = Person Id
	// Value = List of Marriage Ids for that person
	private HashMap<Integer, ArrayList<Integer>> m_hmIndex;
	
	public IndexPersonToMarriages(CFGFamily family, MarriageList marriageList)
	{
		m_hmIndex = new HashMap<Integer, ArrayList<Integer>>();
		Iterator<Marriage> iter = marriageList.getMarriages();
		while (iter.hasNext())
		{
			Marriage marriage = iter.next();
			int iMarriageId = MarriageHelper.getMarriageId(marriage);
			int iHusbandPersonId = MarriageHelper.getHusbandPersonId(marriage);
			int iWifePersonId = MarriageHelper.getWifePersonId(marriage);
			if (MarriageIdHelper.MARRIAGEID_INVALID != iMarriageId)
			{
				if (PersonIdHelper.PERSONID_INVALID != iHusbandPersonId)
				{
					ArrayList<Integer> al = m_hmIndex.get(new Integer(iHusbandPersonId));
					if (null == al)
					{
						al = new ArrayList<Integer>();
						m_hmIndex.put(iHusbandPersonId, al);
					}
					al.add(iMarriageId);
				}

				if (PersonIdHelper.PERSONID_INVALID != iWifePersonId)
				{
					ArrayList<Integer> al = m_hmIndex.get(new Integer(iWifePersonId));
					if (null == al)
					{
						al = new ArrayList<Integer>();
						m_hmIndex.put(iWifePersonId, al);
					}
					al.add(iMarriageId);
				}
			}
			
			// @TODO Sort the marriage indexes by marriage date
			// Sort all marriages by marriage date
			/*
			for (int i=0; i<m_marriageToChildren.Size(); i++)
			{
				CDWordArray *pIdArray = m_personToMarriages.Lookup(i);
				if (NULL != pIdArray)
				{
					m_pFamily->SortMarriagesInArrayByMarriageDate(pIdArray);
				}
			}
			*/
		}
	}
	
	public int getPersonCount()
	{
		return m_hmIndex.size();
	}

	public int getMarriageCount(int iPersonId)
	{
		ArrayList<Integer> al = m_hmIndex.get(new Integer(iPersonId));
		if (null != al)
		{
			return al.size();
		}
		return 0;
	}
	
	public ArrayList<Integer> getMarriageIds(int iPersonId)
	{
		ArrayList<Integer> alMarriages = new ArrayList<Integer>();
		ArrayList<Integer> al = m_hmIndex.get(new Integer(iPersonId));
		if (null != al)
		{
			alMarriages = new ArrayList<Integer>(al);
		}
		return alMarriages;
	}
	
}
