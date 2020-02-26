package home.genealogy.configuration;

import home.genealogy.schema.all.MarriageId;
import home.genealogy.schema.all.PersonId;

public class CFGFamily
{
	public static final String PROPERTY_TAG_SURNAME = "Surname";
	public static final String PROPERTY_TAG_BASEPERSONID = "BasePersonId";
	public static final String PROPERTY_TAG_BASEMARRIAGEID = "BaseMarriageId";
	public static final String PROPERTY_TAG_CONTACTEMAIL = "ContactEMail";
	public static final String PROPERTY_TAG_DATAPATH = "DataPath";
	public static final String PROPERTY_TAG_PHOTOPATH = "PhotoPath";
	public static final String PROPERTY_TAG_OUTPUTPATHHTML = "OutputPathHTML";
	public static final String PROPERTY_TAG_SCHEMAFILE = "SchemaFile";
	public static final String PROPERTY_TAG_URLPREFIX = "URLPrefix";
	public static final String PROPERTY_TAG_PERSONLIST_MAXSIZE = "PersonListMaxSize";
	public static final String PROPERTY_TAG_MARRIAGELIST_MAXSIZE = "MarriageListMaxSize";
	public static final String PROPERTY_TAG_REFERENCELIST_MAXSIZE = "ReferenceListMaxSize";
	public static final String PROPERTY_TAG_PHOTOLIST_MAXSIZE = "PhotoListMaxSize";
	
	// Data Path Appendages
	public static final String APPENDAGE_DATAPATH_PERSONS = "persons";
	public static final String APPENDAGE_DATAPATH_MARRIAGES = "marriages";
	public static final String APPENDAGE_DATAPATH_REFERENCES = "references";
	public static final String APPENDAGE_DATAPATH_PHOTOS = "photos";
	
	private String m_strFamilyIdentifier;
	private String m_strSurname;
	private PersonId m_basePersonId;
	private MarriageId m_baseMarriageId;
	private String m_strDataPath;
	private String m_strPhotoPath;
	private String m_strContactEMail;
	private String m_strSchemaFile;
	private String m_strUrlPrefix;
	
	public static final int PERSONLIST_MAX_SIZE_DEFAULT = 10000;
	public static final int MARRIAGELIST_MAX_SIZE_DEFAULT = 10000;
	public static final int REFERENCELIST_MAX_SIZE_DEFAULT = 1000;
	public static final int PHOTOLIST_MAX_SIZE_DEFAULT = 2000;
	
	private int m_iPersonListMaxSize = PERSONLIST_MAX_SIZE_DEFAULT;
	private int m_iMarriageListMaxSize = MARRIAGELIST_MAX_SIZE_DEFAULT;
	private int m_iReferenceListMaxSize = REFERENCELIST_MAX_SIZE_DEFAULT;
	private int m_iPhotoListMaxSize = PHOTOLIST_MAX_SIZE_DEFAULT;
	
	private String m_strOutputPathHTML;
	
	public CFGFamily(String strFamilyIdentifier, String strSurname)
	{
		m_strSurname = strSurname;
		m_strFamilyIdentifier = strFamilyIdentifier;
	}
	
	public String getSurname()
	{
		return m_strSurname;
	}
	
	public String getFamilyIdentifier()
	{
		return m_strFamilyIdentifier;
	}
	
	public void setBasePersonId(PersonId basePersonId)
	{
		m_basePersonId = basePersonId;
	}
	
	public PersonId getBasePersonId()
	{
		return m_basePersonId;
	}
	
	public void setBaseMarriageId(MarriageId baseMarriageId)
	{
		m_baseMarriageId = baseMarriageId;
	}
	
	public MarriageId getBaseMarriageId()
	{
		return m_baseMarriageId;
	}
	
	public void setContactEMail(String strContactEMail)
	{
		m_strContactEMail = strContactEMail;
	}
	
	public String getContactEMail()
	{
		return m_strContactEMail;
	}
	
	public void setDataPath(String strDataPath)
	{
		m_strDataPath = strDataPath;
	}
	
	public String getDataPath()
	{
		return m_strDataPath;
	}
	
	public void setPhotoPath(String strPhotoPath)
	{
		m_strPhotoPath = strPhotoPath;
	}
	
	public String getPhotoPath()
	{
		return m_strPhotoPath;
	}
	
	public void setOutputPathHTML(String strOutputPathHTML)
	{
		m_strOutputPathHTML = strOutputPathHTML;
	}
	
	public String getOutputPathHTML()
	{
		return m_strOutputPathHTML;
	}
	
	public void setSchemafile(String strSchemaFile)
	{
		m_strSchemaFile = strSchemaFile;
	}
	
	public String getSchemaFile()
	{
		return m_strSchemaFile;
	}
	
	public void setUrlPrefix(String strUrlPrefix)
	{
		m_strUrlPrefix = strUrlPrefix;
	}
	
	public String getUrlPrefix()
	{
		return m_strUrlPrefix;
	}
	
	public void setPersonListMaxSize(int i)
	{
		m_iPersonListMaxSize = i;
	}
	
	public void setPersonListMaxSize(String str)
	{
		if (null != str)
		{
			try{m_iPersonListMaxSize = Integer.parseInt(str);}catch(Exception e){}			
		}
	}
	
	public int getPersonListMaxSize()
	{
		return m_iPersonListMaxSize;
	}
	
	public void setMarriageListMaxSize(int i)
	{
		m_iMarriageListMaxSize = i;
	}
	
	public void setMarriageListMaxSize(String str)
	{
		if (null != str)
		{
			try{m_iMarriageListMaxSize = Integer.parseInt(str);}catch(Exception e){}			
		}
	}
	
	public int getMarriageListMaxSize()
	{
		return m_iMarriageListMaxSize;
	}
	
	public void setReferenceListMaxSize(int i)
	{
		m_iReferenceListMaxSize = i;
	}
	
	public void setReferenceListMaxSize(String str)
	{
		if (null != str)
		{
			try{m_iReferenceListMaxSize = Integer.parseInt(str);}catch(Exception e){}			
		}
	}
	
	public int getReferenceListMaxSize()
	{
		return m_iReferenceListMaxSize;
	}
	
	public void setPhotoListMaxSize(int i)
	{
		m_iPhotoListMaxSize = i;
	}
	
	public void setPhotoListMaxSize(String str)
	{
		if (null != str)
		{
			try{m_iPhotoListMaxSize = Integer.parseInt(str);}catch(Exception e){}			
		}
	}
	
	public int getPhotoListMaxSize()
	{
		return m_iPhotoListMaxSize;
	}
	
}
