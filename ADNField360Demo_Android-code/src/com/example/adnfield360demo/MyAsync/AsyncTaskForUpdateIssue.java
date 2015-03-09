package com.example.adnfield360demo.MyAsync;

import java.util.List;

import com.example.adnfield360demo.MyActivity.MainActivity;
import com.example.adnfield360demo.MyServices.ServicesClass;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

//task for update issue
public class AsyncTaskForUpdateIssue extends AsyncTask<Void, Void, Void> {

	public MainActivity _activity;
	private Boolean _isOK = false;
	
	private ProgressDialog progress;
	  public AsyncTaskForUpdateIssue(ProgressDialog progress) {
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
	  					"Update Issues to Field Succeeded!",
	 					Toast.LENGTH_LONG).show();
		    }
		    else
		    {
		    	 Toast.makeText(
						  _activity.getApplicationContext(),
	  					"Failed to update issues to Field!",
	 					Toast.LENGTH_LONG).show();
		    }
		   
		  }
	@Override
	protected Void doInBackground(Void... params) {
		// TODO Auto-generated method stub
		
		 if(ServicesClass.update_issues())
		 {
			 _isOK = true;
		 }
		 else
		 {
			 _isOK = false;
			 
		 }
		 
		 
		return null;
	}

}
