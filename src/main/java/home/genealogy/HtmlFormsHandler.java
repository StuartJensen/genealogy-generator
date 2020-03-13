package home.genealogy;

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

public class HtmlFormsHandler
{
	private GenealogyContext m_context;
	
	public HtmlFormsHandler(GenealogyContext context)
	{
		m_context = context;
	}
	
	public void execute()
		throws Exception
	{
		CommandLineParameters clp = m_context.getCommandLineParameters();
		IndexMarriageToSpouses idxMarrToSpouses = new IndexMarriageToSpouses(m_context.getFamily(), m_context.getMarriageList());
		IndexPersonToMarriages idxPerToMar = new IndexPersonToMarriages(m_context.getFamily(), m_context.getMarriageList());
		IndexMarriageToChildren idxMarToChild = new IndexMarriageToChildren(m_context.getFamily(), clp, m_context.getPersonList());
		RelationshipManager.setRelationships(m_context, idxPerToMar, idxMarToChild);
		
		if ((clp.isHtmlFormTargetPersonList()) ||
			clp.isHtmlFormTargetAll())
		{
			HTMLPersonListForm form = new HTMLPersonListForm(m_context);
			form.create();
		}
		if ((clp.isHtmlFormTargetPersonInfo()) ||
			clp.isHtmlFormTargetAll())
		{
			HTMLPersonInfoForm form = new HTMLPersonInfoForm(m_context);
			form.create();
		}
		if ((clp.isHtmlFormTargetReferenceList()) ||
			clp.isHtmlFormTargetAll())
		{
			HTMLReferenceListForm form = new HTMLReferenceListForm(m_context, idxMarrToSpouses);
			form.create();
		}
		if ((clp.isHtmlFormTargetPhotos()) ||
			clp.isHtmlFormTargetAll())
		{
			HTMLPhotoForm form = new HTMLPhotoForm(m_context, idxMarrToSpouses);
			form.create();
		}
		if ((clp.isHtmlFormTargetPhotoList()) ||
			clp.isHtmlFormTargetAll())
		{
			HTMLPhotoListForm form = new HTMLPhotoListForm(m_context, idxMarrToSpouses);
			form.create();
		}
		if ((clp.isHtmlFormTargetReferences()) ||
			clp.isHtmlFormTargetAll())
		{
			HTMLReferenceForm form = new HTMLReferenceForm(m_context);
			form.create();
		}
		if ((clp.isHtmlFormTargetFGS()) ||
			clp.isHtmlFormTargetAll())
		{
			HTMLFGSForm form = new HTMLFGSForm(m_context);
			form.create();
		}
	}
}
