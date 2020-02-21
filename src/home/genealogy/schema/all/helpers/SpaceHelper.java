package home.genealogy.schema.all.helpers;

import home.genealogy.schema.all.Space;

public class SpaceHelper
{
	public static String getSpaces(Space space, String strOneSpace)
	{
		String strCount = space.getCount();
		if (null != strCount)
		{
			try
			{
				int iSpaceCount = Integer.parseInt(strCount);
				StringBuffer sb = new StringBuffer((strOneSpace.length() * iSpaceCount) + 1);
				for (int i=0; i<iSpaceCount; i++)
				{
					sb.append(strOneSpace);
				}
				return sb.toString();
			}
			catch (Exception e)
			{/* return zero spaces */}
		}
		return "";
	}
}
