package home.genealogy.lists;

import java.io.File;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.xml.sax.SAXException;

import home.genealogy.CommandLineParameters;
import home.genealogy.GenealogyContext;
import home.genealogy.configuration.CFGFamily;
import home.genealogy.conversions.Place30To50;
import home.genealogy.output.IOutputStream;
import home.genealogy.schema.all.Marriage;
import home.genealogy.schema.all.Person;
import home.genealogy.schema.all.Photo;
import home.genealogy.schema.all.PlaceName;
import home.genealogy.schema.all.PlaceNames;
import home.genealogy.schema.all.Reference;
import home.genealogy.schema.all.helpers.MarriageHelper;
import home.genealogy.schema.all.helpers.PersonHelper;
import home.genealogy.schema.all.helpers.PhotoHelper;
import home.genealogy.schema.all.helpers.PlaceHelper;
import home.genealogy.schema.all.helpers.ReferenceHelper;
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
		throws InvalidParameterException, JAXBException, SAXException
	{
		unMarshallAllFile(family);
	}
	
	public void persist(GenealogyContext context)
		throws Exception
	{
		marshallAllFile(context.getFamily(), context.getFormattedOutput());
		context.output("Place List: Count: " + size() + ": Persisted to ALL XML file.\n");
	}
	
	public PlaceName remove(String strPlaceId)
	{
		if (null != m_mPlaceNames)
		{
			return m_mPlaceNames.remove(strPlaceId);
		}
		return null;
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
		if ((null != strPlaceId) && (null != m_mPlaceNames))
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
		throws InvalidParameterException, JAXBException, SAXException
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
					throw new InvalidParameterException("Place has missing place id: " + place.toString());
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
	
	public Set<String> getAllParentIds(Set<String> sChildPlaceIds)
	{
		Set<String> sParentIds = new HashSet<String>();
		Iterator<String> iterChildIds = sChildPlaceIds.iterator();
		while (iterChildIds.hasNext())
		{
			String strChildId = iterChildIds.next();
			sParentIds.addAll(getAllParentIds(strChildId));
		}
		return sParentIds;
	}

	public Set<String> getIdsOfAllPeers(String strPeerId)
	{
		Set<String> sPeerIds = new HashSet<String>();
		if ((null != strPeerId) && (null != m_mPlaceNames))
		{
			Iterator<String> iter = m_mPlaceNames.keySet().iterator();
			while (iter.hasNext())
			{
				String strKey = iter.next();
				PlaceName current = m_mPlaceNames.get(strKey);
				if (StringUtil.exists(current.getPeerId()))
				{
					if (strPeerId.equals(current.getPeerId()))
					{
						sPeerIds.add(strKey);
					}
				}
			}
		}
		return sPeerIds;
	}
	
	public Set<String> getAllParentIds(String strChildPlaceId)
	{
		Set<String> sParentIds = new HashSet<String>();
		PlaceName child = get(strChildPlaceId);
		while (null != child)
		{
			if (StringUtil.exists(child.getParentIdRef()))
			{
				sParentIds.add(child.getParentIdRef());
			}
			child = get(child.getParentIdRef());
		}
		return sParentIds;
	}
	
	public boolean validate(GenealogyContext context,
							StringBuilder sb)
	{
		boolean bError = false;
		PersonList personList = context.getPersonList();
		MarriageList marriageList = context.getMarriageList();
		PhotoList photoList = context.getPhotoList();
		ReferenceList referenceList = context.getReferenceList();
		Map<String, PlaceName> mPlaces = getPlaces();
		
		Iterator<String> iterPlaces = mPlaces.keySet().iterator();
		while (iterPlaces.hasNext())
		{
			String strPlaceId = iterPlaces.next();
			PlaceName place = mPlaces.get(strPlaceId);
			if (!place.getId().equals(strPlaceId))
			{
				sb.append("WARNING: Place list map links key " + strPlaceId + " to incorrect Place with id " + place.getId() + ".\n");
				bError = true;
			}
			
			if (StringUtil.exists(place.getParentIdRef()))
			{
				PlaceName parent = mPlaces.get(place.getParentIdRef());
				if (null == parent)
				{
					sb.append("WARNING: Place list parent reference id " + place.getParentIdRef() + " in Place with id " + place.getId() + " does not exist.\n");
					bError = true;
				}
			}
		}
		Set<String> allUsedIds = new HashSet<String>();
		// Add Person's Used Place Ids
		Iterator<Person> iterPersons = personList.getPersons();
		while (iterPersons.hasNext())
		{
			Person candidate = iterPersons.next();
			allUsedIds.addAll(PersonHelper.getAllPlaceIds(candidate));
		}
		// Add Marriage's Used Place Ids
		Iterator<Marriage> iterMarriages = marriageList.getMarriages();
		while (iterMarriages.hasNext())
		{
			Marriage candidate = iterMarriages.next();
			allUsedIds.addAll(MarriageHelper.getAllPlaceIds(candidate));
		}
		// Add References's Used Place Ids
		Iterator<Reference> iterReferences = referenceList.getReferences();
		while (iterReferences.hasNext())
		{
			Reference candidate = iterReferences.next();
			allUsedIds.addAll(ReferenceHelper.getAllPlaceIds(candidate));
		}
		// Add Photo's Used Place Ids
		Iterator<Photo> iterPhotos = photoList.getPhotos();
		while (iterPhotos.hasNext())
		{
			Photo candidate = iterPhotos.next();
			allUsedIds.addAll(PhotoHelper.getAllPlaceIds(candidate));
		}
		// Gather all parental ids of all used place names
		allUsedIds.addAll(getAllParentIds(allUsedIds));
		// Gather all "peer" placename ids of "used" placenames
		Set<String> sPeerIds = new HashSet<String>();
		for (String strUsedId : allUsedIds)
		{
			PlaceName candidate = get(strUsedId);
			if (StringUtil.exists(candidate.getPeerId()))
			{
				sPeerIds.addAll(getIdsOfAllPeers(candidate.getPeerId()));
			}
		}
		allUsedIds.addAll(sPeerIds);
		// Show details about the "found" places
		sb.append("Found " + allUsedIds.size() + " Used Places\n");
		sb.append("Place List Contains " + size() + " Places\n");
		// Now look for any places in the placeList that are not
		// in the used list.
		Iterator<String> iter = mPlaces.keySet().iterator();
		while (iter.hasNext())
		{
			String strKey = iter.next();
			if (!allUsedIds.contains(strKey))
			{
				sb.append("WARNING: Unused place id: " + strKey + "\n");
				bError = true;
			}
		}
		// Now look for any places that are NOT in the placeList but that are
		// in the used list. (abandoned place ids)
		for (String strUsedId : allUsedIds)
		{
			PlaceName existingPlace = get(strUsedId);
			if (null == existingPlace)
			{
				sb.append("WARNING: Undefined place id: " + strUsedId + "\n");
				bError = true;
			}
		}
		return bError;
	}
	
	public boolean replacePlaceId(String strIdToBeReplaced,
								String strIdReplacement,
								IOutputStream outputStream,
								boolean bDeleteToBeReplaced)
	{
		boolean bListModified = false;
		if (null != m_mPlaceNames)
		{
			Iterator<String> iter = m_mPlaceNames.keySet().iterator();
			while (iter.hasNext())
			{
				String strKey = iter.next();
				PlaceName current = m_mPlaceNames.get(strKey);
				if (StringUtil.exists(current.getParentIdRef()))
				{
					if (current.getParentIdRef().equals(strIdToBeReplaced))
					{
						outputStream.output("  PlaceList: Place Id Replace: Place Id: " + current.getId() + ", Replacing Parent Ref Id: " + current.getParentIdRef() + " with " + strIdReplacement + "\n");
						current.setParentIdRef(strIdReplacement);
						bListModified = true;
					}
				}
			}
			if (bDeleteToBeReplaced)
			{
				outputStream.output("  PlaceList: Place Id Replace: Removed Place Id: " + strIdToBeReplaced + "\n");
				remove(strIdToBeReplaced);
				bListModified = true;
			}
		}
		return bListModified;
	}

}
