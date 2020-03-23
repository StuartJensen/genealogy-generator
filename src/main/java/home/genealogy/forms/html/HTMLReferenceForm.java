package home.genealogy.forms.html;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import home.genealogy.GenealogyContext;
import home.genealogy.indexes.IndexMarriageToSpouses;
import home.genealogy.indexes.IndexPersonToMarriages;
import home.genealogy.indexes.IndexPersonToParentMarriage;
import home.genealogy.indexes.IndexPersonsToReferences;
import home.genealogy.schema.all.Citation;
import home.genealogy.schema.all.Classification;
import home.genealogy.schema.all.DateRange;
import home.genealogy.schema.all.Entry;
import home.genealogy.schema.all.Location;
import home.genealogy.schema.all.Marriage;
import home.genealogy.schema.all.MarriageTag;
import home.genealogy.schema.all.MarriageTagType;
import home.genealogy.schema.all.Paragraph;
import home.genealogy.schema.all.Person;
import home.genealogy.schema.all.PersonTag;
import home.genealogy.schema.all.PersonTagType;
import home.genealogy.schema.all.Reference;
import home.genealogy.schema.all.Table;
import home.genealogy.schema.all.Tag;
import home.genealogy.schema.all.helpers.ClassificationHelper;
import home.genealogy.schema.all.helpers.DateRangeHelper;
import home.genealogy.schema.all.helpers.EntryHelper;
import home.genealogy.schema.all.helpers.LocationHelper;
import home.genealogy.schema.all.helpers.MarriageHelper;
import home.genealogy.schema.all.helpers.MarriageIdHelper;
import home.genealogy.schema.all.helpers.MarriageTagHelper;
import home.genealogy.schema.all.helpers.PersonHelper;
import home.genealogy.schema.all.helpers.PersonIdHelper;
import home.genealogy.schema.all.helpers.PersonTagHelper;
import home.genealogy.schema.all.helpers.ReferenceHelper;
import home.genealogy.schema.all.helpers.TagHelper;

public class HTMLReferenceForm
{
	private GenealogyContext m_context;
	  
	public HTMLReferenceForm(GenealogyContext context)
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
		File fSubdirectory = new File(strOutputPath + HTMLShared.REFERENCEDIR);
		if (!fSubdirectory.exists())
		{
			if (!fSubdirectory.mkdirs())
			{
				throw new Exception("Error creating sub-directory tree for reference files!");
			}
		}		
		
		// Create necessary Indexes
		IndexPersonToParentMarriage idxPerToParentMar = new IndexPersonToParentMarriage(m_context.getFamily(), m_context.getPersonList());
		IndexPersonToMarriages idxPerToMar = new IndexPersonToMarriages(m_context.getFamily(), m_context.getMarriageList());
		IndexMarriageToSpouses idxMarToSpouses = new IndexMarriageToSpouses(m_context.getFamily(), m_context.getMarriageList());
		IndexPersonsToReferences idxPerToRef = new IndexPersonsToReferences(m_context.getFamily(), m_context.getPersonList(), m_context.getReferenceList());
		
