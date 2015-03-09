package com.example.adnfield360demo.MyServices;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLSocketFactory;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpMessage;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.adnfield360demo.MyDatabase.FieldDatabase;
import com.example.adnfield360demo.MyServices.SSLSocketFactoryEx;
import com.example.adnfield360demo.MyServices.ResponseClass.field_update_issue_field;
import com.example.adnfield360demo.R.drawable;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import com.example.adnfield360demo.Helper.GlobalHelper;
import com.example.adnfield360demo.Helper.ProjListView;
import com.example.adnfield360demo.R;


//services functions of Field 360
public class ServicesClass {
	
	 
	//base url of Field
	private static String BASEUrl = "https://manage.velasystems.com";
	//login
	private static String login_srv = "/api/login";
	//logout
	private static String logout_srv = "/api/logout";
	//get projects
	private static String get_projs_srv = "/api/projects?";
	//get issues
	private static String get_issues_srv = "/api/get_issues?";
	//update issue
	private static String update_issues_srv = "/fieldapi/issues/v1/update";
	//get company info
	private static String get_company_srv = "/fieldapi/admin/v1/companies?";
	//get attachment
	private static String get_attachment_srv = "/api/binary_data?";
	
	//get http client with SSL
	//from http://blog.csdn.net/malinkang1989/article/details/8848534
	public static HttpClient getNewHttpClient() {  
        try {  
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());  
            trustStore.load(null, null);  
            
            SSLSocketFactoryEx sf = new SSLSocketFactoryEx(trustStore);  
            sf.setHostnameVerifier(SSLSocketFactoryEx.ALLOW_ALL_HOSTNAME_VERIFIER);  
    
            HttpParams params = new BasicHttpParams();  
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);  
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);  
    
            SchemeRegistry registry = new SchemeRegistry();  
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));  
            registry.register(new Scheme("https", sf, 443));  
    
            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);  
    
            return new DefaultHttpClient(ccm, params);  
        } catch (Exception e) {  
            return new DefaultHttpClient();  
        }  
    }  
	
	//login
	public static Boolean field_login(String _username,
									String _password)
	{		
		try
		{ 
			//params for login
			 List <NameValuePair> nvps = new ArrayList <NameValuePair>();
			 nvps.add(new BasicNameValuePair("username", _username));
			 nvps.add(new BasicNameValuePair("password", _password)); 
	
			 HttpPost request = new HttpPost(BASEUrl+ login_srv);	 
			 request.setHeader("Content-Type", "application/x-www-form-urlencoded");
		 
			 UrlEncodedFormEntity uf;     								
			 uf = new UrlEncodedFormEntity(nvps, HTTP.UTF_8);
			 request.setEntity(uf); 
		 
			 //HttpClient httpclient = new DefaultHttpClient();
			 HttpClient httpclient =  getNewHttpClient();
			 HttpResponse response = httpclient.execute(request);
		 
			 StatusLine statusLine = response.getStatusLine();
			 if(statusLine.getStatusCode() == HttpStatus.SC_OK)
			 {	    
				 //succeeded
				 //get response json data
				 HttpEntity getResponseEntity = response.getEntity();
				 Reader reader = new InputStreamReader(getResponseEntity.getContent());
				 Gson gson = new Gson();				 
				 ResponseClass.field_login_class _login_cls = gson.fromJson(reader, ResponseClass.field_login_class.class);
				 //store ticket
				 GlobalHelper._field_ticket = _login_cls.ticket;
				 return true;
			 }
			 else
			 {    	
				 return false;
			 }
		}
		 catch (UnsupportedEncodingException e)
		 {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} 
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	 
	}
	
	//logout
	public static Boolean field_logout()
	 {		
		try
		{ 
			//params for login
			List <NameValuePair> nvps = new ArrayList <NameValuePair>();			 
			nvps.add(new BasicNameValuePair("ticket",  GlobalHelper._field_ticket));
			
			HttpPost request = new HttpPost(BASEUrl+ logout_srv);	 
			request.setHeader("Content-Type", "application/x-www-form-urlencoded");
			
			UrlEncodedFormEntity uf;     								
			uf = new UrlEncodedFormEntity(nvps, HTTP.UTF_8);
			request.setEntity(uf); 
			
			//HttpClient httpclient = new DefaultHttpClient();
			HttpClient httpclient =  getNewHttpClient();
			HttpResponse response = httpclient.execute(request);
			
			StatusLine statusLine = response.getStatusLine();
			if(statusLine.getStatusCode() == HttpStatus.SC_OK)
			{	    	 
				 //succeeded
				 
				return true;
			}
			else
			{    	
				return false;
			}
			}
			catch (UnsupportedEncodingException e)
			{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
			} 
			catch (IOException e)
			{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
			}

		}
	
	
	//get projects
	public static Boolean field_projs()
	{	
		 try
    	 {           
			 GlobalHelper.projItems.clear();
			 
    		 HttpGet request = new HttpGet(
    				 BASEUrl+ get_projs_srv + 
    				 "ticket=" + 
    				 GlobalHelper._field_ticket ); 
    		 
    		 //HttpClient httpclient = new DefaultHttpClient();
    		 HttpClient httpclient =  getNewHttpClient();
			 HttpResponse response = httpclient.execute(request);
			 
			 StatusLine statusLine = response.getStatusLine();
		     if(statusLine.getStatusCode() == HttpStatus.SC_OK){
		    	 
		    	//succeeded
		    	 //get response json data
		    	 HttpEntity getResponseEntity = response.getEntity();
		    	 Reader reader = new InputStreamReader(getResponseEntity.getContent());
		    	 Gson gson = new Gson();			    	 
		    	 List<ResponseClass.field_project_class> resjson_prjs =
		    			 gson.fromJson(reader, 
		    			 new TypeToken<List<ResponseClass.field_project_class>>() {}.getType());		      
		    	 
		    	 //store info of each project 
		    	 for(ResponseClass.field_project_class oEachProj : resjson_prjs)
		    	 {
		    		 ProjListView oitem = 
		    				 new ProjListView(oEachProj.name,
		    						 oEachProj.project_id,R.drawable.v);
		    		 GlobalHelper.projItems.add(oitem);
		    	 } 
		         
		    	 return true;
		     }		      
		     else
		     {    	
		    	 return false;
		     }
    		 
    	 }
    	 catch (UnsupportedEncodingException e)
    	 {
			// TODO Auto-generated catch block
			e.printStackTrace();
			 return false;
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			 return false;
		}
	}
	
	//get bitmap of attachment
	public static Bitmap  field_attachment(String obj_id, //attachment id
			String img_type  // thumbnail, original, composite
			 )
	{
		Bitmap returnBM = null;
		
		 try
    	 {              
    		 HttpGet request = new HttpGet(BASEUrl+
    				 get_attachment_srv + 
    				 "ticket=" + GlobalHelper._field_ticket +
    				 "&project_id=" + GlobalHelper.oCurrentPorjId +
    				 "&object_type=" + "Attachment" + 
    				 "&object_id=" + obj_id + 
    				 "&image_type=" + img_type ); 
    		 
    		 
    		 HttpClient httpclient =  getNewHttpClient();
			 HttpResponse response = httpclient.execute(request);
			 
			 StatusLine statusLine = response.getStatusLine();
		     if(statusLine.getStatusCode() == HttpStatus.SC_OK){
		    	 
		    	//succeeded
		    	 HttpEntity entity = response.getEntity();  
		    	 //read the binary data and convert to bitmap
		    	 InputStream is = entity.getContent(); 
		    	 returnBM = BitmapFactory.decodeStream(is);  
	             is.close();   
		    	 
		     }  
    		 
    	 }
    	 catch (UnsupportedEncodingException e)
    	 {
			// TODO Auto-generated catch block
			e.printStackTrace(); 
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace(); 
		}
		 
		
		return returnBM;
	}
	
	//get company info
	public static Boolean field_company()
	{
		 try
    	 {              
    		 HttpGet request = new HttpGet(BASEUrl+
    				 get_company_srv + 
    				 "ticket=" + GlobalHelper._field_ticket +
    				 "&project_id=" + GlobalHelper.oCurrentPorjId); 
    		 
    		 //HttpClient httpclient = new DefaultHttpClient();
    		 HttpClient httpclient =  getNewHttpClient();
			 HttpResponse response = httpclient.execute(request);
			 
			 StatusLine statusLine = response.getStatusLine();
		     if(statusLine.getStatusCode() == HttpStatus.SC_OK){
		    	 
		    	//succeeded
		    	 //get response json data
		    	 HttpEntity getResponseEntity = response.getEntity();
		    	 Reader reader = new InputStreamReader(getResponseEntity.getContent());
		    	 Gson gson = new Gson();			    	 
		    	 List<ResponseClass.field_company> resjson_companies = gson.fromJson(reader, 
		    			 new TypeToken<List<ResponseClass.field_company>>() {}.getType());		     

		    	 //store company info map
		    	 for(ResponseClass.field_company eachCom:resjson_companies)
		    	 {
		    		 GlobalHelper.company_name.put(eachCom.id, eachCom.name);
		    	 } 		
		    	 
		    	 return true;
		     }		      
		     else
		     {    	
		    	 return false;
		     }
    		 
    	 }
    	 catch (UnsupportedEncodingException e)
    	 {
			// TODO Auto-generated catch block
			e.printStackTrace();
			 return false;
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			 return false;
		}
	}
	
	//get issue
	public static Boolean field_issues()	
	{	
		 try
    	 {              
    		 HttpGet request = new HttpGet(BASEUrl+
    				 get_issues_srv + 
    				 "ticket=" + GlobalHelper._field_ticket +
    				 "&project_id=" + GlobalHelper.oCurrentPorjId); 
    		 
    		 //HttpClient httpclient = new DefaultHttpClient();
    		 HttpClient httpclient =  getNewHttpClient();
			 HttpResponse response = httpclient.execute(request);
			 
			 StatusLine statusLine = response.getStatusLine();
		     if(statusLine.getStatusCode() == HttpStatus.SC_OK){
		    	 
		    	//succeeded
		    	 //get response json data
		    	 HttpEntity getResponseEntity = response.getEntity();
		    	 Reader reader = new InputStreamReader(getResponseEntity.getContent());
		    	 Gson gson = new Gson();			    	 
		    	 List<ResponseClass.field_issue_class> resjson_issues = gson.fromJson(reader, 
		    			 new TypeToken<List<ResponseClass.field_issue_class>>() {}.getType());	 
		    	 
		    	 //update issue table in the database
				 FieldDatabase.clear_IssueTable(GlobalHelper._myDBHelper);
				 FieldDatabase.update_IssueTable(GlobalHelper._myDBHelper, 
						 resjson_issues);			
		    	 
		    	 return true;
		     }		      
		     else
		     {    	
		    	 return false;
		     }
    		 
    	 }
    	 catch (UnsupportedEncodingException e)
    	 {
			// TODO Auto-generated catch block
			e.printStackTrace();
			 return false;
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			 return false;
		}
	} 
	
	//update issue
	public static Boolean update_issues()	
	{	
		 try
    	 {    
			 //check the issues which are updated locally
			 SQLiteDatabase db = GlobalHelper._myDBHelper.getWritableDatabase();			 
			 Cursor c = db.rawQuery("SELECT * FROM fieldIssues WHERE updated = ?", 
							new String[]{"true"});
			 
			if(c.getCount() >0)
			{	
				 List<ResponseClass.field_update_issue> issues_to_update =
						 new ArrayList<ResponseClass.field_update_issue>();						 
				
				 
				 while (c.moveToNext()) {  	           
			            String issue_id = c.getString(c.getColumnIndex("issueid"));
			            String issue_status = c.getString(c.getColumnIndex("status"));
			             
			            //param to update issue
			            
			            //The following shows an array with one issue. 
			            // The example is setting the priority to High.
			            //All other fields in the issue would have remained the same.

			            //[
			            //  {
			            //    "id":"bea2eb64-4420-11e2-b406-80c45664f08a",
			            ///    "temporary_id":"Q45",
			            //    "fields":[
			           //       {
			            //        "id":"f--priority",
			            //        "value":"High"
			           //       },
			           //     ]
			           //   }
			          //  ]
			            //
			            //
			            ResponseClass.field_update_issue each_update_issue = new ResponseClass().new field_update_issue();
			            
			            each_update_issue.id = issue_id;
			            
			            ResponseClass.field_update_issue_field one_update_field = 
			            		new ResponseClass().new field_update_issue_field();
			            one_update_field.id = "f--status";
			            one_update_field.value = issue_status;
			            
			            each_update_issue.fields = 
			            		new ArrayList<field_update_issue_field>();
			            		 
			            		 each_update_issue.fields.add(one_update_field) ;
			             issues_to_update.add(each_update_issue);
			            }
			        
			        c.close();
			        
			        //other params
			        List <NameValuePair> nvps = new ArrayList <NameValuePair>();
					 nvps.add(new BasicNameValuePair("ticket", GlobalHelper._field_ticket));
					 nvps.add(new BasicNameValuePair("project_id", GlobalHelper.oCurrentPorjId)); 
					 
			            
					 HttpPost request = new HttpPost(BASEUrl+ update_issues_srv);	 
					 request.setHeader("Content-Type", "application/x-www-form-urlencoded");
					 
					 //convert params to json
					 Gson gson = new Gson();		
					 String issue_ids_array_json = gson.toJson(issues_to_update);
					 nvps.add(new BasicNameValuePair("issues", issue_ids_array_json)); 
				 
					 UrlEncodedFormEntity uf;     								
					 uf = new UrlEncodedFormEntity(nvps, HTTP.UTF_8);
					 request.setEntity(uf);
					  
				 
					 //HttpClient httpclient = new DefaultHttpClient();
					 HttpClient httpclient =  getNewHttpClient();
					 HttpResponse response = httpclient.execute(request);
				 
					 StatusLine statusLine = response.getStatusLine();
					 if(statusLine.getStatusCode() == HttpStatus.SC_OK)
					 {	    	 
						//succeeded
						 //get error report is needed
						 
						 //HttpEntity getResponseEntity = response.getEntity();
						 //Reader reader = new InputStreamReader(getResponseEntity.getContent()); 
						 //Gson gson1 = new Gson();			    	 
						 //ResponseClass.field_update_issue_result _result_cls = gson1.fromJson(reader, ResponseClass.field_update_issue_result.class);
					 
						 return true;
					 }
					 else
					 {    	
						 return false;
					 } 
			}
			else
			{  
				 //do nothing, close c
				c.close();	
				db.close();  
				return false; 
			} 
			
    	 }
		 catch (UnsupportedEncodingException e)
    	 {
			// TODO Auto-generated catch block
			e.printStackTrace();
			 return false;
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			 return false;
		}
			 
	}	
	
}

