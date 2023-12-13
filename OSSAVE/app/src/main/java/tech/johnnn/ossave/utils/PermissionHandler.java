package tech.johnnn.ossave.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
    This handles all the permissions required to function the application
    It requests all required permissions inside constructor.
    Public APIs of this class
        1. constructor(Context)
        2. isAllPermissionAllowed()
 */
public class PermissionHandler {
    public static final int GENERAL_PERMISSION_REQUEST_CODE = 142;
    Context context=null;

    public PermissionHandler(Context context){
        this.context=context;
        requestMissingPermission();
    }

    private boolean isAllowed(String Permission){
        return ContextCompat.checkSelfPermission(context, Permission) == PackageManager.PERMISSION_GRANTED;
    }
    private void requestPerm(String[] permissions,int permission_code){
        ActivityCompat.requestPermissions((Activity) context,permissions, permission_code);
    }

    public boolean isAllPermissionAllowed(){
        return checkMediaPermission();
    }

    public void requestMissingPermission(){
        List<String> perms = new ArrayList<>();
        //if(!checkMediaPermission()) //perms should not be null as it triggers init_after_permission
        // other additional permission should follow the above format instead of if(true)
        if(true){
            List<String> m_perm = new ArrayList<>(Arrays.asList(mediaPermissions()));
            perms.addAll(m_perm);
        }

        if(perms.size() > 0) {
            requestPerm(perms.toArray(new String[0]), GENERAL_PERMISSION_REQUEST_CODE);
        }
    }

    /*
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted. You can now access external storage.
                init_after_permission();
            } else {
                // Permission denied. Handle this case (e.g., show a message or request again later).
                Toast.makeText(getApplicationContext(),"App will not work without this permission!",Toast.LENGTH_SHORT).show();
                check_for_permission(context);
            }
        }
    }
     */




    private boolean checkMediaPermission(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            return isAllowed( Manifest.permission.READ_MEDIA_VIDEO) && isAllowed( Manifest.permission.READ_MEDIA_AUDIO) && isAllowed( Manifest.permission.READ_MEDIA_IMAGES);
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            return isAllowed(Manifest.permission.READ_EXTERNAL_STORAGE) && isAllowed(Manifest.permission.WRITE_EXTERNAL_STORAGE) && isAllowed(Manifest.permission.MANAGE_EXTERNAL_STORAGE);
        }
        return true;
    }

    private String[] mediaPermissions() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            return new String[]{
                                Manifest.permission.READ_MEDIA_VIDEO,
                                Manifest.permission.READ_MEDIA_IMAGES,
                                Manifest.permission.READ_MEDIA_AUDIO};
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            return new String[]{
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE};

        }
        return new String[]{};
    }
}
