package jserver.core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.HashMap;

import org.slf4j.LoggerFactory;

import jserver.storage.Storage;

public class ClientTask implements Runnable{

	private static org.slf4j.Logger logger = LoggerFactory.getLogger(ClientTask.class);

	private final Socket interactiveSocket;
	protected BufferedWriter socketOut;
	protected BufferedReader socketIn;
	protected Storage storage;
	private AccountService service;
	private boolean working;

	public ClientTask(AccountService service, Socket givenSocket, Storage storage, int socketTimeout) throws IOException{
		logger.debug("*** Thread is initializing\n");
		this.service = service;
		this.storage = storage;
		this.interactiveSocket = givenSocket;
		this.interactiveSocket.setSoTimeout(socketTimeout);
		this.socketOut = new BufferedWriter(new OutputStreamWriter(interactiveSocket.getOutputStream()));
		this.socketIn = new BufferedReader(new InputStreamReader(interactiveSocket.getInputStream()));
		this.working = true;
	}


	private void writeResponse(String body, boolean ok) throws Throwable {
		StringBuilder builder = new StringBuilder();
		String result = builder.append("HTTP/1.1 ").
				append(ok? "200 OK" : "500 Internal Server Error").
				append("\r\n").
				append("Server: jserver").
				append("\r\n").
				append("Content-Type: text/html").
				append("\r\n").
				append("Content-Length: " + body.length()). 
				append("\r\n").
				append(body).
				toString();
		socketOut.write(result);
		socketOut.flush();
	}

	private HashMap<String, String> readRequest() throws IOException{
		HashMap<String, String> map = new HashMap<String, String>();
		logger.info("Start to read");
		String line = socketIn.readLine();
		logger.info("Reading line {}",line );
		if (line.contains("GET")){
			map.put("method", "GET");
			String queryString = line.substring(line.lastIndexOf("?") + 1, line.lastIndexOf(" "));
			String[] params = queryString.split("&");
			for (String param : params){
				String name = param.split("=")[0];
				String value = param.split("=")[1];
				map.put(name, value);
				logger.info("{}={}", name, value);
			}
		}else if (line.contains("POST")){
			//OTHER LOGIC MAY GO HERE
			map.put("method", "POST");
		}
		return map;
	}

	public void run(){
		while(working && !Thread.currentThread().isInterrupted()){
			logger.info("Waiting for a client request... ");
			try {
				HashMap<String, String> values = readRequest();
				if (values.get("method").equalsIgnoreCase("GET")){
					String action = values.get("action");
					if (action == null){
						continue;
					}else if (action.equalsIgnoreCase("add")){
						logger.info("Request: add {} {}", values.get("id"), values.get("value"));
						this.service.addAmount(Integer.valueOf(values.get("id")), Long.valueOf(values.get("value")));
					}else if (action.equalsIgnoreCase("get")){
						Long result = this.service.getAmount(Integer.valueOf(values.get("id")));
						writeResponse(result.toString(), true);
					}else if (action.equalsIgnoreCase("fetchstats")){
						Long result = this.service.getStats(values.get("target"));
						writeResponse(result.toString(), true);
					}else if (action.equalsIgnoreCase("nullstats")){
						this.service.nullifyStats();
						writeResponse("done", true);
					}
					
				}
			} catch (Throwable e) {
				logger.error("Error while processing request", e);
				try {
					writeResponse("ERROR", false);
				} catch (Throwable e1) {
					logger.error("Client does not know that we are over", e1);
				}
			}finally{
				stopWorking();
			}
		}
	}


	private void stopWorking() {
		working = false;
		try{
			interactiveSocket.close();
		}catch(IOException e){
			logger.error("Fail to close socket", e);
		}

	}


}
