package home.genealogy.forms.html;

import home.genealogy.configuration.CFGFamily;
import home.genealogy.indexes.IndexMarriageToSpouses;
import home.genealogy.lists.MarriageList;
import home.genealogy.lists.PersonList;
import home.genealogy.lists.PhotoList;
import home.genealogy.lists.ReferenceList;
import home.genealogy.output.IOutputStream;
import home.genealogy.schema.all.Entry;
import home.genealogy.schema.all.Paragraph;
import home.genealogy.schema.all.Reference;
import home.genealogy.schema.all.extensions.ReferenceSortableByPlaceName;
import home.genealogy.schema.all.helpers.EntryHelper;
import home.genealogy.schema.all.helpers.ReferenceHelper;
import home.genealogy.util.CommandLineParameterList;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class HTMLReferenceListForm
{
	public static final String REFERENCE_INDEX_FILE_SYSTEM_SUBDIRECTORY = "index";
	
	private CFGFamily m_family;
	private PersonList m_personList;
	private MarriageList m_marriageList;
	private ReferenceList m_referenceList;
	private PhotoList m_photoList;
	private IndexMarriageToSpouses m_indexMarrToSpouses;
	private boolean m_bSuppressLiving;
	private CommandLineParameterList m_listCLP;
	private IOutputStream m_outputStream;
	  
	public HTMLReferenceListForm(CFGFamily family,
							     PersonList personList,
							     MarriageList marriageList,
							     ReferenceList referenceList,
							     PhotoList photoList,
							     IndexMarriageToSpouses indexMarrToSpouses,
							     boolean bSuppressLiving,
							     CommandLineParameterList listCLP,
							     IOutputStream outputStream)
	{
		m_family = family;
		m_personList = personList;
		m_marriageList = marriageList;
		m_referenceList =  referenceList;
		m_photoList = photoList;
		m_indexMarrToSpouses = indexMarrToSpouses;
		m_bSuppressLiving = bSuppressLiving;
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
		// Make sure the "index" subdirectory exists under
		// the base output file system path
		File fSubdirectory = new File(strOutputPath + REFERENCE_INDEX_FILE_SYSTEM_SUBDIRECTORY);
		if (!fSubdirectory.exists())
		{
			if (!fSubdirectory.mkdirs())
			{
				throw new Exception("Error creating sub-directory tree for reference list file!");
			}
		}

		String strFileName = strOutputPath + REFERENCE_INDEX_FILE_SYSTEM_SUBDIRECTORY + "\\" + HTMLShared.REFINDEXFILENAME + ".htm";
		m_outputStream.output("Generating Reference List: " + strFileName+ "\n");
		HTMLFormOutput output = new HTMLFormOutput(strFileName);

		// Start document creation
		String strTitle = "Documentation Index";
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
		
		// Place all references into sortable objects and sort them by place name
		ArrayList<ReferenceSortableByPlaceName> alReferences = new ArrayList<ReferenceSortableByPlaceName>();
		Iterator<Reference> iter = m_referenceList.getReferences();
		while (iter.hasNext())
		{
			Reference reference = iter.next();
			alReferences.add(new ReferenceSortableByPlaceName(reference));
		}
		Collections.sort(alReferences);
		String strCurrentPlaceName = "";
		for (int i=0; i<alReferences.size(); i++)
		{
			Reference reference = alReferences.get(i).getReference();
			int iReferenceId = ReferenceHelper.getReferenceId(reference);
			String strThisPlaceName = ReferenceHelper.getCitationPlace(reference);
			if (!strThisPlaceName.equals(strCurrentPlaceName))
			{
				output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodySmallHeader, strThisPlaceName);
				output.outputBR(2);
				output.outputCRLF();
				strCurrentPlaceName = strThisPlaceName;
			}
			// Document Title
			String strUrl = m_family.getUrlPrefix() + HTMLShared.REFERENCEDIR + "/" + HTMLShared.REFERENCEFILENAME + ReferenceHelper.getReferenceId(reference) + ".htm";
			output.outputStandardBracketedLink(strUrl, "View Document");
			output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodySmallText, ReferenceHelper.getTitle(reference));
			output.outputBR();
			output.outputCRLF();

			// Document Number
			String strWork = "Document Number: " + ReferenceHelper.getReferenceId(reference);
			output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodySmallText, strWork);
			output.outputBR();
			output.outputCRLF();
			
			// Entry Description List
			int iCount = ReferenceHelper.getEntryCount(reference);
			for (int r=0; r<iCount; r++)
			{
				Entry entry = ReferenceHelper.getEntry(reference, r);
				int iReferenceEntryId = EntryHelper.getEntryId(entry);
				strUrl = m_family.getUrlPrefix() + HTMLShared.REFERENCEDIR + "/" + HTMLShared.REFERENCEFILENAME + iReferenceId + ".htm#REF" + iReferenceId + "ENT" + iReferenceEntryId;
				output.outputStandardBracketedLink(strUrl, "View Entry");
				int iTitleParagraphCount = EntryHelper.getTitleParagraphCount(entry);
				for (int t=0; t<iTitleParagraphCount; t++)
				{
					Paragraph paragraph = EntryHelper.getTitleParagraph(entry, t);					
					String strParagraph = HTMLShared.buildParagraphString(m_family, m_listCLP,
							  paragraph, m_personList, m_marriageList,
                            m_referenceList, m_photoList,
                            m_indexMarrToSpouses,
                            true,
                            true,
                            m_bSuppressLiving,
                            null,
                            iReferenceId,
                            iReferenceEntryId);
					output.output(strParagraph);
					output.outputCRLF();
				}
			}
			output.outputBR();
		}
		output.outputSidebarBackEnd();
		output.commit();
		output = null;
		m_outputStream.output("Completed Generating All Reference List File\n");	
	}
}



