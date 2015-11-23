package hk.ust.cse.hunkim.questionroom;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import hk.ust.cse.hunkim.questionroom.question.ResponseResult;
import hk.ust.cse.hunkim.questionroom.question.User;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class SignUpActivity extends AppCompatActivity {
    private String mUsername;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mUsername="";
        findViewById(R.id.signup_button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TryToSignUp();
            }
        });
        ((EditText) findViewById(R.id.password2)).setTransformationMethod(PasswordTransformationMethod.getInstance());
    }

public void TryToSignUp() {
    final String username = ((EditText) findViewById(R.id.username2)).getText().toString();
    final String password = ((EditText) findViewById(R.id.password2)).getText().toString();
    mUsername=username;
    APIService service = RESTfulAPI.getInstance().getService();
    User user = new User(username, password);
    service.userAuth(user, "signup").enqueue(new Callback<ResponseResult>() {
        @Override
        public void onResponse(Response<ResponseResult> response, Retrofit retrofit) {
            ResponseResult result = response.body();
            if (username.equals("") || password.equals("")) {
                Toast.makeText(SignUpActivity.this, "Empty username/password", Toast.LENGTH_SHORT).show();
            } else if (result != null && result.getResult() == true) {
               signupDialog(username);
            } else if (result != null) {
                ((EditText) findViewById(R.id.password2)).setText("");
                Toast.makeText(SignUpActivity.this, "The username has existed.", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onFailure(Throwable t) {
            ((EditText) findViewById(R.id.password2)).setText("");
            Toast.makeText(SignUpActivity.this, "Invalid username/password", Toast.LENGTH_SHORT).show();
        }
    });
}

    protected void signupDialog(final String Username)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
        builder.setMessage("Successful registered as " + Username).setTitle("Success");
        builder.setNeutralButton("Log in as " + Username , new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                JoinActivity.setmUsername(Username);
                Intent intent = new Intent();
                //intent.putExtra("username",username);
                SignUpActivity.this.setResult(RESULT_OK, intent);
                finish();
            }
        });
        builder.create().show();
    }
}
