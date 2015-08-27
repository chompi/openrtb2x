/**
 * 
 */
package org.openrtb.dsp.intf.model;


/**
 * @author pshroff
 *
 */
public class DSPException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2L;

	public DSPException(String string) {
		super(string);
	}

	public DSPException(Exception e) {
		super(e);
	}

}
