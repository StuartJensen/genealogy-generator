package home.genealogy.util;

import home.genealogy.lists.MarriageList;
import home.genealogy.lists.PersonList;
import home.genealogy.schema.all.Marriage;
import home.genealogy.schema.all.Parents;
import home.genealogy.schema.all.Person;
import home.genealogy.schema.all.helpers.MarriageHelper;
import home.genealogy.schema.all.helpers.MarriageIdHelper;
import home.genealogy.schema.all.helpers.ParentsHelper;
import home.genealogy.schema.all.helpers.PersonHelper;
import home.genealogy.schema.all.helpers.PersonIdHelper;

public class TwoGenerations
{
	// Person - GEN 1
	private int m_iPersonPId;
	private Person m_person;
	
	// Parents - GEN 2
	private int m_iParentsMId;
	private Marriage m_parentMarriage;
	private int m_iFatherPId;
	private Person m_father;
	private int m_iMotherPId;
	private Person m_mother;
	
	public TwoGenerations(int iPersonId, PersonList personList, MarriageList marriageList)
	{
		m_iPersonPId = iPersonId;
		m_iParentsMId = MarriageIdHelper.MARRIAGEID_INVALID;
		m_iFatherPId = PersonIdHelper.PERSONID_INVALID;
		m_iMotherPId = PersonIdHelper.PERSONID_INVALID;
		if (PersonIdHelper.PERSONID_INVALID != m_iPersonPId)
		{
			m_person = personList.get(m_iPersonPId);
			if (null != m_person)
			{
				Parents parents = ParentsHelper.getPreferredBloodThenAnyParents(m_person.getParents());
				if (null != parents)
				{
					m_iParentsMId = parents.getMarriageId();
					m_parentMarriage = marriageList.get(m_iParentsMId);
					if (null != m_parentMarriage)
					{
						m_iFatherPId = MarriageHelper.getHusbandPersonId(m_parentMarriage);
						m_iMotherPId = MarriageHelper.getWifePersonId(m_parentMarriage);
						if (PersonIdHelper.PERSONID_INVALID != m_iFatherPId)
						{
							m_father = personList.get(m_iFatherPId);
						}
						if (PersonIdHelper.PERSONID_INVALID != m_iMotherPId)
						{
							m_mother = personList.get(m_iMotherPId);
						}
					}
				}
			}
		}
	}
	
	public Person getPerson()
	{
		return m_person;
	}
	
	public Person getFather()
	{
		return m_father;
	}
	
	public Person getMother()
	{
		return m_mother;
	}
	
	public int getParentsMarriageId()
	{
		return m_iParentsMId;
	}
	
	public String toString()
	{
		StringBuffer sb = new StringBuffer(256);
		if ((PersonIdHelper.PERSONID_INVALID != m_iPersonPId) && (null != m_person))
		{
			sb.append("Person: Id: ").append(m_iPersonPId).append(", ").append(PersonHelper.getPersonName(m_person)).append("\n");
			if ((MarriageIdHelper.MARRIAGEID_INVALID != m_iParentsMId) && (null != m_parentMarriage))
			{
				sb.append("Parent Marriage: Id: ").append(m_iParentsMId).append("\n");
				if ((PersonIdHelper.PERSONID_INVALID != m_iFatherPId) && (null != m_father))
				{
					sb.append("Father: Id: ").append(m_iFatherPId).append(", ").append(PersonHelper.getPersonName(m_father)).append("\n");
				}
				if ((PersonIdHelper.PERSONID_INVALID != m_iMotherPId) && (null != m_mother))
				{
					sb.append("Mother: Id: ").append(m_iMotherPId).append(", ").append(PersonHelper.getPersonName(m_mother)).append("\n");
				}
			}
		}
		return sb.toString();
	}
}
