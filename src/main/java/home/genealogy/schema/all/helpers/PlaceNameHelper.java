package home.genealogy.schema.all.helpers;

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
