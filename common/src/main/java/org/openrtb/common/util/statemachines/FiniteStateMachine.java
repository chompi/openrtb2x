package org.openrtb.common.util.statemachines;

import java.util.HashMap;
import java.util.Map;

public class FiniteStateMachine<T extends FSMCallback> {	
	private Map<T, FSMState<T>> states = new HashMap<T, FSMState<T>>();
	private Map<FSMTransition<T, String>, T> transitions = new HashMap<FSMTransition<T, String>, T>();
	private FSMState<T> initial, current;
	private FSMTransition<T, String> running;
	
	public FiniteStateMachine() {
	}
	
	
	private synchronized FSMState<T> findState(T stateId) {
		if (states.containsKey(stateId)) return states.get(stateId);
		return null;
	}
		
	public synchronized T getCurrent() { return this.current.state(); }
	
	private synchronized FSMTransition<T, String> getRunning() {
		return running;
	}

	private synchronized boolean isRunning() {
		return (running != null);
	}

	public synchronized void addStates(T... newStates) {
		for (T s : newStates) {
			FSMState<T> state = new FSMState<T>(s);
			states.put(s, state);
		}
	}
	
	public synchronized void addTransition(T from, FSMTransition<T, String> transition, T to) throws FSMException {
		FSMState<T> fromState = findState(from);
		if (fromState == null) throw new FSMException("addTransitions: fromState is not a known FSMState");
		
		FSMState<T> toState = findState(to);
		if (toState == null) throw new FSMException("addTransition: toState is not a known FSMState");
		
		FSMState<T> transitionState = findState(transition.getState());
		if (transitionState == null) throw new FSMException("addTransition: transitionState is not a known FSMState");
		
		transitions.put(transition, to);
	}
	
	
	public synchronized void exec(T start, Object context) throws FSMException {
		this.start(start, context);
		while (isRunning()) {			
			FSMTransition<T, String> t = getRunning(); // transition to follow
			
			// make sure the current state was not modified by another thread
			// while we were executing the call back above
			if  (current.state() != t.getState())
				break;
						
			followTransition(t, context);
		}
	}

	@SuppressWarnings("unchecked")
	private synchronized void start(T start, Object context) {
		FSMState<T> startState = findState(start);
		if (startState == null) throw new FSMException("exec: Initial State not known");
		this.initial = startState;
		this.current = this.initial;
		FSMCallback callback = (FSMCallback)current.state();
		running = callback.exec(context);
	}
	
	@SuppressWarnings("unchecked")
	public synchronized void followTransition(FSMTransition<T, String> t, Object context) {
		// follow the transition t from current state
		if (!transitions.containsKey(t)) 
			throw new FSMException("followTransition: Illegal Transition");

		current = findState(transitions.get(t));
		
		if (current == null)
			throw new FSMException("followTransition: Illegal next State");
		
		// enter the next state
		FSMCallback callback = (FSMCallback)current.state();
		
		// set the resulting transition
		running = callback.exec(context);
		
	}	
}
