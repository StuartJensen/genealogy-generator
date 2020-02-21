package home.genealogy.action.http;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.List;

import home.genealogy.Genealogy;
import home.genealogy.configuration.CFGFamily;
import home.genealogy.forms.html.HTMLFormOutput;
import home.genealogy.forms.html.HTMLShared;
import home.genealogy.lists.MarriageList;
import home.genealogy.lists.PersonList;
import home.genealogy.lists.PhotoList;
import home.genealogy.lists.ReferenceList;
import home.genealogy.output.IOutputStream;
import home.genealogy.schema.all.Marriage;
import home.genealogy.schema.all.Person;
import home.genealogy.schema.all.Photo;
import home.genealogy.schema.all.Reference;
import home.genealogy.schema.all.helpers.FileHelper;
import home.genealogy.schema.all.helpers.MarriageHelper;
import home.genealogy.schema.all.helpers.PersonHelper;
import home.genealogy.schema.all.helpers.PhotoHelper;
import home.genealogy.schema.all.helpers.ReferenceHelper;
import home.genealogy.util.CommandLineParameterList;

public class WebSiteValidator
{
	private CFGFamily m_family;
	private PersonList m_personList;
	private MarriageList m_marriageList;
	private ReferenceList m_referenceList;
	private PhotoList m_photoList;
	private CommandLineParameterList m_listCLP;
	private String m_strType;
	private IOutputStream m_outputStream;
	
	private static final int SLEEP_TIME = 200;
	  
	public WebSiteValidator(CFGFamily family,
							PersonList personList,
							MarriageList marriageList,
							ReferenceList referenceList,
							PhotoList photoList,
							CommandLineParameterList listCLP,
							String strType,
							IOutputStream outputStream)
	{
		m_family = family;
		m_personList = personList;
		m_marriageList = marriageList;
		m_referenceList = referenceList;
		m_photoList = photoList;
		m_listCLP = listCLP;
		m_strType = strType;
		m_outputStream = outputStream;
	}
	