		Iterator<Reference> iter = m_context.getReferenceList().getReferences();
		while (iter.hasNext())
		{
			Reference thisReference = iter.next();
			createReference(idxPerToParentMar, idxPerToMar, idxMarToSpouses, strOutputPath, thisReference);
		}
		m_context.output("Completed Generating All Reference Files\n");
	}
	
	
	private void createReference(
				 IndexPersonToParentMarriage idxPerToParentMar,
				 IndexPersonToMarriages idxPerToMar,
				 IndexMarriageToSpouses idxMarToSpouses,
				 String strOutputPath,
				 Reference reference)
		throws Exception
	{
		//PersonHelper personHelper = new PersonHelper(person, bSuppressLiving);
		String strFileName = strOutputPath + HTMLShared.REFERENCEDIR + "\\" + HTMLShared.REFERENCEFILENAME + reference.getReferenceId() + ".htm";
		m_context.output("Generating Reference File: " + strFileName+ "\n");
		HTMLFormOutput output = new HTMLFormOutput(strFileName);

		int iReferenceId = ReferenceHelper.getReferenceId(reference);
		String strTitle = ReferenceHelper.getTitle(reference);
		
		Citation citation = reference.getCitation();
		
		output.outputSidebarFrontEnd("Reference: " + strTitle, m_context.getFamily(), m_context.getPersonList(), m_context.getMarriageList());

		// Top of Document Internal Link Tag
		output.output("<A name=InfoTop></A>");
		output.outputCRLF();

		// Top of Page Title
		output.outputSpan(HTMLFormOutput.styleSelectors.E_PageTopHeader, strTitle);
		output.outputBR();
		output.outputCRLF();
		
		
		// Document Number
		output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodySmallHeader, "Document Number:");
		output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyNormalText, " " + iReferenceId);
		output.outputBR(2);
		output.outputCRLF();
		// Author
		List<String> alAuthorPersonNames = ReferenceHelper.getPersonAuthorNames(reference);
		List<String> alAuthorGroupNames = ReferenceHelper.getGroupAuthorNames(reference);
		if ((0 != alAuthorPersonNames.size()) || (0 != alAuthorGroupNames.size()))
		{
			for (int i=0; i<alAuthorPersonNames.size(); i++)
			{
				output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodySmallHeader, "Author:");
				output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyNormalText, "&nbsp;" + alAuthorPersonNames.get(i));
				output.outputBR();
				output.outputCRLF();	
			}
			for (int i=0; i<alAuthorGroupNames.size(); i++)
			{
				output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodySmallHeader, "Author Group:");
				output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyNormalText, "&nbsp;" + alAuthorGroupNames.get(i));
				output.outputBR();
				output.outputCRLF();	
			}
		}
		
		// Call Numbers: Institution/Call Number
		int iLocationCount = ReferenceHelper.getLocationCount(reference);
		for (int i=0; i<iLocationCount; i++)
		{
			Location location = ReferenceHelper.getLocation(reference, i);
			String strInstitutionName = LocationHelper.getInstitutionName(location);
			if ((null != strInstitutionName) && (0 != strInstitutionName.length()))
			{
				output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodySmallHeader, "Institution:");
				output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyNormalText, "&nbsp;" + strInstitutionName);
				output.outputBR();
				output.outputCRLF();
			}
			
			int iCallNumberCount = LocationHelper.getCallNumberCount(location);
			for (int c=0; c<iCallNumberCount; c++)
			{
				output.output("&nbsp;&nbsp;&nbsp;&nbsp;");
				output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodySmallHeader, "Call Number:");
				output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyNormalText, "&nbsp;" + LocationHelper.getCallNumber(location, c));
				output.outputBR();
				output.outputCRLF();
			}
		}
		
		// Classification
		int iClassificationCount = ReferenceHelper.getClassificationCount(reference);
		for (int i=0; i<iClassificationCount; i++)
		{
			Classification classification = ReferenceHelper.getClassification(reference, i);
			output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodySmallHeader, "Classification:");
			output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyNormalText, " " + ClassificationHelper.getType(classification) + ": " + ClassificationHelper.getSubType(classification));
			output.outputBR();
			output.outputCRLF();
		}		

		// Primary Geography
		String strCitationPlace = ReferenceHelper.getCitationPlace(reference, m_context.getPlaceList());
		if ((null != strCitationPlace) &&(0 != strCitationPlace.length()))
		{
			output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodySmallHeader, "Location:");
			output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyNormalText, "&nbsp;" + strCitationPlace);
			output.outputBR();
			output.outputCRLF();
		}

		// Start/End Time
		int iDateRangeCount = ReferenceHelper.getCitationDateRangeCount(reference);
		for (int i=0; i<iDateRangeCount; i++)
		{
			DateRange dateRange = ReferenceHelper.getCitationDateRange(reference, i);
			output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodySmallHeader, "Date Range:");
			output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyNormalText, "&nbsp;" + DateRangeHelper.getDateRange(dateRange));
			output.outputBR();
			output.outputCRLF();
		}
		
		output.output("<hr width=\"100%\">");
		output.outputCRLF();

		// For all entries of the current reference
		int iEntryCounted = 0;
		int iEntryCount = ReferenceHelper.getEntryCount(reference);
		for (int iEntry=0; iEntry<iEntryCount; iEntry++)
		{
			Entry entry = ReferenceHelper.getEntry(reference, iEntry);
			int iReferenceEntryId = EntryHelper.getEntryId(entry);
			iEntryCounted++;

			// Top of Reference Entry Internal Link Tag
			String strLink = "<A name=\"REF" + iReferenceId + "ENT" + iReferenceEntryId + "\">";
			output.output(strLink);
			output.outputCRLF();
			
			// Title
			HTMLParagraphFormat paragraphFormat = new HTMLParagraphFormat();
			paragraphFormat.setParagraphTextSpan("pageBodyMediumHeader");
			paragraphFormat.setEditorCommentSpan("pageBodyMediumHeader");
			paragraphFormat.setFormTextSpan("pageBodyMediumHeader");
			paragraphFormat.setPersonIdSpan("pageBodyMediumHeader");
			paragraphFormat.setMarriageIdSpan("pageBodyMediumHeader");
			paragraphFormat.setReferenceIdSpan("pageBodyMediumHeader");
			paragraphFormat.setPhotoIdSpan("pageBodyMediumHeader");
			
			int iTitleParagraphCount = EntryHelper.getTitleParagraphCount(entry);
			if (0 != iTitleParagraphCount)
			{
				for (int i=0; i<iTitleParagraphCount; i++)
				{
					Paragraph paragraph = EntryHelper.getTitleParagraph(entry, i);
					String strEventTitle = HTMLShared.buildParagraphString(m_context, paragraph,
	                        idxMarToSpouses, true, true, paragraphFormat, -1, -1);
					if (0 != strEventTitle.length())
					{
						output.output(strEventTitle);
						output.outputBR();
						output.outputCRLF();
					}
				}
			}
			
			// Document Entry Number
			output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodySmallHeader, "Document Entry Number:");
			output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyNormalText, " " + iReferenceEntryId);
			output.outputBR(2);
			output.outputCRLF();

			if (EntryHelper.hasPrivateAccess(entry) && m_context.getSuppressLiving())
			{
				output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyNormalText, "<b>The content of this document entry has been deemed <i>private</i>.</b><br>This simply means that the family feels it is of a private or sacred nature.<br>However, we would love to share the document entry with anyone who has an honest interest in it.<br>If you are interested in viewing the content of this document entry, email us at the address provided on the sidebar explaining who you are and what your interest in the document entry is.");
				output.outputBR(2);
				output.outputCRLF();
				// End the Entry with a HR
				output.output("<hr>");
				// Do not output any of the entry guts!
				continue;
			}

			if (EntryHelper.hasCopyRightedAccess(entry) && m_context.getSuppressLiving())
			{
				output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyNormalText, "<b>The content of this document entry is (or may be) <i>copyrighted</i>.</b><br>If you cannot locate the resource using the location details, you are welcome to contact us, using the email address on the sidebar, and we may be able to help you.");
				output.outputBR(2);
				output.outputCRLF();
				// End the Entry with a HR
				output.output("<hr>");
				// Do not output any of the entry guts!
				continue;
			}
			
			// Header
			String strVolumeNumbers = EntryHelper.getVolumeNumbers(entry);
			String strChapterNumbers = EntryHelper.getChapterNumbers(entry);
			String strPageNumbers = EntryHelper.getPageNumbers(entry);
			String strEntryNumbers = EntryHelper.getEntryNumbers(entry);
			int iHeaderParagraphCount = EntryHelper.getHeaderParagraphCount(entry);
			
			if ((0 != iHeaderParagraphCount) ||
				((null !=  strVolumeNumbers ) && (0 != strVolumeNumbers.length())) ||
				((null !=  strChapterNumbers ) && (0 != strChapterNumbers.length())) ||
				((null !=  strPageNumbers ) && (0 != strPageNumbers.length())) ||
				((null !=  strEntryNumbers ) && (0 != strEntryNumbers.length())))
			{
				// Header Header
				output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyMediumHeader, "Information:");
				output.outputBR();
				output.outputCRLF();
				// Volume Numbers
				if ((null !=  strVolumeNumbers ) && (0 != strVolumeNumbers.length()))
				{
					output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodySmallHeader, "Volume:");
					output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyNormalText, "&nbsp;" + strVolumeNumbers);
					output.outputBR();
					output.outputCRLF();
				}
				// Chapter Numbers
				if ((null !=  strChapterNumbers ) && (0 != strChapterNumbers.length()))
				{
					output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodySmallHeader, "Chapter:");
					output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyNormalText, "&nbsp;" + strChapterNumbers);
					output.outputBR();
					output.outputCRLF();
				}
				// Page Numbers
				if ((null !=  strPageNumbers ) && (0 != strPageNumbers.length()))
				{
					output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodySmallHeader, "Page:");
					output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyNormalText, "&nbsp;" + strPageNumbers);
					output.outputBR();
					output.outputCRLF();
				}
				// Entry Numbers
				if ((null !=  strEntryNumbers ) && (0 != strEntryNumbers.length()))
				{
					output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodySmallHeader, "Entry:");
					output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyNormalText, "&nbsp;" + strEntryNumbers);
					output.outputBR();
					output.outputCRLF();
				}
				// Header Paragraphs
				for(int i=0; i<iHeaderParagraphCount; i++)
				{
					Paragraph paragraph = EntryHelper.getHeaderParagraph(entry, i);
					String strParagraph = HTMLShared.buildParagraphString(m_context, paragraph, 
	                        idxMarToSpouses, true, true, null, iReferenceId, iReferenceEntryId);
					if (0 != strParagraph.length())
					{
						output.output(strParagraph);
						output.outputCRLF();
					}
				}
				output.outputBR();
				output.outputCRLF();
			}
			
			// Body
			int iBodyParagraphCount = EntryHelper.getBodyObjectCount(entry);
			if (0 != iBodyParagraphCount)
			{
				// Body Header
				output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyMediumHeader, "Transcription:");
				output.outputBR();
				output.outputCRLF();
				for (int i=0; i<iBodyParagraphCount; i++)
				{
					Object oo = EntryHelper.getBodyObject(entry, i);
					if (oo instanceof String)
					{	// String objects represent content data outside of paragraphs and tables
					}	// We do not want to show that data.
					else if (oo instanceof Paragraph)
					{
						Paragraph paragraph = (Paragraph)oo;
						String strParagraph = HTMLShared.buildParagraphString(m_context, paragraph,
		                        idxMarToSpouses, true, true, null, iReferenceId, iReferenceEntryId);
						if (0 != strParagraph.length())
						{
							output.output(strParagraph);
							output.outputCRLF();
						}
					}
					else if (oo instanceof Table)
					{
						Table table = (Table)oo;
						String strTable = HTMLShared.buildTableString(m_context, table, 
		                        idxMarToSpouses, true, true, null, iReferenceId, iReferenceEntryId);
						if (0 != strTable.length())
						{
							output.output(strTable);
							output.outputCRLF();
						}
					}
				}
				output.outputBR();
				output.outputCRLF();
			}
			
			// Comment
			int iCommentParagraphCount = EntryHelper.getCommentParagraphCount(entry);
			if (0 != iCommentParagraphCount)
			{	// Comment Header
				output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyMediumHeader, "Editor's Comments About Entry:");
				output.outputBR();
				output.outputCRLF();
				
				for (int p=0; p<iCommentParagraphCount; p++)
				{
					Paragraph paragraph =  EntryHelper.getCommentParagraph(entry, p);
					String strParagraph = HTMLShared.buildParagraphString(m_context, paragraph,
	                        idxMarToSpouses, true, true, null, iReferenceId, iReferenceEntryId);
					if (0 != strParagraph.length())
					{
						output.output(strParagraph);
						output.outputCRLF();
					}
				}
				output.outputBR();
				output.outputCRLF();
			}				

			// SeeAlso
			if (0 != EntryHelper.getSeeAlsoObjectCount(entry))
			{
				String strSeeAlso = HTMLShared.buildSeeAlsoString(m_context, entry.getSeeAlso(),
																  null, idxMarToSpouses);
				if (0 != strSeeAlso.length())
				{
					output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyMediumHeader, "See Also:");
					output.outputBR();
					output.output(strSeeAlso);
					output.outputBR();
					output.outputCRLF();
				}
			}
			
			// Tag Groups
			output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyMediumHeader, "Associated Persons and Marriages:");
			output.outputBR();
			output.outputCRLF();
			
			List<PersonTag> lPersonTags = EntryHelper.getPersonTagList(entry);
			if (null != lPersonTags)
			{
				for (int i=0; i<lPersonTags.size(); i++)
				{
					PersonTag personTag = lPersonTags.get(i);
					int iPersonId = PersonTagHelper.getPersonId(personTag);
					if (iPersonId != PersonIdHelper.PERSONID_INVALID)
					{
						Person taggedPerson = m_context.getPersonList().get(iPersonId);
						if (null != taggedPerson)
						{
							PersonHelper taggedPersonHelper = new PersonHelper(taggedPerson, m_context.getSuppressLiving(), m_context.getPlaceList());
							String strTaggedPersonName = taggedPersonHelper.getPersonName();
							int iPersonTagCount = PersonTagHelper.getPersonTagTypeCount(personTag);
							for (int t=0; t<iPersonTagCount; t++)
							{
								PersonTagType ptt = PersonTagHelper.getPersonTagType(personTag, t);
								String strQuality = ptt.getQuality();
								String strType = ptt.getType();
								
								// Show <Person Name> (ID# 4535) BornD, MEDIUM [Goto Data]
								output.output("<span class=\"pageBodyNormalLink\">");
								// Build href to PersonInfo and show <Person Name>
								String strUrl = m_context.getFamily().getUrlPrefix() + HTMLShared.PERINFODIR + "/" + HTMLShared.PERINFOFILENAME +  iPersonId + ".htm";
								output.outputStartAnchor(strUrl);
								output.output(strTaggedPersonName);
								output.outputEndAnchor();
								// Show (ID# 4535) BornD, MEDIUM
								String strWork = "&nbsp;(Id#&nbsp;" + iPersonId + ")&nbsp;" + strType + ",&nbsp;" + strQuality + "&nbsp;";
								output.output(strWork);
								output.output("</span>");
								output.outputBR();
								output.outputCRLF();
							}
						}
					}
				}
			}
			// Embedded person tag list
			List<Tag> alTags = EntryHelper.getTagList(entry);
			if (null != alTags)
			{
				for (int t=0; t<alTags.size(); t++)
				{
					Tag tag = alTags.get(t);
					if (TagHelper.isPersonTag(tag))
					{
						int iPersonId = TagHelper.getPersonId(tag);
						if (iPersonId != PersonIdHelper.PERSONID_INVALID)
						{
							Person taggedPerson = m_context.getPersonList().get(iPersonId);
							if (null == taggedPerson)
							{	// Sanity check on person id
								System.out.println("ERROR: Unknown person id \"" + iPersonId + "\" in embedded tag in reference \"" + iReferenceId + "\" entry \"" + iReferenceEntryId + "\"");
							}
							PersonHelper taggedPersonHelper = new PersonHelper(taggedPerson, m_context.getSuppressLiving(), m_context.getPlaceList());
							String strTaggedPersonName = taggedPersonHelper.getPersonName();
							String strQuality = tag.getQuality();
							String strType = tag.getType();
							
							// Show <Person Name> (ID# 4535) BornD, MEDIUM [Goto Data]
							output.output("<span class=\"pageBodyNormalLink\">");
							// Build href to PersonInfo and show <Person Name>
							String strUrl = m_context.getFamily().getUrlPrefix() + HTMLShared.PERINFODIR + "/" + HTMLShared.PERINFOFILENAME +  iPersonId + ".htm";
							output.outputStartAnchor(strUrl);
							output.output(strTaggedPersonName);
							output.outputEndAnchor();
							// Show (ID# 4535) BornD, MEDIUM
							String strWork = "&nbsp;(Id#&nbsp;" + iPersonId + ")&nbsp;" + strType + ",&nbsp;" + strQuality + "&nbsp;";
							output.output(strWork);
							// Show [Goto Data]
							String strHRef = "#C" + iReferenceId + "SC" + iReferenceEntryId + "PID" + iPersonId + "T" + strType;
							output.outputStartAnchor(strHRef);
							output.output("&nbsp;[Goto Data Point]");
							output.outputEndAnchor();
							
                            output.output("</span>");
							output.outputBR();
							output.outputCRLF();
						}
					}
				}
			}
			List<MarriageTag> lMarriageTags = EntryHelper.getMarriageTagList(entry);
			if (null != lMarriageTags)
			{
				for (int i=0; i<lMarriageTags.size(); i++)
				{
					MarriageTag marriageTag = lMarriageTags.get(i);
					int iMarriageId = MarriageTagHelper.getMarriageId(marriageTag);
					if (iMarriageId != MarriageIdHelper.MARRIAGEID_INVALID)
					{
						Marriage taggedMarriage = m_context.getMarriageList().get(iMarriageId);
						if (null != taggedMarriage)
						{
							Person husband = m_context.getPersonList().get(MarriageHelper.getHusbandPersonId(taggedMarriage));
							Person wife = m_context.getPersonList().get(MarriageHelper.getWifePersonId(taggedMarriage));
							if ((null != husband) && (null != wife))
							{
								PersonHelper husbandHelper = new PersonHelper(husband, m_context.getSuppressLiving(), m_context.getPlaceList());
								PersonHelper wifeHelper = new PersonHelper(wife, m_context.getSuppressLiving(), m_context.getPlaceList());
								MarriageHelper taggedMarriageHelper = new MarriageHelper(taggedMarriage, husbandHelper, wifeHelper, m_context.getSuppressLiving(), m_context.getPlaceList());
								String strTaggedMarriageName = taggedMarriageHelper.getMarriageName(m_context.getPersonList());
								int iMarriageTagCount = MarriageTagHelper.getMarriageTagTypeCount(marriageTag);
								for (int t=0; t<iMarriageTagCount; t++)
								{
									MarriageTagType mtt = MarriageTagHelper.getMarriageTagType(marriageTag, t);
									String strQuality = mtt.getQuality();
									String strType = mtt.getType();
									
									// Show <Person Name> (ID# 4535) BornD, MEDIUM [Goto Data]
									output.output("<span class=\"pageBodyNormalLink\">");
									// Build href to PersonInfo and show <Person Name>
									String strUrl = m_context.getFamily().getUrlPrefix() + HTMLShared.FGSDIR + "/" + HTMLShared.FGSFILENAME + iMarriageId + ".htm";
									output.outputStartAnchor(strUrl);
									output.output(strTaggedMarriageName);
									output.outputEndAnchor();
									// Show (ID# 4535) BornD, MEDIUM
									String strWork = "&nbsp;(Id#&nbsp;" + iMarriageId + ")&nbsp;" + strType + ",&nbsp;" + strQuality + "&nbsp;";
									output.output(strWork);
									output.output("</span>");
									output.outputBR();
									output.outputCRLF();
								}
							}
						}
					}
				}
			}
			
			// Embedded marriage tag list
			if (null != alTags)
			{
				for (int t=0; t<alTags.size(); t++)
				{
					Tag tag = alTags.get(t);
					if (TagHelper.isMarriageTag(tag))
					{
						int iMarriageId = TagHelper.getMarriageId(tag);
						if (iMarriageId != MarriageIdHelper.MARRIAGEID_INVALID)
						{
							Marriage taggedMarriage = m_context.getMarriageList().get(iMarriageId);
							if (null == taggedMarriage)
							{	// Sanity check on marriage id
								System.out.println("ERROR: Unknown marriage id \"" + iMarriageId + "\" in embedded tag in reference \"" + iReferenceId + "\" entry \"" + iReferenceEntryId + "\"");
							}
							else
							{
								Person husband = m_context.getPersonList().get(MarriageHelper.getHusbandPersonId(taggedMarriage));
								if (null == husband)
								{	// Sanity check on husband
									System.out.println("ERROR: Unknown person id for husband in embedded reference tag for marriage id \"" + iMarriageId + "\" in reference \"" + iReferenceId + "\" entry \"" + iReferenceEntryId + "\"");
								}
								Person wife = m_context.getPersonList().get(MarriageHelper.getWifePersonId(taggedMarriage));
								if (null == wife)
								{	// Sanity check on husband
									System.out.println("ERROR: Unknown person id for wife in embedded reference tag for marriage id \"" + iMarriageId + "\" in reference \"" + iReferenceId + "\" entry \"" + iReferenceEntryId + "\"");
								}
								if ((null != husband) && (null != wife))
								{
									PersonHelper husbandHelper = new PersonHelper(husband, m_context.getSuppressLiving(), m_context.getPlaceList());
									PersonHelper wifeHelper = new PersonHelper(wife, m_context.getSuppressLiving(), m_context.getPlaceList());
									MarriageHelper taggedMarriageHelper = new MarriageHelper(taggedMarriage, husbandHelper, wifeHelper, m_context.getSuppressLiving(), m_context.getPlaceList());
									String strTaggedMarriageName = taggedMarriageHelper.getMarriageName(m_context.getPersonList());
									String strQuality = tag.getQuality();
									String strType = tag.getType();
									
									// Show <Person Name> (ID# 4535) BornD, MEDIUM [Goto Data]
									output.output("<span class=\"pageBodyNormalLink\">");
									// Build href to PersonInfo and show <Person Name>
									String strUrl = m_context.getFamily().getUrlPrefix() + HTMLShared.FGSDIR + "/" + HTMLShared.FGSFILENAME + iMarriageId + ".htm";
									output.outputStartAnchor(strUrl);
									output.output(strTaggedMarriageName);
									output.outputEndAnchor();
									// Show (ID# 4535) BornD, MEDIUM
									String strWork = "&nbsp;(Id#&nbsp;" + iMarriageId + ")&nbsp;" + strType + ",&nbsp;" + strQuality + "&nbsp;";
									output.output(strWork);
									
									// Show [Goto Data]
									String strHRef = "#C" + iReferenceId + "SC" + iReferenceEntryId + "MID" + iMarriageId + "T" + strType;
									output.outputStartAnchor(strHRef);
									output.output("&nbsp;[Goto Data Point]");
									output.outputEndAnchor();
									
									output.output("</span>");
									output.outputBR();
									output.outputCRLF();
								}
							}
						}
					}
				}
			}
			
			// End the Entry with a HR
			output.output("<hr>");
		}

		// If we saw a different number of entries than we should have seen,
		// log the error and continue.
		if (iEntryCounted != iEntryCount)
		{
			m_context.output("ERROR: Misnumbered reference entries. Total entries: " + iEntryCount + ", Counted: " + iEntryCounted + "!\n");
		}
		
		output.outputSidebarBackEnd();
		
		output.commit();
		output = null;
	}
}