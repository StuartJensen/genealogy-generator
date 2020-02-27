package home.genealogy.forms.html;

import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import home.genealogy.CommandLineParameters;
import home.genealogy.configuration.CFGFamily;
import home.genealogy.indexes.IndexMarriageToSpouses;
import home.genealogy.indexes.TaggedContainerDescriptor;
import home.genealogy.lists.MarriageList;
import home.genealogy.lists.PersonList;
import home.genealogy.lists.PhotoList;
import home.genealogy.lists.ReferenceList;
import home.genealogy.schema.all.Column;
import home.genealogy.schema.all.Marriage;
import home.genealogy.schema.all.MarriageId;
import home.genealogy.schema.all.Paragraph;
import home.genealogy.schema.all.Person;
import home.genealogy.schema.all.PersonId;
import home.genealogy.schema.all.Photo;
import home.genealogy.schema.all.PhotoId;
import home.genealogy.schema.all.Reference;
import home.genealogy.schema.all.ReferenceEntryId;
import home.genealogy.schema.all.ReferenceId;
import home.genealogy.schema.all.Row;
import home.genealogy.schema.all.SeeAlso;
import home.genealogy.schema.all.Space;
import home.genealogy.schema.all.Table;
import home.genealogy.schema.all.Tag;
import home.genealogy.schema.all.helpers.ColumnHelper;
import home.genealogy.schema.all.helpers.MarriageHelper;
import home.genealogy.schema.all.helpers.MarriageIdHelper;
import home.genealogy.schema.all.helpers.ParagraphHelper;
import home.genealogy.schema.all.helpers.ParagraphHelper.eParagraphLineEnd;
import home.genealogy.schema.all.helpers.PersonHelper;
import home.genealogy.schema.all.helpers.PersonIdHelper;
import home.genealogy.schema.all.helpers.PhotoHelper;
import home.genealogy.schema.all.helpers.PhotoIdHelper;
import home.genealogy.schema.all.helpers.PhotoIdHelper.ePhotoIdType;
import home.genealogy.schema.all.helpers.ReferenceEntryIdHelper;
import home.genealogy.schema.all.helpers.ReferenceHelper;
import home.genealogy.schema.all.helpers.ReferenceIdHelper;
import home.genealogy.schema.all.helpers.RowHelper;
import home.genealogy.schema.all.helpers.SeeAlsoHelper;
import home.genealogy.schema.all.helpers.SpaceHelper;
import home.genealogy.schema.all.helpers.TableHelper;
import home.genealogy.schema.all.helpers.TagHelper;

public class HTMLShared
{
	
	public static final String FGSFILENAME = "fgsnoldse";
	public static final String FGSDIR = "fgs";

	public static final String PHOTOWRAPFILENAME = "photowrap";
	public static final String PHOTOWRAPDIR = "photowrappers";

	public static final String REFERENCEFILENAME = "ref";
	public static final String REFERENCEDIR = "references";

	public static final String PHOTOINDEXFILENAME = "photowrapindex";
	public static final String PHOTOINDEXDIR = "index";

	public static final String REFINDEXFILENAME = "refindex";
	public static final String REFINDEXDIR = "index";

	public static final String PERINFOFILENAME = "perinfo";
	public static final String PERINFODIR = "perinfo";
	
