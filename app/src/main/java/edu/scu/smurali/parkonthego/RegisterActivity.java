package edu.scu.smurali.parkonthego;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.scu.smurali.parkonthego.retrofit.reponses.SignUpResponse;
import edu.scu.smurali.parkonthego.retrofit.services.UserServices;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private Context mContext;
    private EditText regFirstName, regLastName, regEmail, regPassword, regCfnPassword;
    private Button regButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        try {
            ActionBar actionBar = getSupportActionBar();
            actionBar.setTitle("ParkOnTheGo");
            actionBar.setIcon(R.mipmap.ic_park);
            //  actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            // actionBar.setHomeButtonEnabled(true);
            this.regFirstName = (EditText) findViewById(R.id.regFirstName);
            this.regLastName = (EditText) findViewById(R.id.regLastName);
            this.regEmail = (EditText) findViewById(R.id.regEmail);
            this.regPassword = (EditText) findViewById(R.id.regPassword);
            this.regCfnPassword = (EditText) findViewById(R.id.regCfnPassword);

            this.regButton = (Button) findViewById(R.id.regRegisterButton);

            this.regButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HashMap data;
                    data = new HashMap();
                    data.put("firstName", regFirstName.getText().toString());
                    data.put("lastName", regLastName.getText().toString());
                    data.put("email", regEmail.getText().toString());
                    data.put("password", regPassword.getText().toString());
                   // register(data);

                    if(!validateFirstName(regFirstName.getText().toString()))
                    {
                        regFirstName.setError("Please Enter your First Name");
                        regFirstName.requestFocus();
                    }

                  else if(!validateLastName(regLastName.getText().toString()))
                    {
                        regLastName.setError("Please Enter your Last Name");
                        regLastName.requestFocus();
                    }


                    else if(!validateEmail(regEmail.getText().toString()))
                        {
                            regEmail.setError("Invalid Email");
                            regEmail.requestFocus();
                        }

                        else if (!validatePassword(regPassword.getText().toString())){
                            regPassword.setError("Invalid Password ");
                            regPassword.requestFocus();

                    }

                        else if (!validatePasswords(regPassword.getText().toString(), regCfnPassword.getText().toString())) {
                            regPassword.setError("Passwords do not match ");
                            regCfnPassword.requestFocus();
                        }
                         else {
                          //  Toast.makeText(RegisterActivity.this, "Input Validation Success", Toast.LENGTH_LONG).show();
                            register(data);
                        }

                }
            });


        } catch (NullPointerException ex) {
            Log.d("RegisterActivity", "onCreate: Null pointer in action bar " + ex.getMessage());
        }
        this.mContext = this;
    }


    protected boolean validateFirstName(String firstName) {
        if(firstName != null) {
            return true;
        } else {
            return false;
        }
    }

    protected boolean validateLastName(String lastName)
    {
        if(lastName != null)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    //Return true if password is valid and false if password is invalid
    protected boolean validatePasswords(String password, String cfnPassword) {
        if(password!=null && password.length()>=8 && password.equals(cfnPassword)) {
            return true;
        } else {
            return false;
        }
    }

    protected boolean validatePassword(String password) {
        if(password!=null && password.length()>=8) {
            return true;
        } else {
            return false;
        }
    }

    //Return true if email is valid and false if email is invalid
    protected boolean validateEmail(String email) {
        String emailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }





    public void register(HashMap data) {

        if (ParkOnTheGo.getInstance().isConnectedToInterNet()) {
            UserServices userServices = ParkOnTheGo.getInstance().getUserServices();
//            ParkOnTheGo.getInstance().showProgressDialog(mContext.getString(R.string
//                    .login_signin), mContext.getString(R.string.login_please_wait));
            Call<SignUpResponse> call = userServices.createNewUser(data.get("firstName").toString(), data.get("lastName").toString(), data.get("email").toString(), data.get("password").toString());
            Log.d("Calling", "register: " + call);
            call.enqueue(new Callback<SignUpResponse>() {
                @Override
                public void onResponse(Call<SignUpResponse> call,
                                       Response<SignUpResponse> response) {
                    ParkOnTheGo.getInstance().hideProgressDialog();
                    if (response.isSuccessful()) {
                        parseResponse(response.body());
                    }
                }

                @Override
                public void onFailure(Call<SignUpResponse> call, Throwable throwable) {
                    Toast.makeText(getApplicationContext(), "Request failed" + throwable, Toast.LENGTH_SHORT).show();

                    // ParkOnTheGo.getInstance().hideProgressDialog();
                    // ParkOnTheGo.getInstance().handleError(throwable);
                }
            });
        } else {
            ParkOnTheGo.getInstance().showAlert(mContext.getString(R.string.no_network));
        }
    }

    private void parseResponse(SignUpResponse response) {
        Toast.makeText(getApplicationContext(), "Request Sucess" + response.getSuccess(), Toast.LENGTH_SHORT).show();
        if (response.getSuccess() == true) {
            Intent intent = new Intent(RegisterActivity.this, HomeScreenActivity.class);
            startActivity(intent);

        } else {

        }
    }
}
