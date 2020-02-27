package home.genealogy;

import java.util.ArrayList;
import java.util.Collection;

import home.genealogy.schema.all.helpers.PersonHelper;
import home.genealogy.util.CommandLineParameterList;
import home.genealogy.util.StringUtil;

public class CommandLineParameters
{
	public static final String COMMAND_LINE_PARAM_CONFIG = "config";
	public static final String COMMAND_LINE_PARAM_FAMILY = "family";
	public static final String COMMAND_LINE_PARAM_ACTION = "action";
	public static final String COMMAND_LINE_PARAM_XML_FORMAT = "format";
	public static final String COMMAND_LINE_PARAM_XML_FORMAT_VALUE_PRETTY = "pretty";
	public static final String COMMAND_LINE_PARAM_XML_FORMAT_VALUE_COMPACT = "compact";
	public static final String COMMAND_LINE_PARAM_SOURCE = "source";
	public static final String COMMAND_LINE_PARAM_SOURCE_VALUE_ALLXML = "allxml";
	public static final String COMMAND_LINE_PARAM_SOURCE_VALUE_INDIVIDUALXML = "individualxml";
	public static final String COMMAND_LINE_PARAM_DESTINATION = "destination";
	public static final String COMMAND_LINE_PARAM_DESTINATION_VALUE_ALLXML = "allxml";
	public static final String COMMAND_LINE_PARAM_DESTINATION_VALUE_INDIVIDUALXML = "individualxml";
	public static final String COMMAND_LINE_PARAM_ACTION_VALUE_VALIDATE = "validate";
	public static final String COMMAND_LINE_PARAM_ACTION_VALUE_VALIDATE_TIME = "time";
	public static final String COMMAND_LINE_PARAM_ACTION_VALUE_VALIDATE_TARGET = "target";
	public static final String COMMAND_LINE_PARAM_ACTION_VALUE_CONVERT = "convert";
	public static final String COMMAND_LINE_PARAM_ACTION_VALUE_HTMLFORM = "htmlform";
	public static final String COMMAND_LINE_PARAM_ACTION_VALUE_HTMLFORM_TARGET = "target";
	public static final String COMMAND_LINE_PARAM_ACTION_VALUE_HTMLFORM_SUPPRESSLIVING = "suppressliving";
	public static final String COMMAND_LINE_PARAM_ACTION_VALUE_HTMLFORM_SUPPRESSLDS = "suppresslds";
	
	public static final String COMMAND_LINE_PARAM_ACTION_VALUE_ERRORCHECK = "errorcheck";
	public static final String COMMAND_LINE_PARAM_ACTION_VALUE_ERRORCHECK_TYPE = "type";
	
	public static final String COMMAND_LINE_PARAM_ACTION_VALUE_HTTPVALIDATE = "httpvalidate";
	public static final String COMMAND_LINE_PARAM_ACTION_VALUE_HTTPVALIDATE_TARGET = "target";
	
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
	public static final String COMMAND_LINE_PARAM_TARGET_FGS = "fgs";
	public static final String COMMAND_LINE_PARAM_TARGET_PERSONLIST = "personlist";
	public static final String COMMAND_LINE_PARAM_TARGET_PERSONINFO = "personinfo";
	public static final String COMMAND_LINE_PARAM_TARGET_PHOTOLIST = "photolist";
	public static final String COMMAND_LINE_PARAM_TARGET_REFERENCELIST = "referencelist";
	public static final String COMMAND_LINE_PARAM_TARGET_ALL = "all";
	
	public static final String COMMAND_LINE_PARAM_LOG = "log";
	public static final String COMMAND_LINE_PARAM_LOG_VALUE_STDOUT = "stdout";
	public static final String COMMAND_LINE_PARAM_LOG_VALUE_FILE = "file";
	
	public static final String COMMAND_LINE_PARAM_LOG_FILE_FILENAME = "filename";
	public static final String COMMAND_LINE_PARAM_LOG_FILE_ECHO = "echo";
	
	private CommandLineParameterList m_listCLP;

	private static Collection<String> VALID_TRUE;
	private static Collection<String> VALID_FALSE;
	private static Collection<String> VALID_PARAMETER_NAMES;
	private static Collection<String> REQUIRED_PARAMETER_NAMES;
	private static Collection<String> VALID_ACTIONS;
	private static Collection<String> VALID_SOURCE_TYPES;
	private static Collection<String> VALID_DESTINATION_TYPES;
	private static Collection<String> VALID_LOG_TYPES;
	private static Collection<String> VALID_LOG_ECHOS;
	private static Collection<String> VALID_XML_FORMAT_TYPES;
	private static Collection<String> VALID_VALIDATE_TARGETS;
	private static Collection<String> VALID_HTMLFORM_TARGETS;
	private static Collection<String> VALID_HTTPVALIDATE_TARGETS;
	private static Collection<String> VALID_SUPPRESS_LIVING;
	private static Collection<String> VALID_GENERATIONS_TYPE;
	
