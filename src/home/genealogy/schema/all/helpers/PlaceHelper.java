package home.genealogy.schema.all.helpers;

import home.genealogy.schema.all.GlobalCoordinates;
import home.genealogy.schema.all.Place;
import home.genealogy.schema.all.Place1Level;
import home.genealogy.schema.all.Place2Level;
import home.genealogy.schema.all.Place3Level;
import home.genealogy.schema.all.Place4Level;
import home.genealogy.schema.all.WGS84;

public class PlaceHelper
{
	public static String getPlace(Place place)
	{
		if (null == place)
		{
			return "";
		}
		StringBuffer sb = new StringBuffer(128);
		String strModifier = place.getModifier();
		if (null != strModifier)
		{
			sb.append(strModifier);
		}
		String strCountry = place.getCountry();
		if ((null != strCountry) && (0 != strCountry.length()))
		{
			if (0 != sb.length())
			{
				sb.append(", ");
			}
			sb.append(strCountry);
		}
		// Level 1
		Place1Level levelOne = place.getPlace1Level();
		if (null != levelOne)
		{
			String strLevelOne = levelOne.getName();
			if ((null != strLevelOne) && (0 != strLevelOne.length()))
			{
				if (0 != sb.length())
				{
					sb.append(", ");
				}
				sb.append(strLevelOne);
			}
			// Level 2
			Place2Level levelTwo = levelOne.getPlace2Level();
			if (null != levelTwo)
			{
				String strLevelTwo = levelTwo.getName();
				if ((null != strLevelTwo) && (0 != strLevelTwo.length()))
				{
					if (0 != sb.length())
					{
						sb.append(", ");
					}
					sb.append(strLevelTwo);
				}
				// Level 3
				Place3Level levelThree = levelTwo.getPlace3Level();
				if (null != levelThree)
				{
					String strLevelThree = levelThree.getName();
					if ((null != strLevelThree) && (0 != strLevelThree.length()))
					{
						if (0 != sb.length())
						{
							sb.append(", ");
						}
						sb.append(strLevelThree);
					}
					// Level 4
					Place4Level levelFour = levelThree.getPlace4Level();
					if (null != levelFour)
					{
						String strLevelFour = levelFour.getName();
						if ((null != strLevelFour) && (0 != strLevelFour.length()))
						{
							if (0 != sb.length())
							{
								sb.append(", ");
							}
							sb.append(strLevelFour);
						}
					}
				}
			}
		}
		// Global Coordinates
		GlobalCoordinates gc = place.getGlobalCoordinates();
		if (null != gc)
		{
			WGS84 wgs = gc.getWGS84();
			if (null != wgs)
			{
				String strCoordinates = WGS84Helper.getCoordinates(wgs);
				sb.append(", ");
				sb.append(strCoordinates);
			}
		}
		return(sb.toString());
	}
	
	public static String getTemple(Place place)
	{
		String strTemple = place.getTemple();
		if (null == strTemple)
		{
			strTemple = "";
		}
		return strTemple;
	}
}
