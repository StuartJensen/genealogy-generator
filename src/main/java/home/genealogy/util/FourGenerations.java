package home.genealogy.util;

import home.genealogy.lists.MarriageList;
import home.genealogy.lists.PersonList;
import home.genealogy.schema.all.Person;
import home.genealogy.schema.all.helpers.MarriageIdHelper;
import home.genealogy.schema.all.helpers.PersonHelper;
import home.genealogy.schema.all.helpers.PersonIdHelper;

public class FourGenerations
{
	// Person - GEN 1
	// Parents - GEN 2
	private TwoGenerations personAndParents;
	
	// Grand Parents (Father's side) - GEN 3a
	private TwoGenerations fatherAndParents;
	// Grand Parents (Mother's side) - GEN 3b
	private TwoGenerations motherAndParents;
	
	// Great Grand Parents (Father's Father's side) - GEN 4a
	private TwoGenerations paternalGrandfatherAndParents;
	// Great Grand Parents (Father's Mother's side) - GEN 4b
	private TwoGenerations paternalGrandmotherAndParents;
	// Great Grand Parents (Mother's Father's side) - GEN 4c
	private TwoGenerations maternalGrandfatherAndParents;
	// Great Grand Parents (Mother's Mother's side) - GEN 4d
	private TwoGenerations maternalGrandmotherAndParents;
	
	public FourGenerations(int iPersonId, PersonList personList, MarriageList marriageList)
	{
		personAndParents = new TwoGenerations(iPersonId, personList, marriageList);
		Person father = personAndParents.getFather();
		if ((null != father) && (PersonIdHelper.PERSONID_INVALID != PersonHelper.getPersonId(father)))
		{
			fatherAndParents = new TwoGenerations(PersonHelper.getPersonId(father), personList, marriageList);
			Person paternalGrandfather = fatherAndParents.getFather();
			if ((null != paternalGrandfather) && (PersonIdHelper.PERSONID_INVALID != PersonHelper.getPersonId(paternalGrandfather)))
			{
				paternalGrandfatherAndParents = new TwoGenerations(PersonHelper.getPersonId(paternalGrandfather), personList, marriageList);
			}
			Person paternalGrandmother = fatherAndParents.getMother();
			if ((null != paternalGrandmother) && (PersonIdHelper.PERSONID_INVALID != PersonHelper.getPersonId(paternalGrandmother)))
			{
				paternalGrandmotherAndParents = new TwoGenerations(PersonHelper.getPersonId(paternalGrandmother), personList, marriageList);
			}
		}
		Person mother = personAndParents.getMother();
		if ((null != mother) && (PersonIdHelper.PERSONID_INVALID != PersonHelper.getPersonId(mother)))
		{
			motherAndParents = new TwoGenerations(PersonHelper.getPersonId(mother), personList, marriageList);
			Person maternalGrandfather = motherAndParents.getFather();
			if ((null != maternalGrandfather) && (PersonIdHelper.PERSONID_INVALID != PersonHelper.getPersonId(maternalGrandfather)))
			{
				maternalGrandfatherAndParents = new TwoGenerations(PersonHelper.getPersonId(maternalGrandfather), personList, marriageList);
			}
			Person maternalGrandmother = motherAndParents.getMother();
			if ((null != maternalGrandmother) && (PersonIdHelper.PERSONID_INVALID != PersonHelper.getPersonId(maternalGrandmother)))
			{
				maternalGrandmotherAndParents = new TwoGenerations(PersonHelper.getPersonId(maternalGrandmother), personList, marriageList);
			}
		}
	}
	
	public Person getPerson()
	{
		if (null != personAndParents)
		{
			return personAndParents.getPerson();
		}
		return null;
	}
	
	public int getParentsMarriageId()
	{
		if (null != personAndParents)
		{
			return personAndParents.getParentsMarriageId();
		}
		return MarriageIdHelper.MARRIAGEID_INVALID;
	}
	
	public Person getFather()
	{
		if (null != personAndParents)
		{
			return personAndParents.getFather();
		}
		return null;
	}
	
	public Person getMother()
	{
		if (null != personAndParents)
		{
			return personAndParents.getMother();
		}
		return null;
	}
	
	public int getPaternalGrandParentsMarriageId()
	{
		if (null != fatherAndParents)
		{
			return fatherAndParents.getParentsMarriageId();
		}
		return MarriageIdHelper.MARRIAGEID_INVALID;
	}
	
	public Person getPaternalGrandFather()
	{
		if (null != fatherAndParents)
		{
			return fatherAndParents.getFather();
		}
		return null;
	}
	