	static
	{
		VALID_TRUE = new ArrayList<String>();
		VALID_TRUE.add("true");
		VALID_TRUE.add("yes");
		VALID_TRUE.add("1");
		
		VALID_FALSE = new ArrayList<String>();
		VALID_FALSE.add("false");
		VALID_FALSE.add("no");
		VALID_FALSE.add("0");
		
		VALID_PARAMETER_NAMES = new ArrayList<String>();
		VALID_PARAMETER_NAMES.add(COMMAND_LINE_PARAM_CONFIG);
		VALID_PARAMETER_NAMES.add(COMMAND_LINE_PARAM_FAMILY);
		VALID_PARAMETER_NAMES.add(COMMAND_LINE_PARAM_ACTION);
		VALID_PARAMETER_NAMES.add(COMMAND_LINE_PARAM_XML_FORMAT);
		VALID_PARAMETER_NAMES.add(COMMAND_LINE_PARAM_SOURCE);
		VALID_PARAMETER_NAMES.add(COMMAND_LINE_PARAM_LOG);
		VALID_PARAMETER_NAMES.add(COMMAND_LINE_PARAM_LOG_FILE_FILENAME);
		VALID_PARAMETER_NAMES.add(COMMAND_LINE_PARAM_LOG_FILE_ECHO);
		VALID_PARAMETER_NAMES.add(COMMAND_LINE_PARAM_ACTION_VALUE_HTMLFORM_SUPPRESSLIVING);

		REQUIRED_PARAMETER_NAMES = new ArrayList<String>();
		REQUIRED_PARAMETER_NAMES.add(COMMAND_LINE_PARAM_CONFIG);
		REQUIRED_PARAMETER_NAMES.add(COMMAND_LINE_PARAM_FAMILY);
		REQUIRED_PARAMETER_NAMES.add(COMMAND_LINE_PARAM_ACTION);
		
		VALID_ACTIONS = new ArrayList<String>();
		VALID_ACTIONS.add(COMMAND_LINE_PARAM_ACTION_VALUE_VALIDATE);
		VALID_ACTIONS.add(COMMAND_LINE_PARAM_ACTION_VALUE_CONVERT);
		VALID_ACTIONS.add(COMMAND_LINE_PARAM_ACTION_VALUE_HTMLFORM);
		VALID_ACTIONS.add(COMMAND_LINE_PARAM_ACTION_VALUE_ERRORCHECK);
		VALID_ACTIONS.add(COMMAND_LINE_PARAM_ACTION_VALUE_HTTPVALIDATE);
		VALID_ACTIONS.add(COMMAND_LINE_PARAM_ACTION_VALUE_GENERATIONS);

		VALID_SOURCE_TYPES = new ArrayList<String>();
		VALID_SOURCE_TYPES.add(COMMAND_LINE_PARAM_SOURCE_VALUE_ALLXML);
		VALID_SOURCE_TYPES.add(COMMAND_LINE_PARAM_SOURCE_VALUE_INDIVIDUALXML);
		
		VALID_DESTINATION_TYPES = new ArrayList<String>();
		VALID_DESTINATION_TYPES.add(COMMAND_LINE_PARAM_DESTINATION_VALUE_ALLXML);
		VALID_DESTINATION_TYPES.add(COMMAND_LINE_PARAM_DESTINATION_VALUE_INDIVIDUALXML);
		
		VALID_LOG_TYPES = new ArrayList<String>();
		VALID_LOG_TYPES.add(COMMAND_LINE_PARAM_LOG_VALUE_STDOUT);
		VALID_LOG_TYPES.add(COMMAND_LINE_PARAM_LOG_VALUE_FILE);
		
		VALID_LOG_ECHOS = new ArrayList<String>();
		VALID_LOG_ECHOS.addAll(VALID_TRUE);
		VALID_LOG_ECHOS.addAll(VALID_FALSE);
		
		VALID_SUPPRESS_LIVING = new ArrayList<String>();
		VALID_SUPPRESS_LIVING.addAll(VALID_TRUE);
		VALID_SUPPRESS_LIVING.addAll(VALID_FALSE);
		
		VALID_XML_FORMAT_TYPES = new ArrayList<String>();
		VALID_XML_FORMAT_TYPES.add(COMMAND_LINE_PARAM_XML_FORMAT_VALUE_PRETTY);
		VALID_XML_FORMAT_TYPES.add(COMMAND_LINE_PARAM_XML_FORMAT_VALUE_COMPACT);

		VALID_VALIDATE_TARGETS = new ArrayList<String>();
		VALID_VALIDATE_TARGETS.add(COMMAND_LINE_PARAM_TARGET_PERSONS);
		VALID_VALIDATE_TARGETS.add(COMMAND_LINE_PARAM_TARGET_MARRIAGES);
		VALID_VALIDATE_TARGETS.add(COMMAND_LINE_PARAM_TARGET_REFERENCES);
		VALID_VALIDATE_TARGETS.add(COMMAND_LINE_PARAM_TARGET_PHOTOS);
		VALID_VALIDATE_TARGETS.add(COMMAND_LINE_PARAM_TARGET_ALL);
		
		VALID_HTMLFORM_TARGETS = new ArrayList<String>();
		VALID_HTMLFORM_TARGETS.add(COMMAND_LINE_PARAM_TARGET_PERSONLIST);
		VALID_HTMLFORM_TARGETS.add(COMMAND_LINE_PARAM_TARGET_PERSONINFO);
		VALID_HTMLFORM_TARGETS.add(COMMAND_LINE_PARAM_TARGET_PHOTOS);
		VALID_HTMLFORM_TARGETS.add(COMMAND_LINE_PARAM_TARGET_PHOTOLIST);
		VALID_HTMLFORM_TARGETS.add(COMMAND_LINE_PARAM_TARGET_FGS);
		VALID_HTMLFORM_TARGETS.add(COMMAND_LINE_PARAM_TARGET_REFERENCES);
		VALID_HTMLFORM_TARGETS.add(COMMAND_LINE_PARAM_TARGET_REFERENCELIST);
		VALID_HTMLFORM_TARGETS.add(COMMAND_LINE_PARAM_TARGET_ALL);
		
		VALID_HTTPVALIDATE_TARGETS = new ArrayList<String>();
		VALID_HTTPVALIDATE_TARGETS.add(COMMAND_LINE_PARAM_TARGET_PERSONINFO);
		VALID_HTTPVALIDATE_TARGETS.add(COMMAND_LINE_PARAM_TARGET_PHOTOS);
		VALID_HTTPVALIDATE_TARGETS.add(COMMAND_LINE_PARAM_TARGET_FGS);
		VALID_HTTPVALIDATE_TARGETS.add(COMMAND_LINE_PARAM_TARGET_REFERENCES);
		VALID_HTTPVALIDATE_TARGETS.add(COMMAND_LINE_PARAM_TARGET_ALL);
		
		VALID_GENERATIONS_TYPE = new ArrayList<String>();
		VALID_GENERATIONS_TYPE.add(COMMAND_LINE_PARAM_ACTION_VALUE_GENERATIONS_TYPE_ALL);
		VALID_GENERATIONS_TYPE.add(COMMAND_LINE_PARAM_ACTION_VALUE_GENERATIONS_TYPE_LIVING);
		VALID_GENERATIONS_TYPE.add(COMMAND_LINE_PARAM_ACTION_VALUE_GENERATIONS_TYPE_DEAD);
	}

