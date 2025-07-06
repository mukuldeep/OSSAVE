package tech.johnnn.ossave;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import tech.johnnn.ossave.compEditor.CompEditorConst;
import tech.johnnn.ossave.compEditor.EditorFragment;
import tech.johnnn.ossave.compMedia.CompMediaConst;
import tech.johnnn.ossave.compMedia.ImportMediaFragment;
import tech.johnnn.ossave.compTest.CompTestConst;
import tech.johnnn.ossave.compTest.TestFragment;
import tech.johnnn.ossave.db.DBinitializer;
import tech.johnnn.ossave.file.InternalFileHandler;
import tech.johnnn.ossave.utils.PermissionHandler;

public class MainActivity extends AppCompatActivity {

    private CardView openEditorButton;
    InternalFileHandler ifh;
    public PermissionHandler permissionHandler;

    FrameLayout uAFragmentContainer;
    Fragment importMediaFragment = null, testFragment = null, editorFragment = null;
    Fragment selectedFragment = null;

    CardView importMediaNavBtn, editorNavBtn, testNavBtn;

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
        openEditorButton = findViewById(R.id.nav_top_sample_btn);
        openEditorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchVideoEditor();
            }
        });

        ifh = new InternalFileHandler(this);
        ifh.createRequiredSubDirectory();

        new DBinitializer(this);


        uAFragmentContainer = findViewById(R.id.unifiedFragmentContainer);

        importMediaNavBtn = findViewById(R.id.nav_top_imported_media_btn);
        editorNavBtn = findViewById(R.id.nav_top_editor_btn);
        testNavBtn = findViewById(R.id.nav_top_test_btn);
        handle_nav_btn_click();
        show_fragment(CompEditorConst.fragmentId);

    }

    public void launchVideoEditor(){
        Intent videoEditorOpenIntent = new Intent(MainActivity.this, VideoEditActivity.class);
        //myIntent.putExtra("key", value); //Optional parameters
        MainActivity.this.startActivity(videoEditorOpenIntent);
    }

    private void handle_nav_btn_click(){
        importMediaNavBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show_fragment(CompMediaConst.fragmentId);
            }
        });

        editorNavBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show_fragment(CompEditorConst.fragmentId);
            }

        });


        testNavBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show_fragment(CompTestConst.fragmentId);
            }
        });
    }


    private void show_fragment(int fragmentId){

        if(fragmentId == CompEditorConst.fragmentId) {
            if(editorFragment == null){
                editorFragment = EditorFragment.newInstance("","");
            }
            selectedFragment = editorFragment;
        }else if(fragmentId == CompMediaConst.fragmentId) {
            if(importMediaFragment == null){
                importMediaFragment = ImportMediaFragment.newInstance("","");
            }
            selectedFragment = importMediaFragment;
        } else if(fragmentId == CompTestConst.fragmentId) {
            if(testFragment == null){
                testFragment = TestFragment.newInstance("","");
            }
            selectedFragment = testFragment;
        }

        if (selectedFragment != null) {
            //getSupportFragmentManager().popBackStackImmediate();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.unifiedFragmentContainer, selectedFragment)
                    .commit();
        }
    }


}