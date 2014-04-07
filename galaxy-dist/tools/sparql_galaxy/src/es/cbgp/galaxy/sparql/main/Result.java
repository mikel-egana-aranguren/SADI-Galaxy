package es.cbgp.galaxy.sparql.main;

public class Result {
	private boolean boolValue;
	private String message;

	public Result(boolean b, String m) {
		this.boolValue = b;
		this.message = m;
	}

	public Result(boolean b) {
		this.boolValue = b;
	}

	public String getMessage() {
		return this.message;
	}

	public boolean getBoolValue() {
		return this.boolValue;
	}
}
