package pe.com.cym.servicio;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.util.List;

/**
 * Created by Carlos Loyola on 20/09/2014.
 */
public class JSONParser {
    static InputStream inStream = null;//Leertor de bytes
    static JSONObject oJson = null;//Objeto json
    static String json = ""; //Variable

    //Constructor
    public JSONParser() {
        super();
        // TODO Auto-generated constructor stub
    }

    /*Esto se realiza creando y ajustando una lista de NameValuePairs como la entidad HttpPost.*/
    public JSONObject obtenerJSON_URL(String url, List<NameValuePair> parametros) {

        // Creando peticion HTTP

        try {

    		/*Crea los objetos HttpClient y HttpPost para ejecutar la solicitud POST*/
            DefaultHttpClient httpClient = new DefaultHttpClient();
    		/*Creamos el timeout*/
            HttpParams httpParamentros = httpClient.getParams();
            HttpConnectionParams.setConnectionTimeout(httpParamentros, 80000);
            HttpConnectionParams.setSoTimeout(httpParamentros, 80000);
    		/*Método de envio POST100*/
            HttpPost httpPost = new HttpPost(url);

    		/* lanzada por httpPost.setEntity().*/
            httpPost.setEntity(new UrlEncodedFormEntity(parametros));
            HttpResponse httpResponse = httpClient.execute(httpPost);
    		/*Entidad que puede ser enviada o recibida con mensaje http*/
            HttpEntity httpEntity = httpResponse.getEntity();
            inStream = httpEntity.getContent();

        } catch (UnsupportedEncodingException e) {
            Log.e("Error, Codificacion no soportada", e.getMessage());
        } catch (ClientProtocolException e) {
            Log.e("Error, Protocolo", e.getMessage());
        } catch(ConnectTimeoutException e){
            Log.e("Exception: Timeout", e.toString());
        } catch(SocketTimeoutException e){
            Log.e("Exception: Timeout", e.toString());
        } catch (Exception e) {
            Log.e("log_tag", "Error in http connection "+e.toString());
        }
        Log.e("TAG-INPUTSTREAM", inStream +"");


        if(inStream!=null)	{
    	/* Recibe la data, la almacena y la transforma a una cadena String*/
            try {
    		/*Permite manejar el flujo de caracteres de entrada almacenado en búfer.*/
                BufferedReader buffReader = new BufferedReader(new InputStreamReader(inStream,"UTF-8"),8);
                StringBuilder sBuider = new StringBuilder(); //Crear string, mejor manejo de memoria.
                String sLinea = null;
                while((sLinea = buffReader.readLine()) != null)
                {
                    sBuider.append(sLinea+"\n"); //Agrega los datos a stringBuider
                }
                inStream.close();//Cerramos el string
                json = sBuider.toString();
                Log.e("JSON-PARSER", json);

            } catch (Exception e) {
                Log.e("Error Buffer", "Error convirtiendo el resultado " + e.toString());
            }


    	/* El analizador de String tranforma a Objeto JSON */
            try {
                oJson = new JSONObject(json);
            } catch (Exception e) {
                Log.e("JSON Parser", "Error al analizar Data" + e.toString());
            }

        }else{
            return oJson;
        }

        return oJson; //devuelve el Objeto JSON

    }

}
