package org.openrtb.common.util;

import java.io.PrintWriter;
import java.io.StringWriter;

public class StringUtils {
	/**
	* Creates and returns a {@link java.lang.String} from
	* <code>t</code>’s stacktrace.
	* @param t Throwable whose stack trace is required
	* @return String representing the stack trace of the exception
	*/
	public static String stackTraceToString(final Throwable t) {
	StringWriter stringWritter = new StringWriter();
	PrintWriter printWritter = new PrintWriter(stringWritter, true);

	t.printStackTrace(printWritter);
	printWritter.flush();
	stringWritter.flush();

	return stringWritter.toString();
	}

}
