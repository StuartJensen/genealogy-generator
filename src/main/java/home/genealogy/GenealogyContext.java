package home.genealogy;

import java.io.File;
import java.io.IOException;
import java.security.InvalidParameterException;

import javax.xml.bind.JAXBException;

import org.xml.sax.SAXException;

import home.genealogy.configuration.CFGFamily;
import home.genealogy.configuration.CFGFamilyList;
import home.genealogy.lists.MarriageList;
import home.genealogy.lists.PersonList;
import home.genealogy.lists.PhotoList;
import home.genealogy.lists.PlaceList;
import home.genealogy.lists.ReferenceList;
import home.genealogy.output.FileOutput;
import home.genealogy.output.IOutputStream;
import home.genealogy.output.StdOutOutput;

public class GenealogyContext
{
	private PersonList m_personList = null;
	private PhotoList m_photoList = null;
	private MarriageList m_marriageList = null;
	private ReferenceList m_referenceList = null;
	private PlaceList m_placeList = null;

	private CFGFamily m_family;
	private CommandLineParameters m_commandLineParameters;
	
	private IOutputStream m_outputStream;
	
	private String m_strAction;
	private boolean m_bFormattedOutput = false;
	private boolean m_bSuppressLiving = true;
	
	private boolean m_bStorePlaceList = false;
	private boolean m_bStorePersonList = false;
	private boolean m_bStoreMarriageList = false;
	private boolean m_bStoreReferenceList = false;
	private boolean m_bStorePhotoList = false;
	
	private Exception m_exception;
	
	public GenealogyContext(String[] args)
		throws IOException, UsageException,
				InvalidParameterException, JAXBException,
				SAXException
	{
		m_commandLineParameters = new CommandLineParameters(args);
		
		// Initialize logging output stream
		m_outputStream = new StdOutOutput();
		String strLogType = m_commandLineParameters.getLogType();
		if (strLogType.equals(CommandLineParameters.COMMAND_LINE_PARAM_LOG_VALUE_FILE))
		{
			m_outputStream = new FileOutput(m_commandLineParameters.getLogFilename(),
											m_commandLineParameters.getLogFileEcho());
		}
		m_outputStream.initialize();
		
		// Initialize Family
		CFGFamilyList listFamily = new CFGFamilyList(new File(m_commandLineParameters.getConfigurationFilename()));
		m_family = listFamily.getFamily(m_commandLineParameters.getFamilyName());
		if (null == m_family)
		{
			throw new UsageException("Specified family name: \"" + m_commandLineParameters.getFamilyName() + "\" not found in configuration file: \"" + m_commandLineParameters.getConfigurationFilename() + "\"");
		}
		
		m_strAction = m_commandLineParameters.getAction();
		m_bFormattedOutput = m_commandLineParameters.isXmlFormatPretty();
		m_bSuppressLiving = m_commandLineParameters.getSuppressLiving();
		
		// Load all lists unless the action does not need ANY lists
		if (!m_strAction.equals(m_commandLineParameters.isActionValidate()))
		{
			m_outputStream.output("Initiating Lists Load:\n");
			m_placeList = new PlaceList(m_family, m_commandLineParameters);
			m_personList = new PersonList(m_family, m_commandLineParameters, m_outputStream);
			m_photoList = new PhotoList(m_family, m_commandLineParameters, m_outputStream);
			m_marriageList = new MarriageList(m_family, m_commandLineParameters, m_outputStream);
			m_marriageList.setInLineFlags(m_family, m_personList);
			m_referenceList = new ReferenceList(m_family, m_commandLineParameters, m_outputStream);
		}
	}
	
	public void destroy()
		throws Exception
	{
		if (getStorePlaceList())
		{
			m_placeList.persist(this);
		}
		if (getStorePersonList())
		{
			m_personList.persist(this);
		}
		if (getStoreMarriageList())
		{
			m_marriageList.persist(this);
		}
		if (getStoreReferenceList())
		{
			m_referenceList.persist(this);
		}
		if (getStorePhotoList())
		{
			m_photoList.persist(this);
		}
		if (null != m_outputStream)
		{
			m_outputStream.deinitialize();
		}
	}
	
	public CFGFamily getFamily()
	{
		return m_family;
	}
	
	public PersonList getPersonList()
	{
		return m_personList;
	}
	
	public PhotoList getPhotoList()
	{
		return m_photoList;
	}
	
	public MarriageList getMarriageList()
	{
		return m_marriageList;
	}
	
	public ReferenceList getReferenceList()
	{
		return m_referenceList;
	}
	
	public PlaceList getPlaceList()
	{
		return m_placeList;
	}
	
	public CommandLineParameters getCommandLineParameters()
	{
		return m_commandLineParameters;
	}
	
	public IOutputStream getOutputStream()
	{
		return m_outputStream;
	}
	
	public void output(String strMessage)
	{
		m_outputStream.output(strMessage);
	}
	
	public String getAction()
	{
		return m_strAction;
	}
	
	public boolean getFormattedOutput()
	{
		return m_bFormattedOutput;
	}
	
	public boolean getSuppressLiving()
	{
		return m_bSuppressLiving;
	}
	
	public void setStorePlaceList()
	{
		m_bStorePlaceList = true;
	}
	
	public boolean getStorePlaceList()
	{
		return m_bStorePlaceList;
	}
	
	public void setStoreAllLists(boolean b)
	{
		m_bStorePlaceList = b;
		m_bStorePersonList = b;
		m_bStoreMarriageList = b;
		m_bStoreReferenceList = b;
		m_bStorePhotoList = b;
	}
	
	public void setStorePersonList()
	{
		m_bStorePersonList = true;
	}
	
	public boolean getStorePersonList()
	{
		return m_bStorePersonList;
	}
	
	public void setStoreMarriageList()
	{
		m_bStoreMarriageList = true;
	}
	
	public boolean getStoreMarriageList()
	{
		return m_bStoreMarriageList;
	}
	
	public void setStoreReferenceList()
	{
		m_bStoreReferenceList = true;
	}
	
	public boolean getStoreReferenceList()
	{
		return m_bStoreReferenceList;
	}
	
	public void setStorePhotoList()
	{
		m_bStorePhotoList = true;
	}
	
	public boolean getStorePhotoList()
	{
		return m_bStorePhotoList;
	}
	
	public void setException(Exception exception)
	{
		m_exception = exception;
		m_bStorePlaceList = false;
		m_bStorePersonList = false;
		m_bStoreMarriageList = false;
		m_bStoreReferenceList = false;
		m_bStorePhotoList = false;
	}
	
}
