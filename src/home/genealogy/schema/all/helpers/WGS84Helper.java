package home.genealogy.schema.all.helpers;

import home.genealogy.schema.all.Latitude;
import home.genealogy.schema.all.Longitude;
import home.genealogy.schema.all.WGS84;

public class WGS84Helper
{
	public static String getCoordinates(WGS84 gc)
	{
		StringBuffer sb = new StringBuffer(256);
		Latitude lat = gc.getLatitude();
		if ((null != lat) && (!LatitudeHelper.isEmpty(lat)))
		{
			 sb.append(LatitudeHelper.getHemisphere(lat));
			 sb.append(" ");
			 sb.append(LatitudeHelper.getDegrees(lat));
			 sb.append(" ");
			 sb.append(LatitudeHelper.getMinutes(lat));
		}
		Longitude lng = gc.getLongitude();
		if ((null != lng) && (!LongitudeHelper.isEmpty(lng)))
		{
		    if (0 != sb.length())
			{
				sb.append(" ");
			}
			sb.append(LongitudeHelper.getHemisphere(lng));
			sb.append(" ");
			sb.append(LongitudeHelper.getDegrees(lng));
			sb.append(" ");
			sb.append(LongitudeHelper.getMinutes(lng));
		}
		return sb.toString();
	}
}