	public void validate()
		throws Exception
	{	
		if ((m_strType.equals(Genealogy.COMMAND_LINE_PARAM_ACTION_VALUE_HTTPVALIDATE_TYPE_ALL)) ||
			(m_strType.equals(Genealogy.COMMAND_LINE_PARAM_ACTION_VALUE_HTTPVALIDATE_TYPE_FGS)))
		{	// Validate that all required FGS exist
			int iFGSCount = 0;
			m_outputStream.output("Validating that all the Family Group Sheet Pages exist!\n");
			Iterator<Marriage> iterMarriage = m_marriageList.getMarriages();
			while (iterMarriage.hasNext())
			{
				Marriage marriage = iterMarriage.next();
				int iMarriageId = MarriageHelper.getMarriageId(marriage);
				String strUrl = m_family.getUrlPrefix() + HTMLShared.FGSDIR + "/" + HTMLShared.FGSFILENAME + iMarriageId + ".htm";
				int iResponseCode = doHttpGet(strUrl);
				if (iResponseCode != HttpURLConnection.HTTP_OK)
				{
					m_outputStream.output("FGS: " + iMarriageId + ", Code: " + iResponseCode + ": Url: " + strUrl + "\n");
				}	
				if (0 == (++iFGSCount % 500))
				{
					m_outputStream.output("Validating Family Group Sheet Pages progress: " + iFGSCount + "\n");
				}
				try{Thread.sleep(SLEEP_TIME);}catch(Throwable t){}
			}
			m_outputStream.output("Validated " + iFGSCount + " Family Group Sheet Pages.\n");
		}
		
		if ((m_strType.equals(Genealogy.COMMAND_LINE_PARAM_ACTION_VALUE_HTTPVALIDATE_TYPE_ALL)) ||
			(m_strType.equals(Genealogy.COMMAND_LINE_PARAM_ACTION_VALUE_HTTPVALIDATE_TYPE_PERSONINFO)))
		{	// Validate that all required Person Infos exist
			int iPersonInfoCount = 0;
			m_outputStream.output("Validating the all Person Information Pages exist!");
			Iterator<Person> iterPerson = m_personList.getPersons();
			while (iterPerson.hasNext())
			{
				Person person = iterPerson.next();
				int iPersonId = PersonHelper.getPersonId(person);
				String strUrl = m_family.getUrlPrefix() + HTMLShared.PERINFODIR + "/" + HTMLShared.PERINFOFILENAME + iPersonId + ".htm";
				int iResponseCode = doHttpGet(strUrl);
				if (iResponseCode != HttpURLConnection.HTTP_OK)
				{
					m_outputStream.output("PersonInfo: " + iPersonId + ", Code: " + iResponseCode + ": Url: " + strUrl + "\n");
				}
				if (0 == (++iPersonInfoCount % 500))
				{
					m_outputStream.output("Validating Person Information Pages progress: " + iPersonInfoCount + "\n");
				}
				try{Thread.sleep(SLEEP_TIME);}catch(Throwable t){}
			}
			m_outputStream.output("Validated " + iPersonInfoCount + " Person Information Pages.\n");
		}
		
		if ((m_strType.equals(Genealogy.COMMAND_LINE_PARAM_ACTION_VALUE_HTTPVALIDATE_TYPE_ALL)) ||
			(m_strType.equals(Genealogy.COMMAND_LINE_PARAM_ACTION_VALUE_HTTPVALIDATE_TYPE_REFERENCES)))
		{	// Validate that all required References exist
			int iReferenceCount = 0;		
			m_outputStream.output("Validating that all the Reference Pages exist!\n");
			Iterator<Reference> iterReference = m_referenceList.getReferences();
			while (iterReference.hasNext())
			{
				Reference reference = iterReference.next();
				int iReferenceId = ReferenceHelper.getReferenceId(reference);
				String strUrl = m_family.getUrlPrefix() + HTMLShared.REFERENCEDIR + "/" + HTMLShared.REFERENCEFILENAME + iReferenceId + ".htm";
				int iResponseCode = doHttpGet(strUrl);
				if (iResponseCode != HttpURLConnection.HTTP_OK)
				{
					m_outputStream.output("Reference: " + iReferenceId + ", Code: " + iResponseCode + ": Url: " + strUrl);
				}
				if (0 == (++iReferenceCount % 100))
				{
					m_outputStream.output("Validating Reference Pages progress: " + iReferenceCount + "\n");
				}
				try{Thread.sleep(SLEEP_TIME);}catch(Throwable t){}
			}
			m_outputStream.output("Validated " + iReferenceCount + " Reference Pages.\n");
		}

		if ((m_strType.equals(Genealogy.COMMAND_LINE_PARAM_ACTION_VALUE_HTTPVALIDATE_TYPE_ALL)) ||
			(m_strType.equals(Genealogy.COMMAND_LINE_PARAM_ACTION_VALUE_HTTPVALIDATE_TYPE_PHOTOS)))
		{		// Validate that all required Photo Wrappers and Photos exist
			int iPhotoWrappersCount = 0;
			int iPhotoCount = 0;
			int iPhotoIgnored = 0;
			m_outputStream.output("Validating that all the Photo Wrapper Pages and Photos exist!\n");
			Iterator<Photo> iterPhoto = m_photoList.getPhotos();
			while (iterPhoto.hasNext())
			{	
				Photo photo = iterPhoto.next();			
				int iPhotoId = PhotoHelper.getPhotoId(photo);
				// First check the photo wrapper
				String strUrl = m_family.getUrlPrefix() + HTMLShared.PHOTOWRAPDIR + "/" + HTMLShared.PHOTOWRAPFILENAME + iPhotoId + ".htm";
				int iResponseCode = doHttpGet(strUrl);
				if (iResponseCode != HttpURLConnection.HTTP_OK)
				{
					m_outputStream.output("PhotoWrapper: " + iPhotoId + ", Code: " + iResponseCode + ": Url: " + strUrl + "\n");
				}
				iPhotoWrappersCount++;
				// Second, check the actual photo files
				
				String strAccess = PhotoHelper.getAccess(photo);
				if ((null == strAccess) || (0 == strAccess.length()) || (strAccess.equals(PhotoHelper.ACCESS_PUBLIC)))
				{
					String strPackage = PhotoHelper.getPackage(photo);
					String strBaseFileName = PhotoHelper.getBaseFileName(photo);
					List<home.genealogy.schema.all.File> lFiles = PhotoHelper.getFiles(photo);
					for (int f=0; f<lFiles.size(); f++)
					{
						home.genealogy.schema.all.File file = lFiles.get(f);
						FileHelper.eFileType eType = FileHelper.getType(file);
						if ((eType == FileHelper.eFileType.eJPG) ||
							(eType == FileHelper.eFileType.eGIF) ||
							(eType == FileHelper.eFileType.ePNG))
						{
							String strPhotoFileName = "photos/" + strPackage + "/" + strBaseFileName + FileHelper.getUniqueId(file) + "." + FileHelper.getTypeExtension(file);
							strPhotoFileName = HTMLShared.replaceBkSlashWithPhotoFileNameSeparator(strPhotoFileName);
							strPhotoFileName = strPhotoFileName.toLowerCase();
							strUrl = m_family.getUrlPrefix() + strPhotoFileName;
							iResponseCode = doHttpGet(strUrl);
							if (iResponseCode != HttpURLConnection.HTTP_OK)
							{
								m_outputStream.output("PhotoWrapper: " + iPhotoId + ", Code: " + iResponseCode + ": Url: " + strUrl + "\n");
							}
							iPhotoCount++;
						}
					}	
				}
				else
				{
					iPhotoIgnored++;
				}
				if (0 == (iPhotoWrappersCount % 100))
				{
					m_outputStream.output("Validating Photo Wrapper Pages and Photos progress: Wrappers: " + iPhotoWrappersCount + ", Photos: " + iPhotoCount + "\n");
				}
				try{Thread.sleep(SLEEP_TIME);}catch(Throwable t){}
			}
			m_outputStream.output("Validated " + iPhotoWrappersCount + " Photo Wrapper Pages and " + iPhotoCount + " Photos. Ignored " + iPhotoIgnored + " due to access restrictions.\n");
		}
		
		m_outputStream.output("HTTP Page Validation Complete!\n");
	}
	
	private int doHttpGet(String strUrl)
		throws Exception
	{
		URL url = new URL(strUrl);
		URLConnection connection = url.openConnection();
		if (connection instanceof HttpURLConnection)
		{
			HttpURLConnection httpConnection = (HttpURLConnection)connection;
			int iResponseCode = httpConnection.getResponseCode();
			httpConnection.disconnect();
			return iResponseCode;
		}
		throw new Exception("Not an HTTP connection for url: " + strUrl);
	}
}
