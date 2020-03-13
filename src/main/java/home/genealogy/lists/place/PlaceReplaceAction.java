package home.genealogy.lists.place;

import java.util.Iterator;

import home.genealogy.GenealogyContext;
import home.genealogy.lists.MarriageList;
import home.genealogy.lists.PersonList;
import home.genealogy.lists.PhotoList;
import home.genealogy.lists.PlaceList;
import home.genealogy.lists.ReferenceList;
import home.genealogy.output.IOutputStream;
import home.genealogy.schema.all.Marriage;
import home.genealogy.schema.all.Person;
import home.genealogy.schema.all.Photo;
import home.genealogy.schema.all.PlaceName;
import home.genealogy.schema.all.Reference;
import home.genealogy.schema.all.helpers.MarriageHelper;
import home.genealogy.schema.all.helpers.PersonHelper;
import home.genealogy.schema.all.helpers.PhotoHelper;
import home.genealogy.schema.all.helpers.ReferenceHelper;

public class PlaceReplaceAction
	extends PlaceActionBase
{
	public static final String ACTION = "replace";
	
	private String m_strToBeReplaced;
	private String m_strReplacement;
	private String m_strLocale;
	private String m_strStreet;
	private String m_strSpot;
	
	public PlaceReplaceAction(String strToBeReplaced, String strReplacement)
	{
		m_strToBeReplaced = strToBeReplaced;
		m_strReplacement = strReplacement;
	}
	
	public void setLocale(String strLocale)
	{
		m_strLocale = strLocale;
	}
	
	public void setStreet(String strStreet)
	{
		m_strStreet = strStreet;
	}
	
	public void setSpot(String strSpot)
	{
		m_strSpot = strSpot;
	}
	
	public String getToBeReplaced()
	{
		return m_strToBeReplaced;
	}
	
	public String getReplacement()
	{
		return m_strReplacement;
	}
	
	public void execute(GenealogyContext context)
		throws PlaceActionException
	{
		context.output("Executing Place Command: Replace: Place Id To Be Replaced " + getToBeReplaced() + ", Replacement: " + getReplacement() + "\n");
		int iAllCount = 0;
		Iterator<Person> iterPersons = context.getPersonList().getPersons();
		while (iterPersons.hasNext())
		{
			int iCount = PersonHelper.replacePlaceId(iterPersons.next(), getToBeReplaced(), getReplacement(), m_strLocale, m_strStreet, m_strSpot, context.getOutputStream());
			if (0 != iCount)
			{
				iAllCount += iCount;
				markPersonListModified();
			}
		}
		Iterator<Marriage> iterMarriages = context.getMarriageList().getMarriages();
		while (iterMarriages.hasNext())
		{
			int iCount = MarriageHelper.replacePlaceId(iterMarriages.next(), getToBeReplaced(), getReplacement(), m_strLocale, m_strStreet, m_strSpot, context.getOutputStream());
			if (0 != iCount)
			{
				iAllCount += iCount;
				markMarriageListModified();
			}
		}
		Iterator<Reference> iterReferences = context.getReferenceList().getReferences();
		while (iterReferences.hasNext())
		{
			int iCount = ReferenceHelper.replacePlaceId(iterReferences.next(), getToBeReplaced(), getReplacement(), m_strLocale, m_strStreet, m_strSpot, context.getOutputStream());
			if (0 != iCount)
			{
				iAllCount += iCount;
				markReferenceListModified();
			}
		}
		Iterator<Photo> iterPhotos = context.getPhotoList().getPhotos();
		while (iterPhotos.hasNext())
		{
			int iCount = PhotoHelper.replacePlaceId(iterPhotos.next(), getToBeReplaced(), getReplacement(), m_strLocale, m_strStreet, m_strSpot, context.getOutputStream());
			if (0 != iCount)
			{
				iAllCount += iCount;
				markPhotoListModified();
			}
		}
		
		if (0 == iAllCount)
		{
			context.output("  WARNING: Zero instances of the place id: " + getToBeReplaced() + " found. No replacements with " + getReplacement() + " performed!\n");
		}
		else
		{
			context.output("  Replaced: " + iAllCount + " place ids.\n");
		}
	}
}
