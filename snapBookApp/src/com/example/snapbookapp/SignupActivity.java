package com.example.snapbookapp;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SignupActivity extends Activity {


	EditText name,email,password1,password2,phone;
	Button signin,login_signin;
	TextView registerErrorMsg;
	private static String KEY_SUCCESS = "success";
    private static String KEY_ERROR = "error";
    private static String KEY_ERROR_MSG = "error_msg";
    private static String KEY_UID = "uid";
    private static String KEY_NAME = "name";
    private static String KEY_EMAIL = "email";
    private static String PHONE = "phone";
    String error;
    NetworkUtil n1;
    private ProgressDialog pDialog;
    int valueok;
    UserFunctions userFunction;
    JSONObject json;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signup_activity);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		valueok=0;
		StrictMode.setThreadPolicy(policy); 
		name=(EditText)findViewById(R.id.editText1);
		email=(EditText)findViewById(R.id.editText2);
		password1=(EditText)findViewById(R.id.editText3);
		password2=(EditText)findViewById(R.id.editText4);
		phone=(EditText)findViewById(R.id.editText5);
		signin=(Button)findViewById(R.id.button1);
		login_signin=(Button)findViewById(R.id.button2);
		registerErrorMsg=(TextView)findViewById(R.id.register_error);
		n1=new NetworkUtil();
		
		signin.setOnClickListener(new View.OnClickListener() {
		
		
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String name1=name.getText().toString();
				String email1=email.getText().toString();
				String pass1=password1.getText().toString();
				String pass2=password2.getText().toString();
				String phone1=phone.getText().toString();
				UserFunctions userFunction = new UserFunctions();
				if(name1.length()>0 && email1.length()>0 && pass1.length()>0 && pass2.length()>0 && phone1.length()>0){
					if(userFunction.isEmailValid(email1)){
						if(pass1.equals(pass2)){
							if(NetworkUtil.getConnectivityStatus(getApplicationContext())!=0){
								new CreateNew().execute();
							}else{
								Toast.makeText(getApplicationContext(), NetworkUtil.getConnectivityStatusString(getApplicationContext()), Toast.LENGTH_SHORT).show();
							}
							
						}else{
							Toast.makeText(getApplicationContext(), "Password not matching", Toast.LENGTH_LONG).show();
						}
					}else{
						Toast.makeText(getApplicationContext(), "Invalid Email id", Toast.LENGTH_LONG).show();
					}
				}else{
					Toast.makeText(getApplicationContext(), "Some values missing", Toast.LENGTH_LONG).show();
				}
			}
		});
		login_signin.setOnClickListener(new View.OnClickListener() {
			 
            @Override
			public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        LoginActivity.class);
                startActivity(i);
                // Close Registration View
                finish();
            }
        });
	}
	class CreateNew extends AsyncTask<String, String, String> {
		 
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SignupActivity.this);
            pDialog.setMessage("Please wait signing you up..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
 
        /**
         * Creating product
         * */
        @Override
		protected String doInBackground(String... args) {
        	String name1=name.getText().toString();
			String email1=email.getText().toString();
			String pass1=password1.getText().toString();
			String pass2=password2.getText().toString();
			String phone1=phone.getText().toString();
			userFunction = new UserFunctions();
				 json = userFunction.registerUser(name1, email1, pass1,phone1);
				 
					try {
								
			                    if (json.getString(KEY_SUCCESS) != null) {
			                        
			                        String res = json.getString(KEY_SUCCESS); 
			                        if(Integer.parseInt(res) == 1){
			                            // user successfully registred
			                            // Store user details in SQLite Database
			                           valueok=1;
			                        }else{
			                            // Error in registration
			                        	valueok=10;
			                        	error=json.getString(KEY_ERROR_MSG);
			                        }
			                    }else{
			                    	error=json.getString(KEY_ERROR_MSG);
			                    	valueok=10;
			                    }
			                } catch (JSONException e) {
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
        		pDialog.setMessage("Done succesfully");
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
                 // Close Registration Screen
                 finish();
            }else if(valueok==10){
            	Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
            	valueok=0;
            }else{
            	Toast.makeText(getApplicationContext(), "Cannot Upload Check connection", Toast.LENGTH_SHORT).show();
            	valueok=0;
            }
            
        }
 
    }
}