	public CommandLineParameters(String[] args)
		throws UsageException
	{
		if (0 == args.length)
		{
			throw new UsageException("Zero Command Line Parameters!");
		}
		
		m_listCLP = new CommandLineParameterList(args);
		
		// Ensure all provided parameters are valid...
		Collection<String> cProvidedParameterNames = m_listCLP.getCommandLineParameterNames();
		for (String strName : cProvidedParameterNames)
		{
			validateParameter(strName);
		}
		// Ensure all required parameters were provided
		validateRequiredParameters(cProvidedParameterNames);
		
		// Validate all parameters
		String strValidated = validateLog(m_listCLP.getValue(COMMAND_LINE_PARAM_LOG));
		m_listCLP.set(COMMAND_LINE_PARAM_LOG, strValidated);
		validateLogFileType(strValidated);
		validateLogEcho();
		
		strValidated = validateAction(m_listCLP.getValue(COMMAND_LINE_PARAM_ACTION));
		m_listCLP.set(COMMAND_LINE_PARAM_ACTION, strValidated);

		strValidated = validateSource(m_listCLP.getValue(COMMAND_LINE_PARAM_SOURCE));
		m_listCLP.set(COMMAND_LINE_PARAM_SOURCE, strValidated);
		
		strValidated = validateDestination(m_listCLP.getValue(COMMAND_LINE_PARAM_DESTINATION));
		m_listCLP.set(COMMAND_LINE_PARAM_DESTINATION, strValidated);
		
		validateSourceWithDestination(m_listCLP.getValue(COMMAND_LINE_PARAM_SOURCE),
										m_listCLP.getValue(COMMAND_LINE_PARAM_DESTINATION));
		
		strValidated = validateXmlFormat(m_listCLP.getValue(COMMAND_LINE_PARAM_XML_FORMAT));
		m_listCLP.set(COMMAND_LINE_PARAM_XML_FORMAT, strValidated);
		
		strValidated = validateValidateTargets(m_listCLP.getValue(COMMAND_LINE_PARAM_ACTION),
											   m_listCLP.getValue(COMMAND_LINE_PARAM_ACTION_VALUE_VALIDATE_TARGET));
		m_listCLP.set(COMMAND_LINE_PARAM_ACTION_VALUE_VALIDATE_TARGET, strValidated);
		
		strValidated = validateHtmlFormTargets(m_listCLP.getValue(COMMAND_LINE_PARAM_ACTION),
											   m_listCLP.getValue(COMMAND_LINE_PARAM_ACTION_VALUE_HTMLFORM_TARGET));
		m_listCLP.set(COMMAND_LINE_PARAM_ACTION_VALUE_HTMLFORM_TARGET, strValidated);
		
		strValidated = validateHttpTargets(m_listCLP.getValue(COMMAND_LINE_PARAM_ACTION),
										   m_listCLP.getValue(COMMAND_LINE_PARAM_ACTION_VALUE_HTTPVALIDATE_TARGET));
		m_listCLP.set(COMMAND_LINE_PARAM_ACTION_VALUE_HTTPVALIDATE_TARGET, strValidated);
		
		validateSuppessLiving();
		
		strValidated = validateGenerationsType(m_listCLP.getValue(COMMAND_LINE_PARAM_ACTION_VALUE_GENERATIONS_TYPE));
		m_listCLP.set(COMMAND_LINE_PARAM_ACTION_VALUE_GENERATIONS_TYPE, strValidated);
	}
	
