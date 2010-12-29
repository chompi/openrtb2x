package org.openrtb.common.model;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

@JsonSerialize(include = Inclusion.NON_DEFAULT)
@JsonPropertyOrder( { "requestToken", "statusCode", "statusMessage" })
public class Status {

	@JsonProperty("requestToken")
	private String requestToken;

	@JsonProperty("statusCode")
	private int statusCode;

	@JsonProperty("statusMessage")
	private String statusMessage;

	public Status() {
		this(null, 0, null);
	}

	public Status(String requestToken, int statusCode, String statusMessage) {
		this.requestToken = requestToken;
		this.statusCode = statusCode;
		this.statusMessage = statusMessage;
	}

	public String getRequestToken() {
		return requestToken;
	}

	public void setRequestToken(String requestToken) {
		this.requestToken = requestToken;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getStatusMessage() {
		return statusMessage;
	}

	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}
}
