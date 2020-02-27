package home.genealogy.forms.html;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import home.genealogy.CommandLineParameters;
import home.genealogy.configuration.CFGFamily;
import home.genealogy.indexes.IndexMarriageToPhotos;
import home.genealogy.indexes.IndexMarriageToSpouses;
import home.genealogy.indexes.IndexMarriagesToReferences;
import home.genealogy.indexes.IndexPersonToMarriages;
import home.genealogy.indexes.IndexPersonToParentMarriage;
import home.genealogy.indexes.IndexPersonToPhotos;
import home.genealogy.indexes.IndexPersonsToReferences;
import home.genealogy.indexes.TaggedContainerDescriptor;
import home.genealogy.lists.MarriageList;
import home.genealogy.lists.PersonList;
import home.genealogy.lists.PhotoList;
import home.genealogy.lists.ReferenceList;
import home.genealogy.output.IOutputStream;
import home.genealogy.schema.all.Entry;
import home.genealogy.schema.all.Event;
import home.genealogy.schema.all.Marriage;
import home.genealogy.schema.all.Paragraph;
import home.genealogy.schema.all.Person;
import home.genealogy.schema.all.Photo;
import home.genealogy.schema.all.Reference;
import home.genealogy.schema.all.helpers.EntryHelper;
import home.genealogy.schema.all.helpers.EventHelper;
import home.genealogy.schema.all.helpers.MarriageIdHelper;
import home.genealogy.schema.all.helpers.PersonHelper;
import home.genealogy.schema.all.helpers.PersonIdHelper;
import home.genealogy.schema.all.helpers.PhotoHelper;
import home.genealogy.schema.all.helpers.ReferenceHelper;

public class HTMLPersonInfoForm
{
	public static final String PERSON_INFO_FILE_SYSTEM_SUBDIRECTORY = "perinfo";

	private static final CommandLineParameters commandLineParameters = null;
	
	private CFGFamily m_family;
	private PersonList m_personList;
	private MarriageList m_marriageList;
	private ReferenceList m_referenceList;
	private PhotoList m_photoList;
	private boolean m_bSuppressLiving;
	private boolean m_bSuppressLds;
	private CommandLineParameters m_commandLineParameters;
	private IOutputStream m_outputStream;
	  
	public HTMLPersonInfoForm(CFGFamily family,
							  PersonList personList,
							  MarriageList marriageList,
							  ReferenceList referenceList,
							  PhotoList photoList,
							  boolean bSuppressLiving,
							  boolean bSuppressLds,
							  CommandLineParameters commandLineParameters,
							  IOutputStream outputStream)
	{
		m_family = family;
		m_personList = personList;
		m_marriageList = marriageList;
		m_referenceList = referenceList;
		m_photoList = photoList;
		m_bSuppressLiving = bSuppressLiving;
		m_bSuppressLds = bSuppressLds;
		m_commandLineParameters = commandLineParameters;
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
		// Make sure the "perinfo" subdirectory exists under
		// the base output file system path
		File fSubdirectory = new File(strOutputPath + PERSON_INFO_FILE_SYSTEM_SUBDIRECTORY);
		if (!fSubdirectory.exists())
		{
			if (!fSubdirectory.mkdirs())
			{
				throw new Exception("Error creating sub-directory tree for person info files!");
			}
		}		
		
		// Create necessary Indexes
		IndexPersonToParentMarriage idxPerToParentMar = new IndexPersonToParentMarriage(m_family, m_personList);
		IndexPersonToMarriages idxPerToMar = new IndexPersonToMarriages(m_family, m_marriageList);
		IndexMarriageToSpouses idxMarToSpouses = new IndexMarriageToSpouses(m_family, m_marriageList);
		IndexPersonToPhotos idxPerToPhoto = new IndexPersonToPhotos(m_family, m_personList, m_photoList);
		IndexMarriageToPhotos idxMarToPhoto = new IndexMarriageToPhotos(m_family, m_marriageList, m_photoList);
		IndexPersonsToReferences idxPerToReference = new IndexPersonsToReferences(m_family, m_personList, m_referenceList);
		IndexMarriagesToReferences idxMarToReference = new IndexMarriagesToReferences(m_family, m_personList, m_marriageList, m_referenceList);
		
		Iterator<Person> iter = m_personList.getPersons();
		while (iter.hasNext())
		{
			Person thisPerson = iter.next();
			createPersonInfo(m_family, m_personList, m_marriageList, m_referenceList,
 					         m_photoList, m_bSuppressLiving, idxPerToParentMar,
 					         idxPerToMar, idxMarToSpouses, idxPerToPhoto, idxMarToPhoto,
 					         idxPerToReference, idxMarToReference, strOutputPath, thisPerson);
		}
		m_outputStream.output("Completed Generating All Person Info Files\n");	
	}
	
