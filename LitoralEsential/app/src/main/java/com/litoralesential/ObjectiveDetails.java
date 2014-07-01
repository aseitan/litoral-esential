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

public class ObjectiveDetails extends Fragment
{
    Objective chosenObjective;

    public ObjectiveDetails()
    {
        super();
        chosenObjective = null;
    }
    public ObjectiveDetails(Objective obj)
    {
        super();
        chosenObjective = obj;
    }

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
        View rootView = null;

        rootView = inflater.inflate(R.layout.fragment_obiectiv, container, false);

        if(rootView != null)
        {
            TextView tv = (TextView) rootView.findViewById(R.id.txtLabel);
            if(tv != null)
            {
                /*
                if(chosenObjective!= null && chosenObjective.description.length() > 0)
                    tv.setText(chosenObjective.description);
                else
                    tv.setText("Descriere indisponibila.");
                */
            }
            //adi todo: add downloaded image


        }

        return rootView;
    }
}