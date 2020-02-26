package home.genealogy.lists;

import home.genealogy.configuration.CFGFamily;
import home.genealogy.indexes.IndexMarriageToChildren;
import home.genealogy.indexes.IndexPersonToMarriages;
import home.genealogy.schema.all.Marriage;
import home.genealogy.schema.all.Person;
import home.genealogy.schema.all.PersonId;
import home.genealogy.schema.all.helpers.MarriageHelper;
import home.genealogy.schema.all.helpers.MarriageIdHelper;
import home.genealogy.schema.all.helpers.PersonHelper;
import home.genealogy.schema.all.helpers.PersonIdHelper;

import java.util.ArrayList;
import java.util.Iterator;

public class RelationshipManager
{
	public static final String PERSON = "P";
	public static final String FATHER = "F"; 
	public static final String MOTHER = "M"; 
	public static final String CHILD = "C"; 
	public static final String HUSBAND = "H"; 
	public static final String WIFE = "W";
	public static final String UNKNOWN = "X";
	
	public static void setRelationships(CFGFamily family, PersonList personList, MarriageList marriageList,
			                            IndexPersonToMarriages idxPerToMar, IndexMarriageToChildren idxMarToChild)
	{
		PersonId basePersonId = family.getBasePersonId();
		if (null != basePersonId)
		{
			int iBasePersonId = PersonIdHelper.getPersonId(basePersonId);
			if (PersonIdHelper.PERSONID_INVALID != iBasePersonId)
			{
				String strRelationship = PERSON;
				setPrimaryRelationships(iBasePersonId, personList, marriageList, strRelationship);
				
				// While secondary relationships are found, keep setting them.  When we
				// finally set zero we know we are done.
				while (0 != setSecondaryRelationships(personList, marriageList, idxPerToMar, idxMarToChild));
			}
		}
	}
	
	private static void setPrimaryRelationships(int iBasePersonId, PersonList personList, MarriageList marriageList, String strRelationship)
	{
		Person person = personList.get(iBasePersonId);
		if (null != person)
		{
			if ((null == person.getRelationshipToPrimary()) ||
				(0 == person.getRelationshipToPrimary().length()))
			{
				person.setRelationshipToPrimary(strRelationship);
				int iParentId = PersonHelper.getParentId(person);
				if (MarriageIdHelper.MARRIAGEID_INVALID != iParentId)
				{
					Marriage marriage = marriageList.get(iParentId);
					if (null != marriage)
					{
						int iHusbandId = MarriageHelper.getHusbandPersonId(marriage);
						int iWifeId = MarriageHelper.getWifePersonId(marriage);
						if (PersonIdHelper.PERSONID_INVALID != iHusbandId)
						{
							Person husband = personList.get(iHusbandId);
							if (null != husband)
							{
								setPrimaryRelationships(iHusbandId, personList, marriageList, strRelationship + FATHER);
							}
						}
						if (PersonIdHelper.PERSONID_INVALID != iWifeId)
						{
							Person wife = personList.get(iWifeId);
							if (null != wife)
							{
								setPrimaryRelationships(iWifeId, personList, marriageList, strRelationship + MOTHER);
							}
						}
					}
				}
			}
		}
	}
	
