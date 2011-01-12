package org.openrtb.common.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class MD5ChecksumTest {
	
	private static String INPUT = "1234567890qwertyuiopasdfghjklzxcvbnm";

	@Test
	public void md5IsNotNull()
	{	
		String checksum = MD5Checksum.getMD5Checksum(INPUT);
		//System.out.println("checksum="+checksum);
		assertNotNull("Checksum cannot be null",checksum);
	}
	
	@Test
	public void md5IsIdenticalforIdenticalInput()
	{	
		String checksum1 = MD5Checksum.getMD5Checksum(INPUT);
		String checksum2 = MD5Checksum.getMD5Checksum(INPUT);
		assertTrue("The same input produces the same digest",checksum1.equals(checksum2));
	}
	
	@Test
	public void md5IsDifferentforDifferentInput()
	{	
		String checksum1 = MD5Checksum.getMD5Checksum(INPUT);
		String checksum2 = MD5Checksum.getMD5Checksum(INPUT+"0");
		assertFalse("Different inputs result in different checksums",checksum1.equals(checksum2));
	}	
}
