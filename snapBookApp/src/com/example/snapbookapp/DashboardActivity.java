package com.example.snapbookapp;



import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class DashboardActivity extends Activity {
	private static final int ALERTDIALOG=0;
	private DialogFragment dialog;
    UserFunctions userFunctions;
    MenuItem item;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       item=(MenuItem)findViewById(R.id.title_menu_item);
        userFunctions = new UserFunctions();
        if(userFunctions.isUserLoggedIn(getApplicationContext())){
            setContentView(R.layout.dashboard);
            Button snapBookCreate=(Button)findViewById(R.id.createSnapbook);
        	snapBookCreate.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(DashboardActivity.this,ListViewImagesActivity.class);
				startActivity(i);
			};
        	});
        }else{
            // user is not logged in show login screen
            Intent login = new Intent(getApplicationContext(), MainActivity.class);
            login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(login);
            // Closing dashboard screen
            finish();
        } 
        
    }
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.logout, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.title_menu_item:
			showDialogFragment(ALERTDIALOG);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	void showDialogFragment(int dialogid){
		switch (dialogid){
		case ALERTDIALOG:
			dialog=AlertDialogFragment.newInstance();
			dialog.show(getFragmentManager(), "Alert");
			break;
		}
	}
	protected void continueShutdown(boolean res){
		if(res){
		
			userFunctions.logoutUser(getApplicationContext());
			finish();
			/*Intent login = new Intent(getApplicationContext(), MainActivity.class);
            login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(login);*/

		}else{
			dialog.dismiss();
		}
	}
	public static class AlertDialogFragment extends DialogFragment{
		public static AlertDialogFragment newInstance(){
			return new AlertDialogFragment();
		}
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			
			return new AlertDialog.Builder(getActivity())
					.setMessage("Do you really want to Log Out?")
					.setCancelable(false)
					.setNegativeButton("No", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							// TODO Auto-generated method stub
							((DashboardActivity)getActivity()).continueShutdown(false);
						}
					})
					.setPositiveButton("YES", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							// TODO Auto-generated method stub
							((DashboardActivity)getActivity()).continueShutdown(true);
						}
					}).create();
		}
	}
	
}