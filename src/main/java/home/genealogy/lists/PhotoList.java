package home.genealogy.lists;

import java.io.File;
import java.security.InvalidParameterException;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.xml.sax.SAXException;

import home.genealogy.CommandLineParameters;
import home.genealogy.GenealogyContext;
import home.genealogy.configuration.CFGFamily;
import home.genealogy.output.IOutputStream;
import home.genealogy.schema.all.Photo;
import home.genealogy.schema.all.Photos;
import home.genealogy.schema.all.Reference;
import home.genealogy.schema.all.helpers.PhotoHelper;
import home.genealogy.schema.all.helpers.PhotoIdHelper;
import home.genealogy.schema.all.helpers.ReferenceHelper;
import home.genealogy.util.FileNameFileFilter;
import home.genealogy.util.MarshallUtil;

public class PhotoList
{
	private Photo[] m_arPhotoList;
	
	public PhotoList(CFGFamily family, CommandLineParameters commandLineParameters, IOutputStream outputStream)
		throws InvalidParameterException, JAXBException, SAXException
	{
		outputStream.output("Photo List: Initiating load.\n");
		if (commandLineParameters.isSourceIndividualXMLs())
		{
			unMarshallIndividualFiles(family);
			outputStream.output("Photo List: Count: " + size() + ": Loaded from Individual XML files.\n");
		}
		else if (commandLineParameters.isSourceAllXMLs())
		{
			unMarshallAllFile(family);
			outputStream.output("Photo List: Count: " + size() + ": Loaded from ALL XML file.\n");
		}
	}
	
	public void persist(GenealogyContext context)
		throws Exception
	{
		if (context.getCommandLineParameters().isDestinationIndividualXMLs())
		{
			marshallIndividualFiles(context.getFamily(), context.getFormattedOutput());
			context.output("Photo List: Count: " + size() + ": Persisted to Individual XML files.\n");
		}
		else if (context.getCommandLineParameters().isDestinationAllXMLs())
		{
			marshallAllFile(context.getFamily(), context.getFormattedOutput());
			context.output("Photo List: Count: " + size() + ": Persisted to ALL XML file.\n");
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
	
	public void unMarshallAllFile(CFGFamily family)
		throws InvalidParameterException, JAXBException, SAXException
	{
		String strDataPath = family.getDataPathSlashTerminated();
		
		m_arPhotoList = new Photo[family.getPhotoListMaxSize()];
		
		// Setup JAXB unmarshaller
		Unmarshaller unmarshaller = MarshallUtil.createUnMarshaller(family.getSchemaFile());
		
		String strDirectoryPhotos = strDataPath + CFGFamily.APPENDAGE_DATAPATH_PHOTOS;
		String strFileName = strDirectoryPhotos + File.separator + CFGFamily.PHOTOS_ALL_FILENAME;

		File fAllFile = new File(strFileName);
		if (fAllFile.exists())
		{
			Photos photos = (Photos)unmarshaller.unmarshal(fAllFile);
			List<Photo> lPhotos = photos.getPhoto();
			for (Photo photo : lPhotos)
			{
				int iPhotoId = PhotoHelper.getPhotoId(photo);
				if (PhotoIdHelper.PHOTOID_INVALID == iPhotoId)
				{
					throw new InvalidParameterException("Photo has invalid photo id: " + fAllFile.getName());
				}
				if (iPhotoId >= m_arPhotoList.length)
				{
					throw new InvalidParameterException("Photo's photo id out of range: " + iPhotoId);
				}
				m_arPhotoList[iPhotoId] = photo;
			}
		}
		else
		{
			System.out.println("WARNING: Photos ALL file not found: " + strFileName);
		}
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
		
		String strDataPath = family.getDataPathSlashTerminated();
		String strDirectory = strDataPath + CFGFamily.APPENDAGE_DATAPATH_PHOTOS;
		MarshallUtil.marshall(marshaller, photos, strDirectory + File.separator + CFGFamily.PHOTOS_ALL_FILENAME);
	}
	
	public void unMarshallIndividualFiles(CFGFamily family)
		throws InvalidParameterException, JAXBException, SAXException
	{
		String strDataPath = family.getDataPathSlashTerminated();
		
		m_arPhotoList = new Photo[family.getPhotoListMaxSize()];
		
		// Setup JAXB unmarshaller
		Unmarshaller unmarshaller = MarshallUtil.createUnMarshaller(family.getSchemaFile());
		
		String strDirectoryPhotos = strDataPath + CFGFamily.APPENDAGE_DATAPATH_PHOTOS;
		File fDirectoryPhotos = new File(strDirectoryPhotos);
		File[] arFiles = fDirectoryPhotos.listFiles(new FileNameFileFilter(CFGFamily.PHOTOS_FILE_PREFIX, CFGFamily.DOTXML_FILE_POSTFIX));
		if ((null != arFiles) && (0 != arFiles.length))
		{
			for (int i=0; i<arFiles.length; i++)
			{
				Photo photo = (Photo)unmarshaller.unmarshal(arFiles[i]);
				int iPhotoId = PhotoHelper.getPhotoId(photo);
				if (PhotoIdHelper.PHOTOID_INVALID == iPhotoId)
				{
					throw new InvalidParameterException("Photo has invalid photo id: " + arFiles[i].getName());
				}
				if (iPhotoId >= m_arPhotoList.length)
				{
					throw new InvalidParameterException("Photo's photo id out of range: " + iPhotoId);
				}
				m_arPhotoList[iPhotoId] = photo;
			}
		}
	}

	public void marshallIndividualFiles(CFGFamily family, boolean bFormattedOutput)
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
		
		String strDataPath = family.getDataPathSlashTerminated();
		String strDirectory = strDataPath + CFGFamily.APPENDAGE_DATAPATH_PHOTOS;
		
		Iterator<Photo> iter = getPhotos();
		while (iter.hasNext())
		{
			Photo photo = iter.next();
			String strPhotoFileName = MessageFormat.format(CFGFamily.PHOTOS_FILE_FORMAT_STRING, String.valueOf(photo.getPhotoId()));
			MarshallUtil.marshall(marshaller, photo, strDirectory + File.separator + strPhotoFileName);
		}
	}
	
	public int replacePlaceId(String strIdToBeReplaced,
							String strIdReplacement,
							IOutputStream outputStream)
	{
		int iCount = 0;
		Iterator<Photo> iter = getPhotos();
		while (iter.hasNext())
		{
		iCount += PhotoHelper.replacePlaceId(iter.next(),
											strIdToBeReplaced,
											strIdReplacement,
											(String)null,
											(String)null,
											(String)null,
											outputStream);
		}
		return iCount;
	}
}
