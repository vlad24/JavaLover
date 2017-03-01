package bridgeexamples;

import java.sql.Connection;

public final class ImplementationDBSQLite extends ImplementationDatabase{

	@Override
	public void add() {
		// TODO Auto-generated method stub
		System.out.println("SQLITE specific add");
	}

	@Override
	public void delete() {
		// TODO Auto-generated method stub
		System.out.println("SQLITE specific delete");
	}

	@Override
	public void search() {
		// TODO Auto-generated method stub
		System.out.println("SQLITE specific search");
	}

	@Override
	public Connection getConnectionToDB() {
		System.out.println("SQLITE specific getting connection");
		return null;
	}

}
