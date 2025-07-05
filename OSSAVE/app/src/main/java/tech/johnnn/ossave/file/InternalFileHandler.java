package tech.johnnn.ossave.file;

import android.content.Context;
import android.util.Pair;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import tech.johnnn.ossave.Log.Log;
import tech.johnnn.ossave.utils.OtherUtils;

public class InternalFileHandler {
    private static String TAG = "ifh";
    Context context;
    final String[] directories={"audio",        // store audio files
                                "video",        // store selected video files
                                "project",      // store all project related files
                                "exported",     // store exported video files
                                "video_temp",   // store selected video files temporarily and process files
                                "thumbs",       // store thumbnails for videos
                                "others"        // to store other files
                                };
    public final String DIR_PROFILE_PRV = "profile_prv";
    public final String DIR_AUDIO = directories[0];
    public final String DIR_VIDEO = directories[1];
    public final String DIR_PROJECT = directories[2];
    public final String DIR_EXPORTED = directories[3];
    public final String DIR_VIDEO_TEMP = directories[4];
    public final String DIR_THUMBS = directories[5];
    public final String DIR_OTHERS = directories[6];

    public File internalStorageDir;
    public InternalFileHandler(Context appContext){
        context=appContext;
        internalStorageDir=getInternalDir();
    }

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public static String generateRandomString(int length) {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(randomIndex);
            sb.append(randomChar);
        }
        return sb.toString();
    }
    public File getInternalDir(){
        File internalStorageDir = context.getFilesDir();
        return internalStorageDir;
    }
    public String getInternalDirPath(){
        File internalStorageDir = context.getFilesDir();
        return internalStorageDir.getAbsolutePath();
    }

    public boolean deleteFile(String subDir,String filename){

        File fileToDelete;
        if(subDir.equals("")) {
            fileToDelete=new File(internalStorageDir, filename);
        }else{
            File subDirRef = new File(internalStorageDir,subDir);
            fileToDelete = new File(subDirRef,filename);
        }

        if(fileToDelete.delete()){
            return true;
        }
        return false;
    }

    /**
     * deletes file given full path
     * @param fullPath file full path
     * @return whether deleted
     * added 05-06-2024 v45
     */
    public boolean deleteFile(String fullPath){
        File fileToDelete;
        fileToDelete = new File(fullPath);
        if(fileToDelete.delete()){
            Log.d(TAG,fileToDelete.getAbsolutePath()+" deleted successfully");
            return true;
        }
        Log.d(TAG,fileToDelete.getAbsolutePath()+" deletion failed");
        return false;
    }

    /**
     * Deletes folder and all contents recurively
     * @param dirPath fullpath of the directory
     * @return result whether deleted everything successfully
     * added 05-06-2024 v45
     */
    public boolean deleteFolder(String dirPath){
        File directory = new File(dirPath);
        if(directory.isFile()){ // 12-09-2024 added if the directory path is actually a file
            deleteFile(dirPath);
            return true;
        }else if(!directory.isDirectory()){
            return false;
        }
        File[] files = directory.listFiles();

        boolean res = true;
        // delete all internal content recursive
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteFolder(dirPath+"/"+file.getName());
                }else{
                    res = (res & file.delete());
                }
            }
        }

        //delete folder after all contents
        if(res) {
            files=directory.listFiles();
            if (files != null && files.length == 0) {
                if (directory.delete()) {
                    return true;
                }
            }
        }

        return false;
    }



    public String getPath(File file){
        return file.getAbsolutePath();
    }

    public int moveFile(String sourceFileFullPath, String destinationFileFullPath) {
        File sourceFile = new File(sourceFileFullPath);
        File destinationFile = new File(destinationFileFullPath);
        if (!sourceFile.exists()) {
            Log.d(TAG,"Source file does not exist.");
            return -2;
        }

        try {
            boolean success = sourceFile.renameTo(destinationFile);
            if (success) {
                Log.d(TAG,"File moved successfully.");
                return 0;
            } else {
                Log.d(TAG,"Failed to move file.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -3;
    }

    public void moveFile(String sourceFolderPath, String destinationFolderPath, String fileName) {
        File sourceFile = new File(sourceFolderPath, fileName);
        File destinationFolder = new File(destinationFolderPath);
        if (!sourceFile.exists()) {
            Log.d(TAG,"Source file does not exist.");
            return;
        }
        if (!destinationFolder.exists()) {
            destinationFolder.mkdirs();
        }
        File destinationFile = new File(destinationFolder, fileName);

        try {
            boolean success = sourceFile.renameTo(destinationFile);
            if (success) {
                Log.d(TAG,"File moved successfully.");
            } else {
                Log.d(TAG,"Failed to move file.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean copyFile(String sourceFilePath, String destinationFilePath) {
        boolean isSuccess = false;
        try {
            FileInputStream fis = new FileInputStream(new File(sourceFilePath));
            FileOutputStream fos = new FileOutputStream(new File(destinationFilePath));
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                fos.write(buffer, 0, length);
            }
            fis.close();
            fos.close();

            isSuccess = true;
            Log.d(TAG,"File copied successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG,"Failed to copy file.");
        }
        return  isSuccess;
    }

    public boolean moveFolder(String sourceFolderPath, String destinationFolderPath){
        try {
            File sourceFolder = new File(sourceFolderPath);
            File destinationFolder = new File(destinationFolderPath);
            if (!sourceFolder.exists()) {
                Log.d(TAG,"Source folder does not exist.");
                return false;
            }
            if (!destinationFolder.exists()) {
                destinationFolder.mkdirs();
            }
            File[] files = sourceFolder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        moveFolder(file.getAbsolutePath(), destinationFolder + "/" + file.getName());
                    } else {
                        file.renameTo(new File(destinationFolder, file.getName()));
                    }
                }
                sourceFolder.delete();

                Log.d(TAG,"Folder moved successfully.");
            }else{
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG,"Failed to move folder.");
            return false;
        }
        return true;
    }

    public long getFileSize(String subDir,String filename){

        File file;
        if(subDir.equals("")) {
            file = new File(internalStorageDir, filename);
        }else{
            file = new File(internalStorageDir.getAbsolutePath()+"/"+subDir+"/"+filename);
        }
        return file.length();
    }

    public void writeToFile(String subDirName, String filename,String data){
        try {

            File file;
            if(subDirName.equals("")){
                file = new File(internalStorageDir, filename);
            }else {
                File subDir = new File(internalStorageDir, subDirName);
                file = new File(subDir, filename);
            }

            FileOutputStream fos = new FileOutputStream(file);
            fos.write(data.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readFromFile(String filename){
        StringBuilder stringBuilder = new StringBuilder();

        try {
            File file = new File(internalStorageDir, filename);
            FileInputStream fis = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));

            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }

            reader.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String storedData = stringBuilder.toString();
        Log.d(TAG,filename+":"+getFileSize("",filename)+":"+storedData);
    }

    public String readFromTextFile(String subDirName,String filename){
        StringBuilder stringBuilder = new StringBuilder();

        try {

            File file;
            if(subDirName.equals("")) {
                file = new File(internalStorageDir, filename);
            }else{
                File subDir = new File(internalStorageDir,subDirName);
                file = new File(subDir,filename);
            }
            FileInputStream fis = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));

            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }

            reader.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String storedData = stringBuilder.toString();
        Log.d(TAG,filename+":"+getFileSize("",filename)+":"+storedData);
        return storedData;
    }

    public List<File> getAllFilesInInternalStorage() {
        List<File> fileList = new ArrayList<>();
        File[] files = context.getFilesDir().listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    fileList.add(file);
                    Log.d(TAG,"File: "+file.getName());
                    //readFromFile(file.getName());
                }else{
                    Log.d(TAG,"!File: "+file.getName());
                }
            }
        }

        return fileList;
    }
    public List<String> getFolderFilesSpecific(String folderName,boolean isFullPath,String extension){
        List<String> specificFiles = new ArrayList<>();

        File folderDir=new File(internalStorageDir,folderName);
        File[] folderFiles = folderDir.listFiles();

        String folderPath="";
        if(isFullPath) {
            folderPath = internalStorageDir.getAbsolutePath() + "/" + folderName + "/";
        }

        if(folderFiles!=null) {

            for (File file : folderFiles) {
                if(extension.equals("") || file.getName().endsWith(extension)){
                    specificFiles.add(folderPath+file.getName());
                }
            }
        }

        return specificFiles;
    }

    public List<String> getFolderFilesSpecificXF(String fullfolderPath,boolean isFullPath,String extension){
        List<String> specificFiles = new ArrayList<>();

        File folderDir=new File(fullfolderPath);
        File[] folderFiles = folderDir.listFiles();

        String folderPath="";
        if(isFullPath) {
            folderPath = fullfolderPath + "/";
        }

        if(folderFiles!=null) {

            for (File file : folderFiles) {
                if(extension.equals("") || file.getName().endsWith(extension)){
                    specificFiles.add(folderPath+file.getName());
                    //Log.d("IFH","specific file:"+folderPath+file.getName()+" size:"+getInternalFileSize(folderName,file.getName())+" checksum:"+getInternalFileChecksum(folderName,file.getName()));
                }
            }
        }

        return specificFiles;
    }


    public List<String> getAllFiles(String relfolderPath,boolean isFullPath){
        List<String> allFiles = new ArrayList<>();
        allFiles.addAll(getFolderFilesSpecific(relfolderPath,isFullPath,""));
        return allFiles;
    }

    public boolean isExists(String subDir,String fileName){
        File subdirectory = new File(internalStorageDir, subDir);
        File file=new File(subdirectory,fileName);
        return file.exists();
    }
    public boolean isExists(String[] subDirs,String fileName){
        for(int i=0;i<subDirs.length;i++){
            if(isExists(subDirs[i],fileName)){
                return true;
            }
        }
        return false;
    }

    public String getFullPath(String subDir, String fileName){
        if(isExists(subDir,fileName)){
            Log.d(TAG,"fullPath: "+internalStorageDir+"/"+subDir+"/"+fileName);
            return internalStorageDir+"/"+subDir+"/"+fileName;
        }
        return null;
    }

    public String getFullPath(String[] subDir, String fileName){
        for(int i=0;i<subDir.length;i++) {
            if (isExists(subDir[i], fileName)) {
                Log.d(TAG, "fullPath: " + internalStorageDir + "/" + subDir[i] + "/" + fileName);
                return internalStorageDir + "/" + subDir[i] + "/" + fileName;
            }
        }
        return null;
    }


    public String getSubDir(String fileName){
        for(String subDir:directories) {
            if (isExists(subDir, fileName)) {
                return subDir;
            }
        }

        return "";
    }
    public boolean checkSubdirectoryInInternalStorage(String subdirectoryName) {
        try {
            File subdirectory = new File(internalStorageDir, subdirectoryName);
            return subdirectory.exists();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean createSubdirectoryInInternalStorage(String directoryName,String subdirectoryName) {
        try {

            File subdirectory;
            if(directoryName != null && directoryName.equals("")) {
                subdirectory = new File(internalStorageDir, subdirectoryName);
            }else{
                File outerDirectory = new File(internalStorageDir, directoryName);
                subdirectory = new File(outerDirectory, subdirectoryName);
            }
            if (!subdirectory.exists()) {
                if (subdirectory.mkdirs()) {
                    Log.d("IFH","subdirectory created successfully");
                    return true;
                } else {
                    Log.d("IFH","subdirectory creation failed");
                    return false;
                }
            } else {
                Log.d("IFH","subdirectory already exists");
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public long getInternalFileSize(String subDir,String fileName){
        File fileToCheck;
        if(subDir.equals("")){
            fileToCheck = new File(internalStorageDir,fileName);
        }else{
            File subdir=new File(internalStorageDir,subDir);
            fileToCheck=new File(subdir,fileName);
        }
        try{
            return fileToCheck.length();
        }catch (Exception e){
            e.printStackTrace();
        }
        return -1;
    }

    public boolean createRequiredSubDirectory(){
        boolean ans=true;
        ans = ans && createSubdirectoryInInternalStorage("",this.DIR_PROFILE_PRV);
        for(String subDir:directories){
            ans = ans && createSubdirectoryInInternalStorage("",subDir);
        }
        return ans;
    }
    /*
        @usage
        replaced//File internalFile = new File(context.getFilesDir(), "your_internal_file.txt"); // Replace with your file
        long startByte = 100; // Start position
        long endByte = 199;   // End position

     */
    public byte[] readBytesFromRange(String filename, long start, long end) {
        File file=new File(internalStorageDir,filename);
        RandomAccessFile randomAccessFile = null;
        try {
            randomAccessFile = new RandomAccessFile(file, "r");
            randomAccessFile.seek(start); // Move the file pointer to the start position
            int length = (int) (end - start + 1); // Calculate the length of the range
            byte[] bytes = new byte[length];
            randomAccessFile.readFully(bytes); // Read the bytes into the array
            return bytes;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (randomAccessFile != null) {
                try {
                    randomAccessFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     *  Directory specific
     */

    public ArrayList<String> getSubDirInDir(String relativeDirPath){
        String dirFullPath = internalStorageDir.getAbsolutePath()+"/"+relativeDirPath;
        ArrayList<String> allSubDirInDir = new ArrayList<>();

        File dirFile = new File(dirFullPath);
        if(dirFile.exists()){
            File[] dirContent = dirFile.listFiles();
            for(File content:dirContent){
                if(content.isDirectory()){
                    allSubDirInDir.add(content.getName());
                }
            }
        }else{
            return null;
        }

        return allSubDirInDir;
    }

    public void printInternalDirectoryTree(){
        printDirectoryTree(internalStorageDir,1,"",false);
    }
    public ArrayList<Pair<String,Long>> printDirectoryTree(File directory,int depth,String parentDir,boolean isReturnFullPath) {
        ArrayList<Pair<String,Long>> allFilesList = new ArrayList<>();
        // Print the current directory at the specified depth
        String preSpaces = "";
        for (int i = 0; i < depth; i++) {
            //Log.d(TAG,"|   ");
            preSpaces +="|   ";
        }
        Log.d(TAG,preSpaces+"|---" + directory.getName() + "/");

        // Get the list of files and directories in the current directory
        File[] files = directory.listFiles();

        if (files != null) {
            // Print the tree structure for each file or directory
            for (File file : files) {
                if (file.isDirectory()) {
                    ArrayList<Pair<String,Long>> tempFileList = printDirectoryTree(file, depth + 1,parentDir+"/"+file.getName(),isReturnFullPath);
                    allFilesList.addAll(tempFileList);
                } else {
                    printFile(file, depth + 1);
                    allFilesList.add(new Pair<String,Long>((isReturnFullPath?parentDir+"/":"")+file.getName(), file.length())); //parentDir+"/"+
                }
            }
        }
        return allFilesList;
    }
    private void printFile(File file, int depth) {
        // Print the current file at the specified depth
        String preSpaces = "";
        for (int i = 0; i < depth; i++) {
            preSpaces += "|   ";
        }
        Log.d(TAG,preSpaces+"|---" + file.getName()+" "+ OtherUtils.byteToReadableSize(file.length(),"2"));
    }


}