	private void createPersonInfo(CFGFamily family,
			  					 PersonList personList,
			  					 MarriageList marriageList,
			  					 ReferenceList referenceList,
			  					 PhotoList photoList,
			  					 boolean bSuppressLiving,
			  					 IndexPersonToParentMarriage idxPerToParentMar,
			  					 IndexPersonToMarriages idxPerToMar,
			  					 IndexMarriageToSpouses idxMarToSpouses,
			  					 IndexPersonToPhotos idxPerToPhoto,
			  					 IndexMarriageToPhotos idxMarToPhoto,
			  					 IndexPersonsToReferences idxPerToReference,
			  					 IndexMarriagesToReferences idxMarToReference,
			  					 String strOutputPath,
			  					 Person person)
		throws Exception
	{
		PersonHelper personHelper = new PersonHelper(person, bSuppressLiving);
		String strFileName = strOutputPath + PERSON_INFO_FILE_SYSTEM_SUBDIRECTORY + "\\" + HTMLShared.PERINFOFILENAME + person.getPersonId() + ".htm";
		m_outputStream.output("Generating Person Info File: " + strFileName+ "\n");
		HTMLFormOutput output = new HTMLFormOutput(strFileName);
		
		// Discover relatives...
		// .... parents
		Person personFather = null;
		Person personMother = null;
		int iParentMarriageId = personHelper.getParentId();
		if (MarriageIdHelper.MARRIAGEID_INVALID != iParentMarriageId)
		{
			int[] arParentsIds = idxMarToSpouses.getSpouses(iParentMarriageId);
			if (PersonIdHelper.PERSONID_INVALID != arParentsIds[IndexMarriageToSpouses.GET_SPOUSES_HUSBAND_IDX])
			{
				personFather = personList.get(arParentsIds[IndexMarriageToSpouses.GET_SPOUSES_HUSBAND_IDX]);
			}
			if (PersonIdHelper.PERSONID_INVALID != arParentsIds[IndexMarriageToSpouses.GET_SPOUSES_WIFE_IDX])
			{
				personMother = personList.get(arParentsIds[IndexMarriageToSpouses.GET_SPOUSES_WIFE_IDX]);
			}
		}
		// .... marriages
		int iMarriageCount = idxPerToMar.getMarriageCount(personHelper.getPersonId());
		ArrayList<Integer> alMarriageIds = idxPerToMar.getMarriageIds(personHelper.getPersonId());
		
		// Gather all references for this person and all this person's marriages
		ArrayList<TaggedContainerDescriptor> alPersonReferences = idxPerToReference.getReferencesForPerson(personHelper.getPersonId());
		ArrayList<ArrayList<TaggedContainerDescriptor>> alMarriagesToReferences = null;
		int iMarriageReferenceTagCount = 0;
		if ((0 != iMarriageCount) && (0 != alMarriageIds.size()))
		{
			alMarriagesToReferences = new ArrayList<ArrayList<TaggedContainerDescriptor>>();
			for (int m=0; m<alMarriageIds.size(); m++)
			{
				ArrayList<TaggedContainerDescriptor> alReferencesForThisMarriage = idxMarToReference.getReferencesForMarriage(alMarriageIds.get(m));
				if (null != alReferencesForThisMarriage)
				{
					iMarriageReferenceTagCount += alReferencesForThisMarriage.size();
				}
				alMarriagesToReferences.add(alReferencesForThisMarriage);
			}
		}
		
		// Gather all photos for this person and all this person's marriages
		ArrayList<TaggedContainerDescriptor> alPersonPhotos = idxPerToPhoto.getPhotosForPerson(personHelper.getPersonId());
		ArrayList<ArrayList<TaggedContainerDescriptor>> alMarriagesToPhotos = null;
		int iMarriagePhotoTagCount = 0;
		if ((0 != iMarriageCount) && (0 != alMarriageIds.size()))
		{
			alMarriagesToPhotos = new ArrayList<ArrayList<TaggedContainerDescriptor>>();
			for (int m=0; m<alMarriageIds.size(); m++)
			{
				ArrayList<TaggedContainerDescriptor> alPhotosForThisMarriage = idxMarToPhoto.getPhotosForMarriage(alMarriageIds.get(m));
				if (null != alPhotosForThisMarriage)
				{
					iMarriagePhotoTagCount += alPhotosForThisMarriage.size();
				}
				alMarriagesToPhotos.add(alPhotosForThisMarriage);
			}
		}
		
		// Start document creation
		String strTitle = "Information for:&nbsp;" + personHelper.getPersonName();
		output.outputSidebarFrontEnd(strTitle, m_family, personList, marriageList);
		
		// Top of Document Internal Link Tag
		output.output("<A name=InfoTop></A>");
		output.outputCRLF();

		// Top of Page Title
		output.output("<center>");
		output.outputSpan(HTMLFormOutput.styleSelectors.E_PageTopHeader, strTitle);
		output.output("</center>");
		output.outputCRLF();
		output.output("<hr width=\"100%\">");
		output.outputCRLF();

		// Small Links to Page Sections
		output.output("<center><span class=\"pageBodySmallLink\">");
		// First Line "(parents) - (marriages)"
		if ((null != personFather) || (null != personMother))
		{
			output.outputStartAnchor("#InfoParents");
			output.output("(Parents)");
			output.outputEndAnchor();
			if (iMarriageCount >= 1)
			{
				output.output(" - ");
			}
		}
		if (iMarriageCount >= 1)
		{
			output.outputStartAnchor("#InfoMarriages");
			output.output("(Marriages)");
			output.outputEndAnchor();
		}
		if (((null != personFather) || (null != personMother)) || (iMarriageCount >= 1))
		{
			output.outputBR();
		}
		
		// Second Line "(photos) - (documentation) - (sundries)"
		if (((null != alPersonPhotos) && (0 != alPersonPhotos.size())) ||
			(0 != iMarriagePhotoTagCount))
		{
			output.outputStartAnchor("#InfoPhotos");
			output.output("(Photos) - ");	// dash here because sundries always exist
			output.outputEndAnchor();
		}
		
		if (((null != alPersonReferences) && (0 != alPersonReferences.size())) ||
			(0 != iMarriageReferenceTagCount))
		{
			output.outputStartAnchor("#InfoReferences");
			output.output("(References) - ");	// dash here because sundries always exist
			output.outputEndAnchor();
		}
		
		output.outputStartAnchor("#InfoSundries");
		output.output("(Sundries)");
		output.outputEndAnchor();
		output.outputBR();
		output.outputCRLF();

		
		// Third Line "(events) - (lds)"
		if (!personHelper.getIsPersonLiving() || !m_bSuppressLiving)
		{
			if (0 != PersonHelper.getEventCount(person))
			{
				output.outputStartAnchor("#InfoEvents");
				output.output("(Events)");	
				output.outputEndAnchor();
			}

			if (!m_bSuppressLds /* && m_pFamily->doesLdsDataExist(pPerson) */)
			{
				if (0 != PersonHelper.getEventCount(person))
				{
					output.output(" - ");	
				}
				output.outputStartAnchor("#InfoLds");
				output.output("(Lds)");	
				output.outputEndAnchor();
			}
		}
	
		
		output.output("</span></center>");
		output.outputBR();
		output.outputCRLF();
	
		
		// Show Parents
		if ((null != personFather) && (null != personMother))
		{
			// Parents Internal Link Tag
			output.output("<A name=InfoParents></A>");
			output.outputCRLF();

			// Parents Header
			output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyMediumHeader, "Parents:");
			output.outputBR(2);
			output.outputCRLF();
			// Father
			PersonHelper fatherHelper = new PersonHelper(personFather, bSuppressLiving);
			String strHRef = m_family.getUrlPrefix() + PERSON_INFO_FILE_SYSTEM_SUBDIRECTORY + "\\" + HTMLShared.PERINFOFILENAME + personFather.getPersonId() + ".htm";
			output.outputStandardBracketedLink(strHRef, "View Father");
			output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyNormalText, fatherHelper.getPersonName());
			output.outputBR();
			output.outputCRLF();
			// Mother
			PersonHelper motherHelper = new PersonHelper(personMother, bSuppressLiving);
			strHRef = m_family.getUrlPrefix() + PERSON_INFO_FILE_SYSTEM_SUBDIRECTORY + "\\" + HTMLShared.PERINFOFILENAME + personMother.getPersonId() + ".htm";
			output.outputStandardBracketedLink(strHRef, "View Mother");
			output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyNormalText, motherHelper.getPersonName());
			output.outputBR();
			output.outputCRLF();

			// Show the link to the parent's FGS
			if (MarriageIdHelper.MARRIAGEID_INVALID != iParentMarriageId)
			{	// Show the "[View Parents Family Group Sheet]" link
				strHRef = m_family.getUrlPrefix() + HTMLShared.FGSDIR + "\\" + HTMLShared.FGSFILENAME + iParentMarriageId + ".htm";
				output.outputStandardBracketedLink(strHRef, "View Family");
				// Show parent's name in the marriage
				String strWork = fatherHelper.getPersonName() + " and " + motherHelper.getPersonName() + " - MID# " + iParentMarriageId;
				output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyNormalText, strWork);
				output.outputBR();
				output.outputCRLF();
			}
			output.outputBR();
		}
		
		// Show Marriages
		if (iMarriageCount >= 1)
		{
			// Marriages Internal Link Tag
			output.output("<A name=InfoMarriages></A>");
			output.outputCRLF();

			// Marriage Header
			output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyMediumHeader, "Marriages:");
			output.outputBR(2);
			output.outputCRLF();
			
			for (int m=0; m<alMarriageIds.size(); m++)
			{
				Person husband = null;
				Person wife = null;
				int[] arParentsIds = idxMarToSpouses.getSpouses(alMarriageIds.get(m).intValue());
				if (PersonIdHelper.PERSONID_INVALID != arParentsIds[IndexMarriageToSpouses.GET_SPOUSES_HUSBAND_IDX])
				{
					husband = personList.get(arParentsIds[IndexMarriageToSpouses.GET_SPOUSES_HUSBAND_IDX]);
				}
				if (PersonIdHelper.PERSONID_INVALID != arParentsIds[IndexMarriageToSpouses.GET_SPOUSES_WIFE_IDX])
				{
					wife = personList.get(arParentsIds[IndexMarriageToSpouses.GET_SPOUSES_WIFE_IDX]);
				}
				if ((null != husband) && (null != wife))
				{	// Show the "[View Family]" link
					String strHRef = m_family.getUrlPrefix() + HTMLShared.FGSDIR + "\\" + HTMLShared.FGSFILENAME + alMarriageIds.get(m).intValue() + ".htm";
					output.outputStandardBracketedLink(strHRef, "View Family");
					// Setup helpers
					PersonHelper husbandHelper = new PersonHelper(husband, bSuppressLiving);
					PersonHelper wifeHelper = new PersonHelper(wife, bSuppressLiving);
					// Show THIS person's name in the marriage
					output.output("<span class=\"pageBodyNormalText\">");
					output.output(husbandHelper.getPersonName() + " and ");
					// Show SPOUSE's name in marriage with link to PERSONINFO
					strHRef = m_family.getUrlPrefix() + HTMLShared.PERINFODIR + "\\" + HTMLShared.PERINFOFILENAME + wifeHelper.getPersonId() + ".htm";
					output.outputStartAnchor(strHRef);
					output.output(wifeHelper.getPersonName());
					output.outputEndAnchor();
					output.output("</span>");
					// Show marriage ID
					output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodySmallText, " - MID# " + alMarriageIds.get(m).intValue());
					output.outputBR();
					output.outputCRLF();
				}
			}
			output.outputBR();
			output.outputCRLF();
		}

		// Show photos for person if they exist
		if (((null != alPersonPhotos) && (0 != alPersonPhotos.size())) ||
			(0 != iMarriagePhotoTagCount))
		{	// Photos Internal Link Tag
			output.output("<A name=InfoPhotos></A>");
			output.outputCRLF();
			if ((null != alPersonPhotos) && (0 != alPersonPhotos.size()))
			{	// Photos for Person Header
				output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyMediumHeader, "Photos for Person:");
				output.outputBR(2);
				output.outputCRLF();
				
				for (int p=0; p<alPersonPhotos.size(); p++)
				{
					TaggedContainerDescriptor tag = alPersonPhotos.get(p);
					int iPhotoId = (int)tag.getContainerId();
					Photo photo = photoList.get(iPhotoId);
					if (null != photo)
					{
						String strUrl = family.getUrlPrefix() + "/" + HTMLShared.PHOTOWRAPDIR + "/" + HTMLShared.PHOTOWRAPFILENAME + iPhotoId + ".htm";
						// Photos
						output.outputStandardBracketedLink(strUrl, "View Photo");
						String strDescription = HTMLShared.buildParagraphListObject(m_family, commandLineParameters,
								  PhotoHelper.getDescription(photo), m_personList, m_marriageList,
								  m_referenceList, m_photoList,
								  idxMarToSpouses,
								  true,
								  true,
								  m_bSuppressLiving,
								  null,
								  iPhotoId,
								  0);
						output.output(strDescription);
						output.outputBR();
						output.outputCRLF();
						// Build the Tag Types string for this photo
						output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodySmallText, "Information Types: " + tag.getConcatenatedTagNames());
						output.outputBR(2);
						output.outputCRLF();
					}
				}
				if (0 != iMarriagePhotoTagCount)
				{
					output.outputBR();
					output.outputCRLF();
				}
			}
			
			// Show photos for marriages if they exist
			if (0 != iMarriagePhotoTagCount)
			{	// Photos for Marriages Header
				output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyMediumHeader, "Photos for Marriages:");
				if ((null != alPersonPhotos) && (0 != alPersonPhotos.size()))
				{
					output.outputBR();
					output.outputCRLF();
					output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodySmallHeader, "(May duplicate some of the photos listed above.)");
				}
				output.outputBR(2);
				output.outputCRLF();

				// Show Marriage Photos
				for (int m=0; m<alMarriagesToPhotos.size(); m++)
				{
					ArrayList<TaggedContainerDescriptor> alTags = alMarriagesToPhotos.get(m);
					if (null != alTags)
					{						
						int iMarriageId = alMarriageIds.get(m);
						Marriage marriage = marriageList.get(iMarriageId);
						if (null != marriage)
						{
							for (int p=0; p<alTags.size(); p++)
							{
								TaggedContainerDescriptor tag = alTags.get(p);
								if (null != tag)
								{
									int iPhotoId = (int)tag.getContainerId();
									Photo photo = photoList.get(iPhotoId);
									if (null != photo)
									{
										String strUrl = family.getUrlPrefix() + "/" + HTMLShared.PHOTOWRAPDIR + "/" + HTMLShared.PHOTOWRAPFILENAME + iPhotoId + ".htm";
										// Photos
										output.outputStandardBracketedLink(strUrl, "View Photo");
										String strDescription = HTMLShared.buildParagraphListObject(m_family, commandLineParameters,
												  PhotoHelper.getDescription(photo), m_personList, m_marriageList,
												  m_referenceList, m_photoList,
												  idxMarToSpouses,
												  true,
												  true,
												  m_bSuppressLiving,
												  null,
												  iPhotoId,
												  0);
										output.output(strDescription);
										output.outputBR();
										output.outputCRLF();
								
										// Build "For Marriage..." string
										String strForMarriage = HTMLShared.buildSimpleMarriageNameString(personList, idxMarToSpouses, iMarriageId, m_bSuppressLiving, "%s and %s");
										output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodySmallText, strForMarriage);
										output.outputBR();
										output.outputCRLF();
										
										// Build the Tag Types string for this photo
										output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodySmallText, "Information Types: " + tag.getConcatenatedTagNames());
										output.outputBR(2);
										output.outputCRLF();
									}
								}
							}
						}
					}
				}
				output.outputBR();
				output.outputCRLF();
			}
		}
				
		// Show references for person if they exist
		if (((null != alPersonReferences) && (0 != alPersonReferences.size())) ||
			(0 != iMarriageReferenceTagCount))
		{	// Photos Internal Link Tag
			output.output("<A name=InfoReferences></A>");
			output.outputCRLF();
			if ((null != alPersonReferences) && (0 != alPersonReferences.size()))
			{	// Photos for Person Header
				output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyMediumHeader, "References for Person:");
				output.outputBR(2);
				output.outputCRLF();
				
				for (int p=0; p<alPersonReferences.size(); p++)
				{
					TaggedContainerDescriptor tag = alPersonReferences.get(p);
					int iReferenceId = (int)tag.getContainerId();
					Reference reference = referenceList.get(iReferenceId);
					if (null != reference)
					{
						int iEntryId = (int)tag.getSubContainerId();
						Entry entry = ReferenceHelper.getEntry(reference, (iEntryId - 1));
						if (null != entry)
						{	// Show Reference Title
							String strUrl = family.getUrlPrefix() + "/" + HTMLShared.REFERENCEDIR + "/" + HTMLShared.REFERENCEFILENAME + iReferenceId + ".htm";
							output.outputStandardBracketedLink(strUrl, "View Reference");
							output.output(ReferenceHelper.getTitle(reference));
							output.outputBR();
							output.outputCRLF();

							// Show Entry Title/Description
							int iTitleParagraphCount = EntryHelper.getTitleParagraphCount(entry);
							if (0 != iTitleParagraphCount)
							{
								strUrl = family.getUrlPrefix() + "/" + HTMLShared.REFERENCEDIR + "/" + HTMLShared.REFERENCEFILENAME + iReferenceId + ".htm#REF" + iReferenceId + "ENT" + iEntryId;
								output.outputStandardBracketedLink(strUrl, "View Entry");
								for (int t=0; t<iTitleParagraphCount; t++)
								{
									Paragraph paragraph = EntryHelper.getTitleParagraph(entry, t);
									String strEntryTitle = HTMLShared.buildParagraphString(family, commandLineParameters,
											paragraph, personList, marriageList, referenceList, photoList,
					                        idxMarToSpouses, true, true, bSuppressLiving, null,
					                        iReferenceId, iEntryId);
									output.output(strEntryTitle);
								}
								output.outputCRLF();
							}
							// Build the Tag Types string for this photo
							output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodySmallText, "Information Types: " + tag.getConcatenatedTagNames());
							output.outputBR(2);
							output.outputCRLF();
						}
					}
				}
				if (0 != iMarriageReferenceTagCount)
				{
					output.outputBR();
					output.outputCRLF();
				}
			}
			
			// Show references for marriages if they exist
			if (0 != iMarriageReferenceTagCount)
			{	// References for Marriages Header
				output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyMediumHeader, "References for Marriages:");
				if ((null != alPersonReferences) && (0 != alPersonReferences.size()))
				{
					output.outputBR();
					output.outputCRLF();
					output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodySmallHeader, "(May duplicate some of the references listed above.)");
				}
				output.outputBR(2);
				output.outputCRLF();

				// Show Marriage References
				for (int m=0; m<alMarriagesToReferences.size(); m++)
				{
					ArrayList<TaggedContainerDescriptor> alTags = alMarriagesToReferences.get(m);
					if (null != alTags)
					{						
						int iMarriageId = alMarriageIds.get(m);
						Marriage marriage = marriageList.get(iMarriageId);
						if (null != marriage)
						{
							for (int p=0; p<alTags.size(); p++)
							{
								TaggedContainerDescriptor tag = alTags.get(p);
								if (null != tag)
								{
									int iReferenceId = (int)tag.getContainerId();
									Reference reference = referenceList.get(iReferenceId);
									if (null != reference)
									{
										int iEntryId = (int)tag.getSubContainerId();
										Entry entry = ReferenceHelper.getEntry(reference, (iEntryId - 1));
										if (null != entry)
										{	// Show Reference Title
											String strUrl = family.getUrlPrefix() + "/" + HTMLShared.REFERENCEDIR + "/" + HTMLShared.REFERENCEFILENAME + iReferenceId + ".htm";
											output.outputStandardBracketedLink(strUrl, "View Reference");
											output.output(ReferenceHelper.getTitle(reference));
											output.outputBR();
											output.outputCRLF();

											// Show Entry Title/Description
											int iTitleParagraphCount = EntryHelper.getTitleParagraphCount(entry);
											if (0 != iTitleParagraphCount)
											{
												strUrl = family.getUrlPrefix() + "/" + HTMLShared.REFERENCEDIR + "/" + HTMLShared.REFERENCEFILENAME + iReferenceId + ".htm#REF" + iReferenceId + "ENT" + iEntryId;
												output.outputStandardBracketedLink(strUrl, "View Entry");
												for (int t=0; t<iTitleParagraphCount; t++)
												{
													Paragraph paragraph = EntryHelper.getTitleParagraph(entry, t);
													String strEntryTitle = HTMLShared.buildParagraphString(family, commandLineParameters,
															paragraph, personList, marriageList, referenceList, photoList,
									                        idxMarToSpouses, true, true, bSuppressLiving, null,
									                        iReferenceId, iEntryId);
													output.output(strEntryTitle);
												}
												output.outputCRLF();
											}
								
											// Build "For Marriage..." string
											String strForMarriage = HTMLShared.buildSimpleMarriageNameString(personList, idxMarToSpouses, iMarriageId, m_bSuppressLiving, "%s and %s");
											output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodySmallText, strForMarriage);
											output.outputBR();
											output.outputCRLF();
										
											// Build the Tag Types string for this photo
											output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodySmallText, "Information Types: " + tag.getConcatenatedTagNames());
											output.outputBR(2);
											output.outputCRLF();
										}
									}
								}
							}
						}
					}
				}
				output.outputBR();
				output.outputCRLF();
			}
		}
		
		// Show Various and Sundries
		// Various and Sundries Internal Link Tag
		output.output("<A name=InfoSundries></A>");
		output.outputCRLF();

		// Various and Sundries Header
		output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyMediumHeader, "Various and Sundries:");
		output.outputBR(2);
		output.outputCRLF();
