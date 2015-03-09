package com.example.adnfield360demo.MyAsync;

import java.util.List;

import com.example.adnfield360demo.R;
import com.example.adnfield360demo.Helper.GlobalHelper;
import com.example.adnfield360demo.MyActivity.AttachmentActivity;
import com.example.adnfield360demo.MyActivity.LoginActivity;
import com.example.adnfield360demo.MyDatabase.FieldDatabase;
import com.example.adnfield360demo.MyServices.ServicesClass;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

//task of login
public class AsyncTaskForLogin extends AsyncTask<List<String>, Void, Void>  {

	//login activity
	public LoginActivity _activity;
	//indicate whether the task completed
	private Boolean _isOK = false;
	
	private String _username;
	private String _password;
	
	//ini progress dialog
	private ProgressDialog progress;
	  public AsyncTaskForLogin(ProgressDialog progress) {
		    this.progress = progress;
		  }

		  public void onPreExecute() {
		    progress.show();
		  } 
		  
		  // task completed
		  public void onPostExecute(Void unused) { 
		    progress.dismiss();
		    
		    
		    if(_isOK)
		    {
		    	// show msg of succeess
		    	 Toast.makeText(
						  _activity.getApplicationContext(),
						  "Login Succeeded!",
	 					Toast.LENGTH_LONG).show(); 
		    	 
		    	 //record this successful login
		    	 //for inputing user name and password next time 
		    	 FieldDatabase.update_Item_AccountTable(GlobalHelper._myDBHelper, 
		    			 _username,
		    			 _password);
	        	 
		    	 
		    	 //tell the main activity to refresh
		    	 _activity.setResult(20, null);
		    }
		    else
		    {
		    	// show msg of failure
		    	 Toast.makeText(
						  _activity.getApplicationContext(),
						  "Failed to login",
	 					Toast.LENGTH_LONG).show();
		    }
		    
		    // end login activity
		    //return to main activity
		   
		    _activity.finish();
		  }

		@Override
		protected Void doInBackground(List<String>... params) {
			// TODO Auto-generated method stub
			

			// call service of login
			// input user name and password
			 if(ServicesClass.field_login(
					 params[0].get(0),   // user name
					 params[0].get(1) ))  // password
			 {
				 _username = params[0].get(0);
						 _password = params[0].get(1);
				 _isOK = true;
			 }
			 else
			 {
				 _isOK = false;
			 }
			 
			return null;
		} 

}
