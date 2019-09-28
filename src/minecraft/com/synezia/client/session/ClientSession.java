package com.synezia.client.session;

import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.synezia.client.http.responses.ClientSessionVerifyResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ClientSession {
	
	@Getter public static ClientSession session;
	@Getter private String sessionToken;
	@Getter @Setter private boolean verified;
	
	@Getter @Setter private String verifyURL = "http://auth.synezia.network:8080/verify"; 
	
	/**
	 * Initialize ClientSession with his token 
	 * @param sessionToken JWT Token
	 */
	
	public ClientSession(String sessionToken)
	{
		this.sessionToken = sessionToken;
		this.session = this;
	}
	
	/**
	 * Return token for HttpRequests
	 * @return String : bearerToken
	 */
	
	public String getToken()
	{
		return new Gson().toJson(new ClientSessionTokenBuilder(this.getSessionToken()));
	}
	
	/**
	 * Verify if Token is good
	 * @throws IOException Error when calling Auth or when parsing JSON
	 */
	
	public void verify()
	{		
		Response response = null;
		ClientSessionVerifyResponse res = null;
		
		OkHttpClient client = new OkHttpClient();
		MediaType mediaType = MediaType.parse("application/json");
		RequestBody body = RequestBody.create(mediaType, this.getToken());
		
		Request request = new Request.Builder()
				.url(this.getVerifyURL())
				.post(body)
				.addHeader("Content-Type", "application/json")
				.build();
		
		try {
			response = client.newCall(request).execute();
		} catch (IOException e) { e.printStackTrace(); }

		try {
			res = new Gson().fromJson(response.body().string(), ClientSessionVerifyResponse.class);
		} catch (JsonSyntaxException | IOException e) { e.printStackTrace(); }
		
		if (res.isStatus())
			this.verified = true;
	}
}

@AllArgsConstructor
class ClientSessionTokenBuilder 
{
	
	private String token;
	
}

