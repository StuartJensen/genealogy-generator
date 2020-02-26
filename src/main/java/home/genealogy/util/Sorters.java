package home.genealogy.util;

import java.util.ArrayList;

import home.genealogy.lists.PersonList;
import home.genealogy.schema.all.Date;
import home.genealogy.schema.all.Person;
import home.genealogy.schema.all.helpers.DateHelper;
import home.genealogy.schema.all.helpers.PersonHelper;

public class Sorters
{
	public static void sortPeopleInArrayByBirth(ArrayList<Integer> alPersonIds, PersonList personList)
	{
		int[] arPersonIds = new int[alPersonIds.size()];
		for (int i=0; i<alPersonIds.size(); i++)
		{
			arPersonIds[i] = alPersonIds.get(i);
		}
		sortPeopleInArrayByBirth(arPersonIds, personList);
		alPersonIds.clear();
		for (int i=0; i<arPersonIds.length; i++)
		{
			alPersonIds.add(arPersonIds[i]);
		}
	}
	
	public static void sortPeopleInArrayByBirth(int[] arPersonIds, PersonList personList)
	{
		int i,j;
		Person topPerson, bottomPerson;

		// Now sort the people by birth date
		for (i=0; i<(arPersonIds.length-1); i++)
		{
			for (j=0; j<(arPersonIds.length-1); j++)
			{
				boolean bSwap = false;
				topPerson = personList.get(arPersonIds[j]);
				bottomPerson = personList.get(arPersonIds[j+1]);

				Date topDate = PersonHelper.getObjectBirthDate(topPerson);
				Date bottomDate = PersonHelper.getObjectBirthDate(bottomPerson);
				long lFirstFlags = DateHelper.compareDate(topDate, bottomDate);

				if (lFirstFlags == DateHelper.DATE_COMPARE_PARAMETER_BEFORE)
				{
					bSwap = true;
				}
				else if (lFirstFlags == DateHelper.DATE_COMPARE_PARAMETER_AFTER)
				{
					bSwap = false;
				}
				else if (lFirstFlags == DateHelper.DATE_COMPARE_EQUAL)
				{
					bSwap = false;
				}
				else
				{	// Some of the INVALID flags were set, so possibly
					// retry with the Christening date
					if (0 != (lFirstFlags & DateHelper.DATE_COMPARE_THIS_INVALID))
					{
						topDate = PersonHelper.getObjectChrDate(topPerson);
					}
					if (0 != (lFirstFlags & DateHelper.DATE_COMPARE_PARAMETER_INVALID))
					{
						bottomDate = PersonHelper.getObjectChrDate(bottomPerson);
					}
					long lSecondFlags = DateHelper.compareDate(topDate, bottomDate);
					if (0 != (lSecondFlags & DateHelper.DATE_COMPARE_PARAMETER_BEFORE))
					{
						bSwap = true;
					}
					else if (0 != (lSecondFlags & DateHelper.DATE_COMPARE_PARAMETER_AFTER))
					{
						bSwap = false;
					}
					else if (0 != (lSecondFlags & DateHelper.DATE_COMPARE_EQUAL))
					{
						bSwap = false;
					}
				}
				if (bSwap)
				{
					int iTemp = arPersonIds[j];
					arPersonIds[j] = arPersonIds[j+1];
					arPersonIds[j + 1] = iTemp;
				}
			}
		}
	}
	
}
