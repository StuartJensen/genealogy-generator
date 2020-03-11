package home.genealogy.schema.all.helpers;

import home.genealogy.lists.PlaceList;
import home.genealogy.schema.all.GlobalCoordinates;
import home.genealogy.schema.all.Place;
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
		String strModifier = place.getModifier();
		if (null != strModifier)
		{
			sb.append(strModifier);
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

}
