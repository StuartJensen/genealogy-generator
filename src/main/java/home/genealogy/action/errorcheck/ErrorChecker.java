package home.genealogy.action.errorcheck;

import home.genealogy.configuration.CFGFamily;
import home.genealogy.lists.MarriageList;
import home.genealogy.lists.PersonList;
import home.genealogy.lists.PhotoList;
import home.genealogy.lists.ReferenceList;
import home.genealogy.output.IOutputStream;
import home.genealogy.schema.all.Entry;
import home.genealogy.schema.all.Marriage;
import home.genealogy.schema.all.MarriageTag;
import home.genealogy.schema.all.Person;
import home.genealogy.schema.all.PersonTag;
import home.genealogy.schema.all.Photo;
import home.genealogy.schema.all.Reference;
import home.genealogy.schema.all.helpers.EntryHelper;
import home.genealogy.schema.all.helpers.FileHelper;
import home.genealogy.schema.all.helpers.MarriageHelper;
import home.genealogy.schema.all.helpers.MarriageIdHelper;
import home.genealogy.schema.all.helpers.MarriageTagHelper;
import home.genealogy.schema.all.helpers.PersonHelper;
import home.genealogy.schema.all.helpers.PersonIdHelper;
import home.genealogy.schema.all.helpers.PersonTagHelper;
import home.genealogy.schema.all.helpers.PhotoHelper;
import home.genealogy.schema.all.helpers.ReferenceHelper;
import home.genealogy.util.CommandLineParameterList;

import java.util.Iterator;
import java.util.List;

public class ErrorChecker
{
	private CFGFamily m_family;
	private PersonList m_personList;
	private MarriageList m_marriageList;
	private ReferenceList m_referenceList;
	private PhotoList m_photoList;
	private CommandLineParameterList m_listCLP;
	private IOutputStream m_outputStream;
	
	public ErrorChecker(CFGFamily family, PersonList personList, MarriageList marriageList,
			            ReferenceList referenceList, PhotoList photoList, CommandLineParameterList listCLP,
			            IOutputStream outputStream)
	{
		m_family = family;
		m_listCLP = listCLP;
		m_personList = personList;
		m_marriageList =  marriageList;
		m_referenceList = referenceList;
		m_photoList = photoList;
		m_outputStream = outputStream;
	}
	
	public void check()
	{
		allParentIdsReferenceMarriage(m_personList, m_marriageList);
		allMarriagesReferencePeople(m_personList, m_marriageList);
		allHusbandsAreMale(m_personList, m_marriageList);
		allWivesAreFemale(m_personList, m_marriageList);
        allReferenceTagsAreValid(m_personList, m_marriageList, m_referenceList);
    	allLivingAreNotDead(m_personList);
    	allDeadThatMayBeDeemedAsLiving(m_personList, m_marriageList);
    	allPhotosExist(m_family, m_photoList);
	}
	
	public void allParentIdsReferenceMarriage(PersonList personList, MarriageList marriageList)
    {
	    Iterator<Person> iter = personList.getPersons();
		while (iter.hasNext())
		{
		    Person person = iter.next();
            int iParentId = PersonHelper.getParentId(person);
			if (iParentId != MarriageIdHelper.MARRIAGEID_INVALID)
			{ // Look for a marriage with this marriage id
			    Marriage marriage = marriageList.get(iParentId);
				if (null == marriage)
				{
				    int iPersonId = PersonHelper.getPersonId(person);
				    m_outputStream.output("ERROR: Parent id [" + iParentId + "] for Person [" + iPersonId + "] references an unavailable Marriage!");
				}
			}
		}
    }
	
