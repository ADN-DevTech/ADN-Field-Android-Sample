package com.example.adnfield360demo.MyAsync;

import java.util.List;

import com.example.adnfield360demo.Helper.GlobalHelper;
import com.example.adnfield360demo.R;
import com.example.adnfield360demo.MyActivity.LoginActivity;
import com.example.adnfield360demo.MyActivity.MainActivity;
import com.example.adnfield360demo.MyServices.ServicesClass;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

//task for get project
public class AsyncTaskForProject extends AsyncTask<Void, Void, Void>  {

public MainActivity _activity;
private Boolean _isOK = false;
	
	private ProgressDialog progress;
	  public AsyncTaskForProject(ProgressDialog progress) {
		    this.progress = progress;
		  }

		  public void onPreExecute() {
		    progress.show();
		  } 
		  
		  public void onPostExecute(Void unused) { 
		    progress.dismiss(); 
		    
		    if(_isOK)
		    {
		    	 Toast.makeText(
						  _activity.getApplicationContext(),
						  "Get Projects Info Succeeded!",
	 					Toast.LENGTH_LONG).show();
		    	 
		    	 _activity.setTitle(_activity.getString(R.string.title_main_activity) + "	>>		" +
		    			 _activity.getString(R.string.title_ask_sel_proj));
		    }
		    else
		    {
		    	 Toast.makeText(
						  _activity.getApplicationContext(),
						  "Failed to Get Projects Info",
	 					Toast.LENGTH_LONG).show();
		    }
		  }

		  
	@Override
	protected Void doInBackground(Void... arg0) {
		// TODO Auto-generated method stub
		
		 if(ServicesClass.field_projs())
		 {
			 _isOK = true;
		 }
		 else
		 {    
		 }
		 
		return null;
	}
	
	

}
