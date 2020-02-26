package home.genealogy.forms.html;

import home.genealogy.configuration.CFGFamily;
import home.genealogy.lists.MarriageList;
import home.genealogy.lists.PersonList;
import home.genealogy.output.IOutputStream;
import home.genealogy.schema.all.Person;
import home.genealogy.schema.all.extensions.PersonSortableByName;
import home.genealogy.schema.all.helpers.PersonHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class HTMLPersonListForm
{
	public static final String PERSON_INDEX_FILE_SYSTEM_SUBDIRECTORY = "index";
	
	private CFGFamily m_family;
	private PersonList m_personList;
	private MarriageList m_marriageList;
	private boolean m_bSuppressLiving;
	private IOutputStream m_outputStream;
	  
	public HTMLPersonListForm(CFGFamily family,
							  PersonList personList,
							  MarriageList marriageList,
							  boolean bSuppressLiving,
							  IOutputStream outputStream)
	{
		m_family = family;
		m_personList = personList;
		m_marriageList = marriageList;
		m_bSuppressLiving = bSuppressLiving;
		m_outputStream = outputStream;
	}
	
	
	public void create()
		throws Exception
	{
		ArrayList<PersonSortableByName> alSortedPersons = new ArrayList<PersonSortableByName>(); 
		Iterator<Person> iter = m_personList.getPersons();
		while (iter.hasNext())
		{
			alSortedPersons.add(new PersonSortableByName(iter.next()));
		}
		m_outputStream.output("Sorting Persons by name...\n");
		Collections.sort(alSortedPersons);
		
		// Get the base output file system path and make sure it 
		// ends with a slash
		String strOutputPath = m_family.getOutputPathHTML();
		if (!strOutputPath.endsWith("\\"))
		{
			strOutputPath += "\\";
		}
		// Make sure the "index" subdirectory exists under
		// the base output file system path
		File fSubdirectory = new File(strOutputPath + PERSON_INDEX_FILE_SYSTEM_SUBDIRECTORY);
		if (!fSubdirectory.exists())
		{
			if (!fSubdirectory.mkdirs())
			{
				throw new Exception("Error creating sub-directory tree for person list files!");
			}
		}
		
		int iCurrentIndex = 0;

		String strWork = null;
		String strFileName = null;
		String strTitle = null;
		int iTerminatorIdx = 0;
		int iOneFifth = alSortedPersons.size()/5;
		for (int iDoc=0; iDoc<5; iDoc++)
		{
			switch(iDoc)
			{
				case 0:
					strFileName = strOutputPath + PERSON_INDEX_FILE_SYSTEM_SUBDIRECTORY + "\\surnames1.htm"; 
					strTitle = "Surname List: Part One";
					iTerminatorIdx = iOneFifth;
					break;
				case 1:
					strFileName = strOutputPath + PERSON_INDEX_FILE_SYSTEM_SUBDIRECTORY + "\\surnames2.htm"; 
					strTitle = "Surname List: Part Two";
					iTerminatorIdx = 2* iOneFifth;
					break;
				case 2:
					strFileName = strOutputPath + PERSON_INDEX_FILE_SYSTEM_SUBDIRECTORY + "\\surnames3.htm"; 
					strTitle = "Surname List: Part Three";
					iTerminatorIdx = 3* iOneFifth;
					break;
				case 3:
					strFileName = strOutputPath + PERSON_INDEX_FILE_SYSTEM_SUBDIRECTORY + "\\surnames4.htm"; 
					strTitle = "Surname List: Part Four";
					iTerminatorIdx = 4* iOneFifth;
					break;
				case 4:
					strFileName = strOutputPath + PERSON_INDEX_FILE_SYSTEM_SUBDIRECTORY + "\\surnames5.htm"; 
					strTitle = "Surname List: Part Five";
					iTerminatorIdx = -1;
					break;
			}
			m_outputStream.output("Generating Person List: " + strFileName + "\n");
			
			HTMLFormOutput output = new HTMLFormOutput(strFileName);

			// Start document creation	
			output.outputSidebarFrontEnd(strTitle, m_family, m_personList, m_marriageList);

			// Top of Document Internal Link Tag
			output.output("<A name=InfoTop></A>");
			output.outputCRLF();

			// Top of Page Title
			output.outputSpan(HTMLFormOutput.styleSelectors.E_PageTopHeader, strTitle);
			output.outputBR();
			output.outputCRLF();
			output.output("<hr width=\"100%\">");
			output.outputCRLF();

			while (true && (iCurrentIndex < alSortedPersons.size()))
			{
				Person thisPerson = alSortedPersons.get(iCurrentIndex).getPerson();
				iCurrentIndex++;
				if ((m_bSuppressLiving) && (PersonHelper.isLiving(thisPerson)))
				{	// Do not include living persons in the list at all.
					continue;
				}
				PersonHelper personHelper = new PersonHelper(thisPerson, m_bSuppressLiving);
				String strPersonName = personHelper.getPersonName();
				if (-1 != iTerminatorIdx)
				{
					if (iCurrentIndex >= iTerminatorIdx)
					{	// Hit terminator person for this file
						break;
					}
				}
				String strGender = "F";
				String strBirthDate = personHelper.getBirthDate();
				String strBirthPlace = personHelper.getBirthPlace();
				if (personHelper.getGender().equals("MALE"))
				{
					strGender = "M";
				}
				output.output("<span class=\"pageBodyNormalLink\">");
				strWork = m_family.getUrlPrefix() + HTMLShared.PERINFODIR + "/" + HTMLShared.PERINFOFILENAME + personHelper.getPersonId() + ".htm";
				output.outputStartAnchor(strWork);
				if ((0 != strBirthDate.length()) && (0 != strBirthPlace.length()))
				{
					strWork = strPersonName + ", (" + strGender + ") B: " + strBirthDate + ", At: " + strBirthPlace;
				}
				else if ((0 != strBirthDate.length()) && (0 == strBirthPlace.length()))
				{
					strWork = strPersonName + ", (" + strGender + ") B: " + strBirthDate;
				}
				else if ((0 == strBirthDate.length()) && (0 != strBirthPlace.length()))
				{
					strWork = strPersonName + ", (" + strGender + ") B At: " + strBirthPlace;
				}
				else if ((0 == strBirthDate.length()) && (0 == strBirthPlace.length()))
				{
					strWork = strPersonName + ", (" + strGender + ")";
				}
				output.output(strWork);
				output.outputEndAnchor();
				output.output("</span>");
				output.outputBR();
				output.outputCRLF();
			}
			output.outputSidebarBackEnd();
			output.commit();
			output = null;
			m_outputStream.output("Completed Generating Person List: " + strFileName + "\n");
		}
	}
	
}
