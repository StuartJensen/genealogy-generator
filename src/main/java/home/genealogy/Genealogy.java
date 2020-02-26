package home.genealogy;

import home.genealogy.action.errorcheck.ErrorChecker;
import home.genealogy.action.generations.GenerationManager;
import home.genealogy.action.http.WebSiteValidator;
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
import home.genealogy.util.CommandLineParameterList;

import java.io.File;

public class Genealogy
{
	public static final String COMMAND_LINE_PARAM_CONFIG = "config";
	public static final String COMMAND_LINE_PARAM_FAMILY = "family";
	public static final String COMMAND_LINE_PARAM_ACTION = "action";
	public static final String COMMAND_LINE_PARAM_FORMAT = "format";
	public static final String COMMAND_LINE_PARAM_FORMAT_VALUE_PRETTY = "pretty";
	public static final String COMMAND_LINE_PARAM_FORMAT_VALUE_COMPACT = "compact";
	public static final String COMMAND_LINE_PARAM_ACTION_VALUE_VALIDATE = "validate";
	public static final String COMMAND_LINE_PARAM_ACTION_VALUE_VALIDATE_TIME = "time";
	public static final String COMMAND_LINE_PARAM_ACTION_VALUE_VALIDATE_TARGET = "target";
	public static final String COMMAND_LINE_PARAM_ACTION_VALUE_GENERATEALL = "generateall";
	public static final String COMMAND_LINE_PARAM_ACTION_VALUE_HTMLFORM = "htmlform";
	public static final String COMMAND_LINE_PARAM_ACTION_VALUE_HTMLFORM_TYPE = "type";
	public static final String COMMAND_LINE_PARAM_ACTION_VALUE_HTMLFORM_TYPE_PERSONLIST = "personlist";
	public static final String COMMAND_LINE_PARAM_ACTION_VALUE_HTMLFORM_TYPE_PERSONINFO = "personInfo";
	public static final String COMMAND_LINE_PARAM_ACTION_VALUE_HTMLFORM_TYPE_PHOTOS = "photos";
	public static final String COMMAND_LINE_PARAM_ACTION_VALUE_HTMLFORM_TYPE_PHOTOLIST = "photolist";
	public static final String COMMAND_LINE_PARAM_ACTION_VALUE_HTMLFORM_TYPE_FGS = "fgs";
	public static final String COMMAND_LINE_PARAM_ACTION_VALUE_HTMLFORM_TYPE_REFERENCES = "references";
	public static final String COMMAND_LINE_PARAM_ACTION_VALUE_HTMLFORM_TYPE_REFERENCELIST = "referencelist";
	public static final String COMMAND_LINE_PARAM_ACTION_VALUE_HTMLFORM_TYPE_ALL = "all";
	public static final String COMMAND_LINE_PARAM_ACTION_VALUE_HTMLFORM_SUPPRESSLIVING = "suppressliving";
	public static final String COMMAND_LINE_PARAM_ACTION_VALUE_HTMLFORM_SUPPRESSLDS = "suppresslds";
	
	public static final String COMMAND_LINE_PARAM_ACTION_VALUE_ERRORCHECK = "errorcheck";
	public static final String COMMAND_LINE_PARAM_ACTION_VALUE_ERRORCHECK_TYPE = "type";
	
	public static final String COMMAND_LINE_PARAM_ACTION_VALUE_HTTPVALIDATE = "httpvalidate";
	public static final String COMMAND_LINE_PARAM_ACTION_VALUE_HTTPVALIDATE_TYPE = "type";
	public static final String COMMAND_LINE_PARAM_ACTION_VALUE_HTTPVALIDATE_TYPE_PERSONINFO = "personInfo";
	public static final String COMMAND_LINE_PARAM_ACTION_VALUE_HTTPVALIDATE_TYPE_PHOTOS = "photos";
	public static final String COMMAND_LINE_PARAM_ACTION_VALUE_HTTPVALIDATE_TYPE_FGS = "fgs";
	public static final String COMMAND_LINE_PARAM_ACTION_VALUE_HTTPVALIDATE_TYPE_REFERENCES = "references";
	public static final String COMMAND_LINE_PARAM_ACTION_VALUE_HTTPVALIDATE_TYPE_ALL = "all";
	
