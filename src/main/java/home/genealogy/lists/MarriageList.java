package home.genealogy.lists;

import java.io.File;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.xml.sax.SAXException;

import home.genealogy.CommandLineParameters;
import home.genealogy.configuration.CFGFamily;
import home.genealogy.schema.all.Marriage;
import home.genealogy.schema.all.MarriageId;
import home.genealogy.schema.all.Marriages;
import home.genealogy.schema.all.Person;
import home.genealogy.schema.all.helpers.MarriageHelper;
import home.genealogy.schema.all.helpers.MarriageIdHelper;
import home.genealogy.schema.all.helpers.PersonHelper;
import home.genealogy.schema.all.helpers.PersonIdHelper;
import home.genealogy.util.FileNameFileFilter;
import home.genealogy.util.MarshallUtil;

public class MarriageList
{
	private Marriage[] m_arMarriageList;
	
	public MarriageList(CFGFamily family, CommandLineParameters commandLineParameters)
		throws Exception
	{
		if (commandLineParameters.isSourceIndividualXMLs())
		{
			unMarshallIndividualFiles(family);
		}
		else if (commandLineParameters.isSourceAllXMLs())
		{
			unMarshallAllFile(family);
		}
	}

	public void setInLineFlags(CFGFamily family, PersonList personList)
	{
		// Set marriage list in line flags. First turn them all off.
		for (int m=0; m<m_arMarriageList.length; m++)
		{
			if (null != m_arMarriageList[m])
			{
				m_arMarriageList[m].setInLine("false");
			}
		}
		MarriageId baseMarriageId = family.getBaseMarriageId();
		if (null != baseMarriageId)
		{
			int iBaseMarriageId = MarriageIdHelper.getMarriageId(baseMarriageId);
			if (MarriageIdHelper.MARRIAGEID_INVALID != iBaseMarriageId)
			{
				setInLineFlags(iBaseMarriageId, personList);
			}
		}
	}
	
	private void setInLineFlags(int iMarriageId, PersonList personList)
	{
		Marriage marriage = get(iMarriageId);
		if (null != marriage)
		{
			marriage.setInLine("true");
			int iHusbandId = MarriageHelper.getHusbandPersonId(marriage);
			int iWifeId = MarriageHelper.getWifePersonId(marriage);
			if (PersonIdHelper.PERSONID_INVALID != iHusbandId)
			{
				Person husband = personList.get(iHusbandId);
				if (null != husband)
				{
					int iHusbandParentMarriageId = PersonHelper.getParentId(husband);
					if (MarriageIdHelper.MARRIAGEID_INVALID != iHusbandParentMarriageId)
					{
						setInLineFlags(iHusbandParentMarriageId, personList);
					}
				}
				Person wife = personList.get(iWifeId);
				if (null != wife)
				{
					int iWifeParentMarriageId = PersonHelper.getParentId(wife);
					if (MarriageIdHelper.MARRIAGEID_INVALID != iWifeParentMarriageId)
					{
						setInLineFlags(iWifeParentMarriageId, personList);
					}
				}
			}
		}
	}
	
	public Iterator<Marriage> getMarriages()
	{
		return new MarriageListIterator(m_arMarriageList);
	}
	
	public Marriage get(int iMarriageId)
	{
		if ((iMarriageId < m_arMarriageList.length) &&
            (MarriageIdHelper.MARRIAGEID_INVALID != iMarriageId))
		{
			return m_arMarriageList[iMarriageId];
		}
		return null;
	}
	
	public int size()
	{
		int iCount = 0;
		for (int i=0; i<m_arMarriageList.length; i++)
		{
			if (null != m_arMarriageList[i])
			{
				iCount++;
			}
		}
		return iCount;
	}
	
