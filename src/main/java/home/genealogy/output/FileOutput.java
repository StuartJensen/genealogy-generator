package home.genealogy.output;

import java.io.File;
import java.io.FileOutputStream;
import java.security.InvalidParameterException;

public class FileOutput implements IOutputStream
{
	private String m_strFileName;
	private boolean m_bEchoToStdOut;
	private FileOutputStream m_fos;
	
	public FileOutput(String strFileName, boolean bEchoToStdOut)
	{
		m_strFileName = strFileName;
		m_bEchoToStdOut = bEchoToStdOut;
	}
	
	public void initialize()
		throws Exception
	{
		if (null == m_strFileName)
		{
			throw new InvalidParameterException("No file name specified!");
		}
		File file = new File(m_strFileName);
		if (file.exists())
		{
			file.delete();
		}
		m_fos = new FileOutputStream(file);
	}
		
	public void output(String str)
	{
		if (null != m_fos)
		{
			try
			{
				m_fos.write(str.getBytes());
			}
			catch (Exception e)
			{
				// Silence for now
			}
		}
		if (m_bEchoToStdOut)
		{
			System.out.print(str);
		}
	}
		
	public void deinitialize()
		throws Exception
	{
		if (null != m_fos)
		{
			m_fos.close();
		}
		m_fos = null;
	}
}