	public static final String COMMAND_LINE_PARAM_ACTION_VALUE_GENERATIONS = "generations";
	public static final String COMMAND_LINE_PARAM_ACTION_VALUE_GENERATIONS_TYPE = "type";
	public static final String COMMAND_LINE_PARAM_ACTION_VALUE_GENERATIONS_TYPE_ALL = "all";
	public static final String COMMAND_LINE_PARAM_ACTION_VALUE_GENERATIONS_TYPE_LIVING = "living";
	public static final String COMMAND_LINE_PARAM_ACTION_VALUE_GENERATIONS_TYPE_DEAD = "dead";
	public static final String COMMAND_LINE_PARAM_ACTION_VALUE_GENERATIONS_GENERATION_NTH = "nth";
	public static final String COMMAND_LINE_PARAM_ACTION_VALUE_GENERATIONS_GENERATION_RANGE_START = "start";
	public static final String COMMAND_LINE_PARAM_ACTION_VALUE_GENERATIONS_GENERATION_RANGE_END = "end";
	
	public static final String COMMAND_LINE_PARAM_TARGET_PERSONS = "persons";
	public static final String COMMAND_LINE_PARAM_TARGET_MARRIAGES = "marriages";
	public static final String COMMAND_LINE_PARAM_TARGET_REFERENCES = "references";
	public static final String COMMAND_LINE_PARAM_TARGET_PHOTOS = "photos";
	
	public static final String COMMAND_LINE_PARAM_LOG = "log";
	public static final String COMMAND_LINE_PARAM_LOG_VALUE_STDOUT = "stdout";
	public static final String COMMAND_LINE_PARAM_LOG_VALUE_FILE = "file";
	
