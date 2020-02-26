package home.genealogy.schema.all.helpers;

import home.genealogy.schema.all.CallNumber;
import home.genealogy.schema.all.Institution;
import home.genealogy.schema.all.Location;

import java.util.List;

public class LocationHelper
{
    public static String getInstitutionName(Location location)
    {
    	if (null != location)
    	{
    		Institution institution = location.getInstitution();
    		if (null != institution)
    		{
    			return InstuitionHelper.getInstitutionName(institution);
    		}
    	}
    	return "";
    }
    
	public static int getCallNumberCount(Location location)
	{
		if (null != location)
		{
			List<CallNumber> lCallNumbers = location.getCallNumber();
			if (null != lCallNumbers)
			{
				return lCallNumbers.size();
			}
		}
		return 0;
	}
	
	public static String getCallNumber(Location location, int iNth)
	{
		if (null != location)
		{
			List<CallNumber> lCallNumbers = location.getCallNumber();
			if ((null != lCallNumbers) && (iNth < lCallNumbers.size()))
			{
				return CallNumberHelper.getCallNumberName(lCallNumbers.get(iNth));
			}
		}
		return null;
	}
	
}
