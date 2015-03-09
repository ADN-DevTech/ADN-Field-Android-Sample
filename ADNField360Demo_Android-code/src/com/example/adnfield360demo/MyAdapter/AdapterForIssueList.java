// adapter for the list view of the main activity

package com.example.adnfield360demo.MyAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.adnfield360demo.Helper.GlobalHelper;
import com.example.adnfield360demo.R;
import com.example.adnfield360demo.MyActivity.AttachmentActivity;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class AdapterForIssueList  extends ArrayAdapter<Map<String,Object>> {

	 //context from the main activity
	 private final Context _context;
	 //attached map with the list view
	 public List<Map<String,Object>> lisMap;
	 //list view layout
	 private final int rowResourceId;
	 
	 public AdapterForIssueList(Context context, 
			 int textViewResourceId,  
			 List<Map<String,Object>> objects) {

	        super(context, textViewResourceId, objects);

	        this._context = context;
	        this.lisMap = objects;
	        this.rowResourceId = textViewResourceId;

	    }
	  

	@Override
	    public View getView(int position, 
	    		View convertView,
	    		ViewGroup parent) {

	        LayoutInflater inflater =
	        		(LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	        View rowView =
	        		inflater.inflate(rowResourceId, parent, false);
	        ImageView imageView_status =
	        		(ImageView) rowView.findViewById(R.id.listview_img1); 
	        imageView_status.setTag(position);
	        TextView textView_desc = 
	        		(TextView) rowView.findViewById(R.id.listview_txt1);
	        ImageView imageView_attach = 
	        		(ImageView) rowView.findViewById(R.id.listview_img2);
	        //record the position (index) of this list item
	        imageView_attach.setTag(position);

	        Map<String,Object> oThisMap =
	        		lisMap.get(position);	 
	        
	        //decide which status image will be displayed.
	        if(oThisMap.get("imagestatus") != null)
	           imageView_status.setImageResource((Integer) oThisMap.get("imagestatus"));
	        textView_desc.setText(oThisMap.get("issuedesc").toString());
	        //decide whether attachment image is displayed or not
	        if(oThisMap.get("imageattach") != null)
	           imageView_attach.setImageResource((Integer) oThisMap.get("imageattach")); 
	        
	         //when list item is selected 
	        imageView_status.setOnClickListener(new OnClickListener() {

	            @Override
	            public void onClick(View v) {
	                // TODO Auto-generated method stub
				    
	            	//index of the selected item
	            	GlobalHelper.oCurrentIssueIndexInListView = 
	            			(Integer) v.getTag();
	            	
	            	//the map for dialog of status
	            	List<Map<String,Object>> listMap_StatusView =
	            			new  ArrayList<Map<String,Object>>();
					
					 for(String issueStatus :GlobalHelper.issue_status_options ){
						 Map<String,Object> map = new HashMap<String,Object>();	
						 
						 if ( issueStatus.equalsIgnoreCase("Draft"))
				            {							    
				            	map.put("image", R.drawable.ic_draft);
				            	map.put("content", "Draft");
				            }
				            else if(issueStatus.equalsIgnoreCase("Open")){
				            	map.put("image", R.drawable.ic_open);	
				            	map.put("content", "Open");
				            }
				            else if(issueStatus.equalsIgnoreCase("Work Completed")){
				            	map.put("image", R.drawable.ic_work_com);
				            	map.put("content", "Work Completed");
				            }
				            else if(issueStatus.equalsIgnoreCase("Ready to Inspect")){
				            	map.put("image", R.drawable.ic_inspector);
				            	map.put("content", "Ready to Inspect");
				            }
				            else if(issueStatus.equalsIgnoreCase("Not Approved")){
				            	map.put("image", R.drawable.ic_not_appro);	
				            	map.put("content", "Not Approved");
				            }
				            else if(issueStatus.equalsIgnoreCase("In Dispute")){
				            	map.put("image", R.drawable.ic_dispute);
				            	map.put("content", "In Dispute");
				            }
				            else if(issueStatus.equalsIgnoreCase("Closed")){
				            	map.put("image", R.drawable.ic_closed);	
				            	map.put("content", "Closed");
				            }
				            else
				            {				            	
				            } 
				           
						 listMap_StatusView.add(map);
						 
					 } 
					 
					SimpleAdapter sa= new SimpleAdapter(_context, 
							listMap_StatusView, 
							R.layout.listview_template,
			                new String[] { "image", "content" }, new int[] {
							R.id.listview_img1, R.id.listview_txt1 });  
		       
					//show status dialog
		        	new AlertDialog.Builder(_context).
			 		setTitle("Select Project").				 
			 		setIcon(R.drawable.ic_launcher).
			 		setSingleChoiceItems(sa,0,
					new DialogInterface.OnClickListener() {
	            
			            @Override
			            public void onClick(DialogInterface dialog, int which) {
		
			            	//set the issue update = true in the database
			            	String oSelStatus = 
			            			GlobalHelper.issue_status_options.get(which);					                	 
		                	SQLiteDatabase db =
		                			GlobalHelper._myDBHelper.getWritableDatabase();      	
					        ContentValues values = 
					        		new ContentValues();
					        values.put("status", oSelStatus);
					        values.put("updated", "true");					          
					        db.update("fieldIssues", values,
					        		"issueid=?", 
					        		new String[]{lisMap.get(GlobalHelper.oCurrentIssueIndexInListView).get("id").toString()}); 
					        db.close();    
						            		    
					         
				            if ( oSelStatus.equalsIgnoreCase("Draft"))
				            {
				            	lisMap.get(GlobalHelper.oCurrentIssueIndexInListView).
				            			put("imagestatus", R.drawable.ic_draft);						             					         
				            }
				            else if(oSelStatus.equalsIgnoreCase("Open"))						            	
				            {
					            	lisMap.get(GlobalHelper.oCurrentIssueIndexInListView).
					            			put("imagestatus", R.drawable.ic_open);
     					        
				            }
				            else if(oSelStatus.equalsIgnoreCase("Work Completed")){
				            	lisMap.get(GlobalHelper.oCurrentIssueIndexInListView).
		            				put("imagestatus", R.drawable.ic_work_com); 					         			        
				            }
				            else if(oSelStatus.equalsIgnoreCase("Ready to Inspect")){
				            	lisMap.get(GlobalHelper.oCurrentIssueIndexInListView).
		            				put("imagestatus", R.drawable.ic_inspector); 		
				             
				            }
				            else if(oSelStatus.equalsIgnoreCase("Not Approved")){
				            	lisMap.get(GlobalHelper.oCurrentIssueIndexInListView).
		            				put("imagestatus", R.drawable.ic_not_appro); 
				            
				            }
				            else if(oSelStatus.equalsIgnoreCase("In Dispute")){
				            	lisMap.get(GlobalHelper.oCurrentIssueIndexInListView).
		            				put("imagestatus", R.drawable.ic_dispute); 
				             
				            }
				            else if(oSelStatus.equalsIgnoreCase("Closed")){
				            	lisMap.get(GlobalHelper.oCurrentIssueIndexInListView).
		            				put("imagestatus", R.drawable.ic_closed); 
				             
				            }
				            else
				            {				            	
				            }  
							 
						      //notify the issue status updated.
							  notifyDataSetChanged();
							  
		                    dialog.dismiss();
			            }
			        }).show();  

	            }
	        });
	        
	        imageView_attach.setOnClickListener(new OnClickListener() {

	            @Override
	            public void onClick(View v) {
	                // TODO Auto-generated method stub
	            	GlobalHelper.oCurrentIssueIndexInListView = (Integer) v.getTag();	
	            	if(lisMap.get(GlobalHelper.oCurrentIssueIndexInListView).get("imageattach")!=null)
	            	{
		            		
		            	 //start attachment activity
		            	GlobalHelper.oCurrentIssueId =
		            			lisMap.get(GlobalHelper.oCurrentIssueIndexInListView).get("id").
		            			toString();
		            	
		        	    Intent intent = new Intent();			      
				        intent.setClass(_context, AttachmentActivity.class);			       
				        _context.startActivity(intent);
	            	}
			        

	            }
	        });
	        
	        return rowView;

	    }

}