	public static final String IGNOREAREA_BEGIN = "<!-- IGNORE AREA BEGIN -->";
	public static final String IGNOREAREA_END = "<!-- IGNORE AREA END -->";

	
	public static String buildParagraphString(CFGFamily family, CommandLineParameters commandLineParameters,
											  Paragraph paragraph, PersonList personList, MarriageList marriageList,
			                                  ReferenceList referenceList, PhotoList photoList,
			                                  IndexMarriageToSpouses indexMarrToSpouses,
			                                  boolean bIndentIfIndicated,
			                                  boolean bLineEndIfIndicated,
			                                  boolean bSuppressLiving,
			                                  HTMLParagraphFormat paragraphFormat,
			                                  long lContainerId,
			                                  long lSubContainerId)
	{
		StringBuffer sb = new StringBuffer(1024);

		// Get the Paragraph format that will be used
		if (null == paragraphFormat)
		{	// Use the default settings for the format
			paragraphFormat = new HTMLParagraphFormat();
		}
	
		// Add indent string if indicated
		if (bIndentIfIndicated && ParagraphHelper.getIndent(paragraph))
		{
			sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
		}
	
		List<Object> alParagraph = ParagraphHelper.getContent(paragraph);
		for(int i=0; i<alParagraph.size(); i++)
		{
			Object oParagraph = alParagraph.get(i);
			if (oParagraph instanceof Space)
			{
				Space space = (Space)oParagraph;			
				sb.append(SpaceHelper.getSpaces(space, "&nbsp;"));
			}
			else if (oParagraph instanceof PersonId)
			{
				PersonId personId = (PersonId)oParagraph;
				int iPersonId = PersonIdHelper.getPersonId(personId);
				Person person = personList.get(iPersonId);
				sb.append("<span class=\"").append(paragraphFormat.getPersonIdSpan()).append("\">");
				if (null != person)
				{
					PersonHelper personHelper = new PersonHelper(person, bSuppressLiving);
					String strUrl = family.getUrlPrefix() + PERINFODIR + "/" + PERINFOFILENAME +  iPersonId + ".htm";
					sb.append("<a href=\"").append(strUrl).append("\">");
					sb.append(personHelper.getPersonName());
					sb.append("</a>");
				}
				else
				{
					System.out.println("HTMLShared::buildParagraphString(): Could not find person given PERSONID " + iPersonId);
					sb.append("<i>[Unknown Person Id: ").append(iPersonId).append("]</i>");
				}
				sb.append("</span>");
			}
			else if (oParagraph instanceof MarriageId)
			{
				MarriageId marriageId = (MarriageId)oParagraph;
				int iMarriageId = MarriageIdHelper.getMarriageId(marriageId);
				Marriage marriage = marriageList.get(iMarriageId);
				sb.append("<span class=\"").append(paragraphFormat.getMarriageIdSpan()).append("\">");
				if (null != marriage)
				{
					String strUrl = family.getUrlPrefix() + "fgs/" + FGSFILENAME + iMarriageId + ".htm";
					sb.append("<a href=\"").append(strUrl).append("\">");
					sb.append(buildSimpleMarriageNameString(personList, indexMarrToSpouses, iMarriageId, bSuppressLiving, "%s and %s"));
					sb.append("</a>");
				}
				else
				{
					System.out.println("HTMLShared::buildParagraphString(): Could not find marriage given MARRIAGEID " + iMarriageId);
					sb.append("<i>[Unknown Person Id: ").append(iMarriageId).append("]</i>");
				}
				sb.append("</span>");
			}
			else if (oParagraph instanceof ReferenceId)
			{
				ReferenceId referenceId = (ReferenceId)oParagraph;
				int iReferenceId = ReferenceIdHelper.getReferenceId(referenceId);
				Reference reference = referenceList.get(iReferenceId);
				sb.append("<span class=\"").append(paragraphFormat.getReferenceIdSpan()).append("\">");
				if (null != reference)
				{
					String strUrl = family.getUrlPrefix() + "references/" + REFERENCEFILENAME + "/" + iReferenceId + ".htm";
					sb.append("<a href=\"").append(strUrl).append("\">");
					sb.append(ReferenceHelper.getTitle(reference));
					sb.append("</a>");
				}
				else
				{
					System.out.println("HTMLShared::buildParagraphString(): Could not find reference given REFERENCEID " + iReferenceId);
					sb.append("<i>[Unknown Reference Id: ").append(iReferenceId).append("]</i>");
				}
				sb.append("</span>");
			}
			else if (oParagraph instanceof ReferenceEntryId)
			{
				ReferenceEntryId referenceEntryId = (ReferenceEntryId)oParagraph;
				int iReferenceId = ReferenceEntryIdHelper.getReferenceId(referenceEntryId);
				int iReferenceEntryId = ReferenceEntryIdHelper.getReferenceEntryId(referenceEntryId);
				Reference reference = referenceList.get(iReferenceId);
				sb.append("<span class=\"").append(paragraphFormat.getReferenceIdSpan()).append("\">");
				if (null != reference)
				{
					String strUrl = family.getUrlPrefix() + REFERENCEDIR + "/" + REFERENCEFILENAME + iReferenceId + ".htm#REF" + iReferenceId + "ENT" + iReferenceEntryId;
					sb.append("<a href=\"").append(strUrl).append("\">");
					sb.append(ReferenceHelper.getTitle(reference));
					sb.append("</a>");
				}
				else
				{
					System.out.println("HTMLShared::buildParagraphString(): Could not find reference given REFERENCEID " + iReferenceId + " ENTRYID " + iReferenceEntryId);
					sb.append("<i>[Unknown Reference Id: ").append(iReferenceId).append("]</i>");
				}
				sb.append("</span>");
			}
			else if (oParagraph instanceof PhotoId)
			{
				PhotoId photoId = (PhotoId)oParagraph;
				int iPhotoId = PhotoIdHelper.getPhotoId(photoId);
				Photo photo = photoList.get(iPhotoId);
				sb.append("<span class=\"").append(paragraphFormat.getPhotoIdSpan()).append("\">");
				if (null != photo)
				{
					if (PhotoIdHelper.getType(photoId) == ePhotoIdType.eLinked)
					{	// Linked
						String strUrl = family.getUrlPrefix() + "/" + PHOTOWRAPDIR + "/" + PHOTOWRAPFILENAME + iPhotoId + ".htm";
						sb.append("<a href=\"").append(strUrl).append("\">[Photo]:</a>&nbsp;&nbsp;");
						
						List<Paragraph> lDescription = PhotoHelper.getDescription(photo);
						sb.append(buildParagraphListObject(family, commandLineParameters, lDescription, personList, marriageList,
		                           	 	 	  	 	 	   referenceList, photoList, indexMarrToSpouses, false,
		                           						   false, bSuppressLiving, paragraphFormat,
		                           						   ReferenceIdHelper.REFERENCEID_INVALID,
		                           						   ReferenceEntryIdHelper.REFERENCEENTRYID_INVALID));
						sb.append("</a>");
					}
					else
					{	// Embedded
						String strWrapperHRef;
						String strFilePath = PhotoHelper.getSmallestWebbableFile(photo);
						strFilePath = replaceBkSlashWithPhotoFileNameSeparator(strFilePath);
						strFilePath = strFilePath.toLowerCase();
						if (0 != strFilePath.length())
						{	// Start Photo Frame Table
							sb.append("<table valign=\"center\">");
							sb.append("<tr>");
							sb.append("<td></td>");
							sb.append("<td>");
							sb.append("<table border=\"10\" bordercolor=\"#660000\" cellpadding=20 bgcolor=\"#dbcbaa\">");
							sb.append("<tr>");
							sb.append("<td>");
							// Actual Link to Photo File
							String strUrl = family.getUrlPrefix() + PHOTOWRAPDIR + "/" + PHOTOWRAPFILENAME + iPhotoId + ".htm";
							String strImg = "<img border=\"0\" src=\"" + family.getUrlPrefix() + "photos\\" + strFilePath + "\" alt=\"Photo#" + iPhotoId + "\">";
							sb.append("<a href=\"").append(strUrl).append("\">");
							sb.append(strImg);
							sb.append("</a>");
							// End Photo Frame Table
							sb.append("</td>");
							sb.append("</tr>");
							sb.append("</table>");
							sb.append("</td>");
							sb.append("</tr>");
							sb.append("</table>");
						}
					}
				}	
			}
			else if (oParagraph instanceof JAXBElement<?>)
			{
				JAXBElement<String> jb = (JAXBElement<String>)oParagraph;
				QName qn = jb.getName();
				String strContent = jb.getValue();
				String strElementName = qn.getLocalPart();
				if (strElementName.equals("editorComment"))
				{
					sb.append("<span class=\"").append(paragraphFormat.getEditorCommentSpan()).append("\">");
					sb.append("<i>").append(replaceCRLFWithSpaces(strContent)).append("</i>");
					sb.append("</span>");
				}
				else if (strElementName.equals("formText"))
				{
					sb.append("<span class=\"").append(paragraphFormat.getFormTextSpan()).append("\">");
					sb.append("<b>").append(replaceCRLFWithSpaces(strContent)).append("</b>");
					sb.append("</span>");
				}
				else if (strElementName.equals("privateText"))
				{
					sb.append("<span class=\"").append(paragraphFormat.getParagraphTextSpan()).append("\">");
					if (!bSuppressLiving)
					{
						sb.append(replaceCRLFWithSpaces(strContent));
					}
					else
					{
						sb.append("[Private Data]");
					}
					sb.append("</span>");
				}
				else
				{
					sb.append("<span class=\"").append(paragraphFormat.getParagraphTextSpan()).append("\">");
					sb.append(replaceCRLFWithSpaces(strContent));
					sb.append("</span>");
				}
			}
			else if (oParagraph instanceof Tag)
			{
				Tag tag = (Tag)oParagraph;
				if (!TagHelper.isInvalidTag(tag))
				{
					int iId = TagHelper.getPersonId(tag);
					String strId = "PID";
					if (TagHelper.isMarriageTag(tag))
					{
						iId = TagHelper.getMarriageId(tag);
						strId = "MID";
					}
					sb.append("<a name=\"C").append(lContainerId).append("SC").append(lSubContainerId);
					sb.append(strId).append(iId).append("T").append(TagHelper.getType(tag));
					sb.append("\"></a>");
				}
			}
			else if (oParagraph instanceof String)
			{
				sb.append("<span class=\"").append(paragraphFormat.getParagraphTextSpan()).append("\">");
				sb.append(oParagraph);
				sb.append("</span>");
			}
			else
			{
				System.out.println("HTMLShared::buildParagraphString(): Invalid Embedded Tag: " + oParagraph);
			}
		}
		// Add Line End if indicated
		if (bLineEndIfIndicated &&
		   (eParagraphLineEnd.eHardReturn == ParagraphHelper.getLineEnd(paragraph)))
		{
			sb.append("<br>");
		}
		return sb.toString();
	}
	