	public String getLogType()
	{
		return m_listCLP.getValue(COMMAND_LINE_PARAM_LOG);
	}
	
	public String getLogFilename()
	{
		return m_listCLP.getValue(COMMAND_LINE_PARAM_LOG_FILE_FILENAME);
	}
	
	public boolean getLogFileEcho()
	{
		return m_listCLP.getBooleanValue(COMMAND_LINE_PARAM_LOG_FILE_ECHO);
	}
	
	public boolean getSuppressLiving()
	{
		return m_listCLP.getBooleanValue(COMMAND_LINE_PARAM_ACTION_VALUE_HTMLFORM_SUPPRESSLIVING);
	}
	
	public String getConfigurationFilename()
	{
		return m_listCLP.getValue(COMMAND_LINE_PARAM_CONFIG);
	}
	
	public String getFamilyName()
	{
		return m_listCLP.getValue(COMMAND_LINE_PARAM_FAMILY);
	}
	
	public String getAction()
	{
		return m_listCLP.getValue(COMMAND_LINE_PARAM_ACTION);
	}
	
	public boolean isActionValidate()
	{
		return getAction().equals(COMMAND_LINE_PARAM_ACTION_VALUE_VALIDATE);
	}

	public boolean isActionConvert()
	{
		return getAction().equals(COMMAND_LINE_PARAM_ACTION_VALUE_CONVERT);
	}
	
	public boolean isActionHtmlForm()
	{
		return getAction().equals(COMMAND_LINE_PARAM_ACTION_VALUE_HTMLFORM);
	}
	
	public boolean isActionErrorCheck()
	{
		return getAction().equals(COMMAND_LINE_PARAM_ACTION_VALUE_ERRORCHECK);
	}
	
	public boolean isActionHttpValidate()
	{
		return getAction().equals(COMMAND_LINE_PARAM_ACTION_VALUE_HTTPVALIDATE);
	}
	
	public boolean isActionGenerations()
	{
		return getAction().equals(COMMAND_LINE_PARAM_ACTION_VALUE_GENERATIONS);
	}
	
	public String getSource()
	{
		return m_listCLP.getValue(COMMAND_LINE_PARAM_SOURCE);
	}
	
	public boolean isSourceIndividualXMLs()
	{
		return getSource().equals(COMMAND_LINE_PARAM_SOURCE_VALUE_INDIVIDUALXML); 
	}
	
	public boolean isSourceAllXMLs()
	{
		return getSource().equals(COMMAND_LINE_PARAM_SOURCE_VALUE_ALLXML); 
	}
	
	public String getDestination()
	{
		return m_listCLP.getValue(COMMAND_LINE_PARAM_DESTINATION);
	}
	
	public boolean isDestinationIndividualXMLs()
	{
		return getDestination().equals(COMMAND_LINE_PARAM_DESTINATION_VALUE_INDIVIDUALXML); 
	}
	
	public boolean isDestinationAllXMLs()
	{
		return getDestination().equals(COMMAND_LINE_PARAM_DESTINATION_VALUE_ALLXML); 
	}
	
	public String getXmlFormat()
	{
		return m_listCLP.getValue(COMMAND_LINE_PARAM_XML_FORMAT);
	}
	
	public long getValidateTimeLimit()
	{
		return m_listCLP.getLongValue(COMMAND_LINE_PARAM_ACTION_VALUE_VALIDATE_TIME, 0);
	}
	
	public String getValidateTarget()
	{
		return m_listCLP.getValue(COMMAND_LINE_PARAM_ACTION_VALUE_VALIDATE_TARGET);
	}
	
	public boolean isValidateTargetAll()
	{
		return getValidateTarget().equals(COMMAND_LINE_PARAM_TARGET_ALL);
	}
	
	public boolean isValidateTargetPersons()
	{
		return getValidateTarget().equals(COMMAND_LINE_PARAM_TARGET_PERSONS);
	}
	
	public boolean isValidateTargetMarriages()
	{
		return getValidateTarget().equals(COMMAND_LINE_PARAM_TARGET_MARRIAGES);
	}
	
	public boolean isValidateTargetPhotos()
	{
		return getValidateTarget().equals(COMMAND_LINE_PARAM_TARGET_PHOTOS);
	}
	
	public boolean isValidateTargetReferences()
	{
		return getValidateTarget().equals(COMMAND_LINE_PARAM_TARGET_REFERENCES);
	}
	
	public String getHtmlFormTarget()
	{
		return m_listCLP.getValue(COMMAND_LINE_PARAM_ACTION_VALUE_HTMLFORM_TARGET);
	}
	
	public boolean isHtmlFormTargetPersonList()
	{
		return getHtmlFormTarget().equals(COMMAND_LINE_PARAM_TARGET_PERSONLIST);
	}
	
	public boolean isHtmlFormTargetPersonInfo()
	{
		return getHtmlFormTarget().equals(COMMAND_LINE_PARAM_TARGET_PERSONINFO);
	}

