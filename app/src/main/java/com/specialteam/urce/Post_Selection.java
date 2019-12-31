package com.specialteam.urce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;

public class Post_Selection extends AppCompatActivity {

    final int PICK_IMAGE = 1;
    final int RESULT_CODE = -1;

    Uri imageUri;
    String compr;

    String id;
    int ID;

    boolean storagePermissionGranted = false;

    final private int Storage_Permission_Requst_Code = 109;

    EditText tag;

    Intent intentForReceive;
    String action;
    String type;

    String AppPreference = "URCE_Preference";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post__selection);

        tag = findViewById(R.id.commentTag);

        intentForReceive = getIntent();
        action = intentForReceive.getAction();
        type = intentForReceive.getType();

        if(storagePermissionGranted) {
            if (Intent.ACTION_SEND.equals(action) && type != null) {

                if(type.startsWith("image/")){
                        handleIntentSend(intentForReceive);
                }
                else{
                    Toast.makeText(getApplicationContext(),"Unsupported format.",Toast.LENGTH_SHORT);
                }

            } else {
                from_explorer();
            }
        }
        else
            storagePermission();
    }

    public void handleIntentSend(Intent data){
        System.out.println("dddd");
        Uri temp_imageUri = data.getParcelableExtra(Intent.EXTRA_STREAM);

        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),temp_imageUri);
        File dir = new File(Environment.getExternalStorageDirectory().getPath()+"/URCE/");
        if(!dir.exists())
            dir.mkdir();
        File imageFile = new File(dir,"temp.png");
        if(!imageFile.exists())
            imageFile.createNewFile();
        else{
            imageFile.delete();
            imageFile.createNewFile();
        }
        OutputStream fout = new FileOutputStream(imageFile);
        bitmap.compress(Bitmap.CompressFormat.PNG, 75, fout);
        fout.flush();
        fout.close();
        imageUri = Uri.fromFile(imageFile);
    } catch (IOException e) {
        e.printStackTrace();
    }

        if(imageUri!=null){

            String compressed_path = compressImage(imageUri.getPath());
            System.out.println(compressed_path);
            compr = compressed_path;
            upload(compressed_path);
        }
    }

    public void storagePermission(){
        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED &&

            ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED){
            storagePermissionGranted = true;
            if (Intent.ACTION_SEND.equals(action) && type != null) {

                if(type.startsWith("image/")){
                    handleIntentSend(intentForReceive);
                }
                else{
                    Toast.makeText(getApplicationContext(),"Unsupported format.",Toast.LENGTH_SHORT);
                }

            } else {
                from_explorer();
            }
        }
        else{
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    Storage_Permission_Requst_Code);
            storagePermissionGranted = false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults){
        storagePermissionGranted = false;
        switch (requestCode) {
            case Storage_Permission_Requst_Code: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    storagePermissionGranted = true;
                    if (Intent.ACTION_SEND.equals(action) && type != null) {

                        if(type.startsWith("image/")){
                            handleIntentSend(intentForReceive);
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"Unsupported format.",Toast.LENGTH_SHORT);
                        }

                    } else {
                        from_explorer();
                    }
                }
                else{
                    finish();
                }
            }
        }
    }

    void from_explorer(){
        Intent img_intent = new Intent(Intent.ACTION_PICK);
        img_intent.setType("image/*");
        img_intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(img_intent,PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==PICK_IMAGE && resultCode==RESULT_CODE && data!=null && data.getData()!=null){
            imageUri = data.getData();

            if(imageUri!=null){

                String compressed_path = compressImage(null);
                System.out.println(compressed_path);
                compr = compressed_path;
                upload(compressed_path);
            }
        }
        else if(requestCode==PICK_IMAGE && data==null){
            finish();
        }
    }

    void upload(String path){
        File file = new File(path);
        System.out.println(file.length());
        Bitmap bitmap;
        if(file.exists()){
            bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            ImageView imageView = findViewById(R.id.post);
            imageView.setImageBitmap(bitmap);
        }
    }

    public void button(View view){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("IDs").child("Container").child("num");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                id = dataSnapshot.getValue().toString();
                ID = Integer.parseInt(id) + 1;
                id = id.toString();
                id = "cont" + ID;
                DatabaseReference reff = FirebaseDatabase.getInstance().getReference().child("IDs").child("Container").child("num");
                reff.setValue(ID);
                System.out.println(id);
                callFuntion();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    private void callFuntion() {
        final StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(id);

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uplodaing....");
        progressDialog.show();

        Bitmap bitmap = BitmapFactory.decodeFile(compr);
        ImageView imageView = findViewById(R.id.post);
        imageView.setImageBitmap(bitmap);

        final Intent intent = new Intent(Post_Selection.this,Home.class);

        storageReference.putFile(Uri.fromFile(new File(compr)))
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.dismiss();
                        System.out.println("ffddd");
                        Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                        String n=null;
                        uri.addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                System.out.println(uri.toString());
                                System.out.println("com"+ID);

                                DatabaseReference reff = FirebaseDatabase.getInstance().getReference();
                                String comID = "com"+ID;
                                SharedPreferences prefs = getSharedPreferences(AppPreference,MODE_PRIVATE);
                                String CName = prefs.getString("Name",null);
                                String url = uri.toString();
                                String tagged = tag.getText().toString();


                                reff.child("Container").child(id).child("CName").setValue(CName);
                                reff.child("Container").child(id).child("PhotoID").setValue(url);
                                reff.child("Container").child(id).child("Liked").setValue("0");
                                reff.child("Container").child(id).child("ID").setValue(id);
                                if(!tagged.equals("")||tagged!=null)
                                    reff.child("Container").child(id).child("Desc").setValue(tagged);
                                else
                                    reff.child("Container").child(id).child("Desc").setValue("null");

                                //To enable comments uncomment this.
                                /*reff.child("Container").child(id).child("CommentID").setValue(comID);
                                reff.child("IDs").child("Comments").child(comID).setValue(0);
                                reff.child("Comments").child(comID).setValue(0);*/
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                        });

                        Toast.makeText(Post_Selection.this,"Uploaded",Toast.LENGTH_SHORT);
                        //startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(Post_Selection.this,"Failed",Toast.LENGTH_SHORT);
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                .getTotalByteCount());
                        progressDialog.setMessage("Uploaded "+(int)progress+"%");
                    }
                });
    }

    public String compressImage(String filePath) {

        if(filePath==null)
            filePath = getPathAPI19(imageUri);
        System.out.println(filePath);
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        String filename = getFilename();
        try {
            out = new FileOutputStream(filename);

//          write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filename;

    }

    public String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "/URCE/Uploads");
        System.out.println(file.getAbsolutePath());
        if (!file.exists()) {
            System.out.println(file.getAbsolutePath());
            file.mkdirs();
            System.out.println("dd");
        }
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        return uriSting;

    }

    private String getRealPathFromURI(Uri contentUri) {
        //Uri contentUri = Uri.parse(contentURI);
        String rwes = null;
        Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            if(cursor.moveToFirst()){
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            rwes = cursor.getString(index);
            }
        }
        return rwes;
    }

    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
            System.out.println(res);
        }
        cursor.close();
        return res;
    }

    public String getpathoreo(Uri contentUri){

        return "";
    }

    public String getPathAPI19(Uri contentUri){
        String path = null;
        String fileid = DocumentsContract.getDocumentId(contentUri);

        String id = fileid.split(":")[1];
        String[] column = {MediaStore.Images.Media.DATA};
        String selector = MediaStore.Images.Media._ID + "=?";
        Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,column,selector,new String[]{id},null);
        if(cursor.moveToFirst()){
            int index = cursor.getColumnIndex(column[0]);
            path = cursor.getString(index);
        }
        return path;

    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

    public void ale(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.progressing,null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.setCancelable(false);
    }

    public void im(View view){
        System.out.println("Text");
    }

    public void te(View view){
        System.out.println("img");
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(Post_Selection.this,Home.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
