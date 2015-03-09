package com.example.adnfield360demo.MyActivity;


import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

 
import com.example.adnfield360demo.MyAsync.AsyncTaskForCompany;
import com.example.adnfield360demo.MyAsync.AsyncTaskForGetIssue;
import com.example.adnfield360demo.MyAsync.AsyncTaskForLogin;
import com.example.adnfield360demo.MyAsync.AsyncTaskForLogout;
import com.example.adnfield360demo.MyAsync.AsyncTaskForProject;
import com.example.adnfield360demo.MyAsync.AsyncTaskForUpdateIssue;
import com.example.adnfield360demo.Helper.GlobalHelper;
import com.example.adnfield360demo.Helper.IssueListView;
import com.example.adnfield360demo.Helper.ProjListView;
import com.example.adnfield360demo.R;
import com.example.adnfield360demo.MyServices.ServicesClass;
import com.example.adnfield360demo.R.drawable;
import com.example.adnfield360demo.R.id;
import com.example.adnfield360demo.R.layout;
import com.example.adnfield360demo.R.menu;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

 
 
  
import com.example.adnfield360demo.MyAdapter.AdapterForIssueList;
import com.example.adnfield360demo.MyDatabase.FieldDatabase;

import android.R.string;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

// main activity of this application
// handle the buttons:
// login
// project
// filter
// sync
public class MainActivity extends Activity {

	//list view
	private ListView listView; 

	//login button
	private  ImageButton btn_Login;
	// project button
	private ImageButton btn_Proj;
	//filter button
	private ImageButton btn_Filter;
	//sync button
	private ImageButton btn_Sync;	 
	 
	
	///Create Activity
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
			
		// initialize list view
		iniListView();
		//initialize login button
		iniButtonLogin();
		//initialize project button
		iniButtonProj();
		//initialize filter button
		iniButtonFilter();
		//initialize sync button
		iniButtonSync();
		//initialize global parameters
		iniGlobal();   
	 
		//set title of current status
		this.setTitle(getString(R.string.title_main_activity) + "	>>	"+
				getString(R.string.title_ask_login));
		
