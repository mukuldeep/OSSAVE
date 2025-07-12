package tech.johnnn.ossave.compMedia;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import tech.johnnn.ossave.Log.Log;
import tech.johnnn.ossave.R;
import tech.johnnn.ossave.compMedia.data.VideoMetaData;
import tech.johnnn.ossave.db.videoDB.VideoDBShim;
import tech.johnnn.ossave.file.FileUtils;
import tech.johnnn.ossave.file.InternalFileHandler;
import tech.johnnn.ossave.utils.RandomNumberGenerator;
import tech.johnnn.ossave.utils.ThumbnailFFmpeg;
import tech.johnnn.ossave.utils.TimeUtil;


public class ImportMediaFragment extends Fragment {
    private static String TAG = "ImportMediaFragment";


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    Context context;
    InternalFileHandler ifh;
    LinearLayout mediaScrollLinView, mediaTagLinLay;
    VideoDBShim videoDBShim;


    ActivityResultLauncher<Intent> activityResultLauncherPickVideo;


    public ImportMediaFragment() {
        // Required empty public constructor
    }

    public static ImportMediaFragment newInstance(String param1, String param2) {
        ImportMediaFragment fragment = new ImportMediaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_import_media, container, false);
        context = v.getContext();
        mediaScrollLinView = v.findViewById(R.id.mediaScrollView);
        mediaTagLinLay = v.findViewById(R.id.mediaCompTagLinLay);

        ifh = new InternalFileHandler(context);
        videoDBShim = new VideoDBShim(context);
        registerActivityResult();
        initMediaView();
        return v;
    }

    private void initMediaView(){

        mediaTagLinLay.removeAllViews();

        //add new video
        View addNewView = LayoutInflater.from(context).inflate(R.layout.tag_button, mediaTagLinLay, false);
        CardView addNewCard = addNewView.findViewById(R.id.btnCard);
        TextView addNewText = addNewView.findViewById(R.id.btnText);
        addNewText.setText(" import video ");
        addNewCard.setCardBackgroundColor(context.getColor(R.color.gx_blue_l_5));
        addNewCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewVideo();
            }
        });
        mediaTagLinLay.addView(addNewView);

        //media scroll views
        initialize_media_scroll_view();

    }

    private void addNewVideo(){
        Log.d(TAG,"add new video");
        selectVideoClickFunctionality();
    }

    /**
     * Video selection functionality
     */
    private void selectVideoClickFunctionality(){
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("video/*");
        activityResultLauncherPickVideo.launch(intent);
    }

    private void saveVideoInBG(Intent data){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            String videoId = "";
            VideoMetaData videoMetaData4id = new VideoMetaData();
            try {
                if (data != null) {
                    Uri videoUri = data.getData();
                    int isSaveSuccess = saveSelectedFile(videoUri,videoMetaData4id);
                    Log.d(TAG, "isSaveSuccess:" + isSaveSuccess);

                    if (isSaveSuccess == 1) {
                        // Post back to the UI thread
                        handler.post(() -> {
                            Toast.makeText(context, "Video saved ", Toast.LENGTH_LONG).show();
                            initMediaView();
                        });
                    }
//                    else {
//                                clearEditorUI();
//                                showHideBottomMenu(false);
//                                hideLoadingBar();
//                    }
                }
            }catch (Exception e){
                e.printStackTrace();
                handler.post(() ->
                        Toast.makeText(context, "Failed to save video", Toast.LENGTH_SHORT).show()
                );
            }


//            handler.post(() -> {
//                //handleLiveProcessingWindow(-1);
//                Log.d(TAG,"videoId = "+videoMetaData4id.getVideoId());
//                //start video tagging process
//                if (!videoMetaData4id.getVideoId().equals("")) {
//                    //startVideoPreProcess(videoMetaData4id.getVideoId());
//                }
//            });

        });


    }

    private int saveSelectedFile(Uri videoUri, VideoMetaData videoIdOut){
        if (videoUri != null) {
            String videoFileName = "vid"+ RandomNumberGenerator.generateSecureRandomInt()+".mp4";
            videoIdOut.setVideoId( videoFileName);
            if (FileUtils.saveFileFromUriToInternalStorage(context, videoUri, ifh.DIR_VIDEO, videoFileName)) {
                Log.d(TAG, "The video file saved "+videoFileName);
//                if(!extractMetaData(videoFileName)){
//                    Log.d(TAG, "The video is Invalid! Some Error occured "+videoFileName);
//                    Toast.makeText(context,"error_try_different_video",Toast.LENGTH_SHORT).show();
//                    //deleteSelectedFile();
//                    return 0;
//                }
//
                String vid_desc = "video_"+ TimeUtil.timestampToLocalTime(TimeUtil.getTimeStampMillis(),"yyyy-MM-dd HH:mm:ss");
                //Save to database
                VideoDBShim videoDBShim = new VideoDBShim(context);
                videoDBShim.addNewVideoOnSelect(videoFileName,vid_desc);

                //detect video scene change
                //FFmpegKitOps fFmpegKitOps = new FFmpegKitOps(context);
                //fFmpegKitOps.detectSceneChange(ifh.getInternalDirPath()+"/"+ifh.DIR_VIDEO+"/"+videoFileName,0.4);
                //InitialVideoResizer.resizeToTarget(context,ifh.getInternalDirPath()+"/"+ifh.DIR_VIDEO+"/"+videoFileName, videoMetaData.getVideoWidthStr(), videoMetaData.getVideoHeightStr());

                return 1;
            } else {// Error occurred while saving the video.
                Log.e(TAG, "error while saving the video");
            }
        }
        return 0;
    }


    public void initialize_media_scroll_view(){
        ArrayList<String> videoIds;
        videoIds = videoDBShim.getAllVideosSortBySelectionTime();

        mediaScrollLinView.removeAllViews();

        //load selected video from db
        for(String videoId: videoIds){
            Log.d(TAG,"videoId:"+videoId);
            View newVideoView = LayoutInflater.from(context).inflate(R.layout.item_video_scroll, mediaScrollLinView, false);
            TextView videoTitle = newVideoView.findViewById(R.id.videoItemScrollText);
            videoTitle.setText(videoId);

            ImageView thumbImg = newVideoView.findViewById(R.id.videoScrollItemThumb);
            if (ifh.isExists(ifh.DIR_THUMBS, videoId + "_thumb.jpg")) {
                Uri thumbUri = Uri.fromFile(new File(ifh.internalStorageDir.getAbsolutePath() + "/" + ifh.DIR_THUMBS + "/" + videoId + "_thumb.jpg"));
                thumbImg.setImageURI(thumbUri);
            } else {
                ThumbnailFFmpeg thumbnailFFmpeg = new ThumbnailFFmpeg();
                thumbnailFFmpeg.setThumbnail(ifh, thumbImg, ifh.DIR_VIDEO, videoId);
            }

            // state buttons
            String tagModel = videoDBShim.getTagModel(videoId);
            TextView btnTxt = newVideoView.findViewById(R.id.videoItemBtnTxt);
            //videoDBShim.updateTagModel(videoId,"[]");

            if(tagModel.equals("") || tagModel.equals("{}")) {
                ImageView startProcessBtn = newVideoView.findViewById(R.id.itemVideoRightBtnProcess);
                btnTxt.setText("Start Process");
                startProcessBtn.setVisibility(View.VISIBLE);
                startProcessBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(context,"Feature under development",Toast.LENGTH_SHORT).show();
//                        startVideoPreProcess(videoId);
//                        extractFrames(videoId);
//                        videoDBShim.updateTagModel(videoId,"{\"status\":\"processing\"}");
//                        init_video_views();
                    }
                });
            }else if(tagModel.equals("{\"status\":\"processing\"}")){
                ImageView processingBtn = newVideoView.findViewById(R.id.itemVideoRightBtnProcessing);
                btnTxt.setText("Processing");
                processingBtn.setVisibility(View.VISIBLE);
            }else{
                ImageView processDoneBtn = newVideoView.findViewById(R.id.itemVideoRightBtnReady);
                btnTxt.setText("All Done");
                processDoneBtn.setVisibility(View.VISIBLE);
            }

            newVideoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context,"Feature under development",Toast.LENGTH_SHORT).show();
