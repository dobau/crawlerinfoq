package br.dobau.crawlerinfoq;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Hello world!
 * 
 */
public class App {
	
	public static final String BASE = "http://www.infoq.com/";
	
	private static final int TIMEOUT = 60 * 1000;
	
	public static void main(String[] args) {
		
		
		String url = BASE + "/architecture-design/interviews/1";
		
		try {
			
			Document page = Jsoup.parse(new URL(url), TIMEOUT);
			Elements links = page.select(".news_type_video .itemtitle a");
			
			for (Element el : links) {
				String linkToVideo = BASE + el.attr("href");
				System.out.println("Parseando url " + linkToVideo);
				
				Document docVideo = Jsoup.parse(new URL(linkToVideo), TIMEOUT);
				Element elMp3 = docVideo.select("#mp3Form input").first();
				
				String mp3 = BASE + elMp3.attr("value");
				
				download(mp3);
				
			}
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	private static void download(String urlMp3) throws Exception {
		HttpClient client = HttpClientBuilder.create().build();
		HttpGet get = new HttpGet(urlMp3);
		HttpResponse response = client.execute(get);
		
		HttpEntity entity = response.getEntity();
		
		FileOutputStream output = new FileOutputStream(urlMp3.substring(urlMp3.lastIndexOf('/') + 1));
		InputStream input = entity.getContent();

		byte[] buffer = new byte[100];
		
		HttpClients.createDefault();
		
		while (input.read(buffer) != -1) {
			output.write(buffer);
		}
		
		output.close();
		input.close();
	}
	
}
