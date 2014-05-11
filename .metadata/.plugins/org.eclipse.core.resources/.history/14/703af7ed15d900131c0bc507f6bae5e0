package pub.pdsd.fbfriendtracker.Facebook;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pub.pdsd.fbfriendtracker.Model.UserData;
import pub.pdsd.fbfriendtracker.Model.UserLocationsData;
import pub.pdsd.fbfriendtracker.Utils.DataConvertor;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;


public class FacebookManager {

	/* Used to announce the caller*/
	private static FacebookOperationsDelegate sDelegate;
	private static Activity sActivity;
	private static boolean sbIsEnabled = false;

	private static final String TAG = "FacebookManager";

	private static UserData me; 
	
	public static void Init(Activity activity,FacebookOperationsDelegate delegate){
		sDelegate = delegate;
		sActivity = activity;

		sbIsEnabled = true;
	}

	public static void onActivityResult(int requestCode, int resultCode, Intent data) {
	    Session session = Session.getActiveSession();
	    if (session != null && !session.isOpened()) {
	        session.onActivityResult(sActivity, requestCode, resultCode, data);
	    }
	}
	
	public static void Login(){

		if(!sbIsEnabled){
			Log.e(TAG, "Manager not initialized");
		}

		Log.d(TAG, "performFacebookLogin");
		final Session.NewPermissionsRequest newPermissionsRequest = new Session.NewPermissionsRequest(sActivity, Arrays.asList("friends_checkins"));
		Session.openActiveSession(sActivity, true, new Session.StatusCallback()
		{


			@Override
			public void call(Session session, SessionState state, Exception exception) {
				Log.d(TAG, "call");
				boolean isFetching = false;
				if (session.isOpened() && !isFetching)
				{
					Log.d(TAG, "if (session.isOpened() && !isFetching)");
					isFetching = true;
					session.requestNewReadPermissions(newPermissionsRequest);
					Request getMe = Request.newMeRequest(session, new Request.GraphUserCallback() {

						@Override
						public void onCompleted(GraphUser user, Response response) {

							Log.d("FACEBOOK", "onCompleted");
							if (user != null)
							{
								Log.d("FACEBOOK", "user != null");
								org.json.JSONObject graphResponse = response.getGraphObject().getInnerJSONObject();
								String email = graphResponse.optString("email");
								String id = graphResponse.optString("id");
								String facebookName = user.getUsername();

								me = new UserData(id, facebookName);
								sDelegate.onLoggedInFinishedCallback(me, true);

								if (email == null || email.length() < 0)
								{    

								}

							}
							else 
							{
								sDelegate.onLoggedInFinishedCallback(null, false);
							}

						}
					});

					getMe.executeAsync();
				}
				else
				{
					if (!session.isOpened())
						Log.d(TAG, "!session.isOpened()");
					else
						Log.d(TAG, "isFetching");
					
					sDelegate.onLoggedInFinishedCallback(null, false);

				}

			}
		});
	}
	
	public static void Logout(){
		if(!sbIsEnabled){
			Log.e(TAG, "Manager not initialized");
		}

		Session activeSession = Session.getActiveSession();

		activeSession.closeAndClearTokenInformation();
	}

	public static void FetchFriends(){
		if(!sbIsEnabled){
			Log.e(TAG, "Manager not initialized");
		}
		
		Session session = Session.getActiveSession();
		Log.d(TAG, "fetching friends");
		Request req = Request.newMyFriendsRequest(session, new Request.GraphUserListCallback() {
			
			@Override
			public void onCompleted(List<GraphUser> users, Response response) {
				if(null == users) {
					Log.d(TAG, "friends fetched:FALSE");
					sDelegate.onFetchUsersFinishedCallback(null, false);					
				} else {
					List<UserData> friends = new ArrayList<UserData>();					
					for(GraphUser user : users){
						friends.add(new UserData(user.getId(), user.getName()));
					}
					Log.d(TAG, "friends fetched:SUCCESS");
					sDelegate.onFetchUsersFinishedCallback(friends, true);
					
				}
			}
		});
		
		req.executeAsync();
	}
	
	public static void GetFriendsCheckins(){
		if(!sbIsEnabled){
			Log.e(TAG, "Manager not initialized");
		}
	
		Session session = Session.getActiveSession();
		Log.d(TAG, "fetching friends");
		
		
		Bundle params = new Bundle();	      
	    params.putString("access_token", session.getAccessToken());
	    params.putString("fields", "locations");
		
		Request req = new Request(session, String.format("%s/friends", me.getUid()), params, HttpMethod.GET);
		req.setCallback( new Request.Callback() {
			
			@Override
			public void onCompleted(Response response) {
				
				Log.d(TAG, "feched locations");
				ArrayList<UserLocationsData> usersWithCheckins = null;
				
				if(null == response.getError()){
					
					JSONObject jsonObject = response.getGraphObject().getInnerJSONObject();					
					usersWithCheckins = new ArrayList<UserLocationsData>();
					
			        try {
			        	JSONArray jsonDataArray = jsonObject.getJSONArray("data");
			        	
			        	
			        	for(int i = 0; i < jsonDataArray.length(); i++) {			        		
			        		usersWithCheckins.add(DataConvertor.ConvertJSONToUserLocationData(jsonDataArray.getJSONObject(i)));
			        	}
					} catch (JSONException e) {
						
						
					}
				}
				sDelegate.onGetFriendsCheckinsFinishedCallback(usersWithCheckins);
			}
		});
		
		req.executeAsync();

	}
}
