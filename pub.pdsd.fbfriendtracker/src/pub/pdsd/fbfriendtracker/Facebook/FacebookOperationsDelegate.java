package pub.pdsd.fbfriendtracker.Facebook;

import java.util.List;

import pub.pdsd.fbfriendtracker.Model.UserData;
import pub.pdsd.fbfriendtracker.Model.UserLocationsData;

public interface FacebookOperationsDelegate {

	public void onLoggedInFinishedCallback(UserData me, boolean success);	
	public void onFetchUsersFinishedCallback(List<UserData> friends, boolean success);
	public void onGetFriendsCheckinsFinishedCallback(List<UserLocationsData> checkIns);
}
