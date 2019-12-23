package Mohamed.mad.markmycar;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.location.Location;
import android.os.Handler;
import androidx.annotation.NonNull;
import com.google.android.material.tabs.TabLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class MainActivity extends AppCompatActivity implements TitleDialog.TitleDialogListener{
    private Toolbar toolbar;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private TabLayout tableLayout;
    private DatabaseHelper databaseHelper;
    private Dialog markedDialog;
    private FusedLocationProviderClient fusedLocationClient;
    private ProgressDialog  progressBar;
    private SearchView searchView;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;

    private ListView listView;
    private boolean mrequestingLocationUpdates;
    @Override
    public void applyText(String title) {

        if(title.isEmpty()){
            Toast.makeText(this,"Check the Fields!!",Toast.LENGTH_LONG).show();
            MarkFragment.marker.setAlpha(1f);
            return;
        }
          //Toast.makeText(this,title,Toast.LENGTH_LONG).show();
        //This gets the text from the fragement
        //then you can get the location and perform SQL insertion
        Log.i("Loc","Water is not there " + title);
        //Insert(title,getDate(),"121323","32434",DatabaseHelper.OPEN);
       // MarkFragment.marker.setAlpha(1f);
        //call location function , pass the title.
        //gets the location of the user
        mrequestingLocationUpdates = true;
        progressBar.show();
        getLocation(title);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.toolbar_men,menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);     //  Fixed Portrait orientation
        setContentView(R.layout.activity_main);
        databaseHelper = new DatabaseHelper(this);
        tableLayout = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.viewPager);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        toolbar = findViewById(R.id.tooldBar);
        setSupportActionBar(toolbar);
        tableLayout.setupWithViewPager(viewPager);
        mrequestingLocationUpdates = false;
        //Title of each fragment
        tableLayout.getTabAt(0).setText("Pin");
        tableLayout.getTabAt(1).setText("Pinned");
        tableLayout.getTabAt(0).setIcon(R.drawable.ic_add_location_black_24dp);
        tableLayout.getTabAt(1).setIcon(R.drawable.ic_location_on_black_24dp);
       // tableLayout.getTabAt(2).setText("Share");
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        progressBar = new ProgressDialog (this);
        progressBar.setMessage("Pinning the location!");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setIndeterminate(true);
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        //tableLayout.getTabAt(1).getCustomView().findViewById()
     //   listView = findViewById(R.id.location_list);
        //listView = findViewById(R.id.location_list);

       /* locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    // Update UI with location data
                    // ...
                }
            }
         };*/
    }
    public String getDate(){

         String date = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
        return date;
    }
    public void Insert(String title, String date, String longitude, String latitude, String status){
            boolean test = databaseHelper.insert(title,date,longitude,latitude,status);
            if(test){
               // Toast.makeText(this,"Succesfull inserted",Toast.LENGTH_LONG).show();
                marked();
                markedDialog.show();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        // yourMethod();
                        markedDialog.cancel();
                        MarkFragment.marker.setAlpha(1f);
                    }
                }, 1000);
                //markedDialog.cancel();
                appRefresh();

            }else{
                Toast.makeText(this,"Something Happend!",Toast.LENGTH_LONG).show();
            }
    }

    //options clicked
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.eraseHistory){
            //perform delete on the database
            //check if the database if already empty

            if(!databaseIsEmpty()){
                //deletes all of the rows
                databaseHelper.deleteAllRows();
               // listView.clearTextFilter();
                //listView.getAdapter().
                 appRefresh();

                Toast.makeText(this," Pins are cleared!",Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(this,"There are no Pins!",Toast.LENGTH_LONG).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }
    private boolean databaseIsEmpty(){
        Cursor cursor = databaseHelper.getData();
        int count = 0;
        while (cursor.moveToNext()){
                count = count + 1;
        }
        if(count == 0) return true;
         return false;
    }
    private void appRefresh(){
        Intent i = getBaseContext().getPackageManager()
                .getLaunchIntentForPackage( getBaseContext().getPackageName() );
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }
    private void marked(){
        markedDialog = new Dialog(MainActivity.this);
        markedDialog.setContentView(R.layout.marked_dialog);
        markedDialog.setTitle("Success!");
    }

    @Override
    protected void onPause() {
        super.onPause();
    //    stopLocationUpdates();
     //   mrequestingLocationUpdates = false;
    }
    private void stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }

    private void checkPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            //request permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1000);
        }

    }
    /*public void gotNext(String s){
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("Data", s);
        startActivity(intent);
    }*/
    private void getLocation(final String title){
        Log.i("TAG","Getting the users location");
        checkPermission();
        //createLocationRequest();
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                //this gets location of the user
                Log.i("TAG","Location : Lat: " + location.getLongitude() + ", Long : " + location.getLatitude() );
                Insert(title ,getDate(),Double.toString(location.getLongitude()),Double.toString(location.getLatitude()),DatabaseHelper.OPEN);
                MarkFragment.marker.setAlpha(1f);
                progressBar.dismiss();
            }
        });/*
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    // Update UI with location data
                    // ...
                    Insert(title ,getDate(),Double.toString(location.getLongitude()),Double.toString(location.getLatitude()),DatabaseHelper.OPEN);
                    MarkFragment.marker.setAlpha(1f);
                    progressBar.dismiss();
                    Log.i("TAG","Location : Lat: " + location.getLongitude() + ", Long : " + location.getLatitude() );
                }
            }
        };*/
    }
    protected void createLocationRequest() {


        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                // All location settings are satisfied. The client can initialize
                // location requests here.
                // ...\
                locationRequest = LocationRequest.create();
                locationRequest.setInterval(10000);
                locationRequest.setFastestInterval(5000);
                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                Log.i("TAG","Location listen sucess *******************");
            }
        });

        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(MainActivity.this,
                                0);
                    } catch (IntentSender.SendIntentException sendEx) {
                        // Ignore the error.
                    }
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //startLocationUpdates();
      /*  if (mrequestingLocationUpdates) {

            startLocationUpdates();
            Log.i("TAG","*********GETING THE LOCARION" );
        }*/
    }

    private void startLocationUpdates() {
        checkPermission();
        fusedLocationClient.requestLocationUpdates(locationRequest,
                locationCallback,
                null /* Looper */);
    }

}
