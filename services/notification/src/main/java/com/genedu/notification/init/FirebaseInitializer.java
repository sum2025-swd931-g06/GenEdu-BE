package com.genedu.notification.init;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class FirebaseInitializer {

    private static final String CONFIG = """
        {
          "type": "service_account",
          "project_id": "fir-project-63ed1",
          "private_key_id": "849cbb604e2994110f50ee26efa19bf665a83a25",
          "private_key": "-----BEGIN PRIVATE KEY-----\\nMIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDXthGmQMa6LLmj\\ndNGv2hVjAH+xARE0N7GMPqKzSGs7XRCgEJMKNvCHpLtMQ8UiVdPVJLmxDq0epzF4\\ncvwuMNIBO/ZjYFDVB9GmCACFP4wB1DpmeYEH3aacE2gx+szZ3A7ZB2bU9LaBJ9cd\\nrRN35moerfRqmf1Pta5U7QVzts94XClHemELyzwFhDLY8xKd07cY2FGOgsZXBuqS\\nP29VDiBKla0KsAr3hSVSrbugtVU+LS63+/wdkvtGhmDIyNSh+qsQsQEwqgAhOb2n\\nACM0Rz7o43Qm02SjC7qZs71/SC9ZBcZccaQQaq8GwyOgLWQ7u4Wzn0Zq+2XFPiE3\\nZFJgVq3xAgMBAAECggEACtmdKMOjHuBsl8WGQTmy+pS7Zb0sAM24yryoP7mprrCB\\nBD3fZ4V1suQOwmUPJrfdIEsFf6LdylnPcPxwiQsjml703GLH2L2Df2iqTEJkrxvB\\nsniCGsOgQ4pfP1KM1QpEJOyzt+GR45qME3GcUmlgk1J5+wgIS5GpOPDKOzEfQRUq\\nAzeBVxSlwAAieEptcI513poeHPaIaZRtKaRfJXjz+AerYsYTKEcr+6LmsN3M7Lno\\nTZ7gQicFJkcDbp0FVtjaNktUCZvoEFZVPxo6/b1JnQC1UtfJGN7J2jMIf2bogESn\\nNI0p1IpGHEvHaM2HoZ1Mwqq18uxJ68/dRY0eBtYpkQKBgQDsnvy42g82fTm3HLUw\\nHM3b2BikqS2ClG5IAK7Z5Q6ltTHEC+4/XTPQ21uXuktlHsy4xFs24HlsC0GlFr6q\\nLv9KCKF/r345Xe9493hiRZojqyx9PsveUMtz8E4bcZLO074oEygTdCaKk8K85dXV\\nnJGWGhykIb/2ITR4xKWcFr6JlwKBgQDpYK9IAaNqrlGKKgjE+AFABi1g5T+V94MX\\ncwmi/zG5iDvHmggOe7qU036No3NTOhQrx8nNIcAvP0tL4KtSSkGavQIa/YizI6St\\nz7RkVIJbkoLWmbJy+e9v1K3wsofb340antJ5hoNx1w2BZcaPIwsi7S6Ta9C335xM\\nY7fF0qyltwKBgQCDgyYBc6kq5tTSMlF+CTRdcTDZK/JQdkEJIeAM2SOX9CpRxTPI\\n4ftvpMu2e+N1s3WjZ/cue/rgky0MHaUhH3fiZQOl3RsxtUTpHdo5/GdcVcInZmSX\\nDp7VtIUk+a3X6JXy3hrTHSZitRdyN+fujtPX8nFixCNWIERyrxFAvN9vxwKBgCdA\\nwtqao/Dalw5lGYp/qD4ri9BjXrmLovn7uGA5ChUJq/xblQVSnERR+lQ6bLhP6Xqf\\npPqitZ98xt5hUI+Lu9MZ/VOq1yZCVX2ClPXXHQHn96e+vDwIe9RUclksvhsmU6Zp\\nlj8od81a/YOJKceDfmkGebIEDVwP+UsD7rIQSEerAoGBAOMhzRhNdGjBv+G7A3mm\\nbfu1Vow59N9J3CQXITiiKDrEH06IxM+td0hBnWPBWoiWdDASgUlvvRAFmRF8YrwH\\nQEP8NhAGZQW80AI4KsG2Chng8oHLm/8tFD9B/12D3Xu8gTaqvvPv9D4PnbRrVIuS\\nPyWxIOP2a4nNHy8n7bxbT3GX\\n-----END PRIVATE KEY-----\\n",
          "client_email": "firebase-adminsdk-fbsvc@fir-project-63ed1.iam.gserviceaccount.com",
          "client_id": "118100564738095063728",
          "auth_uri": "https://accounts.google.com/o/oauth2/auth",
          "token_uri": "https://oauth2.googleapis.com/token",
          "auth_provider_x509_cert_url": "https://www.googleapis.com/oauth2/v1/certs",
          "client_x509_cert_url": "https://www.googleapis.com/robot/v1/metadata/x509/firebase-adminsdk-fbsvc%40fir-project-63ed1.iam.gserviceaccount.com",
          "universe_domain": "googleapis.com"
        }
        """;

    @PostConstruct
    public void init() {
        try {
            InputStream serviceAccount = new ByteArrayInputStream(CONFIG.getBytes());

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}