package pub.pdsd.fbfriendtracker.Model;

import java.util.ArrayList;
import java.util.List;

public class UserLocationsData {

	public UserLocationsData(){
		locations = new ArrayList<LocationData>();
	}
	
	private String uid;
	public String getUserId(){
		return uid;
	}
	public void setUserId(String val){
		uid = val;
	}
	
	private List<LocationData> locations;
	public List<LocationData> getLocations(){
		return locations;
	}
	public void addLocation(LocationData loc){
		locations.add(loc);
	}
}
