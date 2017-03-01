package singletonexamples;

public class DatabaseSingleton {
	private static DatabaseSingleton instance = null;
	private String name;
	
	/*Here a parameter is needed for private constructor, so we cannot use a static block
	 * Therefore we use a special "static constructor"
	 */
	public static void initInstance(String name){
		instance = new DatabaseSingleton(name);
	}
	
	public static DatabaseSingleton getInstance() throws Exception{
		if (instance != null){
			return instance;
		}else{
			throw new Exception("Not inited instance!");
		}
	}
	
	private DatabaseSingleton(String name){
		this.setName(name);
	}
	
	public void doWork(String param){
		System.out.println("We can work!");
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
