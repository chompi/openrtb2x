package org.openrtb.dsp.intf.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;


import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
public class RTBAdvertiserTest {
	RTBAdvertiser advertiser = null;
	Map<String, String> seat = new HashMap<String, String>();
	List<Map<String, String>> seatList = new ArrayList<Map<String, String>>();
	
	@Before
	public void setUp() throws Exception {
		advertiser = mock(RTBAdvertiser.class);
		seat.put("BigAdExchange", "SeatID001");
		seat.put("SmallAdExchange", "SeatID002");
		seatList.add(seat);
		List<String>categories = new ArrayList<String>();
		categories.add("categories 1");
		when(advertiser.getName()).thenReturn("addidas");
		when(advertiser.getNurl()).thenReturn("http//addidas.com/nurl");
		when(advertiser.getLandingPage()).thenReturn("index.htm");
		when(advertiser.getSeats()).thenReturn(seatList);		
		when(advertiser.getCategories()).thenReturn(categories);
	}
	
	@Test
	public void advertiserNameTest()
	{
		assertTrue("Advertiser name not match",advertiser.getName().equals("addidas"));
	}
	
	@Test
	public void advertiserNurlTest()
	{
		assertTrue("Advertiser nurl not match",advertiser.getNurl().equals("http//addidas.com/nurl"));
	}
	
	@Test
	public void advertiserLandingPageTest()
	{
		assertTrue("Advertiser landingpage not match",advertiser.getLandingPage().equals("index.htm"));
	}
	
	@Test
	public void advertiserSeatTest()
	{		
		assertNotNull("Seat list is empty",advertiser.getSeats());		
	}
	@Test
	public void seatMapTest()
	{
		List<Map<String, String>> seatList = advertiser.getSeats();
		Iterator<Map<String, String>> itr = seatList.iterator();
		while(itr.hasNext())
		{
			Map<String,String> seat = itr.next();
			assertTrue("Advertiser seat not found",seat.get("BigAdExchange").equals("SeatID001"));
		}
	}
	@Test
	public void categoriesTest()
	{
		List<String> categories = advertiser.getCategories();
		assertNotNull("categories not found",advertiser.getCategories());
		assertTrue("categorie not match",categories.contains("categories 1"));
	}
	@Test
	public void constructorTest()
	{
		List<String>categories = new ArrayList<String>();
		categories.add("categories 1");
		Map<String, String> seat = new HashMap<String, String>();
		seat.put("BigAdExchange", "SeatID001");
		seat.put("SmallAdExchange", "SeatID002");
		seatList.add(seat);
		List<Map<String, String>> seatList = advertiser.getSeats();
		seatList.add(seat);	
		RTBAdvertiser advertiser = new RTBAdvertiser("index.htm", "addidas", "http//addidas.com/nurl", categories, seatList);
		assertTrue("Advertiser name not match",advertiser.getName().equals("addidas"));
		assertTrue("Advertiser nurl not match",advertiser.getNurl().equals("http//addidas.com/nurl"));
		assertTrue("Advertiser landingpage not match",advertiser.getLandingPage().equals("index.htm"));
		assertNotNull("Seat list is empty",advertiser.getSeats());
		assertNotNull("categories not found",advertiser.getCategories());
	}
	
	
}