//                    createVideoDialog(videoId,ifh.getInternalDirPath()+"/"+ifh.DIR_VIDEO+"/"+videoId);
                }
            });

            //delete button
            ImageView deleteBtn = newVideoView.findViewById(R.id.itemVideoDeleteBtn);
            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteAVideo(videoId);
                }
            });

            mediaScrollLinView.addView(newVideoView);
        }
    }

    public boolean deleteAVideo(String videoID){

        //delete from DB
        boolean isDelDB = videoDBShim.deleteAVideo(videoID);

        //delete from FS
        boolean isDelFS = ifh.deleteFile(ifh.DIR_VIDEO,videoID);
        boolean isDelFSFrames = ifh.deleteFolder(ifh.getInternalDirPath()+"/"+ifh.DIR_VIDEO_TEMP+"/"+videoID+"_frames");
        boolean isDelFSThumb = ifh.deleteFile(ifh.getInternalDirPath()+"/"+ifh.DIR_THUMBS+"/"+videoID+"_thumb.jpg");

        Log.d(TAG,"deleting audio. ID: "+videoID+" isDbDel:"+isDelDB+" isFsDel:"+isDelFS+" isDelFSFrames:"+isDelFSFrames+" isDelFSThumb:"+isDelFSThumb);

        initMediaView();

        return isDelDB & isDelFS;
    }


    private void registerActivityResult() {
        activityResultLauncherPickVideo = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {

                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // Handle the result here
                        Intent data = result.getData();
                        Log.d(TAG,"success");
                        //handleLiveProcessingWindow(1);
                        saveVideoInBG(data);
                    }else{
                        //hideLoadingBar();

                    }
                });
    }



}