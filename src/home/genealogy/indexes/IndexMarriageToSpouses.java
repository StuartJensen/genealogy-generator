package home.genealogy.indexes;

import home.genealogy.configuration.CFGFamily;
import home.genealogy.lists.MarriageList;
import home.genealogy.schema.all.Marriage;
import home.genealogy.schema.all.helpers.MarriageHelper;
import home.genealogy.schema.all.helpers.MarriageIdHelper;
import home.genealogy.schema.all.helpers.PersonIdHelper;

import java.util.Iterator;

public class IndexMarriageToSpouses
{
	// Index into arrays = Marriage Id
	// Value of arrays entry = Person Id of spouse in that marriage
	private int[] m_arHusbandIndex;
	private int[] m_arWifeIndex;
	
	public IndexMarriageToSpouses(CFGFamily family, MarriageList marriageList)
	{
		m_arHusbandIndex = new int[family.getMarriageListMaxSize()];
		m_arWifeIndex = new int[family.getMarriageListMaxSize()];
		for (int i=0; i<m_arHusbandIndex.length; i++)
		{	// Init entire arrays of person ids to invalid
			m_arHusbandIndex[i] = PersonIdHelper.PERSONID_INVALID;
			m_arWifeIndex[i] = PersonIdHelper.PERSONID_INVALID;
		}
		Iterator<Marriage> iter = marriageList.getMarriages();
		while (iter.hasNext())
		{
			Marriage marriage = iter.next();
			int iMarriageId = MarriageHelper.getMarriageId(marriage);
			int iHusbandPersonId = MarriageHelper.getHusbandPersonId(marriage);
			int iWifePersonId = MarriageHelper.getWifePersonId(marriage);
			if ((MarriageIdHelper.MARRIAGEID_INVALID != iMarriageId) &&
				(PersonIdHelper.PERSONID_INVALID != iHusbandPersonId) &&
				(PersonIdHelper.PERSONID_INVALID != iWifePersonId))
			{
				m_arHusbandIndex[iMarriageId] = iHusbandPersonId;
				m_arWifeIndex[iMarriageId] = iWifePersonId;
			}
		}
	}
	
	public static final int GET_SPOUSES_HUSBAND_IDX = 0;
	public static final int GET_SPOUSES_WIFE_IDX = 1;	
	private static final int GET_SPOUSES_ARRAY_SIZE = 2;
	public int[] getSpouses(int iMarriageId)
	{
		int[] ar = new int[GET_SPOUSES_ARRAY_SIZE];
		ar[GET_SPOUSES_HUSBAND_IDX] = PersonIdHelper.PERSONID_INVALID;
		ar[GET_SPOUSES_WIFE_IDX] = PersonIdHelper.PERSONID_INVALID;
		if (iMarriageId < m_arHusbandIndex.length)
		{
			ar[GET_SPOUSES_HUSBAND_IDX] = m_arHusbandIndex[iMarriageId];
			ar[GET_SPOUSES_WIFE_IDX] = m_arWifeIndex[iMarriageId];			
		}
		return ar;
	}

}