	public Person getPaternalGrandMother()
	{
		if (null != fatherAndParents)
		{
			return fatherAndParents.getMother();
		}
		return null;
	}
	
	public int getMaternalGrandParentsMarriageId()
	{
		if (null != motherAndParents)
		{
			return motherAndParents.getParentsMarriageId();
		}
		return MarriageIdHelper.MARRIAGEID_INVALID;
	}
	
	public Person getMaternalGrandFather()
	{
		if (null != motherAndParents)
		{
			return motherAndParents.getFather();
		}
		return null;
	}
	
	public Person getMaternalGrandMother()
	{
		if (null != motherAndParents)
		{
			return motherAndParents.getMother();
		}
		return null;
	}
	
	public int getPaternalGrandFathersFathersMarriageId()
	{
		if (null != paternalGrandfatherAndParents)
		{
			return paternalGrandfatherAndParents.getParentsMarriageId();
		}
		return MarriageIdHelper.MARRIAGEID_INVALID;
	}
	
	public Person getPaternalGrandFathersFather()
	{
		if (null != paternalGrandfatherAndParents)
		{
			return paternalGrandfatherAndParents.getFather();
		}
		return null;
	}
	
	public Person getPaternalGrandFathersMother()
	{
		if (null != paternalGrandfatherAndParents)
		{
			return paternalGrandfatherAndParents.getMother();
		}
		return null;
	}
	
	public int getPaternalGrandMothersFathersMarriageId()
	{
		if (null != paternalGrandmotherAndParents)
		{
			return paternalGrandmotherAndParents.getParentsMarriageId();
		}
		return MarriageIdHelper.MARRIAGEID_INVALID;
	}
	
	public Person getPaternalGrandMothersFather()
	{
		if (null != paternalGrandmotherAndParents)
		{
			return paternalGrandmotherAndParents.getFather();
		}
		return null;
	}
	
	public Person getPaternalGrandMothersMother()
	{
		if (null != paternalGrandmotherAndParents)
		{
			return paternalGrandmotherAndParents.getMother();
		}
		return null;
	}
	
	public int getMaternalGrandFathersFathersMarriageId()
	{
		if (null != maternalGrandfatherAndParents)
		{
			return maternalGrandfatherAndParents.getParentsMarriageId();
		}
		return MarriageIdHelper.MARRIAGEID_INVALID;
	}
	
	public Person getMaternalGrandFathersFather()
	{
		if (null != maternalGrandfatherAndParents)
		{
			return maternalGrandfatherAndParents.getFather();
		}
		return null;
	}
	
	public Person getMaternalGrandFathersMother()
	{
		if (null != maternalGrandfatherAndParents)
		{
			return maternalGrandfatherAndParents.getMother();
		}
		return null;
	}
	
	public int getMaternalGrandMothersFathersMarriageId()
	{
		if (null != maternalGrandmotherAndParents)
		{
			return maternalGrandmotherAndParents.getParentsMarriageId();
		}
		return MarriageIdHelper.MARRIAGEID_INVALID;
	}
	
	public Person getMaternalGrandMothersFather()
	{
		if (null != maternalGrandmotherAndParents)
		{
			return maternalGrandmotherAndParents.getFather();
		}
		return null;
	}
	
	public Person getMaternalGrandMothersMother()
	{
		if (null != maternalGrandmotherAndParents)
		{
			return maternalGrandmotherAndParents.getMother();
		}
		return null;
	}
	
	public String toString()
	{
		StringBuffer sb = new StringBuffer(256);
		if (null != personAndParents)
		{
			sb.append("Person and Parents:\n").append(personAndParents.toString());
		}
		if (null != fatherAndParents)
		{
			sb.append("Father and Parents:\n").append(fatherAndParents.toString());
		}
		if (null != motherAndParents)
		{
			sb.append("Mother and Parents:\n").append(motherAndParents.toString());
		}
		if (null != paternalGrandfatherAndParents)
		{
			sb.append("Father's Father and Parents:\n").append(paternalGrandfatherAndParents.toString());
		}
		if (null != paternalGrandmotherAndParents)
		{
			sb.append("Father's Mother and Parents:\n").append(paternalGrandmotherAndParents.toString());
		}
		if (null != maternalGrandfatherAndParents)
		{
			sb.append("Mother's Father and Parents:\n").append(maternalGrandfatherAndParents.toString());
		}
		if (null != maternalGrandmotherAndParents)
		{
			sb.append("Mother's Mother and Parents:\n").append(maternalGrandmotherAndParents.toString());
		}
		return sb.toString();
	}
	
}
