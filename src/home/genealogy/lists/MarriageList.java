package home.genealogy.lists;

import home.genealogy.configuration.CFGFamily;
import home.genealogy.schema.all.Marriage;
import home.genealogy.schema.all.MarriageId;
import home.genealogy.schema.all.Person;
import home.genealogy.schema.all.helpers.MarriageHelper;
import home.genealogy.schema.all.helpers.MarriageIdHelper;
import home.genealogy.schema.all.helpers.PersonHelper;
import home.genealogy.schema.all.helpers.PersonIdHelper;
import home.genealogy.util.CommandLineParameterList;
import home.genealogy.util.FileNameFileFilter;

import java.io.File;
import java.util.Iterator;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

public class MarriageList
{
	private Marriage[] m_arMarriageList;
	
	public MarriageList(CFGFamily family, CommandLineParameterList listCLP)
		throws Exception
	{
		String strDataPath = family.getDataPath();
		if (!strDataPath.endsWith(File.separator))
		{
			strDataPath += File.separator;
		}
		
		m_arMarriageList = new Marriage[family.getMarriageListMaxSize()];
		
		// Setup JAXB unmarshaller
		JAXBContext jc = JAXBContext.newInstance("home.genealogy.schema.all");
		Unmarshaller unmarshaller = jc.createUnmarshaller();
		SchemaFactory schemaFactory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
		Schema schema = schemaFactory.newSchema(new File(family.getSchemaFile()));
		unmarshaller.setSchema(schema);
		
		String strDirectoryMarriages = strDataPath + CFGFamily.APPENDAGE_DATAPATH_MARRIAGES;
		File fDirectoryMarriages = new File(strDirectoryMarriages);
		File[] arFiles = fDirectoryMarriages.listFiles(new FileNameFileFilter("M", ".xml"));
		if ((null != arFiles) && (0 != arFiles.length))
		{
			for (int i=0; i<arFiles.length; i++)
			{
				Marriage marriage = (Marriage)unmarshaller.unmarshal(arFiles[i]);
				int iMarriageId = MarriageHelper.getMarriageId(marriage);
				if (MarriageIdHelper.MARRIAGEID_INVALID == iMarriageId)
				{
					throw new Exception("Marriage has invalid marriage id: " + arFiles[i].getName());
				}
				if (iMarriageId >= m_arMarriageList.length)
				{
					throw new Exception("Marriage's marriage id out of range: " + iMarriageId);
				}
				m_arMarriageList[iMarriageId] = marriage;
			}
		}
	}

	public void setInLineFlags(CFGFamily family, PersonList personList)
	{
		// Set marriage list in line flags. First turn them all off.
		for (int m=0; m<m_arMarriageList.length; m++)
		{
			if (null != m_arMarriageList[m])
			{
				m_arMarriageList[m].setInLine("false");
			}
		}
		MarriageId baseMarriageId = family.getBaseMarriageId();
		if (null != baseMarriageId)
		{
			int iBaseMarriageId = MarriageIdHelper.getMarriageId(baseMarriageId);
			if (MarriageIdHelper.MARRIAGEID_INVALID != iBaseMarriageId)
			{
				setInLineFlags(iBaseMarriageId, personList);
			}
		}
	}
	
	private void setInLineFlags(int iMarriageId, PersonList personList)
	{
		Marriage marriage = get(iMarriageId);
		if (null != marriage)
		{
			marriage.setInLine("true");
			int iHusbandId = MarriageHelper.getHusbandPersonId(marriage);
			int iWifeId = MarriageHelper.getWifePersonId(marriage);
			if (PersonIdHelper.PERSONID_INVALID != iHusbandId)
			{
				Person husband = personList.get(iHusbandId);
				if (null != husband)
				{
					int iHusbandParentMarriageId = PersonHelper.getParentId(husband);
					if (MarriageIdHelper.MARRIAGEID_INVALID != iHusbandParentMarriageId)
					{
						setInLineFlags(iHusbandParentMarriageId, personList);
					}
				}
				Person wife = personList.get(iWifeId);
				if (null != wife)
				{
					int iWifeParentMarriageId = PersonHelper.getParentId(wife);
					if (MarriageIdHelper.MARRIAGEID_INVALID != iWifeParentMarriageId)
					{
						setInLineFlags(iWifeParentMarriageId, personList);
					}
				}
			}
		}
	}
	
	public Iterator<Marriage> getMarriages()
	{
		return new MarriageListIterator(m_arMarriageList);
	}
	
	public Marriage get(int iMarriageId)
	{
		if ((iMarriageId < m_arMarriageList.length) &&
            (MarriageIdHelper.MARRIAGEID_INVALID != iMarriageId))
		{
			return m_arMarriageList[iMarriageId];
		}
		return null;
	}
	
	public int size()
	{
		int iCount = 0;
		for (int i=0; i<m_arMarriageList.length; i++)
		{
			if (null != m_arMarriageList[i])
			{
				iCount++;
			}
		}
		return iCount;
	}
}
