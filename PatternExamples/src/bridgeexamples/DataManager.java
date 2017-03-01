package bridgeexamples;

import java.util.List;


public abstract class DataManager {
	protected DataOperatorsImplementation impl;
	
	public void add(){
		impl.add();
	}
	
	public void delete(){
		impl.delete();
	}
	
	public void search(){
		impl.search();
	}
	
	public abstract List<String> searchForUnusedData();
	
}
