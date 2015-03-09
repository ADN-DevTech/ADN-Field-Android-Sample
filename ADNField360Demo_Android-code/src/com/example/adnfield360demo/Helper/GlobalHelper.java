package com.example.adnfield360demo.Helper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import com.example.adnfield360demo.MyDatabase.mySQLiteHelper;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.widget.DatePicker;


public class GlobalHelper {
	 
	//database
    public static  mySQLiteHelper _myDBHelper;
    //field ticket
    public static String _field_ticket = ""; 
    //projects item
    public static List<ProjListView> projItems;
    //current project name
    public static String oCurrentPorjName;
    //current project id
	public static String oCurrentPorjId;
	//current issue id
	public static String oCurrentIssueId;
	//index of the current selected issue 
	public static int oCurrentIssueIndexInListView;
	//issue status options
	public static List<String> issue_status_options;
	//company name list
	public static Map<String,String> company_name;
	//issue type list
	public static Map<String,String> issue_type;  
	 
    //create database helper
    public static void createDBInstance(Context pContext){
        if(_myDBHelper == null) {
        	_myDBHelper = new mySQLiteHelper(pContext); 
        }
      }
    
    //get date from date picker control
    public static java.util.Date getDateFromDatePicket(DatePicker datePicker){
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year =  datePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        return calendar.getTime();
    }
}

 