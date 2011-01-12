package org.openrtb.common.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Checksum {

	public static byte[] createChecksum(String input) {
		byte[] buffer = input.getBytes();
		MessageDigest md5;
		try {
			md5 = MessageDigest.getInstance("MD5");
		    md5.update(buffer, 0, buffer.length);
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
		return md5.digest();
   }	
	
	// convert a byte array to a HEX string
	public static String getMD5Checksum(String input) {
		byte[] b = createChecksum(input);
		if (b == null) return null;
		
	    String result = "";
	     for (int i=0; i < b.length; i++) {
	       result +=
	          Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
	      }
	     return result;
	   }

}