	public static String buildParagraphListObject(CFGFamily family, CommandLineParameters commandLineParameters,
			  									  List<Paragraph> lParagraph, PersonList personList, MarriageList marriageList,
			  									  ReferenceList referenceList, PhotoList photoList,
			  									  IndexMarriageToSpouses indexMarrToSpouses,
			  									  boolean bIndentIfIndicated,
			  									  boolean bLineEndIfIndicated,
			  									  boolean bSuppressLiving,
			  									  HTMLParagraphFormat paragraphFormat,
			  									  long lContainerId,
			  									  long lSubContainerId)
	{
		StringBuffer sb = new StringBuffer(1024);
		for (int i=0; i<lParagraph.size(); i++)
		{
			Paragraph paragraph =lParagraph.get(i);
			sb.append(buildParagraphString(family, commandLineParameters, paragraph, personList, marriageList,
						                referenceList, photoList,
						                indexMarrToSpouses,
						                bIndentIfIndicated,
						                bLineEndIfIndicated,
						                bSuppressLiving,
						                paragraphFormat,
						                lContainerId,
						                lSubContainerId));
		}
		return sb.toString();
}
	
	public static String replaceCRLFWithSpaces(String strTarget)
	{
		StringBuffer sb = new StringBuffer(strTarget);
		for (int i=0; i<sb.length(); i++)
		{
			if ((sb.charAt(i) == 0x0a) || (sb.charAt(i) == 0x0d))
			{
				sb.setCharAt(i, ' ');
			}
		}
		return(sb.toString());
	}
	
