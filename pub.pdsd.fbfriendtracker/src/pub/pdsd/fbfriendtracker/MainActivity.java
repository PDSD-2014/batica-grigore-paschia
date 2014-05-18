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
	
	ImageView	mImgView;
	
	UiLifecycleHelper uiHelper;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	//	setContentView(R.layout.map_layout);

		mBtnLogInFb = (Button)findViewById(R.id.btnLoginFb);
		mBtnLogOutFb = (Button)findViewById(R.id.btnLogoutFb);
		mBtnGetFriends = (Button)findViewById(R.id.btnGetFriends);
		mBtnGetCheckins = (Button)findViewById(R.id.btnGetCheckins);
		mLstFriends = (TableLayout)findViewById(R.id.tableFriends);
		
		mBtnLogInFb.setOnClickListener(this);
		mBtnLogOutFb.setOnClickListener(this);
		mBtnGetFriends.setOnClickListener(this);
		mBtnGetCheckins.setOnClickListener(this);
	//	mLstFriends.setVisibility(View.INVISIBLE);
		
	//mImgView = (ImageView)findViewById(R.id.imageView);
		
		mTxtStatus = (TextView)findViewById(R.id.txtLoginStat);

		FacebookManager.Init(this, this);
		Utils.PrintHash(this);
		mBtnGetFriends.setClickable(false);
		mBtnGetCheckins.setClickable(false);
		//FacebookManager.Login();
	}

	

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btnLoginFb:
			Log.d ("PDSD", "loginFb tapped");
			FacebookManager.Login();
			//mBtnGetFriends.setClickable(true);
			break;
		case R.id.btnLogoutFb:
			Log.d ("PDSD", "logoutFb tapped");
			FacebookManager.Logout();
			break;
		case R.id.btnGetFriends:
			Log.d ("PDSD", "fetch Friends tapped");
		//	mBtnGetCheckins.setVisibility(View.INVISIBLE);
			FacebookManager.FetchFriends();
			break;
		case R.id.btnGetCheckins:
			Log.d ("PDSD", "getCheckins tapped");
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
			//FacebookManager.GetFriendsCheckins();
			mBtnGetFriends.setClickable(true);
			mBtnGetCheckins.setClickable(true);
		} else {
			mTxtStatus.setText("Not logged in");
			mBtnGetFriends.setClickable(false);
			mBtnGetCheckins.setClickable(false);
		}
		
		//mBtnLogInFb.setEnabled(!success);
		//mBtnLogOutFb.setEnabled(success);

	}
	
	@Override
	public void onFetchUsersFinishedCallback(List<UserData> friends, boolean success) {
		if (success)
		{
		Log.d ("odsd", "success getting bfriends");
		//mLstFriends.setVisibility(View.VISIBLE);
		int listLength = friends.size();
		Log.d ("odsd", "success getting bfriends length " + listLength);
		TableRow row;
		//Item[] iconArray = serv.getIcons();
		//TableLayout table = (TableLayout) myView.findViewById(R.id.table1);
		 for (int current = 0; current < listLength; current++)
	        {
	            // Create a TableRow and give it an ID
	            TableRow tr = new TableRow(this);
	            tr.setId(100+current);
	            //new LayoutParams(
             //   LayoutParams.FILL_PARENT,
           //     LayoutParams.WRAP_CONTENT)); 
	            tr.setLayoutParams( new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)); 

	            // Create a TextView to house the name of the province
	            TextView labelTV = new TextView(this);
	            labelTV.setId(200+current);
	            labelTV.setText(friends.get(current).getName());
	            labelTV.setTextColor(Color.BLACK);
	            labelTV.setLayoutParams(new LayoutParams(
	                    LayoutParams.WRAP_CONTENT,
	                    LayoutParams.WRAP_CONTENT));
	            tr.addView(labelTV);

	            // Create a TextView to house the value of the after-tax income
	    Log.d("PDSD","Add"); 

	            // Add the TableRow to the TableLayout
	            mLstFriends.addView(tr, new TableLayout.LayoutParams(
	                    LayoutParams.FILL_PARENT,
	                    LayoutParams.WRAP_CONTENT));
	        
	

	        }
		    //ImageView imageicon = (ImageView) v.findViewById(R.id.myImg);
		  //  imageicon.setOnClickListener(new OnClickListener() {
		    //    public void onClick(View v) {
		            //Do some stuff
		     //   }
		   // });
		   // Drawable drawicon = getResources().getDrawable(icon);
		    //imageicon.setBackgroundDrawable(drawicon);
		   
		
		
		}
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
