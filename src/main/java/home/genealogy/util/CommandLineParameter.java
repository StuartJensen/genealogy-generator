package home.genealogy.util;

import java.security.InvalidParameterException;
import java.util.StringTokenizer;

public class CommandLineParameter
{
	// Parses a command line parameter of format key=value
	
	private String m_strKey;
	public String m_strValue;
	
	public CommandLineParameter(String strParameter)
		throws InvalidParameterException
	{
		if ((null == strParameter) || (0 == strParameter.length()))
		{
			throw new InvalidParameterException("Null or Empty Command Line Parameter!");
		}
		StringTokenizer st = new StringTokenizer(strParameter, "=", false);
		if (2 != st.countTokens())
		{
			throw new InvalidParameterException("Improperly formatted Command Line Parameter: " + strParameter);
		}
		m_strKey = st.nextToken();
		m_strValue = st.nextToken();
		if ((null == m_strKey) || (0 == m_strKey.length()))
		{
			throw new InvalidParameterException("Null or Empty Command Line Parameter Key: " + strParameter);
		}
		if ((null == m_strValue) || (0 == m_strValue.length()))
		{
			throw new InvalidParameterException("Null or Empty Command Line Parameter Value: " + strParameter);
		}
	}
	
	public String getKey()
	{
		return m_strKey;
	}
	
	public String getValue()
	{
		return m_strValue;
	}
	
}
