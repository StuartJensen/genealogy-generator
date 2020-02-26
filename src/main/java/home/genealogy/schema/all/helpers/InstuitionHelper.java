package home.genealogy.schema.all.helpers;

import home.genealogy.schema.all.Institution;

public class InstuitionHelper
{
    public static String getInstitutionName(Institution institution)
    {
    	if (null != institution)
    	{
    		String strName = institution.getName();
    		if (null != strName)
    		{
    			return strName;
    		}
    	}
    	return "";
    }
}
