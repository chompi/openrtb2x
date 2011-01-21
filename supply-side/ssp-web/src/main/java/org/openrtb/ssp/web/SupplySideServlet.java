/*
 * Copyright (c) 2010, The OpenRTB Project
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   1. Redistributions of source code must retain the above copyright notice,
 *      this list of conditions and the following disclaimer.
 *
 *   2. Redistributions in binary form must reproduce the above copyright
 *      notice, this list of conditions and the following disclaimer in the
 *      documentation and/or other materials provided with the distribution.
 *
 *   3. Neither the name of the OpenRTB nor the names of its contributors
 *      may be used to endorse or promote products derived from this
 *      software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.openrtb.ssp.SupplySideService;
import org.openrtb.ssp.core.SupplySideServer;

public class SupplySideServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(SupplySideServlet.class);
	private SupplySideServer server = null;
	
	private static final long serialVersionUID = 1L;

	public void init () throws ServletException {
		String clientClassName = getServletConfig().getInitParameter("ClientClassName"); 
		SupplySideService ssp;
		try {
			ssp = (SupplySideService) Class.forName(clientClassName).newInstance();
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
		server = new SupplySideServer(ssp);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String jsonRequest, jsonResponse;		
		//process request
		jsonRequest = getJsonRequest(request);
		jsonResponse = server.process(jsonRequest);
		//return the result
		PrintWriter out = response.getWriter();
		out.println(jsonResponse);
    	log.info(" IN:"+jsonRequest);
    	log.info("OUT:"+jsonResponse);
		out.flush();
		out.close();
	}
	
	/**
	 * Extracts a JSON request form the HTTP request
	 * @param request
	 * @return
	 * @throws IOException
	 */
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