/*		
		// Full Decorated Name
		if (0 != pPersonAccessor->getFullBasicName().Compare(pPersonAccessor->getFullDecoratedName()))
		{
			output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyBoldNormalText, "Full Name: ");
			output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyNormalText, pPersonAccessor->getFullDecoratedName());
			output.outputBR();
			output.outputCRLF();
		}
*/
		// First Name Alt Spellings
		List<String> lAltFirstNames = personHelper.getFirstNameAltSpellings();
		if (0 != lAltFirstNames.size())
		{
			output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyBoldNormalText, "First Name Alternate Spellings: ");
			for (int iAlt=0; iAlt<lAltFirstNames.size(); iAlt++)
			{
				String strAlt = lAltFirstNames.get(iAlt);
				if (0 != iAlt)
				{
					output.output(", ");
				}
				output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyNormalText, strAlt);
			}
			output.outputBR();
			output.outputCRLF();
		}

		// Middle Name Alt Spellings
		List<String> lAltMiddleNames = personHelper.getMiddleNameAltSpellings();
		if (0 != lAltMiddleNames.size())
		{
			output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyBoldNormalText, "Middle Name Alternate Spellings: ");
			for (int iAlt=0; iAlt<lAltMiddleNames.size(); iAlt++)
			{
				String strAlt = lAltMiddleNames.get(iAlt);
				if (0 != iAlt)
				{
					output.output(", ");
				}
				output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyNormalText, strAlt);
			}
			output.outputBR();
			output.outputCRLF();
		}

		// Last Name Alt Spellings
		List<String> lAltLastNames = personHelper.getLastNameAltSpellings();
		if (0 != lAltLastNames.size())
		{
			output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyBoldNormalText, "Last Name Alternate Spellings: ");
			for (int iAlt=0; iAlt<lAltLastNames.size(); iAlt++)
			{
				String strAlt = lAltLastNames.get(iAlt);
				if (0 != iAlt)
				{
					output.output(", ");
				}
				output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyNormalText, strAlt);
			}
			output.outputBR();
			output.outputCRLF();
		}

		// AFN Id
		String strAFN = personHelper.getAfn();
		if ((null != strAFN) && (0 != strAFN.length()))
		{
			output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyBoldNormalText, "AFN: ");
			output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyNormalText, strAFN);
			output.outputBR();
			output.outputCRLF();
		}

		// Relationship
		String strRelationship = personHelper.getRelationshipToPrimary();
		output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyBoldNormalText, "Relationship: ");
		output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyNormalText, strRelationship);
		output.outputBR();
		output.outputCRLF();
		
		// Generation
		int iGeneration = PersonHelper.getGenerationFromPrimary(person);
		output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyBoldNormalText, "Generation: ");
		output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyNormalText, "" + iGeneration);
		output.outputBR();
		output.outputCRLF();
		
		// NickNames
		List<String> lNickNames = personHelper.getNickNames();
		if (0 != lNickNames.size())
		{
			output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyBoldNormalText, "NickNames: ");
			for (int iNick=0; iNick<lNickNames.size(); iNick++)
			{
				String strNickName = lNickNames.get(iNick);
				if (0 != iNick)
				{
					output.output(", ");
				}
				output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyNormalText, strNickName);
			}
			output.outputBR();
			output.outputCRLF();
		}

		// Professions
		List<String> lProfessions = personHelper.getProfessions();
		if (0 != lProfessions.size())
		{
			output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyBoldNormalText, "Professions: ");
			for (int iProf=0; iProf<lProfessions.size(); iProf++)
			{
				String strProfession = lProfessions.get(iProf);
				if (0 != iProf)
				{
					output.output(", ");
				}
				output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyNormalText, strProfession);
			}
			output.outputBR();
			output.outputCRLF();
		}

		// Religions
		List<String> lReligions = personHelper.getReligions();
		if (0 != lReligions.size())
		{
			output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyBoldNormalText, "Religions: ");
			for (int iRelig=0; iRelig<lReligions.size(); iRelig++)
			{
				String strReligion = lReligions.get(iRelig);
				if (0 != iRelig)
				{
					output.output(", ");
				}
				output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyNormalText, strReligion);
			}
			output.outputBR();
			output.outputCRLF();
		}
		
		// Cause of Death
		if (0 != PersonHelper.getDeathCause(person).length())
		{
			output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyBoldNormalText, "Cause of Death: ");
			output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyNormalText, PersonHelper.getDeathCause(person));
			output.outputBR();
			output.outputCRLF();
		}
		output.outputBR();
		
		// Show Person Event Data
		// Person Event Internal Link Tag
		if (!personHelper.getIsPersonLiving() || !m_bSuppressLiving)
		{
			int iEventCount = PersonHelper.getEventCount(person);
			if (0 != iEventCount)
			{
				output.output("<A name=InfoEvents></A>");
				output.outputCRLF();
			
				// Person Event Header
				output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyMediumHeader, "Events:");
				output.outputBR(2);
				output.outputCRLF();
			
				for (int i=0 ;i<iEventCount; i++)
				{
					Event event = PersonHelper.getEvent(person, i);
					EventHelper eventHelper = new EventHelper(event, personHelper.getIsPersonLiving(), bSuppressLiving);
					Paragraph paraTitle = eventHelper.getEventTitleParagraph();
					if (null != paraTitle)
					{
						String strEventTitle = HTMLShared.buildParagraphString(family, commandLineParameters,
								paraTitle, personList, marriageList, referenceList, photoList,
                                idxMarToSpouses, false, false, bSuppressLiving, null, -1, -1);
						if (0 != strEventTitle.length())
						{
							output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyBoldNormalText, "Title: ");
							output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyNormalText, strEventTitle);
							output.outputBR();
							output.outputCRLF();
						}
					}
					
					String strEventDate = eventHelper.getEventDate();
					String strEventPlace = eventHelper.getEventPlace();
					String strEventTag = eventHelper.getEventTag();
					if (0 != strEventTag.length())
					{
						output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyBoldNormalText, "Type: ");
						output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyNormalText, strEventTag);
						output.outputBR();
						output.outputCRLF();
					}
					if (0 != strEventDate.length())
					{
						output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyBoldNormalText, "Date: ");
						output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyNormalText, strEventDate);
						output.outputBR();
						output.outputCRLF();
					}
					if (0 != strEventPlace.length())
					{
						output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyBoldNormalText, "Place: ");
						output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyNormalText, strEventPlace);
						output.outputBR();
						output.outputCRLF();
					}
					
					int iDescriptionParagraphCount = eventHelper.getEventDescriptionParagraphCount();
					if (0 != iDescriptionParagraphCount)
					{
						output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyBoldNormalText, "Description: ");
						output.outputBR();
						output.outputCRLF();
						for (int d=0; d<iDescriptionParagraphCount; d++)
						{
							Paragraph paraDescription = eventHelper.getEventDescriptionParagraph(d);
							if (null != paraDescription)
							{
								String strEventDescriptionParagraph = HTMLShared.buildParagraphString(family,
										commandLineParameters, paraDescription, personList, marriageList, referenceList,
										photoList, idxMarToSpouses, true, true, bSuppressLiving, null, -1, -1);
								if (0 != strEventDescriptionParagraph.length())
								{
									output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyNormalText, strEventDescriptionParagraph);
									output.outputCRLF();
								}
							}
						}
					}
				}
			}
			output.outputBR();
			output.outputCRLF();
		}
		
		if (!m_bSuppressLds)
		{	// Show LDS Data
			// Various and Sundries Internal Link Tag
			output.output("<A name=InfoLds></A>");
			output.outputCRLF();

			// LDS Data Header
			output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyMediumHeader, "LDS Data:");
			output.outputBR(2);
			output.outputCRLF();

			outputBaptism(person, personHelper, output);
			outputEndowment(person, personHelper, output);
			outputSealToParents(person, personHelper, output);
