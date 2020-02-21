package home.genealogy.forms.html;

public class HTMLParagraphFormat
{
	private String m_strParagraphTextSpan;
	private String m_strEditorCommentSpan;
	private String m_strFormTextSpan;
	private String m_strPersonIdSpan;
	private String m_strMarriageIdSpan;
	private String m_strReferenceIdSpan;
	private String m_strPhotoIdSpan;
	
	public HTMLParagraphFormat()
	{
		setAll("pageBodyNormalText");
	}

	public HTMLParagraphFormat(String str)
	{
		setAll(str);
	}

	private void setAll(String str)
	{
		m_strParagraphTextSpan = str;
		m_strEditorCommentSpan= str;
		m_strFormTextSpan= str;
		m_strPersonIdSpan= str;
		m_strMarriageIdSpan= str;
		m_strReferenceIdSpan= str;
		m_strPhotoIdSpan= str;
	}

	public void setParagraphTextSpan(String str){m_strParagraphTextSpan = str;}
	public void setEditorCommentSpan(String str){m_strEditorCommentSpan = str;}
	public void setFormTextSpan(String str){m_strFormTextSpan = str;}
	public void setPersonIdSpan(String str){m_strPersonIdSpan = str;}
	public void setMarriageIdSpan(String str){m_strMarriageIdSpan = str;}
	public void setReferenceIdSpan(String str){m_strReferenceIdSpan = str;}
	public void setPhotoIdSpan(String str){m_strPhotoIdSpan = str;}

	public String getParagraphTextSpan(){return m_strParagraphTextSpan;}
	public String getEditorCommentSpan(){return m_strEditorCommentSpan;}
	public String getFormTextSpan(){return m_strFormTextSpan;}
	public String getPersonIdSpan(){return m_strPersonIdSpan;}
	public String getMarriageIdSpan(){return m_strMarriageIdSpan;}
	public String getReferenceIdSpan(){return m_strReferenceIdSpan;}
	public String getPhotoIdSpan(){return m_strPhotoIdSpan;}
}
