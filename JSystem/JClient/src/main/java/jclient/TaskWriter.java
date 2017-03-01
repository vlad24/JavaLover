package jclient;

import java.util.Random;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class TaskWriter implements Runnable {
	int[] ids = null;
	CloseableHttpClient httpclient = HttpClients.createDefault();
	String server;

	public TaskWriter(String server, int[] ids) {
		this.ids = ids;
		this.server = server;
	}
	public void run() {
		Random rand = new Random();
		try {
			while(true){
				URIBuilder builder = new URIBuilder();
				builder.setHost(server);
				builder
				.addParameter("action","add")
				.addParameter("id",    String.valueOf(this.ids[rand.nextInt(this.ids.length)]))
				.addParameter("value", String.valueOf(rand.nextInt()));
				String address = "http:" + builder.build().toString();
				HttpGet httpGet = new HttpGet(address);
				CloseableHttpResponse response = httpclient.execute(httpGet);
				int code = response.getStatusLine().getStatusCode();
				response.close();
				if (code != 200){
					break;
				}
			}
			httpclient.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}

