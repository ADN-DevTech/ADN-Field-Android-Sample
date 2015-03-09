package com.example.adnfield360demo.MyAsync;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.adnfield360demo.Helper.GlobalHelper;
import com.example.adnfield360demo.Helper.IssueListView;
import com.example.adnfield360demo.R;
import com.example.adnfield360demo.MyActivity.MainActivity;
import com.example.adnfield360demo.MyServices.ServicesClass;

import com.example.adnfield360demo.MyAdapter.AdapterForIssueList;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

//task for get issue
public class AsyncTaskForGetIssue extends AsyncTask<List<String>, Void, Void>{

	public MainActivity  _activity;
	 
	private ProgressDialog progress;
	  public AsyncTaskForGetIssue(ProgressDialog progress) {
		    this.progress = progress;
		  }

		  public void onPreExecute() {
		    progress.show();
		  } 
		  
		  public void onPostExecute(Void unused) { 
		    progress.dismiss();
		    
		  
		   // view_msg_main.setText("Project:" + GlobalHelper.oCurrentPorjName);
			
			//issueItems.clear();
			
		    ListView listView = (ListView)_activity.findViewById(R.id.listView1);  	 
		    
			 List<Map<String,Object>> listMap  = new  ArrayList<Map<String,Object>>();
		 
			
			SQLiteDatabase db = GlobalHelper._myDBHelper.getReadableDatabase();		
			 Cursor c = db.rawQuery("SELECT * FROM fieldIssues",null);
			 
		        while (c.moveToNext()) { 
		        	
		        	
		            String issueDesc= c.getString(c.getColumnIndex("issuedesc"));
		            String issueId = c.getString(c.getColumnIndex("issueid"));
		            String issueStatus = c.getString(c.getColumnIndex("status"));
		            String attids = c.getString(c.getColumnIndex("attids"));
		            //issueItems.add(new IssueListView(issueDesc, issueId)); 
					 
		            
		            Map<String,Object> map = new HashMap<String,Object>();			            
		            if ( issueStatus.equalsIgnoreCase("Draft"))
		            {
		            	map.put("imagestatus", R.drawable.ic_draft);						         
		            }
		            else if(issueStatus.equalsIgnoreCase("Open")){
		            	map.put("imagestatus", R.drawable.ic_open);						        
		            }
		            else if(issueStatus.equalsIgnoreCase("Work Completed")){
		            	map.put("imagestatus", R.drawable.ic_work_com);						        
		            }
		            else if(issueStatus.equalsIgnoreCase("Ready to Inspect")){
		            	map.put("imagestatus", R.drawable.ic_inspector);	
		            }
		            else if(issueStatus.equalsIgnoreCase("Not Approved")){
		            	map.put("imagestatus", R.drawable.ic_not_appro);	
		            }
		            else if(issueStatus.equalsIgnoreCase("In Dispute")){
		            	map.put("imagestatus", R.drawable.ic_dispute);	
		            }
		            else if(issueStatus.equalsIgnoreCase("Closed")){
		            	map.put("imagestatus", R.drawable.ic_closed);	
		            }
		            else
		            {				            	
		            } 
		            map.put("issuedesc", issueDesc);
		            
		            if(attids.length()>0)
		            {
		            	  map.put("imageattach", R.drawable.ic_attachment);
		            }				             
		           
		            map.put("id", issueId);
		            listMap.add(map);
		        
		            
		        }  
		        c.close();  
		    	db.close();  
			  
			
		    	AdapterForIssueList sa = 
		    			new AdapterForIssueList(_activity, 
		    					R.layout.listview_template, 
		    					listMap);
				listView.setAdapter(sa);  
				
				_activity.setTitle(_activity.getString(R.string.title_main_activity) + "		" +
						  GlobalHelper.oCurrentPorjName );
			 
		  }
	@Override
	protected Void doInBackground(List<String>... params) {
		// TODO Auto-generated method stub
		
		//call service of field issues
		if(ServicesClass.field_issues())
		{
			
		}
		else
		{
			Toast.makeText(
					_activity.getApplicationContext(),
					"Failed to get issue list!",
					Toast.LENGTH_LONG).show();
		}
		
		return null;
	}

}
