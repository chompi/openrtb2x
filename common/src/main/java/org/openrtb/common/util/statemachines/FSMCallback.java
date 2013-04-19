package org.openrtb.common.util.statemachines;


public interface FSMCallback {
	@SuppressWarnings("rawtypes")
	public FSMTransition exec(Object context) throws FSMException;
}
