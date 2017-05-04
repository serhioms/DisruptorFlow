package ca.rdmss.dflow;

public enum TaskTransition {
	Next, // Go to the next task. No next task - flow completed.
	Fail, // Task failed - flow fail
	End,  // Task completed - flow completed
	Stop; // Task stop - flow stop
}
