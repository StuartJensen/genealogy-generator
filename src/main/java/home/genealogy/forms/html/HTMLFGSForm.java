package home.genealogy.forms.html;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import home.genealogy.GenealogyContext;
import home.genealogy.indexes.IndexMarriageToChildren;
import home.genealogy.indexes.IndexMarriageToSpouses;
import home.genealogy.indexes.IndexMarriagesToReferences;
import home.genealogy.indexes.IndexPersonToMarriages;
import home.genealogy.indexes.IndexPersonToParentMarriage;
import home.genealogy.indexes.IndexPersonsToReferences;
import home.genealogy.indexes.TaggedContainerDescriptor;
import home.genealogy.schema.all.Marriage;
import home.genealogy.schema.all.Parents;
import home.genealogy.schema.all.Person;
import home.genealogy.schema.all.helpers.MarriageHelper;
import home.genealogy.schema.all.helpers.MarriageIdHelper;
import home.genealogy.schema.all.helpers.PersonHelper;
import home.genealogy.schema.all.helpers.PersonIdHelper;

public class HTMLFGSForm
{
	public static final String FGS_FILE_SYSTEM_SUBDIRECTORY = "fgs";
	
	private GenealogyContext m_context;
	  
	public HTMLFGSForm(GenealogyContext context)
	{
		m_context = context;
	}
	
	public void create()
		throws Exception
	{
		// Get the base output file system path and make sure it 
		// ends with a slash
		String strOutputPath = m_context.getFamily().getOutputPathHTML();
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
		IndexPersonToParentMarriage idxPerToParentMar = new IndexPersonToParentMarriage(m_context.getFamily(), m_context.getPersonList());
		IndexPersonToMarriages idxPerToMar = new IndexPersonToMarriages(m_context.getFamily(), m_context.getMarriageList());
		IndexMarriageToSpouses idxMarToSpouses = new IndexMarriageToSpouses(m_context.getFamily(), m_context.getMarriageList());
		IndexPersonsToReferences idxPerToRef = new IndexPersonsToReferences(m_context.getFamily(), m_context.getPersonList(), m_context.getReferenceList());
		IndexMarriagesToReferences idxMarToRef = new IndexMarriagesToReferences(m_context.getFamily(), m_context.getPersonList(), m_context.getMarriageList(), m_context.getReferenceList());
		IndexMarriageToChildren idxMarToChildren = new IndexMarriageToChildren(m_context.getFamily(), m_context.getCommandLineParameters(), m_context.getPersonList());
		
		Iterator<Marriage> iter = m_context.getMarriageList().getMarriages();
		while (iter.hasNext())
		{
			Marriage thisMarriage = iter.next();
			createFGS(idxPerToParentMar,
 					  idxPerToMar, idxMarToSpouses, idxPerToRef, idxMarToRef,
 					  idxMarToChildren, strOutputPath, thisMarriage);
		}
		m_context.output("Completed Generating All FGS Files\n");
	}
	
