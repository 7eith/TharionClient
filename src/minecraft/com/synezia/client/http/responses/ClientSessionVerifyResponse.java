package com.synezia.client.http.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Snkh
 *	28 sept. 2019
 */


@Getter @Setter @AllArgsConstructor
public class ClientSessionVerifyResponse {
	
	private boolean status;
	private String error;
	private String username;
	private String ip;
	private String token;
	
}