//			OutputEndowment(pPerson, pOutputStream);
//			OutputSealToParents(pPerson, pOutputStream);
//			OutputSealToSpouse(pPerson, pOutputStream);
		}

		output.outputSidebarBackEnd();
		
		output.commit();
		output = null;
	}
	
	private void outputBaptism(Person person, PersonHelper personHelper, HTMLFormOutput output)
	{
//		FormsTagFinderHtml refFinder(m_pConfiguration);
		String strDate = personHelper.getBaptismDate();
		String strPlace = personHelper.getBaptismPlace();
		String strTemple = personHelper.getBaptismTemple();
		String strProxyName = personHelper.getBaptismProxyName();
		if ((0 != strDate.length()) ||
			(0 != strPlace.length()) ||
			(0 != strTemple.length()) ||
			(0 != strProxyName.length()))
		{
			if (0 != strDate.length())
			{
//				refFinder.FindForPerson(pPerson->getPersonIdAsLong(), "BapD");
				output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyBoldNormalText, "Baptised:&nbsp;");
//				if (refFinder.Found())
//				{
//					output.outputStartAnchor(refFinder.GetHRef());
//				}
				output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyNormalText, strDate);
//				if (refFinder.Found())
//				{
//					output.outputEndAnchor();
//				}
				output.outputBR();
				output.outputCRLF();
			}
			if (0 != strPlace.length())
			{
//				refFinder.FindForPerson(pPerson->getPersonIdAsLong(), "BapP");
				output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyBoldNormalText, "Baptised Place:&nbsp;");
//				if (refFinder.Found())
//				{
//					output.outputStartAnchor(refFinder.GetHRef());
//				}
				output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyNormalText, strPlace);
//				if (refFinder.Found())
//				{
//					output.outputEndAnchor();
//				}
				output.outputBR();
				output.outputCRLF();
			}
			if (0 != strTemple.length())
			{
//				refFinder.FindForPerson(pPerson->getPersonIdAsLong(), "BapTemple");
				output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyBoldNormalText, "Baptised Temple:&nbsp;");
//				if (refFinder.Found())
//				{
//					output.outputStartAnchor(refFinder.GetHRef());
//				}
				output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyNormalText, strTemple);
//				if (refFinder.Found())
//				{
//					output.outputEndAnchor();
//				}
				output.outputBR();
				output.outputCRLF();
			}
			if (0 != strProxyName.length())
			{
//				refFinder.FindForPerson(pPerson->getPersonIdAsLong(), "BapProxy");
				output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyBoldNormalText, "Baptised Proxy:&nbsp;");
//				if (refFinder.Found())
//				{
//					output.outputStartAnchor(refFinder.GetHRef());
//				}
				output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyNormalText, strProxyName);
//				if (refFinder.Found())
//				{
//					output.outputEndAnchor();
//				}
				output.outputBR();
				output.outputCRLF();
			}
		}
	}
		
	private void outputEndowment(Person person, PersonHelper personHelper, HTMLFormOutput output)
	{
//		FormsTagFinderHtml refFinder(m_pConfiguration);
		String strDate = personHelper.getEndowmentDate();
		String strPlace = personHelper.getEndowmentPlace();
		String strTemple = personHelper.getEndowmentTemple();
		String strProxyName = personHelper.getEndowmentProxyName();
		if ((0 != strDate.length()) ||
			(0 != strPlace.length()) ||
			(0 != strTemple.length()) ||
			(0 != strProxyName.length()))
		{
			if (0 != strDate.length())
			{
//				refFinder.FindForPerson(pPerson->getPersonIdAsLong(), "EndD");
				output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyBoldNormalText, "Endowed:&nbsp;");
//				if (refFinder.Found())
//				{
//					output.outputStartAnchor(refFinder.GetHRef());
//				}
				output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyNormalText, strDate);
//				if (refFinder.Found())
//				{
//					output.outputEndAnchor();
//				}
				output.outputBR();
				output.outputCRLF();
			}
			if (0 != strPlace.length())
			{
//				refFinder.FindForPerson(pPerson->getPersonIdAsLong(), "EndP");
				output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyBoldNormalText, "Endowed Place:&nbsp;");
//				if (refFinder.Found())
//				{
//					output.outputStartAnchor(refFinder.GetHRef());
//				}
				output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyNormalText, strPlace);
//				if (refFinder.Found())
//				{
//					output.outputEndAnchor();
//				}
				output.outputBR();
				output.outputCRLF();
			}
			if (0 != strTemple.length())
			{
//				refFinder.FindForPerson(pPerson->getPersonIdAsLong(), "EndTemple");
				output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyBoldNormalText, "Endowed Temple:&nbsp;");
//				if (refFinder.Found())
//				{
//					output.outputStartAnchor(refFinder.GetHRef());
//				}
				output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyNormalText, strTemple);
//				if (refFinder.Found())
//				{
//					output.outputEndAnchor();
//				}
				output.outputBR();
				output.outputCRLF();
			}
			if (0 != strProxyName.length())
			{
//				refFinder.FindForPerson(pPerson->getPersonIdAsLong(), "EndProxy");
				output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyBoldNormalText, "Endowed Proxy:&nbsp;");
//				if (refFinder.Found())
//				{
//					output.outputStartAnchor(refFinder.GetHRef());
//				}
				output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyNormalText, strProxyName);
//				if (refFinder.Found())
//				{
//					output.outputEndAnchor();
//				}
				output.outputBR();
				output.outputCRLF();
			}
		}
	}
	
	
	private void outputSealToParents(Person person, PersonHelper personHelper, HTMLFormOutput output)
	{
//		FormsTagFinderHtml refFinder(m_pConfiguration);
		String strDate = personHelper.getSealToParentsDate();
		String strPlace = personHelper.getSealToParentsPlace();
		String strTemple = personHelper.getSealToParentsTemple();
		if ((0 != strDate.length()) ||
			(0 != strPlace.length()) ||
			(0 != strTemple.length()))
		{
			if (0 != strDate.length())
			{
//				refFinder.FindForPerson(pPerson->getPersonIdAsLong(), "SToPD");
				output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyBoldNormalText, "Sealed To Parents Date:&nbsp;");
//				if (refFinder.Found())
//				{
//					output.outputStartAnchor(refFinder.GetHRef());
//				}
				output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyNormalText, strDate);
//				if (refFinder.Found())
//				{
//					output.outputEndAnchor();
//				}
				output.outputBR();
				output.outputCRLF();
			}
			if (0 != strPlace.length())
			{
//				refFinder.FindForPerson(pPerson->getPersonIdAsLong(), "SToPP");
				output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyBoldNormalText, "Sealed To Parents Place:&nbsp;");
//				if (refFinder.Found())
//				{
//					output.outputStartAnchor(refFinder.GetHRef());
//				}
				output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyNormalText, strPlace);
//				if (refFinder.Found())
//				{
//					output.outputEndAnchor();
//				}
				output.outputBR();
				output.outputCRLF();
			}
			if (0 != strTemple.length())
			{
//				refFinder.FindForPerson(pPerson->getPersonIdAsLong(), "SToPTemple");
				output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyBoldNormalText, "Sealed To Parents Temple:&nbsp;");
//				if (refFinder.Found())
//				{
//					output.outputStartAnchor(refFinder.GetHRef());
//				}
				output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyNormalText, strTemple);
//				if (refFinder.Found())
//				{
//					output.outputEndAnchor();
//				}
				output.outputBR();
				output.outputCRLF();
			}
		}
		List<String> lProxies = personHelper.getSealToParentsProxies();
		if (0 != lProxies.size())
		{
			output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyBoldNormalText, "Sealed To Parents Proxy:&nbsp;");
			for (int i=0; i<lProxies.size(); i++)
			{
				String strProxy = lProxies.get(i);
				if (0 != strProxy.length())
				{
					output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyNormalText, strProxy);
					output.outputBR();
					output.outputCRLF();
				}
			}
		}
	}
	
}