	public boolean isHtmlFormTargetPhotos()
	{
		return getHtmlFormTarget().equals(COMMAND_LINE_PARAM_TARGET_PHOTOS);
	}
	
	public boolean isHtmlFormTargetPhotoList()
	{
		return getHtmlFormTarget().equals(COMMAND_LINE_PARAM_TARGET_PHOTOLIST);
	}
	
	public boolean isHtmlFormTargetFGS()
	{
		return getHtmlFormTarget().equals(COMMAND_LINE_PARAM_TARGET_FGS);
	}
	
	public boolean isHtmlFormTargetReferences()
	{
		return getHtmlFormTarget().equals(COMMAND_LINE_PARAM_TARGET_REFERENCES);
	}
	
	public boolean isHtmlFormTargetReferenceList()
	{
		return getHtmlFormTarget().equals(COMMAND_LINE_PARAM_TARGET_REFERENCELIST);
	}
	
	public boolean isHtmlFormTargetAll()
	{
		return getHtmlFormTarget().equals(COMMAND_LINE_PARAM_TARGET_ALL);
	}
	
	public String getHttpValidateTarget()
	{
		return m_listCLP.getValue(COMMAND_LINE_PARAM_ACTION_VALUE_HTTPVALIDATE_TARGET);
	}
	
	public boolean isHttpValidateTargetAll()
	{
		return getHttpValidateTarget().equals(COMMAND_LINE_PARAM_TARGET_ALL);
	}

	public boolean isHttpValidateTargetPersonInfo()
	{
		return getHttpValidateTarget().equals(COMMAND_LINE_PARAM_TARGET_PERSONINFO);
	}
	
	public boolean isHttpValidateTargetPhotos()
	{
		return getHttpValidateTarget().equals(COMMAND_LINE_PARAM_TARGET_PHOTOS);
	}
	
	public boolean isHttpValidateTargetFGS()
	{
		return getHttpValidateTarget().equals(COMMAND_LINE_PARAM_TARGET_FGS);
	}
	
	public boolean isHttpValidateTargetReferences()
	{
		return getHttpValidateTarget().equals(COMMAND_LINE_PARAM_TARGET_REFERENCES);
	}
	
	public String getGenerationsType()
	{
		return m_listCLP.getValue(COMMAND_LINE_PARAM_ACTION_VALUE_GENERATIONS_TYPE);
	}
	
	public boolean isGenerationsTypeAll()
	{
		return getGenerationsType().equals(COMMAND_LINE_PARAM_ACTION_VALUE_GENERATIONS_TYPE_ALL);
	}
	
	public boolean isGenerationsTypeLiving()
	{
		return getGenerationsType().equals(COMMAND_LINE_PARAM_ACTION_VALUE_GENERATIONS_TYPE_LIVING);
	}
	
	public boolean isGenerationsTypeDead()
	{
		return getGenerationsType().equals(COMMAND_LINE_PARAM_ACTION_VALUE_GENERATIONS_TYPE_DEAD);
	}
	
	public int getGenerationsNth(int iDefault)
	{
		return m_listCLP.getIntegerValue(COMMAND_LINE_PARAM_ACTION_VALUE_GENERATIONS_GENERATION_NTH, iDefault);
	}
	
	public int getGenerationsRangeStart(int iDefault)
	{
		return m_listCLP.getIntegerValue(COMMAND_LINE_PARAM_ACTION_VALUE_GENERATIONS_GENERATION_RANGE_START, iDefault);
	}
	
	public int getGenerationsRangeEnd(int iDefault)
	{
		return m_listCLP.getIntegerValue(COMMAND_LINE_PARAM_ACTION_VALUE_GENERATIONS_GENERATION_RANGE_END, iDefault);
	}

	private void validateRequiredParameters(Collection<String> cProvidedParameterNames)
		throws UsageException
	{
		for (String strRequiredName : REQUIRED_PARAMETER_NAMES)
		{
			if (!cProvidedParameterNames.contains(strRequiredName))
			{
				throw new UsageException("Missing required command line parameter: " + strRequiredName + "\n");
			}
			String strValue = m_listCLP.getValue(strRequiredName);
			if (!StringUtil.exists(strValue))
			{
				throw new UsageException("Missing value for required command line parameter: " + strRequiredName + "\n");
			}
		}
	}
	
	private void validateParameter(String strParameterCandidate)
		throws UsageException
	{
		if (StringUtil.exists(strParameterCandidate))
		{
			if (!VALID_PARAMETER_NAMES.contains(strParameterCandidate))
			{
				throw new UsageException("Invalid command line parameter: " + strParameterCandidate + "\n");
			}
		}
		else
		{
			throw new UsageException("Invalid command line parameter: Null or empty value!\n");
		}
	}
	
	/**
	 * Validate the value of the action command line parameter.
	 * <p>
	 * Since the action parameter is required, the provided
	 * parameter value cannot (must not) be {@code null}.
	 * 
	 * @param strActionCandidate The candidate action value. Must not be {@code null}.
	 * @return A valid value for the action parameter.
	 * @throws UsageException if there is an invalid value for the action parameter.
	 */
	private String validateAction(String strActionCandidate)
		throws UsageException
	{
		strActionCandidate = strActionCandidate.toLowerCase();
		if (!VALID_ACTIONS.contains(strActionCandidate))
		{
			throw new UsageException("Invalid value for required command line parameter action: " + strActionCandidate + "\n");
		}
		return strActionCandidate;
	}

