package home.genealogy.forms.html;

import home.genealogy.configuration.CFGFamily;
import home.genealogy.indexes.IndexMarriageToChildren;
import home.genealogy.indexes.IndexMarriageToSpouses;
import home.genealogy.indexes.IndexMarriagesToReferences;
import home.genealogy.indexes.IndexPersonToMarriages;
import home.genealogy.indexes.IndexPersonToParentMarriage;
import home.genealogy.indexes.IndexPersonsToReferences;
import home.genealogy.indexes.TaggedContainerDescriptor;
import home.genealogy.lists.MarriageList;
import home.genealogy.lists.PersonList;
import home.genealogy.lists.PhotoList;
import home.genealogy.lists.ReferenceList;
import home.genealogy.output.IOutputStream;
import home.genealogy.schema.all.Marriage;
import home.genealogy.schema.all.Person;
import home.genealogy.schema.all.Reference;
import home.genealogy.schema.all.helpers.MarriageHelper;
import home.genealogy.schema.all.helpers.MarriageIdHelper;
import home.genealogy.schema.all.helpers.PersonHelper;
import home.genealogy.schema.all.helpers.PersonIdHelper;
import home.genealogy.schema.all.helpers.ReferenceHelper;
import home.genealogy.util.CommandLineParameterList;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

public class HTMLFGSForm
{
	public static final String FGS_FILE_SYSTEM_SUBDIRECTORY = "fgs";
	
	private CFGFamily m_family;
	private PersonList m_personList;
	private MarriageList m_marriageList;
	private ReferenceList m_referenceList;
	private PhotoList m_photoList;
	private boolean m_bSuppressLiving;
	private boolean m_bSuppressLds;
	private CommandLineParameterList m_listCLP;
	private IOutputStream m_outputStream;
	  
	public HTMLFGSForm(CFGFamily family,
							  PersonList personList,
							  MarriageList marriageList,
							  ReferenceList referenceList,
							  PhotoList photoList,
							  boolean bSuppressLiving,
							  boolean bSuppressLds,
							  CommandLineParameterList listCLP,
							  IOutputStream outputStream)
	{
		m_family = family;
		m_personList = personList;
		m_marriageList = marriageList;
		m_referenceList = referenceList;
		m_photoList = photoList;
		m_bSuppressLiving = bSuppressLiving;
		m_bSuppressLds = bSuppressLds;
		m_listCLP = listCLP;
		m_outputStream = outputStream;
	}
	
	public void create()
		throws Exception
	{
		// Get the base output file system path and make sure it 
		// ends with a slash
		String strOutputPath = m_family.getOutputPathHTML();
		if (!strOutputPath.endsWith("\\"))
		{
			strOutputPath += "\\";
		}
		// Make sure the "reference" subdirectory exists under
		// the base output file system path
		File fSubdirectory = new File(strOutputPath + FGS_FILE_SYSTEM_SUBDIRECTORY);
		if (!fSubdirectory.exists())
		{
			if (!fSubdirectory.mkdirs())
			{
				throw new Exception("Error creating sub-directory tree for fgs files!");
			}
		}		
		
		// Create necessary Indexes
		IndexPersonToParentMarriage idxPerToParentMar = new IndexPersonToParentMarriage(m_family, m_personList);
		IndexPersonToMarriages idxPerToMar = new IndexPersonToMarriages(m_family, m_marriageList);
		IndexMarriageToSpouses idxMarToSpouses = new IndexMarriageToSpouses(m_family, m_marriageList);
		IndexPersonsToReferences idxPerToRef = new IndexPersonsToReferences(m_family, m_personList, m_referenceList);
		IndexMarriagesToReferences idxMarToRef = new IndexMarriagesToReferences(m_family, m_personList, m_marriageList, m_referenceList);
		IndexMarriageToChildren idxMarToChildren = new IndexMarriageToChildren(m_family, m_listCLP, m_personList);
		
		Iterator<Marriage> iter = m_marriageList.getMarriages();
		while (iter.hasNext())
		{
			Marriage thisMarriage = iter.next();
			createFGS(m_family, m_personList, m_marriageList, m_referenceList,
 					  m_photoList, m_bSuppressLiving, idxPerToParentMar,
 					  idxPerToMar, idxMarToSpouses, idxPerToRef, idxMarToRef,
 					  idxMarToChildren, strOutputPath, thisMarriage);
		}
		m_outputStream.output("Completed Generating All FGS Files\n");
	}
	
