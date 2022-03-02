package blue.heldplayer.pgpatch;

import javax.net.ssl.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.KeyStore;

public class SslUtils {
    // https://stackoverflow.com/questions/859111/how-can-i-use-different-certificates-on-specific-connections
    // https://stackoverflow.com/questions/2138940/import-pem-into-java-key-store
    // https://stackoverflow.com/questions/344748/how-to-use-a-file-in-a-jar-as-javax-net-ssl-keystore

    private static KeyStore keyStore = null;

    public static InputStream openAssetsXmlStream() {
        try {
            URL url = new URL(Constants.ASSETS_XML);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setConnectTimeout(60000);
            connection.setReadTimeout(60000);
            try {
                return connection.getInputStream();
            } catch (SSLHandshakeException e) {
                try {
                    connection.setSSLSocketFactory(createSocketFactory());
                    return connection.getInputStream();
                } catch (GeneralSecurityException e2) {
                    throw new RuntimeException(e2);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static SSLSocketFactory createSocketFactory() throws GeneralSecurityException, IOException {
        SSLContext context = SSLContext.getInstance("TLS");

        if (keyStore == null) {
            keyStore = KeyStore.getInstance(KeyStore.getDefaultType());

            keyStore.load(SslUtils.class.getResourceAsStream("META-INF/cacerts"), "password".toCharArray());
        }

        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(keyStore);

        context.init(null, tmf.getTrustManagers(), null);

        return context.getSocketFactory();
    }

}
