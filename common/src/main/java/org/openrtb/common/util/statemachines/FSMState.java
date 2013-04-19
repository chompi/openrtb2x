package org.openrtb.common.util.statemachines;


public class FSMState<E extends FSMCallback> {
	private E internalState;	

	FSMState(E state) {
		this.internalState = state;
	}
		
	public E state() {
		return internalState;
	}

	
	public void state(E state) {
		this.internalState = state;
	}


	@SuppressWarnings("unchecked")
	public @Override boolean equals(Object obj) {
		if (!(obj instanceof FSMState)) { return false; }

		FSMState<E> other = (FSMState<E>) obj;

		if (this.internalState == null) {
			return other.state() == null;
		} else {
			return this.internalState.equals(other.state());
		}
	}
}
