package org.openrtb.ssp.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openrtb.ssp.OpenRtbSsp;
import org.openrtb.ssp.core.OpenRtbSspServer;

public class OpenRtbSspServlet extends HttpServlet {

	OpenRtbSspServer server = null;
	
	private static final long serialVersionUID = 1L;

	public void init () throws ServletException {
		String clientClassName = getServletConfig().getInitParameter("ClientClassName"); 
		OpenRtbSsp ssp;
		try {
			ssp = (OpenRtbSsp) Class.forName(clientClassName).newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
			throw new ServletException(e.getMessage());
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			throw new ServletException(e.getMessage());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new ServletException(e.getMessage());
		}
		server = new OpenRtbSspServer(ssp);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String jsonRequest, jsonResponse;		
		//process request
		jsonRequest = getJsonRequest(request);
		jsonResponse = server.process(jsonRequest);
		//return the result
		PrintWriter out = response.getWriter();
		out.println(jsonResponse);
    	System.out.println(" IN:"+jsonRequest);
    	System.out.println("OUT:"+jsonResponse);
		out.flush();
		out.close();
	}
	
	private String getJsonRequest(HttpServletRequest request) throws IOException
	{
		StringBuilder stringBuilder = new StringBuilder();  
		BufferedReader bufferedReader = null;  
		try {  
			InputStream inputStream = request.getInputStream();  
			if (inputStream != null) {  
				bufferedReader = new BufferedReader(new InputStreamReader(inputStream));  
				char[] charBuffer = new char[128];  
				int bytesRead = -1;  
				while ((bytesRead = bufferedReader.read(charBuffer)) > 0)  
					stringBuilder.append(charBuffer, 0, bytesRead); 
			}
			else
				stringBuilder.append("");   
		} 
		finally {  
			if (bufferedReader != null)
				bufferedReader.close();  
		}  
		return stringBuilder.toString();  
	}
}
