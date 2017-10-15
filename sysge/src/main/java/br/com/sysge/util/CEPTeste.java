package br.com.sysge.util;

import javax.ws.rs.client.Client;
import org.json.JSONObject;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

public class CEPTeste {

	private static String URL_API = "http://www.cepaberto.com";
    private static String TOKEN = "Token token=ee81b1a13343b909ffe411531700ccac";
    private static String AUTHORIZATION_HEADER = "Authorization";
    private static String PATH = "api/v2/ceps.json";
            
    public String obter(String cep) {
        try {
            Client clienteHttp = ClientBuilder.newClient();
            WebTarget cepAberto = clienteHttp.target(URL_API).path(PATH).queryParam("cep", cep);
            Response response = cepAberto.request().header(AUTHORIZATION_HEADER, TOKEN).get();
             return fromJson(response.readEntity(String.class));
        } catch (Exception e) {
           e.printStackTrace();
        }
		return cep;
    }
    private String fromJson(String response) {
        JSONObject jsonObject = new JSONObject(response);
        if (jsonObject.has("cep")) {
            String latitude = jsonObject.getString("latitude");
            String longitude = jsonObject.getString("longitude");
            return "Latitude: "+ latitude + " Longitude: "+ longitude;
        }
        return null;
    }
    
    public static void main(String[] args) {
		System.out.println(new CEPTeste().obter("74395200"));
	}
}
