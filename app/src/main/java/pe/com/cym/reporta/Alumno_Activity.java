package pe.com.cym.reporta;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import pe.com.cym.servicio.Funciones;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Carlos Loyola on 20/09/2014.
 */
public class Alumno_Activity extends Activity{
    private TextView tvUser;
    private Button btnObtener;
    private ProgressDialog proDialog;
    String[] arrCurso,arrNotas,arrProfe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alumno);

        tvUser = (TextView)findViewById(R.id.tvWellcomeAlumno);
        Bundle oBundle = getIntent().getExtras();
        tvUser.setText("Bienvenid@, "+oBundle.getString("usuario"));
        btnObtener = (Button)findViewById(R.id.btnObtener);

        btnObtener.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                runBackgroundData();
            }
        });
    }
    protected void  runBackgroundData() {
        AsyncTest backgrounTest = new AsyncTest();
        backgrounTest.execute();
    }
    class AsyncTest extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            proDialog =  new ProgressDialog(Alumno_Activity.this);
            proDialog.setMessage("procesando...");
            proDialog.setIndeterminate(false);
            proDialog.setCancelable(true);
            proDialog.show();
            super.onPreExecute();
        }

        JSONArray jsArrData;
        String sTemp;
        Bundle oBundle = getIntent().getExtras();
        @Override
        protected String doInBackground(String... params) {
            Funciones oFun = new Funciones();
            JSONObject oJson = oFun.obtnerNotas(oBundle.getString("usuario"));

            try {
                jsArrData = new JSONArray(oJson.getJSONArray("data").toString());
                Log.e("ARRAYDATA", jsArrData+"");

                int tamaño = jsArrData.length();
                arrCurso = new String[tamaño];
                arrNotas = new String[tamaño];
                arrProfe = new String[tamaño];

                for (int i = 0; i <= tamaño; i++) {

                    JSONObject data = jsArrData.getJSONObject(i);
                    sTemp = data.getString("idAlumno");
                    Log.e("sTEmp", sTemp);
                    Log.e("bundle", oBundle.getString("usuario"));

                    if (sTemp.equals(oBundle.getString("usuario"))) {

                        sTemp = data.getString("idCurso");
                        arrCurso[i]=sTemp;

                        sTemp = data.getString("idProfesor");
                        arrProfe[i]=sTemp;

                        sTemp = data.getString("nota");
                        arrNotas[i]=sTemp;
                    }
                }

            } catch (JSONException e) {
                Log.e("TAG", "Problemas con array: " + e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            proDialog.dismiss();
            init();
        }
    }
    public void init() {
        TableLayout stk = (TableLayout) findViewById(R.id.table_main);
        stk.removeAllViews();//clean
        TableRow tbrow0 = new TableRow(this);

        TextView tv0 = new TextView(this);
        tv0.setText("\n\tNOTA");
        tv0.setTextColor(Color.GRAY);
        tbrow0.addView(tv0);

        TextView tv1 = new TextView(this);
        tv1.setText("\n\tNOMBRE_DEL_CURSO");
        tv1.setTextColor(Color.GRAY);
        tbrow0.addView(tv1);

        TextView tv2 = new TextView(this);
        tv2.setText("\n\tPROFESOR_A_CARGO\t");
        tv2.setTextColor(Color.GRAY);
        tbrow0.addView(tv2);

        stk.addView(tbrow0);

        for (int i = 0; i < arrCurso.length; i++) {

            TableRow tbrow = new TableRow(this);
            TextView t1v = new TextView(this);
            TextView t2v = new TextView(this);
            TextView t3v = new TextView(this);
            t1v.setText("\t"+arrNotas[i]);
            t1v.setTextColor(Color.GRAY);
            t1v.setGravity(Gravity.CENTER);
            tbrow.addView(t1v);

            t2v.setText("\t"+arrCurso[i]);
            t2v.setTextColor(Color.GRAY);
            t2v.setGravity(Gravity.LEFT);
            tbrow.addView(t2v);

            t3v.setText("\t"+arrProfe[i]+"\t");
            t3v.setTextColor(Color.GRAY);
            t3v.setGravity(Gravity.LEFT);

            tbrow.addView(t3v);
            stk.addView(tbrow);
        }
        Toast.makeText(getApplicationContext(), "Datos cargados!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_alumno, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.mnu1:
                Toast.makeText(getApplicationContext(), "proximamente!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.mnu2:
                Intent itLogin = new Intent(getApplicationContext(),Login_Activity.class);
                startActivity(itLogin);finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
