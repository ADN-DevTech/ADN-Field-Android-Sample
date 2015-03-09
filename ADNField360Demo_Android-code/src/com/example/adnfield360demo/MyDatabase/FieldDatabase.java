package com.example.adnfield360demo.MyDatabase;

import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import com.example.adnfield360demo.R.string;
import com.example.adnfield360demo.Helper.GlobalHelper;
import com.example.adnfield360demo.MyServices.ResponseClass;
import com.example.adnfield360demo.MyServices.ResponseClass.field_attachment;
import com.example.adnfield360demo.MyServices.ResponseClass.field_issue_class;
import com.example.adnfield360demo.MyServices.ResponseClass.field_project_class;
import com.google.gson.annotations.SerializedName;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;



public class FieldDatabase {  
 
	//clear project table
	public static void clear_ProjTable(mySQLiteHelper myHelper)
	{
		SQLiteDatabase db = myHelper.getWritableDatabase();
		db.execSQL("DELETE FROM fieldProjects;");
		db.close(); 
	}
	
	//update project table
	public static void update_ProjTable(mySQLiteHelper myHelper,
			List<ResponseClass.field_project_class> _json_prjs)
	{		
		SQLiteDatabase db = myHelper.getWritableDatabase();
		for(ResponseClass.field_project_class oEachPrj:_json_prjs)
		{
			Cursor c = db.rawQuery("SELECT * FROM fieldProjects WHERE projectid = ?", new String[]{oEachPrj.project_id});
			if(c.getCount() >0)
			{			   
			}
			else
			{
				db.execSQL("INSERT INTO fieldProjects VALUES (NULL, ?, ?)",
						new Object[]{oEachPrj.name,
						oEachPrj.project_id});
			}
			c.close();		 
		} 		 
		db.close();
   }
	
	//clear issue table
	public static void clear_IssueTable(mySQLiteHelper myHelper)
	{
		SQLiteDatabase db = myHelper.getWritableDatabase();
		db.execSQL("DELETE FROM fieldIssues;");
		db.close(); 
	}
	
	//update issue table
	public static void update_IssueTable(mySQLiteHelper myHelper,
			List<ResponseClass.field_issue_class> _json_issues)
	{		
		SQLiteDatabase db = myHelper.getWritableDatabase();
		 
		for(ResponseClass.field_issue_class oEachIssue:_json_issues)
		{    
			{   
				//store issue type
				GlobalHelper.issue_type.put(oEachIssue.issue_type_id, oEachIssue.issue_type);
				
				//convert attachment name info
				String att_name_str = "";
				for(ResponseClass.field_attachment oEachAtt : oEachIssue.attachments)
				{
					att_name_str += oEachAtt.filename + ":"; 
				}
				
				//convert attachment type info
				String att_type_str = "";
				for(ResponseClass.field_attachment oEachAtt : oEachIssue.attachments)
				{
					att_type_str += oEachAtt.content_type + ":"; 
				} 
				
				//convert attachment id info
				String att_id_str = "";
				for(ResponseClass.field_attachment oEachAtt : oEachIssue.attachments)
				{
					att_id_str += oEachAtt.id + ":"; 
				} 
				
				//convert date
				SimpleDateFormat  format = new SimpleDateFormat("yyyy-MM-dd");
				java.util.Date utilDate = null;
				try {
					utilDate = format.parse(oEachIssue.due_date);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}  				
				 
				java.sql.Date sqlDate;
				if(oEachIssue.due_date!="")
					sqlDate = new java.sql.Date(utilDate.getTime());
				else
					sqlDate = null; 
				
				//insert the issue
				db.execSQL("INSERT INTO fieldIssues VALUES (NULL, ?,?,?,?,?,?,?,?,?,?,?,?,?)",  
						new Object[]{"false",
						oEachIssue.issue_id,
						oEachIssue.issue_type,
						oEachIssue.description,
						oEachIssue.status,
						oEachIssue.company_id,
						GlobalHelper.company_name.get(oEachIssue.company_id),
						sqlDate,
						att_name_str,
						att_type_str,						
						att_id_str, 
						oEachIssue.created_by,
						oEachIssue.identifier}); 
				
			}  
		} 		 
		db.close();
   }
	
 
	
	public static void get_ProjTable_Item(mySQLiteHelper myHelper,
			String proj_id)
	{		
		SQLiteDatabase db = myHelper.getWritableDatabase();		
		 Cursor c = db.rawQuery("SELECT * FROM fieldProjects WHERE projectid = ?", new String[]{proj_id});
		 
	        while (c.moveToNext()) {  	           
	            String name = c.getString(c.getColumnIndex("projectname")); 
	        }  
	        c.close();  
		db.close();
   }
	
   
	public static void clear_AccountTable(mySQLiteHelper myHelper)
	{		
		SQLiteDatabase db = myHelper.getWritableDatabase();		
		db.execSQL("DELETE FROM fieldAccounts;"); 
		db.close();
    }
	
	public static void update_Item_AccountTable(mySQLiteHelper myHelper,
											 String _username,
											 String _password)
	{		
		SQLiteDatabase db = myHelper.getWritableDatabase();		
		 
		 Cursor c = null; 
		 c =  db.rawQuery("SELECT * FROM fieldAccounts WHERE username=?",new String[]{_username.toString()});
		
		  
		 if(c.moveToNext())
		 {
			 ContentValues values = 
		        		new ContentValues();		      
		        values.put("password", _password);					          
		        db.update("fieldAccounts", values,
		        		"username=?", 
		        		new String[]{_username.toString()});
		 }
		 else
		 {
				//insert the issue
				db.execSQL("INSERT INTO fieldAccounts VALUES (NULL, ?,?)",  
						new Object[]{_username,_password}); 
		 }
		c.close(); 
		db.close();
    }

}
