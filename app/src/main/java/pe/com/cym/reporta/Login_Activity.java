package pe.com.cym.reporta;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import pe.com.cym.servicio.Funciones;

/**
 * Created by Carlos Loyola on 20/09/2014.
 */
public class Login_Activity extends Activity {
    private static String KEY_STATUS = "status";
    private static String KEY_INFO = "info";
    private static String KEY_USUARIO = "usuario";
    private ProgressDialog proDialog;
    private Button bnLogin;
    private EditText edCodigo, edPassword;
    private String sCod, sPas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*setContentView(R.layout.activity_login);

        bnLogin = (Button) findViewById(R.id.butLogin);
        edCodigo = (EditText) findViewById(R.id.edtCodigo);
        edPassword = (EditText) findViewById(R.id.edtPassword);*/

        bnLogin.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                checkEmpty();
            }
        });
    } //End Create


    protected void checkEmpty() {
        String sCodigo = edCodigo.getText().toString();
        String sPassword = edPassword.getText().toString();

        if (TextUtils.isEmpty(sCodigo))
            Toast.makeText(getApplicationContext(), "Código obligatorio", Toast.LENGTH_SHORT).show();
        else if (TextUtils.isEmpty(sPassword))
            Toast.makeText(getApplicationContext(), "Password obligatorio", Toast.LENGTH_SHORT).show();
        else
            runTesting();
    }

    protected void runTesting() {
        Testing task = new Testing();
        task.execute();
    }


    class Testing extends AsyncTask<String, Void, String> {

        String sInfo = "";

        @Override
        protected String doInBackground(String... arg0) {

            sCod = edCodigo.getText().toString();
            sPas = edPassword.getText().toString();

            String sStat = "";
            Funciones oFun = new Funciones();
            JSONObject oJson = oFun.login(sCod, sPas);//Envia los parametros.

            if (oJson != null) {

                try {

	    		/*Evalua las respuestas del servidor*/
                    if ((oJson.get(KEY_STATUS).toString()).equals("no login!")) {
                        sInfo = oJson.getString(KEY_INFO);
                        sStat = oJson.getString(KEY_STATUS);

                    } else if ((oJson.get(KEY_STATUS).toString()).equals("no user!")) {
                        sInfo = oJson.getString(KEY_INFO);
                        sStat = oJson.getString(KEY_STATUS);

                    } else if ((oJson.get(KEY_STATUS).toString()).equals("user!")) {
                        sInfo = oJson.getString(KEY_INFO);
                        sStat = oJson.getString(KEY_STATUS);

                    } else if ((oJson.get(KEY_STATUS).toString()).equals("ok!")) {
                        sStat = oJson.getString(KEY_STATUS);

                        if ((oJson.getString(KEY_USUARIO)).equals("alumno")) {
                            Intent itAlum = new Intent(getApplicationContext(), Alumno_Activity.class);
                            itAlum.putExtra(KEY_USUARIO, (oJson.getString("nombre")));//Transfiero el usuario
                            startActivity(itAlum);
                            finish();
                        } else {
                            Intent itPro = new Intent(getApplicationContext(), Profesor_Activity.class);
                            itPro.putExtra(KEY_USUARIO, (oJson.getString("nombre")));
                            startActivity(itPro);
                            finish();
                        }
                    } else {
                        sInfo = oJson.getString(KEY_INFO);
                        sStat = oJson.getString("KEY_STATUS");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                sStat = "timeout";
            }
            return sStat;
        }

        protected void onPostExecute(String result) {
            proDialog.dismiss();

            System.out.println("Resultado onPost: " + result);
            if (result.equals("no user!"))
                Toast.makeText(getApplicationContext(), sInfo, Toast.LENGTH_SHORT).show();
            else if (result.equals("no login!"))
                Toast.makeText(getApplicationContext(), sInfo, Toast.LENGTH_SHORT).show();
            else if (result.equals("user!"))
                Toast.makeText(getApplicationContext(), sInfo, Toast.LENGTH_SHORT).show();
            else if (result.equals("ok!"))
                Toast.makeText(getApplicationContext(), "Wellcome!", Toast.LENGTH_LONG).show();
            else if (result.equals("timeout"))
                Toast.makeText(getApplicationContext(), "Timeout: El servidor no responde!", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getApplicationContext(), "Error desconocido: " + sInfo, Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            proDialog = new ProgressDialog(Login_Activity.this);
            proDialog.setMessage("conectando...");
            proDialog.setIndeterminate(false);
            proDialog.setCancelable(true);
            proDialog.show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            /*case R.id.mnu1:
                checkEmpty();
                 break;
            case R.id.mnu2:
                Intent itLogin = new Intent(getApplicationContext(), Login_Activity.class);
                startActivity(itLogin);
                finish();
                break;*/
        }
        return super.onOptionsItemSelected(item);
    }

}