	private static int setSecondaryRelationships(PersonList personList, MarriageList marriageList,
			                                     IndexPersonToMarriages idxPerToMar,
			                                     IndexMarriageToChildren idxMarToChild)
	{
		int iCount = 0;
		Iterator<Person> iter = personList.getPersons();
		while (iter.hasNext())
		{
			boolean bPlaced = false;
			Person person = iter.next();
			String strPersonRelationshipToPrimary = person.getRelationshipToPrimary();
			if ((null == strPersonRelationshipToPrimary) ||
				(0 == strPersonRelationshipToPrimary.length()))
			{
				int iPersonId = PersonHelper.getPersonId(person);
				int iParentId = PersonHelper.getParentId(person);
				int iFatherId = PersonIdHelper.PERSONID_INVALID;
				int iMotherId = PersonIdHelper.PERSONID_INVALID;
				// Am I a child of a person that has a "relationship"
	
				if (MarriageIdHelper.MARRIAGEID_INVALID != iParentId)
				{
					Marriage marriage = marriageList.get(iParentId);
					if (null != marriage)
					{
						iFatherId = MarriageHelper.getHusbandPersonId(marriage);
						iMotherId = MarriageHelper.getWifePersonId(marriage);
						if (PersonIdHelper.PERSONID_INVALID != iFatherId)
						{
							Person father = personList.get(iFatherId);
							if ((null != father) && (!bPlaced) &&
								(null != father.getRelationshipToPrimary()) &&
								(0 != father.getRelationshipToPrimary().length()))
							{
								person.setRelationshipToPrimary(father.getRelationshipToPrimary() + CHILD);
								iCount++;
								bPlaced = true;
							}
						}
						if (PersonIdHelper.PERSONID_INVALID != iMotherId)
						{
							Person mother = personList.get(iMotherId);
							if ((null != mother) && (!bPlaced) &&
								(null != mother.getRelationshipToPrimary()) &&
								(0 != mother.getRelationshipToPrimary().length()))
							{
								person.setRelationshipToPrimary(mother.getRelationshipToPrimary() + CHILD);
								iCount++;
								bPlaced = true;
							}
						}
					}
				}
				
				// Am I a spouse of a "related" person
				if ((!bPlaced) && (PersonIdHelper.PERSONID_INVALID != iPersonId))
				{
					ArrayList<Integer> alMarriageIds = idxPerToMar.getMarriageIds(iPersonId);
					for (int m=0; m<alMarriageIds.size() && (!bPlaced); m++)
					{
						Marriage marriage = marriageList.get(alMarriageIds.get(m).intValue());
						if (null != marriage)
						{
							int iHusbandId = MarriageHelper.getHusbandPersonId(marriage);
							int iWifeId = MarriageHelper.getWifePersonId(marriage);
							if (PersonIdHelper.PERSONID_INVALID != iHusbandId)
							{
								if (iPersonId == iHusbandId)
								{	// I am the husband, does my wife have a relationship?
									Person wife = personList.get(iWifeId);
									if (null != wife)
									{
										String strWifeRelationship = wife.getRelationshipToPrimary();
										if ((null != strWifeRelationship) && (0 != strWifeRelationship.length()))
										{
											person.setRelationshipToPrimary(strWifeRelationship + HUSBAND);
											iCount++;
											bPlaced = true;
										}
									}
								}
							}
							if ((!bPlaced) && (PersonIdHelper.PERSONID_INVALID != iWifeId))
							{
								if (iPersonId == iWifeId)
								{	// I am the wife, does my husband have a relationship?
									Person husband = personList.get(iHusbandId);
									if (null != husband)
									{
										String strHusbandRelationship = husband.getRelationshipToPrimary();
										if ((null != strHusbandRelationship) && (0 != strHusbandRelationship.length()))
										{
											person.setRelationshipToPrimary(strHusbandRelationship + WIFE);
											iCount++;
											bPlaced = true;
										}
									}
								}
							}
						}
					}
				}
				
				// Am I a parent of a "related" person
				if (!bPlaced)
				{
					if ((!bPlaced) && (PersonIdHelper.PERSONID_INVALID != iPersonId))
					{
						ArrayList<Integer> alMarriageIds = idxPerToMar.getMarriageIds(iPersonId);
						for (int m=0; m<alMarriageIds.size() && (!bPlaced); m++)
						{
							Marriage marriage = marriageList.get(alMarriageIds.get(m).intValue());
							if (null != marriage)
							{
								ArrayList<Integer> alChildrenIds = idxMarToChild.getChildrenPersonIds(alMarriageIds.get(m).intValue());
								for (int c=0; c<alChildrenIds.size(); c++)
								{
									int iChildId = alChildrenIds.get(c).intValue();
									Person child = personList.get(iChildId);
									if ((null != child) && (!bPlaced) &&
										(null != child.getRelationshipToPrimary()) &&
										(0 != child.getRelationshipToPrimary().length()))
									{
										String strPaternalSpecifier = UNKNOWN;
										String strPersonGender = PersonHelper.getGender(child);
										if (null != strPersonGender)
										{
											if (strPersonGender.equalsIgnoreCase("MALE"))
											{
												strPaternalSpecifier = FATHER;
											}
											else if (strPersonGender.equalsIgnoreCase("FEMALE"))
											{
												strPaternalSpecifier = MOTHER;
											}
										}
										person.setRelationshipToPrimary(child.getRelationshipToPrimary() + strPaternalSpecifier);
										iCount++;
										bPlaced = true;
									}
								}
							}
						}
					}
				}
			}
		}
		System.out.println("Secondary Relationships: " + iCount);
		return iCount;
	}
	
/*	


	int Family::setSecondaryRelationships()
	{
		int iCount = 0;
		long lCookie;
		IndexPersonToMarriages *pIndexPersonToMarriages = m_familyIndex.GetIndexPersonToMarriages();
		IndexMarriageToChildren *pIndexMarriageToChildren = m_familyIndex.GetIndexMarriageToChildren();
		long lMarriageCookie;
		MARRIAGEID marriageId;
		Person *pPerson;
		if (m_personList.elements(&lCookie))
		{
			while (m_personList.nextElement(&lCookie, &pPerson))
			{
				if (pPerson)
				{
					PERSONID personId = pPerson->getPersonIdAsLong();
					boolean bPlaced = false;
					CString strRelationship = pPerson->getRelationship();
					if (0 == strRelationship.GetLength())
					{
						FourGenerations personFourGenerations(this, pPerson->getPersonIdAsLong());
						MARRIAGEID parentMId = personFourGenerations.getFatherMarriageId();
						Person *pFather = personFourGenerations.getFather();
						Person *pMother = personFourGenerations.getMother();

						// Am I a child of a "related" person
						if (pFather && !bPlaced && (0 != pFather->getRelationship().GetLength()))
						{
							pPerson->setRelationship(pFather->getRelationship() + "C");
							iCount++;
							bPlaced = true;
						}
						else if (pMother && !bPlaced && (0 != pMother->getRelationship().GetLength()))
						{
							pPerson->setRelationship(pMother->getRelationship() + "C");
							iCount++;
							bPlaced = true;
						}

						// Am I a spouse of a "related" person
						if (!bPlaced)
						{
							if (pIndexPersonToMarriages->StartMarriagesOfPersonEnum(personId, &lMarriageCookie))
							{
								while (pIndexPersonToMarriages->NextMarriagesOfPersonEnum(personId, &lMarriageCookie, &marriageId))
								{
									Marriage *pMarriage = m_marriageList.get(marriageId);
									if (pMarriage)
									{
										PERSONID idHusband = pMarriage->getHusbandIdAsLong();
										PERSONID idWife = pMarriage->getWifeIdAsLong();
										ASSERT(PERSONID_INVALID != idHusband);
										ASSERT(PERSONID_INVALID != idWife);
										if (personId == idHusband)
										{	// I am the husband,  does my wife have a relationship?
											Person *pWife = m_personList.get(idWife);
											if (pWife && (0 != pWife->getRelationship().GetLength()))
											{
												pPerson->setRelationship(pWife->getRelationship() + "H");
												iCount++;
												bPlaced = true;
											}
										}
										else
										{	// I am the wife, does my husband have a relationship?
											Person *pHusband = m_personList.get(idHusband);
											if (pHusband && (0 != pHusband->getRelationship().GetLength()))
											{
												pPerson->setRelationship(pHusband->getRelationship() + "W");
												iCount++;
												bPlaced = true;
											}
										}
									}
								}
							}
						}
						// Am I a parent of a "related" person
						if (!bPlaced)
						{
							if (pIndexPersonToMarriages->StartMarriagesOfPersonEnum(personId, &lMarriageCookie))
							{
								while (!bPlaced && pIndexPersonToMarriages->NextMarriagesOfPersonEnum(personId, &lMarriageCookie, &marriageId))
								{
									long lChildrenCookie;
									PERSONID childId;
									if (pIndexMarriageToChildren->StartChildrenOfMarriageEnum(marriageId, &lChildrenCookie))
									{
										while (!bPlaced && pIndexMarriageToChildren->NextChildrenOfMarriageEnum(marriageId, &lChildrenCookie, &childId))
										{
											Person *pChild = m_personList.get(childId);
											if (pChild)
											{
												if (0 != pChild->getRelationship().GetLength())
												{
													CString strPaternalSpecifier = "M";
													if (0 == pPerson->getGender().Compare("MALE"))
													{
														strPaternalSpecifier = "F";
													}
													pPerson->setRelationship(pChild->getRelationship() + strPaternalSpecifier);
													iCount++;
													bPlaced = true;
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return iCount;
	}
*/
}
