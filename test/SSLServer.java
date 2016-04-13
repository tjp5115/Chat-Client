import java.io.*;
import java.net.*;
import java.security.*;
import javax.net.ssl.*;

public class SSLServer
{
	public static void main(String[] args) throws Exception
	{
		String host = "localhost";
		int port = 5678;

		String jksFileName = "keystore.jks";
		final char KEY_STORE_PS[] = "Chat1234".toCharArray();
		final char KEY_PS[] = "Chat5678".toCharArray();
		InputStream in = null;

		try
		{
			KeyStore ks = KeyStore.getInstance("JKS");
			ks.load(new FileInputStream(jksFileName), KEY_STORE_PS);
			KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
			kmf.init(ks, KEY_PS);
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(kmf.getKeyManagers(), null, null);
			SSLServerSocketFactory ssf = sc.getServerSocketFactory();
			SSLServerSocket s = (SSLServerSocket) ssf.createServerSocket(port);
			
			for(;;)
			{
				SSLSocket socket = (SSLSocket) s.accept();
				
				in = socket.getInputStream();
				int i;
				char c;

				while((i=in.read())!=-1)
				{
					// converts integer to character
	            	c=(char)i;
	            	// prints character
	            	System.out.print(c);
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			// releases system resources associated with this stream
			if(in!=null)
			in.close();
		}
	}
}