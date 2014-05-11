package pub.pdsd.fbfriendtracker.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;








import android.util.Log;
import pub.pdsd.fbfriendtracker.Model.LocationData;
import pub.pdsd.fbfriendtracker.Model.UserLocationsData;

public class DataConvertor {

	private static final String TAG = "DataConvertor";

	@SuppressWarnings("finally")
	public static UserLocationsData ConvertJSONToUserLocationData(JSONObject jsonObj) {

		UserLocationsData userData = new UserLocationsData();

		if(null != jsonObj){
			try {
				String userId = jsonObj.getString("id");
				String userName = jsonObj.getString("name");
				userData.setUserData(userId, userName);

				if(jsonObj.has("locations")) {
					JSONArray jsonDataArray = jsonObj.getJSONObject("locations").getJSONArray("data");

					for(int i = 0; i < jsonDataArray.length(); i++) {
						JSONObject jLocation = jsonDataArray.getJSONObject(i);
						userData.addLocation(ConvertJSONToLocationData(jLocation));
					}
				} else {
					//TODO need to free locations
				}

			} catch (JSONException e) {

				Log.e(TAG, String.format("ConvertJSONToUserLocationData error parsing %s", e.toString()));
			} finally {
				return userData;
			}
		}
		return null;
	}

	@SuppressWarnings("finally")
	public static LocationData ConvertJSONToLocationData(JSONObject jsonObj) {

		LocationData locData = new LocationData();
		
		try {
			locData.setLocationId(jsonObj.getString("id"));
			JSONObject jPlace = jsonObj.getJSONObject("place");
			
			locData.setPlaceId(jPlace.getString("id"));
			locData.setPlaceName(jPlace.getString("name"));
			
			JSONObject jLoc = jPlace.getJSONObject("location");
			locData.setCity(jLoc.getString("city"));
			locData.setCountry(jLoc.getString("country"));
			locData.setLatitude(jLoc.getDouble("latitude"));
			locData.setLongitude(jLoc.getDouble("longitude"));
			locData.setStreet(jLoc.getString("street"));
			locData.setZip(jLoc.getString("zip"));
			
		} catch (JSONException e) {
			
		} finally {
			return locData;
		}
		
		//return null;
	}
}
