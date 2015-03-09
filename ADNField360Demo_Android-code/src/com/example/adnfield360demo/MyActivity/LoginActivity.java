package com.example.adnfield360demo.MyActivity; 

import java.util.ArrayList;
import java.util.List;

import com.example.adnfield360demo.MyAsync.AsyncTaskForAttachment;
import com.example.adnfield360demo.MyAsync.AsyncTaskForLogin;
import com.example.adnfield360demo.Helper.GlobalHelper;
import com.example.adnfield360demo.R;
import com.example.adnfield360demo.MyServices.ServicesClass;
import com.example.adnfield360demo.R.id;
import com.example.adnfield360demo.R.layout;
import com.example.adnfield360demo.R.menu;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

 // login activity
public class LoginActivity extends Activity implements TextWatcher {
	 
	//user name
	private EditText txt_UserName;
	//password
	private EditText txt_Password;
	//button of login
	private Button btn_Login;  
	
	private AutoCompleteTextView auto_username;
	
	List<String> user_name_list = new ArrayList<String>();
	List<String> password_list = new ArrayList<String>();
	
	//user name
	private String _userName;
	//password
	private String _password;
	

	 private static final String[] COUNTRIES = new String[] {
         "Belgium", "France", "Italy", "Germany", "Spain"
     };
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_login);
		
		btn_Login = (Button)findViewById(R.id.sign_in_button);
	 
		txt_Password = (EditText)findViewById(R.id.password);				
	
		//get the accounts info from database
		SQLiteDatabase db = GlobalHelper._myDBHelper.getWritableDatabase();		
		 
		 Cursor c = null; 
		 c =  db.rawQuery("SELECT * FROM fieldAccounts",null); 
		  
		 while (c.moveToNext()) 
	     {  
			 user_name_list.add(c.getString(c.getColumnIndex("username")));
			 password_list.add(c.getString(c.getColumnIndex("password"))); 
	     }		  
		c.close(); 
		db.close();
		
		//build the adapter to auto-complete the account name and password
		ArrayAdapter<String> user_adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, user_name_list); 		
         auto_username = (AutoCompleteTextView)findViewById(R.id.autousername);
         auto_username.setAdapter(user_adapter);
         user_adapter.notifyDataSetChanged();
        
        auto_username.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            	
            	_password = password_list.get(position).toString();
            	txt_Password.setText(_password.toString()); 
            }
      });
         
		
		//login button click
				btn_Login.setOnClickListener(new View.OnClickListener() {
			        public void onClick(View v) {
			        	 
			        	_userName = auto_username.getText().toString();
			        	_password = txt_Password.getText().toString();
			        			
			        	if(_userName.equals("") ||
			        			_password.equals(""))
			        	{
			        		Toast.makeText(
			    					getApplication(),
			    					getString(R.string.msg_input_user_info),
			    					Toast.LENGTH_LONG).show();
			        		
			        		return;
			        	}

			        	//prepare user name and password for async task
			        	List<String> forAsync = new ArrayList<String>();
			        	String logname = _userName.toString();
				        String password = _password.toString();
				        forAsync.add(logname);
				        forAsync.add(password);
				        
				        //login task
			        	ProgressDialog progress = new ProgressDialog(LoginActivity.this);
			        	progress.setMessage(getString(R.string.msg_prog_login_async));
			        	AsyncTaskForLogin task_att = new AsyncTaskForLogin(progress);         
			        	task_att._activity = LoginActivity.this;
			        	task_att.execute(forAsync); 
			        	
			    		
			          }  //onClick
					}); // setOnClickListener  login button click	
 
	} 
	
	@Override
	public void afterTextChanged(Editable arg0) {
	 // TODO Auto-generated method stub
		txt_Password.setText(_password); 
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
	  int after) {
	 // TODO Auto-generated method stub
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
	 // TODO Auto-generated method stub
		txt_Password.setText(_password); 
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}	 
}
