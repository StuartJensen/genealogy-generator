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
import home.genealogy.lists.PlaceList;
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
	private PlaceList m_placeList;
	private PersonList m_personList;
	private MarriageList m_marriageList;
	private ReferenceList m_referenceList;
	private PhotoList m_photoList;
	private boolean m_bSuppressLiving;
	private CommandLineParameters m_commandLineParameters;
	private IOutputStream m_outputStream;
	  
	public HTMLPersonInfoForm(CFGFamily family,
							  PlaceList placeList,
							  PersonList personList,
							  MarriageList marriageList,
							  ReferenceList referenceList,
							  PhotoList photoList,
							  boolean bSuppressLiving,
							  CommandLineParameters commandLineParameters,
							  IOutputStream outputStream)
	{
		m_family = family;
		m_placeList = placeList;
		m_personList = personList;
		m_marriageList = marriageList;
		m_referenceList = referenceList;
		m_photoList = photoList;
		m_bSuppressLiving = bSuppressLiving;
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
			createPersonInfo(m_family, m_placeList, m_personList, m_marriageList, m_referenceList,
 					         m_photoList, m_bSuppressLiving, idxPerToParentMar,
 					         idxPerToMar, idxMarToSpouses, idxPerToPhoto, idxMarToPhoto,
 					         idxPerToReference, idxMarToReference, strOutputPath, thisPerson);
		}
		m_outputStream.output("Completed Generating All Person Info Files\n");	
	}
	
	private void createPersonInfo(CFGFamily family,
								 PlaceList placelist,
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
		PersonHelper personHelper = new PersonHelper(person, bSuppressLiving, placelist);
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

		
		// Third Line "(events)"
		if (!personHelper.getIsPersonLiving())
		{
			if (0 != PersonHelper.getEventCount(person))
			{
				output.outputStartAnchor("#InfoEvents");
				output.output("(Events)");	
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
			PersonHelper fatherHelper = new PersonHelper(personFather, bSuppressLiving, placelist);
			String strHRef = m_family.getUrlPrefix() + PERSON_INFO_FILE_SYSTEM_SUBDIRECTORY + "\\" + HTMLShared.PERINFOFILENAME + personFather.getPersonId() + ".htm";
			output.outputStandardBracketedLink(strHRef, "View Father");
			output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyNormalText, fatherHelper.getPersonName());
			output.outputBR();
			output.outputCRLF();
			// Mother
			PersonHelper motherHelper = new PersonHelper(personMother, bSuppressLiving, placelist);
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
					PersonHelper husbandHelper = new PersonHelper(husband, bSuppressLiving, m_placeList);
					PersonHelper wifeHelper = new PersonHelper(wife, bSuppressLiving, m_placeList);
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
								  PhotoHelper.getDescription(photo), placelist, personList, m_marriageList,
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
												  PhotoHelper.getDescription(photo), placelist, personList, m_marriageList,
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
										String strForMarriage = HTMLShared.buildSimpleMarriageNameString(placelist, personList, idxMarToSpouses, iMarriageId, m_bSuppressLiving, "%s and %s");
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
											paragraph, placelist, personList, marriageList, referenceList, photoList,
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
															paragraph, placelist, personList, marriageList, referenceList, photoList,
									                        idxMarToSpouses, true, true, bSuppressLiving, null,
									                        iReferenceId, iEntryId);
													output.output(strEntryTitle);
												}
												output.outputCRLF();
											}
								
											// Build "For Marriage..." string
											String strForMarriage = HTMLShared.buildSimpleMarriageNameString(placelist, personList, idxMarToSpouses, iMarriageId, m_bSuppressLiving, "%s and %s");
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
			output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyBoldNormalText, "Profession(s): ");
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
			output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyBoldNormalText, "Religion(s): ");
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
		List<String> lCauseOfDeath = PersonHelper.getDeathCause(person);
		if (!lCauseOfDeath.isEmpty())
		{
			output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyBoldNormalText, "Cause(s) of Death: ");
			for (String strCauseOfDeath : lCauseOfDeath)
			{
				output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyNormalText, strCauseOfDeath);
				output.outputBR();
			}
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
								paraTitle, placelist, personList, marriageList, referenceList, photoList,
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
					String strEventPlace = eventHelper.getEventPlace(placelist);
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
										commandLineParameters, paraDescription, placelist, personList, marriageList, referenceList,
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
		
		output.outputSidebarBackEnd();
		
		output.commit();
		output = null;
	}
}
