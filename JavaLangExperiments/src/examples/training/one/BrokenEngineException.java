package examples.training.one;

public class BrokenEngineException extends Exception {
	@Override
	public String getMessage() {
		return "Broken engine. It is serious!";
	}
}
