package jserver;


import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

import jserver.core.AccountService;
import jserver.storage.PostgreSQLPersistorWithCache;
import jserver.storage.Storage;

public class ConfigurationModule extends AbstractModule {
	
	@Override
	protected void configure() {
		bind(AccountService.class).to(SmartAccountService.class);
		bind(Storage.class).to(PostgreSQLPersistorWithCache.class);
		bindConstant().annotatedWith(Names.named("dbName")).to("PostrgreSQL");
		bindConstant().annotatedWith(Names.named("dbUser")).to("admin");
		bindConstant().annotatedWith(Names.named("dbPassword")).to("1234");
		bindConstant().annotatedWith(Names.named("maxConnections")).to(40960000);
		bindConstant().annotatedWith(Names.named("socketTimeout")).to(60 * 1000);
		bindConstant().annotatedWith(Names.named("port")).to(22219);
	}

}