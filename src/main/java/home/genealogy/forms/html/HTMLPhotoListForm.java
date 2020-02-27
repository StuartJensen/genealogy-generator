package home.genealogy.forms.html;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import home.genealogy.CommandLineParameters;
import home.genealogy.configuration.CFGFamily;
import home.genealogy.indexes.IndexMarriageToPhotos;
import home.genealogy.indexes.IndexMarriageToSpouses;
import home.genealogy.indexes.IndexPersonToPhotos;
import home.genealogy.indexes.TaggedContainerDescriptor;
import home.genealogy.lists.MarriageList;
import home.genealogy.lists.PersonList;
import home.genealogy.lists.PhotoList;
import home.genealogy.lists.ReferenceList;
import home.genealogy.output.IOutputStream;
import home.genealogy.schema.all.Marriage;
import home.genealogy.schema.all.Paragraph;
import home.genealogy.schema.all.Person;
import home.genealogy.schema.all.Photo;
import home.genealogy.schema.all.PublishedIn;
import home.genealogy.schema.all.ReferenceEntryId;
import home.genealogy.schema.all.Singleton;
import home.genealogy.schema.all.extensions.PersonSortableByName;
import home.genealogy.schema.all.helpers.MarriageHelper;
import home.genealogy.schema.all.helpers.PersonHelper;
import home.genealogy.schema.all.helpers.PhotoHelper;
import home.genealogy.schema.all.helpers.PhotoIdHelper;
import home.genealogy.schema.all.helpers.SingletonHelper;

public class HTMLPhotoListForm
{
	public static final String PHOTO_INDEX_FILE_SYSTEM_SUBDIRECTORY = "index";
	
	private CFGFamily m_family;
	private PersonList m_personList;
	private MarriageList m_marriageList;
	private ReferenceList m_referenceList;
	private PhotoList m_photoList;
	private boolean m_bSuppressLiving;
	private CommandLineParameters m_commandLineParameters;
	private IOutputStream m_outputStream;
	private IndexMarriageToSpouses m_indexMarrToSpouses;
	private IndexPersonToPhotos m_idxPersonToPhotos;
	private IndexMarriageToPhotos m_idxMarriageToPhotos;
	  
	public HTMLPhotoListForm(CFGFamily family,
							     PersonList personList,
							     MarriageList marriageList,
							     ReferenceList referenceList,
							     PhotoList photoList,
							     IndexMarriageToSpouses indexMarrToSpouses,
							     boolean bSuppressLiving,
							     CommandLineParameters commandLineParameters,
							     IOutputStream outputStream)
	{
		m_family = family;
		m_personList = personList;
		m_marriageList = marriageList;
		m_referenceList = referenceList;
		m_photoList = photoList;
		m_bSuppressLiving = bSuppressLiving;
		m_commandLineParameters = commandLineParameters;
		m_outputStream = outputStream;
		m_indexMarrToSpouses = indexMarrToSpouses;
		m_idxPersonToPhotos = new IndexPersonToPhotos(m_family, m_personList, m_photoList);
		m_idxMarriageToPhotos = new IndexMarriageToPhotos(m_family, m_marriageList, m_photoList);
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
		File fSubdirectory = new File(strOutputPath + PHOTO_INDEX_FILE_SYSTEM_SUBDIRECTORY);
		if (!fSubdirectory.exists())
		{
			if (!fSubdirectory.mkdirs())
			{
				throw new Exception("Error creating sub-directory tree for reference list file!");
			}
		}

		String strFileName = strOutputPath + PHOTO_INDEX_FILE_SYSTEM_SUBDIRECTORY + "\\" + HTMLShared.PHOTOINDEXFILENAME + ".htm";
		m_outputStream.output("Generating Photo List: " + strFileName+ "\n");
		HTMLFormOutput output = new HTMLFormOutput(strFileName);

		// Start document creation
		String strTitle = "Photo Index";
		output.outputSidebarFrontEnd(strTitle, m_family, m_personList, m_marriageList);
		
		// Show internal links to sections of the document
		output.output("<center><span class=\"pageBodySmallLink\">");

		output.outputStartAnchor("#PeoplePhotos");
		output.output("(People Photos)");
		output.outputEndAnchor();
		output.output("&nbsp;&nbsp;");

		output.outputStartAnchor("#MarriagePhotos");
		output.output("(Marriage Photos)");
		output.outputEndAnchor();
		output.output("&nbsp;&nbsp;");

		output.outputStartAnchor("#PublishedPhotos");
		output.output("(Published Photos)");
		output.outputEndAnchor();
		output.output("&nbsp;&nbsp;");
		
		output.output("</span></center><br>");

		// Show Photos From Person Categories
		output.output("<A name=\"PeoplePhotos\"></A>");
		output.outputCRLF();
		
		// Create sorted array of persons
		ArrayList<PersonSortableByName> alSortedPersons = new ArrayList<PersonSortableByName>(); 
		Iterator<Person> iter = m_personList.getPersons();
		while (iter.hasNext())
		{
			alSortedPersons.add(new PersonSortableByName(iter.next()));
		}
		m_outputStream.output("Sorting Persons by name...\n");
		Collections.sort(alSortedPersons);

		// For each person, get their associates photos
		for (int p=0; p<alSortedPersons.size(); p++)
		{
			PersonSortableByName personByName = alSortedPersons.get(p);
			ArrayList<TaggedContainerDescriptor> alPhotoDescriptors = m_idxPersonToPhotos.getPhotosForPerson(PersonHelper.getPersonId(personByName.getPerson()));
			if ((null != alPhotoDescriptors) && (0 != alPhotoDescriptors.size()))
			{
				PersonHelper personHelper = new PersonHelper(personByName.getPerson(), m_bSuppressLiving);
				String strPersonName = personHelper.getPersonName();
				output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodySmallHeader, strPersonName);
				output.outputBR(2);
				output.outputCRLF();
				// Show Photos for this person
				for (int d=0; d<alPhotoDescriptors.size(); d++)
				{
					TaggedContainerDescriptor photoDescriptor = alPhotoDescriptors.get(d);
					long lPhotoId = photoDescriptor.getContainerId();
					Photo photo = m_photoList.get((int)lPhotoId);
					if (null != photo)
					{
						showPhoto(photo, output);
					}
				}
			}
		}
	
