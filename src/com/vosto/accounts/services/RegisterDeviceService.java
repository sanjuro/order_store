package com.vosto.accounts.services;

import org.json.JSONException;
import org.json.JSONObject;

import android.provider.Settings.Secure;

import com.vosto.VostoCustomerApp;
import com.vosto.services.OnRestReturn;
import com.vosto.services.RequestMethod;
import com.vosto.services.RestService;
import com.vosto.services.ResultType;

import static com.vosto.utils.CommonUtilities.SERVER_URL;

public class RegisterDeviceService extends RestService {
	private VostoCustomerApp context;
	private String gcmId; //The id assigned and returned by gcm when registering the device with them
	
	public RegisterDeviceService(OnRestReturn listener, VostoCustomerApp context, String gcmId){
		super(SERVER_URL + "/devices/register", RequestMethod.POST, ResultType.REGISTER_DEVICE, listener, context);
		this.context = context;
		this.gcmId = gcmId;
	}
	
	public void setGcmId(String gcmId){
		this.gcmId = gcmId;
	}


	public String getRequestJson(){
		try{
			JSONObject root = new JSONObject();
			root.put("authentication_token", this.context.getAuthenticationToken());
			JSONObject device = new JSONObject();
			device.put("device_type", "android");
			device.put("device_token", this.gcmId);
			device.put("device_identifier", Secure.getString(context.getContentResolver(),
                    Secure.ANDROID_ID));
			root.put("device", device);
			return root.toString();
		}catch(JSONException e){
			e.printStackTrace();
			return "";
		}
	}
	
	/* Request format:
	 * {
    "authentication_token": "CXTTTTED2ASDBSD3",
    "device": {
        "device_type": "blackberry",
        "device_identifier": "NFW3-44LG-E557-E",
        "device_token": "1234123",
    }
}
	 */


}