package com.example.adnfield360demo.MyAsync;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.adnfield360demo.Helper.GlobalHelper;
import com.example.adnfield360demo.R;
import com.example.adnfield360demo.MyActivity.AttachmentActivity;
import com.example.adnfield360demo.MyServices.ResponseClass;
import com.example.adnfield360demo.MyServices.ServicesClass;
import com.example.adnfield360demo.R.id;
import com.example.adnfield360demo.R.layout;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.Toast;

//task for attachment
public class AsyncTaskForAttachment extends AsyncTask<List<String>, Void, Void> {
 
	
	private ProgressDialog progress;
	//attachment activity
	public AttachmentActivity _activity;
	
	//bitmaps collection of thumbnail
	List<Bitmap> oatt_thumb_image_coll= 
			new ArrayList<Bitmap>();
	//bitmaps collection of orignal images
 	List<Bitmap> oatt_ori_image_coll= 
 			new ArrayList<Bitmap>();	
 	//bitmaps collection of composite images
 	List<Bitmap> oatt_comp_image_coll= 
 			new ArrayList<Bitmap>();
 	//bitmaps collection of attachment ids 
	List<String> oatt_id_coll= 
			new ArrayList<String>();
	//bitmaps collection of attachment names
	List<String> oatt_name_coll =
			new  ArrayList<String>();	
	//bitmaps collection of attachment types
	List<String> oatt_type_coll =
			new  ArrayList<String>();
	
	
	  public AsyncTaskForAttachment(ProgressDialog progress) {
		    this.progress = progress;
		  }

		  public void onPreExecute() {
		    progress.show();
		  } 
		  
		  public void onPostExecute(Void unused) { 
		    progress.dismiss();		 
		    
		    
		    ListView listView  =   
		    		(ListView)_activity.findViewById(R.id.att_list_view);
		    List<HashMap<String,Object>> mListData = 
		    		getListData(); 
		    
		    SimpleAdapter adapter = 
		    		new SimpleAdapter(_activity, 
		    				mListData,
		    				R.layout.attach_list_view,   
				    		new String[]{"icon","text"},
				    		new int[]{R.id.img_att_view,R.id.txt_att_view}); 
		    
		     adapter.setViewBinder(new ViewBinder() {   
		    	public boolean setViewValue(View view, Object data,   
		    	String textRepresentation) {   
		     
		    	if(view instanceof ImageView && data instanceof Bitmap)
		    	{   
			    	ImageView iv = (ImageView) view;   
			    	iv.setImageBitmap((Bitmap) data);   
			    	return true;   
		    	}
		    	else   
		    	return false;   
		    	}   
		    	});  
		    listView.setAdapter(adapter); 
		  }
		  
		  public List<HashMap<String,Object>> getListData(){   
			  List<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>(); 
			  
			  //store the images of thumbnail, original, composite
			  HashMap<String,Object> map = null;
			  	  int index = 0;
				  for(Bitmap _eachBM:oatt_thumb_image_coll)
				  {   
					  map = new HashMap<String, Object>();   
					  map.put("icon",_eachBM); 
					  String textStr = "Name: "+ oatt_name_coll.get(index)+ 
							  "   Type:  " +
							  oatt_type_coll.get(index);
					  map.put("text",textStr);
					  map.put("objid", oatt_id_coll.get(index));
					  map.put("objtype", oatt_type_coll.get(index));
					  map.put("compimage", oatt_comp_image_coll.get(index));
					  map.put("oriimage", oatt_ori_image_coll.get(index));
				  
					  list.add(map);   
					  
					  index++;
				  }   
			  return list;   
			  }   

		 


		@Override
		protected Void doInBackground(List<String>... params) {
			// TODO Auto-generated method stub
					 
			 
			//issue id
			 String issueid = params[0].get(0);
			 //image type
			 String atttypeId = params[0].get(1);
			 
			 for(int i = 2;i<params[0].size();i++)
			 {   
				 Bitmap oEachThumbImage = 
						 ServicesClass.field_attachment(params[0].get(i),atttypeId);
				 
				 if(oEachThumbImage != null)
				 {
					 oatt_id_coll.add(params[0].get(i));
					 oatt_thumb_image_coll.add(oEachThumbImage);
					 
				    	 //find the issue in database			
						SQLiteDatabase db1 = 
								GlobalHelper._myDBHelper.getReadableDatabase();		
						Cursor c = 
								db1.rawQuery("SELECT * FROM fieldIssues WHERE issueid= ?",
										new String[]{issueid});
					
						String sql_att_names_str=null;
						String sql_att_types_str=null;
						 while (c.moveToNext()) { 		        	
							 sql_att_names_str= c.getString(c.getColumnIndex("attnames")); 
							 sql_att_types_str= c.getString(c.getColumnIndex("atttypes"));
						               break; 		            
						        }  
						 c.close();  
						 db1.close();
						 
						 //get attachment names collection
						 String[] sql_att_names_str_coll = sql_att_names_str.split(":");
						 if(sql_att_names_str!="")
							 {							 	
							 	 oatt_name_coll.add(sql_att_names_str_coll[i-2]);
							 }
						//get attachment types collection
						 String[] sql_att_types_str_coll = sql_att_types_str.split(":");
						 if(sql_att_types_str!="")
						 { 
							 oatt_type_coll.add(sql_att_types_str_coll[i-2]);
						 } 
						  
						 
						 if(sql_att_types_str_coll[i-2].equals("image/jpeg"))
						 {							 
							 
							 Bitmap oEachCompImage = 
									 ServicesClass.field_attachment(params[0].get(i),"composite");
							 if(oEachCompImage != null)
							 {
								 //get composite image
								 oatt_comp_image_coll.add(oEachCompImage);
								 oatt_ori_image_coll.add(null);
							 }
							 else
							 {
								 oatt_comp_image_coll.add(null);
								 
								//get original image
								 Bitmap oEachOriImage = 
										 ServicesClass.field_attachment(params[0].get(i),"original");
								 if(oEachOriImage != null)
								 {
									 oatt_ori_image_coll.add(oEachOriImage);
								 }
								 else
								 {
									 oatt_ori_image_coll.add(null);
								 } 
								 
							 }
						 }
						 else
						 {
							 oatt_ori_image_coll.add(null);
							 oatt_comp_image_coll.add(null);
						 } 
				 }
				 else
				 {
					  Toast.makeText(
							  _activity.getApplicationContext(),
							  _activity.getString(R.string.msg_lget_att_fail),
		  					Toast.LENGTH_LONG).show();
				 }
			 }
			 
			
			return null;
		}}
