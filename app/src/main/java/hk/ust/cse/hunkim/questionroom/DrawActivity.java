package hk.ust.cse.hunkim.questionroom;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.graphics.BitmapFactory;
import android.widget.Switch;

import com.github.nkzawa.socketio.client.Socket;
import com.google.repacked.apache.commons.lang3.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

import hk.ust.cse.hunkim.questionroom.question.Question;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class DrawActivity extends Activity implements View.OnClickListener {
    private DrawingView drawView;
    private float smallBrush, mediumBrush, largeBrush;
    private ImageButton currPaint, drawBtn, eraseBtn,newBtn;
    private String mRoomName;
    final private String BlankDraw="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAuwAAAN4CAIAAAAXwipKAAAAA3NCSVQICAjb4U/gAAAPS0lEQVR4nO3WQQ0AIBDAMMC/58MDH7KkVbDn9swsAICa8zsAAOCFiQEAkkwMAJBkYgCAJBMDACSZGAAgycQAAEkmBgBIMjEAQJKJAQCSTAwAkGRiAIAkEwMAJJkYACDJxAAASSYGAEgyMQBAkokBAJJMDACQZGIAgCQTAwAkmRgAIMnEAABJJgYASDIxAECSiQEAkkwMAJBkYgCAJBMDACSZGAAgycQAAEkmBgBIMjEAQJKJAQCSTAwAkGRiAIAkEwMAJJkYACDJxAAASSYGAEgyMQBAkokBAJJMDACQZGIAgCQTAwAkmRgAIMnEAABJJgYASDIxAECSiQEAkkwMAJBkYgCAJBMDACSZGAAgycQAAEkmBgBIMjEAQJKJAQCSTAwAkGRiAIAkEwMAJJkYACDJxAAASSYGAEgyMQBAkokBAJJMDACQZGIAgCQTAwAkmRgAIMnEAABJJgYASDIxAECSiQEAkkwMAJBkYgCAJBMDACSZGAAgycQAAEkmBgBIMjEAQJKJAQCSTAwAkGRiAIAkEwMAJJkYACDJxAAASSYGAEgyMQBAkokBAJJMDACQZGIAgCQTAwAkmRgAIMnEAABJJgYASDIxAECSiQEAkkwMAJBkYgCAJBMDACSZGAAgycQAAEkmBgBIMjEAQJKJAQCSTAwAkGRiAIAkEwMAJJkYACDJxAAASSYGAEgyMQBAkokBAJJMDACQZGIAgCQTAwAkmRgAIMnEAABJJgYASDIxAECSiQEAkkwMAJBkYgCAJBMDACSZGAAgycQAAEkmBgBIMjEAQJKJAQCSTAwAkGRiAIAkEwMAJJkYACDJxAAASSYGAEgyMQBAkokBAJJMDACQZGIAgCQTAwAkmRgAIMnEAABJJgYASDIxAECSiQEAkkwMAJBkYgCAJBMDACSZGAAgycQAAEkmBgBIMjEAQJKJAQCSTAwAkGRiAIAkEwMAJJkYACDJxAAASSYGAEgyMQBAkokBAJJMDACQZGIAgCQTAwAkmRgAIMnEAABJJgYASDIxAECSiQEAkkwMAJBkYgCAJBMDACSZGAAgycQAAEkmBgBIMjEAQJKJAQCSTAwAkGRiAIAkEwMAJJkYACDJxAAASSYGAEgyMQBAkokBAJJMDACQZGIAgCQTAwAkmRgAIMnEAABJJgYASDIxAECSiQEAkkwMAJBkYgCAJBMDACSZGAAgycQAAEkmBgBIMjEAQJKJAQCSTAwAkGRiAIAkEwMAJJkYACDJxAAASSYGAEgyMQBAkokBAJJMDACQZGIAgCQTAwAkmRgAIMnEAABJJgYASDIxAECSiQEAkkwMAJBkYgCAJBMDACSZGAAgycQAAEkmBgBIMjEAQJKJAQCSTAwAkGRiAIAkEwMAJJkYACDJxAAASSYGAEgyMQBAkokBAJJMDACQZGIAgCQTAwAkmRgAIMnEAABJJgYASDIxAECSiQEAkkwMAJBkYgCAJBMDACSZGAAgycQAAEkmBgBIMjEAQJKJAQCSTAwAkGRiAIAkEwMAJJkYACDJxAAASSYGAEgyMQBAkokBAJJMDACQZGIAgCQTAwAkmRgAIMnEAABJJgYASDIxAECSiQEAkkwMAJBkYgCAJBMDACSZGAAgycQAAEkmBgBIMjEAQJKJAQCSTAwAkGRiAIAkEwMAJJkYACDJxAAASSYGAEgyMQBAkokBAJJMDACQZGIAgCQTAwAkmRgAIMnEAABJJgYASDIxAECSiQEAkkwMAJBkYgCAJBMDACSZGAAgycQAAEkmBgBIMjEAQJKJAQCSTAwAkGRiAIAkEwMAJJkYACDJxAAASSYGAEgyMQBAkokBAJJMDACQZGIAgCQTAwAkmRgAIMnEAABJJgYASDIxAECSiQEAkkwMAJBkYgCAJBMDACSZGAAgycQAAEkmBgBIMjEAQJKJAQ";
    private RESTfulAPI mAPI;
    private Socket mSocket;
    private String mUsername;
    //private boolean incognitoMode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);
        drawView = (DrawingView)findViewById(R.id.drawing);
        LinearLayout paintLayout = (LinearLayout)findViewById(R.id.paint_colors);
        currPaint = (ImageButton)paintLayout.getChildAt(0);
        currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed, null));
        smallBrush = 5;
        mediumBrush = 10;
        largeBrush = 20;
        drawBtn = (ImageButton)findViewById(R.id.draw_btn);
        drawBtn.setOnClickListener(this);
        eraseBtn = (ImageButton)findViewById(R.id.erase_btn);
        eraseBtn.setOnClickListener(this);
        newBtn = (ImageButton)findViewById(R.id.new_btn);
        newBtn.setOnClickListener(this);
        findViewById(R.id.drawRegion).setVisibility(View.GONE);
        findViewById(R.id.sendButton2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });

        ((Switch)findViewById(R.id.newQuestionSwitch)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    findViewById(R.id.drawRegion).setVisibility(View.VISIBLE);
                }
                else{
                    findViewById(R.id.drawRegion).setVisibility(View.GONE);
                }
            }
        });

        Intent intent=getIntent();
        String picture= intent.getExtras().getString("image");
        mRoomName=intent.getExtras().getString("RoomName");
        mUsername = intent.getExtras().getString("Username");
        //incognitoMode = intent.getBooleanExtra("IncognitoMode",false);
        if (picture!=null) {
            picture=picture.substring(22);
            byte[] encodeByte = Base64.decode(picture, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            drawView.setOriginalBitmap(bitmap);
        }

        drawView.setBrushSize(smallBrush);
        drawView.setLastBrushSize(smallBrush);

        mAPI = RESTfulAPI.getInstance();
        mSocket = mAPI.getSocket();





    }
    @Override
    public void onClick(View view){
        if(view.getId()==R.id.draw_btn){
            final Dialog brushDialog = new Dialog(this);
            brushDialog.setTitle("Brush size:");
            brushDialog.setContentView(R.layout.brush_chooser);
            ImageButton smallBtn = (ImageButton)brushDialog.findViewById(R.id.small_brush);
            smallBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawView.setBrushSize(smallBrush);
                    drawView.setLastBrushSize(smallBrush);
                    brushDialog.dismiss();
                }
            });
            ImageButton mediumBtn = (ImageButton)brushDialog.findViewById(R.id.medium_brush);
            mediumBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawView.setBrushSize(mediumBrush);
                    drawView.setLastBrushSize(mediumBrush);
                    brushDialog.dismiss();
                }
            });

            ImageButton largeBtn = (ImageButton)brushDialog.findViewById(R.id.large_brush);
            largeBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    drawView.setBrushSize(largeBrush);
                    drawView.setLastBrushSize(largeBrush);
                    brushDialog.dismiss();
                }
            });
            drawView.setErase(false);
            brushDialog.show();
            drawView.setBrushSize(mediumBrush);
        }


        else if(view.getId()==R.id.erase_btn){
            final Dialog brushDialog = new Dialog(this);
            brushDialog.setTitle("Eraser size:");
            brushDialog.setContentView(R.layout.brush_chooser);
            ImageButton smallBtn = (ImageButton)brushDialog.findViewById(R.id.small_brush);
            smallBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    drawView.setErase(true);
                    drawView.setBrushSize(smallBrush);
                    brushDialog.dismiss();
                }
            });
            ImageButton mediumBtn = (ImageButton)brushDialog.findViewById(R.id.medium_brush);
            mediumBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawView.setErase(true);
                    drawView.setBrushSize(mediumBrush);
                    brushDialog.dismiss();
                }
            });
            ImageButton largeBtn = (ImageButton)brushDialog.findViewById(R.id.large_brush);
            largeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawView.setErase(true);
                    drawView.setBrushSize(largeBrush);
                    brushDialog.dismiss();
                }
            });
            brushDialog.show();
        }
        else if(view.getId()==R.id.new_btn){
            AlertDialog.Builder newDialog = new AlertDialog.Builder(this);
            newDialog.setTitle("New drawing");
            newDialog.setMessage("Start new drawing (you will lose the current drawing)?");
            newDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    drawView.startNew();
                    dialog.dismiss();
                }
            });
            newDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    dialog.cancel();
                }
            });
            newDialog.show();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void paintClicked(View view){
        if(view!=currPaint){
            drawView.setErase(false);
            ImageButton imgView = (ImageButton)view;
            String color = view.getTag().toString();
            drawView.setBrushSize(drawView.getLastBrushSize());
            drawView.setColor(color);
            imgView.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed,null));
            currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint,null));
            currPaint=(ImageButton)view;
        }
    }


    private void sendMessage() {
        EditText inputText = (EditText) findViewById(R.id.messageInput);
        String input = inputText.getText().toString();
        if (!input.trim().isEmpty()) {
            // Create our 'model', a Chat object
            //Question question = new Question(input, mRoomName);
            Question question = new Question(input, mRoomName, mUsername, JoinActivity.isIncognitoMode()); // change Anonymous to the name of logged in user
            if (((Switch)findViewById(R.id.newQuestionSwitch)).isChecked()) {
                drawView.setDrawingCacheEnabled(true);
                Bitmap bitmap = drawView.getDrawingCache();
                bitmap = (Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), 400, false));
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);

                byte[] b = baos.toByteArray();



                String temp = Base64.encodeToString(b, Base64.DEFAULT);
                String picture = "data:image/png;base64," + temp;
                question.setImage(picture);
            }
            // Create a new, auto-generated child of that chat location, and save our chat data there
            mAPI.saveQuesion(question).enqueue(new Callback<Question>() {
                @Override
                public void onResponse(Response<Question> response, Retrofit retrofit) {
                    Question question = response.body();
                    if (question != null) {
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("id", question.getKey());
                            jsonObject.put("room", mRoomName);
                        } catch (JSONException e) {
                        }
                        mSocket.emit("new post", jsonObject);
                    } else {
                        Log.e("Empty Response Body", "Null Question");
                    }
                }

                @Override
                public void onFailure(Throwable t) {

                }
            });
            inputText.setText("");
            DrawActivity.this.finish();
        }
    }

}