	public static final String COMMAND_LINE_PARAM_LOG_FILE_FILENAME = "filename";
	public static final String COMMAND_LINE_PARAM_LOG_FILE_ECHO = "echo";

	
	public static void main(String[] args)
	{
		if (0 == args.length)
		{
			showUsage("No Command Line Parameters!");
		}
		try
		{
			CommandLineParameterList listCLP = new CommandLineParameterList(args);
			if (null == listCLP.getValue(COMMAND_LINE_PARAM_CONFIG))
			{
				showUsage("No \"config\" Command Line Parameter!");
			}
			if (null == listCLP.getValue(COMMAND_LINE_PARAM_FAMILY))
			{
				showUsage("No \"family\" Command Line Parameter!");
			}
			if (null == listCLP.getValue(COMMAND_LINE_PARAM_ACTION))
			{
				showUsage("No \"action\" Command Line Parameter!");
			}
			
			// Initialize logging output stream
			IOutputStream outputStream = new StdOutOutput();
			String strLogType = listCLP.getValue(COMMAND_LINE_PARAM_LOG);
			if (null != strLogType)
			{
				if (strLogType.equals(COMMAND_LINE_PARAM_LOG_VALUE_FILE))
				{
					String strLogFileName = listCLP.getValue(COMMAND_LINE_PARAM_LOG_FILE_FILENAME);
					String strEchoToStdOut = listCLP.getValue(COMMAND_LINE_PARAM_LOG_FILE_ECHO);
					if (null == strLogFileName)
					{
						showUsage("Log type of \"file\" requires a filename!");
					}
					boolean bEcho = false;
					if (null != strEchoToStdOut)
					{
						if (strEchoToStdOut.equalsIgnoreCase("true"))
						{
							bEcho = true;
						}
					}
					outputStream = new FileOutput(strLogFileName, bEcho);
				}
			}
			outputStream.initialize();
	
			CFGFamilyList listFamily = new CFGFamilyList(new File(listCLP.getValue(COMMAND_LINE_PARAM_CONFIG)));
			CFGFamily family = listFamily.getFamily(listCLP.getValue(COMMAND_LINE_PARAM_FAMILY));
			
			String strAction = listCLP.getValue(COMMAND_LINE_PARAM_ACTION);
			if (strAction.equals(COMMAND_LINE_PARAM_ACTION_VALUE_VALIDATE))
			{
				GenealogyValidator validator = new GenealogyValidator(family, listCLP);
				validator.validate();
			}
			else if (strAction.equals(COMMAND_LINE_PARAM_ACTION_VALUE_GENERATEALL))
			{
				PersonList personList = new PersonList(family, listCLP);
				outputStream.output("Person List: Count: " + personList.size() + "\n");
				PhotoList photoList = new PhotoList(family, listCLP);
				outputStream.output("Photo List: Count: " + photoList.size() + "\n");
				MarriageList marriageList = new MarriageList(family, listCLP);
				marriageList.setInLineFlags(family, personList);
				outputStream.output("Marriage List: Count: " + marriageList.size() + "\n");
				ReferenceList referenceList = new ReferenceList(family, listCLP);
				outputStream.output("Reference List: Count: " + referenceList.size() + "\n");
				
				boolean bFormattedOutput = false;
				String strFormat = listCLP.getValue(COMMAND_LINE_PARAM_FORMAT);
				if (null != strFormat)
				{
					if (COMMAND_LINE_PARAM_FORMAT_VALUE_PRETTY.equals(strFormat))
					{
						bFormattedOutput = true;
					}
				}
				
				personList.marshallAllFile(family, bFormattedOutput);
				outputStream.output("Created Person List: allPersons.xml file\n");
				photoList.marshallAllFile(family, bFormattedOutput);
				outputStream.output("Created Person List: allPhotos.xml file\n");
				marriageList.marshallAllFile(family, bFormattedOutput);
				outputStream.output("Created Person List: allMarriages.xml file\n");
				referenceList.marshallAllFile(family, bFormattedOutput);
				outputStream.output("Created Person List: allReferences.xml file\n");
			}
			else if (strAction.equals(COMMAND_LINE_PARAM_ACTION_VALUE_HTMLFORM))
			{
				String strType = listCLP.getValue(COMMAND_LINE_PARAM_ACTION_VALUE_HTMLFORM_TYPE);
				if (null == strType)
				{
					showUsage("No \"" + COMMAND_LINE_PARAM_ACTION_VALUE_HTMLFORM_TYPE + "\" Command Line Parameter!");
				}
				
				PersonList personList = new PersonList(family, listCLP);
				outputStream.output("Person List: Count: " + personList.size() + "\n");
				PhotoList photoList = new PhotoList(family, listCLP);
				outputStream.output("Photo List: Count: " + photoList.size() + "\n");
				MarriageList marriageList = new MarriageList(family, listCLP);
				marriageList.setInLineFlags(family, personList);
				outputStream.output("Marriage List: Count: " + marriageList.size() + "\n");
				ReferenceList referenceList = new ReferenceList(family, listCLP);
				outputStream.output("Reference List: Count: " + referenceList.size() + "\n");
				// Set relationships in the personList
				IndexMarriageToSpouses idxMarrToSpouses = new IndexMarriageToSpouses(family, marriageList);
				IndexPersonToMarriages idxPerToMar = new IndexPersonToMarriages(family, marriageList);
				IndexMarriageToChildren idxMarToChild = new IndexMarriageToChildren(family, listCLP, personList);
				RelationshipManager.setRelationships(family, personList, marriageList, idxPerToMar, idxMarToChild);
				
				boolean bSuppressLiving = true;
				String strSuppressLiving = listCLP.getValue(COMMAND_LINE_PARAM_ACTION_VALUE_HTMLFORM_SUPPRESSLIVING);
				if (null != strSuppressLiving)
				{
					if (strSuppressLiving.equalsIgnoreCase("false"))
					{
						bSuppressLiving = false;
					}
				}
				
				boolean bSuppressLds = true;
				String strSuppressLds = listCLP.getValue(COMMAND_LINE_PARAM_ACTION_VALUE_HTMLFORM_SUPPRESSLDS);
				if (null != strSuppressLds)
				{
					if (strSuppressLds.equalsIgnoreCase("false"))
					{
						bSuppressLds = false;
					}
				}
				
				if (strType.equals(COMMAND_LINE_PARAM_ACTION_VALUE_HTMLFORM_TYPE_PERSONLIST))
				{
					HTMLPersonListForm form = new HTMLPersonListForm(family, personList, marriageList, bSuppressLiving, outputStream);
					form.create();
				}
				else if (strType.equals(COMMAND_LINE_PARAM_ACTION_VALUE_HTMLFORM_TYPE_PERSONINFO))
				{
					HTMLPersonInfoForm form = new HTMLPersonInfoForm(family, personList, marriageList, referenceList, photoList, bSuppressLiving, bSuppressLds, listCLP, outputStream);
					form.create();
				}
				else if (strType.equals(COMMAND_LINE_PARAM_ACTION_VALUE_HTMLFORM_TYPE_REFERENCELIST))
				{
					HTMLReferenceListForm form = new HTMLReferenceListForm(family, personList,
						     											   marriageList, referenceList,
						     											   photoList, idxMarrToSpouses,
						     											   bSuppressLiving,
						     											   listCLP, outputStream);
					form.create();
				}
				else if (strType.equals(COMMAND_LINE_PARAM_ACTION_VALUE_HTMLFORM_TYPE_PHOTOS))
				{
					HTMLPhotoForm form = new HTMLPhotoForm(family, personList,
							   marriageList, referenceList,
							   photoList, idxMarrToSpouses,
							   bSuppressLiving,
							   listCLP, outputStream);
					form.create();
				}
				else if (strType.equals(COMMAND_LINE_PARAM_ACTION_VALUE_HTMLFORM_TYPE_PHOTOLIST))
				{
					HTMLPhotoListForm form = new HTMLPhotoListForm(family, personList, marriageList, referenceList, photoList, idxMarrToSpouses, bSuppressLiving, listCLP, outputStream);
					form.create();
				}
				else if (strType.equals(COMMAND_LINE_PARAM_ACTION_VALUE_HTMLFORM_TYPE_REFERENCES))
				{
					HTMLReferenceForm form = new HTMLReferenceForm(family, personList, marriageList, referenceList, photoList, bSuppressLiving, bSuppressLds, listCLP, outputStream);
					form.create();
				}
				else if (strType.equals(COMMAND_LINE_PARAM_ACTION_VALUE_HTMLFORM_TYPE_FGS))
				{
					HTMLFGSForm form = new HTMLFGSForm(family, personList, marriageList, referenceList, photoList, bSuppressLiving, bSuppressLds, listCLP, outputStream);
					form.create();
				}
				else if (strType.equals(COMMAND_LINE_PARAM_ACTION_VALUE_HTMLFORM_TYPE_ALL))
				{
					HTMLPersonListForm formPersonList = new HTMLPersonListForm(family, personList, marriageList, bSuppressLiving, outputStream);
					formPersonList.create();
					HTMLPersonInfoForm formPersonInfo = new HTMLPersonInfoForm(family, personList, marriageList, referenceList, photoList, bSuppressLiving, bSuppressLds, listCLP, outputStream);
					formPersonInfo.create();
					HTMLReferenceListForm formReferenceList = new HTMLReferenceListForm(family, personList, marriageList, referenceList, photoList, idxMarrToSpouses, bSuppressLiving, listCLP, outputStream);
					formReferenceList.create();
					HTMLPhotoForm formPhotos = new HTMLPhotoForm(family, personList, marriageList, referenceList, photoList, idxMarrToSpouses, bSuppressLiving, listCLP, outputStream);
					formPhotos.create();
					HTMLReferenceForm formReferences = new HTMLReferenceForm(family, personList, marriageList, referenceList, photoList, bSuppressLiving, bSuppressLds, listCLP, outputStream);
					formReferences.create();
					HTMLFGSForm formFGS = new HTMLFGSForm(family, personList, marriageList, referenceList, photoList, bSuppressLiving, bSuppressLds, listCLP, outputStream);
					formFGS.create();
					HTMLPhotoListForm form = new HTMLPhotoListForm(family, personList, marriageList, referenceList, photoList, idxMarrToSpouses, bSuppressLiving, listCLP, outputStream);
					form.create();
				}
				else
				{
					showUsage("Unrecognized " + COMMAND_LINE_PARAM_ACTION_VALUE_HTMLFORM_TYPE + "\" Command Line Parameter Value!");
				}
			}
			else if (strAction.equals(COMMAND_LINE_PARAM_ACTION_VALUE_ERRORCHECK))
			{
				PersonList personList = new PersonList(family, listCLP);
				outputStream.output("Person List: Count: " + personList.size() + "\n");
				PhotoList photoList = new PhotoList(family, listCLP);
				outputStream.output("Photo List: Count: " + photoList.size() + "\n");
				MarriageList marriageList = new MarriageList(family, listCLP);
				marriageList.setInLineFlags(family, personList);
				outputStream.output("Marriage List: Count: " + marriageList.size() + "\n");
				ReferenceList referenceList = new ReferenceList(family, listCLP);
				outputStream.output("Reference List: Count: " + referenceList.size() + "\n");
				
				ErrorChecker errorChecker = new ErrorChecker(family, personList, marriageList, referenceList, photoList, listCLP, outputStream);
				errorChecker.check();
			}
			else if (strAction.equals(COMMAND_LINE_PARAM_ACTION_VALUE_GENERATIONS))
			{
				PersonList personList = new PersonList(family, listCLP);
				outputStream.output("Person List: Count: " + personList.size() + "\n");
				PhotoList photoList = new PhotoList(family, listCLP);
				outputStream.output("Photo List: Count: " + photoList.size() + "\n");
				MarriageList marriageList = new MarriageList(family, listCLP);
				marriageList.setInLineFlags(family, personList);
				outputStream.output("Marriage List: Count: " + marriageList.size() + "\n");
				ReferenceList referenceList = new ReferenceList(family, listCLP);
				outputStream.output("Reference List: Count: " + referenceList.size() + "\n");
				// Set relationships in the personList
				IndexMarriageToSpouses idxMarrToSpouses = new IndexMarriageToSpouses(family, marriageList);
				IndexPersonToMarriages idxPerToMar = new IndexPersonToMarriages(family, marriageList);
				IndexMarriageToChildren idxMarToChild = new IndexMarriageToChildren(family, listCLP, personList);
				RelationshipManager.setRelationships(family, personList, marriageList, idxPerToMar, idxMarToChild);

				GenerationManager generationManager = new GenerationManager(family, personList, listCLP, outputStream);
				generationManager.process();
			}
			else if (strAction.equals(COMMAND_LINE_PARAM_ACTION_VALUE_HTTPVALIDATE))
			{
				String strType = listCLP.getValue(COMMAND_LINE_PARAM_ACTION_VALUE_HTTPVALIDATE_TYPE);
				if (null == strType)
				{
					showUsage("No \"" + COMMAND_LINE_PARAM_ACTION_VALUE_HTTPVALIDATE_TYPE + "\" Command Line Parameter!");
				}
				
				PersonList personList = new PersonList(family, listCLP);
				outputStream.output("Person List: Count: " + personList.size() + "\n");
				PhotoList photoList = new PhotoList(family, listCLP);
				outputStream.output("Photo List: Count: " + photoList.size() + "\n");
				MarriageList marriageList = new MarriageList(family, listCLP);
				marriageList.setInLineFlags(family, personList);
				outputStream.output("Marriage List: Count: " + marriageList.size() + "\n");
				ReferenceList referenceList = new ReferenceList(family, listCLP);
				outputStream.output("Reference List: Count: " + referenceList.size() + "\n");
				WebSiteValidator validator = new WebSiteValidator(family, personList, marriageList, referenceList, photoList, listCLP, strType, outputStream);
				validator.validate();
			}
			else
			{
				showUsage("Unrecognized \"action\" Command Line Parameter Value!");
			}
			
			if (null != outputStream)
			{
				outputStream.deinitialize();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		System.exit(1);
	}
	
	private static void showUsage(String strMessage)
	{
		if (null != strMessage)
		{
			System.out.println(strMessage);
		}
		System.out.println("Usage: Genealogy [Parameters]");
		System.out.println(" Required Parameters");		
		System.out.println("   " + COMMAND_LINE_PARAM_CONFIG + "=c:\\gen\\config.properties");
		System.out.println("   " + COMMAND_LINE_PARAM_FAMILY + "=Jensen");
		System.out.println("   " + COMMAND_LINE_PARAM_ACTION + "=validate");
		System.out.println(" Possible Actions");
		System.out.println("   " + COMMAND_LINE_PARAM_ACTION_VALUE_VALIDATE);
		System.out.println("     " + COMMAND_LINE_PARAM_ACTION_VALUE_VALIDATE_TIME + "=1 (days, -1 for no time limits)");
		System.out.println("     " + COMMAND_LINE_PARAM_ACTION_VALUE_VALIDATE_TARGET + "=" +COMMAND_LINE_PARAM_TARGET_PERSONS + "," + COMMAND_LINE_PARAM_TARGET_MARRIAGES + " (also: \"" + COMMAND_LINE_PARAM_TARGET_REFERENCES + "\", \"" + COMMAND_LINE_PARAM_TARGET_PHOTOS + "\", default is all");	
		System.out.println("   " + COMMAND_LINE_PARAM_ACTION_VALUE_HTMLFORM);
		System.out.println("     " + COMMAND_LINE_PARAM_ACTION_VALUE_HTMLFORM_TYPE + "=fsg");
		System.out.println("       " + COMMAND_LINE_PARAM_ACTION_VALUE_HTMLFORM_TYPE_FGS + " for Family Group Sheets");
		System.out.println("       " + COMMAND_LINE_PARAM_ACTION_VALUE_HTMLFORM_TYPE_PERSONLIST + " for Person List Index");	
		System.out.println("       " + COMMAND_LINE_PARAM_ACTION_VALUE_HTMLFORM_TYPE_PHOTOLIST + " for Photo List Index");
		System.out.println("     " + COMMAND_LINE_PARAM_ACTION_VALUE_HTMLFORM_SUPPRESSLIVING + "=true/false");
		System.out.println("     " + COMMAND_LINE_PARAM_ACTION_VALUE_HTMLFORM_SUPPRESSLDS + "=true/false");
		System.out.println("   " + COMMAND_LINE_PARAM_ACTION_VALUE_GENERATEALL + " to generate the \"all\" versions of persons, marriages, photos, and references XML files");
		System.out.println("     " + COMMAND_LINE_PARAM_FORMAT + "=[XML Output Format]");
		System.out.println("       " + COMMAND_LINE_PARAM_FORMAT_VALUE_PRETTY + " for line ends and indented XML (default)");
		System.out.println("       " + COMMAND_LINE_PARAM_FORMAT_VALUE_COMPACT + " for unformatted small XML");
		System.exit(1);
	}
}
