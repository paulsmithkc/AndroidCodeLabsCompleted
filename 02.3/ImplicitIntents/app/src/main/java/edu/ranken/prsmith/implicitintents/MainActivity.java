package edu.ranken.prsmith.implicitintents;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private EditText urlEdit;
    private EditText locationEdit;
    private EditText shareEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        urlEdit = findViewById(R.id.url_edit);
        locationEdit = findViewById(R.id.location_edit);
        shareEdit = findViewById(R.id.share_edit);
    }

    public void openWebsite(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(urlEdit.getText().toString()));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(this, "Browser not found.", Toast.LENGTH_SHORT).show();
        }
    }

    public void openLocation(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("geo:0,0?q=" + locationEdit.getText().toString()));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(this, "Map app not found.", Toast.LENGTH_SHORT).show();
        }
    }

    public void share(View view) {
        String txt = shareEdit.getText().toString();
        ShareCompat.IntentBuilder
            .from(this)
            .setType("text/plain")
            .setChooserTitle("Share this text with: ")
            .setText(txt)
            .startChooser();
    }
}
