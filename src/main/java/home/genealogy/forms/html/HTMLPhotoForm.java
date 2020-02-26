package home.genealogy.forms.html;

import home.genealogy.configuration.CFGFamily;
import home.genealogy.indexes.IndexMarriageToSpouses;
import home.genealogy.indexes.IndexPersonToMarriages;
import home.genealogy.indexes.IndexPersonToParentMarriage;
import home.genealogy.indexes.IndexPersonsToReferences;
import home.genealogy.lists.MarriageList;
import home.genealogy.lists.PersonList;
import home.genealogy.lists.PhotoList;
import home.genealogy.lists.ReferenceList;
import home.genealogy.output.IOutputStream;
import home.genealogy.schema.all.Caption;
import home.genealogy.schema.all.Commentary;
import home.genealogy.schema.all.Entry;
import home.genealogy.schema.all.Marriage;
import home.genealogy.schema.all.MarriageTag;
import home.genealogy.schema.all.MarriageTagType;
import home.genealogy.schema.all.Paragraph;
import home.genealogy.schema.all.Person;
import home.genealogy.schema.all.PersonTag;
import home.genealogy.schema.all.PersonTagType;
import home.genealogy.schema.all.Photo;
import home.genealogy.schema.all.PublishedIn;
import home.genealogy.schema.all.Reference;
import home.genealogy.schema.all.ReferenceEntryId;
import home.genealogy.schema.all.Singleton;
import home.genealogy.schema.all.helpers.CaptionHelper;
import home.genealogy.schema.all.helpers.CommentaryHelper;
import home.genealogy.schema.all.helpers.EntryHelper;
import home.genealogy.schema.all.helpers.FileHelper;
import home.genealogy.schema.all.helpers.MarriageHelper;
import home.genealogy.schema.all.helpers.MarriageIdHelper;
import home.genealogy.schema.all.helpers.MarriageTagHelper;
import home.genealogy.schema.all.helpers.PersonHelper;
import home.genealogy.schema.all.helpers.PersonIdHelper;
import home.genealogy.schema.all.helpers.PersonTagHelper;
import home.genealogy.schema.all.helpers.PhotoHelper;
import home.genealogy.schema.all.helpers.PhotoIdHelper;
import home.genealogy.schema.all.helpers.PublishedInHelper;
import home.genealogy.schema.all.helpers.ReferenceEntryIdHelper;
import home.genealogy.schema.all.helpers.ReferenceHelper;
import home.genealogy.schema.all.helpers.ReferenceIdHelper;
import home.genealogy.schema.all.helpers.SingletonHelper;
import home.genealogy.util.CommandLineParameterList;

import java.io.File;
import java.util.Iterator;
import java.util.List;

public class HTMLPhotoForm 
{
	public static final String PHOTOWRAPPER_FILE_SYSTEM_SUBDIRECTORY = "photowrappers";
	
	private CFGFamily m_family;
	private PersonList m_personList;
	private MarriageList m_marriageList;
	private ReferenceList m_referenceList;
	private PhotoList m_photoList;
	private IndexMarriageToSpouses m_indexMarrToSpouses;
	private boolean m_bSuppressLiving;
	private CommandLineParameterList m_listCLP;
	private IOutputStream m_outputStream;
	  
	public HTMLPhotoForm(CFGFamily family,
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
		m_referenceList = referenceList;
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
		// Make sure the "photo" subdirectory exists under
		// the base output file system path
		File fSubdirectory = new File(strOutputPath + PHOTOWRAPPER_FILE_SYSTEM_SUBDIRECTORY);
		if (!fSubdirectory.exists())
		{
			if (!fSubdirectory.mkdirs())
			{
				throw new Exception("Error creating sub-directory tree for photo wrapper files!");
			}
		}		
		
		// Create necessary Indexes
		IndexPersonToParentMarriage idxPerToParentMar = new IndexPersonToParentMarriage(m_family, m_personList);
		IndexPersonToMarriages idxPerToMar = new IndexPersonToMarriages(m_family, m_marriageList);
		IndexMarriageToSpouses idxMarToSpouses = new IndexMarriageToSpouses(m_family, m_marriageList);
		IndexPersonsToReferences idxPerToRef = new IndexPersonsToReferences(m_family, m_personList, m_referenceList);
		
		Iterator<Photo> iter = m_photoList.getPhotos();
		while (iter.hasNext())
		{
			Photo thisPhoto = iter.next();
			createPhoto(m_family, m_personList, m_marriageList, m_referenceList,
						         m_photoList, m_bSuppressLiving, idxPerToParentMar,
						         idxPerToMar, idxMarToSpouses, strOutputPath, thisPhoto);
		}
		m_outputStream.output("Completed Generating All Photo Wrapper Files\n");
	}


