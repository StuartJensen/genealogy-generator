package home.genealogy.schema.all.helpers;

import home.genealogy.schema.all.Event;
import home.genealogy.schema.all.EventGroup;

import java.util.List;

public class EventGroupHelper
{
	public static int getEventCount(EventGroup eventGroup)
	{
		if (null != eventGroup)
		{
			List<Event> lEvents = eventGroup.getEvent();
			if (null != lEvents)
			{
				return lEvents.size();
			}
		}
		return 0;
	}
	
	public static Event getEvent(EventGroup eventGroup, int iNth)
	{
		if (null != eventGroup)
		{
			List<Event> lEvents = eventGroup.getEvent();
			if ((null != lEvents) && (lEvents.size()> iNth))
			{
				return lEvents.get(iNth);
			}
		}
		return null;
	}
}