	private void createFGS(CFGFamily family,
			 PersonList personList,
			 MarriageList marriageList,
			 ReferenceList referenceList,
			 PhotoList photoList,
			 boolean bSuppressLiving,
			 IndexPersonToParentMarriage idxPerToParentMar,
			 IndexPersonToMarriages idxPerToMar,
			 IndexMarriageToSpouses idxMarToSpouses,
			 IndexPersonsToReferences idxPerToRef,
			 IndexMarriagesToReferences idxMarToRef,
			 IndexMarriageToChildren idxMarToChildren,
			 String strOutputPath,
			 Marriage marriage)
	throws Exception
	{
		int iMarriageId = MarriageHelper.getMarriageId(marriage);
		String strFileName = strOutputPath + FGS_FILE_SYSTEM_SUBDIRECTORY + "\\" + HTMLShared.FGSFILENAME + iMarriageId + ".htm";
		m_outputStream.output("Generating FGS File: " + strFileName + "\n");
		HTMLFormOutput output = new HTMLFormOutput(strFileName);

		Person husband = personList.get(MarriageHelper.getHusbandPersonId(marriage));
		Person wife = personList.get(MarriageHelper.getWifePersonId(marriage));
		if ((null != husband) && (null != wife))
		{
			PersonHelper husbandHelper = new PersonHelper(husband, bSuppressLiving);
			PersonHelper wifeHelper = new PersonHelper(wife, bSuppressLiving);
			MarriageHelper marriageHelper = new MarriageHelper(marriage, husbandHelper, wifeHelper, bSuppressLiving);
			String strMarriageTitle = HTMLShared.buildSimpleMarriageNameString(personList, idxMarToSpouses, iMarriageId, bSuppressLiving, "{0} and {2}");
			output.outputSidebarFrontEnd("Family Group Sheet: " + strMarriageTitle, m_family, personList, marriageList);
			
			// Top of Document Internal Link Tag
			output.output("<A name=InfoTop></A>");
			output.outputCRLF();

			// Husband Data
			String strUrl = "<A href=\"";
			strUrl += family.getUrlPrefix() + HTMLShared.PERINFODIR + "/" + HTMLShared.PERINFOFILENAME +  husbandHelper.getPersonId() + ".htm";
			strUrl += "\">";
			strUrl += husbandHelper.getPersonName();
			strUrl += "</a>";
			String strHusbandNameLine = "Husband:&nbsp;" + strUrl + " - ID#&nbsp;" + husbandHelper.getPersonId();
			output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyLargeText, strHusbandNameLine);
			output.outputBR();
			output.outputCRLF();
			// Husband Data
			output.output("<table width=\"500\" border=\"1\">");
			outputBirth(family, husbandHelper, idxPerToRef, output);
			outputChristening(family, husbandHelper, idxPerToRef, output);
			outputMarriage(family, husbandHelper, wifeHelper, idxMarToRef, iMarriageId, personList, marriageList, m_bSuppressLiving, output);
			outputDeath(family, husbandHelper, idxPerToRef, output);
			outputBurial(family, husbandHelper, idxPerToRef, output);
			outputCemetery(family, husbandHelper, idxPerToRef, output);
			outputParents(family, husbandHelper, personList, marriageList, idxPerToRef, bSuppressLiving, output);
			output.output("</table>");
			output.outputBR();
			output.outputCRLF();
			
			// Wife Data
			strUrl = "<A href=\"";
			strUrl += family.getUrlPrefix() + HTMLShared.PERINFODIR + "/" + HTMLShared.PERINFOFILENAME +  wifeHelper.getPersonId() + ".htm";
			strUrl += "\">";
			strUrl += wifeHelper.getPersonName();
			strUrl += "</a>";
			String strWifeNameLine = "Wife:&nbsp;" + strUrl + " - ID#&nbsp;" + wifeHelper.getPersonId();
			output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyLargeText, strWifeNameLine);
			output.outputBR();
			output.outputCRLF();
			// Husband Data
			output.output("<table width=\"500\" border=\"1\">");
			outputBirth(family, wifeHelper, idxPerToRef, output);
			outputChristening(family, wifeHelper, idxPerToRef, output);
			outputDeath(family, wifeHelper, idxPerToRef, output);
			outputBurial(family, wifeHelper, idxPerToRef, output);
			outputCemetery(family, wifeHelper, idxPerToRef, output);
			outputParents(family, wifeHelper, personList, marriageList, idxPerToRef, bSuppressLiving, output);
			output.output("</table>");
			output.outputBR();
			output.outputCRLF();
			
			// Children
			int iChildNumber = 1;
			ArrayList<Integer> alChildrenIds = idxMarToChildren.getChildrenPersonIds(iMarriageId);
			if (null != alChildrenIds)
			{
				// Children Section Header
				output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyLargeText, "Children:");
				output.outputBR(2);
				output.outputCRLF();
				
				for (int c=0; c<alChildrenIds.size(); c++)
				{
					Integer intChildId = alChildrenIds.get(c);
					if (null != intChildId)
					{
						int iChildId = intChildId.intValue();
						Person child = personList.get(iChildId);
						if (null != child)
						{
							PersonHelper childHelper = new PersonHelper(child, bSuppressLiving);
							// "Child #1 FEMALE"
							String strGenderLine = "Child#&nbsp;" + iChildNumber + "&nbsp;" + PersonHelper.getGender(child);
							output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyBoldNormalText, strGenderLine);
							output.outputBR();
							output.outputCRLF();
							
							// "SMITH, Jane - ID#5"
							output.output("<span class=\"pageBodyBoldNormalText\">");
							strUrl = family.getUrlPrefix() + HTMLShared.PERINFODIR + "/" + HTMLShared.PERINFOFILENAME +  iChildId + ".htm";
							output.outputStartAnchor(strUrl);
							output.output(childHelper.getPersonName());
							output.outputEndAnchor();
							output.output("&nbsp;-&nbsp;ID#&nbsp;" + iChildId);
							output.output("</span>");
							output.outputBR();
							output.outputCRLF();
							
							// Start Table
							output.output("<table width=\"500\" border=\"1\">");
							// Child Data
							outputBirth(family, childHelper, idxPerToRef, output);
							outputChristening(family, childHelper, idxPerToRef, output);
							outputDeath(family, childHelper, idxPerToRef, output);
							outputBurial(family, childHelper, idxPerToRef, output);
							outputCemetery(family, childHelper, idxPerToRef, output);
							// Child's Spouse Data
							outputSpouse(family, child, personList, marriageList, idxPerToMar, idxMarToSpouses, bSuppressLiving, output);
							// End Table
							output.output("</table>");
							output.outputBR();
							output.outputCRLF();
							
							iChildNumber++;
						}
					}
				}
			}

