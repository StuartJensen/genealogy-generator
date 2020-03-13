package home.genealogy.action.validate;

import home.genealogy.CommandLineParameters;
import home.genealogy.GenealogyContext;
import home.genealogy.configuration.CFGFamily;
import home.genealogy.output.IOutputStream;

import java.io.File;

import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

public class GenealogyValidator
{
	private static final long MILLISECONDS_ONE_DAY = (60 * 60 * 24 * 1000);
	private CFGFamily m_family;
	private CommandLineParameters m_commandLineParameters;
	private IOutputStream m_outputStream;
	
	public GenealogyValidator(GenealogyContext context)
	{
		m_family = context.getFamily();
		m_commandLineParameters = context.getCommandLineParameters();
		m_outputStream = context.getOutputStream();
	}
	
	public void validate()
		throws Exception
	{
		String strDataPath = m_family.getDataPathSlashTerminated();
		
		// Initialize the validator with the schema
		SchemaFactory sf = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
		Schema s = sf.newSchema(new File(m_family.getSchemaFile()));
		Validator v = s.newValidator();
		
		// Get any time limit the user may have applied to the validate
		long lTimeLimitDays = m_commandLineParameters.getValidateTimeLimit();
		
		// Get any target the user may have specified
		boolean bValidatePersons = m_commandLineParameters.isValidateTargetPersons();
		boolean bValidateMarriages = m_commandLineParameters.isValidateTargetMarriages();
		boolean bValidateReferences = m_commandLineParameters.isValidateTargetReferences();
		boolean bValidatePhotos = m_commandLineParameters.isValidateTargetPhotos();
		if (m_commandLineParameters.isValidateTargetAll())
		{
			bValidatePersons = true;
			bValidateMarriages = true;
			bValidateReferences = true;
			bValidatePhotos = true;
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
			File[] arFiles = fDirectoryPersons.listFiles(new TimeFileFilter("P", CFGFamily.DOTXML_FILE_POSTFIX, lTimeLimitDays * MILLISECONDS_ONE_DAY));
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
						m_outputStream.output("Validation Error (Person): " + arFiles[i].getName() + ": " + t.getMessage() + "\n");
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
			File[] arFiles = fDirectoryMarriages.listFiles(new TimeFileFilter("M", CFGFamily.DOTXML_FILE_POSTFIX, lTimeLimitDays * MILLISECONDS_ONE_DAY));
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
						m_outputStream.output("Validation Error (Marriage): " + arFiles[i].getName() + ": " + t.getMessage() + "\n");
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
			File[] arFiles = fDirectoryReferences.listFiles(new TimeFileFilter("R", CFGFamily.DOTXML_FILE_POSTFIX, lTimeLimitDays * MILLISECONDS_ONE_DAY));
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
						m_outputStream.output("Validation Error (Reference): " + arFiles[i].getName() + ": " + t.getMessage() + "\n");
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
			File[] arFiles = fDirectoryPhotos.listFiles(new TimeFileFilter("Ph", CFGFamily.DOTXML_FILE_POSTFIX, lTimeLimitDays * MILLISECONDS_ONE_DAY));
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
						m_outputStream.output("Validation Error (Photo): " + arFiles[i].getName() + ": " + t.getMessage() + "\n");
						lPhotosErrors++;
					}
				}
			}
		}
		
		m_outputStream.output("Validation Report:\n");
		m_outputStream.output("Persons: Examined: " + lPersonsExamined + ", Errors: " + lPersonsErrors + "\n");
		m_outputStream.output("Marriages: Examined: " + lMarriagesExamined + ", Errors: " + lMarriagesErrors + "\n");
		m_outputStream.output("References: Examined: " + lReferencesExamined + ", Errors: " + lReferencesErrors + "\n");
		m_outputStream.output("Photos Examined: " + lPhotosExamined + ", Errors: " + lPhotosErrors + "\n");
	}

}
