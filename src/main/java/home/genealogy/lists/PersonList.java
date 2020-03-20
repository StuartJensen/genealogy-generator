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
import home.genealogy.schema.all.Person;
import home.genealogy.schema.all.Persons;
import home.genealogy.schema.all.helpers.PersonHelper;
import home.genealogy.schema.all.helpers.PersonIdHelper;
import home.genealogy.util.FileNameFileFilter;
import home.genealogy.util.MarshallUtil;

public class PersonList
{
	private Person[] m_arPersonList;
	private IOutputStream m_outputStream;
	
	public PersonList(CFGFamily family, CommandLineParameters commandLineParameters, IOutputStream outputStream)
		throws InvalidParameterException, JAXBException, SAXException
	{
		m_outputStream = outputStream;
		outputStream.output("Person List: Initiating load.\n");
		if (commandLineParameters.isSourceIndividualXMLs())
		{
			unMarshallIndividualFiles(family);
			outputStream.output("Person List: Count: " + size() + ": Loaded from Individual XML files.\n");
		}
		else if (commandLineParameters.isSourceAllXMLs())
		{
			unMarshallAllFile(family);
			outputStream.output("Person List: Count: " + size() + ": Loaded from ALL XML file.\n");
		}
	}
	
	public void persist(GenealogyContext context)
		throws Exception
	{
		if (context.getCommandLineParameters().isDestinationIndividualXMLs())
		{
			marshallIndividualFiles(context.getFamily(), context.getFormattedOutput());
			context.output("Person List: Count: " + size() + ": Persisted to Individual XML files.\n");
		}
		else if (context.getCommandLineParameters().isDestinationAllXMLs())
		{
			marshallAllFile(context.getFamily(), context.getFormattedOutput());
			context.output("Person List: Count: " + size() + ": Persisted to ALL XML file.\n");
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
		throws InvalidParameterException, JAXBException, SAXException
	{
		String strDataPath = family.getDataPathSlashTerminated();
		
		m_arPersonList = new Person[family.getPersonListMaxSize()];
		
		// Setup JAXB unmarshaller
		Unmarshaller unmarshaller = MarshallUtil.createUnMarshaller(family.getSchemaFile());
		unmarshaller.setEventHandler(new ListsValidationEventHandler(m_outputStream));
		
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
					throw new InvalidParameterException("Person has invalid person id: " + fAllFile.getName());
				}
				if (iPersonId >= m_arPersonList.length)
				{
					throw new InvalidParameterException("Person's person id out of range: " + iPersonId);
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
		throws InvalidParameterException, JAXBException, SAXException
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
					throw new InvalidParameterException("Person has invalid person id: " + arFiles[i].getName());
				}
				if (iPersonId >= m_arPersonList.length)
				{
					throw new InvalidParameterException("Person's person id out of range: " + iPersonId);
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
			String strPersonFileName = MessageFormat.format(CFGFamily.PERSONS_FILE_FORMAT_STRING, String.valueOf(person.getPersonId()));
			MarshallUtil.marshall(marshaller, person, strDirectory + File.separator + strPersonFileName);
		}
	}
	
	public int replacePlaceId(String strIdToBeReplaced,
									String strIdReplacement,
									IOutputStream outputStream)
	{
		int iCount = 0;
		Iterator<Person> iter = getPersons();
		while (iter.hasNext())
		{
			iCount += PersonHelper.replacePlaceId(iter.next(),
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
