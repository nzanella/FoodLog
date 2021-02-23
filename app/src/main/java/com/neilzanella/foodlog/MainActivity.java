package com.neilzanella.foodlog;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.net.*;
import android.os.*;
import android.provider.*;
import android.widget.*;
import java.io.*;
import java.text.*;
import java.util.*;

public class MainActivity extends Activity 
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		dispatchTakePictureIntent();
    }
	// request codes
	
	static final int REQUEST_IMAGE_CAPTURE = 1;
/*
	private void dispatchTakePictureIntent() {
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		try {
			startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
		} catch (ActivityNotFoundException e) {
			// display error state to the user
		}
	}
*/
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
			Bundle extras = data.getExtras();
			Bitmap imageBitmap = (Bitmap) extras.get("data");
			ImageView picImageView = findViewById(R.id.picImageView);
			picImageView.setImageBitmap(imageBitmap);
		}
	}
	
	String currentPhotoPath;

	private File createImageFile() throws IOException {
		// Create an image file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String imageFileName = "JPEG_" + timeStamp + "_";
		File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
		File image = File.createTempFile(
			imageFileName,  /* prefix */
			".jpg",         /* suffix */
			storageDir      /* directory */
		);

		// Save a file: path for use with ACTION_VIEW intents
		currentPhotoPath = image.getAbsolutePath();
		return image;
	}
	
	private void dispatchTakePictureIntent() {
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// Ensure that there's a camera activity to handle the intent
		if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
			// Create the File where the photo should go
			File photoFile = null;
			try {
				photoFile = createImageFile();
			} catch (IOException ex) {
				// Error occurred while creating the File
				//...
			}
			// Continue only if the File was successfully created
			if (photoFile != null) {
				Uri photoURI = FileProvider.getUriForFile(this,
														  "com.example.android.fileprovider",
														  photoFile);
					takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
				startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
			}
		}
	}
}
