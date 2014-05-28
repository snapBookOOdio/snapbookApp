package com.example.snapbookapp;

import org.json.JSONException;
import org.json.JSONObject;



import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {
	Button btnLogin;
    Button btnLinkToRegister;
    EditText inputEmail;
    EditText inputPassword;
    TextView loginErrorMsg;
    NetworkUtil n1;
    private ProgressDialog pDialog;
    int valueok;
    String error;
	private static String KEY_SUCCESS = "success";
    private static String KEY_ERROR = "error";
    private static String KEY_ERROR_MSG = "error_msg";
    private static String KEY_UID = "uid";
    private static String KEY_NAME = "name";
    private static String KEY_EMAIL = "email";
    private static String PHONE = "phone";
    UserFunctions userFunction;
    JSONObject json;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity);
		valueok=0;
		inputEmail=(EditText)findViewById(R.id.login_email);
		inputPassword=(EditText)findViewById(R.id.login_pass);
		btnLogin=(Button)findViewById(R.id.login_me);
		btnLinkToRegister=(Button)findViewById(R.id.register_login_but);
		loginErrorMsg=(TextView)findViewById(R.id.login_error);
		n1=new NetworkUtil();
		inputEmail.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {}
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				if (inputEmail.getText().toString().matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+") && s.length() > 0)
	            {loginErrorMsg.setText("");
				}
	            else
	            {
	            	loginErrorMsg.setText("invalid email");
	            }
			}
		});
		
		btnLogin.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String email = inputEmail.getText().toString();
                String password = inputPassword.getText().toString();
                
                UserFunctions userFunction = new UserFunctions();
                if(userFunction.isEmailValid(email) && password.length()>0){
                	if(NetworkUtil.getConnectivityStatus(getApplicationContext())!=0){
                		loginErrorMsg.setText("");
                		new CreateNew().execute();
                	}else{
                		Toast.makeText(getApplicationContext(), NetworkUtil.getConnectivityStatusString(getApplicationContext()), Toast.LENGTH_SHORT).show();
                	}
                	
                }else{
                	Toast.makeText(getApplicationContext(), "Enter Valid Data", Toast.LENGTH_SHORT).show();
                	loginErrorMsg.setText("Invalid Data");
                	
                }
                
				
			}
		});
	
		btnLinkToRegister.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getApplicationContext(),
                        SignupActivity.class);
                startActivity(i);
                finish();
			}
		});
	}
	class CreateNew extends AsyncTask<String, String, String> {
		 
        
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage("Logging ..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
 
        /**
         * Creating product
         * */
        @Override
		protected String doInBackground(String... args) {
        	String email = inputEmail.getText().toString();
            String password = inputPassword.getText().toString();
			userFunction = new UserFunctions();
			json = userFunction.loginUser(email, password);
        	try{
        		if (json.getString(KEY_SUCCESS) != null) {
                   
                    error = json.getString(KEY_SUCCESS); 
                    if(Integer.parseInt(error) == 1){
                        // user successfully logged in
                        // Store user details in SQLite Database
                       valueok=1;
                    }else{
                        // Error in login
                    	valueok=10;
                    	error=json.getString(KEY_ERROR_MSG);
                    }
                }else{
                	error=json.getString(KEY_ERROR_MSG);
                	valueok=0;
                }
        		
        	}          	
        	catch(JSONException e){
        		e.printStackTrace();
        	}
        	
			return null;
		}
        
        
 
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        @Override
		protected void onPostExecute(String file_url) {
            // dismiss the dialog once done\
        	if(valueok==1){
        		pDialog.setMessage("Logged in succesfully");
        	}else{
        		pDialog.setMessage("Error occured");
        	}
        	 
            pDialog.dismiss();
            if(valueok==1){
            	//Toast.makeText(getApplicationContext(), "Uploaded successfully", Toast.LENGTH_SHORT).show();
            	 DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                 JSONObject json_user;
				try {
					json_user = json.getJSONObject("user");
					userFunction.logoutUser(getApplicationContext());
	                 db.addUser(json_user.getString(KEY_NAME), json_user.getString(KEY_EMAIL), json.getString(KEY_UID), json_user.getString(PHONE));                        
	                  
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                Intent dashboard = new Intent(getApplicationContext(), DashboardActivity.class);
                  
                 // Close all views before launching Dashboard
                 dashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                 startActivity(dashboard);
                  
                 // Close Login Screen
                 finish();
            }else if(valueok==10){
            	Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
            	valueok=0;
            }else{
            	Toast.makeText(getApplicationContext(), "Cannot Login Check connection", Toast.LENGTH_SHORT).show();
            	valueok=0;
            }
            
        }
 
    }

}
