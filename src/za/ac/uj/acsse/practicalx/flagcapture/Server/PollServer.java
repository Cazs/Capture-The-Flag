package za.ac.uj.acsse.practicalx.flagcapture.Server;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class PollServer
{
	private final String POLL_URL = "http://www.biogamers.co.za/neobarrage/get.php";
	private final String USER_AGENT = "Mozilla/5.0";
	
	public String pollServer(String id) throws IOException
	{	
		String urlParams = "access=1&_actor_id_=" + id;
		
		//System.setProperty("Http.proxyHost", "10.200.254.1");
		//System.setProperty("Http.prxyPort", "3128");
		
		URL urlObj = new URL(POLL_URL);
		HttpURLConnection connection = (HttpURLConnection) urlObj.openConnection();
		
		//request method - default is get
		connection.setRequestMethod("POST");
		
		//request properties
		connection.setRequestProperty("User-Agent", USER_AGENT);
		connection.setRequestProperty("Accept-Language", "en-US, en;q=0.5");
		connection.setDoOutput(true);
		
		DataOutputStream dos = new DataOutputStream(connection.getOutputStream());
		dos.writeBytes(urlParams);
		dos.flush();
		dos.close();
		
		int responseCode = connection.getResponseCode();
	
		System.out.println("\nPolling Server:POST");
		System.out.println("Response code: "+responseCode);
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String line,temp;
		
		StringBuffer sb = new StringBuffer();
		while((line = reader.readLine())!=null)
		{
			sb.append(line);
		}
		System.out.println(sb);
		//reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		//while((line = reader.readLine())!=null)
		//{
			//line = reader.readLine();
			//System.out.println("Coordinates: " + line);	
		//}
			
			
			
		return sb.toString();
	}
}
