package blue.heldplayer.legacypatch;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.GeneralSecurityException;
import java.security.KeyStore;

public class PatchTargets {
    public static final String CLASS_IN = "blue/heldplayer/legacypatch/PatchTargets";

    private static final SSLSocketFactory SOCKET_FACTORY;

    static {
        // https://stackoverflow.com/a/859271

        SSLSocketFactory factory = null;
        try {
            SSLContext context = SSLContext.getInstance("TLS");

            KeyStore keyStore = KeyStore.getInstance("Windows-ROOT"); // https://stackoverflow.com/a/49011158
            keyStore.load(null, null); // https://stackoverflow.com/a/5510555
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(keyStore);

            context.init(null, tmf.getTrustManagers(), null);

            factory = context.getSocketFactory();
        } catch (GeneralSecurityException ignored) {
        } catch (IOException ignored) {
        } finally {
            SOCKET_FACTORY = factory;
        }
    }

    public static final String OPEN_ASSETS_XML_STREAM_DESC = "()Ljava/io/InputStream;";

    public static InputStream openAssetsXmlStream() {
        try {
            URL url = new URL(Constants.ASSETS_XML);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setConnectTimeout(60000);
            connection.setReadTimeout(60000);
            connection.setSSLSocketFactory(SOCKET_FACTORY);
            return connection.getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static final String PATCH_SECURITY_CERTIFICATES_DESC = "(Ljava/net/URLConnection;)V";

    public static void patchSecurityCertificates(URLConnection con) {
        if (con instanceof HttpsURLConnection) {
            HttpsURLConnection connection = (HttpsURLConnection) con;
            connection.setSSLSocketFactory(SOCKET_FACTORY);
        }
    }

    public static final String UPGRADE_URL_DESC = "(Ljava/net/URL;)Ljava/net/URL;";

    public static URL upgradeUrl(URL url) {
        if (url.getProtocol().equalsIgnoreCase("http")) {
            String str = url.toString();
            try {
                return new URL(str.replaceFirst("^http", "https"));
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }
        return url;
    }
}
