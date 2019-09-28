package com.synezia.client.http.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor
public class ClientSessionVerifyResponse {
	
	private boolean status;
	private String error;
	private String username;
	private String ip;
	private String token;
	
}
