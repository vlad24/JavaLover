package bridgeexamples;

import java.util.List;

public class SillyDataManager extends DataManager{

	@Override
	public List<String> searchForUnusedData() {
		System.out.println("Silly algorithm");
		return null;
	}

}
