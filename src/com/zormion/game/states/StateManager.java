package com.zormion.game.states;

import java.util.ArrayList;
import java.util.List;

public class StateManager {
	private List<State> states = new ArrayList<State>();
	private State currentState = null;
	
	public void addState(State state) {
		states.add(state);
	}
	
	public void setState(String name) {
		for(int i = 0; i < states.size(); i++) {
			if(states.get(i).getName().equals(name)) currentState = states.get(i);
		}
	}
	
	public State getCurrentState() {
		return currentState;
	}
	
}
