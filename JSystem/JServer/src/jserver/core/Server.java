package jserver.core;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import jserver.storage.Storage;

@Singleton
public class Server {

	private static org.slf4j.Logger logger = LoggerFactory.getLogger(Server.class);

	private static ServerSocket listeningSocket;
	private static Executor threadPool;
	private Storage storage;
	private AccountService service;

	private int socketTimeout;


	@Inject
	public Server(AccountService service, Storage store, @Named("port") int port, @Named("maxConnections") int maxConnections,  @Named("socketTimeout") int socketTimeout) throws IOException{
		this.storage = store;
		this.service = service;
		this.socketTimeout = socketTimeout;
		listeningSocket = new ServerSocket(port);
		threadPool = Executors.newFixedThreadPool(maxConnections);
	}

	public void start(){
		logger.info("Server is on");
		logger.info(listeningSocket.toString());
		try{
			while(true){
				logger.info("Waiting...");
				Socket interactiveSocket = listeningSocket.accept();
				logger.info("Request obtained!");
				threadPool.execute(new ClientTask(service, interactiveSocket, storage, socketTimeout));
				logger.info("The request is processed in a separate thread...");
			}
		} catch (IOException error){
			logger.info(error.getMessage());
			logger.info("Will try to shutdown...");
		}
		finally{
			try {
				storage.disconnect();
				listeningSocket.close();
				logger.info("Server is successfully down");
			} catch (Exception e) {
				logger.info("FAILED TO SHUTDOWN SERVER!" + e.getMessage());
			}
		}
	}
}
