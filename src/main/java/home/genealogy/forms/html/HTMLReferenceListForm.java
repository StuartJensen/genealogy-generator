package home.genealogy.forms.html;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import home.genealogy.GenealogyContext;
import home.genealogy.indexes.IndexMarriageToSpouses;
import home.genealogy.schema.all.Entry;
import home.genealogy.schema.all.Paragraph;
import home.genealogy.schema.all.Reference;
import home.genealogy.schema.all.extensions.ReferenceSortableByPlaceName;
import home.genealogy.schema.all.helpers.EntryHelper;
import home.genealogy.schema.all.helpers.ReferenceHelper;

public class HTMLReferenceListForm
{
	public static final String REFERENCE_INDEX_FILE_SYSTEM_SUBDIRECTORY = "index";
	
	private GenealogyContext m_context;
	private IndexMarriageToSpouses m_indexMarrToSpouses;
	  
	public HTMLReferenceListForm(GenealogyContext context,
							     IndexMarriageToSpouses indexMarrToSpouses)
	{
		m_context = context;
		m_indexMarrToSpouses = indexMarrToSpouses;
	}
	
	
	public void create()
		throws Exception
	{
		// Get the base output file system path and make sure it 
		// ends with a slash
		String strOutputPath = m_context.getFamily().getOutputPathHTML();
		if (!strOutputPath.endsWith("\\"))
		{
			strOutputPath += "\\";
		}
		// Make sure the "index" subdirectory exists under
		// the base output file system path
		File fSubdirectory = new File(strOutputPath + REFERENCE_INDEX_FILE_SYSTEM_SUBDIRECTORY);
		if (!fSubdirectory.exists())
		{
			if (!fSubdirectory.mkdirs())
			{
				throw new Exception("Error creating sub-directory tree for reference list file!");
			}
		}

		String strFileName = strOutputPath + REFERENCE_INDEX_FILE_SYSTEM_SUBDIRECTORY + "\\" + HTMLShared.REFINDEXFILENAME + ".htm";
		m_context.output("Generating Reference List: " + strFileName+ "\n");
		HTMLFormOutput output = new HTMLFormOutput(strFileName);

		// Start document creation
		String strTitle = "Documentation Index";
		output.outputSidebarFrontEnd(strTitle, m_context.getFamily(), m_context.getPersonList(), m_context.getMarriageList());

		// Top of Document Internal Link Tag
		output.output("<A name=InfoTop></A>");
		output.outputCRLF();

		// Top of Page Title
		output.outputSpan(HTMLFormOutput.styleSelectors.E_PageTopHeader, strTitle);
		output.outputBR();
		output.outputCRLF();
		output.output("<hr width=\"100%\">");
		output.outputCRLF();
		
		// Place all references into sortable objects and sort them by place name
		ArrayList<ReferenceSortableByPlaceName> alReferences = new ArrayList<ReferenceSortableByPlaceName>();
		Iterator<Reference> iter = m_context.getReferenceList().getReferences();
		while (iter.hasNext())
		{
			Reference reference = iter.next();
			alReferences.add(new ReferenceSortableByPlaceName(reference, m_context.getPlaceList()));
		}
		Collections.sort(alReferences);
		String strCurrentPlaceName = "";
		for (int i=0; i<alReferences.size(); i++)
		{
			Reference reference = alReferences.get(i).getReference();
			int iReferenceId = ReferenceHelper.getReferenceId(reference);
			String strThisPlaceName = ReferenceHelper.getCitationPlace(reference, m_context.getPlaceList());
			if (!strThisPlaceName.equals(strCurrentPlaceName))
			{
				output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodySmallHeader, strThisPlaceName);
				output.outputBR(2);
				output.outputCRLF();
				strCurrentPlaceName = strThisPlaceName;
			}
			// Document Title
			String strUrl = m_context.getFamily().getUrlPrefix() + HTMLShared.REFERENCEDIR + "/" + HTMLShared.REFERENCEFILENAME + ReferenceHelper.getReferenceId(reference) + ".htm";
			output.outputStandardBracketedLink(strUrl, "View Document");
			output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodySmallText, ReferenceHelper.getTitle(reference));
			output.outputBR();
			output.outputCRLF();

			// Document Number
			String strWork = "Document Number: " + ReferenceHelper.getReferenceId(reference);
			output.outputSpan(HTMLFormOutput.styleSelectors.E_PageBodySmallText, strWork);
			output.outputBR();
			output.outputCRLF();
			
			// Entry Description List
			int iCount = ReferenceHelper.getEntryCount(reference);
			for (int r=0; r<iCount; r++)
			{
				Entry entry = ReferenceHelper.getEntry(reference, r);
				int iReferenceEntryId = EntryHelper.getEntryId(entry);
				strUrl = m_context.getFamily().getUrlPrefix() + HTMLShared.REFERENCEDIR + "/" + HTMLShared.REFERENCEFILENAME + iReferenceId + ".htm#REF" + iReferenceId + "ENT" + iReferenceEntryId;
				output.outputStandardBracketedLink(strUrl, "View Entry");
				int iTitleParagraphCount = EntryHelper.getTitleParagraphCount(entry);
				for (int t=0; t<iTitleParagraphCount; t++)
				{
					Paragraph paragraph = EntryHelper.getTitleParagraph(entry, t);					
					String strParagraph = HTMLShared.buildParagraphString(m_context, paragraph,
                            m_indexMarrToSpouses,
                            true,
                            true,
                            null,
                            iReferenceId,
                            iReferenceEntryId);
					output.output(strParagraph);
					output.outputCRLF();
				}
			}
			output.outputBR();
		}
		output.outputSidebarBackEnd();
		output.commit();
		output = null;
		m_context.output("Completed Generating All Reference List File\n");	
	}
}
