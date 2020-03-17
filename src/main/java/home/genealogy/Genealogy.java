package home.genealogy;

import java.security.InvalidParameterException;

import javax.xml.bind.JAXBException;

import org.xml.sax.SAXException;

import home.genealogy.action.errorcheck.ErrorChecker;
import home.genealogy.action.validate.GenealogyValidator;
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
import home.genealogy.lists.RelationshipManager;
import home.genealogy.lists.place.PlaceActionException;

public class Genealogy
{
	public static void main(String[] args)
	{
		GenealogyContext context = null;
		
		try
		{
			context = new GenealogyContext(args);
			
			if (context.getCommandLineParameters().isActionValidate())
			{
				GenealogyValidator validator = new GenealogyValidator(context);
				validator.validate();
			}
			else if (context.getCommandLineParameters().isActionTransfer())
			{	// Set flags to transfer to destination.
				context.setStorePersonList();
				context.setStoreMarriageList();
				context.setStoreReferenceList();
				context.setStorePhotoList();
			}
			else if (context.getCommandLineParameters().isActionHtmlForm())
			{
				HtmlFormsHandler handler = new HtmlFormsHandler(context);
				handler.execute();
			}
			else if (context.getCommandLineParameters().isActionErrorCheck())
			{
				ErrorChecker errorChecker = new ErrorChecker(context);
				errorChecker.check();
			}
			else if (context.getCommandLineParameters().isActionPlaces())
			{
				PlacesHandler handler = new PlacesHandler(context);
				handler.execute();
			}
			else if (context.getCommandLineParameters().isActionConvert())
			{
/*				
				Iterator<Person> iter = personList.getPersons();
				while (iter.hasNext())
				{
					Person p = iter.next();
					
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
				}
				personList.marshallAllFile(family, true);
				outputStream.output("Stored Person List to destination: " + CFGFamily.PERSONS_ALL_FILENAME + " file\n");
*/
			}
		}
		catch (InvalidParameterException ipe)
		{
			System.out.println("An invalid construct was encountered: " + ipe.getMessage());
			if (null != context)
			{
				context.setException(ipe);
			}
		}
		catch (UsageException ue)
		{
			CommandLineParameters.showUsage(ue.getMessage());
			if (null != context)
			{
				context.setException(ue);
			}
		}
		catch (SAXException sxe)
		{
			System.out.println("Exception instantiating marshalling agent: " + sxe.getMessage());
			if (null != context)
			{
				context.setException(sxe);
			}
		}
		catch (JAXBException jbe)
		{
			System.out.println("Exception marshalling XML file: " + jbe.getMessage());
			if (null != context)
			{
				context.setException(jbe);
			}
		}
		catch (PlaceActionException pae)
		{
			System.out.println("Place action exception: " + pae.getMessage());
			if (null != context)
			{
				context.setException(pae);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			if (null != context)
			{
				context.setException(e);
			}
		}
		finally
		{
			if (null != context)
			{
				try
				{
					context.destroy();
				}
				catch(Exception e)
				{
					System.out.println("Exception destroying context: " + e.toString());
				}
			}
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
