package home.genealogy.lists.place;

import home.genealogy.lists.MarriageList;
import home.genealogy.lists.PersonList;
import home.genealogy.lists.PhotoList;
import home.genealogy.lists.PlaceList;
import home.genealogy.lists.ReferenceList;
import home.genealogy.output.IOutputStream;
import home.genealogy.schema.all.PlaceName;

public class PlaceDeleteAction
	extends PlaceActionBase
{
	public static final String ACTION = "delete";
	
	private String m_strToBeDeleted;
	
	public PlaceDeleteAction(String strToBeDeleted)
	{
		m_strToBeDeleted = strToBeDeleted;
	}
	
	public String getToBeDeleted()
	{
		return m_strToBeDeleted;
	}
	public void execute(PlaceList placeList,
						PersonList personList,
						MarriageList marriageList,
						ReferenceList referenceList,
						PhotoList photoList,
						IOutputStream outputStream)
							throws PlaceActionException
	{
		outputStream.output("Executing Place Command: Delete: Place Id " + m_strToBeDeleted + "\n");
		PlaceName removed = placeList.remove(m_strToBeDeleted);
		if (null == removed)
		{
			outputStream.output("  WARNING: Removal of Place with id: " + m_strToBeDeleted + " failed. No Place with that id!\n");
		}
		else
		{
			outputStream.output("  Place with id " + m_strToBeDeleted + " removed from place list!\n");
			markPlaceListModified();
		}
	}
}
