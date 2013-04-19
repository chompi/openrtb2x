/*
 * Copyright (c) 2010, The OpenRTB Project
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   1. Redistributions of source code must retain the above copyright notice,
 *      this list of conditions and the following disclaimer.
 *
 *   2. Redistributions in binary form must reproduce the above copyright
 *      notice, this list of conditions and the following disclaimer in the
 *      documentation and/or other materials provided with the distribution.
 *
 *   3. Neither the name of the OpenRTB nor the names of its contributors
 *      may be used to endorse or promote products derived from this
 *      software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package org.openrtb.dsp.intf.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;


/**
 * This class implements a prototype Advertiser engaged in the real time bidding framework.
 */
@JsonSerialize(include=Inclusion.NON_DEFAULT)
@JsonPropertyOrder({"landingPage", "name", "nurl", "categories", "seats"})
public class RTBAdvertiser {

    public RTBAdvertiser() {
		super();
	}

	@JsonProperty("landingPage")
	public String landingPage;
    
    @JsonProperty("name")
	public String name; 

	// notification URL of the Advertiser to receive Win (or Loss) notifications directly from SSP
    @JsonProperty("nurl")
	public String nurl;
	// List of categories this Advertiser belongs to
    @JsonProperty("categories")
	private List<String> categories = new ArrayList<String>();

	// Seat ID assigned by each Exchange (key: SSPs orgname, value: seat ID)
    @JsonProperty("seats")
	private List<Map<String, String>> seats = new ArrayList<Map<String, String>> ();
	

	public RTBAdvertiser(String lp, String name, String nurl, List<String> cats,  List<Map<String, String>> seats) {
    	setLandingPage(lp);
    	setName(name);
    	setNurl(nurl);
    	setCategories(cats);
    	setSeats(seats);
    }

    @JsonProperty("landingPage")
   public String getLandingPage() {
		return this.landingPage;
	}
 
    public void setLandingPage(String lp) {
    	 this.landingPage = lp;
	}
   
    @JsonProperty("name")
   public String getName() {
		return this.name;
	}
	
    public void setName(String name) {
		this.name = name;
	}
	
    @JsonProperty("nurl")
   public String getNurl() {
		return this.nurl;
	}

    public void setNurl(String nurl) {
		this.nurl = nurl;
	}
	
    @JsonProperty("categories")
	public List<String> getCategories() {
		return categories;
	}
	
	public void setCategories(List<String> cats) {
		this.categories = cats;
	}

	  @JsonProperty("seats")
	public List<Map<String, String>> getSeats() {
		return seats;
	}

	public void setSeats(List<Map<String, String>> seats) {
		this.seats = seats;
	}

	
	// additional helper methods

	public String getSeat(String exchangeName) {
	    
		Iterator<Map<String, String>>itr = seats.iterator();
		String exchangeValue = null;
		while(itr.hasNext())
		{
			Map<String, String> map = itr.next();
			exchangeValue=map.get(exchangeName);	
		}				
		return exchangeValue;
	}


	public void addSeat(List<Map<String, String>> seats) {
	
		seats.addAll(seats);
	}

	public void addCategories(List<String> categories) {
		for (String cat : categories) {
			this.categories.add(cat);
		}
	}

	public void addCategory(String cat) {
		this.categories.add(cat);
	}
	
}
