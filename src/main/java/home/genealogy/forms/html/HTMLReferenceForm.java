package home.genealogy.forms.html;

import home.genealogy.CommandLineParameters;
import home.genealogy.GenealogyContext;
import home.genealogy.configuration.CFGFamily;
import home.genealogy.indexes.IndexMarriageToSpouses;
import home.genealogy.indexes.IndexPersonToMarriages;
import home.genealogy.indexes.IndexPersonToParentMarriage;
import home.genealogy.indexes.IndexPersonsToReferences;
import home.genealogy.lists.MarriageList;
import home.genealogy.lists.PersonList;
import home.genealogy.lists.PhotoList;
import home.genealogy.lists.PlaceList;
import home.genealogy.lists.ReferenceList;
import home.genealogy.output.IOutputStream;
import home.genealogy.schema.all.Body;
import home.genealogy.schema.all.Citation;
import home.genealogy.schema.all.Classification;
import home.genealogy.schema.all.DateRange;
import home.genealogy.schema.all.Entry;
import home.genealogy.schema.all.EntryTitle;
import home.genealogy.schema.all.Location;
import home.genealogy.schema.all.Marriage;
import home.genealogy.schema.all.MarriageTag;
import home.genealogy.schema.all.MarriageTagType;
import home.genealogy.schema.all.Paragraph;
import home.genealogy.schema.all.Person;
import home.genealogy.schema.all.PersonTag;
import home.genealogy.schema.all.PersonTagType;
import home.genealogy.schema.all.Reference;
import home.genealogy.schema.all.SeeAlso;
import home.genealogy.schema.all.Table;
import home.genealogy.schema.all.Tag;
import home.genealogy.schema.all.helpers.BodyHelper;
import home.genealogy.schema.all.helpers.CitationHelper;
import home.genealogy.schema.all.helpers.ClassificationHelper;
import home.genealogy.schema.all.helpers.DateRangeHelper;
import home.genealogy.schema.all.helpers.EntryHelper;
import home.genealogy.schema.all.helpers.EntryTitleHelper;
import home.genealogy.schema.all.helpers.LocationHelper;
import home.genealogy.schema.all.helpers.MarriageHelper;
import home.genealogy.schema.all.helpers.MarriageIdHelper;
import home.genealogy.schema.all.helpers.MarriageTagHelper;
import home.genealogy.schema.all.helpers.PersonHelper;
import home.genealogy.schema.all.helpers.PersonIdHelper;
import home.genealogy.schema.all.helpers.PersonTagHelper;
import home.genealogy.schema.all.helpers.ReferenceHelper;
import home.genealogy.schema.all.helpers.TagHelper;
import home.genealogy.util.CommandLineParameterList;

import java.io.File;
import java.util.Iterator;
import java.util.List;

public class HTMLReferenceForm
{
	private CFGFamily m_family;
	private PlaceList m_placeList;
	private PersonList m_personList;
	private MarriageList m_marriageList;
	private ReferenceList m_referenceList;
	private PhotoList m_photoList;
	private boolean m_bSuppressLiving;
	private CommandLineParameters m_commandLineParameters;
	private IOutputStream m_outputStream;
	  
