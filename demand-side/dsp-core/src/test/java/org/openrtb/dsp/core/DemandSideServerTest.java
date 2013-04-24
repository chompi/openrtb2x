package org.openrtb.dsp.core;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.Encoder;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumWriter;
import org.junit.Before;
import org.junit.Test;
import org.openrtb.common.api.App;
import org.openrtb.common.api.Banner;
import org.openrtb.common.api.BidRequest;
import org.openrtb.common.api.Impression;
import org.openrtb.common.api.Site;
import org.openrtb.common.api.Video;
import org.openrtb.dsp.intf.model.DSPException;

/*
 *This class is used to Test DemandSideServer class respond() method .
 *Test Json Object with different Content Type(application/json,avro/binary,application/x-protobuf,application/x-thrift) 
 */
public class DemandSideServerTest {
	private static EncoderFactory ENCODER_FACTORY = EncoderFactory.get();
	BidRequest request = null;

	@Before
	public void setUp() {
		request = new BidRequest();
		Banner banner = new Banner();
		List<Integer> btype = new ArrayList<Integer>();
		btype.add(421);
		List<Integer> expdir = new ArrayList<Integer>();
		expdir.add(421);
		List<Integer> battr = new ArrayList<Integer>();
		battr.add(421);
		List<Integer> api = new ArrayList<Integer>();
		api.add(421);
		List<CharSequence> mime = new ArrayList<CharSequence>();
		mime.add("421");
		banner.setId("102dsd");
		banner.setH(25);
		banner.setW(30);
		banner.setPos(2545);
		banner.setTopframe(24);
		banner.setBtype(btype);
		banner.setExpdir(expdir);
		banner.setBattr(battr);
		banner.setApi(api);
		banner.setMimes(mime);
		banner.setExt("45dsa");
		Video video = new Video();
		List<CharSequence> mimes = new ArrayList<CharSequence>();
		mimes.add("video/x-mswmv");
		List<Integer> battr1 = new ArrayList<Integer>();
		battr1.add(421);
		List<Integer> playback = new ArrayList<Integer>();
		playback.add(421);
		List<Integer> delivery = new ArrayList<Integer>();
		delivery.add(421);
		List<Banner> companionad = new ArrayList<Banner>();
		companionad.add(banner);
		List<Integer> api1 = new ArrayList<Integer>();
		api1.add(421);
		List<Integer> compantype = new ArrayList<Integer>();
		compantype.add(421);
		video.setMimes(mimes);
		video.setH(25);
		video.setW(30);
		video.setApi(api1);
		video.setCompaniontype(compantype);
		video.setBattr(battr);
		video.setCompanionad(companionad);
		video.setDelivery(delivery);
		video.setPlaybackmethod(playback);
		video.setLinearity(54);
		video.setMinduration(100);
		video.setMaxduration(500);
		video.setProtocol(200);
		video.setStartdelay(25);
		video.setSequence(125);
		video.setMaxextended(44);
		video.setMinbitrate(55);
		video.setMaxbitrate(55);
		video.setBoxingallowed(55);
		video.setPos(522);
		video.setExt("45dsa");
		Impression imp = new Impression();
		List<CharSequence> iframe = new ArrayList<CharSequence>();
		iframe.add("abcds");
		imp.setId("10212sdsa1");
		imp.setBanner(banner);
		imp.setVideo(video);
		imp.setInstl(55);
		imp.setBidfloor(new Float(2.09));
		imp.setIframebuster(iframe);
		imp.setExt("45dsa");
		imp.setDisplaymanager("sdf555");
		imp.setDisplaymanagerver("ss223");
		imp.setTagid("56666");
		imp.setBidfloorcur("dfsd");
		List<Impression> impression = new ArrayList<Impression>();
		impression.add(imp);
		List<CharSequence> wseat = new ArrayList<CharSequence>();
		wseat.add("SeatID001");
		request.setWseat(wseat);
		App app = new App();
		app.setId("appTest");
		Site site = new Site();
		site.setId("124545sfdghs");
		List<CharSequence> cat = new ArrayList<CharSequence>();
		cat.add("cat1");
		site.setCat(cat);
		List<CharSequence> sectionCat = new ArrayList<CharSequence>();
		sectionCat.add("abcds");
		site.setSectioncat(sectionCat);
		List<CharSequence> pagecat = new ArrayList<CharSequence>();
		pagecat.add("abcds");
		site.setPagecat(pagecat);
		request.setSite(site);
		request.setId("sdafsd252222");
		request.setImp(impression);
		List<CharSequence> cur = new ArrayList<CharSequence>();
		cur.add("abcds");
		request.setCur(cur);
		List<CharSequence> bcat = new ArrayList<CharSequence>();
		bcat.add("abcds");
		request.setBcat(bcat);
		List<CharSequence> badv = new ArrayList<CharSequence>();
		badv.add("abcds");
		request.setBadv(badv);
	}

	 @Test
	public void jsonRespondTest() throws DSPException, IOException {
		String jsonContentType = "application/json";
		OpenRTBAPIDummyTest bidder = new OpenRTBAPIDummyTest();
		DemandSideDAODummyTest dao = new DemandSideDAODummyTest();
		URL url = this.getClass().getResource("/properties.json");
		dao.loadData(url.getPath());
		DemandSideServer server = new DemandSideServer(bidder, dao);
		InputStream in = new ByteArrayInputStream(writeBidRequest(request,
				jsonContentType));
		server.respond("BigAdExchange", in, jsonContentType);

	}

	 @Test
	public void avroRespondTest() throws DSPException, IOException {
		String avroContentType = "avro/binary";
		OpenRTBAPIDummyTest bidder = new OpenRTBAPIDummyTest();
		DemandSideDAODummyTest dao = new DemandSideDAODummyTest();
		URL url = this.getClass().getResource("/properties.json");
		dao.loadData(url.getPath());
		DemandSideServer server = new DemandSideServer(bidder, dao);
		InputStream in = new ByteArrayInputStream(writeBidRequest(request,
				avroContentType));
		server.respond("BigAdExchange", in, avroContentType);
	}

	protected byte[] writeBidRequest(BidRequest bidRequest, String contentType)
			throws DSPException {
		String jsonContentType = "application/json";
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		Encoder encoder = null;
		DatumWriter<BidRequest> writer = null;
		try {// encorder
			if (contentType.equals(jsonContentType)) {
				encoder = ENCODER_FACTORY.jsonEncoder(BidRequest.SCHEMA$, os);
			} else {
				encoder = ENCODER_FACTORY.binaryEncoder(os, null);
			}
			// writer
			writer = new SpecificDatumWriter<BidRequest>(BidRequest.SCHEMA$);
			writer.write(bidRequest, encoder);
			encoder.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (contentType.equals(jsonContentType)) {
			return os.toByteArray();
		} else { // for all binary output, the data needs to be serialized
			ByteBuffer serialized = ByteBuffer
					.allocate(os.toByteArray().length);
			serialized.put(os.toByteArray());
			return serialized.array();
		}
	}

}