	public void allMarriagesReferencePeople(PersonList personList, MarriageList marriageList)
    {
	    Iterator<Marriage> iter = marriageList.getMarriages();
		while (iter.hasNext())
		{
			Marriage marriage = iter.next();
			int iMarriageId = MarriageHelper.getMarriageId(marriage);
            int iHusbandId = MarriageHelper.getHusbandPersonId(marriage);
            int iWifeId = MarriageHelper.getWifePersonId(marriage);
            if (MarriageIdHelper.MARRIAGEID_INVALID == iHusbandId)
            {
            	m_outputStream.output("ERROR: Husband id for Marriage [" + iMarriageId + "] does not exist!");
            }
            else
            {  	// Look for a husband with this person id
            	Person husband = personList.get(iHusbandId);
				if (null == husband)
				{
					m_outputStream.output("ERROR: Husband id [" + iHusbandId + "] for Marriage [" + iMarriageId + "] references an unavailable Person!");
				}
            }
            
            if (MarriageIdHelper.MARRIAGEID_INVALID == iWifeId)
            {
            	m_outputStream.output("ERROR: Wife id for Marriage [" + iMarriageId + "] does not exist!");
            }
            else
            {  	// Look for a wife with this person id
            	Person wife = personList.get(iWifeId);
				if (null == wife)
				{
					m_outputStream.output("ERROR: Wife id [" + iWifeId + "] for Marriage [" + iMarriageId + "] references an unavailable Person!");
				}
            }
		}
    }
	
	
	public void allHusbandsAreMale(PersonList personList, MarriageList marriageList)
    {
	    Iterator<Marriage> iter = marriageList.getMarriages();
		while (iter.hasNext())
		{
			Marriage marriage = iter.next();
			int iMarriageId = MarriageHelper.getMarriageId(marriage);
            int iHusbandId = MarriageHelper.getHusbandPersonId(marriage);
            if (MarriageIdHelper.MARRIAGEID_INVALID != iHusbandId)
            {  	// Look for a husband with this person id
            	Person husband = personList.get(iHusbandId);
				if (null != husband)
				{
					String strGender = PersonHelper.getGender(husband);
					if (null == strGender)
					{
						m_outputStream.output("ERROR: Husband id [" + iHusbandId + "] for Marriage [" + iMarriageId + "] has no gender!");						
					}
					else
					{
						if (!strGender.equals("MALE"))
						{
							m_outputStream.output("ERROR: Husband id [" + iHusbandId + "] for Marriage [" + iMarriageId + "] is not MALE!");	
						}
					}
				}
            }
		}
    }
	
	public void allWivesAreFemale(PersonList personList, MarriageList marriageList)
    {
	    Iterator<Marriage> iter = marriageList.getMarriages();
		while (iter.hasNext())
		{
			Marriage marriage = iter.next();
			int iMarriageId = MarriageHelper.getMarriageId(marriage);
            int iWifeId = MarriageHelper.getWifePersonId(marriage);
            if (MarriageIdHelper.MARRIAGEID_INVALID != iWifeId)
            {  	// Look for a wife with this person id
            	Person wife = personList.get(iWifeId);
				if (null != wife)
				{
					String strGender = PersonHelper.getGender(wife);
					if (null == strGender)
					{
						m_outputStream.output("ERROR: Wife id [" + iWifeId + "] for Marriage [" + iMarriageId + "] has no gender!");						
					}
					else
					{
						if (!strGender.equals("FEMALE"))
						{
							m_outputStream.output("ERROR: Wife id [" + iWifeId + "] for Marriage [" + iMarriageId + "] is not FEMALE!");	
						}
					}
				}
            }
		}
    }
	
	public void allLivingAreNotDead(PersonList personList)
    {
	    Iterator<Person> iter = personList.getPersons();
		while (iter.hasNext())
		{
		    Person person = iter.next();
		    if (PersonHelper.isMarkedAsLiving(person))
		    {	// The "living" attribute explicitly says this person is living
				// Look for death information
				if ((PersonHelper.hasAnyDeathInfo(person)) ||
					(PersonHelper.hasAnyBurialInfo(person)))			
				{	// Has a death/burial date, must be dead
				    m_outputStream.output("ERROR: Person id [" + PersonHelper.getPersonId(person) + "] is explicitly marked as \"living\" but death information exists.\n");
				}
				// Look at age
				int iAge = PersonHelper.getApproximateAge(person);
				if (iAge >= 110)
				{	// We assume all people older than 110 years old are dead
				    m_outputStream.output("ERROR: Person id [" + PersonHelper.getPersonId(person) + "] is explicitly marked as \"living\" but their age is more than 100 years.\n");
				}
		    }
		}
    }
	