/*

#include "stdafx.h"
#include "Family.hpp"
#include "FormsHtmlPersonInfo.hpp"
#include "FormsHtmlShared.hpp"
#include "FourGenerations.hpp"
#include "FormsPersonAccessor.hpp"
#include "FormsTagFinderHtml.hpp"
#include "FormsMarriageAccessor.hpp"

FormsHtmlPersonInfo::FormsHtmlPersonInfo(Family *pFamily, FormsHtmlConfiguration *pConfiguration, long lPersonId)
{
	m_pFamily = pFamily;
	m_lPersonId = lPersonId;
	m_pConfiguration = pConfiguration;
	m_pPersonList = m_pFamily->getPersonList();
	m_pMarriageList = m_pFamily->getMarriageList();
	m_pReferenceList = m_pFamily->getReferenceList();
	m_pPhotoList = m_pFamily->getPhotoList();
	m_pIndex = m_pFamily->getFamilyIndex();
	m_pStatus = pConfiguration->getStatus();
}


void FormsHtmlPersonInfo::Create()
{
	CString strHRef, strWork;
	CString strFileName;
	strFileName.Format("%s%s\\%s%d.htm", m_pConfiguration->getDestinationPath(), PERINFODIR, PERINFOFILENAME, m_lPersonId); 
	m_pStatus->Log("Generating Person Info: " + strFileName, LogLevel_Finest);
	m_pStatus->UpdateStatus("Generating Person Info: " + strFileName);
	FormsHtmlOutputStream *pOutputStream = new FormsHtmlOutputStream(strFileName, m_pStatus);

	Person *pPerson = m_pPersonList->get(m_lPersonId);
	if (pPerson)
	{
		FormsPersonAccessor *pPersonAccessor = new FormsPersonAccessor(pPerson, m_pConfiguration->getSuppressLiving());
		CString strTitle = "Information for:" + pPersonAccessor->getFullBasicName();
		pOutputStream->OutputSidebarFrontEnd(strTitle, m_pConfiguration);

		IndexPersonToMarriages *pIndexPersonToMarriages = m_pIndex->GetIndexPersonToMarriages();
		IndexMarriageToSpouses *pIndexMarriageToSpouses = m_pIndex->GetIndexMarriageToSpouses();
		IndexTags *pIndexReferenceTags = m_pIndex->GetIndexReferenceTags();
		IndexTags *pIndexPhotoTags = m_pIndex->GetIndexPhotoTags();
		FourGenerations fourGeneration(m_pFamily, m_lPersonId);
		Person *pFather = fourGeneration.getFather();
		Person *pMother = fourGeneration.getMother();

		long lPersonId = pPersonAccessor->getPersonIdAsLong();
		long lMarriageCount = pIndexPersonToMarriages->GetMarriagesOfPersonCount(lPersonId);

		// Get the count of references/photos for this person
		long lReferencePersonTagCount = pIndexReferenceTags->GetPersonReferenceCount(lPersonId);
		long lPhotoPersonTagCount = pIndexPhotoTags->GetPersonReferenceCount(lPersonId);

		// Get the count of references/photos for all marriages
		long lReferenceMarriageTagCount = 0;
		long lPhotoMarriageTagCount = 0;
		long lMarriageCookie;
		MARRIAGEID mId;
		if (pIndexPersonToMarriages->StartMarriagesOfPersonEnum(lPersonId, &lMarriageCookie))
		{
			while (pIndexPersonToMarriages->NextMarriagesOfPersonEnum(lPersonId, &lMarriageCookie, &mId))
			{
				lReferenceMarriageTagCount += pIndexReferenceTags->GetMarriageReferenceCount(mId);
				lPhotoMarriageTagCount += pIndexPhotoTags->GetMarriageReferenceCount(mId);
			}
		}

		// See if there are any person events to show
		long lCookie;
		BOOL bEventsExist = pPersonAccessor->startPersonEventGroupEnum(&lCookie);

		// Top of Document Internal Link Tag
		pOutputStream->Output("<A name=InfoTop></A>");
		pOutputStream->OutputFormattingCRLF();

		// Top of Page Title
		pOutputStream->Output("<center>");
		pOutputStream->OutputSpan(E_PageTopHeader, "Information for: " + pPersonAccessor->getFullBasicName());
		pOutputStream->Output("</center>");
		pOutputStream->OutputFormattingCRLF();
		pOutputStream->Output("<hr width=\"100%\">");
		pOutputStream->OutputFormattingCRLF();

		// Small Links to Page Sections
		pOutputStream->Output("<center><span class=\"pageBodySmallLink\">");
		// First Line "(parents) - (marriages)"
		if (pFather || pMother)
		{
			pOutputStream->OutputStartAnchor("#InfoParents");
			pOutputStream->Output("(Parents)");
			pOutputStream->OutputEndAnchor();
			if (lMarriageCount >= 1)
			{
				pOutputStream->Output(" - ");
			}
		}
		if (lMarriageCount >= 1)
		{
			pOutputStream->OutputStartAnchor("#InfoMarriages");
			pOutputStream->Output("(Marriages)");
			pOutputStream->OutputEndAnchor();
		}
		if (pFather || pMother || (lMarriageCount >= 1))
		{
			pOutputStream->OutputBR();
		}
		// Second Line "(photos) - (documentation) - (sundries)"
		if (lPhotoPersonTagCount || lPhotoMarriageTagCount)
		{
			pOutputStream->OutputStartAnchor("#InfoPhotos");
			pOutputStream->Output("(Photos) - ");	// dash here bacause sundries always exist
			pOutputStream->OutputEndAnchor();
		}
		if (lReferencePersonTagCount ||	lReferenceMarriageTagCount)
		{
			pOutputStream->OutputStartAnchor("#InfoReferences");
			pOutputStream->Output("(Documentation) - ");	// dash here bacause sundries always exist
			pOutputStream->OutputEndAnchor();
		}
		pOutputStream->OutputStartAnchor("#InfoSundries");
		pOutputStream->Output("(Sundries)");
		pOutputStream->OutputEndAnchor();
		pOutputStream->OutputBR();
		pOutputStream->OutputFormattingCRLF();

		// Third Line "(events) - (lds)"
		if (bEventsExist)
		{
			pOutputStream->OutputStartAnchor("#InfoEvents");
			pOutputStream->Output("(Events)");	
			pOutputStream->OutputEndAnchor();
		}

		if (m_pConfiguration->getIncludeLds() && m_pFamily->doesLdsDataExist(pPerson))
		{
			if (bEventsExist)
			{
				pOutputStream->Output(" - ");	
			}
			pOutputStream->OutputStartAnchor("#InfoLds");
			pOutputStream->Output("(Lds)");	
			pOutputStream->OutputEndAnchor();
		}
		pOutputStream->Output("</span></center>");
		pOutputStream->OutputBR();
		pOutputStream->OutputFormattingCRLF();


		// Show Parents
		if (pFather && pMother)
		{
			// Parents Internal Link Tag
			pOutputStream->Output("<A name=InfoParents></A>");
			pOutputStream->OutputFormattingCRLF();

			// Parents Header
			pOutputStream->OutputSpan(E_PageBodyMediumHeader, "Parents:");
			pOutputStream->OutputBR(2);
			pOutputStream->OutputFormattingCRLF();
			// Father
			FormsPersonAccessor fatherAccessor(pFather, m_pConfiguration->getSuppressLiving());
			strHRef.Format("%s%s/%s%d.htm", m_pConfiguration->getBasePath(), PERINFODIR, PERINFOFILENAME, pFather->getPersonIdAsLong());
			pOutputStream->OutputStandardBracketedLink(strHRef, "View Father");
			pOutputStream->OutputSpan(E_PageBodyNormalText, fatherAccessor.getFullBasicName());
			pOutputStream->OutputBR();
			pOutputStream->OutputFormattingCRLF();
			// Mother
			FormsPersonAccessor motherAccessor(pMother, m_pConfiguration->getSuppressLiving());
			strHRef.Format("%s%s/%s%d.htm", m_pConfiguration->getBasePath(), PERINFODIR, PERINFOFILENAME, pMother->getPersonIdAsLong());
			pOutputStream->OutputStandardBracketedLink(strHRef, "View Mother");
			pOutputStream->OutputSpan(E_PageBodyNormalText, motherAccessor.getFullBasicName());
			pOutputStream->OutputBR();
			pOutputStream->OutputFormattingCRLF();

			// Show the link to the parent's FGS
			MARRIAGEID mIdParents = fourGeneration.getFatherMarriageId();
			if (mIdParents != MARRIAGEID_INVALID)
			{	// Show the "[View Parents Family Group Sheet]" link
				strHRef.Format("%s%s/%s%d.htm", m_pConfiguration->getBasePath(), FGSDIR, FGSFILENAME, mIdParents);
				pOutputStream->OutputStandardBracketedLink(strHRef, "View Family");
				// Show parent's name in the marriage
				strWork.Format("%s and %s - MID# %d", fatherAccessor.getFullBasicName(), motherAccessor.getFullBasicName(), mIdParents);
				pOutputStream->OutputSpan(E_PageBodyNormalText, strWork);
				pOutputStream->OutputBR();
				pOutputStream->OutputFormattingCRLF();
			}
			pOutputStream->OutputBR();
		}

		// Show Marriages
		if (lMarriageCount >= 1)
		{
			PERSONID husbandId, wifeId;
			CString strText;

			// Marriages Internal Link Tag
			pOutputStream->Output("<A name=InfoMarriages></A>");
			pOutputStream->OutputFormattingCRLF();

			// Marriage Header
			pOutputStream->OutputSpan(E_PageBodyMediumHeader, "Marriages:");
			pOutputStream->OutputBR(2);
			pOutputStream->OutputFormattingCRLF();

			if (pIndexPersonToMarriages->StartMarriagesOfPersonEnum(lPersonId, &lMarriageCookie))
			{
				while (pIndexPersonToMarriages->NextMarriagesOfPersonEnum(lPersonId, &lMarriageCookie, &mId))
				{
					if (pIndexMarriageToSpouses->FindSpousesGivenMarriageId(mId, &husbandId, &wifeId))
					{
						Person *pHusband = m_pPersonList->get(husbandId);
						Person *pWife = m_pPersonList->get(wifeId);
						if (pHusband && pWife)
						{	// Find the spouse of THIS person
							Person *pSpouse = pHusband;
							if (pHusband->getPersonIdAsLong() == m_lPersonId)
							{	// Nope, the spouse is the wife
								pSpouse = pWife;
							}
							// Show the "[View Family]" link
							strHRef.Format("%s%s/%s%d.htm", m_pConfiguration->getBasePath(), FGSDIR, FGSFILENAME, mId);
							pOutputStream->OutputStandardBracketedLink(strHRef, "View Family");
							// Show THIS person's name in the marriage
							pOutputStream->Output("<span class=\"pageBodyNormalText\">");
							pOutputStream->Output(pPersonAccessor->getFullBasicName() + " and ");
							// Show SPOUSE's name in marriage with link to PERSONINFO
							FormsPersonAccessor spouseAccessor(pSpouse, m_pConfiguration->getSuppressLiving());
							strHRef.Format("%s%s/%s%d.htm", m_pConfiguration->getBasePath(), PERINFODIR, PERINFOFILENAME, spouseAccessor.getPersonIdAsLong());
							pOutputStream->OutputStartAnchor(strHRef);
							pOutputStream->Output(spouseAccessor.getFullBasicName());
							pOutputStream->OutputEndAnchor();
							pOutputStream->Output("</span>");
							// Show marriage ID
							strText.Format(" - MID# %d", mId);
							pOutputStream->OutputSpan(E_PageBodySmallText, strText);
							pOutputStream->OutputBR();
							pOutputStream->OutputFormattingCRLF();
						}
					}
				}
			}
			pOutputStream->OutputBR();
			pOutputStream->OutputFormattingCRLF();

		}

		// Show photos if they exist
		if (lPhotoPersonTagCount || lPhotoMarriageTagCount)
		{
			long lPhotoCookie;
			long lPhotoId, lPhotoSubId;

			// Photos Internal Link Tag
			pOutputStream->Output("<A name=InfoPhotos></A>");
			pOutputStream->OutputFormattingCRLF();
			if (lPhotoPersonTagCount)
			{
				// Photos for Person Header
				pOutputStream->OutputSpan(E_PageBodyMediumHeader, "Photos for Person:");
				pOutputStream->OutputBR(2);
				pOutputStream->OutputFormattingCRLF();

				CDWordArray arPhotos;
				CString strHRef;
				CString strText;

				// Show Person Photos
				if (pIndexPhotoTags->StartUniqueReferenceByPersonEnum(lPersonId, &lPhotoCookie, &arPhotos))
				{
					while (pIndexPhotoTags->NextUniqueReferenceByPersonEnum(&lPhotoId, &lPhotoSubId, lPersonId, &lPhotoCookie, &arPhotos))
					{
						Photo *pPhoto = m_pPhotoList->get(lPhotoId);
						if (pPhoto)
						{
							long lPhotoId = pPhoto->getPhotoIdAsLong();

							strHRef.Format("%sphotowrappers/%s%d.htm", m_pConfiguration->getBasePath(), PHOTOWRAPFILENAME, lPhotoId);
							// Photos
							pOutputStream->OutputStandardBracketedLink(strHRef, "View Photo");
							FormsHtmlParagraphFormat titleFormat("pageBodyNormalText");
							pOutputStream->OutputParagraphListObject(pPhoto->getPhotoTitle(), m_pConfiguration, &titleFormat);
							pOutputStream->OutputFormattingCRLF();
							// Build the Tag Types string for this photo
							CString strTagTypes = pIndexPhotoTags->GetAllTagTypesStringForPersonAndEntry(lPersonId, lPhotoId, lPhotoSubId);
							pOutputStream->OutputSpan(E_PageBodySmallText, "Information Types: " + strTagTypes);
							pOutputStream->OutputBR(2);
							pOutputStream->OutputFormattingCRLF();
						}
					}
				}
				if (lPhotoMarriageTagCount)
				{
					pOutputStream->OutputBR();
					pOutputStream->OutputFormattingCRLF();
				}
			}

			if (lPhotoMarriageTagCount)
			{
				CDWordArray arPhotos;

				// Photos for Marriages Header
				pOutputStream->OutputSpan(E_PageBodyMediumHeader, "Photos for Marriages:");
				if (lPhotoPersonTagCount)
				{
					pOutputStream->OutputBR();
					pOutputStream->OutputFormattingCRLF();
					pOutputStream->OutputSpan(E_PageBodySmallHeader, "(May duplicate some of the photos listed above.)");
				}
				pOutputStream->OutputBR(2);
				pOutputStream->OutputFormattingCRLF();

				// Show Marriage Photos
				if (pIndexPersonToMarriages->StartMarriagesOfPersonEnum(lPersonId, &lMarriageCookie))
				{
					while (pIndexPersonToMarriages->NextMarriagesOfPersonEnum(lPersonId, &lMarriageCookie, &mId))
					{
						if (pIndexPhotoTags->StartUniqueReferenceByMarriageEnum(mId, &lPhotoCookie, &arPhotos))
						{
							while (pIndexPhotoTags->NextUniqueReferenceByMarriageEnum(&lPhotoId, &lPhotoSubId, mId, &lPhotoCookie, &arPhotos))
							{
								Photo *pPhoto = m_pPhotoList->get(lPhotoId);
								if (pPhoto)
								{
									long lPhotoId = pPhoto->getPhotoIdAsLong();

									strHRef.Format("%sphotowrappers/%s%d.htm", m_pConfiguration->getBasePath(), PHOTOWRAPFILENAME, lPhotoId);

									// Photos
									pOutputStream->OutputStandardBracketedLink(strHRef, "View Photo");
									FormsHtmlParagraphFormat titleFormat("pageBodyNormalText");
									pOutputStream->OutputParagraphListObject(pPhoto->getPhotoTitle(), m_pConfiguration, &titleFormat);
									pOutputStream->OutputFormattingCRLF();

									// Build "For Marriage..." string
									CString strForMarriage = FormsHtmlShared::BuildSimpleMarriageNameString(m_pConfiguration, mId, "For Marriage: %s and %s");
									pOutputStream->OutputSpan(E_PageBodySmallText, strForMarriage);
									pOutputStream->OutputBR();
									pOutputStream->OutputFormattingCRLF();

									// Build the Tag Types string for this photo entry
									CString strTagTypes = pIndexPhotoTags->GetAllTagTypesStringForMarriageAndEntry(mId, lPhotoId, lPhotoSubId);
									pOutputStream->OutputSpan(E_PageBodySmallText, "Information Types: " + strTagTypes);
									pOutputStream->OutputBR(2);
									pOutputStream->OutputFormattingCRLF();
								}
							}
						}
					}
				}
			}
			pOutputStream->OutputBR();
			pOutputStream->OutputFormattingCRLF();
		}


		// Show references if they exist
		if (lReferencePersonTagCount || lReferenceMarriageTagCount)
		{
			CDWordArray arReferences;
			long lReferenceCookie;
			long lReferenceId, lReferenceSubId;
			CString strHRef;
			CString strTitle, strDescription;

			// References Internal Link Tag
			pOutputStream->Output("<A name=InfoReferences></A>");
			pOutputStream->OutputFormattingCRLF();

			if (lReferencePersonTagCount)
			{
				// Person References Header
				pOutputStream->OutputSpan(E_PageBodyMediumHeader, "Documentation and Notes for Person:");
				pOutputStream->OutputBR(2);
				pOutputStream->OutputFormattingCRLF();

				if (pIndexReferenceTags->StartUniqueReferenceByPersonEnum(lPersonId, &lReferenceCookie, &arReferences))
				{
					while (pIndexReferenceTags->NextUniqueReferenceByPersonEnum(&lReferenceId, &lReferenceSubId, lPersonId, &lReferenceCookie, &arReferences))
					{
						Reference *pReference = m_pReferenceList->get(lReferenceId);
						if (pReference)
						{
							ReferenceEntry *pReferenceEntry = pReference->GetEntryWithId(lReferenceSubId);
							if (pReferenceEntry)
							{	// Show Reference Title
								strHRef.Format("%sreferences/%s%d.htm", m_pConfiguration->getBasePath(), REFERENCEFILENAME, lReferenceId);
								pOutputStream->OutputStandardBracketedLink(strHRef, "View Document");
								pOutputStream->OutputSpan(E_PageBodyNormalText, pReference->getTitle());
								pOutputStream->OutputBR();
								pOutputStream->OutputFormattingCRLF();

								// Show Entry Title/Description
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

								// Build the Tag Types string for this reference entry
								CString strTagTypes = pIndexReferenceTags->GetAllTagTypesStringForPersonAndEntry(lPersonId, lReferenceId, lReferenceSubId);
								pOutputStream->OutputSpan(E_PageBodySmallText, "Information Types: " + strTagTypes);
								pOutputStream->OutputBR();
								pOutputStream->OutputFormattingCRLF();
							}
						}
						pOutputStream->OutputBR();
						pOutputStream->OutputFormattingCRLF();
					}
				}
				pOutputStream->OutputBR();
				pOutputStream->OutputFormattingCRLF();
			}
			if (lReferenceMarriageTagCount)
			{
				// Marriages References Header
				pOutputStream->OutputSpan(E_PageBodyMediumHeader, "Documentation and Notes for Marriages:");
				if (lReferencePersonTagCount)
				{
					pOutputStream->OutputBR();
					pOutputStream->OutputFormattingCRLF();
					pOutputStream->OutputSpan(E_PageBodySmallHeader, "(May duplicate some of the documents listed above.)");
				}
				pOutputStream->OutputBR(2);
				pOutputStream->OutputFormattingCRLF();

				if (pIndexPersonToMarriages->StartMarriagesOfPersonEnum(lPersonId, &lMarriageCookie))
				{
					while (pIndexPersonToMarriages->NextMarriagesOfPersonEnum(lPersonId, &lMarriageCookie, &mId))
					{
						if (pIndexReferenceTags->StartUniqueReferenceByMarriageEnum(mId, &lReferenceCookie, &arReferences))
						{
							while (pIndexReferenceTags->NextUniqueReferenceByMarriageEnum(&lReferenceId, &lReferenceSubId, mId, &lReferenceCookie, &arReferences))
							{
								Reference *pReference = m_pReferenceList->get(lReferenceId);
								if (pReference)
								{
									ReferenceEntry *pReferenceEntry = pReference->GetEntryWithId(lReferenceSubId);
									if (pReferenceEntry)
									{	// Show Reference Title
										strHRef.Format("%sreferences/%s%d.htm#REF%dENT%d", m_pConfiguration->getBasePath(), REFERENCEFILENAME, lReferenceId, lReferenceId, lReferenceSubId);
										pOutputStream->OutputStandardBracketedLink(strHRef, "View Document");
										pOutputStream->OutputSpan(E_PageBodyNormalText, pReference->getTitle());
										pOutputStream->OutputBR();
										pOutputStream->OutputFormattingCRLF();

										// Show Entry Title/Description
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

										// Build "For Marriage..." string
										CString strForMarriage = FormsHtmlShared::BuildSimpleMarriageNameString(m_pConfiguration, mId, "For Marriage: %s and %s");
										pOutputStream->OutputSpan(E_PageBodySmallText, strForMarriage);
										pOutputStream->OutputBR();
										pOutputStream->OutputFormattingCRLF();

										// Build the Tag Types string for this reference entry
										CString strTagTypes = pIndexReferenceTags->GetAllTagTypesStringForMarriageAndEntry(mId, lReferenceId, lReferenceSubId);
										pOutputStream->OutputSpan(E_PageBodySmallText, "Information Types: " + strTagTypes);
										pOutputStream->OutputBR();
										pOutputStream->OutputFormattingCRLF();
									}
								}
								pOutputStream->OutputBR();
								pOutputStream->OutputFormattingCRLF();
							}
						}
					}
				}
			}
		}

		// Show Various and Sundries
		// Various and Sundries Internal Link Tag
		pOutputStream->Output("<A name=InfoSundries></A>");
		pOutputStream->OutputFormattingCRLF();

		// Various and Sundries Header
		pOutputStream->OutputSpan(E_PageBodyMediumHeader, "Various and Sundries:");
		pOutputStream->OutputBR(2);
		pOutputStream->OutputFormattingCRLF();

		// Full Decorated Name
		if (pPersonAccessor->getPersonIdAsLong() == 1207)
		{
			int y = 0;
			y++;
		}
		if (0 != pPersonAccessor->getFullBasicName().Compare(pPersonAccessor->getFullDecoratedName()))
		{
			pOutputStream->OutputSpan(E_PageBodyBoldNormalText, "Full Name: ");
			pOutputStream->OutputSpan(E_PageBodyNormalText, pPersonAccessor->getFullDecoratedName());
			pOutputStream->OutputBR();
			pOutputStream->OutputFormattingCRLF();
		}

		// First Name Alt Spellings
		if (0 != pPersonAccessor->getFirstNameAltSpellingCount())
		{
			pOutputStream->OutputSpan(E_PageBodyBoldNormalText, "First Name Alternate Spellings: ");
			for (int iAlt=0; iAlt<pPersonAccessor->getFirstNameAltSpellingCount(); iAlt++)
			{
				CString strAlt = pPersonAccessor->getFirstNameAltSpelling(iAlt);
				if (0 != iAlt)
				{
					pOutputStream->Output(", ");
				}
				pOutputStream->OutputSpan(E_PageBodyNormalText, strAlt);
			}
			pOutputStream->OutputBR();
			pOutputStream->OutputFormattingCRLF();
		}

		// Middle Name Alt Spellings
		if (0 != pPersonAccessor->getMiddleNameAltSpellingCount())
		{
			pOutputStream->OutputSpan(E_PageBodyBoldNormalText, "Middle Name Alternate Spellings: ");
			for (int iAlt=0; iAlt<pPersonAccessor->getMiddleNameAltSpellingCount(); iAlt++)
			{
				CString strAlt = pPersonAccessor->getMiddleNameAltSpelling(iAlt);
				if (0 != iAlt)
				{
					pOutputStream->Output(", ");
				}
				pOutputStream->OutputSpan(E_PageBodyNormalText, strAlt);
			}
			pOutputStream->OutputBR();
			pOutputStream->OutputFormattingCRLF();
		}

		// Last Name Alt Spellings
		if (0 != pPersonAccessor->getLastNameAltSpellingCount())
		{
			pOutputStream->OutputSpan(E_PageBodyBoldNormalText, "Last Name Alternate Spellings: ");
			for (int iAlt=0; iAlt<pPersonAccessor->getLastNameAltSpellingCount(); iAlt++)
			{
				CString strAlt = pPersonAccessor->getLastNameAltSpelling(iAlt);
				if (0 != iAlt)
				{
					pOutputStream->Output(", ");
				}
				pOutputStream->OutputSpan(E_PageBodyNormalText, strAlt);
			}
			pOutputStream->OutputBR();
			pOutputStream->OutputFormattingCRLF();
		}

		// AFN Id
		if (0 != pPersonAccessor->getAfn().GetLength())
		{
			pOutputStream->OutputSpan(E_PageBodyBoldNormalText, "AFN: ");
			pOutputStream->OutputSpan(E_PageBodyNormalText, pPersonAccessor->getAfn());
			pOutputStream->OutputBR();
			pOutputStream->OutputFormattingCRLF();
		}

		// Relationship
		pOutputStream->OutputSpan(E_PageBodyBoldNormalText, "Relationship: ");
		pOutputStream->OutputSpan(E_PageBodyNormalText, pPersonAccessor->getRelationship());
		pOutputStream->OutputBR();
		pOutputStream->OutputFormattingCRLF();
		
		// NickNames
		if (pPersonAccessor->getNickNameCount())
		{
			pOutputStream->OutputSpan(E_PageBodyBoldNormalText, "NickNames: ");
			for (int iNick=0; iNick<pPersonAccessor->getNickNameCount(); iNick++)
			{
				CString strNickName = pPersonAccessor->getNickName(iNick);
				if (0 != iNick)
				{
					pOutputStream->Output(", ");
				}
				pOutputStream->OutputSpan(E_PageBodyNormalText, strNickName);
			}
			pOutputStream->OutputBR();
			pOutputStream->OutputFormattingCRLF();
		}

		// Professions
		if (pPersonAccessor->getProfessionCount())
		{
			pOutputStream->OutputSpan(E_PageBodyBoldNormalText, "Professions: ");
			for (int iProf=0; iProf<pPersonAccessor->getProfessionCount(); iProf++)
			{
				CString strProfession = pPersonAccessor->getProfession(iProf);
				if (0 != iProf)
				{
					pOutputStream->Output(", ");
				}
				pOutputStream->OutputSpan(E_PageBodyNormalText, strProfession);
			}
			pOutputStream->OutputBR();
			pOutputStream->OutputFormattingCRLF();
		}

		// Religions
		if (pPersonAccessor->getReligionCount())
		{
			pOutputStream->OutputSpan(E_PageBodyBoldNormalText, "Religions: ");
			for (int iRelig=0; iRelig<pPersonAccessor->getReligionCount(); iRelig++)
			{
				CString strReligion = pPersonAccessor->getReligion(iRelig);
				if (0 != iRelig)
				{
					pOutputStream->Output(", ");
				}
				pOutputStream->OutputSpan(E_PageBodyNormalText, strReligion);
			}
			pOutputStream->OutputBR();
			pOutputStream->OutputFormattingCRLF();
		}

		// Cause of Death
		if (0 != pPersonAccessor->getDeathCause().GetLength())
		{
			pOutputStream->OutputSpan(E_PageBodyBoldNormalText, "Cause of Death: ");
			pOutputStream->OutputSpan(E_PageBodyNormalText, pPersonAccessor->getDeathCause());
			pOutputStream->OutputBR();
			pOutputStream->OutputFormattingCRLF();
		}
		pOutputStream->OutputBR();

		// Show Person Event Data
		// Person Event Internal Link Tag
		long lEventCookie;
		if (pPersonAccessor->startPersonEventGroupEnum(&lEventCookie))
		{
			pOutputStream->Output("<A name=InfoEvents></A>");
			pOutputStream->OutputFormattingCRLF();

			// Person Event Header
			pOutputStream->OutputSpan(E_PageBodyMediumHeader, "Events:");
			pOutputStream->OutputBR(2);
			pOutputStream->OutputFormattingCRLF();

			PersonEvent *pPersonEvent;
			while (pPersonAccessor->nextPersonEventGroupEnum(&lEventCookie, &pPersonEvent))
			{
				CString strTitle, strDescription = "";
				Paragraph *pTitleParagraph = pPersonEvent->getEventTitle()->getParagraph();
				if (pTitleParagraph)
				{
					strTitle = FormsHtmlShared::BuildParagraphString(m_pConfiguration, pTitleParagraph, false, false, NULL);
				}
				PersonEventDescription *pEventDescription = pPersonEvent->getEventDescription();
				if (pEventDescription)
				{
					for (WORD i=1; true; i++)
					{
						Paragraph *pParagraph = pEventDescription->getNthParagraph(i);
						if (NULL == pParagraph)
						{
							break;
						}
						strDescription += FormsHtmlShared::BuildParagraphString(m_pConfiguration, pParagraph, true, true, NULL); 
					}
				}
				if (0 != strTitle.GetLength())
				{
					pOutputStream->OutputSpan(E_PageBodyBoldNormalText, "Title: ");
					pOutputStream->OutputSpan(E_PageBodyNormalText, strTitle);
					pOutputStream->OutputBR();
					pOutputStream->OutputFormattingCRLF();
				}

				Date *pDate = pPersonEvent->getEventDate();
				Place *pPlace = pPersonEvent->getEventPlace();
				CString strTag = pPersonEvent->getTag();

				if (0 != strTag.GetLength())
				{
					pOutputStream->OutputSpan(E_PageBodyBoldNormalText, "Type: ");
					pOutputStream->OutputSpan(E_PageBodyNormalText, strTag);
					pOutputStream->OutputBR();
					pOutputStream->OutputFormattingCRLF();
				}
				if (!pDate->isEmpty())
				{
					pOutputStream->OutputSpan(E_PageBodyBoldNormalText, "Date: ");
					pOutputStream->OutputSpan(E_PageBodyNormalText, pDate->getDate());
					pOutputStream->OutputBR();
					pOutputStream->OutputFormattingCRLF();
				}
				if (!pPlace->isEmpty())
				{
					pOutputStream->OutputSpan(E_PageBodyBoldNormalText, "Place: ");
					pOutputStream->OutputSpan(E_PageBodyNormalText, pPlace->getPlace());
					pOutputStream->OutputBR();
					pOutputStream->OutputFormattingCRLF();
				}

				if (0 != strDescription.GetLength())
				{
					pOutputStream->OutputSpan(E_PageBodyBoldNormalText, "Description: ");
					pOutputStream->OutputBR();
					pOutputStream->OutputFormattingCRLF();
					pOutputStream->OutputSpan(E_PageBodyNormalText, strDescription);
					pOutputStream->OutputBR();
					pOutputStream->OutputFormattingCRLF();
				}
			}
			pOutputStream->OutputBR();
		}


		if (m_pConfiguration->getIncludeLds() && m_pFamily->doesLdsDataExist(pPerson))
		{
			// Show LDS Data
			// Various and Sundries Internal Link Tag
			pOutputStream->Output("<A name=InfoLds></A>");
			pOutputStream->OutputFormattingCRLF();

			// LDS Data Header
			pOutputStream->OutputSpan(E_PageBodyMediumHeader, "LDS Data:");
			pOutputStream->OutputBR(2);
			pOutputStream->OutputFormattingCRLF();

			OutputBaptism(pPerson, pOutputStream);
			OutputEndowment(pPerson, pOutputStream);
			OutputSealToParents(pPerson, pOutputStream);
			OutputSealToSpouse(pPerson, pOutputStream);
		}


		pOutputStream->OutputSidebarBackEnd();

		if (pPersonAccessor)
		{
			delete pPersonAccessor;
		}
	}
	if (pOutputStream)
	{
		delete pOutputStream;
	}
	m_pStatus->UpdateStatus("Person Info: " + strFileName);
}


void FormsHtmlPersonInfo::OutputBaptism(Person *pPerson, FormsHtmlOutputStream *pOutputStream)
{
	FormsPersonAccessor personAccessor(pPerson, m_pConfiguration->getSuppressLiving());
	FormsTagFinderHtml refFinder(m_pConfiguration);
	CString strDate = personAccessor.getBaptismDate();
	CString strPlace = personAccessor.getBaptismPlace();
	CString strTemple = personAccessor.getBaptismTemple();
	CString strProxyName = personAccessor.getBaptismProxyName();
	if ((0 != strDate.GetLength()) ||
		(0 != strPlace.GetLength()) ||
		(0 != strTemple.GetLength()) ||
		(0 != strProxyName.GetLength()))
	{
		if (0 != strDate.GetLength())
		{
			refFinder.FindForPerson(pPerson->getPersonIdAsLong(), "BapD");
			pOutputStream->OutputSpan(E_PageBodyBoldNormalText, "Baptised:&nbsp;");
			if (refFinder.Found())
			{
				pOutputStream->OutputStartAnchor(refFinder.GetHRef());
			}
			pOutputStream->OutputSpan(E_PageBodyNormalText, strDate);
			if (refFinder.Found())
			{
				pOutputStream->OutputEndAnchor();
			}
			pOutputStream->OutputBR();
			pOutputStream->OutputFormattingCRLF();
		}
		if (0 != strPlace.GetLength())
		{
			refFinder.FindForPerson(pPerson->getPersonIdAsLong(), "BapP");
			pOutputStream->OutputSpan(E_PageBodyBoldNormalText, "Baptised Place:&nbsp;");
			if (refFinder.Found())
			{
				pOutputStream->OutputStartAnchor(refFinder.GetHRef());
			}
			pOutputStream->OutputSpan(E_PageBodyNormalText, strPlace);
			if (refFinder.Found())
			{
				pOutputStream->OutputEndAnchor();
			}
			pOutputStream->OutputBR();
			pOutputStream->OutputFormattingCRLF();
		}
		if (0 != strTemple.GetLength())
		{
			refFinder.FindForPerson(pPerson->getPersonIdAsLong(), "BapTemple");
			pOutputStream->OutputSpan(E_PageBodyBoldNormalText, "Baptised Temple:&nbsp;");
			if (refFinder.Found())
			{
				pOutputStream->OutputStartAnchor(refFinder.GetHRef());
			}
			pOutputStream->OutputSpan(E_PageBodyNormalText, strTemple);
			if (refFinder.Found())
			{
				pOutputStream->OutputEndAnchor();
			}
			pOutputStream->OutputBR();
			pOutputStream->OutputFormattingCRLF();
		}
		if (0 != strProxyName.GetLength())
		{
			refFinder.FindForPerson(pPerson->getPersonIdAsLong(), "BapProxy");
			pOutputStream->OutputSpan(E_PageBodyBoldNormalText, "Baptised Proxy:&nbsp;");
			if (refFinder.Found())
			{
				pOutputStream->OutputStartAnchor(refFinder.GetHRef());
			}
			pOutputStream->OutputSpan(E_PageBodyNormalText, strProxyName);
			if (refFinder.Found())
			{
				pOutputStream->OutputEndAnchor();
			}
			pOutputStream->OutputBR();
			pOutputStream->OutputFormattingCRLF();
		}
	}
}


void FormsHtmlPersonInfo::OutputEndowment(Person *pPerson, FormsHtmlOutputStream *pOutputStream)
{
	FormsPersonAccessor personAccessor(pPerson, m_pConfiguration->getSuppressLiving());
	FormsTagFinderHtml refFinder(m_pConfiguration);
	CString strDate = personAccessor.getEndowmentDate();
	CString strPlace = personAccessor.getEndowmentPlace();
	CString strTemple = personAccessor.getEndowmentTemple();
	CString strProxyName = personAccessor.getEndowmentProxyName();
	if ((0 != strDate.GetLength()) ||
		(0 != strPlace.GetLength()) ||
		(0 != strTemple.GetLength()) ||
		(0 != strProxyName.GetLength()))
	{
		if (0 != strDate.GetLength())
		{
			refFinder.FindForPerson(pPerson->getPersonIdAsLong(), "EndD");
			pOutputStream->OutputSpan(E_PageBodyBoldNormalText, "Endowed:&nbsp;");
			if (refFinder.Found())
			{
				pOutputStream->OutputStartAnchor(refFinder.GetHRef());
			}
			pOutputStream->OutputSpan(E_PageBodyNormalText, strDate);
			if (refFinder.Found())
			{
				pOutputStream->OutputEndAnchor();
			}
			pOutputStream->OutputBR();
			pOutputStream->OutputFormattingCRLF();
		}
		if (0 != strPlace.GetLength())
		{
			refFinder.FindForPerson(pPerson->getPersonIdAsLong(), "EndP");
			pOutputStream->OutputSpan(E_PageBodyBoldNormalText, "Endowed Place:&nbsp;");
			if (refFinder.Found())
			{
				pOutputStream->OutputStartAnchor(refFinder.GetHRef());
			}
			pOutputStream->OutputSpan(E_PageBodyNormalText, strPlace);
			if (refFinder.Found())
			{
				pOutputStream->OutputEndAnchor();
			}
			pOutputStream->OutputBR();
			pOutputStream->OutputFormattingCRLF();
		}
		if (0 != strTemple.GetLength())
		{
			refFinder.FindForPerson(pPerson->getPersonIdAsLong(), "EndTemple");
			pOutputStream->OutputSpan(E_PageBodyBoldNormalText, "Endowed Temple:&nbsp;");
			if (refFinder.Found())
			{
				pOutputStream->OutputStartAnchor(refFinder.GetHRef());
			}
			pOutputStream->OutputSpan(E_PageBodyNormalText, strTemple);
			if (refFinder.Found())
			{
				pOutputStream->OutputEndAnchor();
			}
			pOutputStream->OutputBR();
			pOutputStream->OutputFormattingCRLF();
		}
		if (0 != strProxyName.GetLength())
		{
			refFinder.FindForPerson(pPerson->getPersonIdAsLong(), "EndProxy");
			pOutputStream->OutputSpan(E_PageBodyBoldNormalText, "Endowed Proxy:&nbsp;");
			if (refFinder.Found())
			{
				pOutputStream->OutputStartAnchor(refFinder.GetHRef());
			}
			pOutputStream->OutputSpan(E_PageBodyNormalText, strProxyName);
			if (refFinder.Found())
			{
				pOutputStream->OutputEndAnchor();
			}
			pOutputStream->OutputBR();
			pOutputStream->OutputFormattingCRLF();
		}
	}
}

void FormsHtmlPersonInfo::OutputSealToParents(Person *pPerson, FormsHtmlOutputStream *pOutputStream)
{
	FormsPersonAccessor personAccessor(pPerson, m_pConfiguration->getSuppressLiving());
	FormsTagFinderHtml refFinder(m_pConfiguration);
	CString strDate = personAccessor.getSToPDate();
	CString strPlace = personAccessor.getSToPPlace();
	CString strTemple = personAccessor.getSToPTemple();
	if ((0 != strDate.GetLength()) ||
		(0 != strPlace.GetLength()) ||
		(0 != strTemple.GetLength()))
	{
		if (0 != strDate.GetLength())
		{
			refFinder.FindForPerson(pPerson->getPersonIdAsLong(), "SToPD");
			pOutputStream->OutputSpan(E_PageBodyBoldNormalText, "Sealed To Parents Date:&nbsp;");
			if (refFinder.Found())
			{
				pOutputStream->OutputStartAnchor(refFinder.GetHRef());
			}
			pOutputStream->OutputSpan(E_PageBodyNormalText, strDate);
			if (refFinder.Found())
			{
				pOutputStream->OutputEndAnchor();
			}
			pOutputStream->OutputBR();
			pOutputStream->OutputFormattingCRLF();
		}
		if (0 != strPlace.GetLength())
		{
			refFinder.FindForPerson(pPerson->getPersonIdAsLong(), "SToPP");
			pOutputStream->OutputSpan(E_PageBodyBoldNormalText, "Sealed To Parents Place:&nbsp;");
			if (refFinder.Found())
			{
				pOutputStream->OutputStartAnchor(refFinder.GetHRef());
			}
			pOutputStream->OutputSpan(E_PageBodyNormalText, strPlace);
			if (refFinder.Found())
			{
				pOutputStream->OutputEndAnchor();
			}
			pOutputStream->OutputBR();
			pOutputStream->OutputFormattingCRLF();
		}
		if (0 != strTemple.GetLength())
		{
			refFinder.FindForPerson(pPerson->getPersonIdAsLong(), "SToPTemple");
			pOutputStream->OutputSpan(E_PageBodyBoldNormalText, "Sealed To Parents Temple:&nbsp;");
			if (refFinder.Found())
			{
				pOutputStream->OutputStartAnchor(refFinder.GetHRef());
			}
			pOutputStream->OutputSpan(E_PageBodyNormalText, strTemple);
			if (refFinder.Found())
			{
				pOutputStream->OutputEndAnchor();
			}
			pOutputStream->OutputBR();
			pOutputStream->OutputFormattingCRLF();
		}
	}
	if (0 != personAccessor.getSToPProxyCount())
	{
		for (int i=0; i<personAccessor.getSToPProxyCount(); i++)
		{
			CString strProxy = personAccessor.getSToPProxyName(i);
			if (0 != strProxy.GetLength())
			{
				pOutputStream->OutputSpan(E_PageBodyBoldNormalText, "Sealed To Parents Proxy:&nbsp;");
				pOutputStream->OutputSpan(E_PageBodyNormalText, strProxy);
				pOutputStream->OutputBR();
				pOutputStream->OutputFormattingCRLF();
			}
		}
	}
}

void FormsHtmlPersonInfo::OutputSealToSpouse(Person *pPerson, FormsHtmlOutputStream *pOutputStream)
{
	FormsPersonAccessor personAccessor(pPerson, m_pConfiguration->getSuppressLiving());
	IndexPersonToMarriages *pIndexPersonToMarriages = m_pIndex->GetIndexPersonToMarriages();
	IndexMarriageToSpouses *pIndexMarriageToSpouses = m_pIndex->GetIndexMarriageToSpouses();
	FormsTagFinderHtml refFinder(m_pConfiguration);
	long lPersonId = pPerson->getPersonIdAsLong();
	long lMarriageCookie;
	MARRIAGEID mId;

	if (pIndexPersonToMarriages->StartMarriagesOfPersonEnum(lPersonId, &lMarriageCookie))
	{
		while (pIndexPersonToMarriages->NextMarriagesOfPersonEnum(lPersonId, &lMarriageCookie, &mId))
		{
			Marriage *pMarriage = m_pMarriageList->get(mId);
			if (pMarriage)
			{
				FormsMarriageAccessor marriageAccessor(pMarriage, m_pConfiguration->getSuppressLiving(), m_pFamily);
				CString strDate = marriageAccessor.getSToSDate();
				CString strPlace = marriageAccessor.getSToSPlace();
				CString strTemple = marriageAccessor.getSToSTemple();
				long lProxyCount = marriageAccessor.getSToSProxyCount();
				if ((0 != strDate.GetLength()) ||
					(0 != strPlace.GetLength()) ||
					(0 != strTemple.GetLength()) ||
					(0 != lProxyCount))
				{
					// Get Spouse's Name
					CString strSpouse;
					if (lPersonId == marriageAccessor.getHusbandIdAsLong())
					{	// I am the husband, show my info sealed to the wife
						Person *pSpouse = m_pPersonList->get(marriageAccessor.getWifeIdAsLong());
						FormsPersonAccessor spouseAccessor(pSpouse, m_pConfiguration->getSuppressLiving());
						strSpouse = spouseAccessor.getFullBasicName();
					}
					else
					{	// I am the wife, show my info sealed to the husband
						Person *pSpouse = m_pPersonList->get(marriageAccessor.getHusbandIdAsLong());
						FormsPersonAccessor spouseAccessor(pSpouse, m_pConfiguration->getSuppressLiving());
						strSpouse = spouseAccessor.getFullBasicName();
					}
					pOutputStream->OutputSpan(E_PageBodyBoldNormalText, "Sealed to Spouse:&nbsp;");
					pOutputStream->OutputSpan(E_PageBodyNormalText, strSpouse);
					pOutputStream->OutputBR();
					pOutputStream->OutputFormattingCRLF();
					// Date
					if (0 != strDate.GetLength())
					{
						refFinder.FindForPerson(pPerson->getPersonIdAsLong(), "SToSD");
						pOutputStream->OutputSpan(E_PageBodyBoldNormalText, "&nbsp;&nbsp;Date:&nbsp;");
						if (refFinder.Found())
						{
							pOutputStream->OutputStartAnchor(refFinder.GetHRef());
						}
						pOutputStream->OutputSpan(E_PageBodyNormalText, strDate);
						if (refFinder.Found())
						{
							pOutputStream->OutputEndAnchor();
						}
						pOutputStream->OutputBR();
						pOutputStream->OutputFormattingCRLF();
					}
					// Place
					if (0 != strPlace.GetLength())
					{
						refFinder.FindForPerson(pPerson->getPersonIdAsLong(), "SToSP");
						pOutputStream->OutputSpan(E_PageBodyBoldNormalText, "&nbsp;&nbsp;Place:&nbsp;");
						if (refFinder.Found())
						{
							pOutputStream->OutputStartAnchor(refFinder.GetHRef());
						}
						pOutputStream->OutputSpan(E_PageBodyNormalText, strPlace);
						if (refFinder.Found())
						{
							pOutputStream->OutputEndAnchor();
						}
						pOutputStream->OutputBR();
						pOutputStream->OutputFormattingCRLF();
					}
					// Temple
					if (0 != strTemple.GetLength())
					{
						refFinder.FindForPerson(pPerson->getPersonIdAsLong(), "SToSTemple");
						pOutputStream->OutputSpan(E_PageBodyBoldNormalText, "&nbsp;&nbsp;Temple:&nbsp;");
						if (refFinder.Found())
						{
							pOutputStream->OutputStartAnchor(refFinder.GetHRef());
						}
						pOutputStream->OutputSpan(E_PageBodyNormalText, strTemple);
						if (refFinder.Found())
						{
							pOutputStream->OutputEndAnchor();
						}
						pOutputStream->OutputBR();
						pOutputStream->OutputFormattingCRLF();
					}
					if (0 != lProxyCount)
					{
						for (int i=0; i<lProxyCount; i++)
						{
							CString strProxy = marriageAccessor.getSToSProxyName(i);
							if (0 != strProxy.GetLength())
							{
								pOutputStream->OutputSpan(E_PageBodyBoldNormalText, "&nbsp;&nbsp;Proxy:&nbsp;");
								pOutputStream->OutputSpan(E_PageBodyNormalText, strProxy);
								pOutputStream->OutputBR();
								pOutputStream->OutputFormattingCRLF();
							}
						}
					}
				}
			}
		}
	}
	pOutputStream->OutputBR();
	pOutputStream->OutputFormattingCRLF();

}




 
 
*/