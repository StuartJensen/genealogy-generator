package home.genealogy.indexes;

import home.genealogy.configuration.CFGFamily;
import home.genealogy.lists.MarriageList;
import home.genealogy.lists.PhotoList;
import home.genealogy.schema.all.Marriage;
import home.genealogy.schema.all.MarriageTag;
import home.genealogy.schema.all.MarriageTagType;
import home.genealogy.schema.all.Photo;
import home.genealogy.schema.all.helpers.MarriageTagHelper;
import home.genealogy.schema.all.helpers.PhotoHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class IndexMarriageToPhotos
{
	// Key = Person Id of person
	// Value = List of Container (photo) ids that the person is tagged in
	private HashMap<String, ArrayList<TaggedContainerDescriptor>> m_hmIndex;
	
	public IndexMarriageToPhotos(CFGFamily family, MarriageList marriageList, PhotoList photoList)
	{
		m_hmIndex = new HashMap<String, ArrayList<TaggedContainerDescriptor>>();
		Iterator<Photo> iter = photoList.getPhotos();
		while (iter.hasNext())
		{
			Photo photo = iter.next();
			int iPhotoId = PhotoHelper.getPhotoId(photo);
			List<MarriageTag> lMarriageTags = PhotoHelper.getMarriageTagList(photo);
			if (null != lMarriageTags)
			{
				for (int t=0; t<lMarriageTags.size(); t++)
				{
					MarriageTag marriageTag = lMarriageTags.get(t);
					int iMarriageId = MarriageTagHelper.getMarriageId(marriageTag);
					Marriage marriage = marriageList.get(iMarriageId);
					if (null == marriage)
					{	// Sanity check on marriage id
						System.out.println("ERROR: Unknown marriage id \"" + iMarriageId + "\" in photo \"" + iPhotoId);
					}
					ArrayList<TaggedContainerDescriptor> alContainers = m_hmIndex.get("" + iMarriageId);
					if (null == alContainers)
					{
						alContainers = new ArrayList<TaggedContainerDescriptor>();
						m_hmIndex.put("" + iMarriageId, alContainers);
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
				    List<MarriageTagType> lTags = marriageTag.getMarriageTagType();
				    if (null != lTags)
				    {
						for (int g=0; g<lTags.size(); g++)
						{
							MarriageTagType marriageTagType = lTags.get(g);
							destination.add(marriageTagType.getType(), marriageTagType.getQuality());
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
	
	public ArrayList<TaggedContainerDescriptor> getPhotosForMarriage(int iMarriageId)
	{
		return m_hmIndex.get("" + iMarriageId);
	}
	
	public ArrayList<TaggedContainerDescriptor> getPhotosForMarriage(int iMarriageId, String strTagType)
	{
		ArrayList<TaggedContainerDescriptor> alResult = new ArrayList<TaggedContainerDescriptor>();
		ArrayList<TaggedContainerDescriptor> alContainers =  m_hmIndex.get("" + iMarriageId);
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
