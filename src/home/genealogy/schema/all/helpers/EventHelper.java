package home.genealogy.schema.all.helpers;

import home.genealogy.schema.all.Event;
import home.genealogy.schema.all.EventDescription;
import home.genealogy.schema.all.EventTitle;
import home.genealogy.schema.all.Paragraph;

public class EventHelper
{
	private Event m_event;
	private boolean m_bIsPersonLiving;
	private boolean m_bSuppressLiving;
	
	public EventHelper(Event event, boolean bIsPersonLiving, boolean bSuppressLiving)
	{
		m_event = event;
		m_bIsPersonLiving = bIsPersonLiving;
		m_bSuppressLiving = bSuppressLiving;
	}
	
	public String getEventDate()
	{
		String strEventDate = getEventDate(m_event);
		if (m_bSuppressLiving && m_bIsPersonLiving)
		{
			if (0 != strEventDate.length())
			{
				return PersonHelper.LIVING;
			}
		}
		return strEventDate;
	}
	
	public String getEventPlace()
	{
		String strEventPlace = getEventPlace(m_event);
		if (m_bSuppressLiving && m_bIsPersonLiving)
		{
			if (0 != strEventPlace.length())
			{
				return PersonHelper.LIVING;
			}
		}
		return strEventPlace;
	}
	
	public String getEventTag()
	{
		String strEventTag = getEventTag(m_event);
		if (m_bSuppressLiving && m_bIsPersonLiving)
		{
			if (0 != strEventTag.length())
			{
				return PersonHelper.LIVING;
			}
		}
		return strEventTag;
	}
	
	public Paragraph getEventTitleParagraph()
	{
		Paragraph paraTitle = getEventTitleParagraph(m_event);
		if (m_bSuppressLiving && m_bIsPersonLiving)
		{
			return null;
		}
		return paraTitle;
	}
	
	public int getEventDescriptionParagraphCount()
	{
		int iParaCount = getEventDescriptionParagraphCount(m_event);
		if (m_bSuppressLiving && m_bIsPersonLiving)
		{
			return 0;
		}
		return iParaCount;
	}
	
	public Paragraph getEventDescriptionParagraph(int iNth)
	{
		Paragraph para = getEventDescriptionParagraph(m_event, iNth);
		if (m_bSuppressLiving && m_bIsPersonLiving)
		{
			return null;
		}
		return para;
	}
	
	
	////////////////////////////////////
	/////// Static methods
	
	public static String getEventDate(Event event)
	{
		if (null != event)
		{
			return DateHelper.getDate(event.getDate());
		}
		return "";
	}
	
	public static String getEventPlace(Event event)
	{
		if (null != event)
		{
			return PlaceHelper.getPlace(event.getPlace());
		}
		return "";
	}
	
	public static String getEventTag(Event event)
	{
		if (null != event)
		{
			if (null != event.getTag())
			{
				return event.getTag();
			}
		}
		return "";
	}
	
	public static Paragraph getEventTitleParagraph(Event event)
	{
		if (null != event)
		{
			EventTitle eventTitle = event.getEventTitle();
			if (null != eventTitle)
			{
				return EventTitleHelper.getEventTitleParagraph(eventTitle);
			}
		}
		return null;
	}
	
	public static int getEventDescriptionParagraphCount(Event event)
	{
		if (null != event)
		{
			EventDescription eventDescription = event.getEventDescription();
			if (null != eventDescription)
			{
				return EventDescriptionHelper.getEventDescriptionParagraphCount(eventDescription);
			}
		}
		return 0;
	}
	
	public static Paragraph getEventDescriptionParagraph(Event event, int iNth)
	{
		if (null != event)
		{
			EventDescription eventDescription = event.getEventDescription();
			if (null != eventDescription)
			{
				return EventDescriptionHelper.getEventDescriptionParagraph(eventDescription, iNth);
			}
		}
		return null;
	}
}
