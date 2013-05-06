package com.vosto.accounts.services;

import org.json.JSONException;
import org.json.JSONObject;

import com.vosto.accounts.vos.StoreUserVo;
import com.vosto.services.IRestResult;
import com.vosto.services.RestResult;

public class AuthenticateResult extends RestResult implements IRestResult {
	
	private JSONObject jsonObj;
	private StoreUserVo storeUser;
	private boolean authenticationSuccessful;
	private String errorMessage;
	 
	 public AuthenticateResult(){
		 super();
	 }
	 
	 public AuthenticateResult(int responseCode, String responseJson){
		 super(responseCode, responseJson);
	 }
	 
	 public JSONObject getJSONObject(){
		 return this.jsonObj;
	 }
	 
	 public StoreUserVo getStoreUser(){
		 return this.storeUser;
	 }
	 
	 public boolean wasAuthenticationSuccessful(){
		 return this.authenticationSuccessful;
	 }
	 
	 public String getErrorMessage(){
		 return this.errorMessage;
	 }
	 
	 
	@Override
	public boolean processJsonAndPopulate(){
		try{
			this.jsonObj = new JSONObject(this.getResponseJson());
			if(!this.jsonObj.isNull("error")){
				this.authenticationSuccessful = false;
				this.errorMessage = this.jsonObj.getString("detail");
				return true;
			}
			
			this.authenticationSuccessful = true;
			this.storeUser = new StoreUserVo();
			this.storeUser.authentication_token = this.jsonObj.getString("authentication_token");
            this.storeUser.full_name = this.jsonObj.getString("full_name");
            this.storeUser.username  = this.jsonObj.getString("username");
            this.storeUser.store_id = this.jsonObj.getString("store_id");

			return true;
		}catch(JSONException e){
			e.printStackTrace();
			return false;
		}
	}

}