package hk.ust.cse.hunkim.questionroom;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A login screen that offers login via email/password.
 */

public class JoinActivity extends Activity {
    public static final String ROOM_NAME = "Room_name";
    private static boolean incognitoMode = false;
    private static String mUsername = "Anonymous";
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    // UI references.
    private TextView roomNameView;
    //private String username= "Anonymous";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);


        // Set up the login form.
        roomNameView = (TextView) findViewById(R.id.room_name);

        roomNameView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_NULL && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    attemptJoin(textView);
                }
                return true;
            }
        });

        findViewById(R.id.login_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoLogin();
            }
        });

        findViewById(R.id.signup_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoSignup();
            }
        });

        ((Switch) findViewById(R.id.incognitoSwitch)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    incognitoMode = true;
                    Toast.makeText(JoinActivity.this, "Incognito Mode On", Toast.LENGTH_SHORT).show();

                }
                else
                {
                    incognitoMode = false;
                    Toast.makeText(JoinActivity.this, "Incognito Mode Off", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptJoin(View view) {
        // Reset errors.
        roomNameView.setError(null);

        // Store values at the time of the login attempt.
        String room_name = roomNameView.getText().toString();

        boolean cancel = false;

        // Check for a valid email address.
        if (TextUtils.isEmpty(room_name)) {
            roomNameView.setError(getString(R.string.error_field_required));

            cancel = true;
        } else if (!isEmailValid(room_name)) {
            roomNameView.setError(getString(R.string.error_invalid_room_name));
            cancel = true;
        }

        if (cancel) {
            roomNameView.setText("");
            roomNameView.requestFocus();
        } else {
            // Start main activity
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(ROOM_NAME, room_name);
            //intent.putExtra("Username",username);
            startActivity(intent);
        }
    }

    private boolean isEmailValid(String room_name) {
        // http://stackoverflow.com/questions/8248277
        // Make sure alphanumeric characters
        return !room_name.matches("^.*[^a-zA-Z0-9 ].*$");
    }

    public void gotoLogin(){
        Intent intent=new Intent(this, LogInActivity.class);
        startActivityForResult(intent,3);
    }

    public void gotoSignup(){
        Intent intent=new Intent(this, SignUpActivity.class);
        startActivityForResult(intent,4);
    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        if (requestCode==3){
            /*if (resultCode==RESULT_OK){
                username=data.getExtras().getString("username");
                ((Button) findViewById(R.id.login_button)).setText(username);
            }*/
                ((TextView) findViewById(R.id.login_button)).setText(username);
            }
        }
        if (requestCode==4){
            /*if (resultCode==RESULT_OK){
                username=data.getExtras().getString("username");
            }*/
        }

    }

    public static boolean isIncognitoMode(){return incognitoMode;}

    public static String getmUsername() {return mUsername;}

    public static void setmUsername(String username) {mUsername = username;}
}

