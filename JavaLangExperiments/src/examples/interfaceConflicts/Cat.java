package examples.interfaceConflicts;

public class Cat implements AnimalInterface 
//1 ,AnimalInterface2
{

	@Override
	public String sound() {
		//Not compiling if 1 presents!!!
		return null;
	}

	@Override
	public String sleep() {
		// TODO Auto-generated method stub
		return null;
	}


}