	private void createPhoto(CFGFamily family,
				 PersonList personList,
				 MarriageList marriageList,
				 ReferenceList referenceList,
				 PhotoList photoList,
				 boolean bSuppressLiving,
				 IndexPersonToParentMarriage idxPerToParentMar,
				 IndexPersonToMarriages idxPerToMar,
				 IndexMarriageToSpouses idxMarToSpouses,
				 String strOutputPath,
				 Photo photo)
		throws Exception
	{
		int iPhotoId = PhotoHelper.getPhotoId(photo);
		String strFileName = strOutputPath + PHOTOWRAPPER_FILE_SYSTEM_SUBDIRECTORY + "\\" + HTMLShared.PHOTOWRAPFILENAME + iPhotoId + ".htm";
		m_outputStream.output("Generating Photo File: " + strFileName+ "\n");
		HTMLFormOutput output = new HTMLFormOutput(strFileName);
		output.outputSidebarFrontEnd("Photo Number: " + iPhotoId, m_family, personList, marriageList);

		// Top of Document Internal Link Tag
		output.output("<A name=InfoTop></A>");
		output.outputCRLF();
	
		// Top of Page Title
		List<Paragraph> lDescription = PhotoHelper.getDescription(photo);
		for (int i=0; i<lDescription.size(); i++)
		{
			String strParagraph = HTMLShared.buildParagraphString(m_family, m_listCLP,
					lDescription.get(i), m_personList, m_marriageList,
                    m_referenceList, m_photoList,
                    m_indexMarrToSpouses,
                    true,
                    true,
                    m_bSuppressLiving,
                    new HTMLParagraphFormat("pageTopHeader"),
                    iPhotoId,
                    PhotoIdHelper.PHOTOID_INVALID);
			output.output(strParagraph);
		}
		output.outputBR(2);
		output.outputCRLF();
		
		output.output("<hr width=\"100%\">");
		
		// If the photo has PRIVATE access, do not show details
		String strAccess = PhotoHelper.getAccess(photo);
		
		if (strAccess.equals("PRIVATE") && m_bSuppressLiving)
		{
			output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyNormalText, "<b>The content of this photo has been deemed <i>private</i>.</b><br>This simply means that the family feels it is of a private or sacred nature.<br>However, we would love to share the photo with anyone who has an honest interest in it.<br>If you are interested in viewing the content of this photo, email us at the address provided on the sidebar explaining who you are and what your interest in the photo is.");
			output.outputBR(2);
			output.outputCRLF();
		}
		else
		{	// access is PUBLIC or COPYRIGHTED, show everything if PUBLIC, parts if COPYRIGHTED
			if (!(strAccess.equals("COPYRIGHTED")) || !m_bSuppressLiving)
			{
				// Start Photo Frame Table
				output.output("<center>");
				output.output("<table valign=\"center\">");
				output.output("<tr>");
				output.output("<td></td>");
				output.output("<td>");
				output.output("<table border=\"10\" bordercolor=\"#660000\" cellpadding=20 bgcolor=\"#dbcbaa\">");
				output.output("<tr>");
				output.output("<td>");

				// Actual Link to Photo File
				String strSmallestWebbableFileName = PhotoHelper.getSmallestWebbableFile(photo);
				strSmallestWebbableFileName = HTMLShared.replaceBkSlashWithPhotoFileNameSeparator(strSmallestWebbableFileName);
				if (0 != strSmallestWebbableFileName.length())
				{
					strSmallestWebbableFileName = strSmallestWebbableFileName.toLowerCase();
					String strUrl = "<img border=\"0\" src=\"" + m_family.getUrlPrefix() + "photos/" + strSmallestWebbableFileName + "\" alt=\" Photo Id: " + iPhotoId + "\">"; 
					output.output(strUrl);
				}
				else
				{	// Log error!
					m_outputStream.output("No Webbable Photo is available!\n");
				}
				// End Photo Frame Table 
				output.output("</table>");
				output.output("</td>");
				output.output("</tr>");
				output.output("</table>");
				output.output("</center>");
				output.outputBR();

				// Show the available photo files
				List<home.genealogy.schema.all.File> lFiles = PhotoHelper.getFiles(photo);
				String strPackage = PhotoHelper.getPackage(photo);
				String strBaseFileName = PhotoHelper.getBaseFileName(photo);
				output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyMediumHeader, "Available Versions of the Photo:");
				output.outputBR();
				output.outputCRLF();
				for (int f=0; f<lFiles.size(); f++)
				{
					home.genealogy.schema.all.File file = lFiles.get(f);
					FileHelper.eFileType eType = FileHelper.getType(file);
					if ((eType == FileHelper.eFileType.eJPG) ||
						(eType == FileHelper.eFileType.eGIF) ||
						(eType == FileHelper.eFileType.ePNG))
					{
						output.output("<span class=\"pageBodyNormalLink\">");
						String strPhotoFileName = "photos\\" + strPackage + "\\" + strBaseFileName + FileHelper.getUniqueId(file) + "." + FileHelper.getTypeExtension(file);
						strPhotoFileName = HTMLShared.replaceBkSlashWithPhotoFileNameSeparator(strPhotoFileName);
						strPhotoFileName = strPhotoFileName.toLowerCase();
						output.outputStartAnchor(m_family.getUrlPrefix() + strPhotoFileName);
						String strWork = "Type: " + FileHelper.getType(file) + ", Size: " + FileHelper.getSize(file) + " (KBytes)";
						output.output(strWork);
						output.outputEndAnchor();
						output.output("</span>");
						output.outputBR();
						output.outputCRLF();
					}
				}
				output.outputBR();
				output.outputCRLF();
			}
			// If the photo has COPYRIGHTED access, do not show details
			else if (strAccess.equals("COPYRIGHTED") && m_bSuppressLiving)
			{
				output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyNormalText, "<b>This photo is (or may be) <i>copyrighted</i>.</b><br>If you cannot locate the resource using the location details, you are welcome to contact us, using the email address on the sidebar, and we may be able to help you.");
				output.outputBR(2);
				output.outputCRLF();
			}
			
			// Starts display for both PUBLIC and COPYRIGHTED photos
			// If the photo has an "publishedIn" entry, then link to the containing reference entries
			if (PhotoHelper.containsPublishedIn(photo))
			{
				output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyMediumHeader, "This Photo was Published in:");
				output.outputBR(2);
				output.outputCRLF();
				
				PublishedIn pi = PhotoHelper.getPublishedIn(photo);
				int iReferenceEntryCount = PublishedInHelper.getReferenceEntryIdCount(pi);
				for (int r=0; r<iReferenceEntryCount; r++)
				{
					ReferenceEntryId referenceEntryId = PublishedInHelper.getReferenceEntryId(pi, r);
					if (null != referenceEntryId)
					{
						int iRefId = ReferenceEntryIdHelper.getReferenceId(referenceEntryId);
						int iRefEntryId = ReferenceEntryIdHelper.getReferenceEntryId(referenceEntryId);
						if (iRefId != ReferenceIdHelper.REFERENCEID_INVALID)
						{
							Reference reference = m_referenceList.get(iRefId);
							if (null != reference)
							{
//								ReferenceCitation *pCitation = pReference->getReferenceCitation();
//								ReferenceSource *pSource = pCitation->getReferenceSource();
//								ReferenceAuthor *pAuthor = pSource->getReferenceAuthor();

								// Document Title
								String strHRef = m_family.getUrlPrefix() + "references/" + HTMLShared.REFERENCEFILENAME + iRefId + ".htm";
								output.outputStandardBracketedLink(strHRef, "View Document");
								output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyNormalText, ReferenceHelper.getTitle(reference));
								output.outputBR();
								output.outputCRLF();

								// Document Number
								String strWork = "Document Number: " + iRefId;
								output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodySmallText, strWork);
								output.outputBR();
								output.outputCRLF();

								// Entry Description
								Entry entry = ReferenceHelper.getEntry(reference, (iRefEntryId - 1));
								if (null != entry)
								{
									// Entry Title and Description
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
					                            iRefId,
					                            iRefEntryId);
										output.output(strParagraph);
										output.outputCRLF();
									}
								}
								output.outputBR();
							}
						}
					}
					output.outputBR();
				}
			}
			else if (PhotoHelper.containsSingleton(photo))
			{	// The photo has a singleton
				Singleton singleton = PhotoHelper.getSingleton(photo);
				String strDate = SingletonHelper.getDate(singleton);
				String strPlace = SingletonHelper.getPlace(singleton);
//				PhotoPhotographer *pPhotographer = pSingleton->GetPhotographer();
				Caption caption = SingletonHelper.getCaption(singleton);
				Commentary commentary = SingletonHelper.getCommentary(singleton);
				HTMLParagraphFormat paragraphFormat = new HTMLParagraphFormat("pageBodySmallText");
				
				boolean bDateEmpty = ((null == strDate) || (0 == strDate.length()));
				boolean bPlaceEmpty = ((null == strPlace) || (0 == strPlace.length()));
				boolean bInfoExists = (!CaptionHelper.isEmpty(caption) ||
						              (!CommentaryHelper.isEmpty(commentary)) ||
						              !bDateEmpty ||
						              !bPlaceEmpty);
				if (bInfoExists)
				{
					output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyMediumHeader, "Photo Information:");
					output.outputBR();
					output.outputCRLF();
				}

				if (!CaptionHelper.isEmpty(caption))
				{
					output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodySmallHeader, "Inscription on the backside of the photo:");
					output.outputBR();
					String strCaption = HTMLShared.buildParagraphListObject(m_family, m_listCLP,
							  CaptionHelper.getParagraphs(caption), m_personList, m_marriageList,
							  m_referenceList, m_photoList,
							  m_indexMarrToSpouses,
							  true,
							  true,
							  m_bSuppressLiving,
							  paragraphFormat,
							  PhotoHelper.getPhotoId(photo),
							  0);
					output.output(strCaption);
					output.outputBR();
					output.outputCRLF();
				}
	//			else
	//			{
	//				output.outputSpan(E_PageBodySmallHeader, "There was no inscription on the backside of the photo.");
	//				output.outputBR();
	//				output.outputFormattingCRLF();
	//			}

				if (!bDateEmpty)
				{
					output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodySmallHeader, "Date:");
					output.output("&nbsp;");
					output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodySmallText, strDate);
					output.outputBR();
					output.outputCRLF();
				}
				if (!bPlaceEmpty)
				{
					output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodySmallHeader, "Place:");
					output.output("&nbsp;");
					output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodySmallText, strPlace);
					output.outputBR();
					output.outputCRLF();
				}

				if (!CommentaryHelper.isEmpty(commentary))
				{
					output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodySmallHeader, "Editor's Comments:");
					output.outputBR();
					String strCommentary = HTMLShared.buildParagraphListObject(m_family, m_listCLP,
							  CommentaryHelper.getParagraphs(commentary), m_personList, m_marriageList,
							  m_referenceList, m_photoList,
							  m_indexMarrToSpouses,
							  true,
							  true,
							  m_bSuppressLiving,
							  paragraphFormat,
							  PhotoHelper.getPhotoId(photo),
							  0);
					output.output(strCommentary);
				}

				if (bInfoExists)
				{
					output.outputBR();
					output.outputCRLF();
				}
				
				// Output the SeeAlso
				if (0 != PhotoHelper.getSeeAlsoObjectCount(photo))
				{
					String strSeeAlso = HTMLShared.buildSeeAlsoString(PhotoHelper.getSeeAlso(photo), family, m_listCLP,
																	  m_personList, m_marriageList, m_referenceList,
																	  m_photoList, null, m_indexMarrToSpouses);
					if (0 != strSeeAlso.length())
					{
						output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyMediumHeader, "See Also:");
						output.outputBR();
						output.output(strSeeAlso);
						output.outputBR(2);
						output.outputCRLF();
					}
				}
				
				// Tag Groups
				output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyMediumHeader, "Associated Persons and Marriages:");
				output.outputBR();
				output.outputCRLF();
				
				List<PersonTag> lPersonTags = SingletonHelper.getPersonTagList(singleton);
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
								PersonHelper taggedPersonHelper = new PersonHelper(taggedPerson, bSuppressLiving);
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
									// Show [Goto Data]
		//							if (thePersonTagEntry.GetEmbedded())
		//							{
		//								strHRef.Format("#C%dSC%dPID%dT%s", m_lReferenceId, lReferenceEntryId, thePersonTagEntry.GetPersonId(), thePersonTagEntry.GetTagType());
		//								output.outputStartAnchor(strHRef);
		//								output.output("&nbsp;[Goto Data Point]");
		//								output.outputEndAnchor();
		//							}
									output.output("</span>");
									output.outputBR();
									output.outputCRLF();
								}
							}
						}
					}
				}
				List<MarriageTag> lMarriageTags = SingletonHelper.getMarriageTagList(singleton);
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
									PersonHelper husbandHelper = new PersonHelper(husband, bSuppressLiving);
									PersonHelper wifeHelper = new PersonHelper(wife, bSuppressLiving);
									MarriageHelper taggedMarriageHelper = new MarriageHelper(taggedMarriage, husbandHelper, wifeHelper, bSuppressLiving);
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
										// Show [Goto Data]
			//							if (thePersonTagEntry.GetEmbedded())
			//							{
			//								strHRef.Format("#C%dSC%dPID%dT%s", m_lReferenceId, lReferenceEntryId, thePersonTagEntry.GetPersonId(), thePersonTagEntry.GetTagType());
			//								output.outputStartAnchor(strHRef);
			//								output.output("&nbsp;[Goto Data Point]");
			//								output.outputEndAnchor();
			//							}
										output.output("</span>");
										output.outputBR();
										output.outputCRLF();
									}
								}
							}
						}
					}
				}
			}
			else
			{	// No singleton, No Published In - error
				m_outputStream.output("Found photo without a Singleton or a PublishedIn! Id: " + iPhotoId + "\n");
			}
		}
		output.outputSidebarBackEnd();
		output.commit();
		output = null;
	}

}




