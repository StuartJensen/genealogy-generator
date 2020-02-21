package home.genealogy.indexes;

import home.genealogy.configuration.CFGFamily;
import home.genealogy.lists.PersonList;
import home.genealogy.lists.ReferenceList;
import home.genealogy.schema.all.Entry;
import home.genealogy.schema.all.Person;
import home.genealogy.schema.all.PersonTag;
import home.genealogy.schema.all.PersonTagType;
import home.genealogy.schema.all.Reference;
import home.genealogy.schema.all.Tag;
import home.genealogy.schema.all.helpers.EntryHelper;
import home.genealogy.schema.all.helpers.PersonTagHelper;
import home.genealogy.schema.all.helpers.ReferenceHelper;
import home.genealogy.schema.all.helpers.TagHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class IndexPersonsToReferences
{
	// Key = Person Id of person
	// Value = List of Container (reference) ids and sub ids that the person is tagged in
	private HashMap<String, ArrayList<TaggedContainerDescriptor>> m_hmIndex;
	
	public IndexPersonsToReferences(CFGFamily family, PersonList personList, ReferenceList referenceList)
	{
		m_hmIndex = new HashMap<String, ArrayList<TaggedContainerDescriptor>>();
		Iterator<Reference> iter = referenceList.getReferences();
		while (iter.hasNext())
		{
			Reference reference = iter.next();
			int iReferenceId = ReferenceHelper.getReferenceId(reference);
			int iEntryCount = ReferenceHelper.getEntryCount(reference);
			for (int i=0; i<iEntryCount; i++)
			{
				Entry entry = ReferenceHelper.getEntry(reference, i);
				if (null != entry)
				{
					int iEntryId = EntryHelper.getEntryId(entry);
					List<PersonTag> lPersonTags = EntryHelper.getPersonTagList(entry);
					if (null != lPersonTags)
					{
						for (int t=0; t<lPersonTags.size(); t++)
						{
							PersonTag personTag = lPersonTags.get(t);
							int iPersonId = PersonTagHelper.getPersonId(personTag);
							Person person = personList.get(iPersonId);
							if (null == person)
							{	// Sanity check on person id
								System.out.println("ERROR: Unknown person id \"" + iPersonId + "\" in reference \"" + iReferenceId + "\" entry \"" + iEntryId);
							}
							ArrayList<TaggedContainerDescriptor> alContainers = m_hmIndex.get("" + iPersonId);
							if (null == alContainers)
							{
								alContainers = new ArrayList<TaggedContainerDescriptor>();
								m_hmIndex.put("" + iPersonId, alContainers);
							}
							// Does a container descriptor already exist for this container/subContainer?
							TaggedContainerDescriptor destination = new TaggedContainerDescriptor(iReferenceId, iEntryId);
							for (int r=0; r<alContainers.size(); r++)
							{
								TaggedContainerDescriptor candidate = alContainers.get(r);
								if ((candidate.getContainerId() == iReferenceId) &&
									(candidate.getSubContainerId() == iEntryId))
								{	// Yes, a container descriptor already exists, use it
									destination = candidate;
								}
							}
							// Now add the tags to the container descriptor
						    List<PersonTagType> lTags = personTag.getPersonTagType();
						    if (null != lTags)
						    {
								for (int g=0; g<lTags.size(); g++)
								{
									PersonTagType personTagType = lTags.get(g);
									destination.add(personTagType.getType(), personTagType.getQuality());
								}
						    }
						    if(!alContainers.contains(destination))
						    {
						    	alContainers.add(destination);
						    }
						}
					}
					
					List<Tag> alTags = EntryHelper.getTagList(entry);
					if (null != alTags)
					{
						for (int t=0; t<alTags.size(); t++)
						{
							Tag tag = alTags.get(t);
							if (TagHelper.isPersonTag(tag))
							{
								int iPersonId = TagHelper.getPersonId(tag);
								Person person = personList.get(iPersonId);
								if (null == person)
								{	// Sanity check on person id
									System.out.println("ERROR: Unknown person id \"" + iPersonId + "\" in reference \"" + iReferenceId + "\" entry \"" + iEntryId);
								}
								ArrayList<TaggedContainerDescriptor> alContainers = m_hmIndex.get("" + iPersonId);
								if (null == alContainers)
								{
									alContainers = new ArrayList<TaggedContainerDescriptor>();
									m_hmIndex.put("" + iPersonId, alContainers);
								}
								// Does a container descriptor already exist for this container/subContainer?
								TaggedContainerDescriptor destination = new TaggedContainerDescriptor(iReferenceId, iEntryId);
								for (int r=0; r<alContainers.size(); r++)
								{
									TaggedContainerDescriptor candidate = alContainers.get(r);
									if ((candidate.getContainerId() == iReferenceId) &&
										(candidate.getSubContainerId() == iEntryId))
									{	// Yes, a container descriptor already exists, use it
										destination = candidate;
									}
								}
								// Now add the tags to the container descriptor
								String strQuality = tag.getQuality();
								String strType = TagHelper.getType(tag);
								destination.add(strType, strQuality);
							    if(!alContainers.contains(destination))
							    {
							    	alContainers.add(destination);
							    }
							}
						}
					}					
				}
			}
		}
	}
	
	public ArrayList<TaggedContainerDescriptor> getReferencesForPerson(int iPersonId)
	{
		return m_hmIndex.get("" + iPersonId);
	}
	
	public ArrayList<TaggedContainerDescriptor> getReferencesForPerson(int iPersonId, String strTagType)
	{
		ArrayList<TaggedContainerDescriptor> alResult = new ArrayList<TaggedContainerDescriptor>();
		ArrayList<TaggedContainerDescriptor> alContainers =  m_hmIndex.get("" + iPersonId);
		if (null != alContainers)
		{
			for (int i=0; i<alContainers.size(); i++)
			{
				TaggedContainerDescriptor descriptor = alContainers.get(i);
				if (null != descriptor.getQuality(strTagType))
				{
					alResult.add(descriptor);
				}
			}
		}
		// Sort from HIGH to LOW
		ArrayList<TaggedContainerDescriptor> alSortedResult = new ArrayList<TaggedContainerDescriptor>();
		String[] arQualities = new String[]{"HIGH", "MEDIUM", "LOW"};
		for (int q=0; q<arQualities.length; q++)
		{
			for (int i=0; i<alResult.size(); i++)
			{
				if (alResult.get(i).getQuality(strTagType).equals(arQualities[q]))
				{
					alSortedResult.add(alResult.get(i));
				}
			}
		}
		return alSortedResult;
	}

}
