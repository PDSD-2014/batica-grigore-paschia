package pub.pdsd.fbfriendtracker.Model;

public class UserData {

	private String uid;
	private String name;
	private String profilePictureUrl;
	
	public UserData(String userID, String userName){
		this.uid = new String(userID);
		this.name = new String(userName);
		this.profilePictureUrl = String.format("http://graph.facebook.com/%s/picture", userID);
	}
	
	public String getName(){
		return this.name;
	}
	
	public String getUid(){
		return this.uid;
	}
	
	public String getProfilePictureUrl(){
		return this.profilePictureUrl;
	}
}