	public void allDeadThatMayBeDeemedAsLiving(PersonList personList, MarriageList marriageList)
    {
		int iCount = 0;
	    Iterator<Person> iter = personList.getPersons();
		while (iter.hasNext())
		{
		    Person person = iter.next();
		    if (PersonHelper.isLiving(person))
		    {	// We think this person is living. Was that due to an explicit mark?
			    if (!PersonHelper.isMarkedAsLiving(person))
			    {	// The "living" attribute does not explicitly says this person is living
			    	//
			    	// Look at the spouse(s) to see when they were alive
			    	int iPersonId = PersonHelper.getPersonId(person);
			    	Iterator<Marriage> iterMarriages = marriageList.getMarriages();
			    	while (iterMarriages.hasNext())
			    	{
			    		Marriage marriage = iterMarriages.next();
			    		int iMarriageId = MarriageHelper.getMarriageId(marriage);
			    		int iSpousePersonId = PersonIdHelper.PERSONID_INVALID;
			    		if (iPersonId == MarriageHelper.getHusbandPersonId(marriage))
			    		{
			    			iSpousePersonId = MarriageHelper.getWifePersonId(marriage);
			    		}
			    		else if (iPersonId == MarriageHelper.getWifePersonId(marriage))
			    		{
			    			iSpousePersonId = MarriageHelper.getHusbandPersonId(marriage);
			    		}
			    		if (PersonIdHelper.PERSONID_INVALID != iSpousePersonId)
			    		{	// Found a spouse
			    			Person spouse = personList.get(iSpousePersonId);
			    			int iSpouseAge = PersonHelper.getApproximateAge(spouse);
			    			if (iSpouseAge >= 110)
			    			{
							    m_outputStream.output("INFO: Person id [" + PersonHelper.getPersonId(person) + "] evaluates as \"living\" but they have a spouse [" + PersonHelper.getPersonId(spouse) + "] older than 110 years.\n");
							    iCount++;
			    			}
					    	// Look at the children to see when they were alive
				    	    Iterator<Person> iterChildren = personList.getPersons();
				    		while (iterChildren.hasNext())
				    		{
				    		    Person possibleChild = iterChildren.next();
				    		    int iParentId = PersonHelper.getParentId(possibleChild);
				    		    if (MarriageIdHelper.MARRIAGEID_INVALID != iParentId)
				    		    {
					    		    if (iParentId == iMarriageId)
					    		    {	// This indeed is a child
						    			int iChildAge = PersonHelper.getApproximateAge(possibleChild);
						    			if (iChildAge >= 80)
						    			{
										    m_outputStream.output("INFO: Person id [" + PersonHelper.getPersonId(person) + "] evaluates as \"living\" but they have a child [" + PersonHelper.getPersonId(possibleChild) + "] older than 80 years.\n");
										    iCount++;
						    			}
					    		    }
				    		    }
				    		}
			    		}
			    	}
			    }
		    }
		}
	    m_outputStream.output("INFO: There were " + iCount + " reasons that dead people may be deemed living!\n");
    }
	
	public void allReferenceTagsAreValid(PersonList personList, MarriageList marriageList, ReferenceList referenceList)
    {
	    Iterator<Reference> iter = referenceList.getReferences();
		while (iter.hasNext())
		{
			Reference reference = iter.next();
			int iReferenceId = ReferenceHelper.getReferenceId(reference);
			int iEntryCount = ReferenceHelper.getEntryCount(reference);
			for (int r=0; r<iEntryCount; r++)
			{
				Entry entry = ReferenceHelper.getEntry(reference, r);
				// Examine Person Tags
				List<PersonTag> lPersonTags = EntryHelper.getPersonTagList(entry);
				if (null != lPersonTags)
				{
					for (int t=0; t<lPersonTags.size(); t++)
					{
						PersonTag personTag = lPersonTags.get(t);
						int iPersonId = PersonTagHelper.getPersonId(personTag);
						if (PersonIdHelper.PERSONID_INVALID == iPersonId)
						{
							m_outputStream.output("ERROR: For Reference [" + iReferenceId + "] entry [" + r + "], there is a Person Tag with no Person Id!");
						}
						else
						{	// Look for a person with this person id
			            	Person person = personList.get(iPersonId);
							if (null == person)
							{
								m_outputStream.output("ERROR: For Reference [" + iReferenceId + "] entry [" + r + "], there is a Person Tag with a Person Id [" + iPersonId + "] that does not reference a Person!");
						    }
						}
					}
				}
				// Now examine Marriage tags
				List<MarriageTag> lMarriageTags = EntryHelper.getMarriageTagList(entry);
				if (null != lMarriageTags)
				{
					for (int t=0; t<lMarriageTags.size(); t++)
					{
						MarriageTag marriageTag = lMarriageTags.get(t);
						int iMarriageId = MarriageTagHelper.getMarriageId(marriageTag);
						if (MarriageIdHelper.MARRIAGEID_INVALID == iMarriageId)
						{
							m_outputStream.output("ERROR: For Reference [" + iReferenceId + "] entry [" + r + "], there is a Marraige Tag with no Marriage Id!");
						}
						else
						{	// Look for a marriage with this marriage id
			            	Marriage marriage = marriageList.get(iMarriageId);
							if (null == marriage)
							{
								m_outputStream.output("ERROR: For Reference [" + iReferenceId + "] entry [" + r + "], there is a Marraige Tag with a Marriage Id [" + iMarriageId + "] that does not reference a Marriage!");
						    }
						}
					}
				}
			}
		}
    }
	
