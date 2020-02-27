package home.genealogy;

import java.io.File;

import home.genealogy.action.errorcheck.ErrorChecker;
import home.genealogy.action.validate.GenealogyValidator;
import home.genealogy.configuration.CFGFamily;
import home.genealogy.configuration.CFGFamilyList;
import home.genealogy.forms.html.HTMLFGSForm;
import home.genealogy.forms.html.HTMLPersonInfoForm;
import home.genealogy.forms.html.HTMLPersonListForm;
import home.genealogy.forms.html.HTMLPhotoForm;
import home.genealogy.forms.html.HTMLPhotoListForm;
import home.genealogy.forms.html.HTMLReferenceForm;
import home.genealogy.forms.html.HTMLReferenceListForm;
import home.genealogy.indexes.IndexMarriageToChildren;
import home.genealogy.indexes.IndexMarriageToSpouses;
import home.genealogy.indexes.IndexPersonToMarriages;
import home.genealogy.lists.MarriageList;
import home.genealogy.lists.PersonList;
import home.genealogy.lists.PhotoList;
import home.genealogy.lists.ReferenceList;
import home.genealogy.lists.RelationshipManager;
import home.genealogy.output.FileOutput;
import home.genealogy.output.IOutputStream;
import home.genealogy.output.StdOutOutput;

