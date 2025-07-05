package tech.johnnn.ossave;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import tech.johnnn.ossave.file.InternalFileHandler;
import tech.johnnn.ossave.utils.PermissionHandler;

public class MainActivity extends AppCompatActivity {

    private Button openEditorButton;
    InternalFileHandler ifh;
    public PermissionHandler permissionHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        permissionHandler = new PermissionHandler(this);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermissionHandler.GENERAL_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted. You can now access external storage.
                init_after_permission();
            } else {
                // Permission denied. Handle this case (e.g., show a message or request again later).
                Toast.makeText(getApplicationContext(),"App will not work without this permission!",Toast.LENGTH_SHORT).show();
                permissionHandler.requestMissingPermission();
            }
        }
    }

    public void init_after_permission(){
        openEditorButton = findViewById(R.id.openEditorButtonID);
        openEditorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchVideoEditor();
            }
        });

        ifh = new InternalFileHandler(this);
        ifh.createRequiredSubDirectory();

    }

    public void launchVideoEditor(){
        Intent videoEditorOpenIntent = new Intent(MainActivity.this, VideoEditActivity.class);
        //myIntent.putExtra("key", value); //Optional parameters
        MainActivity.this.startActivity(videoEditorOpenIntent);
    }
}