	private void createFGS(
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
		m_context.output("Generating FGS File: " + strFileName + "\n");
		HTMLFormOutput output = new HTMLFormOutput(strFileName);

		Person husband = m_context.getPersonList().get(MarriageHelper.getHusbandPersonId(marriage));
		Person wife = m_context.getPersonList().get(MarriageHelper.getWifePersonId(marriage));
		if ((null != husband) && (null != wife))
		{
			PersonHelper husbandHelper = new PersonHelper(husband, m_context.getSuppressLiving(), m_context.getPlaceList());
			PersonHelper wifeHelper = new PersonHelper(wife, m_context.getSuppressLiving(), m_context.getPlaceList());
			MarriageHelper marriageHelper = new MarriageHelper(marriage, husbandHelper, wifeHelper, m_context.getSuppressLiving(), m_context.getPlaceList());
			String strMarriageTitle = HTMLShared.buildSimpleMarriageNameString(m_context, idxMarToSpouses, iMarriageId, "{0} and {2}");
			output.outputSidebarFrontEnd("Family Group Sheet: " + strMarriageTitle, m_context.getFamily(), m_context.getPersonList(), m_context.getMarriageList());
			
			// Top of Document Internal Link Tag
			output.output("<A name=InfoTop></A>");
			output.outputCRLF();

			// Husband Data
			String strUrl = "<A href=\"";
			strUrl += m_context.getFamily().getUrlPrefix() + HTMLShared.PERINFODIR + "/" + HTMLShared.PERINFOFILENAME +  husbandHelper.getPersonId() + ".htm";
			strUrl += "\">";
			strUrl += husbandHelper.getPersonName();
			strUrl += "</a>";
			String strHusbandNameLine = "Husband:&nbsp;" + strUrl + " - ID#&nbsp;" + husbandHelper.getPersonId();
			output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyLargeText, strHusbandNameLine);
			output.outputBR();
			output.outputCRLF();
			// Husband Data
			output.output("<table width=\"500\" border=\"1\">");
			outputBirth(husbandHelper, idxPerToRef, output);
			outputChristening(husbandHelper, idxPerToRef, output);
			outputMarriage(husbandHelper, wifeHelper, idxMarToRef, iMarriageId, output);
			outputDeath(husbandHelper, idxPerToRef, output);
			outputBurial(husbandHelper, idxPerToRef, output);
			outputCemetery(husbandHelper, idxPerToRef, output);
			outputParents(husbandHelper, idxPerToRef, output);
			output.output("</table>");
			output.outputBR();
			output.outputCRLF();
			
			// Wife Data
			strUrl = "<A href=\"";
			strUrl += m_context.getFamily().getUrlPrefix() + HTMLShared.PERINFODIR + "/" + HTMLShared.PERINFOFILENAME +  wifeHelper.getPersonId() + ".htm";
			strUrl += "\">";
			strUrl += wifeHelper.getPersonName();
			strUrl += "</a>";
			String strWifeNameLine = "Wife:&nbsp;" + strUrl + " - ID#&nbsp;" + wifeHelper.getPersonId();
			output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyLargeText, strWifeNameLine);
			output.outputBR();
			output.outputCRLF();
			// Husband Data
			output.output("<table width=\"500\" border=\"1\">");
			outputBirth(wifeHelper, idxPerToRef, output);
			outputChristening(wifeHelper, idxPerToRef, output);
			outputDeath(wifeHelper, idxPerToRef, output);
			outputBurial(wifeHelper, idxPerToRef, output);
			outputCemetery(wifeHelper, idxPerToRef, output);
			outputParents(wifeHelper, idxPerToRef, output);
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
						Person child = m_context.getPersonList().get(iChildId);
						if (null != child)
						{
							PersonHelper childHelper = new PersonHelper(child, m_context.getSuppressLiving(), m_context.getPlaceList());
							// "Child #1 FEMALE"
							String strGenderLine = "Child#&nbsp;" + iChildNumber + "&nbsp;" + PersonHelper.getGender(child);
							output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyBoldNormalText, strGenderLine);
							output.outputBR();
							output.outputCRLF();
							
							// "SMITH, Jane - ID#5"
							output.output("<span class=\"pageBodyBoldNormalText\">");
							strUrl = m_context.getFamily().getUrlPrefix() + HTMLShared.PERINFODIR + "/" + HTMLShared.PERINFOFILENAME +  iChildId + ".htm";
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
							outputBirth(childHelper, idxPerToRef, output);
							outputChristening(childHelper, idxPerToRef, output);
							outputDeath(childHelper, idxPerToRef, output);
							outputBurial(childHelper, idxPerToRef, output);
							outputCemetery(childHelper, idxPerToRef, output);
							// Child's Spouse Data
							outputSpouse(child, idxPerToMar, idxMarToSpouses, output);
							// End Table
							output.output("</table>");
							output.outputBR();
							output.outputCRLF();
							
