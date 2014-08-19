package com.litoralesential;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import com.litoralesential.adapter.ItemListBaseAdapter;

public class ObjectiveList extends Fragment
{
    ListView listView;
	MainActivity mActivity;
    ArrayList<Objective> chosenObjectives;
    String[] displayObjectives;
    public boolean objectifeChosen;

	public static ObjectiveList newInstance()
    {
		ObjectiveList objective = new ObjectiveList();
		return objective;
	}

	public void onCreate (Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		mActivity = (MainActivity)getActivity();
        objectifeChosen = false;
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
        objectifeChosen = false;
        View rootView = null;
        rootView = inflater.inflate(R.layout.fragment_general, container, false);

        listView = (ListView) rootView.findViewById(R.id.list);
        if(listView != null && chosenObjectives.size() > 0)
        {
            listView.setAdapter(new ItemListBaseAdapter(mActivity, chosenObjectives));

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> a, View v, int position, long id)
                {
                    if (mActivity != null)
                    {
                        ObjectiveDetails objectiveFragment = ObjectiveDetails.newInstance(chosenObjectives.get(position));
                        FragmentManager fragmentManager = mActivity.getFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.frame_container, objectiveFragment).commit();
                        objectifeChosen = true;
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

        /*
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
                        ObjectiveDetails objectiveFragment = ObjectiveDetails.newInstance(chosenObjectives.get(position));
                        FragmentManager fragmentManager = mActivity.getFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.frame_container, objectiveFragment).commit();
                        objectifeChosen = true;
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
        */

        return rootView;
    }
}