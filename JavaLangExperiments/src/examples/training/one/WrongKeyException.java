package examples.training.one;

public class WrongKeyException extends RuntimeException {
	@Override
	public String getMessage() {
		return "Check that you have picked up right key.";
	}
}
