package edu.ranken.prsmith.imageupload.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.android.material.snackbar.Snackbar;

import java.io.File;

import edu.ranken.prsmith.imageupload.BuildConfig;
import edu.ranken.prsmith.imageupload.R;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = "ImageUpload";
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_GALLERY = 2;

    // views
    private ImageView uploadImage;
    private ProgressBar uploadProgress;
    private Button uploadButton;

    // data
    private String _outputFile;
    private Bitmap _outputImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // find views
        uploadImage = findViewById(R.id.upload_image);
        uploadProgress = findViewById(R.id.upload_progress);
        uploadButton = findViewById(R.id.upload_button);
    }

    /**
     * Responds to when the user clicks the ImageView.
     *
     * @param view the view that was clicked on
     * @see <a href="https://developer.android.com/training/camera/photobasics">Take photos</a>
     * @see <a href="https://guides.codepath.com/android/Accessing-the-Camera-and-Stored-Media">Accessing the Camera and Stored Media</a>
     * @see <a href="https://stackoverflow.com/questions/56598480/couldnt-find-meta-data-for-provider-with-authority">Couldn't find meta-data for provider with authority</a>
     */
    public void onSelectImage(View view) {
        Intent imageCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        try {
            String outputFilePrefix = "image";
            String outputFileSuffix = ".jpg";
            File storageDir = new File(getFilesDir(), "images/");

            // make directories, if missing
            storageDir.mkdirs();

            // create temporary output file
            File outputFile = File.createTempFile(
                outputFilePrefix,
                outputFileSuffix,
                storageDir
            );

            // save path to output file
            _outputFile = outputFile.getAbsolutePath();
            Log.i(LOG_TAG, "_outputFile = " + _outputFile);

            // convert to content:// uri
            Uri outputUri = FileProvider.getUriForFile(
                this,
                BuildConfig.APPLICATION_ID,
                outputFile
            );

            // add output uri to intent
            imageCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);

            // start the camera app
            startActivityForResult(imageCaptureIntent, REQUEST_IMAGE_CAPTURE);

        } catch (ActivityNotFoundException ex) {
            String error = getString(R.string.error_activity_not_found);
            Log.e(LOG_TAG, error, ex);
            Snackbar.make(uploadImage, error, Snackbar.LENGTH_SHORT).show();
        } catch (Exception ex) {
            String error = getString(R.string.error_create_output_file);
            Log.e(LOG_TAG, error, ex);
            Snackbar.make(uploadImage, error, Snackbar.LENGTH_SHORT).show();
        }
    }

    /**
     * Responds to when the user clicks the Upload Button.
     *
     * @param view the view that was clicked on
     * @see <a href="https://developer.android.com/topic/libraries/architecture/workmanager">Schedule tasks with WorkManager</a>
     */
    public void onUploadImage(View view) {
    }

    /**
     * Responds to the user taking a picture or picking one from their gallery.
     *
     * @param requestCode the request code given to startActivityForResult
     * @param resultCode the returned result code
     * @param data the returned data
     * @see <a href="https://developer.android.com/training/camera/photobasics">Take photos</a>
     * @see <a href="https://guides.codepath.com/android/Accessing-the-Camera-and-Stored-Media">Accessing the Camera and Stored Media</a>
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK) {
                _outputImage = BitmapFactory.decodeFile(_outputFile);
                uploadImage.setImageBitmap(_outputImage);
            } else {
                _outputFile = null;
                _outputImage = null;
                uploadImage.setImageResource(R.drawable.ic_image_search);
            }
        }
        if (requestCode == REQUEST_IMAGE_GALLERY) {
            if (resultCode == RESULT_OK) {
                // TODO
            }
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putString("_outputFile", _outputFile);
        bundle.putParcelable("_outputImage", _outputImage);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle bundle) {
        super.onRestoreInstanceState(bundle);
        _outputFile = bundle.getString("_outputFile");
        _outputImage = bundle.getParcelable("_outputImage");
        if (_outputImage != null) {
            uploadImage.setImageBitmap(_outputImage);
        } else {
            uploadImage.setImageResource(R.drawable.ic_image_search);
        }
    }
}
