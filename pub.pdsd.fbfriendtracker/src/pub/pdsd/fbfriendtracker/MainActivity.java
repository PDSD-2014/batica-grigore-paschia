package pub.pdsd.fbfriendtracker;


import android.app.Activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import pub.pdsd.fbfriendtracker.Facebook.FacebookManager;
import pub.pdsd.fbfriendtracker.Facebook.FacebookOperationsDelegate;
import pub.pdsd.fbfriendtracker.Facebook.Utils;
import pub.pdsd.fbfriendtracker.Model.LocationData;
import pub.pdsd.fbfriendtracker.Model.UserData;
import pub.pdsd.fbfriendtracker.Model.UserLocationsData;
import pub.pdsd.fbfriendtracker.Utils.AvatarFetcher;
import pub.pdsd.fbfriendtracker.Utils.AvatarFetcherDelegate;
import pub.pdsd.fbfriendtracker.R;
import android.content.ClipData.Item;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.facebook.UiLifecycleHelper;
import com.facebook.internal.Utility.FetchedAppSettings;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends FragmentActivity 
						  implements FacebookOperationsDelegate, View.OnClickListener,
										AvatarFetcherDelegate, Serializable	{

	private final String TAG = "MainActivity";
	
	/* Controlls */
	TextView 	mTxtStatus;
	Button 		mBtnLogInFb;
	Button 		mBtnLogOutFb;
	Button		mBtnGetFriends;
	Button		mBtnGetCheckins;
	TableLayout	mLstFriends;
	TableLayout	mLstAvatars;
	ImageView	mImgView;
	
	boolean getFriendsList = false;
	
	UiLifecycleHelper uiHelper;
	
	boolean avatarFetched = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mBtnLogInFb = (Button)findViewById(R.id.btnLoginFb);
		mBtnLogOutFb = (Button)findViewById(R.id.btnLogoutFb);
		mBtnGetFriends = (Button)findViewById(R.id.btnGetFriends);
		mBtnGetCheckins = (Button)findViewById(R.id.btnGetCheckins);
		mLstFriends = (TableLayout)findViewById(R.id.tableFriends);
		//mLstFriends.setBackgroundColor(Color.parseColor("#11000000"));
		mLstFriends.setAlpha(0.7f);
		mBtnLogInFb.setOnClickListener(this);
		mBtnLogOutFb.setOnClickListener(this);
		mBtnGetFriends.setOnClickListener(this);
		mBtnGetCheckins.setOnClickListener(this);
		
		mTxtStatus = (TextView)findViewById(R.id.txtLoginStat);

		FacebookManager.Init(this, this);
		Utils.PrintHash(this);
		mBtnGetFriends.setClickable(false);
		mBtnGetCheckins.setClickable(false);
	}

	

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btnLoginFb:
			FacebookManager.Login();
			break;
		case R.id.btnLogoutFb:
			FacebookManager.Logout();
			break;
		case R.id.btnGetFriends:
		{
			getFriendsList = true;
			mLstFriends.removeAllViews();
			FacebookManager.FetchFriends();
		}
			break;
		case R.id.btnGetCheckins:
		{
			getFriendsList = false;
			FacebookManager.FetchFriends();
			FacebookManager.GetFriendsCheckins();
		}
			break;
		
		default:
			break;
		}
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    FacebookManager.onActivityResult(requestCode, resultCode, data);
	}

	
	//////////////////////////////////////////////////////////////////////////
	// FacebookOperationsDelegate implementation
	
	@Override
	public void onLoggedInFinishedCallback(UserData me, boolean success) {
		if(success){
			mTxtStatus.setText(String.format("User: %s", me.getName()));
			mBtnGetFriends.setClickable(true);
			mBtnGetCheckins.setClickable(true);
		} else {
			mTxtStatus.setText("Not logged in");
			mBtnGetFriends.setClickable(false);
			mBtnGetCheckins.setClickable(false);
		}

	}
	UserData []users;
	@Override
	public void onFetchUsersFinishedCallback(List<UserData> friends, boolean success) {
		
			int listLength = friends.size();
			AvatarFetcher imgDw = new AvatarFetcher(this);
			users = new UserData[listLength]; 
			for(int i = 0; i < listLength; i++) {
				users[i] = friends.get(i);
			}
		
			imgDw.execute (users);
			FacebookManager.GetFriendsCheckins();
	}
	
	
	List<UserLocationsData> mCheckIns;
	@Override
	public void onGetFriendsCheckinsFinishedCallback(List<UserLocationsData> checkIns) {
		mCheckIns = checkIns;
//		users = new UserData[checkIns.size()]; 
//		
//		for(int i = 0; i < checkIns.size(); i++) {
//			users[i] = checkIns.get(i).getUser();
//			
//			
//		}
		
		//AvatarFetcher imgDw = new AvatarFetcher(this);
		//imgDw.execute(users);
		
		if (!getFriendsList){
			Intent myIntent = new Intent(getBaseContext(), AllFriendsMapActivity.class);
			Bundle bundle = new Bundle();
			Bitmap[] avatars = new Bitmap[users.length];
			double[] lat = new double[users.length];
			double[] lng = new double[users.length];
			
			for (int i = 0; i< users.length; i++){
				if ((mCheckIns.get(i).getLocations() != null) && (mCheckIns.get(i).getLocations().size() != 0))
				{
					avatars[i] = users[i].getAvatar();
					lat[i] = mCheckIns.get(i).getLocations().get(0).getLatitude();
					lng[i] = mCheckIns.get(i).getLocations().get(0).getLongitude();
				}
			}
			bundle.putParcelableArray("avatars", avatars);
			bundle.putDoubleArray("lat", lat);
			bundle.putDoubleArray("lng", lng);
			
			myIntent.putExtra("params", bundle);
			startMapActivity(myIntent);
		}
	}


	//////////////////////////////////////////////////////////////////////////
	// AvatarFetcherDelegate implementation
	
	@Override
	public void onSingleAvatarDwFinishedCallback(int idx) {
		if (getFriendsList)
		{
		  TableRow tr = new  TableRow(getApplicationContext());
          
          tr.setLayoutParams( new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)); 
          tr.setBackgroundColor( Color.WHITE);
          
          ImageView avatar = new ImageView(getApplicationContext());
          if (users[idx] != null)
          {
        	  Log.d("pdsd", "users idx is not null" + idx);
        	  avatar.setImageBitmap(users[idx].getAvatar());
        	  TextView friendName = new TextView(getApplicationContext());
              
              friendName.setText(users[idx].getName());
              friendName.setTextColor(Color.BLACK);
              friendName.setTextScaleX(2);
              friendName.setWidth(600);
              friendName.setHeight(100);
          	  
              tr.addView(avatar);
          	  tr.addView(friendName);
          	  tr.setAlpha(1);
              mLstFriends.addView(tr);
              final int index = idx;              
              tr.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent myIntent = new Intent(getBaseContext(), MapActivity.class);
					Bundle bundle = new Bundle();
					bundle.putParcelable("avatar", users[index].getAvatar());
					bundle.putString("name", users[index].getName());
					
					double[] lat = new double[mCheckIns.get(index).getLocations().size()];
					double[] lng = new double[mCheckIns.get(index).getLocations().size()];
					String[] places = new String[mCheckIns.get(index).getLocations().size()];
					
					for (int i = 0; i < mCheckIns.get(index).getLocations().size(); i++){
						lat[i] = mCheckIns.get(index).getLocations().get(i).getLatitude();
						lng[i] = mCheckIns.get(index).getLocations().get(i).getLongitude();
						places[i] = mCheckIns.get(index).getLocations().get(i).getPlaceName();
					}
					
					bundle.putDoubleArray("lat", lat);
					bundle.putDoubleArray("lng", lng);
					bundle.putStringArray("places", places);
					
					myIntent.putExtra("params", bundle);
					startMapActivity(myIntent);
					
					
				}
			});
          }
		}
	}

	private void startMapActivity(Intent intent){
		
		this.startActivity(intent);
	}
	@Override
	public void onAvatarsDwFinishedCallback(int result) {
		avatarFetched = true;
	}
}