	public static String buildSimpleMarriageNameString(PersonList personList, IndexMarriageToSpouses index,
			                                           int iMarriageId, boolean bSupressLiving, String strTemplate)
	{
		int arSpouses[] = index.getSpouses(iMarriageId);
		int iHusbandPersonId = arSpouses[IndexMarriageToSpouses.GET_SPOUSES_HUSBAND_IDX];
		int iWifePersonId = arSpouses[IndexMarriageToSpouses.GET_SPOUSES_WIFE_IDX];
		
		if ((PersonIdHelper.PERSONID_INVALID != iHusbandPersonId) &&
			(PersonIdHelper.PERSONID_INVALID != iWifePersonId))
		{
			Person husband = personList.get(iHusbandPersonId);
			Person wife = personList.get(iWifePersonId);
			if ((null != husband) && (null != wife))
			{
				PersonHelper helperHusband = new PersonHelper(husband, bSupressLiving);
				PersonHelper helperWife = new PersonHelper(wife, bSupressLiving);
				String strNames = String.format(strTemplate, helperHusband.getPersonName(), helperWife.getPersonName());
				return strNames;
			}
		}
		return "";
	}
	
	public static String buildStandardBracketedLink(String strHRef, String strText)
	{
		StringBuffer sb = new StringBuffer();
		sb.append("<span class=\"pageBodyNormalLink\">");
		sb.append("[&nbsp;");
		sb.append("<a href=\"").append(strHRef).append("\">");
		sb.append(strText);
		sb.append("</a>&nbsp;]:</span>&nbsp;");
		return sb.toString();
	}
	
