package home.genealogy.lists;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.xml.sax.SAXException;

import home.genealogy.CommandLineParameters;
import home.genealogy.configuration.CFGFamily;
import home.genealogy.conversions.Place30To50;
import home.genealogy.schema.all.PlaceName;
import home.genealogy.schema.all.PlaceNames;
import home.genealogy.util.MarshallUtil;
import home.genealogy.util.StringUtil;

public class PlaceList
{
	private Map<String, PlaceName> m_mPlaceNames;
	
	public PlaceList()
	{
		m_mPlaceNames = new HashMap<String, PlaceName>();
	}
	
	public PlaceList(CFGFamily family, CommandLineParameters commandLineParameters)
		throws Exception
	{
		unMarshallAllFile(family);
	}
	
	public Map<String, PlaceName> getPlaces()
	{
		if (null != m_mPlaceNames)
		{
			return Collections.unmodifiableMap(m_mPlaceNames);
		}
		return Collections.emptyMap();
	}
	
	public Map<String, PlaceName> getPlacesDeleteMe()
	{
		return m_mPlaceNames;
	}
	
	public void setMapDeleteMe(Map<String, PlaceName> replacementPlaceList)
	{
		m_mPlaceNames = replacementPlaceList;
	}
	
	public PlaceName get(String strPlaceId)
	{
		if (null != m_mPlaceNames)
		{
			return m_mPlaceNames.get(strPlaceId);
		}
		return null;
	}
	
	public void put(PlaceName place)
	{
		if (null != m_mPlaceNames)
		{
			m_mPlaceNames.put(place.getId(), place);
		}
	}
	
	public int size()
	{
		if (null != m_mPlaceNames)
		{
			return m_mPlaceNames.size();
		}
		return 0;
	}
	
	public void unMarshallAllFile(CFGFamily family)
		throws Exception
	{
		String strDataPath = family.getDataPathSlashTerminated();
		
		m_mPlaceNames = new HashMap<String, PlaceName>();
		
		// Setup JAXB unmarshaller
		Unmarshaller unmarshaller = MarshallUtil.createUnMarshaller(family.getSchemaFile());
		
		String strFileName = strDataPath + CFGFamily.PLACES_ALL_FILENAME;

		File fAllFile = new File(strFileName);
		if (fAllFile.exists())
		{
			PlaceNames places = (PlaceNames)unmarshaller.unmarshal(fAllFile);
			List<PlaceName> lPlaces = places.getPlaceName();
			for (PlaceName place : lPlaces)
			{
				String strPlaceId = place.getId();
				if (!StringUtil.exists(strPlaceId))
				{
					throw new Exception("Place has missing place id: " + place.toString());
				}
				m_mPlaceNames.put(strPlaceId, place);
			}
		}
		else
		{
			System.out.println("WARNING: Places ALL file not found: " + strFileName);
		}
	}
	
	public void marshallAllFile(CFGFamily family, boolean bFormattedOutput)
	{
		Marshaller marshaller = null;
		try
		{
			marshaller = MarshallUtil.createMarshaller(family.getSchemaFile(), bFormattedOutput);
		}
		catch (JAXBException jb)
		{
			System.out.println("Exception creating JAXB Marshaller: " + jb.toString());
			return;
		}
		catch (SAXException se)
		{
			System.out.println("Exception creating JAXB Marshaller: " + se.toString());
			return;
		}
		
		// Build schema "Persons" instance
		PlaceNames places = new PlaceNames();
		
		List<PlaceName> lPlaces = places.getPlaceName();

		Map<String, PlaceName> mPlaces = this.getPlaces();
		if (null != mPlaces)
		{
			// Get a sorted list of place ids
			List<String> lToSort = new ArrayList<String>();
			Iterator<String> iter = mPlaces.keySet().iterator();
			while (iter.hasNext())
			{
				lToSort.add(iter.next());
			}
			Collections.sort(lToSort);
			// Write sorted places
			for (String strKey : lToSort)
			{
				lPlaces.add(mPlaces.get(strKey));
			}
		}
		
		String strDataPath = family.getDataPathSlashTerminated();
		String strDirectory = strDataPath + CFGFamily.PLACES_ALL_FILENAME;
		MarshallUtil.marshall(marshaller, places, strDirectory);
	}
}
