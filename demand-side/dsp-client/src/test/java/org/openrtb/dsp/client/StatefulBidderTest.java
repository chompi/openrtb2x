package org.openrtb.dsp.client;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.avro.AvroRemoteException;
import org.junit.Before;
import org.junit.Test;
import org.openrtb.common.api.App;
import org.openrtb.common.api.Banner;
import org.openrtb.common.api.BidRequest;
import org.openrtb.common.api.BidResponse;
import org.openrtb.common.api.Impression;
import org.openrtb.common.api.Site;
import org.openrtb.common.api.Video;
import org.openrtb.dsp.intf.model.DSPException;
import org.openrtb.dsp.intf.model.RTBAdvertiser;
import org.openrtb.dsp.intf.model.RTBExchange;
import org.openrtb.dsp.intf.model.RTBRequestWrapper;

public class StatefulBidderTest {
	StatefulBidder bidder = null;
	BidRequest request = null;

	@Before
	public void setUp() throws Exception {
		bidder = new StatefulBidder();
		request = mock(BidRequest.class);
		Banner banner = new Banner();
		banner.setH(25);
		banner.setW(30);
		Impression imp = new Impression();
		imp.setId("10212sdsa1");
		imp.setBanner(banner);
		imp.setBidfloor(new Float(10.085) );
		Video video = new Video();
		List<CharSequence> mimes = new ArrayList<CharSequence>();
		mimes.add("video/x-mswmv");
		video.setMimes(mimes);
		video.setLinearity(54);
		video.setMinduration(100);
		video.setMaxduration(500);
		video.setProtocol(200);
		imp.setVideo(video);
		Site site = new Site();
		site.setId("124545sfdghs");
		App app = new App();
		app.setId("appTest");
		app.setBundle("com.mygame");
		when(request.getId()).thenReturn(
				"ad1d762d6d9719b6b3c9e09f6433a76d9b593738");
		when(request.getImp()).thenReturn(
				Collections.<Impression> singletonList(imp));
		when(request.getSite()).thenReturn(site);
		when(request.getApp()).thenReturn(app);
		when(request.getWseat()).thenReturn(
				Collections.<CharSequence> singletonList("012asfdfd25"));
	}

	@Test
	public void validateRequestTest() throws DSPException {
		boolean test = bidder.validateRequest(request);
		assertTrue("Not a valid Request ", test == true);
	}

	@Test
	public void appTest() {
		App app = request.getApp();
		assertNotNull("App should be null in a BidRequest", request.getApp());
		assertTrue("App id should be provided ", app.getId().equals("appTest"));
		assertTrue("Application bundle required ",app.getBundle().equals("com.mygame"));
	}

	// This method test id,height and Width of a Banner object inside Impression
	// Object.
	@Test
	public void bannerAdTest() {
		List<Impression> impList = request.getImp();
		Iterator<Impression> itr = impList.iterator();
		while (itr.hasNext()) {
			Impression imp = itr.next();
			Banner b = imp.getBanner();
			assertTrue("Height of the impression must be provided in Banner Object",
					b.getH() == 25);
			assertTrue("Width of the impression must be provided in Banner Object",
					b.getW() == 30);
		}
	}
	// This method test id,height and Width of a Banner object inside Impression
	// Object.
	@Test
	public void videoAdTest() {
		List<Impression> impList = request.getImp();
		Iterator<Impression> itr = impList.iterator();
		while (itr.hasNext()) {
			Impression imp = itr.next();
			Video video = imp.getVideo();
			assertNotNull("mimes types must be provided in Video Object",
					video.getMimes());
			assertTrue("linearity types must be provided in Video Object",
					video.getLinearity() == 54);
			assertTrue("min duration must be provided in Video Object",
					video.getMinduration() == 100);
			assertTrue("max duration must be provided in Video Object",
					video.getMaxduration() == 500);
			assertTrue("protocol must be provided in Video Object",
					video.getProtocol() == 200);
		}
	}

	@Test
	public void wseatTest() {
		List<CharSequence> wseat = request.getWseat();
		assertTrue("wseat is empty", wseat.size() == 1);
		assertTrue("Match wseatId", wseat.get(0).equals("012asfdfd25"));
	}

	@Test
	public void selectBidsTest() throws DSPException {
		RTBRequestWrapper wReq = new RTBRequestWrapper(request);
		RTBExchange exchange = new RTBExchange("BigAdExchange",
				"http://bigadex.com/rtb", "application/json");
		List<String> categories = new ArrayList<String>();
		categories.add("cat1");
		Map<String, String> seats = new HashMap<String, String>();
		seats.put("BigAdExchange", "1001");
		List<Map<String, String>> seatList = new ArrayList<Map<String, String>>();
		seatList.add(seats);// add seats Map to List

		RTBAdvertiser adv = new RTBAdvertiser("MyPage", "BigBrandIndia",
				"http://bigbrand-adserver.com/nurl", categories, seatList);
		Map<String, RTBAdvertiser> advertisersMap = new HashMap<String, RTBAdvertiser>();
		advertisersMap.put(adv.getLandingPage(), adv);
		long reqTimeout = 2000;
		long offerTimeout = 1000;
		wReq.setContext(exchange, advertisersMap, reqTimeout, offerTimeout);
		BidResponse response = new BidResponse();
		BidResponse res = bidder.selectBids(wReq, response);

		assertNotNull("Response should not be empty ", res);
		assertTrue("Response should atleast one seat Bid object ", res
				.getSeatbid().size() == 1);
	}

	//@Test
	public void processTest() throws AvroRemoteException {
		RTBRequestWrapper wReq = new RTBRequestWrapper(request);
		RTBExchange exchange = new RTBExchange("BigAdExchange",
				"http://bigadex.com/rtb", "application/json");
		List<String> categories = new ArrayList<String>();
		categories.add("cat1");
		Map<String, String> seats = new HashMap<String, String>();
		seats.put("BigAdExchange", "151");
		seats.put("SmallAdExchange", "102");
		List<Map<String, String>> seatList = new ArrayList<Map<String, String>>();
		seatList.add(seats);// add seats Map to List

		RTBAdvertiser adv = new RTBAdvertiser("AdversiderPage", "BigIndia",
				"http://bigbrand-adserver.com", categories, seatList);
		Map<String, RTBAdvertiser> advertisers = new HashMap<String, RTBAdvertiser>();
		advertisers.put(adv.getLandingPage(), adv);
		long reqTimeout = 500;
		long offerTimeout = 1000;
		wReq.setContext(exchange, advertisers, reqTimeout, offerTimeout);
		BidResponse response = bidder.process(wReq);

		assertNotNull("Response should not be empty ", response);
		assertTrue("Response should atleast one seat Bid object ", response
				.getSeatbid().size() == 1);
	}

}