	public static String replaceBkSlashWithPhotoFileNameSeparator(String strTarget)
	{
		StringBuffer sb = new StringBuffer(strTarget);
		for (int i=0; i<sb.length(); i++)
		{
			if (sb.charAt(i) == '\\')
			{
				sb.setCharAt(i, '/');
			}
		}
		return(sb.toString());
	}
	
	public static String buildSeeAlsoString(SeeAlso seeAlso, CFGFamily family, CommandLineParameters commandLineParameters,
			                                PersonList personList, MarriageList marriageList, ReferenceList referenceList,
			                                PhotoList photoList, HTMLParagraphFormat paragraphFormat, IndexMarriageToSpouses indexMarrToSpouses)
	{
		// Get the Paragraph format that will be used
		if (null == paragraphFormat)
		{	// Use the default settings for the format
			paragraphFormat = new HTMLParagraphFormat();
		}
		
		boolean bSuppressLiving = commandLineParameters.getSuppressLiving();
		StringBuffer sb = new StringBuffer(256);
		
		int iSeeAlsoObjectCount = SeeAlsoHelper.getObjectCount(seeAlso);
		for (int i=0; i<iSeeAlsoObjectCount; i++)
		{
			Object object = SeeAlsoHelper.getObject(seeAlso, i);
			if (object instanceof PersonId)
			{
				PersonId pId = (PersonId)object;
				int iPersonId = PersonIdHelper.getPersonId(pId);
				Person person = personList.get(iPersonId);
				sb.append("<span class=\"").append(paragraphFormat.getPersonIdSpan()).append("\">");
				if (null != person)
				{
					PersonHelper personHelper = new PersonHelper(person, bSuppressLiving);
					String strUrl = family.getUrlPrefix() + PERINFODIR + "/" + PERINFOFILENAME + personHelper.getPersonId() + ".htm";
					sb.append("<a href=\"").append(strUrl).append("\">");
					sb.append(personHelper.getPersonName());
					sb.append("</a>");
				}
				else
				{
					System.out.println("HTMLShared.buildSeeAlsoString(): Could not find person given person id: " + iPersonId);
					sb.append("<i>[Unknown Person Id: " + iPersonId + "]</i>");
				}
				sb.append("</span><br>");
			}
			else if (object instanceof MarriageId)
			{
				MarriageId mId = (MarriageId)object;
				int iMarriageId = MarriageIdHelper.getMarriageId(mId);
				Marriage marriage = marriageList.get(iMarriageId);
				sb.append("<span class=\"").append(paragraphFormat.getMarriageIdSpan()).append("\">");
				if (null != marriage)
				{
					Person husband = personList.get(MarriageHelper.getHusbandPersonId(marriage));
					Person wife = personList.get(MarriageHelper.getWifePersonId(marriage));
					if ((null != husband) && (null != wife))
					{
						PersonHelper husbandHelper = new PersonHelper(husband, bSuppressLiving);
						PersonHelper wifeHelper = new PersonHelper(wife, bSuppressLiving);
						MarriageHelper marriageHelper = new MarriageHelper(marriage, husbandHelper, wifeHelper, bSuppressLiving);
						String strUrl = family.getUrlPrefix() + FGSDIR + "/" + FGSFILENAME + MarriageHelper.getMarriageId(marriage) + ".htm";
						sb.append("<a href=\"").append(strUrl).append("\">");
						sb.append(marriageHelper.getMarriageName(personList));
						sb.append("</a>");
					}
				}
				else
				{
					System.out.println("HTMLShared.buildSeeAlsoString(): Could not find marriage given MARRIAGEID: " + iMarriageId);
					sb.append("<i>[Unknown Marriage Id: " + iMarriageId + "]</i>");
				}
				sb.append("</span><br>");
			}
			else if (object instanceof ReferenceId)
			{
				ReferenceId rId = (ReferenceId)object;
				int iReferenceId = ReferenceIdHelper.getReferenceId(rId);
				Reference reference = referenceList.get(iReferenceId);
				sb.append("<span class=\"").append(paragraphFormat.getReferenceIdSpan()).append("\">");
				if (null != reference)
				{
					String strUrl = family.getUrlPrefix() + REFERENCEDIR + "/" + REFERENCEFILENAME + ReferenceHelper.getReferenceId(reference) + ".htm";
					sb.append("<a href=\"").append(strUrl).append("\">");
					sb.append(ReferenceHelper.getTitle(reference));
					sb.append("</a>");
				}
				else
				{
					System.out.println("HTMLShared.buildSeeAlsoString(): Could not find reference given REFERENCEID: " + iReferenceId);
					sb.append("<i>[Unknown Reference Id: " + iReferenceId + "]</i>");
				}
				sb.append("</span><br>");
			}
			else if (object instanceof PhotoId)
			{
				PhotoId photoId = (PhotoId)object;
				int iPhotoId = PhotoIdHelper.getPhotoId(photoId);
				Photo photo = photoList.get(iPhotoId);
				sb.append("<span class=\"").append(paragraphFormat.getPhotoIdSpan()).append("\">");
				if (null != photo)
				{
					List<Paragraph> lDescription = PhotoHelper.getDescription(photo);
					String strDescription = buildParagraphListObject(family, commandLineParameters, lDescription, personList, marriageList,
	                           referenceList, photoList, indexMarrToSpouses, false,
	                           false, bSuppressLiving, paragraphFormat,
	                           ReferenceIdHelper.REFERENCEID_INVALID,
	                           ReferenceEntryIdHelper.REFERENCEENTRYID_INVALID);
					
					if (PhotoIdHelper.isLinked(photoId))
					{	// Linked
						String strUrl = family.getUrlPrefix() + PHOTOWRAPDIR + "/" + PHOTOWRAPFILENAME + PhotoHelper.getPhotoId(photo) + ".htm";
						sb.append("<a href=\"").append(strUrl).append("\">[Photo]:</a>&nbsp;&nbsp;");
						sb.append(strDescription);
	
					}
					else
					{	// Embedded
						String strFilePath = PhotoHelper.getSmallestWebbableFile(photo);
						strFilePath = replaceBkSlashWithPhotoFileNameSeparator(strFilePath);
						if (0 != strFilePath.length())
						{
							sb.append("<img border=\"0\" src=\"").append(family.getUrlPrefix()).append("photos\\").append(strFilePath).append("\" alt=\"").append(strDescription).append("\">");
						}
					}
				}
				else
				{
					System.out.println("HTMLShared.buildSeeAlsoString(): Could not find photo given PHOTOID: " + iPhotoId);
					sb.append("<i>[Unknown Photo Id: " + iPhotoId + "]</i>");	
				}
				sb.append("</span><br>");
			}
			else if (object instanceof ReferenceEntryId)
			{
				ReferenceEntryId referenceEntryId = (ReferenceEntryId)object;
				int iReferenceId = ReferenceEntryIdHelper.getReferenceId(referenceEntryId);
				int iReferenceEntryId = ReferenceEntryIdHelper.getReferenceEntryId(referenceEntryId);				
				Reference reference = referenceList.get(iReferenceId);
				sb.append("<span class=\"").append(paragraphFormat.getReferenceIdSpan()).append("\">");
				if (null != reference)
				{
					StringBuffer sbUrl = new StringBuffer(256);
					sbUrl.append(family.getUrlPrefix()).append(REFERENCEDIR).append("/").append(REFERENCEFILENAME);
					sbUrl.append(iReferenceId).append(".htm#REF").append(iReferenceId).append("ENT").append(iReferenceEntryId);
					sb.append("<a href=\"").append(sbUrl.toString()).append("\">[Document Entry]:</a>&nbsp;&nbsp;");
					sb.append(ReferenceHelper.getTitle(reference));
				}
				else
				{
					System.out.println("HTMLShared.buildSeeAlsoString(): Could not find reference entry given REFERENCEID: " + iReferenceId + ":" + iReferenceEntryId);
					sb.append("<i>[Unknown Reference Entry Id: " + iReferenceId + ":" + iReferenceEntryId + "]</i>");	
				}
				sb.append("</span><br>");
			}
		}
		return sb.toString();
	}

	
	public static String buildTableString(CFGFamily family, CommandLineParameters commandLineParameters,
			  							  Table table, PersonList personList, MarriageList marriageList,
			  							  ReferenceList referenceList, PhotoList photoList,
			  							  IndexMarriageToSpouses indexMarrToSpouses,
			  							  boolean bIndentIfIndicated,
			  							  boolean bLineEndIfIndicated,
			  							  boolean bSuppressLiving,
			  							  HTMLParagraphFormat paragraphFormat,
			  							  long lContainerId,
			  							  long lSubContainerId)
	{
		StringBuffer sb = new StringBuffer(256);
		
		String strBorderValue = "0";
		if (TableHelper.isBorderSingleLine(table))
		{
			strBorderValue = "1";
		}
		sb.append("<table border=\"").append(strBorderValue).append("\">\n");
		int iRowCount = TableHelper.getRowCount(table);
		for (int r=0; r<iRowCount; r++)
		{
			Row row = TableHelper.getRow(table, r);
			sb.append("<tr>\n");
			int iColCount = RowHelper.getColumnCount(row);
			for (int c=0; c<iColCount; c++)
			{
				Column column = RowHelper.getColumn(row, c);
				sb.append("<td>\n");
				
				int iParagraphCount = ColumnHelper.getParagraphCount(column);
				for (int p=0; p<iParagraphCount; p++)
				{
					Paragraph paragraph = ColumnHelper.getParagraph(column, p);
					String strParagraph = HTMLShared.buildParagraphString(family, commandLineParameters,
							paragraph, personList, marriageList, referenceList, photoList,
							indexMarrToSpouses, true, true, bSuppressLiving, null, lContainerId,
	                        lSubContainerId);
					if (0 != strParagraph.length())
					{
						sb.append(strParagraph).append("\n");
					}
				}
				sb.append("</td>\n");
			}
			sb.append("</tr>\n");
		}
		sb.append("</table>\n");
		return sb.toString();
	}