/*
FormsHtmlReferenceList::FormsHtmlReferenceList(Family *pFamily, FormsHtmlConfiguration *pConfiguration)
{
	m_pFamily = pFamily;
	m_pConfiguration = pConfiguration;
	m_pPersonList = m_pFamily->getPersonList();
	m_pMarriageList = m_pFamily->getMarriageList();
	m_pReferenceList = m_pFamily->getReferenceList();
	m_pPhotoList = m_pFamily->getPhotoList();
	m_pIndex = m_pFamily->getFamilyIndex();
	m_pStatus = pConfiguration->getStatus();
}


void FormsHtmlReferenceList::Create()
{
	int i;
	CString strWork, strHRef;
	CString strFileName;
	strFileName.Format("%sindex\\%s.htm", m_pConfiguration->getDestinationPath(), REFINDEXFILENAME); 
	m_pStatus->Log("Generating Reference List: " + strFileName, LogLevel_Finer);
	m_pStatus->UpdateStatus("Generating Reference List: " + strFileName);
	FormsHtmlOutputStream *pOutputStream = new FormsHtmlOutputStream(strFileName, m_pStatus);

	CString strPageHeader = "Documentation Index (Sorted by Place Name)";
	if (m_pConfiguration->getSortReferenceBy() == SORTREF_BY_ID)
	{
		strPageHeader = "Documentation Index (Sorted by Document Id)";
	}

	pOutputStream->OutputSidebarFrontEnd("Documentation Index", m_pConfiguration);

	// Top of Document Internal Link Tag
	pOutputStream->Output("<A name=InfoTop></A>");
	pOutputStream->OutputFormattingCRLF();

	// Top of Page Title
	pOutputStream->OutputSpan(E_PageTopHeader, strPageHeader);
	pOutputStream->OutputBR();
	pOutputStream->OutputFormattingCRLF();

	pOutputStream->Output("<hr>");
	pOutputStream->OutputBR();

	ReferenceList *pReferenceList = m_pFamily->getReferenceList();

	if (m_pConfiguration->getSortReferenceBy() == SORTREF_BY_ID)
	{
		for (i=1; i<10000; i++)
		{
			Reference *pReference = pReferenceList->get(i);
			if (pReference)
			{
				ReferenceCitation *pCitation = pReference->getReferenceCitation();
				ReferenceSource *pSource = pCitation->getReferenceSource();
				ReferenceAuthor *pAuthor = pSource->getReferenceAuthor();

				// Document Title
				strHRef.Format("%sreferences/%s%d.htm", m_pConfiguration->getBasePath(), REFERENCEFILENAME, pReference->getReferenceIdAsLong());
				pOutputStream->OutputStandardBracketedLink(strHRef, "View Document");
				pOutputStream->OutputSpan(E_PageBodySmallText, pReference->getTitle());
				pOutputStream->OutputBR();
				pOutputStream->OutputFormattingCRLF();

				// Document Number
				strWork.Format("Document Number: %d", pReference->getReferenceIdAsLong());
				pOutputStream->OutputSpan(E_PageBodySmallText, strWork);
				pOutputStream->OutputBR();
				pOutputStream->OutputFormattingCRLF();

				// Entry Description List
				int iEntryCounted = 0;
				int iEntryCount = pReference->GetEntryCount();
				for (int iEntry=1; iEntry<=iEntryCount; iEntry++)
				{
					ReferenceEntry *pReferenceEntry = pReference->GetEntryWithId(iEntry);
					if (!pReferenceEntry)
					{
						break;
					}
					iEntryCounted++;
					// Entry Title and Description
					ReferenceEntryTitle *pReferenceEntryTitle = pReferenceEntry->GetReferenceEntryTitle();
					FormsHtmlParagraphFormat paragraphFormat("pageBodySmallText");
					strHRef.Format("%sreferences/%s%d.htm#REF%dENT%d", m_pConfiguration->getBasePath(), REFERENCEFILENAME, pReference->getReferenceIdAsLong(), pReference->getReferenceIdAsLong(), pReferenceEntry->GetReferenceEntryIdAsLong());
					pOutputStream->OutputStandardBracketedLink(strHRef, "View Entry");
					for(WORD nth=1; nth <= pReferenceEntryTitle->getParagraphCount(); nth++)
					{
						Paragraph *pParagraph = pReferenceEntryTitle->getParagraph(nth);
						if (pParagraph)
						{
							pOutputStream->OutputParagraph(m_pConfiguration, pParagraph, true, true, &paragraphFormat);
						}
					}
					pOutputStream->OutputFormattingCRLF();
				}
				pOutputStream->OutputBR();

				// If we saw a different number of entries than we should have seen,
				// log the error and continue.
				if (iEntryCounted != iEntryCount)
				{
					strWork.Format("Misnumbered reference entries. Total entries: %d, Counted: %d!", iEntryCount, iEntryCounted);
					m_pStatus->Log(strWork, LogLevel_Severe);
				}
				MessagePump::PumpDaMessages();
			}
		}
	}
	else if (m_pConfiguration->getSortReferenceBy() == SORTREF_BY_PLACENAME)
	{
		// Output References sorted by place name
		CUIntArray arRefIds;
		CString strCurrentPlaceName = "";
		long lCurrentIndex = 0;


		for (i=1; i<10000; i++)
		{
			Reference *pReference = pReferenceList->get(i);
			if (pReference)
			{
				arRefIds.Add(pReference->getReferenceIdAsLong());
			}
		}

		if (0 != arRefIds.GetSize())
		{
			FormsHtmlShared::SortReferenceArrayByLocation(m_pConfiguration, &arRefIds, arRefIds.GetSize());

			while (lCurrentIndex < arRefIds.GetSize())
			{
				REFERENCEID lReferenceId = arRefIds.GetAt(lCurrentIndex);
				Reference *pReference = pReferenceList->get(lReferenceId);
				if (pReference)
				{

					ReferenceCitation *pCitation = pReference->getReferenceCitation();
					ReferenceSource *pSource = pCitation->getReferenceSource();
					ReferenceAuthor *pAuthor = pSource->getReferenceAuthor();

					CString strThisPlaceName = pCitation->getPlace()->getPlace();
					if (0 != strThisPlaceName.Compare(strCurrentPlaceName))
					{
						pOutputStream->OutputSpan(E_PageBodySmallHeader, strThisPlaceName);
						pOutputStream->OutputBR(2);
						pOutputStream->OutputFormattingCRLF();
						strCurrentPlaceName = strThisPlaceName;;
					}

					// Document Title
					strHRef.Format("%sreferences/%s%d.htm", m_pConfiguration->getBasePath(), REFERENCEFILENAME, pReference->getReferenceIdAsLong());
					pOutputStream->OutputStandardBracketedLink(strHRef, "View Document");
					pOutputStream->OutputSpan(E_PageBodySmallText, pReference->getTitle());
					pOutputStream->OutputBR();
					pOutputStream->OutputFormattingCRLF();

					// Document Number
					strWork.Format("Document Number: %d", pReference->getReferenceIdAsLong());
					pOutputStream->OutputSpan(E_PageBodySmallText, strWork);
					pOutputStream->OutputBR();
					pOutputStream->OutputFormattingCRLF();

					// Entry Description List
					int iEntryCounted = 0;
					int iEntryCount = pReference->GetEntryCount();
					for (int iEntry=1; iEntry<=iEntryCount; iEntry++)
					{
						ReferenceEntry *pReferenceEntry = pReference->GetEntryWithId(iEntry);
						if (!pReferenceEntry)
						{
							break;
						}
						iEntryCounted++;
						// Entry Title and Description
						ReferenceEntryTitle *pReferenceEntryTitle = pReferenceEntry->GetReferenceEntryTitle();
						FormsHtmlParagraphFormat paragraphFormat("pageBodySmallText");
						strHRef.Format("%sreferences/%s%d.htm#REF%dENT%d", m_pConfiguration->getBasePath(), REFERENCEFILENAME, pReference->getReferenceIdAsLong(), pReference->getReferenceIdAsLong(), pReferenceEntry->GetReferenceEntryIdAsLong());
						pOutputStream->OutputStandardBracketedLink(strHRef, "View Entry");
						for(WORD nth=1; nth <= pReferenceEntryTitle->getParagraphCount(); nth++)
						{
							Paragraph *pParagraph = pReferenceEntryTitle->getParagraph(nth);
							if (pParagraph)
							{
								pOutputStream->OutputParagraph(m_pConfiguration, pParagraph, true, true, &paragraphFormat);
							}
						}
						pOutputStream->OutputFormattingCRLF();
					}
					pOutputStream->OutputBR();

					// If we saw a different number of entries than we should have seen,
					// log the error and continue.
					if (iEntryCounted != iEntryCount)
					{
						strWork.Format("Misnumbered reference entries. Total entries: %d, Counted: %d!", iEntryCount, iEntryCounted);
						m_pStatus->Log(strWork, LogLevel_Severe);
					}
					MessagePump::PumpDaMessages();
				}
				lCurrentIndex++;
			}
		}
	}

	pOutputStream->OutputSidebarBackEnd();
	if (pOutputStream)
	{
		delete pOutputStream;
	}
	m_pStatus->UpdateStatus("Completed Generating Reference List: " + strFileName);
}
 
*/
