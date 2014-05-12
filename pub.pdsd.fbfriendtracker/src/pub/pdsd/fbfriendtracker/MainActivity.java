package pub.pdsd.fbfriendtracker;



import java.util.ArrayList;
import java.util.List;

import pub.pdsd.fbfriendtracker.Facebook.FacebookManager;
import pub.pdsd.fbfriendtracker.Facebook.FacebookOperationsDelegate;
import pub.pdsd.fbfriendtracker.Facebook.Utils;
import pub.pdsd.fbfriendtracker.Model.LocationData;
import pub.pdsd.fbfriendtracker.Model.UserData;
import pub.pdsd.fbfriendtracker.Model.UserLocationsData;
import pub.pdsd.fbfriendtracker.Utils.AvatarFetcher;
import pub.pdsd.fbfriendtracker.Utils.AvatarFetcherDelegate;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.UiLifecycleHelper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends FragmentActivity 
						  implements FacebookOperationsDelegate, View.OnClickListener,
										AvatarFetcherDelegate	{

	private final String TAG = "MainActivity";
	
	/* Controlls */
	TextView 	mTxtStatus;
	Button 		mBtnLogInFb;
	Button 		mBtnLogOutFb;
	Button		mBtnGetFriends;
	Button		mBtnGetCheckins;
	
	ImageView	mImgView;
	
	UiLifecycleHelper uiHelper;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_main);
		setContentView(R.layout.map_layout);

//		mBtnLogInFb = (Button)findViewById(R.id.btnLoginFb);
//		mBtnLogOutFb = (Button)findViewById(R.id.btnLogoutFb);
//		mBtnGetFriends = (Button)findViewById(R.id.btnGetFriends);
//		mBtnGetCheckins = (Button)findViewById(R.id.btnGetCheckins);
//		mBtnLogInFb.setOnClickListener(this);
//		mBtnLogOutFb.setOnClickListener(this);
//		mBtnGetFriends.setOnClickListener(this);
//		mBtnGetCheckins.setOnClickListener(this);
//		
//		mImgView = (ImageView)findViewById(R.id.imageView);
//		
//		mTxtStatus = (TextView)findViewById(R.id.txtLoginStat);

		FacebookManager.Init(this, this);
		Utils.PrintHash(this);
		FacebookManager.Login();
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
			FacebookManager.FetchFriends();
			break;
		case R.id.btnGetCheckins:
			FacebookManager.GetFriendsCheckins();
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
			//mTxtStatus.setText(String.format("User: %s", me.getName()));
			FacebookManager.GetFriendsCheckins();
		} else {
			//mTxtStatus.setText("Not logged in");
		}
		
		//mBtnLogInFb.setEnabled(!success);
		//mBtnLogOutFb.setEnabled(success);

	}
	
	@Override
	public void onFetchUsersFinishedCallback(List<UserData> friends, boolean success) {
		
	}
	
	UserData []users;
	List<UserLocationsData> mCheckIns;
	@Override
	public void onGetFriendsCheckinsFinishedCallback(List<UserLocationsData> checkIns) {
		//ArrayList<UserData> users = new ArrayList<UserData>();
		mCheckIns = checkIns;
		users = new UserData[checkIns.size()]; 
		
		for(int i = 0; i < checkIns.size(); i++) {
			users[i] = checkIns.get(i).getUser();
			
			
		}
		
		AvatarFetcher imgDw = new AvatarFetcher(this);
		imgDw.execute(users);
	}


	//////////////////////////////////////////////////////////////////////////
	// AvatarFetcherDelegate implementation
	
	@Override
	public void onSingleAvatarDwFinishedCallback(int idx) {
		//mImgView.setImageBitmap(users[idx].getAvatar());
		

		MapFragment frag = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
		GoogleMap map = frag.getMap();
		
		
		if (mCheckIns.get(idx).getLocations().size() != 0)
		{
			LocationData userLocation = mCheckIns.get(idx).getLocations().get(0);
			
			double lat = userLocation.getLatitude();
			double lng = userLocation.getLongitude();
			LatLng position = new LatLng(lat, lng);//44.4453545719, 26.055957504);
			Bitmap icon = users[idx].getAvatar();//(Bitmap) BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
			Marker firstMarker = map.addMarker(new MarkerOptions().position(position).title(userLocation.getPlaceName()).icon(BitmapDescriptorFactory.fromBitmap(Bitmap.createScaledBitmap(icon, 120, 120, false))));
					
		}
		
		//map.moveCamera( CameraUpdateFactory.newLatLngZoom(position , 14.0f)); 
	}



	@Override
	public void onAvatarsDwFinishedCallback(int result) {

	}
}