							iChildNumber++;
						}
					}
				}
			}
		}
		
		output.outputSidebarBackEnd();
		
		output.commit();
		output = null;
	}
	
	private void outputParents(PersonHelper personHelper,
								IndexPersonsToReferences idxPerToRef,
								HTMLFormOutput output)
	{
		Person father = null;
		Person mother = null;
		Parents parents = personHelper.getPreferredBloodThenAnyParents();
		int iParentMarriageId = MarriageIdHelper.MARRIAGEID_INVALID;
		if (null != parents)
		{
			iParentMarriageId = parents.getMarriageId();
			Marriage marriage = m_context.getMarriageList().get(iParentMarriageId);
			if (null != marriage)
			{
				int iFatherId = MarriageHelper.getHusbandPersonId(marriage);
				int iMotherId = MarriageHelper.getWifePersonId(marriage);
				if (PersonIdHelper.PERSONID_INVALID != iFatherId)
				{
					father = m_context.getPersonList().get(iFatherId);
				}
				if (PersonIdHelper.PERSONID_INVALID != iMotherId)
				{
					mother = m_context.getPersonList().get(iMotherId);
				}
			}
		}
		
		if ((null != father) && (null != mother))
		{
			PersonHelper fatherHelper = new PersonHelper(father, m_context.getSuppressLiving(), m_context.getPlaceList());
			PersonHelper motherHelper = new PersonHelper(mother, m_context.getSuppressLiving(), m_context.getPlaceList());
			String strFatherName = fatherHelper.getPersonName();
			String strMotherName = motherHelper.getPersonName();
			if ((0 != strFatherName.length()) || (0 != strMotherName.length()))
			{
				ArrayList<TaggedContainerDescriptor> alLinkCToFatherRefs = idxPerToRef.getReferencesForPerson(fatherHelper.getPersonId(), "LinkCToFather");
				ArrayList<TaggedContainerDescriptor> alLinkCToMotherRefs = idxPerToRef.getReferencesForPerson(motherHelper.getPersonId(), "LinkCToMother");
				// Output Father
				String strUrl = m_context.getFamily().getUrlPrefix() + HTMLShared.PERINFODIR + "/" + HTMLShared.PERINFOFILENAME +  fatherHelper.getPersonId() + ".htm";
				output.output("<tr><td>");
				output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyBoldNormalText, "Father:&nbsp;");
				output.outputStartAnchor(strUrl);
				output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyNormalText, strFatherName);
				output.outputEndAnchor();
				if (0 != alLinkCToFatherRefs.size())
				{
					output.output("&nbsp;&nbsp;");
					String strHRef = HTMLShared.getReferenceHRef(m_context.getFamily(), alLinkCToFatherRefs.get(0));
					output.outputStartAnchor(strHRef);
					output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyNormalText, "[Prove Link]");
					output.outputEndAnchor();
				}
				// Show "[Family]" Link
				strUrl = m_context.getFamily().getUrlPrefix() + HTMLShared.FGSDIR + "/" + HTMLShared.FGSFILENAME +  iParentMarriageId + ".htm";
				output.output("&nbsp;&nbsp;");
				output.outputStartAnchor(strUrl);
				output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyNormalText, "[Family]");
				output.outputEndAnchor();
				output.outputBR();
				output.outputCRLF();
				// Output Mother
				strUrl = m_context.getFamily().getUrlPrefix() + HTMLShared.PERINFODIR + "/" + HTMLShared.PERINFOFILENAME +  motherHelper.getPersonId() + ".htm";
				output.output("<tr><td>");
				output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyBoldNormalText, "Mother:&nbsp;");
				output.outputStartAnchor(strUrl);
				output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyNormalText, strMotherName);
				output.outputEndAnchor();
				if (0 != alLinkCToMotherRefs.size())
				{
					output.output("&nbsp;&nbsp;");
					String strHRef = HTMLShared.getReferenceHRef(m_context.getFamily(), alLinkCToMotherRefs.get(0));
					output.outputStartAnchor(strHRef);
					output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyNormalText, "[Prove Link]");
					output.outputEndAnchor();
				}
			}
		}	
		
	}
	
	private void outputBirth(PersonHelper personHelper, IndexPersonsToReferences idxPerToRef, HTMLFormOutput output)
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
				String strHRef = HTMLShared.getReferenceHRef(m_context.getFamily(), alBornDRefs.get(0));
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
				String strHRef = HTMLShared.getReferenceHRef(m_context.getFamily(), alBornPRefs.get(0));

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
	
	private void outputChristening(PersonHelper personHelper, IndexPersonsToReferences idxPerToRef, HTMLFormOutput output)
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
				String strHRef = HTMLShared.getReferenceHRef(m_context.getFamily(), alChrDRefs.get(0));
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
				String strHRef = HTMLShared.getReferenceHRef(m_context.getFamily(), alChrPRefs.get(0));

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
	
	
	private void outputDeath(PersonHelper personHelper, IndexPersonsToReferences idxPerToRef, HTMLFormOutput output)
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
				String strHRef = HTMLShared.getReferenceHRef(m_context.getFamily(), alDiedDRefs.get(0));
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
				String strHRef = HTMLShared.getReferenceHRef(m_context.getFamily(), alDiedPRefs.get(0));

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
	
	private void outputBurial(PersonHelper personHelper, IndexPersonsToReferences idxPerToRef, HTMLFormOutput output)
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
				String strHRef = HTMLShared.getReferenceHRef(m_context.getFamily(), alDiedDRefs.get(0));
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
				String strHRef = HTMLShared.getReferenceHRef(m_context.getFamily(), alDiedPRefs.get(0));

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
	
	private void outputCemetery(PersonHelper personHelper, IndexPersonsToReferences idxPerToRef, HTMLFormOutput output)
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
				String strHRef = HTMLShared.getReferenceHRef(m_context.getFamily(), alCemeteryNameRefs.get(0));
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
				String strHRef = HTMLShared.getReferenceHRef(m_context.getFamily(), alCemeteryPlotRefs.get(0));
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
	
	
	private void outputMarriage(PersonHelper husbandHelper, PersonHelper wifeHelper,
							    IndexMarriagesToReferences idxMarToRef, int iMarriageId,
								HTMLFormOutput output)
	{
		Marriage marriage = m_context.getMarriageList().get(iMarriageId);
		if (null != marriage)
		{
			MarriageHelper mh = new MarriageHelper(marriage, husbandHelper, wifeHelper, m_context.getSuppressLiving(), m_context.getPlaceList());
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
					String strHRef = HTMLShared.getReferenceHRef(m_context.getFamily(), alDateRefs.get(0));
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
					String strHRef = HTMLShared.getReferenceHRef(m_context.getFamily(), alPlaceRefs.get(0));
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
	
	private void outputSpouse(Person person, IndexPersonToMarriages idxPerToMar, IndexMarriageToSpouses idxMarToSpouses, HTMLFormOutput output)
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
				marriage = m_context.getMarriageList().get(iMarriageId);
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
					spouse = m_context.getPersonList().get(iWifeId);
				}
				else if (iPersonId == iWifeId)
				{
					spouse = m_context.getPersonList().get(iHusbandId);
				}
				// Did we find the spouse?
				if (null != spouse)
				{	// Yes
					PersonHelper spouseHelper = new PersonHelper(spouse, m_context.getSuppressLiving(), m_context.getPlaceList());
					String strSpouseName = spouseHelper.getPersonName();
					if ((null != strSpouseName) && (0 != strSpouseName.length()))
					{
						output.output("<tr><td>");
						output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyBoldNormalText, "Spouse:&nbsp;");
						// Show Spouse Name
						String strUrl = m_context.getFamily().getUrlPrefix() + HTMLShared.PERINFODIR + "/" + HTMLShared.PERINFOFILENAME +  spouseHelper.getPersonId() + ".htm";
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
						strUrl = m_context.getFamily().getUrlPrefix() + HTMLShared.FGSDIR + "/" + HTMLShared.FGSFILENAME +  iMarriageId + ".htm";
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

