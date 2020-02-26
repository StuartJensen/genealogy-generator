package home.genealogy.lists;

import home.genealogy.Genealogy;
import home.genealogy.configuration.CFGFamily;
import home.genealogy.schema.all.Person;
import home.genealogy.schema.all.Persons;
import home.genealogy.schema.all.helpers.PersonHelper;
import home.genealogy.schema.all.helpers.PersonIdHelper;
import home.genealogy.util.CommandLineParameterList;
import home.genealogy.util.FileNameFileFilter;
import home.genealogy.util.MarshallUtil;

import java.io.File;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.xml.sax.SAXException;

public class PersonList
{
	private Person[] m_arPersonList;
	
	public PersonList(CFGFamily family)
		throws Exception
	{
		this(family, Genealogy.COMMAND_LINE_PARAM_SOURCE_VALUE_INDIVIDUALXML);
	}
	
	public PersonList(CFGFamily family, String strSource)
		throws Exception
	{
		if ((null == strSource) ||
			(0 == strSource.length()) ||
			strSource.equalsIgnoreCase(Genealogy.COMMAND_LINE_PARAM_SOURCE_VALUE_INDIVIDUALXML))
		{
			unMarshallIndividualFiles(family);
		}
		else if (strSource.equalsIgnoreCase(Genealogy.COMMAND_LINE_PARAM_SOURCE_VALUE_ALLXML))
		{
			unMarshallAllFile(family);
		}
	}
	
	public Iterator<Person> getPersons()
	{
		return new PersonListIterator(m_arPersonList);
	}
	
	public Person get(int iPersonId)
	{
		if ((iPersonId < m_arPersonList.length) &&
            (PersonIdHelper.PERSONID_INVALID != iPersonId))
		{
			return m_arPersonList[iPersonId];
		}
		return null;
	}
	
	public int size()
	{
		int iCount = 0;
		for (int i=0; i<m_arPersonList.length; i++)
		{
			if (null != m_arPersonList[i])
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
		
		m_arPersonList = new Person[family.getPersonListMaxSize()];
		
		// Setup JAXB unmarshaller
		Unmarshaller unmarshaller = MarshallUtil.createUnMarshaller(family.getSchemaFile());
		
		String strDirectoryPersons = strDataPath + CFGFamily.APPENDAGE_DATAPATH_PERSONS;
		String strFileName = strDirectoryPersons + File.separator + CFGFamily.PERSONS_ALL_FILENAME;

		File fAllFile = new File(strFileName);
		if (fAllFile.exists())
		{
			Persons persons = (Persons)unmarshaller.unmarshal(fAllFile);
			List<Person> lPersons = persons.getPerson();
			for (Person person : lPersons)
			{
				int iPersonId = PersonHelper.getPersonId(person);
				if (PersonIdHelper.PERSONID_INVALID == iPersonId)
				{
					throw new Exception("Person has invalid person id: " + fAllFile.getName());
				}
				if (iPersonId >= m_arPersonList.length)
				{
					throw new Exception("Person's person id out of range: " + iPersonId);
				}
				m_arPersonList[iPersonId] = person;
			}
		}
		else
		{
			System.out.println("WARNING: Persons ALL file not found: " + strFileName);
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
		Persons persons = new Persons();
		
		List<Person> lPersons = persons.getPerson();
		Iterator<Person> iter = this.getPersons();
		while (iter.hasNext())
		{
			lPersons.add(iter.next());
		}
		
		String strDataPath = family.getDataPathSlashTerminated();
		String strDirectory = strDataPath + CFGFamily.APPENDAGE_DATAPATH_PERSONS;
		MarshallUtil.marshall(marshaller, persons, strDirectory + File.separator + CFGFamily.PERSONS_ALL_FILENAME);
	}
	
	public void unMarshallIndividualFiles(CFGFamily family)
		throws Exception
	{
		String strDataPath = family.getDataPathSlashTerminated();

		m_arPersonList = new Person[family.getPersonListMaxSize()];
		
		// Setup JAXB unmarshaller
		Unmarshaller unmarshaller = MarshallUtil.createUnMarshaller(family.getSchemaFile());
		
		// Get the list of Person files and unmarshall each file		
		String strDirectoryPersons = strDataPath + CFGFamily.APPENDAGE_DATAPATH_PERSONS;
		File fDirectoryPersons = new File(strDirectoryPersons);
		File[] arFiles = fDirectoryPersons.listFiles(new FileNameFileFilter(CFGFamily.PERSONS_FILE_PREFIX, CFGFamily.DOTXML_FILE_POSTFIX));
		if ((null != arFiles) && (0 != arFiles.length))
		{
			for (int i=0; i<arFiles.length; i++)
			{
				Person person = (Person)unmarshaller.unmarshal(arFiles[i]);
				int iPersonId = PersonHelper.getPersonId(person);
				if (PersonIdHelper.PERSONID_INVALID == iPersonId)
				{
					throw new Exception("Person has invalid person id: " + arFiles[i].getName());
				}
				if (iPersonId >= m_arPersonList.length)
				{
					throw new Exception("Person's person id out of range: " + iPersonId);
				}
				m_arPersonList[iPersonId] = person;
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
		String strDirectory = strDataPath + CFGFamily.APPENDAGE_DATAPATH_PERSONS;
		
		Iterator<Person> iter = getPersons();
		while (iter.hasNext())
		{
			Person person = iter.next();
			String strPersonFileName = MessageFormat.format(CFGFamily.PERSONS_FILE_FORMAT_STRING, person.getPersonId());
			MarshallUtil.marshall(marshaller, person, strDirectory + File.separator + strPersonFileName);
		}
	}
}