/*			
			if (pIndexMarriageToChildren->StartChildrenOfMarriageEnum(m_lMarriageId, &lCookie))
			{
				while (pIndexMarriageToChildren->NextChildrenOfMarriageEnum(m_lMarriageId, &lCookie, &childId))
				{
					Person *pChild = m_pPersonList->get(childId);
					if (pChild)
					{
						FormsPersonAccessor childAccessor(pChild, m_pConfiguration->getSuppressLiving());
						// "Child #1 FEMALE"
						strWork.Format("Child#&nbsp;%d&nbsp;%s",  iChildNumber, childAccessor.getGender());
						output.outputSpan(E_PageBodyBoldNormalText, strWork);
						output.outputBR();
						output.outputFormattingCRLF();
						// "SMITH, Jane - ID#5"
						CString strAsterisk = "";
						if (childAccessor.isInLine())
						{
							strAsterisk = "*";
						}
						output.output("<span class=\"pageBodyBoldNormalText\">");
						strWork.Format("%s%s/%s%d.htm", m_pConfiguration->getBasePath(), PERINFODIR, PERINFOFILENAME, childAccessor.getPersonIdAsLong());
						output.outputStartAnchor(strWork);
						output.output(strAsterisk + "&nbsp;" + childAccessor.getFullBasicName());
						output.outputEndAnchor();
						strWork.Format("&nbsp;-&nbsp;ID#&nbsp;%d", childId);
						output.output(strWork);
						output.output("</span>");
						output.outputBR();
						output.outputFormattingCRLF();
						// Start Table
						output.output("<table width=\"450\" border=\"1\">");
						// Child Data
						OutputBirth(pChild, pOutputStream);
						OutputChristening(pChild, pOutputStream);
						OutputDeath(pChild, pOutputStream);
						OutputBurial(pChild, pOutputStream);
						OutputCemetery(pChild, pOutputStream);
						// Child's Spouse Data
						OutputSpouse(pChild, pOutputStream);
						// End Table
						output.output("</table>");
						output.outputBR();
						output.outputFormattingCRLF();

						iChildNumber++;
					}
				}
			}
*/			
		}
		
		output.outputSidebarBackEnd();
		
		output.commit();
		output = null;
	}
	
	private void outputParents(CFGFamily family, PersonHelper personHelper, PersonList personList,
			 MarriageList marriageList, IndexPersonsToReferences idxPerToRef, boolean bSuppressLiving,
			 HTMLFormOutput output)
	{
		Person father = null;
		Person mother = null;
		int iParentMarriageId = personHelper.getParentId();
		if (MarriageIdHelper.MARRIAGEID_INVALID != iParentMarriageId)
		{
			Marriage marriage = marriageList.get(iParentMarriageId);
			if (null != marriage)
			{
				int iFatherId = MarriageHelper.getHusbandPersonId(marriage);
				int iMotherId = MarriageHelper.getWifePersonId(marriage);
				if (PersonIdHelper.PERSONID_INVALID != iFatherId)
				{
					father = personList.get(iFatherId);
				}
				if (PersonIdHelper.PERSONID_INVALID != iMotherId)
				{
					mother = personList.get(iMotherId);
				}
			}
		}
		
		if ((null != father) && (null != mother))
		{
			PersonHelper fatherHelper = new PersonHelper(father, bSuppressLiving);
			PersonHelper motherHelper = new PersonHelper(mother, bSuppressLiving);
			String strFatherName = fatherHelper.getPersonName();
			String strMotherName = motherHelper.getPersonName();
			if ((0 != strFatherName.length()) || (0 != strMotherName.length()))
			{
				ArrayList<TaggedContainerDescriptor> alLinkCToFatherRefs = idxPerToRef.getReferencesForPerson(fatherHelper.getPersonId(), "LinkCToFather");
				ArrayList<TaggedContainerDescriptor> alLinkCToMotherRefs = idxPerToRef.getReferencesForPerson(motherHelper.getPersonId(), "LinkCToMother");
				// Output Father
				String strUrl = family.getUrlPrefix() + HTMLShared.PERINFODIR + "/" + HTMLShared.PERINFOFILENAME +  fatherHelper.getPersonId() + ".htm";
				output.output("<tr><td>");
				output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyBoldNormalText, "Father:&nbsp;");
				output.outputStartAnchor(strUrl);
				output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyNormalText, strFatherName);
				output.outputEndAnchor();
				if (0 != alLinkCToFatherRefs.size())
				{
					output.output("&nbsp;&nbsp;");
					String strHRef = HTMLShared.getReferenceHRef(family, alLinkCToFatherRefs.get(0));
					output.outputStartAnchor(strHRef);
					output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyNormalText, "[Prove Link]");
					output.outputEndAnchor();
				}
				// Show "[Family]" Link
				strUrl = family.getUrlPrefix() + HTMLShared.FGSDIR + "/" + HTMLShared.FGSFILENAME +  iParentMarriageId + ".htm";
				output.output("&nbsp;&nbsp;");
				output.outputStartAnchor(strUrl);
				output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyNormalText, "[Family]");
				output.outputEndAnchor();
				output.outputBR();
				output.outputCRLF();
				// Output Mother
				strUrl = family.getUrlPrefix() + HTMLShared.PERINFODIR + "/" + HTMLShared.PERINFOFILENAME +  motherHelper.getPersonId() + ".htm";
				output.output("<tr><td>");
				output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyBoldNormalText, "Mother:&nbsp;");
				output.outputStartAnchor(strUrl);
				output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyNormalText, strMotherName);
				output.outputEndAnchor();
				if (0 != alLinkCToMotherRefs.size())
				{
					output.output("&nbsp;&nbsp;");
					String strHRef = HTMLShared.getReferenceHRef(family, alLinkCToMotherRefs.get(0));
					output.outputStartAnchor(strHRef);
					output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyNormalText, "[Prove Link]");
					output.outputEndAnchor();
				}
			}
		}	
		
	}
	
	private void outputBirth(CFGFamily family, PersonHelper personHelper, IndexPersonsToReferences idxPerToRef, HTMLFormOutput output)
	{
		String strEvent = personHelper.getBirthDate();
		String strPlace = personHelper.getBirthPlace();
		if ((0 != strEvent.length()) || (0 != strPlace.length()))
		{
			ArrayList<TaggedContainerDescriptor> alBornDRefs = idxPerToRef.getReferencesForPerson(personHelper.getPersonId(), "BornD");
			ArrayList<TaggedContainerDescriptor> alBornPRefs = idxPerToRef.getReferencesForPerson(personHelper.getPersonId(), "BornP");
			
			output.output("<tr><td>");
			output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyBoldNormalText, "Born:&nbsp;");
			if (0 != alBornDRefs.size())
			{
				String strHRef = HTMLShared.getReferenceHRef(family, alBornDRefs.get(0));
				output.outputStartAnchor(strHRef);
			}
			output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyNormalText, strEvent);
			if (0 != alBornDRefs.size())
			{
				output.outputEndAnchor();
			}
			output.outputBR();
			output.outputCRLF();
			output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyBoldNormalText, "Place:&nbsp;");
			if (0 != alBornPRefs.size())
			{
				String strHRef = HTMLShared.getReferenceHRef(family, alBornPRefs.get(0));

				output.outputStartAnchor(strHRef);
			}
			output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyNormalText, strPlace);
			if (0 != alBornPRefs.size())
			{
				output.outputEndAnchor();
			}
			output.outputBR();
			output.outputCRLF();
			output.output("</td></tr>");
		}
	}
	
	private void outputChristening(CFGFamily family, PersonHelper personHelper, IndexPersonsToReferences idxPerToRef, HTMLFormOutput output)
	{
		String strEvent = personHelper.getChrDate();
		String strPlace = personHelper.getChrPlace();
		
		ArrayList<TaggedContainerDescriptor> alChrDRefs = idxPerToRef.getReferencesForPerson(personHelper.getPersonId(), "ChrD");
		ArrayList<TaggedContainerDescriptor> alChrPRefs = idxPerToRef.getReferencesForPerson(personHelper.getPersonId(), "ChrP");
		
		if ((0 != strEvent.length()) || (0 != strPlace.length()))
		{
			output.output("<tr><td>");
			output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyBoldNormalText, "Chr:&nbsp;");
			if (0 != alChrDRefs.size())
			{
				String strHRef = HTMLShared.getReferenceHRef(family, alChrDRefs.get(0));
				output.outputStartAnchor(strHRef);
			}
			output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyNormalText, strEvent);
			if (0 != alChrDRefs.size())
			{
				output.outputEndAnchor();
			}
			output.outputBR();
			output.outputCRLF();
			output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyBoldNormalText, "Place:&nbsp;");
			if (0 != alChrPRefs.size())
			{
				String strHRef = HTMLShared.getReferenceHRef(family, alChrPRefs.get(0));

				output.outputStartAnchor(strHRef);
			}
			output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyNormalText, strPlace);
			if (0 != alChrPRefs.size())
			{
				output.outputEndAnchor();
			}
			output.outputBR();
			output.outputCRLF();
			output.output("</td></tr>");
		}
	}
	
	
	private void outputDeath(CFGFamily family, PersonHelper personHelper, IndexPersonsToReferences idxPerToRef, HTMLFormOutput output)
	{
		String strEvent = personHelper.getDeathDate();
		String strPlace = personHelper.getDeathPlace();
		
		ArrayList<TaggedContainerDescriptor> alDiedDRefs = idxPerToRef.getReferencesForPerson(personHelper.getPersonId(), "DiedD");
		ArrayList<TaggedContainerDescriptor> alDiedPRefs = idxPerToRef.getReferencesForPerson(personHelper.getPersonId(), "DiedP");
		
		if ((0 != strEvent.length()) || (0 != strPlace.length()))
		{
			output.output("<tr><td>");
			output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyBoldNormalText, "Died:&nbsp;");
			if (0 != alDiedDRefs.size())
			{
				String strHRef = HTMLShared.getReferenceHRef(family, alDiedDRefs.get(0));
				output.outputStartAnchor(strHRef);
			}
			output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyNormalText, strEvent);
			if (0 != alDiedDRefs.size())
			{
				output.outputEndAnchor();
			}
			output.outputBR();
			output.outputCRLF();
			output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyBoldNormalText, "Place:&nbsp;");
			if (0 != alDiedPRefs.size())
			{
				String strHRef = HTMLShared.getReferenceHRef(family, alDiedPRefs.get(0));

				output.outputStartAnchor(strHRef);
			}
			output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyNormalText, strPlace);
			if (0 != alDiedPRefs.size())
			{
				output.outputEndAnchor();
			}
			output.outputBR();
			output.outputCRLF();
			output.output("</td></tr>");
		}
	}
	
	private void outputBurial(CFGFamily family, PersonHelper personHelper, IndexPersonsToReferences idxPerToRef, HTMLFormOutput output)
	{
		String strEvent = personHelper.getBurialDate();
		String strPlace = personHelper.getBurialPlace();
		
		ArrayList<TaggedContainerDescriptor> alDiedDRefs = idxPerToRef.getReferencesForPerson(personHelper.getPersonId(), "BurD");
		ArrayList<TaggedContainerDescriptor> alDiedPRefs = idxPerToRef.getReferencesForPerson(personHelper.getPersonId(), "BurP");
		
		if ((0 != strEvent.length()) || (0 != strPlace.length()))
		{
			output.output("<tr><td>");
			output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyBoldNormalText, "Buried:&nbsp;");
			if (0 != alDiedDRefs.size())
			{
				String strHRef = HTMLShared.getReferenceHRef(family, alDiedDRefs.get(0));
				output.outputStartAnchor(strHRef);
			}
			output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyNormalText, strEvent);
			if (0 != alDiedDRefs.size())
			{
				output.outputEndAnchor();
			}
			output.outputBR();
			output.outputCRLF();
			output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyBoldNormalText, "Place:&nbsp;");
			if (0 != alDiedPRefs.size())
			{
				String strHRef = HTMLShared.getReferenceHRef(family, alDiedPRefs.get(0));

				output.outputStartAnchor(strHRef);
			}
			output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyNormalText, strPlace);
			if (0 != alDiedPRefs.size())
			{
				output.outputEndAnchor();
			}
			output.outputBR();
			output.outputCRLF();
			output.output("</td></tr>");
		}
	}
	
	private void outputCemetery(CFGFamily family, PersonHelper personHelper, IndexPersonsToReferences idxPerToRef, HTMLFormOutput output)
	{
		String strEvent = personHelper.getBurialCemeteryName();
		String strPlace = personHelper.getBurialCemeteryPlotAddress();
		
		ArrayList<TaggedContainerDescriptor> alCemeteryNameRefs = idxPerToRef.getReferencesForPerson(personHelper.getPersonId(), "CemeteryName");
		ArrayList<TaggedContainerDescriptor> alCemeteryPlotRefs = idxPerToRef.getReferencesForPerson(personHelper.getPersonId(), "CemeteryPlot");
		
		if ((0 != strEvent.length()) || (0 != strPlace.length()))
		{
			output.output("<tr><td>");
			output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyBoldNormalText, "Cemetery:&nbsp;");
			if (0 != alCemeteryNameRefs.size())
			{
				String strHRef = HTMLShared.getReferenceHRef(family, alCemeteryNameRefs.get(0));
				output.outputStartAnchor(strHRef);
			}
			output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyNormalText, strEvent);
			if (0 != alCemeteryNameRefs.size())
			{
				output.outputEndAnchor();
			}
			output.outputBR();
			output.outputCRLF();
			output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyBoldNormalText, "Place:&nbsp;");
			if (0 != alCemeteryPlotRefs.size())
			{
				String strHRef = HTMLShared.getReferenceHRef(family, alCemeteryPlotRefs.get(0));
				output.outputStartAnchor(strHRef);
			}
			output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyNormalText, strPlace);
			if (0 != alCemeteryPlotRefs.size())
			{
				output.outputEndAnchor();
			}
			output.outputBR();
			output.outputCRLF();
			output.output("</td></tr>");
		}
	}
	
	
	private void outputMarriage(CFGFamily family, PersonHelper husbandHelper, PersonHelper wifeHelper,
							    IndexMarriagesToReferences idxMarToRef, int iMarriageId, PersonList personList, MarriageList marriageList,
								boolean bSuppressLiving, HTMLFormOutput output)
	{
		Marriage marriage = marriageList.get(iMarriageId);
		if (null != marriage)
		{
			MarriageHelper mh = new MarriageHelper(marriage, husbandHelper, wifeHelper, bSuppressLiving);
			String strDate = mh.getDate();
			String strPlace = mh.getPlace();
			if ((0 != strDate.length()) || (0 != strPlace.length()))
			{
				ArrayList<TaggedContainerDescriptor> alDateRefs = idxMarToRef.getReferencesForMarriage(iMarriageId, "MarrD");
				ArrayList<TaggedContainerDescriptor> alPlaceRefs = idxMarToRef.getReferencesForMarriage(iMarriageId, "MarrP");

				output.output("<tr><td>");
				output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyBoldNormalText, "Marr:&nbsp;");
				if (0 != alDateRefs.size())
				{
					String strHRef = HTMLShared.getReferenceHRef(family, alDateRefs.get(0));
					output.outputStartAnchor(strHRef);
				}
				output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyNormalText, strDate);
				if (0 != alDateRefs.size())
				{
					output.outputEndAnchor();
				}
				output.outputBR();
				output.outputCRLF();
				output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyBoldNormalText, "Place:&nbsp;");
				if (0 != alPlaceRefs.size())
				{
					String strHRef = HTMLShared.getReferenceHRef(family, alPlaceRefs.get(0));
					output.outputStartAnchor(strHRef);
				}
				output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyNormalText, strPlace);
				if (0 != alPlaceRefs.size())
				{
					output.outputEndAnchor();
				}
				output.outputBR();
				output.outputCRLF();
				output.output("</td></tr>");
			}
		}
	}
	
	private void outputSpouse(CFGFamily family, Person person, PersonList personList, MarriageList marriageList, IndexPersonToMarriages idxPerToMar, IndexMarriageToSpouses idxMarToSpouses, boolean bSuppressLiving, HTMLFormOutput output)
	{
		// Find the marriage that should be used
		// 1 - In-Line Marriage
		// 2 - Any other marriage
		Marriage marriage = null;
		ArrayList<Integer> alMarriageIds = idxPerToMar.getMarriageIds(PersonHelper.getPersonId(person));
		if (null != alMarriageIds)
		{
			for (int m=0; m<alMarriageIds.size(); m++)
			{
				int iMarriageId = alMarriageIds.get(m).intValue();
				marriage = marriageList.get(iMarriageId);
				if (null != marriage)
				{	// Found a marriage, is it inline
					if (MarriageHelper.isInLine(marriage))
					{	// yes, use it
						break;
					}
				}
			}
		}
		
		if (null != marriage)
		{
			Person spouse = null;
			int iMarriageId = MarriageHelper.getMarriageId(marriage);
			int iHusbandId = MarriageHelper.getHusbandPersonId(marriage);
			int iWifeId = MarriageHelper.getWifePersonId(marriage);			
			int iPersonId = PersonHelper.getPersonId(person);
			if (PersonIdHelper.PERSONID_INVALID != iPersonId)
			{
				if (iPersonId == iHusbandId)
				{
					spouse = personList.get(iWifeId);
				}
				else if (iPersonId == iWifeId)
				{
					spouse = personList.get(iHusbandId);
				}
				// Did we find the spouse?
				if (null != spouse)
				{	// Yes
					PersonHelper spouseHelper = new PersonHelper(spouse, bSuppressLiving);
					String strSpouseName = spouseHelper.getPersonName();
					if ((null != strSpouseName) && (0 != strSpouseName.length()))
					{
						output.output("<tr><td>");
						output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyBoldNormalText, "Spouse:&nbsp;");
						// Show Spouse Name
						String strUrl = family.getUrlPrefix() + HTMLShared.PERINFODIR + "/" + HTMLShared.PERINFOFILENAME +  spouseHelper.getPersonId() + ".htm";
						output.outputStartAnchor(strUrl);
						output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyNormalText, strSpouseName);
						output.outputEndAnchor();
						// Show "[Prove Link] Link"
//						if (dataPointRefSpouse.Found())
//						{
//							output.output("&nbsp;&nbsp;");
//							output.outputStartAnchor(dataPointRefSpouse.GetHRef());
//							output.outputSpan(E_PageBodyNormalText, "[Prove Link]");
//							output.outputEndAnchor();
//						}
						// Show "[Family]" Link
						output.output("&nbsp;&nbsp;");
						strUrl = family.getUrlPrefix() + HTMLShared.FGSDIR + "/" + HTMLShared.FGSFILENAME +  iMarriageId + ".htm";
						output.outputStartAnchor(strUrl);
						output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyNormalText, "[Family]");
						output.outputEndAnchor();

						output.outputBR();
						output.outputCRLF();
						output.output("</td></tr>");

					}
				}
			}
		}
	}
	
}


