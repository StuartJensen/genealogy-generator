package home.genealogy.schema.all.helpers;

import java.util.Comparator;

import home.genealogy.lists.PlaceList;
import home.genealogy.schema.all.PlaceName;

public class PlaceNameComparator
	implements Comparator<PlaceName> 
{
	private PlaceList m_placeList;
	
	public PlaceNameComparator(PlaceList placeList)
	{
		m_placeList = placeList;
	}
	
	public int compare(PlaceName a, PlaceName b) 
	{
		String strAName = PlaceNameHelper.getPlaceName(a.getId(), m_placeList);
		String strBName = PlaceNameHelper.getPlaceName(b.getId(), m_placeList);
		return strAName.compareTo(strBName);
	} 
}
