import java.io.*;
import java.net.*;
import java.security.*;
import javax.net.ssl.*;

public class SSLClient
{
	public static void main(String[] args) throws Exception
	{
		String host = "localhost";
		int port = 5678;
		String jksFileName = "keystore.jks";
		final char KEY_STORE_PS[] = "Chat1234".toCharArray();

		KeyStore keystore = KeyStore.getInstance("JKS");
		keystore.load(new FileInputStream(jksFileName), KEY_STORE_PS);
		TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
		tmf.init(keystore);
		SSLContext context = SSLContext.getInstance("TLS");
		TrustManager[] trustManagers = tmf.getTrustManagers();

		context.init(null, trustManagers, null);
		SSLSocketFactory sf = context.getSocketFactory();
		SSLSocket s = (SSLSocket) sf.createSocket(host, port);
		OutputStream out = s.getOutputStream();
		out.write("\nConnection established.\n\n".getBytes());
   		out.flush();
   		out.close();
   		s.close();
	}
}