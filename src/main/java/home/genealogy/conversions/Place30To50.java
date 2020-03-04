package home.genealogy.conversions;

public class Place30To50
{
/*	
	public static List<String> getPlaceIdRefParts(Place place)
	{
		List<String> lResult = new ArrayList<String>();
		String strCountry = place.getCountry();
		lResult.add(strCountry.replaceAll("\\s+", ""));

		Place1Level oneLevel = place.getPlace1Level();
		if (null != oneLevel)
		{
			String strOneLevelName = oneLevel.getName();
			lResult.add(strOneLevelName.replaceAll("\\s+", ""));
			Place2Level twoLevel = oneLevel.getPlace2Level();
			if (null != twoLevel)
			{
				String strTwoLevelName = twoLevel.getName();
				lResult.add(strTwoLevelName.replaceAll("\\s+", ""));
				Place3Level threeLevel = twoLevel.getPlace3Level();
				if (null != threeLevel)
				{
					String strThreeLevelName = threeLevel.getName();
					lResult.add(strThreeLevelName.replaceAll("\\s+", ""));
					Place4Level fourLevel = threeLevel.getPlace4Level();
					if (null != fourLevel)
					{
						String strFourLevelName = fourLevel.getName();
						lResult.add(strFourLevelName.replaceAll("\\s+", ""));
					}
				}
			}
		}
		return lResult;
	}
	
	public static String getPlaceIdRef(List<String> lPlaceParts, int iParts)
	{
		String strId = "";
		int iPartCount = 0;
		for (String strPart : lPlaceParts)
		{
			if (0 != strId.length())
			{
				strId += ".";
			}
			strId += strPart;
			iPartCount++;
			if (iPartCount == iParts)
			{
				break;
			}
		}
		return strId;
	}
	
	public static String getPlaceIdRef(List<String> lPlaceParts)
	{
		String strId = "";
		for (String strPart : lPlaceParts)
		{
			if (0 != strId.length())
			{
				strId += ".";
			}
			strId += strPart;
		}
		return strId;
	}
	
	public static PlaceName getExactMatch(PlaceList placeList, List<String> lPlaceParts)
	{
		return placeList.get(Place30To50.getPlaceIdRef(lPlaceParts));
	}
	
	public static PlaceName getParent(PlaceList placeList, List<String> lPlaceParts)
	{
		PlaceName lastParentPlace = null;
		for (int i=1; i<=lPlaceParts.size(); i++)
		{
			String strPlaceId = getPlaceIdRef(lPlaceParts, i);
			PlaceName resultantPlace = placeList.get(strPlaceId);
			if (null != resultantPlace)
			{
				lastParentPlace = resultantPlace;
			}
		}
		return lastParentPlace;
	}
	
	public static String addPlaceXX(PlaceList placeList, PlaceName parent, Place child)
	{
		List<String> lChildParts = getPlaceIdRefParts(child);
		int iParentLevel = getPlaceXXLevel(placeList, parent);
		String strParentRefId = parent.getId();
		for (int i=0; i<lChildParts.size(); i++)
		{
			if (i >= iParentLevel)
			{
				String strLevelName = "";
				if (1 == i)
				{
					strLevelName = child.getPlace1Level().getName();
				}
				else if (2 == i)
				{
					strLevelName = child.getPlace1Level().getPlace2Level().getName();
				}
				else if (3 == i)
				{
					strLevelName = child.getPlace1Level().getPlace2Level().getPlace3Level().getName();
				}
				else if (4 == i)
				{
					strLevelName = child.getPlace1Level().getPlace2Level().getPlace3Level().getPlace4Level().getName();
				}
				PlaceName addition = new PlaceName();
				addition.setName(strLevelName);
				addition.setParentIdRef(strParentRefId);
				strParentRefId = strParentRefId + "." + strLevelName.replaceAll("\\s+", "");
				addition.setId(strParentRefId);
				placeList.put(addition);
			}
		}
		return strParentRefId;
	}
	
	public static int getPlaceXXLevel(PlaceList placeList, PlaceName target)
	{
		int iLevel = 1;
		String strParentIdRef = target.getParentIdRef();
		while (null != strParentIdRef)
		{
			iLevel++;
			PlaceName parent = placeList.get(strParentIdRef);
			if (null != parent)
			{
				strParentIdRef = parent.getParentIdRef();
			}
		}
		return iLevel;
	}

	public static Map<String, PlaceName> abbreviate(PlaceList placeList, PersonList personList,
								  MarriageList marriageList, ReferenceList referenceList,
								  PhotoList photoList)
	{
		 Map<String, PlaceName> mReplacement = new HashMap<String, PlaceName>();
		 Map<String, String> mAbbrToId = new HashMap<String, String>();
		 Map<String, PlaceName> mPlaces = placeList.getPlacesDeleteMe();
		 Iterator<String> iterPlaces = mPlaces.keySet().iterator(); 
		 while (iterPlaces.hasNext())
		 {
			 String strCurrentPlaceId = iterPlaces.next();
			 PlaceName currentPlace = placeList.get(strCurrentPlaceId);
			 // Determine Abbreviation
			 String strAbbreviation = "";
			 
			 // If USA
			 if ("USA".equals(strCurrentPlaceId))
			 {
				 strAbbreviation = "US";
			 }
			 else if ("UNKNOWN".equals(strCurrentPlaceId))
			 {
				 strAbbreviation = "UN";
			 }
			 else
			 {
			     StringTokenizer st = new StringTokenizer(strCurrentPlaceId, ".", false);
			     while (st.hasMoreTokens())
			     {
			    	 String strThisPartsAbbr = "";
			    	 String strPart = st.nextToken();
			    	 List<Integer> lUpperIndexes = new ArrayList<Integer>();
			    	 for (int i=0; i<strPart.length(); i++)
			    	 {
			    		 char ch = strPart.charAt(i);
			    		 if (Character.isUpperCase(ch))
			    		 {
			    			 lUpperIndexes.add(i);
			    		 }
			    	 }
			    	 if ((lUpperIndexes.size() >= 2) && 
			    		 (lUpperIndexes.size() < strPart.length()))
			    	 {
			    		 for (Integer index : lUpperIndexes)
			    		 {
			    			 strThisPartsAbbr += strPart.charAt(index.intValue());
			    			 if (strPart.length() > (index.intValue() + 1))
			    			 {
			    				 strThisPartsAbbr += strPart.charAt(index.intValue() + 1);
			    			 }
			    		 }
			    	 }
			    	 else
			    	 {	// Just take the first two letters of this part
				    	 for (int i=0; i<strPart.length(); i++)
				    	 {
				    		 strThisPartsAbbr += strPart.charAt(i);
				    		 if (2 <= strThisPartsAbbr.length())
				    		 {
				    			 break;
				    		 }
				    	 }
			    	 }
			    	 if (0 == strThisPartsAbbr.length())
			    	 {
			    		 System.out.println("WARNING: Zero length abberviation!!");
			    		 strThisPartsAbbr = strPart;
			    	 }
			    	 strAbbreviation += strThisPartsAbbr;
			     }
			 }
		     // Ensure the abbreviation is unique
		     String strMapped = mAbbrToId.get(strAbbreviation);
		     String strAbbreviationFinalized = strAbbreviation;
		     int iCounter = 1;
		     while (null != strMapped)
		     {
		    	 strAbbreviationFinalized = strAbbreviation + iCounter;
		    	 iCounter++;
		    	 strMapped = mAbbrToId.get(strAbbreviationFinalized);
		     }
		     
		     mAbbrToId.put(strAbbreviationFinalized, strCurrentPlaceId);
		     
		     // Change all parent ref ids to this one
			 Iterator<String> iterPlacesRefIds = mPlaces.keySet().iterator(); 
			 while (iterPlacesRefIds.hasNext())
			 {
				 String strCurrentPlaceIdRefId = iterPlacesRefIds.next();
				 PlaceName currentPlaceRefId = placeList.get(strCurrentPlaceIdRefId);
				 String strParentRef = currentPlaceRefId.getParentIdRef();
				 if (StringUtil.exists(strParentRef))
				 {
					 if (strParentRef.equals(strCurrentPlaceId))
					 {
						 currentPlaceRefId.setParentIdRef(strAbbreviationFinalized);
					 }
				 }
			 }
			 // Change this placename itself to a new abbreviatec id
		     currentPlace.setId(strAbbreviationFinalized);
		     // Put in new replacement map
		     mReplacement.put(strAbbreviationFinalized, currentPlace);
		     
		     // Fixup Person List
		     Iterator<Person> iterPerson = personList.getPersons();
		     while (iterPerson.hasNext())
		     {
		    	 Person person = iterPerson.next();
		    	 if (null != person.getBirthInfo() &&
				    null != person.getBirthInfo().getInfo() &&
				    null != person.getBirthInfo().getInfo().getPlace() &&
		    		person.getBirthInfo().getInfo().getPlace().getIdRef().equals(strCurrentPlaceId))
				{
				    person.getBirthInfo().getInfo().getPlace().setIdRef(strAbbreviationFinalized);
				}
				if (null != person.getChrInfo() &&
				    null != person.getChrInfo().getInfo() &&
				    null != person.getChrInfo().getInfo().getPlace() &&
		    		person.getChrInfo().getInfo().getPlace().getIdRef().equals(strCurrentPlaceId))
				{
				    person.getChrInfo().getInfo().getPlace().setIdRef(strAbbreviationFinalized);
				}
				if (null != person.getDeathInfo() &&
				    null != person.getDeathInfo().getInfo() &&
				    null != person.getDeathInfo().getInfo().getPlace() &&
		    		person.getDeathInfo().getInfo().getPlace().getIdRef().equals(strCurrentPlaceId))
				{
				    person.getDeathInfo().getInfo().getPlace().setIdRef(strAbbreviationFinalized);
				}
				if (null != person.getBurialInfo() &&
				    null != person.getBurialInfo().getInfo() &&
				    null != person.getBurialInfo().getInfo().getPlace() &&
		    		person.getBurialInfo().getInfo().getPlace().getIdRef().equals(strCurrentPlaceId))
				{
				    person.getBurialInfo().getInfo().getPlace().setIdRef(strAbbreviationFinalized);
				}
		     }
		     
		     // Fixup Marriage List
			Iterator<Marriage> iterMarriages = marriageList.getMarriages();
			while (iterMarriages.hasNext())
			{
				Marriage marriage = iterMarriages.next();
				if (null != marriage.getMarriageInfo() &&
				    null != marriage.getMarriageInfo().getInfo() &&
				    null != marriage.getMarriageInfo().getInfo().getPlace() &&
		    		marriage.getMarriageInfo().getInfo().getPlace().getIdRef().equals(strCurrentPlaceId))
				{
				    marriage.getMarriageInfo().getInfo().getPlace().setIdRef(strAbbreviationFinalized);
				}
			}
		     
			Iterator<Reference> iterReferences = referenceList.getReferences();
			while (iterReferences.hasNext())
			{
				Reference reference = iterReferences.next();
				if (null != reference.getCitation() &&
				    null != reference.getCitation().getPlace() &&
		    		reference.getCitation().getPlace().getIdRef().equals(strCurrentPlaceId))
				{
					reference.getCitation().getPlace().setIdRef(strAbbreviationFinalized);
				}
			}
			
			Iterator<Photo> iterPhotos = photoList.getPhotos();
			while (iterPhotos.hasNext())
			{
				Photo photo = iterPhotos.next();
				if (null != photo.getSource() &&
				    null != photo.getSource().getSingleton() &&
				    null != photo.getSource().getSingleton().getPlace() &&
				    photo.getSource().getSingleton().getPlace().getIdRef().equals(strCurrentPlaceId))
				{
					photo.getSource().getSingleton().getPlace().setIdRef(strAbbreviationFinalized);
				}
			}
		 }
		 return mReplacement;
	}
*/
}
