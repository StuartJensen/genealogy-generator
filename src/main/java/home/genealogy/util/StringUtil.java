package home.genealogy.util;

public class StringUtil
{
	public static String toLowerCase(String strSource)
	{
		if (null != strSource)
		{
			strSource = strSource.toLowerCase();
		}
		return strSource;
	}
	
	public static boolean exists(String strCandidate)
	{
		return ((null != strCandidate) && (0 != strCandidate.length()));
	}
}
