package za.ac.uj.acsse.practicalx.flagcapture.Server;

/** Reference www.mkyong.com/java/how-to-send-http-request-getpost-in-java/ **/

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.Encoder;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class UpdateServer
{
	private final String USER_AGENT = "Mozilla/5.0";
	private final String UPDATE_URL = "http://www.biogamers.co.za/neobarrage/update.php";
	private static long id		= 0;
	
	
	public String updateServer(String id,int x,int y) throws IOException
	{	
		//System.setProperty("Http.proxyHost", "10.200.254.1");
		//System.setProperty("Http.prxyPort", "3128");
		
		URL urlObj = new URL(UPDATE_URL);
		
		HttpURLConnection connection = (HttpURLConnection)urlObj.openConnection();
		
		//System.setProperty("http.proxyHost", "10.200.254.1");
		//System.setProperty("http.proxyPort", "3128");
		//URL url=new URL(UPDATE_URL);
		//URLConnection connection = url.openConnection ();
		//String encoded = new String(URLEncoder.encode("", "")("username:password").getBytes());
		//connection.setRequestProperty("Proxy-Authorization", "Basic " + encoded);
		//connection.connect();
		
		//Add request header
		connection.setRequestMethod("POST");
		//connection.setRequestProperty("User-Agent", USER_AGENT);
		//connection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		
		String urlParams = "access=1&_actor_id_="+id+"&_actor_x_coordinate_="+x+"&_actor_y_coordinate_="+y;
		
		//String encoded = URLEncoder.encode("access=1", "UTF-8");
		//System.out.println(encoded);
		
		//Write data and post it
		connection.setDoOutput(true);
		DataOutputStream dos = new DataOutputStream(connection.getOutputStream());
		dos.writeBytes(urlParams);
		dos.flush();
		dos.close();
		
		int responseCode = connection.getResponseCode();
		
		System.out.println("\nSending data to server:POST");
		System.out.println("Response code:"+responseCode);
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String line;
		
		while((line = reader.readLine())!=null)
		{
			System.out.println(line);	
		}
		
		return line;//response from server
	}
}
