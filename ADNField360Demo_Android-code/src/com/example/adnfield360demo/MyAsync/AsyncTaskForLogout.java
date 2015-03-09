package com.example.adnfield360demo.MyAsync;

import java.util.List;

import com.example.adnfield360demo.Helper.GlobalHelper;
import com.example.adnfield360demo.R;
import com.example.adnfield360demo.MyActivity.LoginActivity;
import com.example.adnfield360demo.MyActivity.MainActivity;
import com.example.adnfield360demo.MyAdapter.AdapterForIssueList;
import com.example.adnfield360demo.MyServices.ServicesClass;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

//Task of logout
public class AsyncTaskForLogout extends AsyncTask<List<String>, Void, Void>  {

	// main activity	
	public MainActivity _activity;
	//indicate whether the task completed
	private Boolean _isOK = false;
	
	private ProgressDialog progress;
	  public AsyncTaskForLogout(ProgressDialog progress) {
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
		    	//
		    	ImageButton btn_Login = 
		    			(ImageButton)_activity.findViewById(R.id.img_btn_login);;
		    	ImageButton btn_Proj =  
		    			(ImageButton)_activity.findViewById(R.id.img_btn_proj);;
		    	ImageButton btn_Filter =  
		    			(ImageButton)_activity.findViewById(R.id.img_btn_filter);;
		    	ImageButton btn_Sync =  
		    			(ImageButton)_activity.findViewById(R.id.img_btn_sync);;
		    	
		    	//change image of login button 
		    	btn_Login.setImageResource(R.drawable.log_in);
		    	
		    	//set title of current status
		    	_activity.setTitle(_activity.getString(R.string.title_main_activity) +"	>>	" + 
						_activity.getString(R.string.title_ask_login));
		    	
		    	//disable project, filter, sync buttons
		    	btn_Proj.setEnabled(false);
				btn_Filter.setEnabled(false);
				btn_Sync.setEnabled(false);
				
				//set ticket empty
		    	GlobalHelper._field_ticket = "";
		    	
		    	 Toast.makeText(
						  _activity.getApplicationContext(),
						  "Logout Succeeded!",
	 					Toast.LENGTH_LONG).show();
		    	
		    	 //clean up listview
		    	 ListView listview = (ListView)_activity.findViewById(R.id.listView1);;
		    	//clean up the list view of previous project
  	        	if(listview.getAdapter() != null)
  	        	{
  	        		AdapterForIssueList oAdapter = 
  	        				(AdapterForIssueList)listview.getAdapter(); 
  	        		oAdapter.lisMap.clear();
  	        		oAdapter.notifyDataSetChanged();
  	        	}     	
		    }
		    else
		    {
		    	 Toast.makeText(
						  _activity.getApplicationContext(),
						  "Failed to logout",
	 					Toast.LENGTH_LONG).show();
		    }
		     
		  }

		  
	@Override
	protected Void doInBackground(List<String>... arg0) {
		// TODO Auto-generated method stub

		//call logout service
		 if(ServicesClass.field_logout())
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
