package com.ecs.mapview.sample;

import java.util.ArrayList;
import java.util.List;

import android.app.LocalActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class MapViewSample extends MapActivity implements OnTabChangeListener {

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    	
        final Drawable drawable;
        final MapView mapView;
        final MapController mc;
    	
        drawable = getResources().getDrawable(R.drawable.pin);
        mapView = (MapView) findViewById(R.id.mapview1);
        mapView.setBuiltInZoomControls(true);
        mc = mapView.getController();
        mapView.invalidate();      
         
        final List<Overlay> mapOverlays = mapView.getOverlays();
        final MyItemizedOverlay itemizedOverlay = new MyItemizedOverlay(drawable);
        final Button btnSearch = (Button)findViewById(R.id.btn_search);

        final EditText longitudeText = (EditText) findViewById(R.id.longitude);
        final EditText latitudeText = (EditText) findViewById(R.id.latitude);
        
        
        final TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);
        
        LocalActivityManager mLocalActivityManager = new LocalActivityManager(this, false);
        tabHost.setup(mLocalActivityManager);
        tabHost.setOnTabChangedListener(this);

        TabSpec spec = tabHost.newTabSpec("tab1").setIndicator("Location Input").setContent(R.id.test);
        tabHost.addTab(spec);
        
        spec = tabHost.newTabSpec("tab2").setIndicator("Empty Tab").setContent(R.id.test2);
        tabHost.addTab(spec);     

        spec = tabHost.newTabSpec("tab3").setIndicator("Map Overlay").setContent(R.id.test3);
        tabHost.addTab(spec);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mapOverlays.clear();
                itemizedOverlay.clear();
                double longitude = Double.parseDouble(longitudeText.getText().toString());
                double latitude = Double.parseDouble(latitudeText.getText().toString());
                GeoPoint p = null;
                for (int i=1 ; i<10 ; i++) {
                    p = new GeoPoint((int)((latitude + i) * 1E6), (int)((longitude +i) * 1E6));
                    OverlayItem overlayitem = new OverlayItem(p, "", "");
                    itemizedOverlay.addOverlay(overlayitem);
                }
                mapOverlays.add(itemizedOverlay);
                
                mc.animateTo(p);
                mc.setCenter(p);
                mapView.invalidate();    
                tabHost.setCurrentTab(2);
            }
        });

    }
	
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
	
	public class MyItemizedOverlay extends ItemizedOverlay<OverlayItem>{

		private List<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
		private Context mContext;

		public MyItemizedOverlay(Drawable defaultMarker) {
		    super(boundCenterBottom(defaultMarker));        
		}

		public MyItemizedOverlay(Drawable defaultMarker, Context mContext) {
		    super(defaultMarker);
		    this.mContext = mContext;
		}

		@Override
		protected OverlayItem createItem(int i) {
		    return mOverlays.get(i);
		}

		public void addOverlay(OverlayItem overlay) {
		    mOverlays.add(overlay);
		    populate();
		}

		public void clear() {
		    mOverlays.clear();
		}

		@Override
		public int size() {
		    return mOverlays.size();
		}
	}

	@Override
	public void onTabChanged(String tabId) {
	}
}