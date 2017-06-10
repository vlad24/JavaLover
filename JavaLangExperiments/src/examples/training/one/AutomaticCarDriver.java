package examples.training.one;

import java.util.Map;

public interface AutomaticCarDriver {
	
	public static final String ENGINE_KEY = "engineIs";
	public static final String STATUS_KEY = "carIs";
	
	
	public void toggleEngineState(String key) throws BrokenEngineException;
	public Map<String, Status> getStatus();
}