	public static String getReferenceHRef(CFGFamily family, TaggedContainerDescriptor tagDescriptor)
	{
		StringBuffer sbHRef = new StringBuffer(128);
		sbHRef.append(family.getUrlPrefix()).append(REFERENCEDIR);
		sbHRef.append("/");
		sbHRef.append(REFERENCEFILENAME);
		sbHRef.append(tagDescriptor.getContainerId());
		sbHRef.append(".htm#REF");
		sbHRef.append(tagDescriptor.getContainerId());
		sbHRef.append("ENT");
		sbHRef.append(tagDescriptor.getSubContainerId());
		
		return sbHRef.toString();
	}
	
	public static String getEmbeddedReferenceHRef(CFGFamily family, TaggedContainerDescriptor tagDescriptor, String strTagType, PersonHelper personHelper)
	{
		StringBuffer sbMarker = new StringBuffer(56);
		sbMarker.append("C").append(tagDescriptor.getContainerId());
		sbMarker.append("SC").append(tagDescriptor.getSubContainerId());
		sbMarker.append("PID").append(personHelper.getPersonId());
		sbMarker.append("T").append(strTagType);
		
		StringBuffer sbHRef = new StringBuffer(128);
		sbHRef.append(family.getUrlPrefix()).append(REFERENCEDIR);
		sbHRef.append("/");
		sbHRef.append(REFERENCEFILENAME);
		sbHRef.append(tagDescriptor.getContainerId());
		sbHRef.append(sbMarker);
		
		return sbHRef.toString();
	}
	
/*	
	CString GetHRef()
	{
		CString strWork, strMarker;
		if (m_bPersonOrMarriage)
		{	// Person
			if (m_bType)
			{	// reference
				if (!m_tagPerson.GetEmbedded())
				{
					strWork.Format("%s%s/%s%d.htm#REF%dENT%d", ((FormsHtmlConfiguration *)m_pConfiguration)->getBasePath(), REFERENCEDIR, REFERENCEFILENAME, m_tagPerson.GetContainingObjectId(), m_tagPerson.GetContainingObjectId(), m_tagPerson.GetContainingObjectSubId());
				}
				else
				{
					strMarker.Format("C%dSC%dPID%dT%s", m_tagPerson.GetContainingObjectId(), m_tagPerson.GetContainingObjectSubId(), m_tagPerson.GetPersonId(), m_tagPerson.GetTagType());
					strWork.Format("%s%s/%s%d.htm#%s", ((FormsHtmlConfiguration *)m_pConfiguration)->getBasePath(), REFERENCEDIR, REFERENCEFILENAME, m_tagPerson.GetContainingObjectId(), strMarker);
				}
			}
			else
			{	// photo
				if (!m_tagPerson.GetEmbedded())
				{
					strWork.Format("%s%s/%s%d.htm", ((FormsHtmlConfiguration *)m_pConfiguration)->getBasePath(), PHOTOWRAPDIR, PHOTOWRAPFILENAME, m_tagPerson.GetContainingObjectId());
				}
				else
				{
					strMarker.Format("C%dSC0PID%dT%s", m_tagPerson.GetContainingObjectId(), m_tagPerson.GetPersonId(), m_tagPerson.GetTagType());
					strWork.Format("%s%s/%s%d.htm#%s", ((FormsHtmlConfiguration *)m_pConfiguration)->getBasePath(), REFERENCEDIR, REFERENCEFILENAME, m_tagPerson.GetContainingObjectId(), strMarker);
				}
			}
		}
		else
		{	// Marriage
			if (m_bType)
			{	// reference
				if (!m_tagMarriage.GetEmbedded())
				{
					strWork.Format("%s%s/%s%d.htm#REF%dENT%d", ((FormsHtmlConfiguration *)m_pConfiguration)->getBasePath(), REFERENCEDIR, REFERENCEFILENAME, m_tagMarriage.GetContainingObjectId(), m_tagMarriage.GetContainingObjectId(), m_tagMarriage.GetContainingObjectSubId());
				}
				else
				{
					strMarker.Format("C%dSC%dMID%dT%s", m_tagMarriage.GetContainingObjectId(), m_tagMarriage.GetContainingObjectSubId(), m_tagMarriage.GetMarriageId(), m_tagMarriage.GetTagType());
					strWork.Format("%s%s/%s%d.htm#%s", ((FormsHtmlConfiguration *)m_pConfiguration)->getBasePath(), REFERENCEDIR, REFERENCEFILENAME, m_tagMarriage.GetContainingObjectId(), strMarker);
				}
			}
			else
			{	// photo
				if (!m_tagMarriage.GetEmbedded())
				{
					strWork.Format("%s%s/%s%d.htm", ((FormsHtmlConfiguration *)m_pConfiguration)->getBasePath(), PHOTOWRAPDIR, PHOTOWRAPFILENAME, m_tagMarriage.GetContainingObjectId());
				}
				else
				{
					strMarker.Format("C%dSC0MID%dT%s", m_tagMarriage.GetContainingObjectId(), m_tagMarriage.GetMarriageId(), m_tagMarriage.GetTagType());
					strWork.Format("%s%s/%s%d.htm#%s", ((FormsHtmlConfiguration *)m_pConfiguration)->getBasePath(), REFERENCEDIR, REFERENCEFILENAME, m_tagMarriage.GetContainingObjectId(), strMarker);
				}
			}
		}
		return strWork;
	}
*/
}
