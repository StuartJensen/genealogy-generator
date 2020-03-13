package home.genealogy;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import home.genealogy.lists.PlaceList;
import home.genealogy.lists.place.PlaceAction;
import home.genealogy.lists.place.PlaceActionException;
import home.genealogy.lists.place.PlaceActionScanner;
import home.genealogy.output.IOutputStream;
import home.genealogy.schema.all.Person;
import home.genealogy.schema.all.PlaceName;
import home.genealogy.schema.all.helpers.PlaceHelper;
import home.genealogy.schema.all.helpers.PlaceNameComparator;
import home.genealogy.schema.all.helpers.PlaceNameHelper;

public class PlacesHandler
{
	private GenealogyContext m_context;
	
	public PlacesHandler(GenealogyContext context)
	{
		m_context = context;
	}
	
	public void execute()
		throws PlaceActionException, IOException
	{
		if (m_context.getCommandLineParameters().isSubActionPlacesList())
		{
			Map<String, PlaceName> mPlaces = m_context.getPlaceList().getPlaces();
			Iterator<String> iter = mPlaces.keySet().iterator();
			List<PlaceName> lPlaceNames = new ArrayList<PlaceName>();
			while (iter.hasNext())
			{
				String strKey = iter.next();
				lPlaceNames.add(mPlaces.get(strKey));
			}
			Collections.sort(lPlaceNames, new PlaceNameComparator(m_context.getPlaceList()));
			for (PlaceName placeName : lPlaceNames)
			{
				String strPlaceName = PlaceNameHelper.getPlaceName(placeName.getId(), m_context.getPlaceList());
				m_context.output(strPlaceName + " : (" + placeName.getId() + ")\n");
			}					
		}
		else if (m_context.getCommandLineParameters().isSubActionPlacesValidate())
		{
			StringBuilder sb = new StringBuilder();
			m_context.getPlaceList().validate(m_context, sb);
			m_context.output(sb.toString());
		}
		else if (m_context.getCommandLineParameters().isSubActionPlacesDuplicates())
		{
			List<List<PlaceName>> lDuplicates = PlaceHelper.findAllDuplicates(m_context.getPlaceList());
			for (List<PlaceName> lPlaceNames : lDuplicates)
			{
				m_context.output("Duplicate Set:\n");
				for (PlaceName placeName : lPlaceNames)
				{
					String strPlaceName = PlaceNameHelper.getPlaceName(placeName.getId(), m_context.getPlaceList());
					m_context.output(strPlaceName + " : (" + placeName.getId() + ")\n");
				}
			}
			if (lDuplicates.isEmpty())
			{
				m_context.output("Zero Duplicates Found!\n");
			}
		}
		else if (m_context.getCommandLineParameters().isSubActionPlacesClean())
		{
			StringBuilder sb = new StringBuilder();
			if (m_context.getPlaceList().validate(m_context, sb))
			{
				m_context.output("PRE_PROCESS: Aborting place list clean due to existing errors: " + sb.toString() + "\n");
			}
			else
			{
/*				
				List<List<PlaceName>> lDuplicates = PlaceHelper.findAllDuplicates(m_context.getPlaceList());
				for (List<PlaceName> lPlaceNames : lDuplicates)
				{
					m_context.output("Duplicate Set:\n");
					for (PlaceName placeName : lPlaceNames)
					{
						String strPlaceName = PlaceNameHelper.getPlaceName(placeName.getId(), m_context.getPlaceList());
						m_context.output(strPlaceName + " : (" + placeName.getId() + ")\n");
					}
				}
*/				
				boolean bCleaning = true;
				while (bCleaning)
				{
					List<PlaceName> lDuplicates = PlaceHelper.findFirstDuplicates(m_context.getPlaceList());
					if (!lDuplicates.isEmpty())
					{
						for (PlaceName pn : lDuplicates)
						{
							System.out.println("Duplicate: " + PlaceNameHelper.getPlaceName(pn.getId(), m_context.getPlaceList()));
							System.out.println();
							
							PlaceName placeToLiveOn = lDuplicates.get(0);
							for (int i=1; i<lDuplicates.size(); i++)
							{
								PlaceName placeToDie = lDuplicates.get(i);
								if (0 != m_context.getPersonList().replacePlaceId(
										placeToDie.getId(),
										placeToLiveOn.getId(),
										m_context.getOutputStream()))
								{
									m_context.setStorePersonList();
								}
								if (0 != m_context.getMarriageList().replacePlaceId(
										placeToDie.getId(),
										placeToLiveOn.getId(),
										m_context.getOutputStream()))
								{
									m_context.setStoreMarriageList();
								}
								if (0 != m_context.getPhotoList().replacePlaceId(
										placeToDie.getId(),
										placeToLiveOn.getId(),
										m_context.getOutputStream()))
								{
									m_context.setStorePhotoList();
								}
								if (0 != m_context.getReferenceList().replacePlaceId(
										placeToDie.getId(),
										placeToLiveOn.getId(),
										m_context.getOutputStream()))
								{
									m_context.setStoreReferenceList();
								}
								if (m_context.getPlaceList().replacePlaceId(
										placeToDie.getId(),
										placeToLiveOn.getId(),
										m_context.getOutputStream(),
										true))
								{
									m_context.setStorePlaceList();
								}
							}
						}
					}
					else
					{
						bCleaning = false;
					}
				}
				
				sb = new StringBuilder();
				if (m_context.getPlaceList().validate(m_context, sb))
				{
					m_context.output("POST-PROCESS: Aborting place list clean due to caused errors: " + sb.toString() + "\n");
					m_context.setStoreAllLists(false);
				}
			}
		}
		else if (m_context.getCommandLineParameters().isSubActionPlacesCommands())
		{
			String strInputFile = m_context.getCommandLineParameters().getInputFile();
			PlaceActionScanner scanner = new PlaceActionScanner(new File(strInputFile), m_context.getOutputStream());
			Iterator<PlaceAction> iterActions = scanner.getActions();
			while (iterActions.hasNext())
			{
				PlaceAction action = iterActions.next();
				action.execute(m_context);
			}
			if (m_context.getCommandLineParameters().getCommit())
			{
				if (scanner.placeListModified())
				{
					m_context.setStorePlaceList();
				}
				if (scanner.personListModified())
				{
					m_context.setStorePersonList();
				}
				if (scanner.photoListModified())
				{
					m_context.setStorePhotoList();
				}
				if (scanner.marriageListModified())
				{
					m_context.setStoreMarriageList();
				}
				if (scanner.referenceListModified())
				{
					m_context.setStoreReferenceList();
				}
			}
			else
			{
				m_context.output("Place Action Command results NOT persisted. Set command line parameter \"commit\" to true for persist.\n");
			}
		}
	}
}
