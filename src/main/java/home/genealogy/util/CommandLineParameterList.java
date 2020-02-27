package home.genealogy.util;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collection;

public class CommandLineParameterList
{
	private Collection<CommandLineParameter> m_alCommandLineParameters = new ArrayList<CommandLineParameter>();
	
	public CommandLineParameterList(String[] arParameters)
		throws InvalidParameterException
	{
		for (int i=0; i<arParameters.length; i++)
		{
			m_alCommandLineParameters.add(new CommandLineParameter(arParameters[i]));
		}
	}
	
	public Collection<String> getCommandLineParameterNames()
	{
		Collection<String> cNames = new ArrayList<String>();
		for (CommandLineParameter clp : m_alCommandLineParameters)
		{
			cNames.add(clp.getKey());
		}
		return cNames;
	}
	
	public String getValue(String strKey)
	{
		for (CommandLineParameter clp : m_alCommandLineParameters)
		{
			if (clp.getKey().equals(strKey))
			{
				return clp.getValue();
			}
		}
		return null;
	}
	
	public void set(String strKey, String strValue)
	{
		for (CommandLineParameter clp : m_alCommandLineParameters)
		{
			if (clp.getKey().equalsIgnoreCase(strKey))
			{
				m_alCommandLineParameters.remove(clp);
			}
		}
		m_alCommandLineParameters.add(new CommandLineParameter(strKey, strValue));
	}

	public boolean getBooleanValue(String strKey)
	{
		String strValue = getValue(strKey);
		if (null != strValue)
		{
			if ((strValue.equalsIgnoreCase(Boolean.TRUE.toString())) ||
				(strValue.equalsIgnoreCase("yes")) ||
				(strValue.equalsIgnoreCase("1")))
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
	
	public long getLongValue(String strKey, long lDefault)
	{
		String strValue = getValue(strKey);
		if (null != strValue)
		{
			try
			{
				return Long.parseLong(strValue);
			}
			catch (NumberFormatException nfe)
			{
			}
		}
		return lDefault;
	}
}