		//disable the buttons if the user has not logged in
		btn_Proj.setEnabled(false);
		btn_Filter.setEnabled(false);
		btn_Sync.setEnabled(false);
	} 
		 
	
	void iniListView(){
	
		 // get list view object
		 listView = (ListView)findViewById(R.id.listView1);   
	}
	
	
	//initialize login button
	void iniButtonLogin()
	{ 
		//get login button object
		btn_Login = (ImageButton)findViewById(R.id.img_btn_login);
		
		//login button click
		btn_Login.setOnClickListener(new View.OnClickListener() {
	        public void onClick(View v) { 
	        	
	        	if(GlobalHelper._field_ticket == "")
	    		{
	        		// if has NOT logged in
	        		// start Login activity
	        	    Intent intent = new Intent();			      
			        intent.setClass(MainActivity.this, 
			        				LoginActivity.class);			       
			        MainActivity.this.startActivityForResult(intent, 100);	
	    		}
	        	else
	        	{
	        		//if has logged in, log out
	        		ProgressDialog progress = new ProgressDialog(MainActivity.this);
	            	progress.setMessage(getString(R.string.msg_prog_logout_async));
	            	//start logout task
	            	AsyncTaskForLogout task_logout = new AsyncTaskForLogout(progress);
	            	task_logout._activity= MainActivity.this;
	            	task_logout.execute();  
	        	}   
	          }  //onClick
			}); // setOnClickListener  login button click
	}
	

	// the dialog to choose projects
	void projs_items_dialog()
	{   
		//the map for the list view in this dialog
		List<Map<String,Object>> projs_items_listMap = 
				new  ArrayList<Map<String,Object>>();
		
		//iterate the projects and prepare the images and projects name
		for(ProjListView oEachProj:GlobalHelper.projItems)
		{
			// map object
			Map<String,Object> map = new HashMap<String,Object>();
			//project image
			map.put("image", R.drawable.project);
			//project name
	        map.put("content", oEachProj.proj_name);       
	        projs_items_listMap.add(map);
		} 
		
		//adapter for this list view
		SimpleAdapter sa= new SimpleAdapter(this, 
				projs_items_listMap, 
				R.layout.listview_template,
                new String[] { "image", "content" },
                new int[] {R.id.listview_img1, R.id.listview_txt1 }); 
		
		// build dialog
		// single choice
		new AlertDialog.Builder(MainActivity.this).
		 		setTitle("Select Project").				 
		 		setIcon(R.drawable.ic_launcher).
		 		setSingleChoiceItems(sa,0,
				new DialogInterface.OnClickListener() {
            
            @Override
            public void onClick(DialogInterface dialog, int which) {

            	// when any item is selected
            	ProjListView oSelectItem = 
            			GlobalHelper.projItems.get(which);
            	
            	// if it is the same project which
            	if( GlobalHelper.oCurrentPorjId==
            			oSelectItem.proj_id)
            	{
            		
            		 new  AlertDialog.Builder(MainActivity.this)     
		               .setTitle(R.string.title_main_activity ) 
		               .setMessage(R.string.msg_proj_same_projects)  
		               .show(); 
            	}
            	else
            	{              		
            	    // select a different project
            		
            		//start task of getting company info
            		ProgressDialog progress = 
            				new ProgressDialog(MainActivity.this);
     	        	progress.setMessage(getString(R.string.msg_prog_company_async));
     	        	AsyncTaskForCompany task_company = new AsyncTaskForCompany(progress);
     	        	task_company._context = getApplication();
     	        	task_company.execute();
     	        	
     	        	//clean up the list view of previous project
     	        	if(listView.getAdapter() != null)
     	        	{
     	        		AdapterForIssueList oAdapter = 
     	        				(AdapterForIssueList)listView.getAdapter(); 
     	        		oAdapter.lisMap.clear();
     	        		oAdapter.notifyDataSetChanged();
     	        	}     	        	
            	}
            	
            	//update the project name and id with the info of the selected item
            	GlobalHelper.oCurrentPorjName = oSelectItem.proj_name;            	
            	GlobalHelper.oCurrentPorjId = oSelectItem.proj_id;
            	
            	//set title of current status
            	setTitle(getString(R.string.title_main_activity) + "	>>		"+
            			GlobalHelper.oCurrentPorjName +"	>>		"+
            			getString(R.string.title_ask_sync));
            	
            	//close the dialog
                dialog.dismiss();
            }
        }).show(); 
		 
	}
	
	//initialize project button
	void iniButtonProj()
	{
		btn_Proj = (ImageButton)findViewById(R.id.img_btn_proj);
		//login button click
		btn_Proj.setOnClickListener(new View.OnClickListener() {
	        public void onClick(View v) { 
	        	
	        	if(GlobalHelper._field_ticket =="")
	        	{
	        		 new  AlertDialog.Builder(getApplication())     
	        		               .setTitle(R.string.title_main_activity ) 
	        		               .setMessage(getString(R.string.msg_ask_login) )  
	        		        .show();  

	        	}
	        	else
	        	{
	        		//display dialog of selecting projects
	        		projs_items_dialog();
	        	}
	        	
	        	    	 
	          }  //onClick
			}); // setOnClickListener  project button click	
	}
	
	
	//update list view of issues after filter
	void showIssues(Boolean is_show_all,  
			String comany_name,		
			String issue_type,
			java.util.Date  due_date)
	{
		
		//map for the list view
		 List<Map<String,Object>> listMap  = 
				 new  ArrayList<Map<String,Object>>();
					
		 SQLiteDatabase db = 
				 GlobalHelper._myDBHelper.getReadableDatabase();		 
		 Cursor c = null;
		 if(is_show_all)
			 //select all from database
		   c = db.rawQuery("SELECT * FROM fieldIssues",null);
		 else
		 { 	 
			 //convert common date to sql date
			 java.sql.Date sqlDate=
					 new java.sql.Date(due_date.getTime());
				 
			  c =  db.rawQuery("SELECT * FROM fieldIssues WHERE companyname=? AND issuetype=? AND duedate=?",
					  new String[]{comany_name,issue_type,sqlDate.toString()});
		   
		 }
		
		 // iterate the results of search
        while (c.moveToNext()) 
        {  
        	//description
            String issueDesc= c.getString(c.getColumnIndex("issuedesc"));
            //issue id
            String issueId = c.getString(c.getColumnIndex("issueid"));
            //issue status
            String issueStatus = c.getString(c.getColumnIndex("status"));
            //attachment ids
            String attids = c.getString(c.getColumnIndex("attids")); 
            
            //build each map
            Map<String,Object> map = new HashMap<String,Object>();
            // decide to use which image for the current status
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
          
            //add description
            map.put("issuedesc", issueDesc);
            
            //if the issue has attachments
            if(attids.length()>0)
            {
            	//image for attachment
            	  map.put("imageattach", R.drawable.ic_attachment);
            }			             
           
            //issue id
            map.put("id", issueId);
            
            listMap.add(map);
        
        }
        
        //close database
		c.close();  
		db.close(); 
			  
			
		 // refresh the adapter of the list view
         AdapterForIssueList sa = 
        		 new AdapterForIssueList(
        				 MainActivity.this,
        				 R.layout.listview_template,
        				 listMap);
			listView.setAdapter(sa); 
	}
	
	//ini filter button 
	void iniButtonFilter()
	{
		//get filter button object
		btn_Filter = (ImageButton)findViewById(R.id.img_btn_filter);
		//login button click
		btn_Filter.setOnClickListener(new View.OnClickListener() {
	        public void onClick(View v) {  
	        	
	        	 LayoutInflater inflater = 
	        			 LayoutInflater.from(MainActivity.this);  
	             final View filterV = inflater.inflate(  
	                     R.layout.filter_view, null); 	             
	            
	             //get spinner control of company in the dialog
        		 Spinner sel_company_ini = 
        				 (Spinner)filterV.findViewById(R.id.spin_company);
        		 //build adapter for the spinner control
        		 List theList_company = 
        				 new ArrayList(GlobalHelper.company_name.values());
        		 ArrayAdapter<String> adapter_company = 
        				 new ArrayAdapter<String>(MainActivity.this,
        				 android.R.layout.simple_spinner_item,        				 
        				 theList_company);        		 
        		 adapter_company.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); 
    	    	sel_company_ini.setAdapter(adapter_company);
    	    		
    	    	// get spinner control of issue type in the dialog
        		Spinner sel_issuetype_ini = 
        				(Spinner)filterV.findViewById(R.id.spin_issuetype);
        		
        		
        		// not use at this moment
        		// can ini if needed
        		DatePicker sel_datepicker_ini = 
        				(DatePicker)filterV.findViewById(R.id.date_picker);
        		 
        	    	             
        		 List theList_issuetype = 
        				 new ArrayList(GlobalHelper.issue_type.values());
        		 ArrayAdapter<String> adapter_issuetype = 
        				 new ArrayAdapter<String>(MainActivity.this,
        				 android.R.layout.simple_spinner_item,        				 
        				 theList_issuetype);        		 
        		 adapter_issuetype.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); 
    	    		sel_issuetype_ini.setAdapter(adapter_issuetype);
    	    		
    	    	 //build the filter dialog	
	        	 final AlertDialog.Builder builder = 
	        			 new AlertDialog.Builder(MainActivity.this);	        	 
	             builder.setIcon(R.drawable.ic_launcher);  
	             builder.setTitle(getString(R.string.title_filter_dialog));  
	             builder.setView(filterV);	    
	            
	             builder.setPositiveButton("OK",  
	                     new DialogInterface.OnClickListener() {  
	                         public void onClick(DialogInterface dialog, int whichButton) {
	                        	 
	                        	 // query database to get all issues
	                        	 SQLiteDatabase db = GlobalHelper._myDBHelper.getReadableDatabase();		 
	                    		 Cursor c =   db.rawQuery("SELECT * FROM fieldIssues",null);	                    		 
	                    		 Boolean hasissues =false;
	                    		 if(c.moveToNext())
	                    			 hasissues = true;
	                    		 c.close();
	                    		 db.close();
	                    		 
	                    		 
	                    		 if(!hasissues) //if no issues
	                    			 return;
	                    		 
	                    		 //if [check all] is selected
	                        	 CheckBox chkBox = (CheckBox)((AlertDialog) dialog).
	                        			 findViewById(R.id.chk_show_all);
	                        	 
	                        	 Boolean is_show_all = chkBox.isChecked();
	                        	 if(is_show_all)
	                        	 {
	                        		// read all from database
	                        		 showIssues(true,"","",new java.util.Date());
	                        	 }
	                        	 else
	                        	 {
	                        	    //filter out of database
	                        		 
	                        		 //get value of spinner control of company
	                        		 Spinner sel_company = (Spinner)((AlertDialog) dialog).
		                        			 findViewById(R.id.spin_company);	
	                        		  
	                        		 //get value of spinner control of issue type
	                        		 Spinner sel_issuetype = (Spinner)((AlertDialog) dialog).
		                        			 findViewById(R.id.spin_issuetype);
	                        		 
	                        		 //get value of date of due 
	                        		 DatePicker date_picker = (DatePicker)((AlertDialog) dialog).
		                        			 findViewById(R.id.date_picker);
	                        		   
	                        		 //show the corresponding issues
	                        		 showIssues(false, sel_company.getSelectedItem().toString(),
	                        				 sel_issuetype.getSelectedItem().toString(),
	                        				 GlobalHelper.getDateFromDatePicket(date_picker)); 
	                        		 
	                        	 }
	                            
	                         }  
	                     });  
	             
	             
	             builder.setNegativeButton("Cancel",  
	                     new DialogInterface.OnClickListener() {  
	                         public void onClick(DialogInterface dialog, int whichButton) {  
	                             //cancel. do nothing
	                         }  
	                     });  
	             
	             builder.show();  
	        	
	        	
	          }  //onClick
			}); // setOnClickListener  filter button click	
	}
	
	//ini Sync button
	void iniButtonSync()
	{
		//get Sync button object
		btn_Sync = (ImageButton)findViewById(R.id.img_btn_sync);
		//Sync button click
		btn_Sync.setOnClickListener(new View.OnClickListener() {
	        public void onClick(View v) {	   
	        	
	        	//show Sync dialog
	        	sync_items_dialog();
	        	  
	          }  //onClick
			}); // setOnClickListener  sync button click
	}
	
	//ini global params
	void iniGlobal()
	{
		//database
		GlobalHelper.createDBInstance(getApplicationContext());
		//projects list
		GlobalHelper.projItems = new  ArrayList<ProjListView>();
		//company names list
		GlobalHelper.company_name = new HashMap<String,String>();
		//issue types map
		GlobalHelper.issue_type = new HashMap<String,String>();
		
		//issue type
		GlobalHelper.issue_status_options = new ArrayList<String>();
		GlobalHelper.issue_status_options.add("Draft");
		GlobalHelper.issue_status_options.add("Open");
		GlobalHelper.issue_status_options.add("Work Completed");
		GlobalHelper.issue_status_options.add("Ready to Inspect");
		GlobalHelper.issue_status_options.add("Not Approved");
		GlobalHelper.issue_status_options.add("In Dispute");
		GlobalHelper.issue_status_options.add("Closed");
		
	}
	
	
	//sync dialog
	void sync_items_dialog()
	{
		  
		    // map for sync dialog
			List<Map<String,Object>> listMap = 
					new  ArrayList<Map<String,Object>>();
			
			 //prepare the images for the list
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("image", R.drawable.ic_download);
	        map.put("content", getString(R.string.msg_sync_dialog_download));       
	        listMap.add(map);
	        
	        Map<String,Object> map1 = new HashMap<String,Object>();
			map1.put("image", R.drawable.ic_upload);
	        map1.put("content", getString(R.string.msg_sync_dialog_upload));       
	        listMap.add(map1);
			  
			
			SimpleAdapter sa= new SimpleAdapter(this, listMap, R.layout.listview_template,
	                new String[] { "image", "content" }, new int[] {
	                        R.id.listview_img1, R.id.listview_txt1 }); 
			
			
			new AlertDialog.Builder(MainActivity.this).
	 		setTitle(getString(R.string.title_sync_dialog)).				 
	 		setIcon(R.drawable.ic_launcher).
	 		setSingleChoiceItems(sa,0,
			new DialogInterface.OnClickListener() {
	 			  @Override
	 	            public void onClick(DialogInterface dialog, int which) {

	 	            	if(which == 0)
	 	            	{
	 	            		//download
	 	            		sync_download_issue();
	 	            	}
	 	            	else
	 	            	{
	 	            		//upload
	 	            		sync_upload_issue();
	 	            	}
	 	            		
	 	            	 
	 	                dialog.dismiss();
	 	            }
		    }).show();  
		 
	}
	
	//download
	void sync_download_issue()
	{
		ProgressDialog progress = new ProgressDialog(MainActivity.this);
    	progress.setMessage(getString(R.string.msg_prog_download_async));
    	AsyncTaskForGetIssue task_getissue = new AsyncTaskForGetIssue(progress);
    	task_getissue._activity= MainActivity.this;
    	task_getissue.execute(); 
    }  
	
	//upload
	void sync_upload_issue()
	{
		ProgressDialog progress = new ProgressDialog(MainActivity.this);
    	progress.setMessage(getString(R.string.msg_prog_upload_async));
    	AsyncTaskForUpdateIssue task_setissue = new AsyncTaskForUpdateIssue(progress);
    	task_setissue._activity= MainActivity.this;
    	task_setissue.execute();  
    }   
	 
	@Override  
    protected void onActivityResult(int requestCode, int resultCode, Intent data)  
    {   
		//login activity returned
        if(20==resultCode)  
        {  
        	btn_Login.setImageResource(R.drawable.log_out);
        	
        
         
        	//enable project, filter, sync buttons
        	btn_Proj.setEnabled(true);
    		btn_Filter.setEnabled(true);
    		btn_Sync.setEnabled(true);
    		
        	//get projects list
        	ProgressDialog progress = new ProgressDialog(MainActivity.this);
        	progress.setMessage(getString(R.string.msg_prog_get_proj_async));
        	AsyncTaskForProject task_getproj= new AsyncTaskForProject(progress);
        	task_getproj._activity= MainActivity.this;
        	task_getproj.execute(); 
        }   
     
        super.onActivityResult(requestCode, resultCode, data);  
    }   

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
