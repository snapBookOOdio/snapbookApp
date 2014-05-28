package com.example.snapbookapp;

import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.net.MailTo;
import android.os.Bundle;

public class MainActivity extends ActionBarActivity {
	Button login,signup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        login=(Button)findViewById(R.id.login);
        signup=(Button)findViewById(R.id.sign_up);
        login.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent log=new Intent(MainActivity.this,LoginActivity.class);
				startActivity(log);
				
			}
		});
        signup.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent log1=new Intent(MainActivity.this,SignupActivity.class);
				startActivity(log1);
				
			}
		});
    }


   

}
