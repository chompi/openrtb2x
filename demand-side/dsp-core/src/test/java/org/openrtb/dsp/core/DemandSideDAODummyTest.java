
package org.openrtb.dsp.core;

import java.io.File;


import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.openrtb.dsp.intf.model.DSPException;
import org.openrtb.dsp.intf.model.DemandSideDAO;
import org.openrtb.dsp.intf.model.RTBAdvertiser;
import org.openrtb.dsp.intf.model.RTBExchange;


public class DemandSideDAODummyTest implements DemandSideDAO {	
	private String dbLocation;
	
	private final ObjectMapper mapper = new ObjectMapper();
	
	// data
	private final ConcurrentMap<String, Integer> properties = new ConcurrentHashMap<String, Integer>();	
	private final ConcurrentMap<String, RTBExchange> exchanges = new ConcurrentHashMap<String, RTBExchange>();
	private final ConcurrentMap<String, RTBAdvertiser> advertisers = new ConcurrentHashMap<String, RTBAdvertiser>();
	
	// encapsulate all the config properties in the Json file backed data store in a temporary class
	// this is used by a Jackson object mapper to load data from a JSON file.
	// this is not designed to for concurrent access from multiple threads simultaneous, 
	// hence loadData and persistData are the only methods (synchronized) that should 
	// use this internal class

	@JsonSerialize(include=Inclusion.NON_DEFAULT)
	@JsonPropertyOrder({"serverport", "requestTO", "offerTO", "exchanges", "advertisers"})	
	class DSPProps {
	    @JsonProperty("serverport")
		int dspServerPort;

	    @JsonProperty("requestTO")
		int defaultReqTimeout;

	    @JsonProperty("offerTO")
	    int defaultOfferTimeout;

	    @JsonProperty("exchanges")
		List<RTBExchange> exchanges;

	    @JsonProperty("advertisers")
		List<RTBAdvertiser> advertisers;
				
	    @JsonProperty("serverport")
		int getDspServerPort() {
			return dspServerPort;
		}
		void setDspServerPort(int dspServerPort) {
			this.dspServerPort = dspServerPort;
		}

		@JsonProperty("requestTO")
		int getDefaultReqTimeout() {
			return defaultReqTimeout;
		}
		void setDefaultReqTimeout(int defaultReqTimeout) {
			this.defaultReqTimeout = defaultReqTimeout;
		}

	    @JsonProperty("offerTO")
		int getDefaultOfferTimeout() {
			return defaultOfferTimeout;
		}
		void setDefaultOfferTimeout(int defaultOfferTimeout) {
			this.defaultOfferTimeout = defaultOfferTimeout;
		}

	    @JsonProperty("exchanges")
		List<RTBExchange> getExchanges() {
			return exchanges;
		}
		void setExchanges(List<RTBExchange> exchanges) {
			this.exchanges = exchanges;
		}

	    @JsonProperty("advertisers")
		List<RTBAdvertiser> getAdvertisers() {
			return advertisers;
		}
		void setAdvertisers(List<RTBAdvertiser> advertisers) {
			this.advertisers = advertisers;
		}
	}
	
	public synchronized void reload() throws DSPException {
		this.loadData(dbLocation);
	}
	
	// the get method of ConcurrentMap is not blocking, hence make this synchronized
	@Override
	public synchronized long getServerPort() {
		return properties.get("server_port");
	}

	@Override
	public ConcurrentMap<String, RTBExchange> getExchanges() {
		return exchanges;
	}

	@Override
	public ConcurrentMap<String, RTBAdvertiser> getAdvertisers() {
		return advertisers;
	}

	@Override
	public synchronized void loadData(String dbLocation) throws DSPException {
		try {			
			if ((dbLocation != null) && (dbLocation != "")) {
				this.dbLocation = dbLocation;
			} else {  // otherwise use a default location and filename relative to this class
				this.dbLocation = getClass().getResource("dspConf.json").getFile();
			}
			mapper.enableDefaultTyping();
			// read the properties as an object using ObjectMapper
			DSPProps props = mapper.readValue(new File(this.dbLocation), DSPProps.class);
			
			this.properties.put("server_port", props.dspServerPort);
			this.properties.put("request_timeout", props.defaultReqTimeout);
			this.properties.put("offer_timeout", props.defaultOfferTimeout);
			
			for (RTBExchange ex : props.exchanges) {
				this.exchanges.put(ex.getOrgName(), ex);
			}
			
			for (RTBAdvertiser adv : props.advertisers) {
				this.advertisers.put(adv.getLandingPage(), adv);
			}
			
		} catch (Exception e) {
			throw new DSPException(e.getMessage());
		}
	}

	@Override
	public synchronized long getDefaultTimeout(String string) {
		return properties.get(string);
	}	
	
}
