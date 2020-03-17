package home.genealogy.schema.all.helpers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import home.genealogy.lists.PlaceList;
import home.genealogy.schema.all.GlobalCoordinates;
import home.genealogy.schema.all.Place;
import home.genealogy.schema.all.PlaceModifiers;
import home.genealogy.schema.all.PlaceName;
import home.genealogy.schema.all.WGS84;
import home.genealogy.util.StringUtil;

public class PlaceHelper
{
	public static String getPlace(Place place, PlaceList placeList)
	{
		if (null == place)
		{
			return "";
		}
		StringBuilder sb = new StringBuilder(128);
		PlaceModifiers placeModifier = place.getModifier();
		if (null != placeModifier)
		{
			sb.append(placeModifier.value());
		}
		String strPlaceName = PlaceNameHelper.getPlaceName(place.getIdRef(), placeList);
		if (StringUtil.exists(strPlaceName))
		{
			StringUtil.commaTerminateExisting(sb);
			sb.append(strPlaceName);
		}
		String strLocale = place.getLocale();
		if (StringUtil.exists(strLocale))
		{
			StringUtil.commaTerminateExisting(sb);
			sb.append(strLocale);
		}
		String strStreet = place.getStreet();
		if (StringUtil.exists(strStreet))
		{
			StringUtil.commaTerminateExisting(sb);
			sb.append(strStreet);
		}
		String strSpot = place.getSpot();
		if (StringUtil.exists(strSpot))
		{
			StringUtil.commaTerminateExisting(sb);
			sb.append(strSpot);
		}
		// Global Coordinates
		GlobalCoordinates gc = place.getGlobalCoordinates();
		if (null != gc)
		{
			WGS84 wgs = gc.getWGS84();
			if (null != wgs)
			{
				String strCoordinates = WGS84Helper.getCoordinates(wgs);
				StringUtil.commaTerminateExisting(sb);
				sb.append(strCoordinates);
			}
		}
		return(sb.toString());
	}
	
	public static void setPlaceData(Place target, String strPlaceId, String strLocale, String strStreet, String strSpot)
	{
		if (null != strPlaceId)
		{
			target.setIdRef(strPlaceId);
		}
		if (null != strLocale)
		{
			target.setLocale(strLocale);
		}
		if (null != strStreet)
		{
			target.setStreet(strStreet);
		}
		if (null != strSpot)
		{
			target.setSpot(strSpot);
		}
	}
	
	public static List<PlaceName> findFirstDuplicates(PlaceList placeList)
	{
		List<PlaceName> lDuplicates = new ArrayList<PlaceName>();
		
		Iterator<String> iter = placeList.getPlaces().keySet().iterator();
		while (iter.hasNext() && lDuplicates.isEmpty())
		{
			String strCurrentKey = iter.next();
			PlaceName currentPlaceName = placeList.get(strCurrentKey);
			String strCurrentPlaceName = PlaceNameHelper.getPlaceName(strCurrentKey, placeList);
			Iterator<String> iterInternal = placeList.getPlaces().keySet().iterator();
			while (iterInternal.hasNext())
			{
				String strInnerKey = iterInternal.next();
				PlaceName innerPlaceName = placeList.get(strInnerKey);
				if (currentPlaceName != innerPlaceName)
				{
					String strInnerPlaceName = PlaceNameHelper.getPlaceName(strInnerKey, placeList);
					if (strInnerPlaceName.equals(strCurrentPlaceName))
					{
						lDuplicates.add(innerPlaceName);
					}
				}
			}
			if (!lDuplicates.isEmpty())
			{
				lDuplicates.add(currentPlaceName);
			}
		}
		return lDuplicates;
	}
	
	public static List<List<PlaceName>> findAllDuplicates(PlaceList placeList)
	{
		List<List<PlaceName>> lDuplicates = new ArrayList<List<PlaceName>>();
		List<String> lAlreadyDone = new ArrayList<String>();
		Iterator<String> iter = placeList.getPlaces().keySet().iterator();
		while (iter.hasNext())
		{
			String strCurrentKey = iter.next();
			PlaceName currentPlaceName = placeList.get(strCurrentKey);
			String strCurrentPlaceName = PlaceNameHelper.getPlaceName(strCurrentKey, placeList);
			if (!lAlreadyDone.contains(strCurrentPlaceName))
			{
				List<PlaceName> lThisSet = new ArrayList<PlaceName>();
				lThisSet.add(currentPlaceName);
				Iterator<String> iterInternal = placeList.getPlaces().keySet().iterator();
				while (iterInternal.hasNext())
				{
					String strInnerKey = iterInternal.next();
					PlaceName innerPlaceName = placeList.get(strInnerKey);
					if (currentPlaceName != innerPlaceName)
					{
						String strInnerPlaceName = PlaceNameHelper.getPlaceName(strInnerKey, placeList);
						if (strInnerPlaceName.equals(strCurrentPlaceName))
						{
							lThisSet.add(innerPlaceName);
						}
					}
				}
				if (1 < lThisSet.size())
				{	// Found duplicates. Return set and mark this place name as DONE!
					lDuplicates.add(lThisSet);
					lAlreadyDone.add(PlaceNameHelper.getPlaceName(lThisSet.get(0).getId(), placeList));
				}
			}
		}
		return lDuplicates;
	}
}
