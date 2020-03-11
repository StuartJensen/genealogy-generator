package home.genealogy;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import home.genealogy.lists.PlaceList;
import home.genealogy.lists.ReferenceList;
import home.genealogy.lists.RelationshipManager;
import home.genealogy.lists.place.PlaceAction;
import home.genealogy.lists.place.PlaceActionException;
import home.genealogy.lists.place.PlaceActionScanner;
import home.genealogy.output.FileOutput;
import home.genealogy.output.IOutputStream;
import home.genealogy.output.StdOutOutput;
import home.genealogy.schema.all.BirthInfo;
import home.genealogy.schema.all.Info;
import home.genealogy.schema.all.Marriage;
import home.genealogy.schema.all.Parents;
import home.genealogy.schema.all.Person;
import home.genealogy.schema.all.Photo;
import home.genealogy.schema.all.Place;
import home.genealogy.schema.all.PlaceName;
import home.genealogy.schema.all.Reference;
import home.genealogy.schema.all.helpers.MarriageHelper;
import home.genealogy.schema.all.helpers.PersonHelper;
import home.genealogy.schema.all.helpers.PhotoHelper;
import home.genealogy.schema.all.helpers.PlaceHelper;
import home.genealogy.schema.all.helpers.PlaceNameHelper;
import home.genealogy.schema.all.helpers.ReferenceHelper;
import home.genealogy.util.StringUtil;

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
			boolean bFormattedOutput = commandLineParameters.isXmlFormatPretty();
			
			// Load all lists unless the action does not need ANY lists
			PersonList personList = null;
			PhotoList photoList = null;
			MarriageList marriageList = null;
			ReferenceList referenceList = null;
			PlaceList placeList = null;
			if (!strAction.equals(commandLineParameters.isActionValidate()))
			{
				outputStream.output("Initiating Lists Load:\n");
				placeList = new PlaceList(family, commandLineParameters);
				personList = new PersonList(family, commandLineParameters, outputStream);
				photoList = new PhotoList(family, commandLineParameters, outputStream);
				marriageList = new MarriageList(family, commandLineParameters, outputStream);
				marriageList.setInLineFlags(family, personList);
				referenceList = new ReferenceList(family, commandLineParameters, outputStream);
			}
			
			boolean bStorePlaceList = false;
			boolean bStorePersonList = false;
			boolean bStoreMarriageList = false;
			boolean bStoreReferenceList = false;
			boolean bStorePhotoList = false;
			
			if (strAction.equals(commandLineParameters.isActionValidate()))
			{
				GenealogyValidator validator = new GenealogyValidator(family, commandLineParameters, outputStream);
				validator.validate();
			}
			else if (commandLineParameters.isActionTransfer())
			{	// Set flags to transfer to destination.
				bStorePersonList = true;
				bStoreMarriageList = true;
				bStoreReferenceList = true;
				bStorePhotoList = true;
			}
			else if (commandLineParameters.isActionHtmlForm())
			{	// Set relationships in the personList
				IndexMarriageToSpouses idxMarrToSpouses = new IndexMarriageToSpouses(family, marriageList);
				IndexPersonToMarriages idxPerToMar = new IndexPersonToMarriages(family, marriageList);
				IndexMarriageToChildren idxMarToChild = new IndexMarriageToChildren(family, commandLineParameters, personList);
				RelationshipManager.setRelationships(family, personList, marriageList, idxPerToMar, idxMarToChild, outputStream);
				
				boolean bSuppressLiving = commandLineParameters.getSuppressLiving();
				
				if ((commandLineParameters.isHtmlFormTargetPersonList()) ||
					 commandLineParameters.isHtmlFormTargetAll())
				{
					HTMLPersonListForm form = new HTMLPersonListForm(family, placeList, personList, marriageList, bSuppressLiving, outputStream);
					form.create();
				}
				if ((commandLineParameters.isHtmlFormTargetPersonInfo()) ||
					 commandLineParameters.isHtmlFormTargetAll())
				{
					HTMLPersonInfoForm form = new HTMLPersonInfoForm(family, placeList, personList, marriageList, referenceList, photoList, bSuppressLiving, commandLineParameters, outputStream);
					form.create();
				}
				if ((commandLineParameters.isHtmlFormTargetReferenceList()) ||
					 commandLineParameters.isHtmlFormTargetAll())
				{
					HTMLReferenceListForm form = new HTMLReferenceListForm(family, placeList, personList,
						     											   marriageList, referenceList,
						     											   photoList, idxMarrToSpouses,
						     											   bSuppressLiving,
						     											   commandLineParameters, outputStream);
					form.create();
				}
				if ((commandLineParameters.isHtmlFormTargetPhotos()) ||
					 commandLineParameters.isHtmlFormTargetAll())
				{
					HTMLPhotoForm form = new HTMLPhotoForm(family, placeList, personList,
							   marriageList, referenceList,
							   photoList, idxMarrToSpouses,
							   bSuppressLiving,
							   commandLineParameters, outputStream);
					form.create();
				}
				if ((commandLineParameters.isHtmlFormTargetPhotoList()) ||
					 commandLineParameters.isHtmlFormTargetAll())
				{
					HTMLPhotoListForm form = new HTMLPhotoListForm(family, placeList, personList, marriageList, referenceList, photoList, idxMarrToSpouses, bSuppressLiving, commandLineParameters, outputStream);
					form.create();
				}
				if ((commandLineParameters.isHtmlFormTargetReferences()) ||
					 commandLineParameters.isHtmlFormTargetAll())
				{
					HTMLReferenceForm form = new HTMLReferenceForm(family, placeList, personList, marriageList, referenceList, photoList, bSuppressLiving, commandLineParameters, outputStream);
					form.create();
				}
				if ((commandLineParameters.isHtmlFormTargetFGS()) ||
					 commandLineParameters.isHtmlFormTargetAll())
				{
					HTMLFGSForm form = new HTMLFGSForm(family, placeList, personList, marriageList, referenceList, photoList, bSuppressLiving, commandLineParameters, outputStream);
					form.create();
				}
			}
			else if (commandLineParameters.isActionErrorCheck())
			{
				ErrorChecker errorChecker = new ErrorChecker(family, placeList, personList, marriageList, referenceList, photoList, commandLineParameters, outputStream);
				errorChecker.check();
			}
			else if (commandLineParameters.isActionPlaces())
			{
				if (commandLineParameters.isSubActionPlacesList())
				{
					Map<String, PlaceName> mPlaces = placeList.getPlaces();
					Iterator<String> iter = mPlaces.keySet().iterator();
					List<String> lPlaceNames = new ArrayList<String>();
					while (iter.hasNext())
					{
						String strKey = iter.next();
						lPlaceNames.add(PlaceNameHelper.getPlaceName(strKey, placeList));
					}
					Collections.sort(lPlaceNames);
					for (String strPlace : lPlaceNames)
					{
						String strId = PlaceNameHelper.getPlaceNameId(strPlace, placeList);
						outputStream.output(strPlace + " : (" + strId + ")\n");
					}					
				}
				else if (commandLineParameters.isSubActionPlacesValidate())
				{
					StringBuilder sb = new StringBuilder();
					placeList.validate(personList, marriageList,
										photoList, referenceList,
										sb);
					outputStream.output(sb.toString());
				}
				else if (commandLineParameters.isSubActionPlacesCommands())
				{
					String strInputFile = commandLineParameters.getInputFile();
					PlaceActionScanner scanner = new PlaceActionScanner(new File(strInputFile), outputStream);
					Iterator<PlaceAction> iterActions = scanner.getActions();
					while (iterActions.hasNext())
					{
						PlaceAction action = iterActions.next();
						action.execute(placeList, personList, marriageList, referenceList, photoList, outputStream);
					}
					if (commandLineParameters.getCommit())
					{
						bStorePlaceList = scanner.placeListModified();
						bStorePersonList = scanner.personListModified();
						bStorePhotoList = scanner.photoListModified();
						bStoreMarriageList = scanner.marriageListModified();
						bStoreReferenceList = scanner.referenceListModified();
					}
					else
					{
						outputStream.output("Place Action Command results NOT persisted. Set command line parameter \"commit\" to true for persist.\n");
					}
				}
			}
			else if (commandLineParameters.isActionConvert())
			{				
				Iterator<Person> iter = personList.getPersons();
				while (iter.hasNext())
				{
					Person p = iter.next();
/*					
					if ((null != p.getBurialInfo()) &&
						(StringUtil.exists(p.getBurialInfo().getCemeteryName())))
					{
						p.getBurialInfo().setCemetery(p.getBurialInfo().getCemeteryName());
						p.getBurialInfo().setCemeteryName(null);
					}
					if ((null != p.getBurialInfo()) &&
						(StringUtil.exists(p.getBurialInfo().getCemeteryPlotAddress())))
					{
						p.getBurialInfo().setCemeteryPlot(p.getBurialInfo().getCemeteryPlotAddress());
						p.getBurialInfo().setCemeteryPlotAddress(null);
					}
					List<Religion> lR = p.getReligion();
					for (Religion r : lR)
					{
						if (StringUtil.exists(r.getName()))
						{
							r.setContent(r.getName());
							r.setName(null);
						}
					}
					if (null != p.getProfessionXX())
					{
						List<String> lCXX = p.getProfessionXX();
						List<String> lC = p.getProfession();
						lC.addAll(lCXX);
						lCXX.clear();
					}
					
					if (StringUtil.exists(p.getParentId()))
					{
						int iParentId = 0;
						try
						{
							iParentId = Integer.parseInt(p.getParentId());
						}
						catch (NumberFormatException nfe) {nfe.printStackTrace();}
						List<Parents> lParents = p.getParents();
						Parents pnew = new Parents();
						pnew.setMarriageId(iParentId);
						lParents.add(pnew);
						p.setParentId(null);
					}
*/
				}
				personList.marshallAllFile(family, true);
				outputStream.output("Stored Person List to destination: " + CFGFamily.PERSONS_ALL_FILENAME + " file\n");
			}
			
			if (bStorePlaceList)
			{
				placeList.persist(family, bFormattedOutput, outputStream);
			}
			if (bStorePersonList)
			{
				personList.persist(family, commandLineParameters, bFormattedOutput, outputStream);
			}
			if (bStoreMarriageList)
			{
				 marriageList.persist(family, commandLineParameters, bFormattedOutput, outputStream);
			}
			if (bStoreReferenceList)
			{
				referenceList.persist(family, commandLineParameters, bFormattedOutput, outputStream);
			}
			if (bStorePhotoList)
			{
				photoList.persist(family, commandLineParameters, bFormattedOutput, outputStream);
			}
			if (null != outputStream)
			{
				outputStream.deinitialize();
			}
		}
		catch (UsageException ue)
		{
			CommandLineParameters.showUsage(ue.getMessage());
		}
		catch (PlaceActionException pae)
		{
			System.out.println("Place action exception: " + pae.getMessage());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		System.exit(1);
	}
/*	
	private static void addPlace(Info info, PlaceList placeList)
	{
		Place place = info.getPlace();
		if (null != place)
		{
			List<String> lPlaceParts = Place30To50.getPlaceIdRefParts(place);
			
			PlaceName exactMatch = Place30To50.getExactMatch(placeList, lPlaceParts);
			if (null != exactMatch)
			{
				place.setIdRef(exactMatch.getId());
				place.setCountry(null);
				place.setPlace1Level(null);
				return;
			}
			
			PlaceName parentMatch = Place30To50.getParent(placeList, lPlaceParts);
			if (null != parentMatch)
			{
				place.setIdRef(Place30To50.addPlaceXX(placeList, parentMatch, place));
				place.setCountry(null);
				place.setPlace1Level(null);
				return;
			}
		
			String strCountry = place.getCountry();
			
			PlaceName additionCountry = new PlaceName();
			additionCountry.setName(strCountry);
			additionCountry.setParentIdRef(null);
			additionCountry.setId(strCountry.replaceAll("\\s+", ""));
			placeList.put(additionCountry);
			
			place.setIdRef(Place30To50.addPlaceXX(placeList, additionCountry, place));
			place.setCountry(null);
			place.setPlace1Level(null);
		}
	}
*/	
}
