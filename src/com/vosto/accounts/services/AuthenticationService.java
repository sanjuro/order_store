package com.vosto.accounts.services;

import org.apache.http.StatusLine;
import org.json.JSONException;
import org.json.JSONObject;

import com.vosto.VostoBaseActivity;
import com.vosto.services.OnRestReturn;
import com.vosto.services.RequestMethod;
import com.vosto.services.RestService;
import com.vosto.services.ResultType;

import static com.vosto.utils.CommonUtilities.SERVER_URL;

public class AuthenticationService extends RestService {
	
	private String username;
	private String password;
	
	public AuthenticationService(OnRestReturn listener, VostoBaseActivity activity){
		super(SERVER_URL + "/store_users/authenticate", RequestMethod.POST, ResultType.AUTHENTICATE_STORE_USER, listener, activity);
	}
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getRequestJson(){
		try{
			JSONObject root = new JSONObject();
			root.put("authentication_token", "DXTTTTED2ASDBSD3");
			root.put("username", this.username);
			root.put("password", this.password);
			return root.toString();
		}catch(JSONException e){
			e.printStackTrace();
			return "";
		}
	}
	
	@Override
	protected AuthenticateResult getRestResult(StatusLine statusLine, String responseJson){
		AuthenticateResult result = new AuthenticateResult(200, responseJson);
		result.processJsonAndPopulate();
		return result;
	}

}