	public HTMLReferenceForm(GenealogyContext context)
	{
		m_family = context.getFamily();
		m_placeList = context.getPlaceList();
		m_personList = context.getPersonList();
		m_marriageList = context.getMarriageList();
		m_referenceList = context.getReferenceList();
		m_photoList = context.getPhotoList();
		m_bSuppressLiving = context.getSuppressLiving();
		m_commandLineParameters = context.getCommandLineParameters();
		m_outputStream = context.getOutputStream();
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
		File fSubdirectory = new File(strOutputPath + HTMLShared.REFERENCEDIR);
		if (!fSubdirectory.exists())
		{
			if (!fSubdirectory.mkdirs())
			{
				throw new Exception("Error creating sub-directory tree for reference files!");
			}
		}		
		
		// Create necessary Indexes
		IndexPersonToParentMarriage idxPerToParentMar = new IndexPersonToParentMarriage(m_family, m_personList);
		IndexPersonToMarriages idxPerToMar = new IndexPersonToMarriages(m_family, m_marriageList);
		IndexMarriageToSpouses idxMarToSpouses = new IndexMarriageToSpouses(m_family, m_marriageList);
		IndexPersonsToReferences idxPerToRef = new IndexPersonsToReferences(m_family, m_personList, m_referenceList);
		
		Iterator<Reference> iter = m_referenceList.getReferences();
		while (iter.hasNext())
		{
			Reference thisReference = iter.next();
			createReference(m_family, m_placeList, m_personList, m_marriageList, m_referenceList,
 					         m_photoList, m_bSuppressLiving, idxPerToParentMar,
 					         idxPerToMar, idxMarToSpouses, strOutputPath, thisReference);
		}
		m_outputStream.output("Completed Generating All Reference Files\n");
	}
	
	
	private void createReference(CFGFamily family,
				 PlaceList placeList,
				 PersonList personList,
				 MarriageList marriageList,
				 ReferenceList referenceList,
				 PhotoList photoList,
				 boolean bSuppressLiving,
				 IndexPersonToParentMarriage idxPerToParentMar,
				 IndexPersonToMarriages idxPerToMar,
				 IndexMarriageToSpouses idxMarToSpouses,
				 String strOutputPath,
				 Reference reference)
		throws Exception
	{
		//PersonHelper personHelper = new PersonHelper(person, bSuppressLiving);
		String strFileName = strOutputPath + HTMLShared.REFERENCEDIR + "\\" + HTMLShared.REFERENCEFILENAME + reference.getReferenceId() + ".htm";
		m_outputStream.output("Generating Reference File: " + strFileName+ "\n");
		HTMLFormOutput output = new HTMLFormOutput(strFileName);

		int iReferenceId = ReferenceHelper.getReferenceId(reference);
		String strTitle = ReferenceHelper.getTitle(reference);
		
		Citation citation = reference.getCitation();
		
		output.outputSidebarFrontEnd("Reference: " + strTitle, m_family, personList, marriageList);

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
		String strCitationPlace = ReferenceHelper.getCitationPlace(reference, m_placeList);
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
					String strEventTitle = HTMLShared.buildParagraphString(family, m_commandLineParameters,
							paragraph, placeList, personList, marriageList, referenceList, photoList,
	                        idxMarToSpouses, true, true, bSuppressLiving, paragraphFormat, -1, -1);
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

			if (EntryHelper.hasPrivateAccess(entry) && m_bSuppressLiving)
			{
				output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyNormalText, "<b>The content of this document entry has been deemed <i>private</i>.</b><br>This simply means that the family feels it is of a private or sacred nature.<br>However, we would love to share the document entry with anyone who has an honest interest in it.<br>If you are interested in viewing the content of this document entry, email us at the address provided on the sidebar explaining who you are and what your interest in the document entry is.");
				output.outputBR(2);
				output.outputCRLF();
				// End the Entry with a HR
				output.output("<hr>");
				// Do not output any of the entry guts!
				continue;
			}