public class Genealogy
{
	public static void main(String[] args)
	{
		try
		{
			CommandLineParameters commandLineParameters = new CommandLineParameters(args);
			
			// Initialize logging output stream
			IOutputStream outputStream = new StdOutOutput();
			String strLogType = commandLineParameters.getLogType();
			if (strLogType.equals(CommandLineParameters.COMMAND_LINE_PARAM_LOG_VALUE_FILE))
			{
				outputStream = new FileOutput(commandLineParameters.getLogFilename(),
											  commandLineParameters.getLogFileEcho());
			}
			outputStream.initialize();
	
			CFGFamilyList listFamily = new CFGFamilyList(new File(commandLineParameters.getConfigurationFilename()));
			CFGFamily family = listFamily.getFamily(commandLineParameters.getFamilyName());
			if (null == family)
			{
				throw new UsageException("Specified family name: \"" + commandLineParameters.getFamilyName() + "\" not found in configuration file: \"" + commandLineParameters.getConfigurationFilename() + "\"");
			}
			
			String strAction = commandLineParameters.getAction();
			if (strAction.equals(commandLineParameters.isActionValidate()))
			{
				GenealogyValidator validator = new GenealogyValidator(family, commandLineParameters, outputStream);
				validator.validate();
			}
			else if (commandLineParameters.isActionConvert())
			{
				String strSource = commandLineParameters.getSource();
				String strDestination = commandLineParameters.getDestination();
				PersonList personList = new PersonList(family, commandLineParameters);
				outputStream.output("Person List: Count: " + personList.size() + "\n");
				PhotoList photoList = new PhotoList(family, commandLineParameters);
				outputStream.output("Photo List: Count: " + photoList.size() + "\n");
				MarriageList marriageList = new MarriageList(family, commandLineParameters);
				marriageList.setInLineFlags(family, personList);
				outputStream.output("Marriage List: Count: " + marriageList.size() + "\n");
				ReferenceList referenceList = new ReferenceList(family, commandLineParameters);
				outputStream.output("Reference List: Count: " + referenceList.size() + "\n");
				
				boolean bFormattedOutput = true;
				String strFormat = commandLineParameters.getXmlFormat();
				if (CommandLineParameters.COMMAND_LINE_PARAM_XML_FORMAT_VALUE_PRETTY.equals(strFormat))
				{
					bFormattedOutput = true;
				}
				
				if (strDestination.equalsIgnoreCase(CommandLineParameters.COMMAND_LINE_PARAM_DESTINATION_VALUE_ALLXML))
				{
					personList.marshallAllFile(family, bFormattedOutput);
					outputStream.output("Stored Person List to destination: " + CFGFamily.PERSONS_ALL_FILENAME + " file\n");
					photoList.marshallAllFile(family, bFormattedOutput);
					outputStream.output("Stored Photo List to destination: " + CFGFamily.PHOTOS_ALL_FILENAME + " file\n");
					marriageList.marshallAllFile(family, bFormattedOutput);
					outputStream.output("Stored Marriage List to destination: " + CFGFamily.MARRIAGES_ALL_FILENAME + " file\n");
					referenceList.marshallAllFile(family, bFormattedOutput);
					outputStream.output("Stored Reference List to destination: " + CFGFamily.REFERENCES_ALL_FILENAME + " file\n");
				}
				else if (strDestination.equalsIgnoreCase(CommandLineParameters.COMMAND_LINE_PARAM_DESTINATION_VALUE_INDIVIDUALXML))
				{
					personList.marshallIndividualFiles(family, bFormattedOutput);
					outputStream.output("Stored Person List to destination: individual xml files\n");
					photoList.marshallIndividualFiles(family, bFormattedOutput);
					outputStream.output("Stored Photo List to destination: individual xml files\n");
					marriageList.marshallIndividualFiles(family, bFormattedOutput);
					outputStream.output("Stored Marriage List to destination: individual xml files\n");
					referenceList.marshallIndividualFiles(family, bFormattedOutput);
					outputStream.output("Stored Reference List to destination: individual xml files\n");
				}
			}
			else if (commandLineParameters.isActionHtmlForm())
			{
				PersonList personList = new PersonList(family, commandLineParameters);
				outputStream.output("Person List: Count: " + personList.size() + "\n");
				PhotoList photoList = new PhotoList(family, commandLineParameters);
				outputStream.output("Photo List: Count: " + photoList.size() + "\n");
				MarriageList marriageList = new MarriageList(family, commandLineParameters);
				marriageList.setInLineFlags(family, personList);
				outputStream.output("Marriage List: Count: " + marriageList.size() + "\n");
				ReferenceList referenceList = new ReferenceList(family, commandLineParameters);
				outputStream.output("Reference List: Count: " + referenceList.size() + "\n");
				// Set relationships in the personList
				IndexMarriageToSpouses idxMarrToSpouses = new IndexMarriageToSpouses(family, marriageList);
				IndexPersonToMarriages idxPerToMar = new IndexPersonToMarriages(family, marriageList);
				IndexMarriageToChildren idxMarToChild = new IndexMarriageToChildren(family, commandLineParameters, personList);
				RelationshipManager.setRelationships(family, personList, marriageList, idxPerToMar, idxMarToChild, outputStream);
				
				boolean bSuppressLiving = commandLineParameters.getSuppressLiving();
				boolean bSuppressLds = true;
/*				
				String strSuppressLds = listCLP.getValue(COMMAND_LINE_PARAM_ACTION_VALUE_HTMLFORM_SUPPRESSLDS);
				if (null != strSuppressLds)
				{
					if (strSuppressLds.equalsIgnoreCase("false"))
					{
						bSuppressLds = false;
					}
				}
*/				
				if ((commandLineParameters.isHtmlFormTargetPersonList()) ||
					 commandLineParameters.isHtmlFormTargetAll())
				{
					HTMLPersonListForm form = new HTMLPersonListForm(family, personList, marriageList, bSuppressLiving, outputStream);
					form.create();
				}
				else if ((commandLineParameters.isHtmlFormTargetPersonInfo()) ||
						  commandLineParameters.isHtmlFormTargetAll())
				{
					HTMLPersonInfoForm form = new HTMLPersonInfoForm(family, personList, marriageList, referenceList, photoList, bSuppressLiving, bSuppressLds, commandLineParameters, outputStream);
					form.create();
				}
				else if ((commandLineParameters.isHtmlFormTargetReferenceList()) ||
						  commandLineParameters.isHtmlFormTargetAll())
				{
					HTMLReferenceListForm form = new HTMLReferenceListForm(family, personList,
						     											   marriageList, referenceList,
						     											   photoList, idxMarrToSpouses,
						     											   bSuppressLiving,
						     											   commandLineParameters, outputStream);
					form.create();
				}
				else if ((commandLineParameters.isHtmlFormTargetPhotos()) ||
						  commandLineParameters.isHtmlFormTargetAll())
				{
					HTMLPhotoForm form = new HTMLPhotoForm(family, personList,
							   marriageList, referenceList,
							   photoList, idxMarrToSpouses,
							   bSuppressLiving,
							   commandLineParameters, outputStream);
					form.create();
				}
				else if ((commandLineParameters.isHtmlFormTargetPhotoList()) ||
						  commandLineParameters.isHtmlFormTargetAll())
				{
					HTMLPhotoListForm form = new HTMLPhotoListForm(family, personList, marriageList, referenceList, photoList, idxMarrToSpouses, bSuppressLiving, commandLineParameters, outputStream);
					form.create();
				}
				else if ((commandLineParameters.isHtmlFormTargetReferences()) ||
						  commandLineParameters.isHtmlFormTargetAll())
				{
					HTMLReferenceForm form = new HTMLReferenceForm(family, personList, marriageList, referenceList, photoList, bSuppressLiving, bSuppressLds, commandLineParameters, outputStream);
					form.create();
				}
				else if ((commandLineParameters.isHtmlFormTargetFGS()) ||
						  commandLineParameters.isHtmlFormTargetAll())
				{
					HTMLFGSForm form = new HTMLFGSForm(family, personList, marriageList, referenceList, photoList, bSuppressLiving, bSuppressLds, commandLineParameters, outputStream);
					form.create();
				}
			}
			else if (commandLineParameters.isActionErrorCheck())
			{
				PersonList personList = new PersonList(family, commandLineParameters);
				outputStream.output("Person List: Count: " + personList.size() + "\n");
				PhotoList photoList = new PhotoList(family, commandLineParameters);
				outputStream.output("Photo List: Count: " + photoList.size() + "\n");
				MarriageList marriageList = new MarriageList(family, commandLineParameters);
				marriageList.setInLineFlags(family, personList);
				outputStream.output("Marriage List: Count: " + marriageList.size() + "\n");
				ReferenceList referenceList = new ReferenceList(family, commandLineParameters);
				outputStream.output("Reference List: Count: " + referenceList.size() + "\n");
				
				ErrorChecker errorChecker = new ErrorChecker(family, personList, marriageList, referenceList, photoList, commandLineParameters, outputStream);
				errorChecker.check();
			}
/*			
			else if (commandLineParameters.isActionGenerations())
			{
				PersonList personList = new PersonList(family);
				outputStream.output("Person List: Count: " + personList.size() + "\n");
				PhotoList photoList = new PhotoList(family);
				outputStream.output("Photo List: Count: " + photoList.size() + "\n");
				MarriageList marriageList = new MarriageList(family);
				marriageList.setInLineFlags(family, personList);
				outputStream.output("Marriage List: Count: " + marriageList.size() + "\n");
				ReferenceList referenceList = new ReferenceList(family);
				outputStream.output("Reference List: Count: " + referenceList.size() + "\n");
				// Set relationships in the personList
				IndexMarriageToSpouses idxMarrToSpouses = new IndexMarriageToSpouses(family, marriageList);
				IndexPersonToMarriages idxPerToMar = new IndexPersonToMarriages(family, marriageList);
				IndexMarriageToChildren idxMarToChild = new IndexMarriageToChildren(family, listCLP, personList);
				RelationshipManager.setRelationships(family, personList, marriageList, idxPerToMar, idxMarToChild, outputStream);

				GenerationManager generationManager = new GenerationManager(family, personList, listCLP, outputStream);
				generationManager.process();
			}
			else if (commandLineParameters.isActionHttpValidate())
			{
				String strType = listCLP.getValue(COMMAND_LINE_PARAM_ACTION_VALUE_HTTPVALIDATE_TYPE);
				if (null == strType)
				{
					throw new UsageException("No \"" + COMMAND_LINE_PARAM_ACTION_VALUE_HTTPVALIDATE_TYPE + "\" Command Line Parameter!");
				}
				
				PersonList personList = new PersonList(family);
				outputStream.output("Person List: Count: " + personList.size() + "\n");
				PhotoList photoList = new PhotoList(family);
				outputStream.output("Photo List: Count: " + photoList.size() + "\n");
				MarriageList marriageList = new MarriageList(family);
				marriageList.setInLineFlags(family, personList);
				outputStream.output("Marriage List: Count: " + marriageList.size() + "\n");
				ReferenceList referenceList = new ReferenceList(family);
				outputStream.output("Reference List: Count: " + referenceList.size() + "\n");
				WebSiteValidator validator = new WebSiteValidator(family, personList, marriageList, referenceList, photoList, listCLP, strType, outputStream);
				validator.validate();
			}
*/			
			if (null != outputStream)
			{
				outputStream.deinitialize();
			}
		}
		catch (UsageException ue)
		{
			CommandLineParameters.showUsage(ue.getMessage());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		System.exit(1);
	}
	
}
