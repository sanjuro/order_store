package com.vosto;

import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.vosto.accounts.activities.SignInActivity;

/**
 * This is the base class from which all activities should inherit.
 * It provides common functionality such as checking user login status, getting the auth token,
 * getting and saving the current cart, etc.
 *
 *
 */
public abstract class VostoBaseActivity extends Activity {
	
	// Subclasses can display a basic please wait dialog with spinner:
	public ProgressDialog pleaseWaitDialog;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
	}
	
	/**
	 * Gets the base application context. This should be used wherever an application context is needed.
	 * 
	 */
	public VostoCustomerApp getContext(){
		return (VostoCustomerApp)getApplicationContext();
	}

	/**
	 * Returns the stores user auth token, or returns the default Android token if there isn't a user token.
	 */
	public String getAuthenticationToken(){
		VostoCustomerApp context = (VostoCustomerApp)getApplicationContext();
		return context.getAuthenticationToken();
	}
	
	public boolean isUserSignedIn(){
		SharedPreferences settings = getSharedPreferences("VostoPreferences", 0);
		String token = settings.getString("userToken", "");
		return !token.trim().equals("");
	}

    /**
     * Clears all the locally stored data (auth token, pin, username, cart, order)
     *
     * @param v The logout button, at the moment it's just the user name label for debugging purposes,
     * the design doesn't have a real logout button yet.
     */
    public void logout(View v){
        SharedPreferences settings = getSharedPreferences("VostoPreferences", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("userToken", "");
        editor.putString("userName", "");
        editor.commit();

        //Blank slate, redirect to signin page for new user signin:
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
        finish();
    }
    
    /** 
     * Shows a standard alert dialog with Close button in this activity.
     * @param title
     * @param message
     */
    public void showAlertDialog(String title, String message){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
        .setMessage(message)
        .setCancelable(false)
        .setNegativeButton("Close",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
	}
    
    /**
     * Dismisses the please wait dialog if it is showing.
     */
    public void dismissPleaseWaitDialog(){
    	if(this.pleaseWaitDialog != null && this.pleaseWaitDialog.isShowing()){
    		this.pleaseWaitDialog.dismiss();
    	}
    }

    public static String generateString()
    {
        char[] chars = "0123456789".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        return sb.toString();
    }

	/*
	
	public abstract void storesPressed();

	public void cartPressed(){
		  Intent intent = new Intent(this, CartActivity.class);
      	  startActivity(intent);
	}

	public abstract void ordersPressed();

	public abstract void settingsPressed();
	
	*/
    
}