package tech.johnnn.ossave;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import tech.johnnn.ossave.utils.other;

public class VideoEditActivity extends AppCompatActivity {
    ImageView timelinePointerView;
    HorizontalScrollView timelineScrollView,bottomMenuBarScrollView;
    LinearLayout bottomTimelinesBarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_edit);
        timelinePointerView = findViewById(R.id.timelinePointerID);
        timelineScrollView = findViewById(R.id.timelineScrollID);
        bottomTimelinesBarLayout = findViewById(R.id.bottomTimelinesBarLayoutID);
        bottomMenuBarScrollView = findViewById(R.id.bottomMenuBarScrollID);

        timelineScrollView.setHorizontalScrollBarEnabled(false);
        bottomMenuBarScrollView.setHorizontalScrollBarEnabled(false);

        timelinePointerView.setMaxHeight(timelineScrollView.getHeight());
        timelinePointerView.setScaleY(5f);
        bottomTimelinesBarLayout.setPadding(other.getScreenWidth(this)/2-60,0,other.getScreenWidth(this)/2,0);
    }
}