	private String validateSource(String strSourceCandidate)
		throws UsageException
	{
		String strResult = COMMAND_LINE_PARAM_SOURCE_VALUE_INDIVIDUALXML;
		if (StringUtil.exists(strSourceCandidate))
		{
			strSourceCandidate = strSourceCandidate.toLowerCase();
			if (!VALID_SOURCE_TYPES.contains(strSourceCandidate))
			{
				throw new UsageException("Invalid value for command line parameter source: " + strSourceCandidate + "\n");
			}
			strResult = strSourceCandidate;
		}
		return strResult;
	}
	
	private String validateDestination(String strDestinationCandidate)
		throws UsageException
	{
		String strResult = COMMAND_LINE_PARAM_DESTINATION_VALUE_ALLXML;
		if (StringUtil.exists(strDestinationCandidate))
		{
			strDestinationCandidate = strDestinationCandidate.toLowerCase();
			if (!VALID_DESTINATION_TYPES.contains(strDestinationCandidate))
			{
				throw new UsageException("Invalid value for command line parameter destination: " + strDestinationCandidate + "\n");
			}
			strResult = strDestinationCandidate;
		}
		return strResult;
	}
	
	/**
	 * Validate that the source and destination values make sense.
	 * This method assumes that the values have already been validate
	 * as valid values. It does not make sense to have the source and
	 * destination be the same because it results in a NOP.
	 * <p>
	 * If this method executes without throwing an exception, then
	 * the source and destination identifiers are considered valid.
	 * 
	 * @param strSourceCandidate The proposed source identifier.
	 * @param strDestinationCandidate The proposed destination identifier.
	 * @throws UsageException Only thrown if an invalid combination o source
	 * and destination identifiers exist.
	 */
	private void validateSourceWithDestination(String strSourceCandidate, String strDestinationCandidate)
		throws UsageException
	{
		String strMessage = "Source and destination are the same: " + strSourceCandidate;
		if (strSourceCandidate == strDestinationCandidate)
		{
			throw new UsageException(strMessage);
		}
		else if ((StringUtil.exists(strSourceCandidate)) &&
				 (StringUtil.exists(strDestinationCandidate)) &&
				 strSourceCandidate.equalsIgnoreCase(strDestinationCandidate))
		{
			throw new UsageException(strMessage);
		}
	}
	
	private String validateLog(String strLogCandidate)
		throws UsageException
	{
		String strResult = COMMAND_LINE_PARAM_LOG_VALUE_STDOUT;
		if (StringUtil.exists(strLogCandidate))
		{
			strLogCandidate = strLogCandidate.toLowerCase();
			if (!VALID_LOG_TYPES.contains(strLogCandidate))
			{
				throw new UsageException("Invalid value for command line parameter log: " + strLogCandidate + "\n");
			}
			strResult = strLogCandidate;
		}
		return strResult;
	}
	
	private void validateLogFileType(String strLogType)
		throws UsageException
	{
		if (strLogType.equals(COMMAND_LINE_PARAM_LOG_VALUE_FILE))
		{
			String strFilename = m_listCLP.getValue(COMMAND_LINE_PARAM_LOG_FILE_FILENAME);
			if (!StringUtil.exists(strFilename))
			{
				throw new UsageException("Invalid value for command line parameter log: " + strLogType + ". A log type of \"file\" was specified, but no \"" + COMMAND_LINE_PARAM_LOG_FILE_FILENAME + "\" parameter was provided.\n");
			}
		}
	}
	
	private void validateLogEcho()
		throws UsageException
	{
		String strEcho = m_listCLP.getValue(COMMAND_LINE_PARAM_LOG_FILE_ECHO);
		if (!StringUtil.exists(strEcho))
		{
			m_listCLP.set(COMMAND_LINE_PARAM_LOG_FILE_ECHO, "false");
		}
		else
		{
			strEcho = strEcho.toLowerCase();
			if (!VALID_LOG_ECHOS.contains(strEcho))
			{
				throw new UsageException("Invalid value for command line parameter echo: " + strEcho + "\n");
			}
			m_listCLP.set(COMMAND_LINE_PARAM_LOG_FILE_ECHO, strEcho);
		}
	}
	
	private String validateXmlFormat(String strFormatCandidate)
		throws UsageException
	{
		String strResult = COMMAND_LINE_PARAM_XML_FORMAT_VALUE_PRETTY;
		if (StringUtil.exists(strFormatCandidate))
		{
			strFormatCandidate = strFormatCandidate.toLowerCase();
			if (!VALID_XML_FORMAT_TYPES.contains(strFormatCandidate))
			{
				throw new UsageException("Invalid value for command line parameter xml format: " + strFormatCandidate + "\n");
			}
			strResult = strFormatCandidate;
		}
		return strResult;
	}
	
