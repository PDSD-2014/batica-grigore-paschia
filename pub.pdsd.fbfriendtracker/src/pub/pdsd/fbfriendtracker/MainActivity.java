package pub.pdsd.fbfriendtracker;



import java.util.List;






import com.facebook.UiLifecycleHelper;






import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import pub.pdsd.fbfriendtracker.R;
import pub.pdsd.fbfriendtracker.Facebook.FacebookManager;
import pub.pdsd.fbfriendtracker.Facebook.FacebookOperationsDelegate;
import pub.pdsd.fbfriendtracker.Facebook.Utils;
import pub.pdsd.fbfriendtracker.Model.UserData;
import pub.pdsd.fbfriendtracker.Model.UserLocationsData;
import pub.pdsd.fbfriendtracker.Utils.AvatarFetcher;
import pub.pdsd.fbfriendtracker.Utils.AvatarFetcherDelegate;

public class MainActivity extends Activity 
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
		setContentView(R.layout.activity_main);

		mBtnLogInFb = (Button)findViewById(R.id.btnLoginFb);
		mBtnLogOutFb = (Button)findViewById(R.id.btnLogoutFb);
		mBtnGetFriends = (Button)findViewById(R.id.btnGetFriends);
		mBtnGetCheckins = (Button)findViewById(R.id.btnGetCheckins);
		mBtnLogInFb.setOnClickListener(this);
		mBtnLogOutFb.setOnClickListener(this);
		mBtnGetFriends.setOnClickListener(this);
		mBtnGetCheckins.setOnClickListener(this);
		
		mImgView = (ImageView)findViewById(R.id.imageView);
		
		mTxtStatus = (TextView)findViewById(R.id.txtLoginStat);

		FacebookManager.Init(this, this);
		Utils.PrintHash(this);
		
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
			mTxtStatus.setText(String.format("User: %s", me.getName()));			
		} else {
			mTxtStatus.setText("Not logged in");
		}
		
		mBtnLogInFb.setEnabled(!success);
		mBtnLogOutFb.setEnabled(success);

	}
	
	@Override
	public void onFetchUsersFinishedCallback(List<UserData> friends, boolean success) {
		
	}
	
	UserData []users;
	@Override
	public void onGetFriendsCheckinsFinishedCallback(List<UserLocationsData> checkIns) {
		//ArrayList<UserData> users = new ArrayList<UserData>();
		
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
		mImgView.setImageBitmap(users[idx].getAvatar());
	}



	@Override
	public void onAvatarsDwFinishedCallback(int result) {

	}
}