		// Show Photos From Marriage Categories
		output.output("<A name=\"MarriagePhotos\"></A>");
		output.outputCRLF();
				
		output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyMediumHeader, "Photos Associated With Marriages");
		output.outputBR(2);
		output.outputCRLF();
		
		// For each marriage, get their associates photos
		Iterator<Marriage> iterMarriages = m_marriageList.getMarriages();
		while (iterMarriages.hasNext())
		{
			Marriage marriage = iterMarriages.next();
			ArrayList<TaggedContainerDescriptor> alPhotoDescriptors = m_idxMarriageToPhotos.getPhotosForMarriage(MarriageHelper.getMarriageId(marriage));
			if ((null != alPhotoDescriptors) && (0 != alPhotoDescriptors.size()))
			{
				String strMarriageName = HTMLShared.buildSimpleMarriageNameString(m_personList, m_indexMarrToSpouses, MarriageHelper.getMarriageId(marriage), m_bSuppressLiving, "%s and %s");
				output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodySmallHeader, strMarriageName);
				output.outputBR(2);
				output.outputCRLF();
				// Show Photos for this marriage
				for (int d=0; d<alPhotoDescriptors.size(); d++)
				{
					TaggedContainerDescriptor photoDescriptor = alPhotoDescriptors.get(d);
					long lPhotoId = photoDescriptor.getContainerId();
					Photo photo = m_photoList.get((int)lPhotoId);
					if (null != photo)
					{
						showPhoto(photo, output);
					}
				}
			}
		}
		
		// Show Photos That are Published in a Reference
		output.output("<A name=\"PublishedPhotos\"></A>");
		output.outputCRLF();
				
		output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodyMediumHeader, "Published Photos");
		output.outputBR(2);
		output.outputCRLF();
		
		Iterator<Photo> iterPhotos = m_photoList.getPhotos();
		while (iterPhotos.hasNext())
		{
			Photo photo = iterPhotos.next();
			if (PhotoHelper.containsPublishedIn(photo))
			{
				showPhoto(photo, output);
			}
		}
		
		output.outputSidebarBackEnd();
		output.commit();
		output = null;
		m_outputStream.output("Completed Generating All Photo List File\n");
	}
	
	private void showPhoto(Photo photo, HTMLFormOutput output)
	{
		String strHRef, strWork;
		if (null != photo)
		{
			int iPhotoId = PhotoHelper.getPhotoId(photo);
			// Photo Title
			strHRef = m_family.getUrlPrefix() + HTMLShared.PHOTOWRAPDIR + "/" + HTMLShared.PHOTOWRAPFILENAME + iPhotoId + ".htm";
			output.outputStandardBracketedLink(strHRef, "View Photo");
			output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodySmallText, "&nbsp;&nbsp;<b>Title:</b>&nbsp;");
			List<Paragraph> lDescription = PhotoHelper.getDescription(photo);
			for (int i=0; i<lDescription.size(); i++)
			{
				String strParagraph = HTMLShared.buildParagraphString(m_family, m_commandLineParameters,
						lDescription.get(i), m_personList, m_marriageList,
	                    m_referenceList, m_photoList,
	                    m_indexMarrToSpouses,
	                    true,
	                    true,
	                    m_bSuppressLiving,
	                    new HTMLParagraphFormat("pageBodyNormalText"),
	                    iPhotoId,
	                    PhotoIdHelper.PHOTOID_INVALID);
				output.output(strParagraph);
			}
			output.outputBR(1);
			output.outputCRLF();
			
			// Photo Number
			strWork = "<b>Photo Number:</b> " + iPhotoId;
			output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodySmallText, strWork);
			output.outputBR();
			output.outputCRLF();

			if (PhotoHelper.containsSingleton(photo))
			{
				Singleton singleton = PhotoHelper.getSingleton(photo);
				String strDate = SingletonHelper.getDate(singleton);
				String strPlace = SingletonHelper.getPlace(singleton);
				boolean bDateEmpty = ((null == strDate) || (0 == strDate.length()));
				boolean bPlaceEmpty = ((null == strPlace) || (0 == strPlace.length()));
				if (!bDateEmpty)
				{
					output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodySmallText, "<b>Date:</b>&nbsp;" + strDate);
					output.outputBR();
					output.outputCRLF();
				}
				if (!bPlaceEmpty)
				{
					output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodySmallText, "<b>Place:</b>&nbsp;" + strPlace);
					output.outputBR();
					output.outputCRLF();
				}
			}
			else if (PhotoHelper.containsPublishedIn(photo))
			{
				PublishedIn publishedIn = PhotoHelper.getPublishedIn(photo);
				List<ReferenceEntryId> lRefEntIds = publishedIn.getReferenceEntryId();
				
				strWork = "Photo published in " + lRefEntIds.size() + " documents.";
				output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodySmallText, strWork);
				output.outputBR();
				output.outputCRLF();
			}
			output.outputBR();
		}
	}

}