/*

void FormsHtmlFgs::Create()
{
	long lCookie;
	CString strWork;
	CString strFileName;
	CString strEvent, strPlace;
	strFileName.Format("%s%s/%s%d.htm", m_pConfiguration->getDestinationPath(), FGSDIR, FGSFILENAME, m_lMarriageId); 
	m_pStatus->Log("Generating FGS: " + strFileName, LogLevel_Finest);
	m_pStatus->UpdateStatus("Generating FGS: " + strFileName);
	IndexMarriageToChildren *pIndexMarriageToChildren = m_pIndex->GetIndexMarriageToChildren();

	FormsHtmlOutputStream *pOutputStream = new FormsHtmlOutputStream(strFileName, m_pStatus);

	Marriage *pMarriage = m_pMarriageList->get(m_lMarriageId);
	if (pMarriage)
	{
		PERSONID idHusband = pMarriage->getHusbandIdAsLong();
		PERSONID idWife = pMarriage->getWifeIdAsLong();

		FourGenerations fourGenHusband(m_pFamily, idHusband);
		FourGenerations fourGenWife(m_pFamily, idWife);
		Person *pHusband = fourGenHusband.getPerson();
		Person *pWife = fourGenWife.getPerson();
		FormsPersonAccessor husbandAccessor(pHusband, m_pConfiguration->getSuppressLiving());
		FormsPersonAccessor wifeAccessor(pWife, m_pConfiguration->getSuppressLiving());

		CString strMarriageName = FormsHtmlShared::BuildSimpleMarriageNameString(m_pConfiguration, m_lMarriageId, "%s and %s");

		pOutputStream->OutputSidebarFrontEnd("Family Group Sheet: " + strMarriageName, m_pConfiguration);

		// Top of Document Internal Link Tag
		pOutputStream->Output("<A name=InfoTop></A>");
		pOutputStream->OutputFormattingCRLF();

		// Husband Name
		CString strHusbandName;
		strHusbandName.Format("<A href=\"%s%s/%s%d.htm\">%s</a>", m_pConfiguration->getBasePath(), PERINFODIR, PERINFOFILENAME, husbandAccessor.getPersonIdAsLong(), husbandAccessor.getFullBasicName());
		strWork.Format("Husband:&nbsp;%s - ID#&nbsp;%d", strHusbandName, husbandAccessor.getPersonIdAsLong());
		pOutputStream->OutputSpan(E_PageBodyLargeText, strWork);
		pOutputStream->OutputBR();
		pOutputStream->OutputFormattingCRLF();
		// Husband Data
		pOutputStream->Output("<table width=\"450\" border=\"1\">");
		OutputBirth(pHusband, pOutputStream);
		OutputChristening(pHusband, pOutputStream);
		OutputMarriage(pMarriage, pOutputStream);
		OutputDeath(pHusband, pOutputStream);
		OutputBurial(pHusband, pOutputStream);
		OutputCemetery(pHusband, pOutputStream);
		OutputParents(&fourGenHusband, pOutputStream);

		pOutputStream->Output("</table>");
		pOutputStream->OutputBR();
		pOutputStream->OutputFormattingCRLF();

		// Wife Name
		CString strWifeName;
		strWifeName.Format("<A href=\"%s%s/%s%d.htm\">%s</a>", m_pConfiguration->getBasePath(), PERINFODIR, PERINFOFILENAME, wifeAccessor.getPersonIdAsLong(), wifeAccessor.getFullBasicName());
		strWork.Format("Wife:&nbsp;%s - ID#&nbsp;%d", strWifeName, wifeAccessor.getPersonIdAsLong());
		pOutputStream->OutputSpan(E_PageBodyLargeText, strWork);
		pOutputStream->OutputBR();
		pOutputStream->OutputFormattingCRLF();
		// Wife Data
		pOutputStream->Output("<table width=\"450\" border=\"1\">");
		OutputBirth(pWife, pOutputStream);
		OutputChristening(pWife, pOutputStream);
		OutputDeath(pWife, pOutputStream);
		OutputBurial(pWife, pOutputStream);
		OutputCemetery(pWife, pOutputStream);
		OutputParents(&fourGenWife, pOutputStream);
		pOutputStream->Output("</table>");
		pOutputStream->OutputBR();
		pOutputStream->OutputFormattingCRLF();

		// Children
		PERSONID childId;
		int iChildNumber = 1;
		if (pIndexMarriageToChildren->StartChildrenOfMarriageEnum(m_lMarriageId, &lCookie))
		{
			// Children Section Header
			pOutputStream->OutputSpan(E_PageBodyLargeText, "Children:");
			pOutputStream->OutputBR(2);
			pOutputStream->OutputFormattingCRLF();

			while (pIndexMarriageToChildren->NextChildrenOfMarriageEnum(m_lMarriageId, &lCookie, &childId))
			{
				Person *pChild = m_pPersonList->get(childId);
				if (pChild)
				{
					FormsPersonAccessor childAccessor(pChild, m_pConfiguration->getSuppressLiving());
					// "Child #1 FEMALE"
					strWork.Format("Child#&nbsp;%d&nbsp;%s",  iChildNumber, childAccessor.getGender());
					pOutputStream->OutputSpan(E_PageBodyBoldNormalText, strWork);
					pOutputStream->OutputBR();
					pOutputStream->OutputFormattingCRLF();
					// "SMITH, Jane - ID#5"
					CString strAsterisk = "";
					if (childAccessor.isInLine())
					{
						strAsterisk = "*";
					}
					pOutputStream->Output("<span class=\"pageBodyBoldNormalText\">");
					strWork.Format("%s%s/%s%d.htm", m_pConfiguration->getBasePath(), PERINFODIR, PERINFOFILENAME, childAccessor.getPersonIdAsLong());
					pOutputStream->OutputStartAnchor(strWork);
					pOutputStream->Output(strAsterisk + "&nbsp;" + childAccessor.getFullBasicName());
					pOutputStream->OutputEndAnchor();
					strWork.Format("&nbsp;-&nbsp;ID#&nbsp;%d", childId);
					pOutputStream->Output(strWork);
					pOutputStream->Output("</span>");
					pOutputStream->OutputBR();
					pOutputStream->OutputFormattingCRLF();
					// Start Table
					pOutputStream->Output("<table width=\"450\" border=\"1\">");
					// Child Data
					OutputBirth(pChild, pOutputStream);
					OutputChristening(pChild, pOutputStream);
					OutputDeath(pChild, pOutputStream);
					OutputBurial(pChild, pOutputStream);
					OutputCemetery(pChild, pOutputStream);
					// Child's Spouse Data
					OutputSpouse(pChild, pOutputStream);
					// End Table
					pOutputStream->Output("</table>");
					pOutputStream->OutputBR();
					pOutputStream->OutputFormattingCRLF();

					iChildNumber++;
				}
			}
		}


		pOutputStream->OutputSidebarBackEnd();
	}
	m_pStatus->UpdateStatus("Completed FGS: " + strFileName);

	if (pOutputStream)
	{
		delete pOutputStream;
	}
}

void FormsHtmlFgs::OutputMarriage(Marriage *pMarriage, FormsHtmlOutputStream *pOutputStream)
{
	FormsMarriageAccessor marriageAccessor(pMarriage, m_pConfiguration->getSuppressLiving(), m_pConfiguration->getFamily());
	CString strEvent = marriageAccessor.getDate();
	CString strPlace = marriageAccessor.getPlace();
	if ((0 != strEvent.GetLength()) || (0 != strPlace.GetLength()))
	{
		FormsTagFinderHtml dataPointRefEvent(m_pConfiguration);
		FormsTagFinderHtml dataPointRefPlace(m_pConfiguration);
		dataPointRefEvent.FindForMarriage(marriageAccessor.getMarriageIdAsLong(), "MarrD");
		dataPointRefPlace.FindForMarriage(marriageAccessor.getMarriageIdAsLong(), "MarrP");

		pOutputStream->Output("<tr><td>");
		pOutputStream->OutputSpan(E_PageBodyBoldNormalText, "Marr:&nbsp;");
		if (dataPointRefEvent.Found())
		{
			pOutputStream->OutputStartAnchor(dataPointRefEvent.GetHRef());
		}
		pOutputStream->OutputSpan(E_PageBodyNormalText, strEvent);
		if (dataPointRefEvent.Found())
		{
			pOutputStream->OutputEndAnchor();
		}
		pOutputStream->OutputBR();
		pOutputStream->OutputFormattingCRLF();
		pOutputStream->OutputSpan(E_PageBodyBoldNormalText, "Place:&nbsp;");
		if (dataPointRefPlace.Found())
		{
			pOutputStream->OutputStartAnchor(dataPointRefPlace.GetHRef());
		}
		pOutputStream->OutputSpan(E_PageBodyNormalText, strPlace);
		if (dataPointRefPlace.Found())
		{
			pOutputStream->OutputEndAnchor();
		}
		pOutputStream->OutputBR();
		pOutputStream->OutputFormattingCRLF();
		pOutputStream->Output("</td></tr>");
	}
}

void FormsHtmlFgs::OutputCemetery(Person *pPerson, FormsHtmlOutputStream *pOutputStream)
{
	FormsPersonAccessor personAccessor(pPerson, m_pConfiguration->getSuppressLiving());
	CString strEvent = personAccessor.getBurialCemetery();
	CString strPlace = personAccessor.getBurialPlotAddress();
	if ((0 != strEvent.GetLength()) || (0 != strPlace.GetLength()))
	{
		FormsTagFinderHtml dataPointRefEvent(m_pConfiguration);
		FormsTagFinderHtml dataPointRefPlace(m_pConfiguration);
		dataPointRefEvent.FindForPerson(pPerson->getPersonIdAsLong(), "CemeteryName");
		dataPointRefPlace.FindForPerson(pPerson->getPersonIdAsLong(), "CemeteryPlot");

		pOutputStream->Output("<tr><td>");
		pOutputStream->OutputSpan(E_PageBodyBoldNormalText, "Cemetery:&nbsp;");
		if (dataPointRefEvent.Found())
		{
			pOutputStream->OutputStartAnchor(dataPointRefEvent.GetHRef());
		}
		pOutputStream->OutputSpan(E_PageBodyNormalText, strEvent);
		if (dataPointRefEvent.Found())
		{
			pOutputStream->OutputEndAnchor();
		}
		pOutputStream->OutputBR();
		pOutputStream->OutputFormattingCRLF();
		pOutputStream->OutputSpan(E_PageBodyBoldNormalText, "Plot:&nbsp;");
		if (dataPointRefPlace.Found())
		{
			pOutputStream->OutputStartAnchor(dataPointRefPlace.GetHRef());
		}
		pOutputStream->OutputSpan(E_PageBodyNormalText, strPlace);
		if (dataPointRefPlace.Found())
		{
			pOutputStream->OutputEndAnchor();
		}
		pOutputStream->OutputBR();
		pOutputStream->OutputFormattingCRLF();
		pOutputStream->Output("</td></tr>");
	}
}

void FormsHtmlFgs::OutputParents(FourGenerations *pFourGen, FormsHtmlOutputStream *pOutputStream)
{
	Person *pPerson = pFourGen->getPerson();
	Person *pFather = pFourGen->getFather();
	Person *pMother = pFourGen->getMother();
	CString strWork;

	if (pPerson && pFather && pMother)
	{
		FormsPersonAccessor personAccessor(pPerson, m_pConfiguration->getSuppressLiving());
		FormsPersonAccessor fatherAccessor(pFather, m_pConfiguration->getSuppressLiving());
		FormsPersonAccessor motherAccessor(pMother, m_pConfiguration->getSuppressLiving());
		CString strFatherName = fatherAccessor.getFullBasicName();
		CString strMotherName = motherAccessor.getFullBasicName();
		if ((0 != strFatherName.GetLength()) || (0 != strMotherName.GetLength()))
		{
			FormsTagFinderHtml dataPointRefFather(m_pConfiguration);
			FormsTagFinderHtml dataPointRefMother(m_pConfiguration);
			dataPointRefFather.FindForPerson(personAccessor.getPersonIdAsLong(), "LinkCToFather");
			dataPointRefMother.FindForPerson(personAccessor.getPersonIdAsLong(), "LinkCToMother");

			// Output Father
			strWork.Format("%s%s/%s%d.htm", m_pConfiguration->getBasePath(), PERINFODIR, PERINFOFILENAME, fatherAccessor.getPersonIdAsLong());
			pOutputStream->Output("<tr><td>");
			pOutputStream->OutputSpan(E_PageBodyBoldNormalText, "Father:&nbsp;");
			pOutputStream->OutputStartAnchor(strWork);
			pOutputStream->OutputSpan(E_PageBodyNormalText, strFatherName);
			pOutputStream->OutputEndAnchor();
			// Show "Prove Link"
			if (dataPointRefFather.Found())
			{
				pOutputStream->Output("&nbsp;&nbsp;");
				pOutputStream->OutputStartAnchor(dataPointRefFather.GetHRef());
				pOutputStream->OutputSpan(E_PageBodyNormalText, "[Prove Link]");
				pOutputStream->OutputEndAnchor();
			}
			// Show "[Family]" Link
			pOutputStream->Output("&nbsp;&nbsp;");
			strWork.Format("%s%s/%s%d.htm", m_pConfiguration->getBasePath(), FGSDIR, FGSFILENAME, pFourGen->getFatherMarriageId()); 
			pOutputStream->OutputStartAnchor(strWork);
			pOutputStream->OutputSpan(E_PageBodyNormalText, "[Family]");
			pOutputStream->OutputEndAnchor();

			pOutputStream->OutputBR();
			pOutputStream->OutputFormattingCRLF();

			// Output Mother
			strWork.Format("%s%s/%s%d.htm", m_pConfiguration->getBasePath(), PERINFODIR, PERINFOFILENAME ,motherAccessor.getPersonIdAsLong());
			pOutputStream->OutputSpan(E_PageBodyBoldNormalText, "Mother:&nbsp;");
			pOutputStream->OutputStartAnchor(strWork);
			pOutputStream->OutputSpan(E_PageBodyNormalText, strMotherName);
			pOutputStream->OutputEndAnchor();
			// Show "Prove Link"
			if (dataPointRefMother.Found())
			{
				pOutputStream->Output("&nbsp;&nbsp;");
				pOutputStream->OutputStartAnchor(dataPointRefMother.GetHRef());
				pOutputStream->OutputSpan(E_PageBodyNormalText, "[Prove Link]");
				pOutputStream->OutputEndAnchor();
			}
			pOutputStream->OutputBR();
			pOutputStream->OutputFormattingCRLF();
			pOutputStream->Output("</td></tr>");
		}
	}
}

void FormsHtmlFgs::OutputSpouse(Person *pPerson, FormsHtmlOutputStream *pOutputStream)
{
	MARRIAGEID idMarriage = MARRIAGEID_INVALID;
	MARRIAGEID idMarriageCurrent = MARRIAGEID_INVALID;
	long lCookie;
	CString strWork;

	IndexPersonToMarriages *pIndexPersonToMarriages = m_pIndex->GetIndexPersonToMarriages();
	IndexMarriageToSpouses *pIndexMarriageToSpouses = m_pIndex->GetIndexMarriageToSpouses();

	// Find the marriage that should be used
	// 1 - In-Line Marriage
	// 2 - Any other marriage
	if (pIndexPersonToMarriages->StartMarriagesOfPersonEnum(pPerson->getPersonIdAsLong(), &lCookie))
	{
		while (pIndexPersonToMarriages->NextMarriagesOfPersonEnum(pPerson->getPersonIdAsLong(), &lCookie, &idMarriage))
		{
			if (MARRIAGEID_INVALID != idMarriage)
			{
				Marriage *pMarriage = m_pMarriageList->get(idMarriage);
				if (pMarriage)
				{
					if (pMarriage->isInLine())
					{	// As soon as an In-Line marriage is found, done!
						idMarriageCurrent = idMarriage;
						break;
					}
					else if (MARRIAGEID_INVALID == idMarriageCurrent)
					{
						idMarriageCurrent = idMarriage;
					}
				}
			}
		}
	}

	if (MARRIAGEID_INVALID != idMarriageCurrent)
	{
		PERSONID idHusband, idWife, idPerson;
		if (pIndexMarriageToSpouses->FindSpousesGivenMarriageId(idMarriageCurrent, &idHusband, &idWife))
		{
			idPerson = idHusband;
			if (idHusband == pPerson->getPersonIdAsLong())
			{
				idPerson = idWife;
			}

			Person *pSpouse = m_pPersonList->get(idPerson);
			if (pSpouse)
			{
				FormsPersonAccessor spouseAccessor(pSpouse, m_pConfiguration->getSuppressLiving());
				CString strSpouseName = spouseAccessor.getFullBasicName();
				if (0 != strSpouseName.GetLength())
				{
					FormsTagFinderHtml dataPointRefSpouse(m_pConfiguration);
					dataPointRefSpouse.FindForMarriage(idMarriageCurrent, "LinkMarriage");

					pOutputStream->Output("<tr><td>");
					pOutputStream->OutputSpan(E_PageBodyBoldNormalText, "Spouse:&nbsp;");
					// Show Spouse Name
					strWork.Format("%s%s/%s%d.htm", m_pConfiguration->getBasePath(), PERINFODIR, PERINFOFILENAME, pSpouse->getPersonIdAsLong());
					pOutputStream->OutputStartAnchor(strWork);
					pOutputStream->OutputSpan(E_PageBodyNormalText, strSpouseName);
					pOutputStream->OutputEndAnchor();
					// Show "[Prove Link] Link"
					if (dataPointRefSpouse.Found())
					{
						pOutputStream->Output("&nbsp;&nbsp;");
						pOutputStream->OutputStartAnchor(dataPointRefSpouse.GetHRef());
						pOutputStream->OutputSpan(E_PageBodyNormalText, "[Prove Link]");
						pOutputStream->OutputEndAnchor();
					}
					// Show "[Family]" Link
					pOutputStream->Output("&nbsp;&nbsp;");
					strWork.Format("%sfgs/%s%d.htm", m_pConfiguration->getBasePath(), FGSFILENAME, idMarriageCurrent); 
					pOutputStream->OutputStartAnchor(strWork);
					pOutputStream->OutputSpan(E_PageBodyNormalText, "[Family]");
					pOutputStream->OutputEndAnchor();

					pOutputStream->OutputBR();
					pOutputStream->OutputFormattingCRLF();
					pOutputStream->Output("</td></tr>");
				}
			}
		}
	}
}




*/