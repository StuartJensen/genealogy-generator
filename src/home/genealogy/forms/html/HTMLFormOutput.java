package home.genealogy.forms.html;

import home.genealogy.configuration.CFGFamily;
import home.genealogy.lists.MarriageList;
import home.genealogy.lists.PersonList;
import home.genealogy.schema.all.Person;
import home.genealogy.schema.all.PersonId;
import home.genealogy.schema.all.helpers.MarriageIdHelper;
import home.genealogy.schema.all.helpers.PersonHelper;
import home.genealogy.schema.all.helpers.PersonIdHelper;
import home.genealogy.util.FourGenerations;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.SimpleTimeZone;

public class HTMLFormOutput
{
	public enum styleSelectors
	{
		E_SidebarLogoFont,
		E_SidebarHeader,
		E_SidebarLink,
		E_SidebarSmallText,
		E_PageTopHeader,
		E_PageBodyMediumHeader,
		E_PageBodySmallHeader,
		E_PageBodySmallLink,
		E_PageBodyNormalLink,
		E_PageBodyLargeText,
		E_PageBodyMediumText,
		E_PageBodyNormalText,
		E_PageBodyBoldNormalText,
		E_PageBodySmallText};
	
	private StringBuffer m_sb;
	private FileOutputStream m_fos;
	
	public HTMLFormOutput(String strFileName)
		throws Exception
	{
		m_sb = new StringBuffer(3 * 1024);
		File f = new File(strFileName);
		f.delete();
		m_fos = new FileOutputStream(f);
	}
	
	public void commit()
		throws Exception
	{
		m_fos.write(m_sb.toString().getBytes("UTF-8"));
		m_fos.close();
	}
	
	public void output(String str)
	{
		m_sb.append(str);
	}
	
	public void outputStartAnchor(String strHRef)
	{
		m_sb.append("<A href=\"").append(strHRef).append("\">");
	}
	
	public void outputEndAnchor()
	{
		output("</a>");
	}

	public void outputBR()
	{
		outputBR(1);
	}

	public void outputBR(long lCount)
	{
		if (0 >= lCount)
		{
			return;
		}
		for (long i=0; i<lCount; i++)
		{
			m_sb.append("<br>");
		}
	}
	public void outputCRLF()
	{
		m_sb.append("\n");
	}
	
	public void outputSpan(styleSelectors type, String strBoundedText)
	{
		m_sb.append("<span class=\"");
		switch (type)
		{
			case E_SidebarLogoFont:
				m_sb.append("sidebarLogoFont");
				break;
			case E_SidebarHeader:
				m_sb.append("sidebarHeader");
				break;
			case E_SidebarSmallText:
				m_sb.append("sidebarSmallText");
				break;
			case E_SidebarLink:
				m_sb.append("sidebarLink");
				break;
			case E_PageTopHeader:
				m_sb.append("pageTopHeader");
				break;
			case E_PageBodyMediumHeader:
				m_sb.append("pageBodyMediumHeader");
				break;
			case E_PageBodySmallHeader:
				m_sb.append("pageBodySmallHeader");
				break;
			case E_PageBodySmallLink:
				m_sb.append("pageBodySmallLink");
				break;
			case E_PageBodyNormalLink:
				m_sb.append("pageBodyNormalLink");
				break;
			case E_PageBodyLargeText:
				m_sb.append("pageBodyLargeText");
				break;
			case E_PageBodyMediumText:
				m_sb.append("pageBodyMediumText");
				break;
			case E_PageBodyNormalText:
				m_sb.append("pageBodyNormalText");
				break;
			case E_PageBodyBoldNormalText:
				m_sb.append("pageBodyBoldNormalText");
				break;
			case E_PageBodySmallText:
				m_sb.append("pageBodySmallText");
				break;
			default:
				m_sb.append("UnknownSpanType");
		}
		m_sb.append("\">");
		m_sb.append(strBoundedText);
		m_sb.append("</span>");
	}

