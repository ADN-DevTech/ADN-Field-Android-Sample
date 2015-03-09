package com.example.adnfield360demo.MyAsync;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import com.example.adnfield360demo.MyServices.ServicesClass;

 

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

//task for Company
public class AsyncTaskForCompany extends AsyncTask<Void, Void, Void> {

	public Context _context;
	private Boolean _isOK = false;
	
	private ProgressDialog progress;
	  public AsyncTaskForCompany(ProgressDialog progress) {
		    this.progress = progress;
		  }

		  public void onPreExecute() {
		    progress.show();
		  } 
		  
		  public void onPostExecute(Void unused) { 
		    progress.dismiss();
		    
		    if(_isOK)
		    {
		    	 Toast.makeText(
		    			 _context.getApplicationContext(),
						  "Get company information Succeeded!",
	 					Toast.LENGTH_LONG).show();
		    }
		    else
		    {
		    	 Toast.makeText(
		    			 _context.getApplicationContext(),
						  "Failed to get company information",
	 					Toast.LENGTH_LONG).show();
		    }
		  }

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			
			 if(ServicesClass.field_company())
			 {
				_isOK = true;
			 }
			 else
			 {
				  
			 }
			 
			
			return null;
		}

}
