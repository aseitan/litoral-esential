package com.litoralesential;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ObjectiveList extends Fragment
{
    ListView listView;
    List<Map<String, String>> planetsList = new ArrayList<Map<String,String>>();
    MainActivity mActivity;
    ArrayList<Objective> chosenObjectives;
    String[] displayObjectives;
    Fragment mFragment;
    private boolean showingList;

    public ObjectiveList(){super();mFragment = this;showingList = false;}
    public ObjectiveList(Activity a)
    {
        super();
        mFragment = this;
        mActivity = (MainActivity)a;
        showingList = true;
        chosenObjectives = new ArrayList<Objective>();
    }
    public void SetChosenObjectives(ArrayList<Objective> co)
    {
        chosenObjectives = co;
        if(chosenObjectives.size() > 0)
        {
            displayObjectives = new String[chosenObjectives.size()];
            for(int i=0; i<chosenObjectives.size(); i++)
            {
                displayObjectives[i] = chosenObjectives.get(i).name;
            }
        }
        else
        {
            displayObjectives = new String[0];
        }
    }

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{

        View rootView = null;
        rootView = inflater.inflate(R.layout.fragment_general, container, false);
        listView = (ListView) rootView.findViewById(R.id.list);

        if(listView != null && displayObjectives.length > 0)
        {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(mActivity, android.R.layout.simple_list_item_1, android.R.id.text1, displayObjectives);

            // Assign adapter to ListView
            listView.setAdapter(adapter);

            // ListView Item Click Listener
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                {
                    if (mActivity != null)
                    {
                        ObjectiveDetails obiectivFragment = new ObjectiveDetails(chosenObjectives.get(position), mActivity);
                        FragmentManager fragmentManager = mActivity.getFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.frame_container, obiectivFragment).commit();
                    }
                }
            });
        }
        else
        {
            TextView tv = (TextView) rootView.findViewById(R.id.txtLabel);
            if(tv != null)
            {
                tv.setText("Nu exista obiective in aceasta categorie.");
            }
        }
        return rootView;
    }
}