package bridgeexamples;

import java.sql.Connection;

public abstract class ImplementationDatabase implements DataOperatorsImplementation{
	
	public abstract Connection getConnectionToDB();
	
}
