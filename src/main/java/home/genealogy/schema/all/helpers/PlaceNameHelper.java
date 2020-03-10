package home.genealogy.schema.all.helpers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import home.genealogy.lists.PlaceList;
import home.genealogy.schema.all.PlaceName;
import home.genealogy.util.StringUtil;

public class PlaceNameHelper
{
	public static String getPlaceName(String strPlaceNameId, PlaceList placeList)
	{
		StringBuilder sb = new StringBuilder();
		Stack<String> stack = new Stack<String>();
		getPlaceName(strPlaceNameId, placeList, stack);
		while (!stack.isEmpty())
		{
			String strPart = stack.pop();
			StringUtil.commaTerminateExisting(sb);
			sb.append(strPart);
		}
		return sb.toString();
	}
	
	public static String getPlaceNameId(String strPlaceName, PlaceList placeList)
	{
		Map<String, PlaceName> mPlaces = placeList.getPlaces();
		Iterator<String> iter = mPlaces.keySet().iterator();
		while (iter.hasNext())
		{
			String strKey = iter.next();
			String strCandidate = PlaceNameHelper.getPlaceName(strKey, placeList);
			if (strPlaceName.equals(strCandidate))
			{
				return strKey;
			}
		}
		return null;
	}
	
	private static void getPlaceName(String strPlaceNameId, PlaceList placeList, Stack<String> stack)
	{
		PlaceName target = placeList.get(strPlaceNameId);
		if (null != target)
		{
			if (StringUtil.exists(target.getName()))
			{
				stack.push(target.getName());
			}
			if (StringUtil.exists(target.getParentIdRef()))
			{
				getPlaceName(target.getParentIdRef(), placeList, stack);
			}
		}
	}
}
