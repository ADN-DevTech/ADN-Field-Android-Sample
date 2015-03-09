package com.example.adnfield360demo.MyActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.adnfield360demo.MyAsync.AsyncTaskForAttachment;
import com.example.adnfield360demo.Helper.GlobalHelper;
import com.example.adnfield360demo.R;
import com.example.adnfield360demo.MyServices.ResponseClass;
import com.example.adnfield360demo.MyServices.ServicesClass;
import com.example.adnfield360demo.MyServices.ResponseClass.field_attachment;
import com.example.adnfield360demo.R.layout;
import com.example.adnfield360demo.R.menu;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;

//attachment activity
public class AttachmentActivity extends Activity {

	ListView listView; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_attachment);		
  
	   	 List<Map<String,Object>> listMap  = 
	   			 new  ArrayList<Map<String,Object>>();
	   	 
	   	 //current issue id
	   	 String currentIssueId = 
	   			 GlobalHelper.oCurrentIssueId; 
	   	 
	   	 //find the issue in database			
		SQLiteDatabase db1 = 
				GlobalHelper._myDBHelper.getReadableDatabase();		
		Cursor c = 
				db1.rawQuery("SELECT * FROM fieldIssues WHERE issueid = ?",
						new String[]{currentIssueId});
	
		String att_str=null;
		 while (c.moveToNext()) { 		        	
		               att_str= 
		            		   c.getString(c.getColumnIndex("attids")); 
		               break; 		            
		        }  
		 c.close();  
		 db1.close();
		 
		 //prepare for attachment task
		 String[] att_ids = att_str.split(":");
		 List<String> forAsync = new ArrayList<String>();
		 forAsync.add(GlobalHelper.oCurrentIssueId);
		 forAsync.add("thumb");		 
		 for(String each_att_id:att_ids)
		 {
			 forAsync.add(each_att_id); 
		 }
			  
		 // attachment task
		ProgressDialog progress = new ProgressDialog(AttachmentActivity.this);
    	progress.setMessage(getString(R.string.msg_prog_get_att_async));
    	AsyncTaskForAttachment task_att =
    			new AsyncTaskForAttachment(progress);         
    	task_att._activity = AttachmentActivity.this;
    	task_att.execute(forAsync); 

    	 
    	listView =  (ListView)findViewById(R.id.att_list_view); 
 
    	listView.setOnItemClickListener(new OnItemClickListener(){   
            @Override   
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,   
                    long arg3) {   
            	
            	//when any attachment is selected
            	if(listView.getAdapter()!=null)
            	{
            		// get info of this selected attachment
            		HashMap<String,Object> map =
            				(HashMap<String,Object>)listView.getItemAtPosition(arg2);
            		
            		          
            		  //create the temp file to store the image of attachment
		        	  File folder = new File(Environment.getExternalStorageDirectory(), 
		        			  getString(R.string.title_main_activity));
		        	  File toWrite = 
		        			  new File(folder, "save.png");
		        	  if(!toWrite.exists())
		        	  {
		        	      if(!folder.mkdirs())
						  {
						       
						  }
		        	   }
		        	  
		        	  
            		Bitmap att_bitmap = (Bitmap)map.get("icon"); 
            		if(map.get("objtype").equals("image/jpeg")) 
        			  {
            			//get composite image
            			att_bitmap = (Bitmap)map.get("compimage");
            			if(att_bitmap != null)
            			{            				
            			}
            			else
            			{
            				//get orignal image
            				att_bitmap = (Bitmap)map.get("oriimage");            			 
            			} 
        			  }  
            	 
            		//produce the file stream for the bitmap
            		FileOutputStream fos = null;
					try {
						 fos = new FileOutputStream(toWrite);
						 att_bitmap.compress( CompressFormat.PNG, 100, fos );						
						fos.close(); 
						fos.flush();
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return;
					}
            		  
					//start activity to open the bitmap in the built-in view of photo
            		Intent intent = new Intent();
            		intent.setAction(android.content.Intent.ACTION_VIEW);
            		intent.setDataAndType(Uri.fromFile(new File(toWrite.getPath())), "image/png");
            		startActivity(intent); 

            	} 
            }   
               
        });  
    	 
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.attachment, menu);
		return true;
	}

}
