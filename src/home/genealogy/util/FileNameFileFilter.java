package home.genealogy.util;

import java.io.File;
import java.io.FileFilter;

public class FileNameFileFilter implements FileFilter
 {
 	private String m_strFilePrefix;
 	private String m_strFileExtension;

 	public FileNameFileFilter(String strFilePrefix, String strFileExtension)
 	{
 		m_strFilePrefix = strFilePrefix;
 		m_strFileExtension = strFileExtension;
 	}
 	
 	public boolean accept(File fCandidate)
 	{
 		boolean bResult = false;
 		if (fCandidate.canRead())
 		{
 			if ((null != m_strFilePrefix) && (0 != m_strFilePrefix.length()))
 			{	// A prefix was provided, so enforce that the file start with the prefix.
 				String strPrefix = fCandidate.getName().substring(0, m_strFilePrefix.length());
 				if (!m_strFilePrefix.equalsIgnoreCase(strPrefix))
 				{
 					return false;
 				}
 			}
 			
 			if ((null != m_strFileExtension) && (0 != m_strFileExtension.length()))
 			{	// A prefix was provided, so enforce that the file start with the prefix.
 				int iIdx = fCandidate.getName().length() - m_strFileExtension.length();
 				String strExtension = fCandidate.getName().substring(iIdx);
 				if (!m_strFileExtension.equalsIgnoreCase(strExtension))
 				{
 					return false;
 				}
 			}
 			return true;
 		}
 		return bResult;
 	}

}
