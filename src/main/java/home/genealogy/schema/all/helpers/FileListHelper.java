package home.genealogy.schema.all.helpers;

import java.util.ArrayList;
import java.util.List;

import home.genealogy.schema.all.File;
import home.genealogy.schema.all.FileList;

public class FileListHelper
{
	public static String getPackage(FileList fileList)
	{
		if (null != fileList)
		{
			String strPackage = fileList.getPackage();
			if (null != strPackage)
			{
				return strPackage;
			}
		}
		return "";
	}
	
	public static String getBaseFileName(FileList fileList)
	{
		if (null != fileList)
		{
			String strBaseFileName = fileList.getBaseFileName();
			if (null != strBaseFileName)
			{
				return strBaseFileName;
			}
		}
		return "";
	}
	
	public static List<File> getFiles(FileList fileList)
	{
		if (null != fileList)
		{
			List<File> lFile = fileList.getFile();
			return lFile;
		}
		return new ArrayList<File>();
	}
	
	public static File getSmallestWebbableFile(FileList fileList)
	{
		if (null != fileList)
		{
			List<File> lFiles = getFiles(fileList);
			if (null != lFiles)
			{
				File fCurrent = null;
				for (int i=0; i<lFiles.size(); i++)
				{
					File f = lFiles.get(i);
					FileHelper.eFileType eType = FileHelper.getType(f);
					int iSize = FileHelper.getSize(f);
					if ((eType == FileHelper.eFileType.eJPG) ||
						(eType == FileHelper.eFileType.eGIF) ||
						(eType == FileHelper.eFileType.ePNG))
					{
						if (null == fCurrent)
						{
							fCurrent = f;
						}
						else if (iSize < FileHelper.getSize(fCurrent))
						{
							fCurrent = f;
						}
					}
				}
				return fCurrent;
			}
		}
		return null;
	}
	
}
