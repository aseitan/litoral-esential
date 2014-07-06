package com.litoralesential;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class ObjectiveDetails extends Fragment
{
    Objective chosenObjective;
    private MapView mMapView;
    private GoogleMap mMap;
    private Bundle mBundle;
    static final LatLng HAMBURG = new LatLng(53.558, 9.927);
    static final LatLng KIEL = new LatLng(53.551, 9.993);

    private Activity mActivity;

    public ObjectiveDetails()
    {
        super();
        chosenObjective = null;
    }
    public ObjectiveDetails(Objective obj, Activity m)
    {
        super();
        chosenObjective = obj;
        mActivity = m;
    }

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
        View rootView = null;

        try
        {
            rootView = inflater.inflate(R.layout.fragment_obiectiv_map_complete, container, false);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        try
        {
            MapsInitializer.initialize(getActivity());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        if(rootView != null)
        {
            mMapView = (MapView) rootView.findViewById(R.id.map);
            if(mMapView != null)
            {
                mMapView.onCreate(mBundle);

                //TODO: load the actual position
                mMapView.getMap().addMarker(new MarkerOptions().position(HAMBURG).title("Hamburg"));
                mMapView.getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(HAMBURG, 15));
                mMapView.getMap().animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
                //mMapView.setLayoutParams(new com.google.android.gms.maps.MapView.LayoutParams(500,200));
                //mMapView.setMinimumHeight(500);
            }


            ImageView objectiveImage = (ImageView) rootView.findViewById(R.id.imageObjective);
            if(objectiveImage != null)
            {
                //TODO: load the actual position
            }

            TextView objectiveName = (TextView) rootView.findViewById(R.id.objectiveName);
            if(objectiveName != null)
            {
                if(chosenObjective!= null && chosenObjective.name.length() > 0)
                    objectiveName.setText(chosenObjective.name);
                else
                    objectiveName.setText("-");
            }

            TextView objectiveDescription = (TextView) rootView.findViewById(R.id.objectiveAddress);
            if(objectiveDescription != null)
            {
                if(chosenObjective != null && chosenObjective.description.length() > 0)
                    objectiveDescription.setText(chosenObjective.description);
                else
                    objectiveDescription.setText("");
            }

            TextView objectivePhone = (TextView) rootView.findViewById(R.id.objectivePhone);
            if(objectivePhone != null)
            {
                if(chosenObjective != null && chosenObjective.telephone.length() > 0)
                    objectivePhone.setText(chosenObjective.telephone);
                else
                    objectivePhone.setText("-");
            }

            TextView objectiveWebsite = (TextView) rootView.findViewById(R.id.objectiveWebsite);
            if(objectiveWebsite != null)
            {
                if(chosenObjective != null && chosenObjective.website.length() > 0)
                    objectiveWebsite.setText(chosenObjective.website);
                else
                    objectiveWebsite.setText("-");
            }
        }

        return rootView;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy()
    {
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
    }
}

