package jserver;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;

import jserver.core.Server;



public class MainProgram {
	private static org.slf4j.Logger logger = LoggerFactory.getLogger(MainProgram.class);

	public static void main(String[] args) {
		logger.info("Program started");
		Injector injector = Guice.createInjector(new ConfigurationModule());
		Server server = injector.getInstance(Server.class);
		server.start();
		logger.info("Program finished");
	}

}