	private String validateGenerationsType(String strTypeCandidate)
		throws UsageException
	{
		String strResult = COMMAND_LINE_PARAM_ACTION_VALUE_GENERATIONS_TYPE_ALL;
		if (StringUtil.exists(strTypeCandidate))
		{
			strTypeCandidate = strTypeCandidate.toLowerCase();
			if (!VALID_GENERATIONS_TYPE.contains(strTypeCandidate))
			{
				throw new UsageException("Invalid value for command line parameter generations type: " + strTypeCandidate + "\n");
			}
			strResult = strTypeCandidate;
		}
		return strResult;
	}
	
	private String validateValidateTargets(String strAction, String strTargetCandidate)
		throws UsageException
	{
		String strResult = COMMAND_LINE_PARAM_TARGET_ALL;
		if (strAction.equals(COMMAND_LINE_PARAM_ACTION_VALUE_VALIDATE))
		{	// Only need to look further if the action is VALIDATE
			if (StringUtil.exists(strTargetCandidate))
			{
				strTargetCandidate = strTargetCandidate.toLowerCase();
				if (!VALID_VALIDATE_TARGETS.contains(strTargetCandidate))
				{
					throw new UsageException("Invalid value for command line parameter validate target: " + strTargetCandidate + "\n");
				}
				strResult = strTargetCandidate;
			}
		}
		return strResult;
	}
	
	private String validateHtmlFormTargets(String strAction, String strTargetCandidate)
		throws UsageException
	{
		String strResult = COMMAND_LINE_PARAM_TARGET_ALL;
		if (strAction.equals(COMMAND_LINE_PARAM_ACTION_VALUE_HTMLFORM))
		{	// Only need to look further if the action is HTMLFORM
			if (StringUtil.exists(strTargetCandidate))
			{
				strTargetCandidate = strTargetCandidate.toLowerCase();
				if (!VALID_HTMLFORM_TARGETS.contains(strTargetCandidate))
				{
					throw new UsageException("Invalid value for command line parameter htmlform target: " + strTargetCandidate + "\n");
				}
				strResult = strTargetCandidate;
			}
		}
		return strResult;
	}
	
	private String validateHttpTargets(String strAction, String strTargetCandidate)
		throws UsageException
	{
		String strResult = COMMAND_LINE_PARAM_TARGET_ALL;
		if (strAction.equals(COMMAND_LINE_PARAM_ACTION_VALUE_HTTPVALIDATE))
		{	// Only need to look further if the action is HTTPVALIDATE
			if (StringUtil.exists(strTargetCandidate))
			{
				strTargetCandidate = strTargetCandidate.toLowerCase();
				if (!VALID_HTTPVALIDATE_TARGETS.contains(strTargetCandidate))
				{
					throw new UsageException("Invalid value for command line parameter http validate target: " + strTargetCandidate + "\n");
				}
				strResult = strTargetCandidate;
			}
		}
		return strResult;
	}
	
	private void validateSuppessLiving()
		throws UsageException
	{
		String strSuppressLiving = m_listCLP.getValue(COMMAND_LINE_PARAM_ACTION_VALUE_HTMLFORM_SUPPRESSLIVING);
		if (!StringUtil.exists(strSuppressLiving))
		{
			m_listCLP.set(COMMAND_LINE_PARAM_ACTION_VALUE_HTMLFORM_SUPPRESSLIVING, "true");
		}
		else
		{
			strSuppressLiving = strSuppressLiving.toLowerCase();
			if (!VALID_SUPPRESS_LIVING.contains(strSuppressLiving))
			{
				throw new UsageException("Invalid value for command line parameter suppressliving: " + strSuppressLiving + "\n");
			}
			m_listCLP.set(COMMAND_LINE_PARAM_ACTION_VALUE_HTMLFORM_SUPPRESSLIVING, strSuppressLiving);
		}
	}
	
