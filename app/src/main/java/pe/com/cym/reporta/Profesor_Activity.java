package pe.com.cym.reporta;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import pe.com.cym.servicio.Funciones;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Carlos Loyola on 20/09/2014.
 */
public class Profesor_Activity extends Activity{
    private static String KEY_STATUS="status";
    private static String KEY_INFO  ="info";
    private String sInfo="";
    private TextView tvLogCat;
    private TextView tvUser;
    private ListView lsvCursos;
    private Spinner spiAlumNotas;
    private Button btnGrabar,btnClearLog;
    private EditText edtNota;
    private String sAlumno,sCurso, sNota;
    private String[] arrNombres;
    private String[] arrCursos = { "Calculo I", "Tecnicas de programacion", "Lenguaje I", "Psicologia organizacional",
            "Comunicaciones de datos", "Seguridad de redes", "Arquitectua de software",
            "Sistemas operativos II","Curso gratis android"};

    /*----------CREATE----------*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profesor);
        Bundle oBundle = getIntent().getExtras();

        tvUser = (TextView) findViewById(R.id.tvWellcomeProfesor);
        tvUser.setText("Bienvenid@, " + oBundle.getString("usuario"));
        spiAlumNotas = (Spinner) findViewById(R.id.spinAlumNota);
        tvLogCat = (TextView)findViewById(R.id.tvPantalla);
        btnGrabar = (Button)findViewById(R.id.btnGrabar);
        btnClearLog = (Button)findViewById(R.id.btnClear);
        lsvCursos = (ListView)findViewById(R.id.lsvCursos);
        edtNota = (EditText)findViewById(R.id.edtNota);

        runBackroundSpinner();

        btnGrabar.setOnClickListener( new OnClickListener() {

            @Override
            public void onClick(View v) {
                sNota = edtNota.getText().toString();

                if(sCurso==null)
                    Toast.makeText(getApplicationContext(), "Click en cursos antes de grabar!", Toast.LENGTH_SHORT).show();
                else if(TextUtils.isEmpty(sNota))
                    Toast.makeText(getApplicationContext(), "No olvide ingresar una nota!", Toast.LENGTH_SHORT).show();
                else if(!TextUtils.isEmpty(sNota))
                { int iNota=Integer.parseInt(sNota);
                    if(iNota>20)
                        Toast.makeText(getApplicationContext(), "La calificación debe ser vigesimal!", Toast.LENGTH_SHORT).show();
                    else
                        runBackroundInsertar();
                }else
                    runBackroundInsertar();
            }
        });//end Listener button Grabar

        btnClearLog.setOnClickListener( new OnClickListener() {

            @Override
            public void onClick(View v) {
                tvLogCat.setText("");
                Toast.makeText(getApplicationContext(), "Cleaned!", Toast.LENGTH_SHORT).show();
            }
        });//end Listener button ClearLog

        lsvCursos.setOnItemClickListener( new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
                sCurso = arrCursos[arg2];
                Toast.makeText(getApplicationContext(), "Curso: "+sCurso, Toast.LENGTH_SHORT).show();	}
        });	//end Listener listView


        spiAlumNotas.setOnItemSelectedListener( new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
                sAlumno=arrNombres[arg2]; }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {}
        });	//end Listener spinner

    } //end OnCreate



    /*----------INSERTAR NOTAS----------*/
    protected void runBackroundInsertar() {
        AsyncTest2 backgrounTest = new AsyncTest2();
        backgrounTest.execute();
    }

    class AsyncTest2 extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String sStatus ="";
            Bundle oBundle = getIntent().getExtras();
            String sProfesor= oBundle.getString("usuario");

            Funciones oFun = new Funciones();
            JSONObject oJson = oFun.insertarNota(sCurso, sNota, sAlumno, sProfesor);
            Log.e("json--",oJson+"");
            if(oJson!=null){
                try {

                    if((oJson.getString(KEY_STATUS)).equals("ok!"))
                    { sInfo = oJson.getString(KEY_INFO);
                        sStatus = oJson.getString(KEY_STATUS);
                    }else{sStatus = "error insert!";}

                }catch (JSONException e) {
                    Log.e("TAG", "Error con el servicio: " + e.getMessage());
                }

            }else{
                sStatus = "error!"; }

            return sStatus;
        }

        @Override
        protected void onPostExecute(String result) {
            if(result.equals("ok!"))
                Toast.makeText(getApplicationContext(), "Successful!", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_SHORT).show();

            String sInput;
            sInput="+------------------------------------+\n";
            sInput +="\bAlumno: "+sAlumno+"\n";
            sInput +="\bCurso : "+sCurso+"\n";
            sInput +="\bNota  : "+sNota+"\n";
            sInput +="\bStatus: "+sInfo+"\n";

            tvLogCat.append(sInput);
            tvLogCat.append("\n");
        }
    }


    /*----------LLENAR SPINNER----------*/
    protected void  runBackroundSpinner() {
        AsyncTest backgrounTest = new AsyncTest();
        backgrounTest.execute();
    }

    class AsyncTest extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            Funciones oFun = new Funciones();
            JSONObject oJson = oFun.listarAlumnos();

            try {
                JSONArray jsArrData = new JSONArray(oJson.getJSONArray("data").toString());

                int tamaño = jsArrData.length();
                arrNombres = new String[tamaño];

                for (int i = 0; i <= jsArrData.length(); i++) {
                    JSONObject data = jsArrData.getJSONObject(i);
                    arrNombres[i] = data.getString("nombre");
                }
            } catch (JSONException e) {
                Log.e("TAG", "Problemas con array: " + e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            ArrayAdapter<String> arrAdapter1 = new ArrayAdapter<String>(
                    Profesor_Activity.this, android.R.layout.simple_list_item_1,arrCursos);
            lsvCursos.setAdapter(arrAdapter1);

            ArrayAdapter<String> arrAdapter2 = new ArrayAdapter<String>(
                    Profesor_Activity.this, android.R.layout.simple_spinner_dropdown_item, arrNombres);
            spiAlumNotas.setAdapter(arrAdapter2);

        }

    }//end llenar spinner




    /*----------MENU----------*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profesor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.mnu1:
                runBackroundSpinner();
                break;
            case R.id.mnu2:
                Intent itLogin = new Intent(getApplicationContext(),
                        Login_Activity.class);
                startActivity(itLogin);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
