package home.genealogy.lists;

import home.genealogy.configuration.CFGFamily;
import home.genealogy.schema.all.Reference;
import home.genealogy.schema.all.helpers.ReferenceHelper;
import home.genealogy.schema.all.helpers.ReferenceIdHelper;
import home.genealogy.util.CommandLineParameterList;
import home.genealogy.util.FileNameFileFilter;

import java.io.File;
import java.util.Iterator;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

public class ReferenceList
{
	private Reference[] m_arReferenceList;
	
	public ReferenceList(CFGFamily family, CommandLineParameterList listCLP)
		throws Exception
	{
		String strDataPath = family.getDataPath();
		if (!strDataPath.endsWith(File.separator))
		{
			strDataPath += File.separator;
		}
		
		m_arReferenceList = new Reference[family.getReferenceListMaxSize()];
		
		// Setup JAXB unmarshaller
		JAXBContext jc = JAXBContext.newInstance("home.genealogy.schema.all");
		Unmarshaller unmarshaller = jc.createUnmarshaller();
		SchemaFactory schemaFactory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
		Schema schema = schemaFactory.newSchema(new File(family.getSchemaFile()));
		unmarshaller.setSchema(schema);
		
		String strDirectoryReferences = strDataPath + CFGFamily.APPENDAGE_DATAPATH_REFERENCES;
		File fDirectoryReferences = new File(strDirectoryReferences);
		File[] arFiles = fDirectoryReferences.listFiles(new FileNameFileFilter("R", ".xml"));
		if ((null != arFiles) && (0 != arFiles.length))
		{
			for (int i=0; i<arFiles.length; i++)
			{
				Reference reference = (Reference)unmarshaller.unmarshal(arFiles[i]);
				int iReferenceId = ReferenceHelper.getReferenceId(reference);
				if (ReferenceIdHelper.REFERENCEID_INVALID == iReferenceId)
				{
					throw new Exception("Reference has invalid marriage id: " + arFiles[i].getName());
				}
				if (iReferenceId >= m_arReferenceList.length)
				{
					throw new Exception("Reference's reference id out of range: " + iReferenceId);
				}
				m_arReferenceList[iReferenceId] = reference;
			}
		}
	}
	
	public Iterator<Reference> getReferences()
	{
		return new ReferenceListIterator(m_arReferenceList);
	}
	
	public Reference get(int iReferenceId)
	{
		if (iReferenceId < m_arReferenceList.length)
		{
			return m_arReferenceList[iReferenceId];
		}
		return null;
	}
	
	public int size()
	{
		int iCount = 0;
		for (int i=0; i<m_arReferenceList.length; i++)
		{
			if (null != m_arReferenceList[i])
			{
				iCount++;
			}
		}
		return iCount;
	}
}
