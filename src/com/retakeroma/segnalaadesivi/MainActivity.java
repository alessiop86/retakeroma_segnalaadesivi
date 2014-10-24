package com.retakeroma.segnalaadesivi;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

	private static int TAKE_PICTURE = 1; 
	private Uri imageUri;
	
	private Button button;
	
	private static final String EMAIL_RETAKE ="adesivileaks@gmail.com";
	   
	    
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		//elementi UI
		button			=	(Button)findViewById(R.id.segnala_adesivo_button);
               		
        button.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {				
				takePhotoAndSendEmail();
			}
		});
        
        //
        
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	//Menu actionbar non previsto per ora
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		// Handle action bar item clicks here. The action bar will
//		// automatically handle clicks on the Home/Up button, so long
//		// as you specify a parent activity in AndroidManifest.xml.
//		int id = item.getItemId();
//		if (id == R.id.action_settings) {
//			return true;
//		}
//		return super.onOptionsItemSelected(item);
//	}
//	
	
	private void takePhotoAndSendEmail() {
		
		Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
	    File photo = new File(Environment.getExternalStorageDirectory(),  "Pic.jpg");
	    intent.putExtra(MediaStore.EXTRA_OUTPUT,
	            Uri.fromFile(photo));
	    imageUri = Uri.fromFile(photo);
	    startActivityForResult(intent, TAKE_PICTURE);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    
	    if (requestCode == TAKE_PICTURE) {
	    	
	        if (resultCode == Activity.RESULT_OK) {	        	
	        	
	            Uri selectedImage = imageUri;
	            getContentResolver().notifyChange(selectedImage, null);
	 
	            LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);	 
	            try {
		            Criteria criteria = new Criteria();
		            String provider = service.getBestProvider(criteria, false);
		            Location location = null;
		            location = service.getLastKnownLocation(provider);
	
		            if (location != null) {
		            	
			            /*
			             * Reverse geocoding is long-running and synchronous.
			             * Run it on a background thread.
			             * Pass the current location to the background task.
			             * When the task finishes,
			             * onPostExecute() displays the address.
			             */
			            (new GetAddressTask(this)).execute(location);
		            }
		            else
		            	throw new Exception("Location=null");
	            
	            }
	            catch (Exception e) {
	            	sendMail(getString(R.string.messaggio_errore_recupero_location)+"\n\n");
	            }
	            
	
	        }
	    }
	}
	
	
	void sendMail(String message) {
		
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("message/rfc822");
		
		if (imageUri != null)
			intent.putExtra(android.content.Intent.EXTRA_STREAM, imageUri); 
		
		intent.putExtra(Intent.EXTRA_EMAIL, new String[] { EMAIL_RETAKE });
		intent.putExtra(Intent.EXTRA_SUBJECT, "Segnalazione da app");
		intent.putExtra(Intent.EXTRA_TEXT, message);

		startActivity(Intent.createChooser(intent, "Invia mail"));
	}

}
