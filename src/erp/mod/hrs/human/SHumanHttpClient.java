/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.human;

import org.apache.hc.client5.http.classic.methods.HttpDelete;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPatch;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;

import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntityContainer;

import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;


/**
 *
 * @author Cesar Orozco
 */
public class SHumanHttpClient {

    public static SHumanResponse sendRequest(
            final SHumanConfig config,
            final String method,
            final String url,
            final String jsonBody
    ) {

        SHumanResponse response = new SHumanResponse();

        try (CloseableHttpClient client = HttpClients.createDefault()) {

            HttpUriRequestBase request;

            switch (method.toUpperCase()) {
                case "POST":
                    request = new HttpPost(url);
                    break;
                case "PATCH":
                    request = new HttpPatch(url);
                    break;
                case "DELETE":
                    request = new HttpDelete(url);
                    break;
                case "GET":
                    request = new HttpGet(url);
                    break;
                default:
                    throw new UnsupportedOperationException(
                            "Método no soportado: " + method
                    );
            }

            request.setHeader("Authorization", config.getApiKey());
            request.setHeader("Accept", "application/json");
            request.setHeader("Content-Type", "application/json");

            if (jsonBody != null && request instanceof HttpEntityContainer) {

                ((HttpEntityContainer) request).setEntity(
                        new StringEntity(
                                jsonBody,
                                ContentType.APPLICATION_JSON
                        )
                );
            }

            CloseableHttpResponse res = client.execute(request);

            int status = res.getCode();

            response.setStatusCode(status);
            response.setSuccess(status >= 200 && status < 300);

            String body = res.getEntity() != null ? EntityUtils.toString(res.getEntity()) : "";
            response.setResponseBody(body);
        }
        catch (Exception e) {

            response.setSuccess(false);
            response.setError(e.toString());
            e.printStackTrace();
        }

        return response;
    }
}
