package home.genealogy.schema.all.helpers;

import home.genealogy.schema.all.CallNumber;

public class CallNumberHelper
{
    public static String getCallNumberName(CallNumber callNumber)
    {
    	if (null != callNumber)
    	{
    		String strName = callNumber.getName();
    		if (null != strName)
    		{
    			return strName;
    		}
    	}
    	return "";
    }
}
