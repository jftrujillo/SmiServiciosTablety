package com.example.jhon.smiserviciostablet;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.jhon.smiserviciostablet.Models.Users;
import com.example.jhon.smiserviciostablet.Util.Constants;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

import java.net.MalformedURLException;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    TextInputLayout email, password;
    Button sing_in, log_in;
    SharedPreferences preferences;
    SharedPreferences.Editor editorPreferences;
    public MobileServiceClient mClient;
    public MobileServiceTable<Users> mTableUsers;
    public MobileServiceList<Users> mListUsers;
    ProgressDialog progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        preferences = getSharedPreferences(Constants.preferencesName,MODE_PRIVATE);
        editorPreferences = preferences.edit();


        email =(TextInputLayout) findViewById(R.id.login_email);
        password = (TextInputLayout) findViewById(R.id.login_password);
        sing_in = (Button) findViewById(R.id.login_sigin);
        log_in = (Button) findViewById(R.id.login_login);

        sing_in.setOnClickListener(this);
        log_in.setOnClickListener(this);

        try {
            mClient = new MobileServiceClient("https://smiserviciosmovil.azure-mobile.net/",
                    "qIufyUhXNGYkLUXenUUDufQFPMdcUm65",
                    this);

            mTableUsers = mClient.getTable(Users.class);

        } catch (MalformedURLException e) {
            Toast.makeText(this,"No fue posible conectar con la base de datos",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.login_sigin:
                startActivity(new Intent(LoginActivity.this,SignInAcitivity.class));
                break;

            case R.id.login_login:
                progress = ProgressDialog.show(this,getString(R.string.startin_session),getString(R.string.please_wait),true);
                getLogin(email.getEditText().getText().toString(), password.getEditText().getText().toString());
                break;


        }
    }


    public void getLogin(final String mail, final String pass){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    mListUsers = mTableUsers.where().field("mail").eq(mail).and().field("password").eq(pass).execute().get();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mListUsers.size() > 0){
                                editorPreferences.putBoolean(Constants.isLoged,true);
                                editorPreferences.putString(Constants.userEmai,mListUsers.get(0).getMail());
                                editorPreferences.putString(Constants.userID, String.valueOf(mListUsers.get(0).getId()));
                                editorPreferences.putString(Constants.typeUser,String.valueOf(mListUsers.get(0).getType()));
                                editorPreferences.commit();

                                if (mListUsers.get(0).getType() == Constants.CLIENT){
                                    progress.dismiss();
                                }

                                if (mListUsers.get(0).getType() == Constants.ADMIN){
                                    progress.dismiss();
                                    startActivity(new Intent(getApplicationContext(),DashboardActivity.class));
                                    finish();
                                }

                            }

                            else {
                                progress.dismiss();
                                Toast.makeText(getApplicationContext(),"Usuario no encontrado",Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                } catch (Exception exception) {
                    progress.dismiss();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),getString(R.string.check_internet),Toast.LENGTH_SHORT).show();
                        }
                    });

                    Log.i("AZURE EX",""+exception.toString()+"Error");
                }
                return null;
            }

        }.execute();

    }
}