	public void outputStyle()
	{
		m_sb.append("<style>\n");
		m_sb.append(".sidebarLogoFont {\n");
		m_sb.append("font-family : Arial, Helvetica, sans-serif;\n");
		m_sb.append("font-size : 16pt;\n");
		m_sb.append("color : #660000;\n");
		m_sb.append("font-weight : bold;\n");
		m_sb.append("}\n\n");

		m_sb.append(".sidebarHeader {\n");
		m_sb.append("font-family : Arial, Helvetica, sans-serif;\n");
		m_sb.append("font-size : 9pt;\n");
		m_sb.append("color : #660000;\n");
		m_sb.append("font-weight : bold;\n");
		m_sb.append("}\n\n");

		m_sb.append(".sidebarLink {\n");
		m_sb.append("font-family : Arial, Helvetica, sans-serif;\n");
		m_sb.append("font-size : 8pt;\n");
		m_sb.append("color : #000000;\n");
		m_sb.append("}\n\n");

		m_sb.append(".sidebarSmallText {\n");
		m_sb.append("font-family : Arial, Helvetica, sans-serif;\n");
		m_sb.append("font-size : 8pt;\n");
		m_sb.append("color : #000000;\n");
		m_sb.append("}\n\n");

		m_sb.append(".pageTopHeader {\n");
		m_sb.append("font-family : Arial, Helvetica, sans-serif;\n");
		m_sb.append("font-size : 18pt;\n");
		m_sb.append("color : #660000;\n");
		m_sb.append("font-weight : bold;\n");
		m_sb.append("}\n\n");

		m_sb.append(".pageBodyMediumHeader {\n");
		m_sb.append("font-family : Arial, Helvetica, sans-serif;\n");
		m_sb.append("font-size : 14pt;\n");
		m_sb.append("color : #660000;\n");
		m_sb.append("font-weight : bold;\n");
		m_sb.append("}\n\n");

		m_sb.append(".pageBodySmallHeader {\n");
		m_sb.append("font-family : Arial, Helvetica, sans-serif;\n");
		m_sb.append("font-size : 10pt;\n");
		m_sb.append("color : #660000;\n");
		m_sb.append("font-weight : bold;\n");
		m_sb.append("}\n\n");

		m_sb.append(".pageBodySmallLink {\n");
		m_sb.append("font-family : Arial, Helvetica, sans-serif;\n");
		m_sb.append("font-size : 8pt;\n");
		m_sb.append("color : #000000;\n");
		m_sb.append("}\n\n");

		m_sb.append(".pageBodyNormalLink {\n");
		m_sb.append("font-family : Arial, Helvetica, sans-serif;\n");
		m_sb.append("font-size : 10pt;\n");
		m_sb.append("color : #000000;\n");
		m_sb.append("}\n\n");

		m_sb.append(".pageBodyLargeText {\n");
		m_sb.append("font-family : Arial, Helvetica, sans-serif;\n");
		m_sb.append("font-size : 14pt;\n");
		m_sb.append("color : #000000;\n");
		m_sb.append("}\n\n");

		m_sb.append(".pageBodyMediumText {\n");
		m_sb.append("font-family : Arial, Helvetica, sans-serif;\n");
		m_sb.append("font-size : 12pt;\n");
		m_sb.append("color : #000000;\n");
		m_sb.append("}\n\n");

		m_sb.append(".pageBodyNormalText {\n");
		m_sb.append("font-family : Arial, Helvetica, sans-serif;\n");
		m_sb.append("font-size : 10pt;\n");
		m_sb.append("color : #000000;\n");
		m_sb.append("}\n\n");

		m_sb.append(".pageBodyBoldNormalText {\n");
		m_sb.append("font-family : Arial, Helvetica, sans-serif;\n");
		m_sb.append("font-size : 10pt;\n");
		m_sb.append("color : #000000;\n");
		m_sb.append("font-weight : bold;\n");
		m_sb.append("}\n\n");

		m_sb.append(".pageBodySmallText {\n");
		m_sb.append("font-family : Arial, Helvetica, sans-serif;\n");
		m_sb.append("font-size : 8pt;\n");
		m_sb.append("color : #000000;\n");
		m_sb.append("}\n\n");

		m_sb.append("</style>\n");
	}
	
