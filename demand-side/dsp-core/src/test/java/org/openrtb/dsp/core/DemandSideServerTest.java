package org.openrtb.dsp.core;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.Encoder;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumWriter;
import org.junit.Test;
import org.openrtb.common.api.App;
import org.openrtb.common.api.Banner;
import org.openrtb.common.api.BidRequest;
import org.openrtb.common.api.Impression;
import org.openrtb.common.api.OpenRTBAPI;
import org.openrtb.common.api.Site;
import org.openrtb.common.api.Video;
import org.openrtb.dsp.client.SimpleBidder;
import org.openrtb.dsp.intf.model.DSPException;
import org.openrtb.dsp.intf.model.DemandSideDAO;

/*
 *This class is used to Test DemandSideServer class respond() method .
 *Test Json Object with different Content Type(application/json,avro/binary,application/x-protobuf,application/x-thrift) 
 */
public class DemandSideServerTest {
	protected static EncoderFactory ENCODER_FACTORY = EncoderFactory.get();
	private DemandSideServer server = null;
	private OpenRTBAPI bidder = null;
	private DemandSideDAO dao = null;
	private InputStream in = null;
	BidRequest request = null;
	String jsonContentType = "application/json";
	String avroContentType = "avro/binary";
	String protoBufContentType = "application/x-protobuf";
	String thriftContentType = "aplication/x-thrift";

	/*
	 * String jsonRequestContent = "{\"id\" :" + "\"ad1d762d6d9b593738\" ,\n" +
	 * "\"at\" :" + 2 + "," + "\n" + "\"tmax\" :" + 150 + ",\n" +
	 * "\"imp\" : [{ \n" + "\"id\" :" + "\"1\" ," + "\"tagid\" :" +
	 * "\"30027\" ,\n" + "\"banner\" : \n" + "{\n" + "\"w\" :" + 468 + "," +
	 * "\"h\" :" + 60 + "," + "\"battr\" : " + "[\n" + 9 + "," + 1 + "," + 14014
	 * + "], \n" + "\"api\" :" + "[" + "]} \n" + "}]," + "\"site\" : \n {" +
	 * "\"id\" : " + "\"144655\" ,\n" + "\"domain\" :" + "\"discovery.com\"," +
	 * "\"cat\" :[\n" + "\"IABI\" ],\n" + "\"page\" :" + "\"mypage.com\" ,\n" +
	 * "\"publisher\" :" + "{ \n" + "\"id\" :" + "\"1021\"" + "},\n" +
	 * "\"ext\" :" + "{" + "\"mopt\" :" + 0 + "}},\n" + "\"device\" : {\n " +
	 * "\"ua\" :" + "\"Mozila (Mac ;Intel)AppleWebKit) chrome Safari\" ," +
	 * "\"ip\" :" + "\"10.83.4.24\" ,\n" + "\"language\" :" + "\"en\" ,\n" +
	 * "\"devicetype\" :" + 2 + " ,\n" + "\"js\" :" + 1 + ",\n" +
	 * "\"geo\" :{ \n" + "\"country\" :" + "\"INDIA\" ," + "\"region\" :" +
	 * "\"tx\" }},\n" + "\"user\" :{" + "\"id\" :" +
	 * "\"edcb45ee1cf871183ca4d\" ,\n" + "\"ext\": {" + "\"sessiondepth\" :" + 1
	 * + "}}}";
	 */
	public DemandSideServerTest() throws DSPException, IOException {
		bidder = new SimpleBidder();
		request = mock(BidRequest.class);

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

		App app = new App();
		app.setId("appTest");
		Site site = new Site();
		site.setId("124545sfdghs");

		when(request.getId()).thenReturn("ad1d762d69b593738");
		when(request.getImp()).thenReturn(
				Collections.<Impression> singletonList(imp));
		when(request.getApp()).thenReturn(app);
		when(request.getSite()).thenReturn(site);
		when(request.getWseat()).thenReturn(
				Collections.<CharSequence> singletonList("012asfdfd25"));
		when(request.getBadv()).thenReturn(
				Collections.<CharSequence> singletonList("2054sdsdf412"));
		when(request.getBcat()).thenReturn(
				Collections.<CharSequence> singletonList("2dds4sdas445412"));
		bidder = new OpenRTBAPIDummyTest();
		dao = new DemandSideDAODummyTest();
		server = new DemandSideServer(bidder, dao);
		String s = new String(writeRequest(request));
		System.out.println("Test : " + s);
		in = new ByteArrayInputStream(writeRequest(request));
	}

	// Test respond() with Json Content Type
	// @Test
	public void jsonRespondTest() throws DSPException {
		server.respond("Json_ssp", in, jsonContentType);
	}

	protected byte[] writeRequest(BidRequest bidRequest) throws DSPException,
			IOException {
		// System.out.println(bidRequest.getImp());
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Encoder encoder = ENCODER_FACTORY.jsonEncoder(BidRequest.SCHEMA$, out);
		DatumWriter<BidRequest> writer = new SpecificDatumWriter<BidRequest>(
				BidRequest.SCHEMA$);
		writer.write(bidRequest, encoder);
		out.flush();
		System.out.println("Length : " + out.toByteArray().length);
		return out.toByteArray();
	}
}
