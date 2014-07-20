package com.litoralesential;
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//IMPORTS
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.litoralesential.downloadable.AppDataDownloadable;
import com.litoralesential.downloadable.CategoryImageDownloadable;
import com.litoralesential.downloadable.Downloadable;
import com.litoralesential.downloadable.HttpMethod;
import com.litoralesential.downloadable.ObjectiveImageDownloadable;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class DataManager
{
//VARIABLES
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private JSONArray categoriesArray = null;
    private JSONArray objectivesArray = null;

    private File mUserFile;

    private ArrayList<Category> localCategories;
    private ArrayList<Objective> localObjectives;
    private ArrayList<Category> serverCategories;
    private ArrayList<Objective> serverObjectives;
    private MainActivity mActivity;
    private byte[] serverResponse = null;
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//Class implementation
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public DataManager(){}
    public DataManager(MainActivity activity, File f)
    {
        mActivity = activity;
        mUserFile = f;
    }
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public ArrayList<Category> GetLocalCategories()
    {
        return localCategories;
    }
    public ArrayList<Objective> GetLocalObjectives()
    {
        return localObjectives;
    }
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void fillLocalArraysFromLocalFile()
    {
        try
        {
            File localFile = new File(mUserFile + File.separator + Utils.userFile);
            if(localFile.exists())
            {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(localFile));
                String read;
                StringBuilder builder = new StringBuilder("");

                while((read = bufferedReader.readLine()) != null)
                {
                    builder.append(read);
                }
                bufferedReader.close();

                if(builder.toString().length() != 0)
                {
                    JSONObject jsonObj = new JSONObject(builder.toString());
                    categoriesArray = jsonObj.getJSONArray(Utils.TAG_CATEGORIES);
                    objectivesArray = jsonObj.getJSONArray(Utils.TAG_OBJECTIVES);
                    localCategories = new ArrayList<Category>();
                    localObjectives = new ArrayList<Objective>();
                    for(int i = 0; i < categoriesArray.length(); i++)
                    {
                        JSONObject c = categoriesArray.getJSONObject(i);

                        String name = c.getString(Utils.TAG_CATEGORY_NAME);
                        String imageURL = c.getString(Utils.TAG_CATEGORY_IMAGEURL);
                        String website = c.getString(Utils.TAG_CATEGORY_WEBSITE);
                        int id = c.getInt(Utils.TAG_CATEGORY_ID);
                        localCategories.add(new Category(id, name, imageURL, website));
                    }
                    for(int i = 0; i < objectivesArray.length(); i++)
                    {
                        JSONObject c = objectivesArray.getJSONObject(i);

                        int id = c.getInt(Utils.TAG_OBJECTIVE_ID);
                        int categoryID = c.getInt(Utils.TAG_OBJECTIVE_CATEGORYID);
                        String name = c.getString(Utils.TAG_OBJECTIVE_NAME);
                        String imageURL = c.getString(Utils.TAG_OBJECTIVE_IMAGEURL);
                        String description = c.getString(Utils.TAG_OBJECTIVE_DESCRIPTION);
                        String telephone = c.getString(Utils.TAG_OBJECTIVE_TELEPHONE);
                        String GPSposition = c.getString(Utils.TAG_OBJECTIVE_GPSPOSITION);
                        String website = c.getString(Utils.TAG_OBJECTIVE_WEBSITE);

                        localObjectives.add(new Objective(id, categoryID, name, imageURL, description, telephone, GPSposition, website));
                    }
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void fillServerArraysFromString(String s )
    {
        if(s != null)
        {
            try
            {
                JSONObject jsonObj = new JSONObject(s);
                categoriesArray = jsonObj.getJSONArray(Utils.TAG_CATEGORIES);
                objectivesArray = jsonObj.getJSONArray(Utils.TAG_OBJECTIVES);
                serverCategories = new ArrayList<Category>();
                serverObjectives = new ArrayList<Objective>();
                for(int i = 0; i < categoriesArray.length(); i++)
                {
                    JSONObject c = categoriesArray.getJSONObject(i);
                    String name = c.getString(Utils.TAG_CATEGORY_NAME);
                    String imageURL = c.getString(Utils.TAG_CATEGORY_IMAGEURL);
                    String website = c.getString(Utils.TAG_CATEGORY_WEBSITE);
                    int id = c.getInt(Utils.TAG_CATEGORY_ID);
                    serverCategories.add(new Category(id, name, imageURL, website));
                }
                for(int i = 0; i < objectivesArray.length(); i++)
                {
                    JSONObject c = objectivesArray.getJSONObject(i);

                    int id = c.getInt(Utils.TAG_OBJECTIVE_ID);
                    int categoryID = c.getInt(Utils.TAG_OBJECTIVE_CATEGORYID);
                    String name = c.getString(Utils.TAG_OBJECTIVE_NAME);
                    String imageURL = c.getString(Utils.TAG_OBJECTIVE_IMAGEURL);
                    String description = c.getString(Utils.TAG_OBJECTIVE_DESCRIPTION);
                    String telephone = c.getString(Utils.TAG_OBJECTIVE_TELEPHONE);
                    String GPSposition = c.getString(Utils.TAG_OBJECTIVE_GPSPOSITION);
                    String website = c.getString(Utils.TAG_OBJECTIVE_WEBSITE);

                    serverObjectives.add(new Objective(id, categoryID, name, imageURL, description, telephone, GPSposition, website));
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void fillServerArraysFromServer()
    {
        AppDataDownloadable dld = new AppDataDownloadable();
        dld.setUrl(Utils.jsonSiteURL);
        new ServiceHandler(true).execute(dld);
    }
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public boolean updateFromLocalFile()
    {
        fillLocalArraysFromLocalFile();
        if( localCategories!=null && localCategories.size() > 0 && localObjectives!=null && localObjectives.size() > 0)
            return true;
        else
            return false;
    }
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private boolean isLocalCategoriesUpdateRequired()
    {
        //checking
        if( serverCategories==null ||  localCategories==null) return true;
        for(int i=0; i < serverCategories.size(); i++)
        {
            boolean found = false;
            for(int j=0; j < localCategories.size(); j++)
            {
                Category serverCategory = serverCategories.get(i);
                Category localCategory = localCategories.get(j);
                if( serverCategory.id == localCategory.id ) found = true;
            }
            if(!found) return true;
        }
        return false;
    }
    private boolean isLocalObjectivesUpdateRequired()
    {
        if(serverObjectives==null || localObjectives==null) return true;
        for(int i=0; i < serverObjectives.size(); i++)
        {
            boolean found = false;
            for(int j=0; j < localObjectives.size(); j++)
            {
                Objective serverObjective = serverObjectives.get(i);
                Objective localObjective = localObjectives.get(j);
                if( serverObjective.id == localObjective.id ) found = true;
            }
            if(!found) return true;
        }
        return false;
    }
////////////////////////////////////////////////////////////////////////////////////////////////////
    private void requestCategoryImages()
    {
        CategoryImageDownloadable []cdldArray = new CategoryImageDownloadable[localCategories.size()];
        boolean hasImages = false;

        for(int i=0; i< localCategories.size(); i++)
        {
            ////////////////////////////////////////////////////////////////////////////////////////
            localCategories.get(i).imageURL = "http://icons.iconarchive.com/icons/cedarseed/patisserie/128/Fondant-au-chocolat-icon.png"; // TEST ONLY
            ////////////////////////////////////////////////////////////////////////////////////////

            if(localCategories.get(i).imageURL.isEmpty())
            {
                continue;
            }
            CategoryImageDownloadable dld = new CategoryImageDownloadable();
            dld.setUrl(localCategories.get(i).imageURL);
            dld.setCategory(localCategories.get(i));
            cdldArray[i] = dld;
            hasImages = true;
        }

        if(hasImages)
        {
            new ServiceHandler(false).execute(cdldArray);
        }
    }
////////////////////////////////////////////////////////////////////////////////////////////////////
	private void requestObjectiveImages()
	{
		ObjectiveImageDownloadable []cdldArray = new ObjectiveImageDownloadable[localObjectives.size()];
		boolean hasImages = false;

		for(int i=0; i< localObjectives.size(); i++)
		{
			////////////////////////////////////////////////////////////////////////////////////////
			//localObjectives.get(i).imageURL = "http://i2.cdn.turner.com/cnn/dam/assets/130530161523-100-beaches-crane-beach-horizontal-gallery.jpg"; // TEST ONLY
			////////////////////////////////////////////////////////////////////////////////////////

			if(localObjectives.get(i).imageURL.isEmpty())
			{
				continue;
			}
			ObjectiveImageDownloadable dld = new ObjectiveImageDownloadable();
			dld.setUrl(localObjectives.get(i).imageURL);
			dld.setObjective(localObjectives.get(i));
			cdldArray[i] = dld;
			hasImages = true;
		}

		if(hasImages)
		{
			new ServiceHandler(false).execute(cdldArray);
		}
	}
////////////////////////////////////////////////////////////////////////////////////////////////////
    private void downloadComplete(Downloadable dld)
    {
        switch (dld.getType())
        {
            case NONE:
                break;
            case APP_DATA:
				boolean hasLocalFile = updateFromLocalFile();

				if(dld.getStatusCode() != 200 && !hasLocalFile)
				{
					//we are fucked, basically
					//could not connect to server and no local file saved
					//this is where we should construct a message for the user that tells him he
					//cannot use the application unless he can connect to server

					return;
				}

				if(dld.getStatusCode() != 200)
				{
					//tell the user somehow the app could not connect to server
					//in the meantime use the local file

					fillLocalArraysFromLocalFile();
					mActivity.InitUI();

					return;
				}

				//at this point we have the server json. Use it if we need an update

                fillServerArraysFromString(new String(dld.getResponseBody()));
                if(isLocalCategoriesUpdateRequired() || isLocalObjectivesUpdateRequired())
                {
                    Utils.writeToFile(dld.getResponseBody(), mUserFile + File.separator + Utils.userFile);
                    fillLocalArraysFromLocalFile();
                    requestCategoryImages();
					requestObjectiveImages();
					mActivity.InitUI();
                    //if(mActivity != null)
                    //    mActivity.reload();
                }
                else
                {
                    if(mActivity != null)
                        mActivity.InitUI();
                }
                break;
            case CATEGORY_IMAGE: {
				if(dld.getStatusCode() != 200)
					return;

				CategoryImageDownloadable cdld = (CategoryImageDownloadable) dld;

				boolean success = Utils.writeToFile(cdld.getResponseBody(), Utils.externalPathRoot +
						File.separator + Utils.CATEGORY_IMAGE_PREFIX + Integer.toString(cdld.getCategory().id));

				if (success) {
					//refresh category list
					mActivity.RefreshCategoryAdapter();
				}

				break;
			}
            case OBJECTIVE_IMAGE: {
				if(dld.getStatusCode() != 200)
					return;

				ObjectiveImageDownloadable cdld = (ObjectiveImageDownloadable) dld;

				boolean success = Utils.writeToFile(cdld.getResponseBody(), Utils.externalPathRoot +
						File.separator + Utils.OBJECTIVE_IMAGE_PREFIX + Integer.toString(cdld.getObjective().id));

				if (success) {
					//refresh category list
					mActivity.RefreshCategoryAdapter();
				}

				break;
			}
        }
    }
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//END
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public class ServiceHandler extends AsyncTask<Downloadable, Void, List<Downloadable>>
    {
        private boolean showWaitDialog = false;

        public ServiceHandler(boolean showWaitDialog){
            this.showWaitDialog = showWaitDialog;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            if(showWaitDialog) {
                if (mActivity != null) {
                    mActivity.pDialog = new ProgressDialog(mActivity);
                    mActivity.pDialog.setMessage("Please wait...");
                    mActivity.pDialog.setCancelable(false);
                    mActivity.pDialog.show();
                }
            }
        }

        @Override
        protected List<Downloadable> doInBackground(Downloadable... args)
        {
            // Creating service handler class instance
            ConnectionManager cm = new ConnectionManager();
            List<Downloadable> dldList = new ArrayList<Downloadable>();

            for(Downloadable dld : args){
                // Making a request to url and getting response
                cm.makeServiceCall(dld);
                dldList.add(dld);
            }

            return dldList;
        }

        @Override
        protected void onPostExecute(List<Downloadable> result)
        {
            super.onPostExecute(result);
            if(showWaitDialog) {
                if (mActivity != null && mActivity.pDialog != null) {
                    if (mActivity.pDialog.isShowing()) {
                        mActivity.pDialog.dismiss();
                    }
                }
            }

            for(Downloadable dld : result)
            {
                //send result
                downloadComplete(dld);
            }
        }
    }

    class ConnectionManager
    {

/*        public String makeServiceCall(String url, int method)
        {
            return this.makeServiceCall(url, method, null);
        }*/

        public byte[] makeServiceCall(Downloadable dld)
        {
            try
            {
                // http client
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpEntity httpEntity;
                HttpResponse httpResponse = null;

                // Checking http request method type
                if (dld.getHttpMethod() == HttpMethod.POST)
                {
                    HttpPost httpPost = new HttpPost(dld.getUrl());
                    // adding post params
                    if (dld.hasParameters())
                    {
                        httpPost.setEntity(dld.getPOSTParams());
                    }
                    httpResponse = httpClient.execute(httpPost);
                }
                else if (dld.getHttpMethod() == HttpMethod.GET)
                {
                    try
                    {
                        String url;
                        // appending params to url
                        if (dld.hasParameters())
                        {
                            url = dld.getUrlWithParams();
                        }
                        else
                        {
                            url = dld.getUrl();
                        }
                        HttpGet httpGet = new HttpGet(url);
                        httpResponse = httpClient.execute(httpGet);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
                try
                {
					if (httpResponse != null) {
						httpEntity = httpResponse.getEntity();
						serverResponse = EntityUtils.toByteArray(httpEntity);
						dld.setStatusCode(httpResponse.getStatusLine().getStatusCode());
						dld.setResponseBody(serverResponse);
					}
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
            catch (UnsupportedEncodingException e)
            {
                e.printStackTrace();
            }
            catch (ClientProtocolException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            return serverResponse;
        }
    }
}