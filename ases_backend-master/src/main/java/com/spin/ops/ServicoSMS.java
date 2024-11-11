package com.spin.ops;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class ServicoSMS {

    private static final String API_KEY = "";
    private static final String API_URL = "https://sms.comtele.com.br/api/v2/send";

    public String sendSms(String numeroTelefone, String mensagem)
    {
        try
            (CloseableHttpClient client = HttpClients.createDefault())
            {
                HttpPost post = new HttpPost(API_URL);

                post.setHeader("Content-type", "application/json");
                post.setHeader("auth-key", API_KEY);

                String jsonBody = String.format
            (
                "{\"Sender\": \"NomeDoRemetente\", \"Receivers\": \"%s\", \"Content\": \"%s\"}",
                numeroTelefone, mensagem
            );

            StringEntity entity = new StringEntity(jsonBody);
            post.setEntity(entity);

            try
            (CloseableHttpResponse response = client.execute(post))
            {
                String responseBody = EntityUtils.toString(response.getEntity());
                return responseBody;
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            return "Erro ao enviar SMS";
        }
    }
}
