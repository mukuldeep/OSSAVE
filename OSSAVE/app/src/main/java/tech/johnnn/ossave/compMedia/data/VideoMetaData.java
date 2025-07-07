package tech.johnnn.ossave.compMedia.data;

public class VideoMetaData {

    String videoId;

    String framecntStr;
    String framerateStr;
    String videoLengthStr;
    String videoHasVideoStr;
    String videoHasAudioStr;
    String videoHeightStr;
    String videoWidthStr;
    String videoAudioSampleRateStr;
    String videoBitrateStr;

    int framecnt;// = Integer.parseInt(framecntStr);
    int videoLength;// = Integer.parseInt(videoLengthStr);
    int videoHeight;// = Integer.parseInt(videoHeightStr);
    int videoWidth;// = Integer.parseInt(videoWidthStr);

    public VideoMetaData(){
        videoId="";
        framecntStr="";
        framerateStr="";
        videoLengthStr="";
        videoHasVideoStr="";
        videoHasAudioStr="";
        videoHeightStr="";
        videoWidthStr="";
        videoAudioSampleRateStr="";
        videoBitrateStr="";

        framecnt=0;
        videoLength=0;
        videoHeight=0;
        videoWidth=0;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getFramecntStr() {
        return framecntStr;
    }
    public void setFramecntStr(String framecntStr) {
        this.framecntStr = framecntStr;
        if(!framecntStr.equals("")) {
            try {
                framecnt = (int) Float.parseFloat(framecntStr);
            }catch (Exception e){
                //do nothing
            }
        }
    }

    public String getFramerateStr() {
        return framerateStr;
    }

    public void setFramerateStr(String framerateStr) {
        this.framerateStr = framerateStr;
    }

    public String getVideoLengthStr() {
        return videoLengthStr;
    }

    public void setVideoLengthStr(String videoLengthStr) {
        this.videoLengthStr = videoLengthStr;
        if(!videoLengthStr.equals("")) {
            try {
                videoLength = Integer.parseInt(videoLengthStr);
            }catch (Exception e){
                //do nothing
            }
        }
    }

    public String getVideoHasVideoStr() {
        return videoHasVideoStr;
    }

    public void setVideoHasVideoStr(String videoHasVideoStr) {
        this.videoHasVideoStr = videoHasVideoStr;
    }

    public String getVideoHasAudioStr() {
        return videoHasAudioStr;
    }

    public void setVideoHasAudioStr(String videoHasAudioStr) {
        this.videoHasAudioStr = videoHasAudioStr;
    }

    public String getVideoHeightStr() {
        return videoHeightStr;
    }

    public void setVideoHeightStr(String videoHeightStr) {
        this.videoHeightStr = videoHeightStr;
        if(!videoHeightStr.equals("")) {
            try {
                videoHeight = Integer.parseInt(videoHeightStr);
            }catch (Exception e){
            //do nothing
        }
        }
    }

    public String getVideoWidthStr() {
        return videoWidthStr;
    }

    public void setVideoWidthStr(String videoWidthStr) {
        this.videoWidthStr = videoWidthStr;
        if(!videoWidthStr.equals("")) {
            try{
            videoWidth = Integer.parseInt(videoWidthStr);
            }catch (Exception e){
                //do nothing
            }
        }
    }

    public String getVideoAudioSampleRateStr() {
        return videoAudioSampleRateStr;
    }

    public void setVideoAudioSampleRateStr(String videoAudioSampleRateStr) {
        this.videoAudioSampleRateStr = videoAudioSampleRateStr;
    }

    public String getVideoBitrateStr() {
        return videoBitrateStr;
    }

    public void setVideoBitrateStr(String videoBitrateStr) {
        this.videoBitrateStr = videoBitrateStr;
    }

    public int getFramecnt() {
        return framecnt;
    }

    public void setFramecnt(int framecnt) {
        this.framecnt = framecnt;
    }

    public int getVideoLength() {
        return videoLength;
    }

    public void setVideoLength(int videoLength) {
        this.videoLength = videoLength;
    }

    public int getVideoHeight() {
        return videoHeight;
    }

    public void setVideoHeight(int videoHeight) {
        this.videoHeight = videoHeight;
    }

    public int getVideoWidth() {
        return videoWidth;
    }

    public void setVideoWidth(int videoWidth) {
        this.videoWidth = videoWidth;
    }
}
