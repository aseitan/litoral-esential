package com.litoralesential;

import com.litoralesential.adapter.NavDrawerListAdapter;
import com.litoralesential.model.NavDrawerItem;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity 
{
    private static MainActivity mActivity = null;
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

    private ArrayAdapter<String> listAdapter ;
	// nav drawer title
	private CharSequence mDrawerTitle;

    public ProgressDialog pDialog;

	// used to store app title
	private CharSequence mTitle;

	// slide menu items
	private String[] navMenuTitles;

	private ArrayList<NavDrawerItem> navDrawerItems;
	private NavDrawerListAdapter adapter;
    public DataManager dataManager;

	@Override
	protected void onCreate(Bundle savedInstanceState)
    {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

        mActivity = this;
        dataManager = new DataManager(mActivity, getFilesDir());
        if(dataManager.updateFromLocalFile() == 1)
        {
            //we have a valid load
            mTitle = mDrawerTitle = getTitle();

            ArrayList<Category> categories = dataManager.GetLocalCategories();
            String menuTitles [] = new String[categories.size()];
            for(int i=0; i<categories.size(); i++)
            {
                menuTitles[i] = categories.get(i).name;
            }

            navMenuTitles = menuTitles;

            mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

            navDrawerItems = new ArrayList<NavDrawerItem>();

            for(int i=0; i<navMenuTitles.length; i++)
            {
                navDrawerItems.add(new NavDrawerItem(navMenuTitles[i]));
            }

            mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

            // setting the nav drawer list adapter
            adapter = new NavDrawerListAdapter(getApplicationContext(), navDrawerItems);
            mDrawerList.setAdapter(adapter);

            // enabling action bar app icon and behaving it as toggle button
            getActionBar().setDisplayHomeAsUpEnabled(true);
            getActionBar().setHomeButtonEnabled(true);

            mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                    R.drawable.ic_drawer, //nav menu toggle icon
                    R.string.app_name, // nav drawer open - description for accessibility
                    R.string.app_name // nav drawer close - description for accessibility
            )
            {
                public void onDrawerClosed(View view)
                {
                    getActionBar().setTitle(mTitle);
                    // calling onPrepareOptionsMenu() to show action bar icons
                    invalidateOptionsMenu();
                }

                public void onDrawerOpened(View drawerView)
                {
                    getActionBar().setTitle(mDrawerTitle);
                    // calling onPrepareOptionsMenu() to hide action bar icons
                    invalidateOptionsMenu();
                }
            };
            mDrawerLayout.setDrawerListener(mDrawerToggle);

            if (savedInstanceState == null)
            {
                // on first time display view for first nav item
                Toast.makeText(mActivity.getApplicationContext(), "Pentru a accesa meniul, trageti dinspre stanga!", Toast.LENGTH_LONG).show();
                displayView(0);
            }

            dataManager.fillServerArraysFromServer();
        }
        else
        {
            //we show a loading and we wait...eh, kind of
            dataManager.fillServerArraysFromServer();
        }
    }

    public void reload()
    {
        Intent intent = getIntent();
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();

        overridePendingTransition(0, 0);
        startActivity(intent);
    }

	/**
	 * Slide menu item click listener
	 * */
	private class SlideMenuClickListener implements ListView.OnItemClickListener
    {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
			// display view for selected nav drawer item
			displayView(position);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
    {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
    {
		// toggle nav drawer on selecting action bar app icon/title
		if (mDrawerToggle.onOptionsItemSelected(item))
        {
			return true;
		}
		// Handle action bar actions click
		switch (item.getItemId())
        {
		case R.id.action_settings:
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/* *
	 * Called when invalidateOptionsMenu() is triggered
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu)
    {
		// if nav drawer is opened, hide the action items
        if(mDrawerLayout != null)
        {
            boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
            menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
            return super.onPrepareOptionsMenu(menu);
        }
        return false;
	}

	/**
	 * Diplaying fragment view for selected nav drawer list item
	 * */
	private void displayView(int position)
    {
		// update the main content by replacing fragments
        ObjectiveList fragment = null;
        fragment = new ObjectiveList(mActivity);

        //navMenuTitles[position]

		if (fragment != null && dataManager != null)
        {
            if(dataManager.GetLocalCategories() != null)
            {
                if( position < dataManager.GetLocalCategories().size())
                {
                    Category cat = dataManager.GetLocalCategories().get(position);
                    ArrayList<Objective> objectives = dataManager.GetLocalObjectives();
                    ArrayList<Objective> chosenOnes = new ArrayList<Objective>();
                    for(int i=0; i<objectives.size(); i++)
                    {
                        if(objectives.get(i).categoryID == cat.id)
                        {
                            chosenOnes.add(objectives.get(i));
                        }
                    }
                    fragment.SetChosenObjectives(chosenOnes);
                }
            }

			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();

			// update selected item and title, then close the drawer
			mDrawerList.setItemChecked(position, true);
			mDrawerList.setSelection(position);
			setTitle(navMenuTitles[position]);
			mDrawerLayout.closeDrawer(mDrawerList);
		}
        else
        {
			// error in creating fragment
			Log.e("MainActivity", "Error in creating fragment");
		}
	}

	@Override
	public void setTitle(CharSequence title)
    {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState)
    {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		if(mDrawerToggle != null)mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

}
