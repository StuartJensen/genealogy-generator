package home.genealogy.configuration;

import home.genealogy.schema.all.MarriageId;
import home.genealogy.schema.all.PersonId;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Properties;

public class CFGFamilyList {
	public static final String PROPERTY_TAG_FAMILY = "family";
	public static final String PROPERTY_TAG_SEPARATOR = ".";

	private ArrayList<CFGFamily> m_alFamilyList = new ArrayList<CFGFamily>();

	public CFGFamilyList(File fConfiguration)
		throws Exception
	{
		Properties properties = new Properties();
		if (fConfiguration.exists())
		{
			FileInputStream fis = new FileInputStream(fConfiguration);
			properties.load(fis);
			fis.close();
			
			// Load families
			for (int i=1; true; i++)
			{
				String strFamilyIdentifier = properties.getProperty(PROPERTY_TAG_FAMILY + PROPERTY_TAG_SEPARATOR + i);
				if (null != strFamilyIdentifier)
				{
					String strSurname = properties.getProperty(PROPERTY_TAG_FAMILY + PROPERTY_TAG_SEPARATOR + strFamilyIdentifier + PROPERTY_TAG_SEPARATOR + CFGFamily.PROPERTY_TAG_SURNAME);
					String strBasePersonId = properties.getProperty(PROPERTY_TAG_FAMILY + PROPERTY_TAG_SEPARATOR + strFamilyIdentifier + PROPERTY_TAG_SEPARATOR + CFGFamily.PROPERTY_TAG_BASEPERSONID);
					String strBaseMarriageId = properties.getProperty(PROPERTY_TAG_FAMILY + PROPERTY_TAG_SEPARATOR + strFamilyIdentifier + PROPERTY_TAG_SEPARATOR + CFGFamily.PROPERTY_TAG_BASEMARRIAGEID);
					String strContactEMail = properties.getProperty(PROPERTY_TAG_FAMILY + PROPERTY_TAG_SEPARATOR + strFamilyIdentifier + PROPERTY_TAG_SEPARATOR + CFGFamily.PROPERTY_TAG_CONTACTEMAIL);
					String strDataPath = properties.getProperty(PROPERTY_TAG_FAMILY + PROPERTY_TAG_SEPARATOR + strFamilyIdentifier + PROPERTY_TAG_SEPARATOR + CFGFamily.PROPERTY_TAG_DATAPATH);
					String strPhotoPath = properties.getProperty(PROPERTY_TAG_FAMILY + PROPERTY_TAG_SEPARATOR + strFamilyIdentifier + PROPERTY_TAG_SEPARATOR + CFGFamily.PROPERTY_TAG_PHOTOPATH);
					String strOutputPathHTML = properties.getProperty(PROPERTY_TAG_FAMILY + PROPERTY_TAG_SEPARATOR + strFamilyIdentifier + PROPERTY_TAG_SEPARATOR + CFGFamily.PROPERTY_TAG_OUTPUTPATHHTML);
					String strSchemaFile = properties.getProperty(PROPERTY_TAG_FAMILY + PROPERTY_TAG_SEPARATOR + strFamilyIdentifier + PROPERTY_TAG_SEPARATOR + CFGFamily.PROPERTY_TAG_SCHEMAFILE);
					String strUrlPrefix = properties.getProperty(PROPERTY_TAG_FAMILY + PROPERTY_TAG_SEPARATOR + strFamilyIdentifier + PROPERTY_TAG_SEPARATOR + CFGFamily.PROPERTY_TAG_URLPREFIX);
					String strPersonListMaxSize = properties.getProperty(PROPERTY_TAG_FAMILY + PROPERTY_TAG_SEPARATOR + strFamilyIdentifier + PROPERTY_TAG_SEPARATOR + CFGFamily.PROPERTY_TAG_PERSONLIST_MAXSIZE);
					String strMarriageListMaxSize = properties.getProperty(PROPERTY_TAG_FAMILY + PROPERTY_TAG_SEPARATOR + strFamilyIdentifier + PROPERTY_TAG_SEPARATOR + CFGFamily.PROPERTY_TAG_MARRIAGELIST_MAXSIZE);
					String strReferenceListMaxSize = properties.getProperty(PROPERTY_TAG_FAMILY + PROPERTY_TAG_SEPARATOR + strFamilyIdentifier + PROPERTY_TAG_SEPARATOR + CFGFamily.PROPERTY_TAG_REFERENCELIST_MAXSIZE);
					String strPhotoListMaxSize = properties.getProperty(PROPERTY_TAG_FAMILY + PROPERTY_TAG_SEPARATOR + strFamilyIdentifier + PROPERTY_TAG_SEPARATOR + CFGFamily.PROPERTY_TAG_PHOTOLIST_MAXSIZE);
					
					if ((null != strSurname) && (null != strBasePersonId) && (null != strBaseMarriageId) &&
						(null != strDataPath) && (null != strContactEMail) && (null != strOutputPathHTML) &&
						(null != strPhotoPath))
					{
						CFGFamily family = new CFGFamily(strFamilyIdentifier, strSurname);
						PersonId personId = new PersonId();
						personId.setId(strBasePersonId);
						family.setBasePersonId(personId);
						MarriageId marriageId = new MarriageId();
						marriageId.setId(strBaseMarriageId);
						family.setBaseMarriageId(marriageId);
						family.setDataPath(strDataPath);
						family.setPhotoPath(strPhotoPath);
						family.setContactEMail(strContactEMail);
						family.setOutputPathHTML(strOutputPathHTML);
						family.setSchemafile(strSchemaFile);
						family.setUrlPrefix(strUrlPrefix);
						family.setPersonListMaxSize(strPersonListMaxSize);
						family.setMarriageListMaxSize(strMarriageListMaxSize);
						family.setReferenceListMaxSize(strReferenceListMaxSize);
						family.setPhotoListMaxSize(strPhotoListMaxSize);
						m_alFamilyList.add(family);
					}
				}
				else
				{
					break;
				}
			}
		}
	}
	
	public CFGFamily getFamily(String strFamilyIdentifier)
	{
		for (int i=0; i<m_alFamilyList.size(); i++)
		{
			CFGFamily family = m_alFamilyList.get(i);
			if (family.getFamilyIdentifier().equals(strFamilyIdentifier))
			{
				return family;
			}
		}
		return null;
	}
}
