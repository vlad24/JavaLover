package bridgeexamples;

import java.sql.Connection;

public final class ImplementationDBOracle extends ImplementationDatabase{

	@Override
	public void add() {
		// TODO Auto-generated method stub
		System.out.println("Oracle specific add");
	}

	@Override
	public void delete() {
		// TODO Auto-generated method stub
		System.out.println("Oracle specific delete");
	}

	@Override
	public void search() {
		// TODO Auto-generated method stub
		System.out.println("Oracle specific search");
	}

	@Override
	public Connection getConnectionToDB() {
		System.out.println("Oracle specific getting connection");
		return null;
	}

}
