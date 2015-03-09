package com.example.adnfield360demo.MyDatabase;

import java.util.List;

import com.google.gson.annotations.SerializedName;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

//SQ Lite database helper
public class mySQLiteHelper extends SQLiteOpenHelper {		
	 
	public static String _field_ticket;
	 
	
	private static final int DATABASE_VERSION = 1;
	//db name
	private static final String DATABASE_NAME = "myAPPDB";
	//name of project table
	private static final String FIELD_PROJECTS_TABLE_NAME = "fieldProjects";
	//name of issue table
	private static final String FIELD_ISSUES_TABLE_NAME = "fieldIssues";
	//name of account table
	private static final String FIELD_ACCOUNTS_TABLE_NAME = "fieldAccounts";
	
	//sql of create project table
	private static final String FIELD_PROJECTS_TABLE_CREATE =
	                "CREATE TABLE IF NOT EXISTS " + FIELD_PROJECTS_TABLE_NAME + " (" +
	                		"_id integer primary key, " +
	                		"projectname" + " TEXT, " +
	                		"projectid" + " TEXT);";
	
	 
	//sql of create issue table 
	private static final String FIELD_ISSUES_TABLE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + FIELD_ISSUES_TABLE_NAME + " (" +
            		"_id integer primary key, " +
            		"updated" + " TEXT," +     //is the issue is updated locally
            		"issueid" + " TEXT," +	   //issue id 	
            		"issuetype" + " TEXT, " +  //issue type
            		"issuedesc" + " TEXT," +   //issue description
            		"status" + " TEXT, " +     //issue status 
            		"companyid" + " TEXT, " +  //company id
            		"companyname" + " TEXT, " + //company name
            		"duedate" + " DATE, " +     //due date
            		"attnames" + " TEXT, " +    //attachment names collection, split by :
            		"atttypes" + " TEXT, " +    //attachment types collection, split by : 
            		"attids" + " TEXT, " +      //attachment ids collection, split by :
            		"createdby" + " TEXT, " +   //created by 
            		"identifier" + " TEXT);";   // indentifier
	
	//sql of create account table 
	private static final String FIELD_ACCOUNT_TABLE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + FIELD_ACCOUNTS_TABLE_NAME + " (" +
            		"_id integer primary key, " +
            		"username" + " TEXT, " +
            		"password" + " TEXT);";
	
	
	
	public  mySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

	//create the tables
    @Override
    public void onCreate(SQLiteDatabase db) {    	
     
       db.execSQL(FIELD_PROJECTS_TABLE_CREATE);
       db.execSQL(FIELD_ISSUES_TABLE_CREATE);
       db.execSQL(FIELD_ACCOUNT_TABLE_CREATE);
    }

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}  
	
}
