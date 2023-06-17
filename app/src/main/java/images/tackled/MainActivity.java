package images.tackled;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

public class MainActivity extends AppCompatActivity {
    private static final int Pick_image = 1;
    private Button mbuttonchoose;
    private Button mbuttonupload;
    private TextView mtextviewshow;
    private EditText medittextname;
    private ImageView mimageview;
    private Uri mImageUri;
    private StorageReference mstorageref;
    private DatabaseReference mdatabaseref;
    private ProgressBar mprogressbar;
    private StorageTask mstoragetask;
    private StorageTask<UploadTask.TaskSnapshot> mUploadtask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mbuttonchoose = findViewById(R.id.button_choose);
        mbuttonupload = findViewById(R.id.button_upload);
        mtextviewshow = findViewById(R.id.show_uploads);
        medittextname = findViewById(R.id.edit_text_name);
        mimageview = findViewById(R.id.image_view);
        mprogressbar=findViewById(R.id.progressbar);
        mstorageref= FirebaseStorage.getInstance().getReference("uploads");
        mdatabaseref= FirebaseDatabase.getInstance().getReference("uploads");
        mbuttonchoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();

            }


        });
        mbuttonupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mUploadtask!=null && mUploadtask.isInProgress()){
                    Toast.makeText(MainActivity.this, "Upload in progress ", Toast.LENGTH_SHORT).show();
                }else
                {
                    uploadFile();
                }



            }
        });
        mtextviewshow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openimagesactivity();
            }
        });
    }

    private void openimagesactivity() {
        Intent intent =new Intent(this,ImagesActivity.class);
        startActivity(intent);

    }

    private String getfileextension(Uri uri){
        ContentResolver cr=getContentResolver();
        MimeTypeMap mime=MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));

    }
    private void uploadFile() {
        if(mImageUri!=null){
            StorageReference filereference=mstorageref.child(System.currentTimeMillis()+"."+
                    getfileextension(mImageUri));

            mUploadtask = filereference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mprogressbar.setProgress(0);
                                }
                            }, 500);
                             //new code trial
                            {
                                Toast.makeText(MainActivity.this, "upload successful", Toast.LENGTH_LONG).show();

                                Upload upload = new Upload(medittextname.getText().toString().trim(), taskSnapshot.getStorage()
                                        .getDownloadUrl().toString());
                                String uploadid = mdatabaseref.push().getKey();
                                mdatabaseref.child(uploadid).setValue(upload);
                            }

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                            mprogressbar.setProgress((int) progress);
                        }
                    });

        }else{
            Toast.makeText(this,"No file selected ",Toast.LENGTH_SHORT).show();
        }


    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        galleryActivityResultLauncher.launch(intent);
    }

    private ActivityResultLauncher<Intent> galleryActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        mImageUri=data.getData();
                        mimageview.setImageURI(mImageUri);


                    } else {
                        //cancelled
                        Toast.makeText(MainActivity.this, "Cancelled...", Toast.LENGTH_SHORT).show();
                    }

                }
            }
    );
}

