package pub.pdsd.fbfriendtracker.Model;

import java.util.ArrayList;
import java.util.List;

public class UserLocationsData {

	public UserLocationsData(){
		locations = new ArrayList<LocationData>();
	}
	
	
	private UserData user;
	public UserData getUser() {
		return user;
	}
	
	public void setUserData(String userID, String userName){
		user = new UserData(userID, userName);
	}
	
	private List<LocationData> locations;
	public List<LocationData> getLocations(){
		return locations;
	}
	public void addLocation(LocationData loc){
		locations.add(loc);
	}
}