	public void allPhotoTagsAreValid(PersonList personList, MarriageList marriageList, PhotoList photoList)
    {
	    Iterator<Photo> iter = photoList.getPhotos();
		while (iter.hasNext())
		{
			Photo photo = iter.next();
			int iPhotoId = PhotoHelper.getPhotoId(photo);
			// @TODO Implement
		}
    }

	public void allPhotosExist(CFGFamily family, PhotoList photoList)
    {
		long lMissingPhotoFiles = 0;
		long lMissingPhotoSizes = 0;
		long lOutOfRangePhotoSizes = 0;
		String strPhotoPath = family.getPhotoPath();
	    Iterator<Photo> iter = photoList.getPhotos();
		while (iter.hasNext())
		{
			StringBuffer sb = new StringBuffer(256);
			Photo photo = iter.next();
			int iPhotoId = PhotoHelper.getPhotoId(photo);
			sb.append("Photo #").append(iPhotoId).append("\n");
			List<home.genealogy.schema.all.File> lFiles = PhotoHelper.getFiles(photo);
			String strPackage = PhotoHelper.getPackage(photo);
			String strBaseFileName = PhotoHelper.getBaseFileName(photo);
			for (int f=0; f<lFiles.size(); f++)
			{
				home.genealogy.schema.all.File file = lFiles.get(f);
				String strPhotoFileName = strPhotoPath + "\\" + strPackage + "\\" + strBaseFileName + FileHelper.getUniqueId(file) + "." + FileHelper.getTypeExtension(file);
				java.io.File fileOnDisk = new java.io.File(strPhotoFileName);
				if (!fileOnDisk.exists())
				{
					sb.append("    Missing disk photo file: " + strPhotoFileName).append("\n");
					lMissingPhotoFiles++;
				}
				else
				{
					long lActualFileLength = fileOnDisk.length();
					String strFileSize = file.getSize();
					if (null == strFileSize)
					{
						sb.append("    Missing disk photo file size: " + strPhotoFileName).append("\n");
						lMissingPhotoSizes++;
					}
					else if (0 == strFileSize.length())
					{
						sb.append("    Empty disk photo file size: " + strPhotoFileName).append("\n");
						lMissingPhotoSizes++;
					}
					else
					{
						long lDocumentedFileSize = Long.parseLong(strFileSize);
						// Convert from kilo bytes to bytes
						lDocumentedFileSize *= 1000;
						// Find a +/-10% range around the actual file length and
						// then make sure the documented size is within that range.
						long lTopRange = lActualFileLength + ((lActualFileLength * 10)/100);
						long lBottomRange = lActualFileLength - ((lActualFileLength * 10)/100);
						if ((lDocumentedFileSize > lTopRange) || (lDocumentedFileSize < lBottomRange))
						{
							sb.append("    Disk photo file size out of range: ").append(strPhotoFileName).append("\n");
							sb.append("      Actual: ").append(lActualFileLength).append(", Documented: ").append(lDocumentedFileSize).append("\n");
							lOutOfRangePhotoSizes++;
						}
					}
				}
			}
			if (sb.length() > 15)
			{
				m_outputStream.output(sb.toString());
			}
		}
		m_outputStream.output("Missing photo disk files: " + lMissingPhotoFiles + "\n");
		m_outputStream.output("Missing photo disk sizes: " + lMissingPhotoSizes + "\n");
		m_outputStream.output("Out of range photo sizes: " + lOutOfRangePhotoSizes + "\n");
    }
}
