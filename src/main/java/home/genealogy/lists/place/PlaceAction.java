package home.genealogy.lists.place;

import home.genealogy.GenealogyContext;
import home.genealogy.lists.MarriageList;
import home.genealogy.lists.PersonList;
import home.genealogy.lists.PhotoList;
import home.genealogy.lists.PlaceList;
import home.genealogy.lists.ReferenceList;
import home.genealogy.output.IOutputStream;

public interface PlaceAction
{
	public void execute(GenealogyContext context)
		throws PlaceActionException;
	
	public boolean placeListModified();
	public boolean personListModified();
	public boolean marriageListModified();
	public boolean referenceListModified();
	public boolean photoListModified();
}
