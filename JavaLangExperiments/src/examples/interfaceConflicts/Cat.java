package examples.interfaceConflicts;

public class Cat implements AnimalInterface, AnimalInterface2{

	@Override
	public String sound() {
		//Not compiling!!!
	}


}
