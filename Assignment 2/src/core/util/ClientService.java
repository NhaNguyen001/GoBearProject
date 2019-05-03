package core.util;

import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import okhttp3.Credentials;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ClientService {
	public String application_json = "application/json";
	public final MediaType JSON = MediaType.parse(application_json);


	public JsonObject sendGetRequestBasic(String url , String key,String secret,Map<String,String> params) {
		Log.logInfo(String.format("Start send Get Request Basic url %s key %s secret %s params %s", url, key,
				secret,params.toString()));
		
		OkHttpClient client = new OkHttpClient();

	    String cred = Credentials.basic(key,secret);
	    
	    HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
	    for (Entry<String, String> entry : params.entrySet()) {
	    	  urlBuilder.addQueryParameter(entry.getKey(), entry.getValue());
		}
	    
	    String urlGet = urlBuilder.build().toString();
	    
	    Request request = new Request.Builder()
	            .url(urlGet)
	            .addHeader("Authorization", cred)
	            .addHeader("ContentType", application_json)
	            .addHeader("Accept", application_json)
	            .build();
	    
	    JsonObject jObject = null;
	    try (Response response = client.newCall(request).execute()) {
	    	Log.logInfo(String.format("Get response from Request Basic url %s key %s secret %s params %s", url, key,
					secret,params.toString()));
			
			// Deserialize HTTP response to concrete type.
			String str = response.body().string();
			JsonParser parser = new JsonParser();
			jObject = parser.parse(str).getAsJsonObject();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	    
	    return jObject;
	}
	
	public JsonObject sendPostRequest(String url, String token, String json) {
		Log.logInfo(String.format("start send Post Request url %s token %s json %s", url, token,
				json));
		
		JsonObject jObject = null;
		OkHttpClient client = new OkHttpClient();
		RequestBody body = RequestBody.create(JSON, json);
		Request request;
		if (!token.isEmpty()) {
			request = new Request.Builder().url(url).post(body).addHeader("Authorization", "Bearer " + token)
					.addHeader("Content-Type", application_json).build();
		} else {
			request = new Request.Builder().url(url).post(body).addHeader("Content-Type", application_json).build();
		}

		// Execute the request and retrieve the response.
		try (Response response = client.newCall(request).execute()) {
			Log.logInfo(String.format("Get Response from Post Request url %s token %s json %s", url, token,
					json));
			
			// Deserialize HTTP response to concrete type.
			String str = response.body().string();
			JsonParser parser = new JsonParser();
			jObject = parser.parse(str).getAsJsonObject();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return jObject;
	}

	public String sendGet(String url, String token) {
		Log.logInfo(String.format("start send Get url %s token %s", url, token));
		String strResponse = "";
		OkHttpClient client = new OkHttpClient();
		Request request;

		if (token.isEmpty()) {
			request = new Request.Builder().url(url).build();
		} else {
			request = new Request.Builder().url(url).addHeader("Authorization", "Bearer " + token).build();
		}

		// Execute the request and retrieve the response.
		try (Response response = client.newCall(request).execute()) {
			Log.logInfo(String.format("Get response from Get url %s token %s", url, token));
			// Deserialize HTTP response to concrete type.
			strResponse = response.body().string();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return strResponse;
	}

	public JsonObject sendGetRequest(String url, String token) {
		JsonParser parser = new JsonParser();
		String str = sendGet(url, token);
		JsonObject jObject = str.isEmpty() ? parser.parse(str).getAsJsonObject() : null;
		return jObject;
	}
	
}
