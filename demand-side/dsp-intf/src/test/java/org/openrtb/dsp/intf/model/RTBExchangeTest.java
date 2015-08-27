package org.openrtb.dsp.intf.model;


import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
/**
 * This class is used to test the functionality of RTBExchanger class
 */
public class RTBExchangeTest {

	RTBExchange exchanger = null;
	
	@Before
	public void setUp() throws Exception {
		exchanger = mock(RTBExchange.class);
		when(exchanger.getOrgName()).thenReturn("LGExchange");
		when(exchanger.getRtbContentType()).thenReturn("application/json");
		when(exchanger.getRtbServiceUrl()).thenReturn("http://bigadex.com/rtb");		
	}

	/**
	 * This method is used to test the getOrgName property of RTBExchanger class
	 */
	@Test
	public void orgNameTest()
	{
		assertTrue("Exchanger name not match",exchanger.getOrgName().equals("LGExchange"));
	}

	/**
	 * This method is used to test the getRtbContentType property of RTBExchanger class
	 */
	@Test
	public void contentTypeTest()
	{
		assertTrue("Content type not match",exchanger.getRtbContentType().equals("application/json"));
	}
	
	/**
	 * This method is used to test the getRtbServiceUrl property of RTBExchanger class
	 */
	@Test
	public void serviceUrlTest()
	{
		assertTrue("service url not match",exchanger.getRtbServiceUrl().equals("http://bigadex.com/rtb"));
	}
	/**
	 * This method is used to test the parameterize constructor of RTBExchanger class
	 */
	@Test
	public void constructorAllParamTest()
	{
		RTBExchange exchanger = new RTBExchange("LGExchange", "http://bigadex.com/rtb", "application/json"); 
		assertTrue("Exchanger name not match",exchanger.getOrgName().equals("LGExchange"));
		assertTrue("Content type not match",exchanger.getRtbContentType().equals("application/json"));
		assertTrue("service url not match",exchanger.getRtbServiceUrl().equals("http://bigadex.com/rtb"));
	}
	
	/**
	 * This method is used to test the single parameterize constructor of RTBExchanger class
	 */
	@Test
	public void constructorTest()
	{
		RTBExchange exchanger1 = new RTBExchange("LGExchange", "http://bigadex.com/rtb", "application/json");
		RTBExchange exchanger = new RTBExchange(exchanger1);
		assertTrue("Exchanger name not match",exchanger.getOrgName().equals("LGExchange"));
		assertTrue("Content type not match",exchanger.getRtbContentType().equals("application/json"));
		assertTrue("service url not match",exchanger.getRtbServiceUrl().equals("http://bigadex.com/rtb"));
	}
}