	public void unMarshallAllFile(CFGFamily family)
		throws Exception
	{
		String strDataPath = family.getDataPathSlashTerminated();
		
		m_arMarriageList = new Marriage[family.getMarriageListMaxSize()];
		
		// Setup JAXB unmarshaller
		Unmarshaller unmarshaller = MarshallUtil.createUnMarshaller(family.getSchemaFile());
		
		String strDirectoryMarriages = strDataPath + CFGFamily.APPENDAGE_DATAPATH_MARRIAGES;
		String strFileName = strDirectoryMarriages + File.separator + CFGFamily.MARRIAGES_ALL_FILENAME;

		File fAllFile = new File(strFileName);
		if (fAllFile.exists())
		{
			Marriages marriages = (Marriages)unmarshaller.unmarshal(fAllFile);
			List<Marriage> lMarriages = marriages.getMarriage();
			for (Marriage marriage : lMarriages)
			{
				int iMarriageId = MarriageHelper.getMarriageId(marriage);
				if (MarriageIdHelper.MARRIAGEID_INVALID == iMarriageId)
				{
					throw new Exception("Marriage has invalid marriage id: " + fAllFile.getName());
				}
				if (iMarriageId >= m_arMarriageList.length)
				{
					throw new Exception("Marriage's marriage id out of range: " + iMarriageId);
				}
				m_arMarriageList[iMarriageId] = marriage;
			}
		}
		else
		{
			System.out.println("WARNING: Marriages ALL file not found: " + strFileName);
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
		
		// Build schema "Marriages" instance
		Marriages marriages = new Marriages();
		
		List<Marriage> lMarriages = marriages.getMarriage();
		Iterator<Marriage> iter = this.getMarriages();
		while (iter.hasNext())
		{
			lMarriages.add(iter.next());
		}
		
		String strDataPath = family.getDataPathSlashTerminated();
		String strDirectory = strDataPath + CFGFamily.APPENDAGE_DATAPATH_MARRIAGES;
		MarshallUtil.marshall(marshaller, marriages, strDirectory + File.separator + CFGFamily.MARRIAGES_ALL_FILENAME);
	}
	
	public void unMarshallIndividualFiles(CFGFamily family)
		throws Exception
	{
		String strDataPath = family.getDataPathSlashTerminated();
		
		m_arMarriageList = new Marriage[family.getMarriageListMaxSize()];
		
		// Setup JAXB unmarshaller
		Unmarshaller unmarshaller = MarshallUtil.createUnMarshaller(family.getSchemaFile());
		
		String strDirectoryMarriages = strDataPath + CFGFamily.APPENDAGE_DATAPATH_MARRIAGES;
		File fDirectoryMarriages = new File(strDirectoryMarriages);
		File[] arFiles = fDirectoryMarriages.listFiles(new FileNameFileFilter(CFGFamily.MARRIAGES_FILE_PREFIX, CFGFamily.DOTXML_FILE_POSTFIX));
		if ((null != arFiles) && (0 != arFiles.length))
		{
			for (int i=0; i<arFiles.length; i++)
			{
				Marriage marriage = (Marriage)unmarshaller.unmarshal(arFiles[i]);
				int iMarriageId = MarriageHelper.getMarriageId(marriage);
				if (MarriageIdHelper.MARRIAGEID_INVALID == iMarriageId)
				{
					throw new Exception("Marriage has invalid marriage id: " + arFiles[i].getName());
				}
				if (iMarriageId >= m_arMarriageList.length)
				{
					throw new Exception("Marriage's marriage id out of range: " + iMarriageId);
				}
				m_arMarriageList[iMarriageId] = marriage;
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
		String strDirectory = strDataPath + CFGFamily.APPENDAGE_DATAPATH_MARRIAGES;
		
		Iterator<Marriage> iter = getMarriages();
		while (iter.hasNext())
		{
			Marriage marriage = iter.next();
			String strMarriageFileName = MessageFormat.format(CFGFamily.MARRIAGES_FILE_FORMAT_STRING, marriage.getMarriageId());
			MarshallUtil.marshall(marshaller, marriage, strDirectory + File.separator + strMarriageFileName);
		}
	}
}
