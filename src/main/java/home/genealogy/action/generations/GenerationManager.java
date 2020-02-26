package home.genealogy.action.generations;

import home.genealogy.Genealogy;
import home.genealogy.configuration.CFGFamily;
import home.genealogy.lists.PersonList;
import home.genealogy.output.IOutputStream;
import home.genealogy.schema.all.Person;
import home.genealogy.schema.all.helpers.PersonHelper;
import home.genealogy.util.CommandLineParameterList;

import java.util.Iterator;

public class GenerationManager
{
	private CFGFamily m_family;
	private PersonList m_personList;
	private IOutputStream m_outputStream;
	private CommandLineParameterList m_listCLP;
	
	private int m_iGenerationNth;
	private int m_iGenerationStart;
	private int m_iGenerationEnd;
	
	private static final int TYPE_ALL = 0;
	private static final int TYPE_LIVING = 1;
	private static final int TYPE_DEAD = 2;
	private int m_iType = TYPE_ALL;
	
	private boolean m_bIsRange = false;
	
	public GenerationManager(CFGFamily family, PersonList personList, CommandLineParameterList listCLP, IOutputStream outputStream)
	{
		m_family = family;
		m_personList = personList;
		m_outputStream = outputStream;
		m_listCLP = listCLP;
		
		m_iGenerationNth = m_listCLP.getIntegerValue(Genealogy.COMMAND_LINE_PARAM_ACTION_VALUE_GENERATIONS_GENERATION_NTH, PersonHelper.GENERATION_UNKNOWN);
		m_iGenerationStart = m_listCLP.getIntegerValue(Genealogy.COMMAND_LINE_PARAM_ACTION_VALUE_GENERATIONS_GENERATION_RANGE_START, PersonHelper.GENERATION_UNKNOWN);
		m_iGenerationEnd = m_listCLP.getIntegerValue(Genealogy.COMMAND_LINE_PARAM_ACTION_VALUE_GENERATIONS_GENERATION_RANGE_END, PersonHelper.GENERATION_UNKNOWN);
		String strType = m_listCLP.getValue(Genealogy.COMMAND_LINE_PARAM_ACTION_VALUE_GENERATIONS_TYPE);
		if (null != strType)
		{
			if (strType.equals(Genealogy.COMMAND_LINE_PARAM_ACTION_VALUE_GENERATIONS_TYPE_LIVING))
			{
				m_iType = TYPE_LIVING;
			}
			else if (strType.equals(Genealogy.COMMAND_LINE_PARAM_ACTION_VALUE_GENERATIONS_TYPE_DEAD))
			{
				m_iType = TYPE_DEAD;
			}
		}
		if ((PersonHelper.GENERATION_UNKNOWN == m_iGenerationNth) &&
			(PersonHelper.GENERATION_UNKNOWN != m_iGenerationStart) &&
			(PersonHelper.GENERATION_UNKNOWN != m_iGenerationEnd))
		{
			m_bIsRange = true;
		}
	}
	
	public void process()
	{
		if ((PersonHelper.GENERATION_UNKNOWN == m_iGenerationNth) && !m_bIsRange)
		{
			m_outputStream.output("The specified generation or generation range is unknown or not resolvable!\n");
			return;
		}
		
		int iNotInGeneration = 0;
		int iInGeneration = 0;
		int iInGenerationShown = 0;
		int iUnknown = 0;
		Iterator<Person> iter = m_personList.getPersons();
		while (iter.hasNext())
		{
			Person person = iter.next();
			int iGeneration = PersonHelper.getGenerationFromPrimary(person);
			if (PersonHelper.GENERATION_UNKNOWN == iGeneration)
			{
				m_outputStream.output("Person Id#" + person.getPersonId() + ", " + PersonHelper.getPersonName(person)  + ", does not have an assigned relationship. Generation UNKNOWN!\n");
				iUnknown++;
			}
			
			boolean bIsDesiredGeneration = false;
			if (!m_bIsRange && (iGeneration == m_iGenerationNth))
			{
				bIsDesiredGeneration = true;
			}
			else if (m_bIsRange && (iGeneration >= m_iGenerationStart) && (iGeneration <= m_iGenerationEnd))
			{
				bIsDesiredGeneration = true;
			}
			
			if (bIsDesiredGeneration)
			{	// This person is within the generations that must be checked.
				iInGeneration++;
				boolean bShowPerson = false;
				if ((m_iType == TYPE_LIVING) && PersonHelper.isLiving(person))
				{
					bShowPerson = true;
				}
				else if ((m_iType == TYPE_DEAD) && !PersonHelper.isLiving(person))
				{
					bShowPerson = true;
				}
				else if (m_iType == TYPE_ALL)
				{
					bShowPerson = true;
				}
				if (bShowPerson)
				{
					iInGenerationShown++;
					String strMortality = (PersonHelper.isLiving(person)? "Living" : "Dead");
					m_outputStream.output("Person Id#" + person.getPersonId() + ", " + PersonHelper.getPersonName(person)  + ", Generation: " + iGeneration + ", Mortality: \"" + strMortality + "\".\n");
				}
			}
			else
			{
				iNotInGeneration++;
			}
		}
		m_outputStream.output("In Generation: " + iInGeneration + "\n");
		m_outputStream.output("Not In Generation: " + iNotInGeneration + "\n");
		m_outputStream.output("In Generation Matching Filter Criteria: " + iInGenerationShown + "\n");
		m_outputStream.output("Total with Unknown Generation: " + iUnknown + "\n");
	}

}
