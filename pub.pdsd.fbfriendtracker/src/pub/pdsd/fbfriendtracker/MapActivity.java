package pub.pdsd.fbfriendtracker;

import pub.pdsd.fbfriendtracker.Model.LocationData;
import pub.pdsd.fbfriendtracker.Model.UserData;
import pub.pdsd.fbfriendtracker.Model.UserLocationsData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends FragmentActivity{
	
	private UserData friend;
	private UserLocationsData friendCheckins;
	private ImageView friendAvatar;
	private TextView friendName;
	
	public MapActivity(){
		
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_layout);
		
		Intent intent = getIntent();
		Bundle params = intent.getBundleExtra("params");
		double[] lat = (double[]) params.get("lat");
		double[] lng = (double[]) params.get("lng");
		String name = params.getString("name");
		Bitmap bIcon = (Bitmap) params.get("avatar");
		String[] places = params.getStringArray("places");
		
		friendAvatar = (ImageView) findViewById(R.id.mapHeaderImage);
		friendName = (TextView) findViewById(R.id.mapHeaderName);
		
		friendAvatar.setImageBitmap(bIcon);
		friendName.setText(name);
		
		MapFragment fragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
		GoogleMap map = fragment.getMap();
		
		for (int i = 0; i< lat.length; i++)
		{
			LatLng position = new LatLng(lat[i], lng[i]);
			Marker firstMarker = map.addMarker(new MarkerOptions().position(position).title(places[i]));
		}	
		
	}

}
