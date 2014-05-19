package pub.pdsd.fbfriendtracker;


import android.app.Activity;

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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.facebook.UiLifecycleHelper;
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
	TableLayout	mLstFriends;
	TableLayout	mLstAvatars;
	ImageView	mImgView;
	
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
			mLstFriends.removeAllViews();
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
	}
	
	
	List<UserLocationsData> mCheckIns;
	@Override
	public void onGetFriendsCheckinsFinishedCallback(List<UserLocationsData> checkIns) {
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
              mLstFriends.addView(tr);
          }
	}



	@Override
	public void onAvatarsDwFinishedCallback(int result) {
		avatarFetched = true;
	}
}