			if (EntryHelper.hasCopyRightedAccess(entry) && m_bSuppressLiving)
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
					String strParagraph = HTMLShared.buildParagraphString(family, m_commandLineParameters,
							paragraph, placeList, personList, marriageList, referenceList, photoList,
	                        idxMarToSpouses, true, true, bSuppressLiving, null, iReferenceId, iReferenceEntryId);
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
						String strParagraph = HTMLShared.buildParagraphString(family, m_commandLineParameters,
								paragraph, placeList, personList, marriageList, referenceList, photoList,
		                        idxMarToSpouses, true, true, bSuppressLiving, null, iReferenceId, iReferenceEntryId);
						if (0 != strParagraph.length())
						{
							output.output(strParagraph);
							output.outputCRLF();
						}
					}
					else if (oo instanceof Table)
					{
						Table table = (Table)oo;
						String strTable = HTMLShared.buildTableString(family, m_commandLineParameters,
								table, placeList, personList, marriageList, referenceList, photoList,
		                        idxMarToSpouses, true, true, bSuppressLiving, null, iReferenceId, iReferenceEntryId);
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
					String strParagraph = HTMLShared.buildParagraphString(family, m_commandLineParameters,
							paragraph, placeList, personList, marriageList, referenceList, photoList,
	                        idxMarToSpouses, true, true, bSuppressLiving, null, iReferenceId, iReferenceEntryId);
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
				String strSeeAlso = HTMLShared.buildSeeAlsoString(entry.getSeeAlso(), family, m_commandLineParameters,
																  placeList, personList, marriageList, referenceList,
																  photoList, null, idxMarToSpouses);
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
						Person taggedPerson = personList.get(iPersonId);
						if (null != taggedPerson)
						{
							PersonHelper taggedPersonHelper = new PersonHelper(taggedPerson, bSuppressLiving, m_placeList);
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
								String strUrl = family.getUrlPrefix() + HTMLShared.PERINFODIR + "/" + HTMLShared.PERINFOFILENAME +  iPersonId + ".htm";
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
							Person taggedPerson = personList.get(iPersonId);
							if (null == taggedPerson)
							{	// Sanity check on person id
								System.out.println("ERROR: Unknown person id \"" + iPersonId + "\" in embedded tag in reference \"" + iReferenceId + "\" entry \"" + iReferenceEntryId + "\"");
							}
							PersonHelper taggedPersonHelper = new PersonHelper(taggedPerson, bSuppressLiving, m_placeList);
							String strTaggedPersonName = taggedPersonHelper.getPersonName();
							String strQuality = tag.getQuality();
							String strType = tag.getType();
							
							// Show <Person Name> (ID# 4535) BornD, MEDIUM [Goto Data]
							output.output("<span class=\"pageBodyNormalLink\">");
							// Build href to PersonInfo and show <Person Name>
							String strUrl = family.getUrlPrefix() + HTMLShared.PERINFODIR + "/" + HTMLShared.PERINFOFILENAME +  iPersonId + ".htm";
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
						Marriage taggedMarriage = marriageList.get(iMarriageId);
						if (null != taggedMarriage)
						{
							Person husband = personList.get(MarriageHelper.getHusbandPersonId(taggedMarriage));
							Person wife = personList.get(MarriageHelper.getWifePersonId(taggedMarriage));
							if ((null != husband) && (null != wife))
							{
								PersonHelper husbandHelper = new PersonHelper(husband, bSuppressLiving, m_placeList);
								PersonHelper wifeHelper = new PersonHelper(wife, bSuppressLiving, m_placeList);
								MarriageHelper taggedMarriageHelper = new MarriageHelper(taggedMarriage, husbandHelper, wifeHelper, bSuppressLiving, m_placeList);
								String strTaggedMarriageName = taggedMarriageHelper.getMarriageName(personList);
								int iMarriageTagCount = MarriageTagHelper.getMarriageTagTypeCount(marriageTag);
								for (int t=0; t<iMarriageTagCount; t++)
								{
									MarriageTagType mtt = MarriageTagHelper.getMarriageTagType(marriageTag, t);
									String strQuality = mtt.getQuality();
									String strType = mtt.getType();
									
									// Show <Person Name> (ID# 4535) BornD, MEDIUM [Goto Data]
									output.output("<span class=\"pageBodyNormalLink\">");
									// Build href to PersonInfo and show <Person Name>
									String strUrl = family.getUrlPrefix() + HTMLShared.FGSDIR + "/" + HTMLShared.FGSFILENAME + iMarriageId + ".htm";
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
							Marriage taggedMarriage = marriageList.get(iMarriageId);
							if (null == taggedMarriage)
							{	// Sanity check on marriage id
								System.out.println("ERROR: Unknown marriage id \"" + iMarriageId + "\" in embedded tag in reference \"" + iReferenceId + "\" entry \"" + iReferenceEntryId + "\"");
							}
							else
							{
								Person husband = personList.get(MarriageHelper.getHusbandPersonId(taggedMarriage));
								if (null == husband)
								{	// Sanity check on husband
									System.out.println("ERROR: Unknown person id for husband in embedded reference tag for marriage id \"" + iMarriageId + "\" in reference \"" + iReferenceId + "\" entry \"" + iReferenceEntryId + "\"");
								}
								Person wife = personList.get(MarriageHelper.getWifePersonId(taggedMarriage));
								if (null == wife)
								{	// Sanity check on husband
									System.out.println("ERROR: Unknown person id for wife in embedded reference tag for marriage id \"" + iMarriageId + "\" in reference \"" + iReferenceId + "\" entry \"" + iReferenceEntryId + "\"");
								}
								if ((null != husband) && (null != wife))
								{
									PersonHelper husbandHelper = new PersonHelper(husband, bSuppressLiving, m_placeList);
									PersonHelper wifeHelper = new PersonHelper(wife, bSuppressLiving, m_placeList);
									MarriageHelper taggedMarriageHelper = new MarriageHelper(taggedMarriage, husbandHelper, wifeHelper, bSuppressLiving, m_placeList);
									String strTaggedMarriageName = taggedMarriageHelper.getMarriageName(personList);
									String strQuality = tag.getQuality();
									String strType = tag.getType();
									
									// Show <Person Name> (ID# 4535) BornD, MEDIUM [Goto Data]
									output.output("<span class=\"pageBodyNormalLink\">");
									// Build href to PersonInfo and show <Person Name>
									String strUrl = family.getUrlPrefix() + HTMLShared.FGSDIR + "/" + HTMLShared.FGSFILENAME + iMarriageId + ".htm";
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
			m_outputStream.output("ERROR: Misnumbered reference entries. Total entries: " + iEntryCount + ", Counted: " + iEntryCounted + "!\n");
		}
		
		output.outputSidebarBackEnd();
		
		output.commit();
		output = null;
	}
}

/*
 
  
  void FormsHtmlReference::Create()
{
	long lCookie;
	CString strWork, strHRef;
	CString strFileName;
	strFileName.Format("%sreferences\\%s%d.htm", m_pConfiguration->getDestinationPath(), REFERENCEFILENAME, m_lReferenceId); 
	m_pStatus->Log("Generating Reference: " + strFileName, LogLevel_Finest);
	m_pStatus->UpdateStatus("Generating Reference: " + strFileName);
	FormsHtmlOutputStream *pOutputStream = new FormsHtmlOutputStream(strFileName, m_pStatus);

	Reference *pReference = m_pReferenceList->get(m_lReferenceId);
	if (pReference)
	{
		ReferenceCitation *pCitation = pReference->getReferenceCitation();
		ReferenceSource *pSource = pCitation->getReferenceSource();
		ReferenceAuthor *pAuthor = pSource->getReferenceAuthor();

		pOutputStream->OutputSidebarFrontEnd("Reference: " + pReference->getTitle(), m_pConfiguration);

		// Top of Document Internal Link Tag
		pOutputStream->Output("<A name=InfoTop></A>");
		pOutputStream->OutputFormattingCRLF();

		// Top of Page Title
		pOutputStream->OutputSpan(E_PageTopHeader, pReference->getTitle());
		pOutputStream->OutputBR();
		pOutputStream->OutputFormattingCRLF();
		// Document Number
		pOutputStream->OutputSpan(E_PageBodySmallHeader, "Document Number:");
		strWork.Format(" %d", m_lReferenceId);
		pOutputStream->OutputSpan(E_PageBodyNormalText, strWork);
		pOutputStream->OutputBR(2);
		pOutputStream->OutputFormattingCRLF();
		// Author
		if (!pAuthor->isEmpty())
		{
			if (pAuthor->StartPersonNameEnum(&lCookie))
			{
				PersonName *pPersonName;
				while (pAuthor->NextPersonNameEnum(&pPersonName, &lCookie))
				{
					if (!pPersonName->isEmpty())
					{
						pOutputStream->OutputSpan(E_PageBodySmallHeader, "Author:");
						pOutputStream->OutputSpan(E_PageBodyNormalText, "&nbsp;" + pPersonName->getFullName());
						pOutputStream->OutputBR();
						pOutputStream->OutputFormattingCRLF();
					}
				}
			}
			if (pAuthor->StartGroupNameEnum(&lCookie))
			{
				ReferenceGroupName *pReferenceGroupName;
				while (pAuthor->NextGroupNameEnum(&pReferenceGroupName, &lCookie))
				{
					if (!pReferenceGroupName->isEmpty())
					{
						pOutputStream->OutputSpan(E_PageBodySmallHeader, "Author Group:");
						pOutputStream->OutputSpan(E_PageBodyNormalText, "&nbsp;" + pReferenceGroupName->getText());
						pOutputStream->OutputBR();
						pOutputStream->OutputFormattingCRLF();
					}
				}
			}
		}
		// Call Numbers: Institution/Call Number
		if (pSource->StartReferenceAddressEnum(&lCookie))
		{
			ReferenceAddress *pReferenceAddress;
			while (pSource->NextReferenceAddressEnum(&pReferenceAddress, &lCookie))
			{
				if (!pReferenceAddress->isEmpty())
				{
					CString strInstitution = pReferenceAddress->getInstitution();
					if (0 != strInstitution.GetLength())
					{
						pOutputStream->OutputSpan(E_PageBodySmallHeader, "Institution:");
						pOutputStream->OutputSpan(E_PageBodyNormalText, "&nbsp;" + strInstitution);
						pOutputStream->OutputBR();
						pOutputStream->OutputFormattingCRLF();
						long lCookieCallNumber;
						if (pReferenceAddress->StartReferenceCallNumberEnum(&lCookieCallNumber))
						{
							ReferenceCallNumber *pReferenceCallNumber;
							while (pReferenceAddress->NextReferenceCallNumberEnum(&pReferenceCallNumber, &lCookieCallNumber))
							{
								if (!pReferenceCallNumber->isEmpty())
								{
									pOutputStream->Output("&nbsp;&nbsp;&nbsp;&nbsp;");
									pOutputStream->OutputSpan(E_PageBodySmallHeader, "Call Number:");
									pOutputStream->OutputSpan(E_PageBodyNormalText, "&nbsp;" + pReferenceCallNumber->getText());
									pOutputStream->OutputBR();
									pOutputStream->OutputFormattingCRLF();
								}
							}
						}
					}
					else
					{
						m_pStatus->Log("Empty reference source institution with non-empty call number!", LogLevel_Severe);
					}
				}
			}
		}
		// Classification
		if (pCitation->StartClassificationEnum(&lCookie))
		{
			ReferenceClassification *pClassification;
			while (pCitation->NextClassificationEnum(&pClassification, &lCookie))
			{
				if (!pClassification->isEmpty())
				{
					pOutputStream->OutputSpan(E_PageBodySmallHeader, "Classification:");
					strWork.Format(" %s: %s", pClassification->getType(), pClassification->getSubType());
					pOutputStream->OutputSpan(E_PageBodyNormalText, strWork);
					pOutputStream->OutputBR();
					pOutputStream->OutputFormattingCRLF();
				}
			}
		}
		// Primary Geography
		Place *pPlace = pCitation->getPlace();
		if (!pPlace->isEmpty())
		{
			pOutputStream->OutputSpan(E_PageBodySmallHeader, "Location:");
			pOutputStream->OutputSpan(E_PageBodyNormalText, "&nbsp;" + pPlace->getPlace());
			pOutputStream->OutputBR();
			pOutputStream->OutputFormattingCRLF();
		}
		// Start/End Time
		if (pCitation->StartDateRangeEnum(&lCookie))
		{
			DateRange *pDateRange;
			while (pCitation->NextDateRangeEnum(&pDateRange, &lCookie))
			{
				if (!pDateRange->isEmpty())
				{
					pOutputStream->OutputSpan(E_PageBodySmallHeader, "Date Range:");
					pOutputStream->OutputSpan(E_PageBodyNormalText, "&nbsp;" + pDateRange->getDateRange());
					pOutputStream->OutputBR();
					pOutputStream->OutputFormattingCRLF();
				}
			}
		}

		pOutputStream->Output("<hr width=\"100%\">");
		pOutputStream->OutputFormattingCRLF();

		// For all entries of the current reference
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
			long lReferenceEntryId = pReferenceEntry->GetReferenceEntryIdAsLong();
			ReferenceHeader *pHeader = pReferenceEntry->GetHeader();
			ReferenceBody *pBody = pReferenceEntry->GetBody();
			ReferenceComment *pComment = pReferenceEntry->GetComment();
			SeeAlso *pSeeAlso = pReferenceEntry->getSeeAlso();

			// Top of Reference Entry Internal Link Tag
			strWork.Format("<A name=\"REF%dENT%d\">", m_lReferenceId, lReferenceEntryId);
			pOutputStream->Output(strWork);
			pOutputStream->OutputFormattingCRLF();

			// Entry Title and Description
			FormsHtmlShared::OutputReferenceEntryDescription(m_pConfiguration, pReferenceEntry, pOutputStream);

			// Document Entry Number
			pOutputStream->OutputSpan(E_PageBodySmallHeader, "Document Entry Number:");
			strWork.Format(" %d", lReferenceEntryId);
			pOutputStream->OutputSpan(E_PageBodyNormalText, strWork);
			pOutputStream->OutputBR(2);
			pOutputStream->OutputFormattingCRLF();

			if (pReferenceEntry->hasPrivateAccess() && m_pConfiguration->getSuppressLiving())
			{
				pOutputStream->OutputSpan(E_PageBodyNormalText, "<b>The content of this document entry has been deemed <i>private</i>.</b><br>This simply means that the family feels it is of a private or sacred nature.<br>However, we would love to share the document entry with anyone who has an honest interest in it.<br>If you are interested in viewing the content of this document entry, email us at the address provided on the sidebar explaining who you are and what your interest in the document entry is.");
				pOutputStream->OutputBR(2);
				pOutputStream->OutputFormattingCRLF();
				// End the Entry with a HR
				pOutputStream->Output("<hr>");
				// Do not output any of the entry guts!
				continue;
			}

			if (pReferenceEntry->hasCopyRightedAccess() && m_pConfiguration->getSuppressLiving())
			{
				pOutputStream->OutputSpan(E_PageBodyNormalText, "<b>The content of this document entry is (or may be) <i>copyrighted</i>.</b><br>If you cannot locate the resource using the location details, you are welcome to contact us, using the email address on the sidebar, and we may be able to help you.");
				pOutputStream->OutputBR(2);
				pOutputStream->OutputFormattingCRLF();
				// End the Entry with a HR
				pOutputStream->Output("<hr>");
				// Do not output any of the entry guts!
				continue;
			}
			

			if (!pHeader->isEmpty())
			{
				// Header Header
				pOutputStream->OutputSpan(E_PageBodyMediumHeader, "Information:");
				pOutputStream->OutputBR();
				pOutputStream->OutputFormattingCRLF();
				// Volume Numbers
				if (0 != pHeader->getVolumeNumbers().GetLength())
				{
					pOutputStream->OutputSpan(E_PageBodySmallHeader, "Volume:");
					pOutputStream->OutputSpan(E_PageBodyNormalText, "&nbsp;" + pHeader->getVolumeNumbers());
					pOutputStream->OutputBR();
					pOutputStream->OutputFormattingCRLF();
				}
				// Chapter Numbers
				if (0 != pHeader->getChapterNumbers().GetLength())
				{
					pOutputStream->OutputSpan(E_PageBodySmallHeader, "Chapter:");
					pOutputStream->OutputSpan(E_PageBodyNormalText, "&nbsp;" + pHeader->getChapterNumbers());
					pOutputStream->OutputBR();
					pOutputStream->OutputFormattingCRLF();
				}
				// Page Numbers
				if (0 != pHeader->getPageNumbers().GetLength())
				{
					pOutputStream->OutputSpan(E_PageBodySmallHeader, "Page:");
					pOutputStream->OutputSpan(E_PageBodyNormalText, "&nbsp;" + pHeader->getPageNumbers());
					pOutputStream->OutputBR();
					pOutputStream->OutputFormattingCRLF();
				}
				// Entry Numbers
				if (0 != pHeader->getEntryNumbers().GetLength())
				{
					pOutputStream->OutputSpan(E_PageBodySmallHeader, "Entry:");
					pOutputStream->OutputSpan(E_PageBodyNormalText, "&nbsp;" + pHeader->getEntryNumbers());
					pOutputStream->OutputBR();
					pOutputStream->OutputFormattingCRLF();
				}
				// Header Paragraphs
				for(WORD nth=1; nth <= pHeader->getParagraphCount(); nth++)
				{
					Paragraph *pParagraph = pHeader->getParagraph(nth);
					if (pParagraph)
					{
						pOutputStream->OutputParagraph(m_pConfiguration, pParagraph, true, true, NULL, m_lReferenceId, iEntry);
					}
				}
				pOutputStream->OutputBR();
				pOutputStream->OutputFormattingCRLF();
			}

			if (!pBody->isEmpty())
			{
				// Body Header
				pOutputStream->OutputSpan(E_PageBodyMediumHeader, "Transcription:");
				pOutputStream->OutputBR();
				pOutputStream->OutputFormattingCRLF();
				// Header Paragraphs
				WORD nth = 1;
				int iObjectType;
				while (true)
				{
					CObject *pObject = pBody->getNthObject(nth, &iObjectType);
					if (!pObject)
					{	// done with all objects
						break;
					}
					Paragraph *pParagraph;
					Table *pTable;
					switch (iObjectType)
					{
						case REFERENCEBODY_TYPE_PARAGRAPH:
							pParagraph = (Paragraph *)pObject;
							pOutputStream->OutputParagraph(m_pConfiguration, pParagraph, true, true, NULL, m_lReferenceId, iEntry);
							break;
						case REFERENCEBODY_TYPE_TABLE:
							pTable = (Table *)pObject;
							pOutputStream->OutputTable(m_pConfiguration, pTable, m_pStatus, m_lReferenceId, iEntry);
							break;
					}
					nth++;
				}
				pOutputStream->OutputBR();
				pOutputStream->OutputFormattingCRLF();
			}

			if (!pComment->isEmpty())
			{
				// Comment Header
				pOutputStream->OutputSpan(E_PageBodyMediumHeader, "Editor's Comments About Entry:");
				pOutputStream->OutputBR();
				pOutputStream->OutputFormattingCRLF();
				// Comment Paragraphs
				for(WORD nth=1; nth <= pComment->getParagraphCount(); nth++)
				{
					Paragraph *pParagraph = pComment->getParagraph(nth);
					if (pParagraph)
					{
						pOutputStream->OutputParagraph(m_pConfiguration, pParagraph, true, true, NULL, m_lReferenceId, iEntry);
					}
				}
				pOutputStream->OutputBR();
				pOutputStream->OutputFormattingCRLF();
			}

			// Output the SeeAlso
			if (!pSeeAlso->isEmpty())
			{
				strWork = FormsHtmlShared::BuildSeeAlsoString(m_pConfiguration, pSeeAlso, NULL);
				if (0 != strWork.GetLength())
				{
					pOutputStream->OutputSpan(E_PageBodyMediumHeader, "See Also:");
					pOutputStream->OutputBR();
					pOutputStream->Output(strWork);
					pOutputStream->OutputBR();
					pOutputStream->OutputFormattingCRLF();
				}
			}

			// Ouput the Tag Group
			// Tag Group Header
			pOutputStream->OutputSpan(E_PageBodyMediumHeader, "Associated Persons and Marriages:");
			pOutputStream->OutputBR();
			pOutputStream->OutputFormattingCRLF();

			IndexTags *pReferenceTagIndex = m_pIndex->GetIndexReferenceTags();
			IndexMarriageToSpouses *pIndexMarriageToSpouses = m_pIndex->GetIndexMarriageToSpouses();

			IndexPersonTagEntry thePersonTagEntry;
			IndexMarriageTagEntry theMarriageTagEntry;

			// Get tags for persons first
			if (pReferenceTagIndex->StartPersonTagDescriptorByReferenceEnum(m_lReferenceId, &lCookie))
			{	// For each Tag associated with the current reference
				while(pReferenceTagIndex->NextPersonTagDescriptorByReferenceEnum(&thePersonTagEntry, m_lReferenceId, &lCookie))
				{
					if (lReferenceEntryId == thePersonTagEntry.GetContainingObjectSubId())
					{
						Person *pReferencePerson = m_pPersonList->get(thePersonTagEntry.GetPersonId());
						if (pReferencePerson)
						{	// Show <Person Name> (ID# 4535) BornD, MEDIUM [Goto Data]
							FormsPersonAccessor referencePersonAccessor(pReferencePerson, m_pConfiguration->getSuppressLiving());
							pOutputStream->Output("<span class=\"pageBodyNormalLink\">");
							// Build href to PersonInfo and show <Person Name>
							strHRef.Format("%s%s/%s%d.htm", m_pConfiguration->getBasePath(), PERINFODIR, PERINFOFILENAME, thePersonTagEntry.GetPersonId());
							pOutputStream->OutputStartAnchor(strHRef);
							pOutputStream->Output(referencePersonAccessor.getFullBasicName());
							pOutputStream->OutputEndAnchor();
							// Show (ID# 4535) BornD, MEDIUM
							strWork.Format("&nbsp;(Id#&nbsp;%d)&nbsp;%s,&nbsp;%s&nbsp;", thePersonTagEntry.GetPersonId(), thePersonTagEntry.GetTagType(), thePersonTagEntry.GetQuality());
							pOutputStream->Output(strWork);
							// Show [Goto Data]
							if (thePersonTagEntry.GetEmbedded())
							{
								strHRef.Format("#C%dSC%dPID%dT%s", m_lReferenceId, lReferenceEntryId, thePersonTagEntry.GetPersonId(), thePersonTagEntry.GetTagType());
								pOutputStream->OutputStartAnchor(strHRef);
								pOutputStream->Output("&nbsp;[Goto Data Point]");
								pOutputStream->OutputEndAnchor();
							}
							pOutputStream->Output("</span>");
							pOutputStream->OutputBR();
							pOutputStream->OutputFormattingCRLF();
						}
					}
				}
			}

			// Get tags for marriages next
			if (pReferenceTagIndex->StartMarriageTagDescriptorByReferenceEnum(m_lReferenceId, &lCookie))
			{	// For each Tag associated with the current reference
				while(pReferenceTagIndex->NextMarriageTagDescriptorByReferenceEnum(&theMarriageTagEntry, m_lReferenceId, &lCookie))
				{
					if (lReferenceEntryId == theMarriageTagEntry.GetContainingObjectSubId())
					{
						Marriage *pReferenceMarriage = m_pMarriageList->get(theMarriageTagEntry.GetMarriageId());
						if (pReferenceMarriage)
						{
							PERSONID husbandId, wifeId;
							// Load the two spouses
							if (pIndexMarriageToSpouses->FindSpousesGivenMarriageId(theMarriageTagEntry.GetMarriageId(), &husbandId, &wifeId))
							{
								Person *pHusbandPerson = m_pPersonList->get(husbandId);
								Person *pWifePerson = m_pPersonList->get(wifeId);
								if (pHusbandPerson && pWifePerson)
								{	// Show <Persons Name> (MID# 4535) MarrD, MEDIUM [Goto Data]
									FormsPersonAccessor referenceHusbandAccessor(pHusbandPerson, m_pConfiguration->getSuppressLiving());
									FormsPersonAccessor referenceWifeAccessor(pWifePerson, m_pConfiguration->getSuppressLiving());
									pOutputStream->Output("<span class=\"pageBodyNormalLink\">");
									// Build href to FGS and show <Person Name>
									strHRef.Format("%sfgs/%s%d.htm", m_pConfiguration->getBasePath(), FGSFILENAME, theMarriageTagEntry.GetMarriageId());
									pOutputStream->OutputStartAnchor(strHRef);
									strWork.Format("%s&nbsp;and&nbsp;%s", referenceHusbandAccessor.getFullBasicName(), referenceWifeAccessor.getFullBasicName());
									pOutputStream->Output(strWork);
									pOutputStream->OutputEndAnchor();
									// Show (MID# 4535) MarrD, MEDIUM
									strWork.Format("&nbsp;(Id#&nbsp;%d)&nbsp;%s,&nbsp;%s&nbsp;", theMarriageTagEntry.GetMarriageId(), theMarriageTagEntry.GetTagType(), theMarriageTagEntry.GetQuality());
									pOutputStream->Output(strWork);
									// Show [Goto Data]
									if (theMarriageTagEntry.GetEmbedded())
									{
										strHRef.Format("#C%dSC%dMID%dT%s", m_lReferenceId, lReferenceEntryId, theMarriageTagEntry.GetMarriageId(), theMarriageTagEntry.GetTagType());
										pOutputStream->OutputStartAnchor(strHRef);
										pOutputStream->Output("&nbsp;[Goto Data Point]");
										pOutputStream->OutputEndAnchor();
									}
									pOutputStream->Output("</span>");
									pOutputStream->OutputBR();
									pOutputStream->OutputFormattingCRLF();
								}
							}
						}
					}
				}
			}

			// End the Entry with a HR
			pOutputStream->Output("<hr>");
		}

		// If we saw a different number of entries than we should have seen,
		// log the error and continue.
		if (iEntryCounted != iEntryCount)
		{
			strWork.Format("Misnumbered reference entries. Total entries: %d, Counted: %d!", iEntryCount, iEntryCounted);
			m_pStatus->Log(strWork, LogLevel_Severe);
		}

		pOutputStream->OutputSidebarBackEnd();
	}
	if (pOutputStream)
	{
		delete pOutputStream;
	}
	m_pStatus->UpdateStatus("Completed Reference: " + strFileName);
}


 
  
 */