	private void outputGoogleAnalyticsScript()
	{
		m_sb.append("<script type=\"text/javascript\">\n");

		m_sb.append(" var _gaq = _gaq || [];\n");
		m_sb.append(" _gaq.push(['_setAccount', 'UA-36404295-1']);\n");
		m_sb.append(" _gaq.push(['_trackPageview']);\n");
		m_sb.append("  (function() {\n");
		m_sb.append("   var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;\n");
		m_sb.append("   ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';\n");
		m_sb.append("   var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);\n");
		m_sb.append("  })();\n");
		m_sb.append("</script>\n");
	}
	
	public void outputSidebarFrontEnd(String strPageTitle, CFGFamily family, PersonList personList, MarriageList marriageList)
	{
		String strAnchor;
		m_sb.append("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\">\n");
		m_sb.append("<HTML><HEAD><TITLE>" + strPageTitle + "</TITLE>\n");
		outputStyle();
		outputGoogleAnalyticsScript();
		m_sb.append("</head>\n");

		m_sb.append("<BODY aLink=#0000ff background=\"").append(family.getUrlPrefix()).append("home/backgrnd.jpg\" link=#0000a0 text=#000000 vLink=#800080>\n");
		m_sb.append("<TABLE>\n");
		m_sb.append("<TBODY>\n");
		m_sb.append("<TR vAlign=top>\n");
		m_sb.append("<TD vAlign=top width=142>");
		outputSpan(styleSelectors.E_SidebarLogoFont, family.getSurname() + "<BR>Family<BR>Genealogy<BR>");
		outputBR();
		outputCRLF();
		strAnchor = family.getUrlPrefix() + "home/index.htm";
		outputStartAnchor(strAnchor);
		outputSpan(styleSelectors.E_SidebarLink, "Home Page");
		outputEndAnchor();
		outputBR(2);
		outputCRLF();

		// FGS Family Links Header
		outputSpan(styleSelectors.E_SidebarHeader, "Family Group<BR>Sheets:");
		outputBR();
		outputCRLF();
		
		// Show Four great grandparents of the base person
		PersonId basePersonId = family.getBasePersonId();
		if (null != basePersonId)
		{
			FourGenerations fourG = new FourGenerations(PersonIdHelper.getPersonId(basePersonId), personList, marriageList);
			Person pFFF = fourG.getPaternalGrandFathersFather();
			if (null != pFFF)
			{
				String strSurname = PersonHelper.getLastName(pFFF);
				int iMarriageId = fourG.getPaternalGrandFathersFathersMarriageId();
				if ((null != strSurname) && (MarriageIdHelper.MARRIAGEID_INVALID != iMarriageId))
				{
					String strUrl = family.getUrlPrefix() + HTMLShared.FGSDIR + "/" + HTMLShared.FGSFILENAME +  iMarriageId + ".htm";
					outputStartAnchor(strUrl);
					outputSpan(styleSelectors.E_SidebarLink, strSurname + " Family");
					outputEndAnchor();
					outputBR();
					outputCRLF();
				}
			}
			Person pFMF = fourG.getPaternalGrandMothersFather();
			if (null != pFMF)
			{
				String strSurname = PersonHelper.getLastName(pFMF);
				int iMarriageId = fourG.getPaternalGrandMothersFathersMarriageId();
				if ((null != strSurname) && (MarriageIdHelper.MARRIAGEID_INVALID != iMarriageId))
				{
					String strUrl = family.getUrlPrefix() + HTMLShared.FGSDIR + "/" + HTMLShared.FGSFILENAME +  iMarriageId + ".htm";
					outputStartAnchor(strUrl);
					outputSpan(styleSelectors.E_SidebarLink, strSurname + " Family");
					outputEndAnchor();
					outputBR();
					outputCRLF();
				}
			}
			Person pMFF = fourG.getMaternalGrandFathersFather();
			if (null != pMFF)
			{
				String strSurname = PersonHelper.getLastName(pMFF);
				int iMarriageId = fourG.getMaternalGrandFathersFathersMarriageId();
				if ((null != strSurname) && (MarriageIdHelper.MARRIAGEID_INVALID != iMarriageId))
				{
					String strUrl = family.getUrlPrefix() + HTMLShared.FGSDIR + "/" + HTMLShared.FGSFILENAME +  iMarriageId + ".htm";
					outputStartAnchor(strUrl);
					outputSpan(styleSelectors.E_SidebarLink, strSurname + " Family");
					outputEndAnchor();
					outputBR();
					outputCRLF();
				}
			}
			Person pMMF = fourG.getMaternalGrandMothersFather();
			if (null != pMMF)
			{
				String strSurname = PersonHelper.getLastName(pMMF);
				int iMarriageId = fourG.getMaternalGrandMothersFathersMarriageId();
				if ((null != strSurname) && (MarriageIdHelper.MARRIAGEID_INVALID != iMarriageId))
				{
					String strUrl = family.getUrlPrefix() + HTMLShared.FGSDIR + "/" + HTMLShared.FGSFILENAME +  iMarriageId + ".htm";
					outputStartAnchor(strUrl);
					outputSpan(styleSelectors.E_SidebarLink, strSurname + " Family");
					outputEndAnchor();
					outputBR();
					outputCRLF();
				}
			}
			outputBR();
		}
		
		// Index Links Header
		outputSpan(styleSelectors.E_SidebarHeader, "Indexes:");
		outputBR();
		outputCRLF();

			// Surnames Link
		output("<b>");
		outputSpan(styleSelectors.E_SidebarLink, "Surnames:");
		output("</b>");
		outputBR();
		outputCRLF();

		output("&nbsp;&nbsp;");
		
		strAnchor = family.getUrlPrefix() + HTMLPersonListForm.PERSON_INDEX_FILE_SYSTEM_SUBDIRECTORY + "/surnames1.htm";
		outputStartAnchor(strAnchor);
		outputSpan(styleSelectors.E_SidebarLink, "Part One");
		outputEndAnchor();
		outputBR();
		outputCRLF();

		output("&nbsp;&nbsp;");
		strAnchor = family.getUrlPrefix() + HTMLPersonListForm.PERSON_INDEX_FILE_SYSTEM_SUBDIRECTORY + "/surnames2.htm";
		outputStartAnchor(strAnchor);
		outputSpan(styleSelectors.E_SidebarLink, "Part Two");
		outputEndAnchor();
		outputBR();
		outputCRLF();
		
		output("&nbsp;&nbsp;");
		strAnchor = family.getUrlPrefix() + HTMLPersonListForm.PERSON_INDEX_FILE_SYSTEM_SUBDIRECTORY + "/surnames3.htm";
		outputStartAnchor(strAnchor);
		outputSpan(styleSelectors.E_SidebarLink, "Part Three");
		outputEndAnchor();
		outputBR();
		outputCRLF();

		output("&nbsp;&nbsp;");
		strAnchor = family.getUrlPrefix() + HTMLPersonListForm.PERSON_INDEX_FILE_SYSTEM_SUBDIRECTORY + "/surnames4.htm";
		outputStartAnchor(strAnchor);
		outputSpan(styleSelectors.E_SidebarLink, "Part Four");
		outputEndAnchor();
		outputBR();
		outputCRLF();

		output("&nbsp;&nbsp;");
		strAnchor = family.getUrlPrefix() + HTMLPersonListForm.PERSON_INDEX_FILE_SYSTEM_SUBDIRECTORY + "/surnames5.htm";
		outputStartAnchor(strAnchor);
		outputSpan(styleSelectors.E_SidebarLink, "Part Five");
		outputEndAnchor();
		outputBR(2);
		outputCRLF();

			// Photos Link
		output("<b>");
		outputSpan(styleSelectors.E_SidebarLink, "Photos:");
		output("</b>");
		outputBR();
		outputCRLF();

		  // All
		output("&nbsp;&nbsp;");
		strAnchor = family.getUrlPrefix() + HTMLPersonListForm.PERSON_INDEX_FILE_SYSTEM_SUBDIRECTORY + "/" + HTMLShared.PHOTOINDEXFILENAME + ".htm";
		outputStartAnchor(strAnchor);
		outputSpan(styleSelectors.E_SidebarLink, "All Photos");
		outputEndAnchor();
		outputBR(2);
		outputCRLF();

		// Documentation Link
		output("<b>");
		outputSpan(styleSelectors.E_SidebarLink, "Documentation:");
		output("</b>");
		outputBR();
		outputCRLF();

		  // All
		output("&nbsp;&nbsp;");
		strAnchor = family.getUrlPrefix() + HTMLPersonListForm.PERSON_INDEX_FILE_SYSTEM_SUBDIRECTORY + "/" + HTMLShared.REFINDEXFILENAME + ".htm";
		outputStartAnchor(strAnchor);
		outputSpan(styleSelectors.E_SidebarLink, "All Docs");
		outputEndAnchor();
		outputBR(3);
		outputCRLF();

		// Contact Us Header
		outputSpan(styleSelectors.E_SidebarHeader, "Contact Us:");
		outputBR();
		outputCRLF();

		// Sign GuestBook Link
		strAnchor = family.getUrlPrefix() + "/home/guestbook.htm";
		outputStartAnchor(strAnchor);
		outputSpan(styleSelectors.E_SidebarLink, "Sign GuestBook");
		outputEndAnchor();
		outputBR();
		outputCRLF();
		outputBR(2);

		// Email Header
		outputSpan(styleSelectors.E_SidebarHeader, "EMail:");
		outputBR();
		outputCRLF();

		// EMail Address Text
		String strEMail = family.getContactEMail();
		if (null != strEMail)
		{
			strEMail = strEMail.replace("@", "<BR>@");
			outputSpan(styleSelectors.E_SidebarSmallText, strEMail);
			outputBR();
			outputCRLF();
			outputBR(2);
		}
		// Last Updated Header
		outputSpan(styleSelectors.E_SidebarHeader, "Last Updated:");
		outputBR();
		outputCRLF();

		// Last Updated Text
		long lCurrentTime = System.currentTimeMillis();
		SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
		output(HTMLShared.IGNOREAREA_BEGIN);
		outputSpan(styleSelectors.E_SidebarSmallText, sdf.format(new Date(lCurrentTime)));
		output(HTMLShared.IGNOREAREA_END);
		outputBR();
		outputCRLF();
		outputBR(2);

/* No Counters?		
		// Determine if Counter should be output
		String strCounterString = pConfiguration->getCounterStringForDocType();
		if (0 != strCounterString.GetLength())
		{
			// Visit Count Header
			outputSpan(styleSelectors.E_SidebarHeader, "Visit Count:");
			outputBR();
			outputCRLF();

			// Visit Count Image
			outputBR();
			output(strCounterString);
			outputBR();
			outputCRLF();
		}
*/
		// Close left sidebar table cell, Open right side body table cell
		output("</td><td>");
		outputCRLF();
	}

	public void outputSidebarBackEnd()
	{
		output("</td></tr></tbody></table></body></html>");
	}
	
	public void outputStandardBracketedLink(String strHRef, String strText)
	{
		String str;
		str = "<span class=\"pageBodyNormalLink\">";
		str += "[&nbsp;";
		str += "<a href=\"" + strHRef + "\">";
		str += strText;
		str += "</a>&nbsp;]:</span>&nbsp;";
		output(str);
	}

}
