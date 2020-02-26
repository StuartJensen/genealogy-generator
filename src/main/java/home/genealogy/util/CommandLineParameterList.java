package home.genealogy.util;

import java.security.InvalidParameterException;
import java.util.ArrayList;

public class CommandLineParameterList
{
	private ArrayList<CommandLineParameter> m_alCommandLineParameters = new ArrayList<CommandLineParameter>();
	
	public CommandLineParameterList(String[] arParameters)
		throws InvalidParameterException
	{
		for (int i=0; i<arParameters.length; i++)
		{
			m_alCommandLineParameters.add(new CommandLineParameter(arParameters[i]));
		}
	}
	
	public String getValue(String strKey)
	{
		for (int i=0; i<m_alCommandLineParameters.size(); i++)
		{
			CommandLineParameter clp = m_alCommandLineParameters.get(i);
			if (clp.getKey().equals(strKey))
			{
				return clp.getValue();
			}
		}
		return null;
	}
	
	
	public boolean getBooleanValue(String strKey)
	{
		String strValue = getValue(strKey);
		if (null != strValue)
		{
			if (strValue.equalsIgnoreCase(Boolean.TRUE.toString()))
			{
				return true;
			}
		}
		return false;
	}
	
	public int getIntegerValue(String strKey, int iDefault)
	{
		String strValue = getValue(strKey);
		if (null != strValue)
		{
			try
			{
				return Integer.parseInt(strValue);
			}
			catch (NumberFormatException nfe)
			{
			}
		}
		return iDefault;
	}
}
