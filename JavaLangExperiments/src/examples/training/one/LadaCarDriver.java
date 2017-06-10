package examples.training.one;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class LadaCarDriver implements AutomaticCarDriver{

	String carId = null;
	String key   = null;
	boolean isStarted = false;
	
	
	public LadaCarDriver(String carId) {
		key  = carId.chars().filter(c -> Character.isAlphabetic(c)).map(c -> c + 1).toString();
	}
	
	
	@Override
	public void toggleEngineState(String providedKey) throws BrokenEngineException {
		if (providedKey.equalsIgnoreCase(this.key)){
			if (Double.compare(new Random().nextFloat(), 0.75) < 0) {
				throw new BrokenEngineException();
			}else{
				isStarted = !isStarted;
				System.out.println("Is on: " + isStarted);
			}
		}else{
			throw new WrongKeyException();
		}
		
	}

	@Override
	public Map<String, Status> getStatus() {
		Map<String, Status> map = new HashMap<String, Status>();
		map.put(STATUS_KEY, Status.GOOD);
		if (isStarted){
			map.put(ENGINE_KEY, Status.ON);
		}else{
			map.put(ENGINE_KEY, Status.OFF);
		}
		return map;
	}

}
