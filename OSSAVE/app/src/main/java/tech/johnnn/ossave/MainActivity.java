package tech.johnnn.ossave;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button openEditorButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        openEditorButton = findViewById(R.id.openEditorButtonID);

        openEditorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchVideoEditor();
            }
        });
    }

    public void launchVideoEditor(){
        Intent videoEditorOpenIntent = new Intent(MainActivity.this, VideoEditActivity.class);
        //myIntent.putExtra("key", value); //Optional parameters
        MainActivity.this.startActivity(videoEditorOpenIntent);
    }
}