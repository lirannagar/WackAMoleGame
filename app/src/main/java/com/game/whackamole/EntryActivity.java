package com.game.whackamole;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

public class EntryActivity  extends AppCompatActivity {

    private EditText nameET;
    private Button nextBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        // Set to fullscreen
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //set view
        setContentView(R.layout.entry_activity);

        nameET=findViewById(R.id.name_et);
        nextBtn=findViewById(R.id.next_btn);



        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                EditText editText = (EditText) findViewById(R.id.name_et);
                String name = nameET.getText().toString();

                if(name.length()<=0){
                    editText.setError("Please enter your name.");
                }else{
                    intent.putExtra("name", name);
                    startActivity(intent);
                    finish();
                }



            }
        });


    }
}
