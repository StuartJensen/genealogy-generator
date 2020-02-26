package home.genealogy.action.validate;

import home.genealogy.Genealogy;
import home.genealogy.configuration.CFGFamily;
import home.genealogy.util.CommandLineParameterList;

import java.io.File;

import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

public class GenealogyValidator
{
	private static final long MILLISECONDS_ONE_DAY = (60 * 60 * 24 * 1000);
	private CFGFamily m_family;
	private CommandLineParameterList m_listCLP;
	
	public GenealogyValidator(CFGFamily family, CommandLineParameterList listCLP)
	{
		m_family = family;
		m_listCLP = listCLP;
	}
	
	public void validate()
		throws Exception
	{
		String strDataPath = m_family.getDataPath();
		if (!strDataPath.endsWith(File.separator))
		{
			strDataPath += File.separator;
		}
		
		// Initialize the validator with the schema
		SchemaFactory sf = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
		Schema s = sf.newSchema(new File(m_family.getSchemaFile()));
		Validator v = s.newValidator();
		
		// Get any time limit the user may have applied to the validate
		long lTimeLimitDays = 0;
		String strTimeLimit = m_listCLP.getValue(Genealogy.COMMAND_LINE_PARAM_ACTION_VALUE_VALIDATE_TIME);
		if (null != strTimeLimit)
		{
			lTimeLimitDays = Long.parseLong(strTimeLimit);
		}
		
		// Get any target the user may have specified
		boolean bValidatePersons = true;
		boolean bValidateMarriages = true;
		boolean bValidateReferences = true;
		boolean bValidatePhotos = true;
		String strTarget = m_listCLP.getValue(Genealogy.COMMAND_LINE_PARAM_ACTION_VALUE_VALIDATE_TARGET);
		if (null != strTarget)
		{
			bValidatePersons = false;
			bValidateMarriages = false;
			bValidateReferences = false;
			bValidatePhotos = false;
			if (-1 != strTarget.indexOf(Genealogy.COMMAND_LINE_PARAM_TARGET_PERSONS))
			{
				bValidatePersons = true;
			}
			if (-1 != strTarget.indexOf(Genealogy.COMMAND_LINE_PARAM_TARGET_MARRIAGES))
			{
				bValidateMarriages = true;
			}
			if (-1 != strTarget.indexOf(Genealogy.COMMAND_LINE_PARAM_TARGET_REFERENCES))
			{
				bValidateReferences = true;
			}
			if (-1 != strTarget.indexOf(Genealogy.COMMAND_LINE_PARAM_TARGET_PHOTOS))
			{
				bValidatePhotos = true;
			}
		}
		
		long lPersonsErrors = 0;
		long lPersonsExamined = 0;
		long lMarriagesErrors = 0;
		long lMarriagesExamined = 0;
		long lReferencesErrors = 0;
		long lReferencesExamined = 0;
		long lPhotosErrors = 0;
		long lPhotosExamined = 0;
		
		// Validate PERSONS
		if (bValidatePersons)
		{
			String strDirectoryPersons = strDataPath + CFGFamily.APPENDAGE_DATAPATH_PERSONS;
			File fDirectoryPersons = new File(strDirectoryPersons);
			File[] arFiles = fDirectoryPersons.listFiles(new TimeFileFilter("P", ".xml", lTimeLimitDays * MILLISECONDS_ONE_DAY));
			if ((null != arFiles) && (0 != arFiles.length))
			{
				for (int i=0; i<arFiles.length; i++)
				{
					try
					{
						lPersonsExamined++;
						v.validate(new StreamSource(arFiles[i]));
					}
					catch (Throwable t)
					{
						System.out.println("Validation Error (Person): " + arFiles[i].getName() + ": " + t.getMessage());
						lPersonsErrors++;
					}
				}
			}
		}
		
		// Validate MARRIAGES
		if (bValidateMarriages)
		{
			String strDirectoryMarriages = strDataPath + CFGFamily.APPENDAGE_DATAPATH_MARRIAGES;
			File fDirectoryMarriages = new File(strDirectoryMarriages);
			File[] arFiles = fDirectoryMarriages.listFiles(new TimeFileFilter("M", ".xml", lTimeLimitDays * MILLISECONDS_ONE_DAY));
			if ((null != arFiles) && (0 != arFiles.length))
			{
				for (int i=0; i<arFiles.length; i++)
				{
					try
					{
						lMarriagesExamined++;
						v.validate(new StreamSource(arFiles[i]));
					}
					catch (Throwable t)
					{
						System.out.println("Validation Error (Marriage): " + arFiles[i].getName() + ": " + t.getMessage());
						lMarriagesErrors++;
					}
				}
			}
		}
		
		// Validate REFERENCES
		if (bValidateReferences)
		{
			String strDirectoryReferences = strDataPath + CFGFamily.APPENDAGE_DATAPATH_REFERENCES;
			File fDirectoryReferences = new File(strDirectoryReferences);
			File[] arFiles = fDirectoryReferences.listFiles(new TimeFileFilter("R", ".xml", lTimeLimitDays * MILLISECONDS_ONE_DAY));
			if ((null != arFiles) && (0 != arFiles.length))
			{
				for (int i=0; i<arFiles.length; i++)
				{
					try
					{
						lReferencesExamined++;
						v.validate(new StreamSource(arFiles[i]));
					}
					catch (Throwable t)
					{
						System.out.println("Validation Error (Reference): " + arFiles[i].getName() + ": " + t.getMessage());
						lReferencesErrors++;
					}
				}
			}
		}
		
		// Validate PHOTOS
		if (bValidatePhotos)
		{
			String strDirectoryPhotos = strDataPath + CFGFamily.APPENDAGE_DATAPATH_PHOTOS;
			File fDirectoryPhotos = new File(strDirectoryPhotos);
			File[] arFiles = fDirectoryPhotos.listFiles(new TimeFileFilter("Ph", ".xml", lTimeLimitDays * MILLISECONDS_ONE_DAY));
			if ((null != arFiles) && (0 != arFiles.length))
			{
				for (int i=0; i<arFiles.length; i++)
				{
					try
					{
						lPhotosExamined++;
						v.validate(new StreamSource(arFiles[i]));
					}
					catch (Throwable t)
					{
						System.out.println("Validation Error (Photo): " + arFiles[i].getName() + ": " + t.getMessage());
						lPhotosErrors++;
					}
				}
			}
		}
		
		System.out.println("Validation Report:");
		System.out.println("Persons: Examined: " + lPersonsExamined + ", Errors: " + lPersonsErrors);
		System.out.println("Marriages: Examined: " + lMarriagesExamined + ", Errors: " + lMarriagesErrors);
		System.out.println("References: Examined: " + lReferencesExamined + ", Errors: " + lReferencesErrors);
		System.out.println("Photos Examined: " + lPhotosExamined + ", Errors: " + lPhotosErrors);
		
	}

}
