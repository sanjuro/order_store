package com.vosto.accounts.activities;

import java.util.Locale;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import android.widget.Toast;
import com.google.android.gcm.GCMRegistrar;
import com.vosto.HomeActivity;
import com.vosto.R;
import com.vosto.VostoBaseActivity;
import com.vosto.accounts.services.*;
import com.vosto.services.OnRestReturn;
import com.vosto.services.RestResult;
import com.vosto.utils.GCMUtils;

import static com.vosto.utils.CommonUtilities.SENDER_ID;

import com.vosto.utils.ToastExpander;
/**
 * The Sign In screen where an existing user logs in.
 *
 */
public class SignInActivity extends VostoBaseActivity implements OnRestReturn {

    public static String GCMID = "";
    private String gcmRegistrationId;

	@Override
    public void onCreate(Bundle args)
    {
        super.onCreate(args);
        setContentView(R.layout.activity_signin);

        if(!GCMUtils.checkGCMAndAlert(this, false)){
            return;
        }
        this.gcmRegistrationId = GCMRegistrar.getRegistrationId(this);
        Log.d("GCM", "GCM id: " + this.gcmRegistrationId);
    }

	/**
	 * Validates the input fields and makes the REST call to authenticate.
	 * @param v the sign in button instance
	 */
	public void signInClicked(View v){
		if(!GCMUtils.checkGCMAndAlert(this, false)){
			return;
		}
		
		TextView txtUsername = (TextView)findViewById(R.id.loginUsernameField);
		TextView txtPassword = (TextView)findViewById(R.id.loginPasswordField);
		String username = txtUsername.getText().toString().trim();
		String password = txtPassword.getText().toString().trim();
		
		boolean inputValid = true;
		if(username.equals("")){
            txtUsername.setError("Enter your username.");
			inputValid = false;
		}
		if(password.equals("")){
			txtPassword.setError("Enter your password.");
			inputValid = false;
		}
		
		if(!inputValid){
			return;
		}
		
		//Make the REST call:
		AuthenticationService service = new AuthenticationService(this, this);
		service.setUsername(username);
		service.setPassword(password);
		service.execute();
	}

	/**
	 * Called from within the base RestService after a rest call completes.
	 * @param result Can be any result type. This function should check the type and handle accordingly. 
	 */
	@Override
	public void onRestReturn(RestResult result) {
		if(result == null){
			this.showAlertDialog("Login Failed", "Please try again.");
			return;
		}
		if(result instanceof AuthenticateResult){

            /*
            *  Register the device with GCM if not already registered.
            *  GCM will respond and the callback in GCMIntentService will be called.
            */
            Log.d("GCM", "GCM Registrsion id: " + this.gcmRegistrationId );
            if(this.gcmRegistrationId != null && this.gcmRegistrationId.equals("")){
                // GCM is supported, but device has not been registered yet.
                Log.d("GCM", "Calling gcm register...");
                GCMRegistrar.register(this, SENDER_ID);
            }else{
                Log.d("GCM", "Not registering with gcm");
            }

			processAuthenticateResult((AuthenticateResult)result);
		}
	}
	
	/**
	 * Called when the authentication rest call returns.
	 * If the auth is successful, save the token, etc and redirect to the home screen.
	 * @param authResult
	 */
	private void processAuthenticateResult(AuthenticateResult authResult){
		SharedPreferences settings = getSharedPreferences("VostoPreferences", 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("userToken", "");
		editor.putString("userName", "");
		editor.commit();
		
		if(!authResult.wasAuthenticationSuccessful()){
			this.showAlertDialog("Login Failed", authResult.getErrorMessage());
		}else{
			 // Save the auth token in the app's shared preferences.
            editor = settings.edit();
            editor.putString("userToken", authResult.getStoreUser().authentication_token);
            editor.putString("userName", authResult.getStoreUser().full_name);
            editor.putString("userStoreId", authResult.getStoreUser().store_id);
            editor.commit();
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            finish();
		}
	}

	
}