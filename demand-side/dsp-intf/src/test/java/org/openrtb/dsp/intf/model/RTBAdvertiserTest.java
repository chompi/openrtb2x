package org.openrtb.dsp.intf.model;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;

//This Test class is used to test the functionality of RTBAdvertiser class
public class RTBAdvertiserTest {
	RTBAdvertiser advertiser = null;
	RTBAdvertiser advertiserSeatTest = null;
	RTBAdvertiser advertiserCategoriesTest = null;
	Map<String, String> seat ;
	
	@Before
	public void setUp()  {
		advertiser = mock(RTBAdvertiser.class);
		seat = new HashMap<String, String>();
		seat.put("BigAdExchange", "SeatID001");
		seat.put("SmallAdExchange", "SeatID002");
		List<String>categories = new ArrayList<String>();
		categories.add("categories 1");
		when(advertiser.getName()).thenReturn("addidas");
		when(advertiser.getNurl()).thenReturn("http//addidas.com/nurl");
		when(advertiser.getLandingPage()).thenReturn("index.htm");
		when(advertiser.getSeats()).thenReturn(seat);		
		when(advertiser.getCategories()).thenReturn(categories);
	}
	
	@Before
	public void setUpAddSeat()  {
		advertiserSeatTest = new RTBAdvertiser();
		advertiserSeatTest.addSeat("MyAdExchange", "SeatID8881");
		advertiserSeatTest.addSeat("BidAdExchange", "SeatID9902");		
		advertiserSeatTest.addSeat("AdExchange", "SeatID4445902");	
	}
	
	@Before
	public void setUpAddCategories() {
		advertiserCategoriesTest = new RTBAdvertiser();
		List<String>categories = new ArrayList<String>();
		categories.add("categories 1");
		List<String>categories2 = new ArrayList<String>();
		categories2.add("categories 2");
		advertiserCategoriesTest.addCategories(categories);
		advertiserCategoriesTest.addCategories(categories2);
	}
	

	/**
	 * This method test the parameterize constructor of Advertiser class
	 */
	@Test
	public void constructorTest()
	{
		List<String>categories = new ArrayList<String>();
		categories.add("categories 1");
		Map<String, String> seat = new HashMap<String, String>();
		seat.put("BigAdExchange", "SeatID001");
		seat.put("SmallAdExchange", "SeatID002");
		RTBAdvertiser advertiser = new RTBAdvertiser("index.htm", "addidas", "http//addidas.com/nurl", categories, seat);
		assertTrue("Advertiser name not match",advertiser.getName().equals("addidas"));
		assertTrue("Advertiser nurl not match",advertiser.getNurl().equals("http//addidas.com/nurl"));
		assertTrue("Advertiser landingpage not match",advertiser.getLandingPage().equals("index.htm"));
		assertNotNull("Seat list is empty",advertiser.getSeats());
		assertNotNull("categories not found",advertiser.getCategories());
	}
	
	
	/**
	 * This method test the getName property of Advertiser class
	 */
	@Test
	public void advertiserNameTest()
	{
		assertTrue("Advertiser name not match",advertiser.getName().equals("addidas"));
	}
	
	/**
	 * This method test the getNurl property of Advertiser class
	 */
	@Test
	public void advertiserNurlTest()
	{
		assertTrue("Advertiser nurl not match",advertiser.getNurl().equals("http//addidas.com/nurl"));
	}

	/**
	 * This method test the getLandingPage property of Advertiser class
	 */
	@Test
	public void advertiserLandingPageTest()
	{
		assertTrue("Advertiser landingpage not match",advertiser.getLandingPage().equals("index.htm"));
	}

	/**
	 * This method test the getSeats property of Advertiser class
	 */
	@Test
	public void advertiserSeatTest()
	{		
		assertNotNull("Seat list is empty",advertiser.getSeats());		
	}

	/**
	 * This method test the seats property of Advertiser class
	 */
	@Test
	public void seatMapTest()
	{
		
			assertTrue("Advertiser seat not found",seat.get("BigAdExchange").equals("SeatID001"));
		
	}
	/**
	 * This method test the categories property of Advertiser class
	 */
	@Test
	public void categoriesTest()
	{
		List<String> categories = advertiser.getCategories();
		assertNotNull("categories not found",advertiser.getCategories());
		assertTrue("categorie not match",categories.contains("categories 1"));
	}
	
	/**
	 * This method test the addSeat property of Advertiser class
	 */
	@Test
	public void addSeatsTest()
	{		
		assertTrue("Seat should have Two object",advertiserSeatTest.getSeats().size()==3);
	}
	
	/**
	 * This method test the addCategories property of Advertiser class
	 */
	@Test
	public void addCategoriesTest()
	{		
		assertTrue("Categories should have Two object ",advertiserCategoriesTest.getCategories().size()==2);
		
	}
	
}













