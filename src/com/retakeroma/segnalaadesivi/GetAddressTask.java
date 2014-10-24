package com.retakeroma.segnalaadesivi;

import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.view.View;

/**
 * A subclass of AsyncTask that calls getFromLocation() in the background. The
 * class definition has these generic types: Location - A Location object
 * containing the current location. Void - indicates that progress units are not
 * used String - An address passed to onPostExecute()
 */
public class GetAddressTask extends AsyncTask<Location, Void, String> {
	MainActivity mContext;

	public GetAddressTask(Context context) {
		super();
		mContext = (MainActivity) context;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		mContext.findViewById(R.id.frame_progressbar).setVisibility(View.VISIBLE);
	}
	
	
	@Override
	protected String doInBackground(Location... params) {
		
		
		
		String returnValue = "";
		
		Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
		// Get the current location from the input parameter list
		Location loc = params[0];
		// Create a list to contain the result address
		List<Address> addresses = null;
		try {
			/*
			 * Return 1 address.
			 */
			addresses = geocoder.getFromLocation(loc.getLatitude(),
					loc.getLongitude(), 1);
		} catch (Exception e) {
			return mContext.getString(R.string.messaggio_errore_recupero_location);
		}
		
		// If the reverse geocode returned an address
		if (addresses != null && addresses.size() > 0) {
			// Get the first address
			Address address = addresses.get(0);
			/*
			 * Format the first line of address (if available), city, and
			 * country name.
			 */
			String addressText = String.format(
					"%s, %s, %s",
					// If there's a street address, add it
					address.getMaxAddressLineIndex() > 0 ? address
							.getAddressLine(0) : "",
					// Locality is usually a city
					address.getLocality(),
					// The country of the address
					address.getCountryName());
			// Return the text
			returnValue = "Indirizzo trovato (controlla sia giusto):\n\n" + addressText;
		} else {
			 returnValue = "Indirizzo non trovato, coordinate Lat./Long. (" + Double.toString(loc.getLatitude()) + " , " + Double.toString(loc.getLongitude()) +"),\n\n se vuoi scrivi l'indirizzo manualmente qui sotto:\n\n";
		}
		
		
		return returnValue; 
	}
	
	  protected void onPostExecute(String result) {
		  
		mContext.findViewById(R.id.frame_progressbar).setVisibility(View.GONE);		 
		mContext.sendMail(result);
      }
	  
	  
	

}