package com.litoralesential;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.litoralesential.adapter.NavDrawerListAdapter;
import com.litoralesential.model.NavDrawerItem;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends Activity 
{
    private static MainActivity mActivity = null;
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
    private int chosenID;
    private ObjectiveList fragment;

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

        chosenID = -1;

        CreateAppDirectory();
		DetectScreenSize();

        mActivity = this;
        dataManager = new DataManager(mActivity, getFilesDir());

		dataManager.fillServerArraysFromServer();
    }

	public void InitUI()
	{
		//we have a valid load
		mTitle = mDrawerTitle = getTitle();

		ArrayList<Category> categories = dataManager.GetLocalCategories();
		navMenuTitles = new String[categories.size()];

		navDrawerItems = new ArrayList<NavDrawerItem>();

		String categoryName;
		int categoryID;
		for(int i=0; i < categories.size(); i++)
		{
			categoryName = categories.get(i).name;
			categoryID = categories.get(i).id;
			navDrawerItems.add(new NavDrawerItem(categoryID, categoryName));

			navMenuTitles[i] = categoryName;
		}

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

		// setting the nav drawer list adapter
		adapter = new NavDrawerListAdapter(mActivity, navDrawerItems);
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

        displayView(0);
        Toast.makeText(mActivity.getApplicationContext(), "Pentru a accesa meniul, trageti dinspre stanga!", Toast.LENGTH_LONG).show();
	}

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            if(chosenID != -1 && fragment.objectifeChosen)
            {
                displayView(chosenID);
            }
            else
            {
                return super.onKeyDown(keyCode, event);
            }
        }
        else
        {
            super.onKeyDown(keyCode, event);
        }

        return false;
    }


    private void CreateAppDirectory()
    {
        String strPackageName = this.getClass().getPackage().getName();
        try
        {
            android.content.pm.PackageManager manager = this.getPackageManager();
            android.content.pm.PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            strPackageName = info.packageName;
        }
        catch(Exception e){}

        String externalPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        String externalPathRoot = externalPath + File.separator + "Android" + File.separator + "data" + File.separator + strPackageName;
        Utils.externalPathRoot = externalPathRoot;

        File rootDir;
        try
        {
            rootDir = new File(externalPathRoot);
            boolean res;
            int cnt = 0;

            do
            {
                res = rootDir.mkdirs();
                ++cnt;
            }
            while (!res && cnt < 3);
        }
        catch(Exception ex){}
        finally
        {
            rootDir = null;
        }
    }

	private void DetectScreenSize()
	{
		Utils.SCREEN_SIZE = getResources().getConfiguration().screenLayout &
				Configuration.SCREENLAYOUT_SIZE_MASK;

		switch (Utils.SCREEN_SIZE){
			case Configuration.SCREENLAYOUT_SIZE_SMALL:
				Utils.OBJECTIVE_IMAGES_URL = "http://www.reporterntv.ro/obiective/%d/small.jpg";
				break;
			case Configuration.SCREENLAYOUT_SIZE_NORMAL:
				Utils.OBJECTIVE_IMAGES_URL = "http://www.reporterntv.ro/obiective/%d/normal.jpg";
				break;
			case Configuration.SCREENLAYOUT_SIZE_LARGE:
				Utils.OBJECTIVE_IMAGES_URL = "http://www.reporterntv.ro/obiective/%d/large.jpg";
				break;
			case Configuration.SCREENLAYOUT_SIZE_XLARGE:
				Utils.OBJECTIVE_IMAGES_URL = "http://www.reporterntv.ro/obiective/%d/extralarge.jpg";
				break;
			default:
				Utils.OBJECTIVE_IMAGES_URL = "http://www.reporterntv.ro/obiective/%d/normal.jpg";
				break;
		}
	}

    public void RefreshCategoryAdapter()
	{
		if(adapter != null)
        {
			adapter.notifyDataSetChanged();
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

        fragment = ObjectiveList.newInstance();

        //navMenuTitles[position]

        chosenID = position;

		if (dataManager != null)
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
