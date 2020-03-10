package home.genealogy.lists.place;

public abstract class PlaceActionBase
	implements PlaceAction
{
	private boolean m_placeListModified;
	private boolean m_personListModified;
	private boolean m_marriageListModified;
	private boolean m_referenceListModified;
	private boolean m_photoListModified;
	
	public void markPlaceListModified()
	{
		m_placeListModified = true;
	}
	
	public boolean placeListModified()
	{
		return m_placeListModified;
	}
	
	public void markPersonListModified()
	{
		m_personListModified = true;
	}
	
	public boolean personListModified()
	{
		return m_personListModified;
	}
	
	public void markMarriageListModified()
	{
		m_marriageListModified = true;
	}
	
	public boolean marriageListModified()
	{
		return m_marriageListModified;
	}

	public void markReferenceListModified()
	{
		m_referenceListModified = true;
	}
	
	public boolean referenceListModified()
	{
		return m_referenceListModified;
	}
	
	public void markPhotoListModified()
	{
		m_photoListModified = true;
	}
	
	public boolean photoListModified()
	{
		return m_photoListModified;
	}
	
}
