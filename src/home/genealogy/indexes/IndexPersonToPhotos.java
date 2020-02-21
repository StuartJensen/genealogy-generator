package home.genealogy.indexes;

import home.genealogy.configuration.CFGFamily;
import home.genealogy.lists.PersonList;
import home.genealogy.lists.PhotoList;
import home.genealogy.schema.all.Person;
import home.genealogy.schema.all.PersonTag;
import home.genealogy.schema.all.PersonTagType;
import home.genealogy.schema.all.Photo;
import home.genealogy.schema.all.helpers.PersonTagHelper;
import home.genealogy.schema.all.helpers.PhotoHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class IndexPersonToPhotos
{
	// Key = Person Id of person
	// Value = List of Container (photo) ids that the person is tagged in
	private HashMap<String, ArrayList<TaggedContainerDescriptor>> m_hmIndex;
	
	public IndexPersonToPhotos(CFGFamily family, PersonList personList, PhotoList photoList)
	{
		m_hmIndex = new HashMap<String, ArrayList<TaggedContainerDescriptor>>();
		Iterator<Photo> iter = photoList.getPhotos();
		while (iter.hasNext())
		{
			Photo photo = iter.next();
			int iPhotoId = PhotoHelper.getPhotoId(photo);
			List<PersonTag> lPersonTags = PhotoHelper.getPersonTagList(photo);
			if (null != lPersonTags)
			{
				for (int t=0; t<lPersonTags.size(); t++)
				{
					PersonTag personTag = lPersonTags.get(t);
					int iPersonId = PersonTagHelper.getPersonId(personTag);
					Person person = personList.get(iPersonId);
					if (null == person)
					{	// Sanity check on person id
						System.out.println("ERROR: Unknown person id \"" + iPersonId + "\" in photo \"" + iPhotoId);
					}
					ArrayList<TaggedContainerDescriptor> alContainers = m_hmIndex.get("" + iPersonId);
					if (null == alContainers)
					{
						alContainers = new ArrayList<TaggedContainerDescriptor>();
						m_hmIndex.put("" + iPersonId, alContainers);
					}
					// Does a container descriptor already exist for this container/subContainer?
					TaggedContainerDescriptor destination = new TaggedContainerDescriptor(iPhotoId, -1);
					for (int r=0; r<alContainers.size(); r++)
					{
						TaggedContainerDescriptor candidate = alContainers.get(r);
						if (candidate.getContainerId() == iPhotoId)
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
		}
	}
	
	public ArrayList<TaggedContainerDescriptor> getPhotosForPerson(int iPersonId)
	{
		return m_hmIndex.get("" + iPersonId);
	}
	
	public ArrayList<TaggedContainerDescriptor> getPhotosForPerson(int iPersonId, String strTagType)
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
