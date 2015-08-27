package org.openrtb.common.util.statemachines;

// a HashMap compatible Transition class
public class FSMTransition<A extends FSMCallback, B> {
    private A state;
    private B event;

    public FSMTransition() { 
    	super(); 
    }

    public FSMTransition(B event) { 
    	super(); 
    	this.event = event;
    }
    
    public FSMTransition(A state, B event) {
    	super();
    	this.state = state;
    	this.event = event;
    }
        
    public int hashCode() {
    	int hashState = state != null ? state.hashCode() : 0;
    	int hashEvent = event != null ? event.hashCode() : 0;

    	return (hashState + hashEvent) * hashEvent + hashState;
    }

    public boolean equals(Object other) {
    	if (other instanceof FSMTransition) {
    		@SuppressWarnings("rawtypes")
    		FSMTransition otherTransition = (FSMTransition) other;
    		return 
    		((  this.state == otherTransition.state ||
    			( this.state != null && otherTransition.state != null &&
    			  this.state.equals(otherTransition.state))) &&
    		 (	this.event == otherTransition.event ||
    			( this.event != null && otherTransition.event != null &&
    			  this.event.equals(otherTransition.event))) );
    	}
    	return false;
    }

    public String toString()
    { 
           return "(" + state + ", " + event + ")"; 
    }

    public A getState() {
    	return state;
    }

    public void setState(A state) {
    	this.state = state;
    }

    public B getEvent() {
    	return event;
    }
}

