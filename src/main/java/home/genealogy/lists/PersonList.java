package home.genealogy.lists;

import home.genealogy.configuration.CFGFamily;
import home.genealogy.schema.all.Person;
import home.genealogy.schema.all.Persons;
import home.genealogy.schema.all.helpers.PersonHelper;
import home.genealogy.schema.all.helpers.PersonIdHelper;
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

public class PersonList
{
	private Person[] m_arPersonList;
	
	public PersonList(CFGFamily family, CommandLineParameterList listCLP)
		throws Exception
	{
		String strDataPath = family.getDataPath();
		if (!strDataPath.endsWith(File.separator))
		{
			strDataPath += File.separator;
		}
		
		m_arPersonList = new Person[family.getPersonListMaxSize()];
		
		// Setup JAXB unmarshaller
		Unmarshaller unmarshaller = MarshallUtil.createUnMarshaller(family.getSchemaFile());
		
		// Get the list of Person files and unmarshall each file		
		String strDirectoryPersons = strDataPath + CFGFamily.APPENDAGE_DATAPATH_PERSONS;
		File fDirectoryPersons = new File(strDirectoryPersons);
		File[] arFiles = fDirectoryPersons.listFiles(new FileNameFileFilter("P", ".xml"));
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
		
		String strDataPath = family.getDataPath();
		if (!strDataPath.endsWith(File.separator))
		{
			strDataPath += File.separator;
		}
		String strDirectory = strDataPath + CFGFamily.APPENDAGE_DATAPATH_PERSONS;
		MarshallUtil.marshall(marshaller, persons, strDirectory + File.separator + "allPersons.xml");
	}
}
