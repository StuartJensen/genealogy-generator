package home.genealogy.action.validate;

import home.genealogy.util.FileNameFileFilter;

import java.io.File;
import java.io.FileFilter;

public class TimeFileFilter extends FileNameFileFilter implements FileFilter
{
	private long m_lOldestAllowableTime;

	public TimeFileFilter(String strFilePrefix, String strFileExtension, long lLastMilliseconds)
	{
		super(strFilePrefix, strFileExtension);
		m_lOldestAllowableTime = 0;
		if (lLastMilliseconds > 0)
		{
			m_lOldestAllowableTime = System.currentTimeMillis() - lLastMilliseconds;
		}
	}
	
	public boolean accept(File fCandidate)
	{
		if (super.accept(fCandidate))
		{
			long lLastModified = fCandidate.lastModified();
			if (lLastModified > m_lOldestAllowableTime)
			{
				return true;
			}
		}
		return false;
	}

}
