package pub.pdsd.fbfriendtracker.Model;



public class LocationData {

	private String placeId;
	public void setPlaceId(String val){
		placeId = val;
	}
	public String getPlaceId(){
		return placeId;
	}
	
	private String locationId;
	public void setLocationId(String val){
		locationId = val;
	}
	public String getLocationId(){
		return locationId;
	}
	
	private String placeName;
	public void setPlaceName(String val){
		placeName = val;
	}
	public String getPlaceName(){
		return placeName;
	}
	
	private String city;
	public String getCity() {		
		return city;
	}
	public void setCity(String val) {
		city = val;
	}
	
	private String street;
	public String getStreet() {
		return street;
	}
	public void setStreet(String val) {
		street = val;
	}
	
	private String country;
	public String getCountry() {
		return country;
	}
	public void setCountry(String val) {
		country = val;
	}
	
	private String zip;
	public String getZip() {
		return zip;
	}
	public void setZip(String val) {
		zip = val;
	}
	
	private double latitude;
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double val) {
		latitude = val;
	}
	
	private double longitude;
	public double getLongitude() {
		return longitude;
	}	
	public void setLongitude(double val) {
		longitude = val;
	}
	
}