/*

FormsHtmlPhotoWrapper::FormsHtmlPhotoWrapper(Family *pFamily, FormsHtmlConfiguration *pConfiguration, long lPhotoId)
{
	m_pFamily = pFamily;
	m_lPhotoId = lPhotoId;
	m_pConfiguration = pConfiguration;
	m_pPersonList = m_pFamily->getPersonList();
	m_pMarriageList = m_pFamily->getMarriageList();
	m_pReferenceList = m_pFamily->getReferenceList();
	m_pPhotoList = m_pFamily->getPhotoList();
	m_pIndex = m_pFamily->getFamilyIndex();
	m_pStatus = pConfiguration->getStatus();
}


void FormsHtmlPhotoWrapper::Create()
{
	long lCookie;
	CString strWork, strHRef, strNumericTitle;
	CString strFileName;
	strFileName.Format("%sphotowrappers\\%s%d.htm", m_pConfiguration->getDestinationPath(), PHOTOWRAPFILENAME, m_lPhotoId); 
	m_pStatus->Log("Generating Photo Wrapper: " + strFileName, LogLevel_Finest);
	m_pStatus->UpdateStatus("Generating Photo Wrapper: " + strFileName);
	FormsHtmlOutputStream *pOutputStream = new FormsHtmlOutputStream(strFileName, m_pStatus);

	Photo *pPhoto = m_pPhotoList->get(m_lPhotoId);
	if (pPhoto)
	{
		strNumericTitle.Format("Photo Number: %d", m_lPhotoId);
		pOutputStream->OutputSidebarFrontEnd(strNumericTitle, m_pConfiguration);

		// Top of Document Internal Link Tag
		pOutputStream->Output("<A name=InfoTop></A>");
		pOutputStream->OutputFormattingCRLF();

		// Top of Page Title
		FormsHtmlParagraphFormat photoTitleFormat("pageTopHeader");
		pOutputStream->OutputParagraphListObject(pPhoto->getPhotoTitle(), m_pConfiguration, &photoTitleFormat);
		pOutputStream->OutputBR(2);
		pOutputStream->OutputFormattingCRLF();

		pOutputStream->Output("<hr width=\"100%\">");

		// If the photo has PRIVATE access, do not show details
		if (pPhoto->hasPrivateAccess() && m_pConfiguration->getSuppressLiving())
		{
			pOutputStream->OutputSpan(E_PageBodyNormalText, "<b>The content of this photo has been deemed <i>private</i>.</b><br>This simply means that the family feels it is of a private or sacred nature.<br>However, we would love to share the photo with anyone who has an honest interest in it.<br>If you are interested in viewing the content of this photo, email us at the address provided on the sidebar explaining who you are and what your interest in the photo is.");
			pOutputStream->OutputBR(2);
			pOutputStream->OutputFormattingCRLF();
		}		
		else
		{	// access is PUBLIC or COPYRIGHTED, show everything if PUBLIC, parts if COPYRIGHTED
			
			if (!pPhoto->hasCopyRightedAccess() || !m_pConfiguration->getSuppressLiving())
			{
				// Start Photo Frame Table
				pOutputStream->Output("<center>");
				pOutputStream->Output("<table valign=\"center\">");
				pOutputStream->Output("<tr>");
				pOutputStream->Output("<td></td>");
				pOutputStream->Output("<td>");
				pOutputStream->Output("<table border=\"10\" bordercolor=\"#660000\" cellpadding=20 bgcolor=\"#dbcbaa\">");
				pOutputStream->Output("<tr>");
				pOutputStream->Output("<td>");

				// Actual Link to Photo File
				CString strSmallestWebbableFileName = pPhoto->GetSmallestWebbableFile();
				strSmallestWebbableFileName = FormsHtmlShared::ReplaceBkSlashWithPhotoFileNameSeparator(strSmallestWebbableFileName);
				if (0 != strSmallestWebbableFileName.GetLength())
				{
					strSmallestWebbableFileName.MakeLower();
					strWork.Format("<img border=\"0\" src=\"%sphotos/%s\" alt=\"%s\">", m_pConfiguration->getBasePath(), strSmallestWebbableFileName, strNumericTitle);
					pOutputStream->Output(strWork);
				}
				else
				{	// Log error!
					m_pStatus->Log("No Webbable Photo is available!", LogLevel_Severe);
				}
				// End Photo Frame Table
				pOutputStream->Output("</td>");
				pOutputStream->Output("</tr>");
				pOutputStream->Output("</table>");
				pOutputStream->Output("</td>");
				pOutputStream->Output("</tr>");
				pOutputStream->Output("</table>");
				pOutputStream->Output("</center>");
				pOutputStream->OutputBR();

				// Show the avilable photo files
				PhotoFileList *pPhotoFileList = pPhoto->getFileList();
				CString strPackage = pPhotoFileList->GetPackage();
				CString strBaseFileName = pPhotoFileList->GetBaseFileName();
				PhotoFile *pFile;
				long lFileCookie;
				if (pPhotoFileList->StartWebbablePhotoFileEnum(&lFileCookie))
				{
					pOutputStream->OutputSpan(E_PageBodyMediumHeader, "Available Versions of the Photo:");
					pOutputStream->OutputBR();
					pOutputStream->OutputFormattingCRLF();

					while (pPhotoFileList->NextWebbablePhotoFileEnum(&pFile, &lFileCookie))
					{
						pOutputStream->Output("<span class=\"pageBodyNormalLink\">");
						CString strFileName = "photos\\" + strPackage + "\\" + strBaseFileName + pFile->GetUniqueId() + "." + pFile->GetType();
						strFileName = FormsHtmlShared::ReplaceBkSlashWithPhotoFileNameSeparator(strFileName);
						strFileName.MakeLower();
						pOutputStream->OutputStartAnchor(m_pConfiguration->getBasePath() + strFileName);
						strWork.Format("Type: %s, Size: %d (KBytes)", pFile->GetType(), pFile->GetSize());
						pOutputStream->Output(strWork);
						pOutputStream->OutputEndAnchor();
						pOutputStream->Output("</span>");
						pOutputStream->OutputBR();
						pOutputStream->OutputFormattingCRLF();
					}
					pOutputStream->OutputBR();
					pOutputStream->OutputFormattingCRLF();
				}
			}
			// If the photo has COPYRIGHTED access, do not show details
			else if (pPhoto->hasCopyRightedAccess() && m_pConfiguration->getSuppressLiving())
			{
				pOutputStream->OutputSpan(E_PageBodyNormalText, "<b>This photo is (or may be) <i>copyrighted</i>.</b><br>If you cannot locate the resource using the location details, you are welcome to contact us, using the email address on the sidebar, and we may be able to help you.");
				pOutputStream->OutputBR(2);
				pOutputStream->OutputFormattingCRLF();
			}
			// Starts display for both PUBLIC and COPYRIGHTED photos
			// If the photo has an "publishedIn" entry, then link to the containing reference entries
			if (pPhoto->containsPublishedIn())
			{
				pOutputStream->OutputSpan(E_PageBodyMediumHeader, "This Photo was Published in:");
				pOutputStream->OutputBR(2);
				pOutputStream->OutputFormattingCRLF();
				
				long lPublishedInCookie;
				ReferenceEntryId *pPublishedInRefEntryId;
				PhotoPublishedIn *pPublishedIn = pPhoto->getPublishedIn();
				if (pPublishedIn->startReferenceEntryIdEnum(&lPublishedInCookie))
				{
					while (pPublishedIn->nextReferenceEntryIdEnum(&lPublishedInCookie, &pPublishedInRefEntryId))
					{
						long lRefId = pPublishedInRefEntryId->getIdAsLong();
						long lRefEntId = pPublishedInRefEntryId->getEntryIdAsLong();
						if (lRefId != REFERENCEID_INVALID)
						{
							ReferenceList *pReferenceList = m_pFamily->getReferenceList();
							Reference *pReference = pReferenceList->get(lRefId);
							if (pReference)
							{
								ReferenceCitation *pCitation = pReference->getReferenceCitation();
								ReferenceSource *pSource = pCitation->getReferenceSource();
								ReferenceAuthor *pAuthor = pSource->getReferenceAuthor();

								// Document Title
								strHRef.Format("%sreferences/%s%d.htm", m_pConfiguration->getBasePath(), REFERENCEFILENAME, pReference->getReferenceIdAsLong());
								pOutputStream->OutputStandardBracketedLink(strHRef, "View Document");
								pOutputStream->OutputSpan(E_PageBodyNormalText, pReference->getTitle());
								pOutputStream->OutputBR();
								pOutputStream->OutputFormattingCRLF();

								// Document Number
								strWork.Format("Document Number: %d", pReference->getReferenceIdAsLong());
								pOutputStream->OutputSpan(E_PageBodySmallText, strWork);
								pOutputStream->OutputBR();
								pOutputStream->OutputFormattingCRLF();

								// Entry Description
								ReferenceEntry *pReferenceEntry = pReference->GetEntryWithId(lRefEntId);
								if (pReferenceEntry)
								{
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
							}
						}
						pOutputStream->OutputBR();
					}
				}
				pOutputStream->OutputBR();
			}
			else if (pPhoto->containsSingleton())
			{	// The photo has a singleton

				PhotoSingleton *pSingleton = pPhoto->getSingleton();
				Date *pDate = pSingleton->GetDate();
				Place *pPlace = pSingleton->GetPlace();
				PhotoPhotographer *pPhotographer = pSingleton->GetPhotographer();
				PhotoCaption * pCaption = pSingleton->GetCaption();
				PhotoComment *pComment = pSingleton->GetComment();
				FormsHtmlParagraphFormat paragraphFormat("pageBodySmallText");
				bool bInfoExists = (!pCaption->isEmpty() || !pDate->isEmpty() || !pPlace->isEmpty() || !pComment->isEmpty());

				if (bInfoExists)
				{
					pOutputStream->OutputSpan(E_PageBodyMediumHeader, "Photo Information:");
					pOutputStream->OutputBR();
					pOutputStream->OutputFormattingCRLF();
				}

				if (!pCaption->isEmpty())
				{
					pOutputStream->OutputSpan(E_PageBodySmallHeader, "Inscription on the backside of the photo:");
					pOutputStream->OutputBR();
					pOutputStream->OutputParagraphListObject(pCaption, m_pConfiguration, &paragraphFormat, m_lPhotoId, 0);
				}
	//			else
	//			{
	//				pOutputStream->OutputSpan(E_PageBodySmallHeader, "There was no inscription on the backside of the photo.");
	//				pOutputStream->OutputBR();
	//				pOutputStream->OutputFormattingCRLF();
	//			}

				if (!pDate->isEmpty())
				{
					pOutputStream->OutputSpan(E_PageBodySmallHeader, "Date:");
					pOutputStream->Output("&nbsp;");
					pOutputStream->OutputSpan(E_PageBodySmallText, pDate->getDate());
					pOutputStream->OutputBR();
					pOutputStream->OutputFormattingCRLF();
				}
				if (!pPlace->isEmpty())
				{
					pOutputStream->OutputSpan(E_PageBodySmallHeader, "Place:");
					pOutputStream->Output("&nbsp;");
					pOutputStream->OutputSpan(E_PageBodySmallText, pPlace->getPlace());
					pOutputStream->OutputBR();
					pOutputStream->OutputFormattingCRLF();
				}

				if (!pComment->isEmpty())
				{
					pOutputStream->OutputSpan(E_PageBodySmallHeader, "Editor's Comments:");
					pOutputStream->OutputBR();
					for (WORD i=1; true; i++)
					{
						Paragraph *pParagraph = pComment->getNthParagraph(i);
						if (NULL == pParagraph)
						{
							break;
						}
						pOutputStream->OutputParagraph(m_pConfiguration, pParagraph, true, true, &paragraphFormat, m_lPhotoId, 0);
						if (!pParagraph->shouldLineEnd())
						{
							pOutputStream->OutputBR();
						}
						pOutputStream->OutputFormattingCRLF();
					}
				}

				if (bInfoExists)
				{
					pOutputStream->OutputBR();
					pOutputStream->OutputFormattingCRLF();
				}

				// Output the SeeAlso
				SeeAlso *pSeeAlso = pPhoto->getSeeAlso();
				if (!pSeeAlso->isEmpty())
				{
					strWork = FormsHtmlShared::BuildSeeAlsoString(m_pConfiguration, pSeeAlso, NULL);
					if (0 != strWork.GetLength())
					{
						pOutputStream->OutputSpan(E_PageBodyMediumHeader, "See Also:");
						pOutputStream->OutputBR();
						pOutputStream->Output(strWork);
						pOutputStream->OutputBR(2);
						pOutputStream->OutputFormattingCRLF();
					}
				}

				// Ouput the Tag Group
				// Tag Group Header
				IndexTags *pPhotoTagIndex = m_pIndex->GetIndexPhotoTags();
				IndexMarriageToSpouses *pIndexMarriageToSpouses = m_pIndex->GetIndexMarriageToSpouses();

				IndexPersonTagEntry thePersonTagEntry;
				IndexMarriageTagEntry theMarriageTagEntry;

				// Put out a single header for both persons and marriages
				if ((pPhotoTagIndex->StartPersonTagDescriptorByReferenceEnum(m_lPhotoId, &lCookie)) ||
					(pPhotoTagIndex->StartMarriageTagDescriptorByReferenceEnum(m_lPhotoId, &lCookie)))
				{
					pOutputStream->OutputSpan(E_PageBodyMediumHeader, "Associated Persons and Marriages:");
					pOutputStream->OutputBR();
					pOutputStream->OutputFormattingCRLF();
				}

				// Get tags for persons first
				if (pPhotoTagIndex->StartPersonTagDescriptorByReferenceEnum(m_lPhotoId, &lCookie))
				{
					// For each Tag associated with the current reference
					while(pPhotoTagIndex->NextPersonTagDescriptorByReferenceEnum(&thePersonTagEntry, m_lPhotoId, &lCookie))
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
								strHRef.Format("#C%dSC0PID%dT%s", m_lPhotoId, thePersonTagEntry.GetPersonId(), thePersonTagEntry.GetTagType());
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

				// Get tags for marriages next
				if (pPhotoTagIndex->StartMarriageTagDescriptorByReferenceEnum(m_lPhotoId, &lCookie))
				{	// For each Tag associated with the current reference
					while(pPhotoTagIndex->NextMarriageTagDescriptorByReferenceEnum(&theMarriageTagEntry, m_lPhotoId, &lCookie))
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
										strHRef.Format("#C%dSC0MID%dT%s", m_lPhotoId, theMarriageTagEntry.GetMarriageId(), theMarriageTagEntry.GetTagType());
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
			else
			{	// No singleton, No Published In - error
				m_pStatus->Log("Found photo without a Singleton or a PublishedIn!", LogLevel_Finer);
			}
		}
		pOutputStream->OutputSidebarBackEnd();
	}
	if (pOutputStream)
	{
		delete pOutputStream;
	}
	m_pStatus->UpdateStatus("Photo Wrapper: " + strFileName);
}

*/