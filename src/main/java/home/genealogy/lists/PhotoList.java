package home.genealogy.lists;

import home.genealogy.configuration.CFGFamily;
import home.genealogy.schema.all.Photo;
import home.genealogy.schema.all.Photos;
import home.genealogy.schema.all.helpers.PhotoHelper;
import home.genealogy.schema.all.helpers.PhotoIdHelper;
import home.genealogy.util.CommandLineParameterList;
import home.genealogy.util.FileNameFileFilter;
import home.genealogy.util.MarshallUtil;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.xml.sax.SAXException;

public class PhotoList
{
	private Photo[] m_arPhotoList;
	
	public PhotoList(CFGFamily family, CommandLineParameterList listCLP)
		throws Exception
	{
		String strDataPath = family.getDataPath();
		if (!strDataPath.endsWith(File.separator))
		{
			strDataPath += File.separator;
		}
		
		m_arPhotoList = new Photo[family.getPhotoListMaxSize()];
		
		// Setup JAXB unmarshaller
		Unmarshaller unmarshaller = MarshallUtil.createUnMarshaller(family.getSchemaFile());
		
		String strDirectoryPhotos = strDataPath + CFGFamily.APPENDAGE_DATAPATH_PHOTOS;
		File fDirectoryPhotos = new File(strDirectoryPhotos);
		File[] arFiles = fDirectoryPhotos.listFiles(new FileNameFileFilter("Ph", ".xml"));
		if ((null != arFiles) && (0 != arFiles.length))
		{
			for (int i=0; i<arFiles.length; i++)
			{
				Photo photo = (Photo)unmarshaller.unmarshal(arFiles[i]);
				int iPhotoId = PhotoHelper.getPhotoId(photo);
				if (PhotoIdHelper.PHOTOID_INVALID == iPhotoId)
				{
					throw new Exception("Photo has invalid photo id: " + arFiles[i].getName());
				}
				if (iPhotoId >= m_arPhotoList.length)
				{
					throw new Exception("Photo's photo id out of range: " + iPhotoId);
				}
				m_arPhotoList[iPhotoId] = photo;
			}
		}
	}
	
	public Iterator<Photo> getPhotos()
	{
		return new PhotoListIterator(m_arPhotoList);
	}
	
	public Photo get(int iPhotoId)
	{
		if (iPhotoId < m_arPhotoList.length)
		{
			return m_arPhotoList[iPhotoId];
		}
		return null;
	}
	
	public int size()
	{
		int iCount = 0;
		for (int i=0; i<m_arPhotoList.length; i++)
		{
			if (null != m_arPhotoList[i])
			{
				iCount++;
			}
		}
		return iCount;
	}

	public void marshallAllFile(CFGFamily family, boolean bFormattedOutput)
	{
		Marshaller marshaller = null;
		try
		{
			marshaller = MarshallUtil.createMarshaller(family.getSchemaFile(), bFormattedOutput);
		}
		catch (JAXBException jb)
		{
			System.out.println("Exception creating JAXB Marshaller: " + jb.toString());
			return;
		}
		catch (SAXException se)
		{
			System.out.println("Exception creating JAXB Marshaller: " + se.toString());
			return;
		}
		
		// Build schema "Persons" instance
		Photos photos = new Photos();
		
		List<Photo> lPhotos = photos.getPhoto();
		Iterator<Photo> iter = this.getPhotos();
		while (iter.hasNext())
		{
			lPhotos.add(iter.next());
		}
		
		String strDataPath = family.getDataPath();
		if (!strDataPath.endsWith(File.separator))
		{
			strDataPath += File.separator;
		}
		String strDirectory = strDataPath + CFGFamily.APPENDAGE_DATAPATH_PHOTOS;
		MarshallUtil.marshall(marshaller, photos, strDirectory + File.separator + "allPhotos.xml");
	}

}
