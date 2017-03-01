package jclient;

import java.util.Random;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class TaskReader implements Runnable {
	int[] ids = null;
	String server = null;
	CloseableHttpClient httpclient = HttpClients.createDefault();

	public TaskReader(String server, int[] ids) {
		this.server = server;
		this.ids = ids;
	}
	public void run() {
		Random rand = new Random();
		while(true){
			try {
				URIBuilder builder = new URIBuilder();
				builder.setHost(server);
				builder
				.addParameter("action","get")
				.addParameter("id",    String.valueOf(this.ids[rand.nextInt(this.ids.length)]));
				String address = "http:" + builder.build().toString();
				HttpGet httpGet = new HttpGet(address);
				httpclient.execute(httpGet);
				CloseableHttpResponse response = httpclient.execute(httpGet);
				int code = response.getStatusLine().getStatusCode();
				response.close();
				if (code != 200){
					break;
				}
				httpclient.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
