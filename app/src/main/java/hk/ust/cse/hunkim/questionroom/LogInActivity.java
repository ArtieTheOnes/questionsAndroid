package hk.ust.cse.hunkim.questionroom;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import hk.ust.cse.hunkim.questionroom.question.ResponseResult;
import hk.ust.cse.hunkim.questionroom.question.User;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class LogInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        findViewById(R.id.login_button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TryToLogIn();
            }
        });
    }

public void TryToLogIn() {
    final String username = ((EditText) findViewById(R.id.username)).getText().toString();
    String password = ((EditText) findViewById(R.id.password)).getText().toString();
    User user = new User(username, password);
    APIService service = RESTfulAPI.getInstance().getService();
    service.userAuth(user, "login").enqueue(new Callback<ResponseResult>() {
        @Override
        public void onResponse(Response<ResponseResult> response, Retrofit retrofit) {
            if (response.body() != null && response.body().getResult() == true) {
                Intent intent = new Intent();
                intent.putExtra("username", username);
                LogInActivity.this.setResult(RESULT_OK, intent);
                LogInActivity.this.finish();
            }
        }

        @Override
        public void onFailure(Throwable t) {
            Toast.makeText(LogInActivity.this, "Invalid username/password", Toast.LENGTH_SHORT).show();
        }
    });
}
}