	public static void showUsage(String strMessage)
	{
		if (null != strMessage)
		{
			System.out.println(strMessage);
		}
		System.out.println("Usage:");
		System.out.println(" Required Parameters");		
		System.out.println("   " + COMMAND_LINE_PARAM_CONFIG + "=[absolute path to config file]");
		System.out.println("   " + COMMAND_LINE_PARAM_FAMILY + "=[family surname]");
		System.out.println("   " + COMMAND_LINE_PARAM_ACTION + "=[action]");
		System.out.println(" Optional (or Action Sensitive Parameters");
		System.out.println("   " + COMMAND_LINE_PARAM_LOG + "=[file or stdout specifier]");
		System.out.println("       " + COMMAND_LINE_PARAM_LOG_VALUE_STDOUT + " for Standard Out logging only (default)");
		System.out.println("       " + COMMAND_LINE_PARAM_LOG_VALUE_FILE + " for File logging (requires filename to be specified)");
		System.out.println("   " + COMMAND_LINE_PARAM_LOG_FILE_FILENAME + "=[absolute path to log file]");
		System.out.println("   " + COMMAND_LINE_PARAM_LOG_FILE_ECHO + "=[true or false]");		
		System.out.println("       only applicable when Standard Out logging is specified, if true will echo logged data to Standard Out");
		System.out.println(" Possible Actions");
		System.out.println("   " + COMMAND_LINE_PARAM_ACTION_VALUE_VALIDATE);
		System.out.println("     " + COMMAND_LINE_PARAM_ACTION_VALUE_VALIDATE_TIME + "=1 (days, -1 for no time limits)");
		System.out.println("     " + COMMAND_LINE_PARAM_ACTION_VALUE_VALIDATE_TARGET + "=" +COMMAND_LINE_PARAM_TARGET_PERSONS + "," + COMMAND_LINE_PARAM_TARGET_MARRIAGES + " (also: \"" + COMMAND_LINE_PARAM_TARGET_REFERENCES + "\", \"" + COMMAND_LINE_PARAM_TARGET_PHOTOS + "\", default is all");	
		System.out.println("   " + COMMAND_LINE_PARAM_ACTION_VALUE_HTMLFORM);
		System.out.println("     " + COMMAND_LINE_PARAM_ACTION_VALUE_HTMLFORM_TARGET + "=[specifier for which forms to generate]");
		System.out.println("       " + COMMAND_LINE_PARAM_TARGET_FGS + " for Family Group Sheets only");
		System.out.println("       " + COMMAND_LINE_PARAM_TARGET_PHOTOS + " for Photos only");
		System.out.println("       " + COMMAND_LINE_PARAM_TARGET_REFERENCES + " for References only");
		System.out.println("       " + COMMAND_LINE_PARAM_TARGET_PERSONLIST + " for Person List Index only");
		System.out.println("       " + COMMAND_LINE_PARAM_TARGET_PHOTOLIST + " for Photo List Index only");
		System.out.println("       " + COMMAND_LINE_PARAM_TARGET_REFERENCELIST + " for Reference List Index only");
		System.out.println("       " + COMMAND_LINE_PARAM_TARGET_ALL + " for all targets");
		System.out.println("     " + COMMAND_LINE_PARAM_ACTION_VALUE_HTMLFORM_SUPPRESSLIVING + "=true/false");
		System.out.println("     " + COMMAND_LINE_PARAM_ACTION_VALUE_HTMLFORM_SUPPRESSLDS + "=true/false");

		System.out.println("   " + COMMAND_LINE_PARAM_SOURCE + "=[source: all*.XML files OR individual XML files]");
		System.out.println("     " + COMMAND_LINE_PARAM_SOURCE_VALUE_ALLXML + " for populating lists from the all*.xml files");
		System.out.println("     " + COMMAND_LINE_PARAM_SOURCE_VALUE_INDIVIDUALXML + " for populating lists from individual xml files");
		System.out.println("   " + COMMAND_LINE_PARAM_DESTINATION + "=[destination: all*.XML files OR individual XML files]");
		System.out.println("     " + COMMAND_LINE_PARAM_DESTINATION_VALUE_ALLXML + " for writing lists to the all*.xml files");
		System.out.println("     " + COMMAND_LINE_PARAM_DESTINATION_VALUE_INDIVIDUALXML + " for writing lists to individual xml files");

		System.out.println("   " + COMMAND_LINE_PARAM_ACTION_VALUE_CONVERT + " to read all lists from a source and write to a destination");
		System.out.println("     " + COMMAND_LINE_PARAM_XML_FORMAT + "=[XML Output Format]");
		System.out.println("       " + COMMAND_LINE_PARAM_XML_FORMAT_VALUE_PRETTY + " for line ends and indented XML (default)");
		System.out.println("       " + COMMAND_LINE_PARAM_XML_FORMAT_VALUE_COMPACT + " for unformatted small XML");

		System.out.println(" Example Command Lines");
		System.out.println("   java -jar d:\\bin\\generator-1.0.0-jar-with-dependencies.jar " + COMMAND_LINE_PARAM_CONFIG + "=d:\\genealogy\\configuration\families.properties " + COMMAND_LINE_PARAM_FAMILY + "=jensen " + COMMAND_LINE_PARAM_ACTION + "=" + COMMAND_LINE_PARAM_ACTION_VALUE_CONVERT + " " + COMMAND_LINE_PARAM_SOURCE + "=" + COMMAND_LINE_PARAM_SOURCE_VALUE_INDIVIDUALXML +" " + COMMAND_LINE_PARAM_DESTINATION + "=" + COMMAND_LINE_PARAM_DESTINATION_VALUE_ALLXML);
		System.out.println("      to read all lists from individual xml files and write all list content to their respective all*.xml files");
		System.out.println("      to read and write the opposite, flip the soure and destination values");
		System.out.println("   java -jar d:\\bin\\generator-1.0.0-jar-with-dependencies.jar " + COMMAND_LINE_PARAM_CONFIG + "=d:\\genealogy\\configuration\\families.properties " + COMMAND_LINE_PARAM_FAMILY + "=jensen " + COMMAND_LINE_PARAM_ACTION + "=" + COMMAND_LINE_PARAM_ACTION_VALUE_VALIDATE + " " +  COMMAND_LINE_PARAM_LOG + "=" + COMMAND_LINE_PARAM_LOG_VALUE_FILE + " " + COMMAND_LINE_PARAM_LOG_FILE_FILENAME + "=d:\\temp\\genlog.txt");
		System.out.println("      to validate all lists and direct the output (validation results) to a specified log file");

		System.exit(1);
	}

	
}
