package home.genealogy.lists;

import home.genealogy.schema.all.Photo;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class PhotoListIterator implements Iterator<Photo>
{
	private Photo[] m_arPhotoList;
	private int m_iCursor;
	
	PhotoListIterator(Photo[] arPhotoList)
	{
		m_iCursor = 0;
		m_arPhotoList = arPhotoList;
		advance();
	}
	
	public boolean hasNext()
	{
		if (m_iCursor < m_arPhotoList.length)
		{	// Cursor still within range of list. By virtue of how the next()
			// function is implemented, this means there is a next element.
			return true;
		}
		return false;
	}
	
	public Photo next()
	{
		if (m_iCursor >= m_arPhotoList.length)
		{	// Cursor out of range of list
			throw new NoSuchElementException();
		}
		Photo photo = m_arPhotoList[m_iCursor];
		// Find the next valid (non-null) entry and set the cursor to point there
		m_iCursor++;
		advance();
		return photo;
	}
	
	public void remove()
	{
		throw new UnsupportedOperationException("Functionality not supported!");
	}
	
	private void advance()
	{
		while (m_iCursor < m_arPhotoList.length)
		{
			if (null != m_arPhotoList[m_iCursor])
			{	// The cursor now points at the next entry
				break;
			}
			m_iCursor++;
		}
	}
}
