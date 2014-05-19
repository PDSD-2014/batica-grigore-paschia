package pub.pdsd.fbfriendtracker;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class AllFriendsMapActivity extends FragmentActivity{

	public AllFriendsMapActivity(){
		
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.all_friends_map);
		
		Intent intent = getIntent();
		Bundle params = intent.getBundleExtra("params");
		double[] lat = (double[]) params.get("lat");
		double[] lng = (double[]) params.get("lng");
	
		Parcelable[] bIcon = (Parcelable[]) params.get("avatars");
		
		MapFragment fragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map_all_friends);
		GoogleMap map = fragment.getMap();
		
		for (int i = 0; i< lat.length; i++)
		{
			LatLng position = new LatLng(lat[i], lng[i]);
			Marker firstMarker = map.addMarker(new MarkerOptions().position(position).icon(BitmapDescriptorFactory.fromBitmap(Bitmap.createScaledBitmap((Bitmap) bIcon[i], 50, 50, false))));
		}	
		
	}

}
