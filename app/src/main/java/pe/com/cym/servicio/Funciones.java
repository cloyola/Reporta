package pe.com.cym.servicio;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.util.Log;

/**
 * Created by Carlos Loyola on 20/09/2014.
 */
public class Funciones {
    //http://192.168.1.33
    //itlab.fis.ulima.edu.pe
    private JSONParser jsonParser;
    private static String URL_LOGIN= "http://cloyola.qox-corp/webservice/api/login/";
    private static String URL_NOTA= "http://cloyola.qox-corp/webservice/api/nota/";
    private static String URL_ALUMNO= "http://cloyola.qox-corp/webservice/api/alumno/";
    private static String ACCIONES[]= {"login","listar","insertar","obtener"};

    /* ----------CONSTRUCTOR---------- */
    public Funciones() {
        jsonParser = new JSONParser(); }

    /* ----------LOGIN---------- */
    public JSONObject login(String codigo , String clave)
    {
        System.out.println(ACCIONES[0]);
        JSONObject json = null;
        //Construyendo los parametros.  NameValuePair encapsula (Attributo/Valor).
        List<NameValuePair> parametros = new ArrayList<NameValuePair>();
        parametros.add( new BasicNameValuePair("accion", ACCIONES[0]));
        parametros.add( new BasicNameValuePair("id", codigo));
        parametros.add( new BasicNameValuePair("clave", clave));
        //Envia los parametros a la clase para obtener json.
        try {
            json = jsonParser.obtenerJSON_URL(URL_LOGIN, parametros);
        } catch (Exception e) {
            Log.e("URL", e.getMessage());
        }
        return json;
    }

    /* ----------LISTAR ALUMNOS---------- */
    public JSONObject listarAlumnos()
    {
        JSONObject json = null;

        List<NameValuePair> parametros = new ArrayList<NameValuePair>();
        parametros.add( new BasicNameValuePair("accion", ACCIONES[1]));

        try {
            json = jsonParser.obtenerJSON_URL(URL_ALUMNO, parametros);
        } catch (Exception e) {
            Log.e("URL", e.getMessage());
        }
        Log.e("LISTADO-JSON", json+"");
        Log.e("ACCION",ACCIONES[1]);

        return json;

    }
    /* ----------OBTENER NOTAS---------- */
    public JSONObject obtnerNotas(String alumno)
    {
        JSONObject json = null;

        List<NameValuePair> parametros = new ArrayList<NameValuePair>();
        parametros.add( new BasicNameValuePair("accion", ACCIONES[3]));
        parametros.add( new BasicNameValuePair("alumno", alumno));

        try {
            json = jsonParser.obtenerJSON_URL(URL_NOTA, parametros);
        } catch (Exception e) {
            Log.e("URL", e.getMessage());
        }
        return json;

    }



    /* ----------INSERTAR NOTA---------- */
    public JSONObject insertarNota(String curso , String nota, String alumno, String profesor)
    {
        System.out.println(ACCIONES[2]);
        JSONObject json = null;

        List<NameValuePair> parametros = new ArrayList<NameValuePair>();
        parametros.add( new BasicNameValuePair("accion", ACCIONES[2]));
        parametros.add( new BasicNameValuePair("curso", curso));
        parametros.add( new BasicNameValuePair("nota", nota));
        parametros.add( new BasicNameValuePair("alumno", alumno));
        parametros.add( new BasicNameValuePair("profesor", profesor));

        try {
            json = jsonParser.obtenerJSON_URL(URL_NOTA, parametros);
        } catch (Exception e) {
            Log.e("URL", e.getMessage());
        }
        return json;
    }
}
