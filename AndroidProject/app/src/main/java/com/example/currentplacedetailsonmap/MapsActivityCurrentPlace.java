package com.example.currentplacedetailsonmap;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CustomCap;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.Arrays;
import java.util.List;

/**
 * An activity that displays a map showing the place at the device's current location.
 */
public class MapsActivityCurrentPlace extends AppCompatActivity
        implements OnMapReadyCallback {

    private static final String TAG = MapsActivityCurrentPlace.class.getSimpleName();
    private static final int COLOR_BLACK_ARGB = 0xff000000;
    private static final int COLOR_WHITE_ARGB = 0xffffffff;
    private static final int COLOR_GREEN_ARGB = 0xff388E3C;
    private static final int COLOR_PURPLE_ARGB = 0xff81c784;
    private static final int COLOR_RED_ARGB = 0xffff0000;

    private static final int COLOR_NO_WATER = 0xfff8f9fb;

    private static final int POLYGON_STROKE_WIDTH_PX = 8;
    private static final int POLYLINE_STROKE_WIDTH_PX = 20;
    private static final int PATTERN_DASH_LENGTH_PX = 20;
    private static final int PATTERN_GAP_LENGTH_PX = 20;

    private static final PatternItem  DASH = new Dash(PATTERN_DASH_LENGTH_PX);
    private static final PatternItem GAP = new Gap(PATTERN_GAP_LENGTH_PX);
    private GoogleMap mMap;

    /// Markers
    private static final LatLng B = new LatLng( -6.229241,106.610547);
    private static final LatLng F = new LatLng(  -6.228537,106.612613);
    private static final LatLng FJ = new LatLng( -6.228094,106.610855);
    private static final LatLng D = new LatLng( -6.229036, 106.611989);
    private static final LatLng A = new LatLng(-6.228090,106.610668);
    private static final LatLng E = new LatLng( -6.228094,106.610855);
    private static final LatLng Gym = new LatLng( -6.228404,106.611792);
    private static final LatLng Fpark = new LatLng(-6.228498,106.613301);
    private static final LatLng Bpark = new LatLng(-6.230242,106.610362);
    private static final LatLng VIP = new LatLng(-6.227800,106.611549);
    private static final LatLng BBC = new LatLng(-6.228430,106.611490);
    private static final LatLng Fparkcircuit = new LatLng(-6.229436,106.614263);
    private static final LatLng Cpark = new LatLng(-6.229589,106.611805);

    private Marker mB;
    private Marker mF;
    private Marker mFJ;
    private Marker mD;
    private Marker mA;
    private Marker mE;
    private Marker mGym;
    private Marker mFpark;
    private Marker mBpark;
    private Marker mVIP;
    private Marker mBBC;
    private Marker mFparkcircuit;
    private Marker mCpark;

    private CameraPosition mCameraPosition;

    // The entry points to the Places API.
    private GeoDataClient mGeoDataClient;
    private PlaceDetectionClient mPlaceDetectionClient;

    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient mFusedLocationProviderClient;

    // A default location (Sydney, Australia) and default zoom to use when location permission is
    // not granted.
    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final int DEFAULT_ZOOM = 20;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location mLastKnownLocation;

    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    private static final List<PatternItem> PATTERN_POLYGON_ALPHA = Arrays.asList(GAP, DASH);

    // Used for selecting the current place.
    private static final int M_MAX_ENTRIES = 5;
    private String[] mLikelyPlaceNames;
    private String[] mLikelyPlaceAddresses;
    private String[] mLikelyPlaceAttributions;
    private LatLng[] mLikelyPlaceLatLngs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retrieve location and camera position from saved instance state.
        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }

        String from1 = getIntent().getStringExtra("FROM1");
        String from2 = getIntent().getStringExtra("FROM1");
        String destination1 = getIntent().getStringExtra("DESTINATION1");
        String destination2 = getIntent().getStringExtra("DESTINATION1");
//
//        int from_lat = Integer.parseInt(from1);
//
        Log.v(TAG, "Destination " + destination1 );
        Log.d(TAG, "From " + from1 );

        // Retrieve the content view that renders the map.
        setContentView(R.layout.activity_maps);

        // Construct a GeoDataClient.
        mGeoDataClient = Places.getGeoDataClient(this, null);

        // Construct a PlaceDetectionClient.
        mPlaceDetectionClient = Places.getPlaceDetectionClient(this, null);

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Build the map.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    /**
     * Saves the state of the map when the activity is paused.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
            super.onSaveInstanceState(outState);
        }
    }

    /**
     * Sets up the options menu.
     * @param menu The options menu.
     * @return Boolean.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.current_place_menu, menu);
        return true;
    }

    /**
     * Handles a click on the menu option to get a place.
     * @param item The menu item to handle.
     * @return Boolean.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.option_get_place) {
            showCurrentPlace();
        }
        return true;
    }


    /**
     * Manipulates the map when it's available.
     * This callback is triggered when the map is ready to be used.
     */
    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;

        mB = mMap.addMarker(new MarkerOptions()
                .position(B)
                .title("Gedung B")
                .snippet("-School of Design\n" +
                        "-Liberal Art and Social Science\n" +
                        "-Informatics\n" +
                        "-Previously a parking lot"));
        mB.setTag(0);

        mF = mMap.addMarker(new MarkerOptions()
                .position(F)
                .title("Gedung F")
                .snippet("-School of Business"));
        mF.setTag(0);

        mFJ = mMap.addMarker(new MarkerOptions()
                .position(FJ)
                .title("Food Junction")
                .snippet("-UPH Canteen"));
        mFJ.setTag(0);


        mD = mMap.addMarker(new MarkerOptions()
                .position(D)
                .title("Gedung D")
                .snippet("-School of Law\n" +
                        "-School of Hospitality"));
        mD.setTag(0);


        mA = mMap.addMarker(new MarkerOptions()
                .position(A)
                .title("Gedung A")
                .snippet("-Johannes Oentoro Library\n" +
                        "-Grand Chapel"));
        mA.setTag(0);


        mE = mMap.addMarker(new MarkerOptions()
                .position(E)
                .title("Gedung E")
                .snippet("-UPH College\n" +
                        "-Swimming Pool" +
                        "-Shower Room"));
        mE.setTag(0);

        mGym = mMap.addMarker(new MarkerOptions()
                .position(Gym)
                .title("UPH GYM")
                .snippet("-Free Access for students"));
        mGym.setTag(0);

        mVIP = mMap.addMarker(new MarkerOptions()
                .position(VIP)
                .title("VIP Parking Area")
                .snippet("-Up to Rp. 25,000 in a day").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        mVIP.setTag(0);

        mFpark = mMap.addMarker(new MarkerOptions()
                .position(Fpark)
                .title("F Parking Area")
                .snippet("-Up to Rp. 15,000 in a day").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        mFpark.setTag(0);

        mBpark = mMap.addMarker(new MarkerOptions()
                .position(Bpark)
                .title("B Parking Area")
                .snippet("-Up to Rp. 15,000 in a day\n" +
                        "-Motor Parking Area").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        mBpark.setTag(0);

        mCpark = mMap.addMarker(new MarkerOptions()
                .position(Cpark)
                .title("C Parking Area")
                .snippet("-Up to Rp. 15,000 in a day").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        mCpark.setTag(0);
        mBBC = mMap.addMarker(new MarkerOptions()
                .position(BBC)
                .title("Basket Ball Court"));
        mBBC.setTag(0);
        mFparkcircuit = mMap.addMarker(new MarkerOptions()
                .position(Fparkcircuit)
                .title("F Circuit Parking Area")
                .snippet("-Up to Rp. 15,000 in a day").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        mFparkcircuit.setTag(0);





        ///
        //Adding polylines to indicate roads/routes on map
        //Gate and D
        Polyline GateD = map.addPolyline(new PolylineOptions()
                .clickable(true)
                .add(
                        new LatLng(-6.228373,106.610364),
                        new LatLng(-6.228959,106.611867)
                ));

        GateD.setTag("A");
        stylePolyline(GateD);



        //F Gym Pool Gate
        Polyline GateF = map.addPolyline(new PolylineOptions()
                .clickable(true)
                .add(
                        new LatLng(-6.228368,106.612430),
                        new LatLng(-6.228271,106.611820),
                        new LatLng(-6.228823,106.611486)
                ));
        GateF.setTag("A");
        stylePolyline(GateF);



    //FJ to B

        Polyline BFJ = map.addPolyline(new PolylineOptions()
                .clickable(true)
                .add(
                        new LatLng(-6.228417,106.610872),
                        new LatLng(-6.228772,106.610754)
                ));

        BFJ.setTag("A");
        stylePolyline(BFJ);

    //gate to fj
    /*
    Polyline polyline4 = map.addPolyline(new PolylineOptions()
            .clickable(true)
            .add(
                    new LatLng(-6.228373,106.610364),
                    new LatLng(-6.228417,106.610872)
            ));

    polyline4.setTag("A");
    stylePolyline(polyline4);

     */
        Polyline polyline5 = map.addPolyline(new PolylineOptions()
                .clickable(true)
                .add(
                        new LatLng(-6.228373,106.610364),
                        new LatLng(-6.228772,106.610754)
                )
        );

        polyline5.setTag("A");
        stylePolyline(polyline5);


    //Gedung C
        Polyline polyline6 = map.addPolyline(new PolylineOptions()
                .clickable(true)
                .add(
                        new LatLng(-6.228373,106.610364),
                        new LatLng( -6.228232,106.610637)
                )
        );
        polyline6.setTag("A");
        stylePolyline(polyline6);


    //Gate D F
        Polyline polyline7 = map.addPolyline(new PolylineOptions()
                .clickable(true)
                .add(
                        new LatLng(-6.228959,106.611867),
                        new LatLng(-6.229099,106.612303)
                ));

        polyline7.setTag("A");
        stylePolyline(polyline7);



        Polyline CparkRoad = map.addPolyline(new PolylineOptions()
                .clickable(true)
                .add(
                        new LatLng(-6.228941,106.611812),
                        new LatLng(-6.229351,106.611699)
                ));

        CparkRoad.setTag("A");
        stylePolyline(CparkRoad);


        Polyline SoccerfieldRoad = map.addPolyline(new PolylineOptions()
                .clickable(true)
                .add(
                        new LatLng(-6.229113,106.612324),
                        new LatLng(-6.229175,106.612415)
                ));

        SoccerfieldRoad.setTag("A");
        stylePolyline(SoccerfieldRoad);


        Polyline VIPparkingRoad = map.addPolyline(new PolylineOptions()
                .clickable(true)
                .add(
                        new LatLng(-6.228607,106.611053),
                        new LatLng(-6.227759,106.611355)
                ));

        VIPparkingRoad.setTag("A");
        stylePolyline(VIPparkingRoad);

        Polyline FParkRoad = map.addPolyline(new PolylineOptions()
                .clickable(true)
                .add(
                        new LatLng(-6.229250,106.613391),
                        new LatLng(-6.229574,106.614033)
                ));

        FParkRoad.setTag("A");
        stylePolyline(FParkRoad);




        Polyline BParkRoad = map.addPolyline(new PolylineOptions()
                .clickable(true)
                .add(
                        new LatLng(-6.229864, 106.610801),
                        new LatLng(-6.228751,106.611202)
                ));

        BParkRoad.setTag("A");
        stylePolyline(BParkRoad);







//        if (from=="Front Gate"){
//            if (destination=="F"){
//
//            }
//        }



        Polygon SchoolBorder = map.addPolygon(new PolygonOptions()
                .clickable(true)
                .add(
                        new LatLng(-6.227431,106.610088),
                        new LatLng(-6.227361,106.610366),
                        new LatLng (-6.228100,106.612436),
                        new LatLng (-6.228279,106.613268),
                        new LatLng (-6.228431,106.613749),
                        new LatLng (-6.228820,106.614655),
                        new LatLng (-6.229832,106.614182),
                        new LatLng (-6.230977,106.612589),
                        new LatLng (-6.231693,106.613160),
                        new LatLng (-6.232081,106.612857),
                        new LatLng (-6.231415,106.610594),
                        //new
                        new LatLng (-6.231104,106.609691),
                        //new LatLng (-6.230659,106.611038),
                        new LatLng (-6.230309,106.609974),
                        new LatLng (-6.229737,106.610170),
                        new LatLng (-6.229466,106.609224),
                        new LatLng (-6.228941,106.609334)
                ));


        Polygon ground = map.addPolygon(new PolygonOptions()
                .clickable(false)
                .add(
                        new LatLng(-6.229015,106.611178),
                        new LatLng(-6.229068,106.611506),
                        new LatLng ( -6.229810,106.611192),
                        new LatLng (-6.229633,106.610932)
                ));

        ground.setTag("alpha");
        stylePolygon(ground);

        // Store a data object with the polygon, used here to indicate an arbitrary type.
        //polygon1.setTag("alpha");
        //stylePolygon(polygon1);
        // Style the polygon.
        // Use a custom info window adapter to handle multiple lines of text in the
        // info window contents.
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            // Return null here, so that getInfoContents() is called next.
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                // Inflate the layouts for the info window, title and snippet.
                View infoWindow = getLayoutInflater().inflate(R.layout.custom_info_contents,
                        (FrameLayout) findViewById(R.id.map), false);

                TextView title = ((TextView) infoWindow.findViewById(R.id.title));
                title.setText(marker.getTitle());

                TextView snippet = ((TextView) infoWindow.findViewById(R.id.snippet));
                snippet.setText(marker.getSnippet());

                return infoWindow;
            }
        });

        // Prompt the user for permission.
        getLocationPermission();

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();
    }

    private void stylePolyline(Polyline polyline) {
        String type = "";
        if (polyline.getTag() != null) {
            type = polyline.getTag().toString();
        }
        List<PatternItem> pattern = null;
        switch (type){
            case "A":
                polyline.setStartCap(new RoundCap());
                polyline.setWidth(POLYLINE_STROKE_WIDTH_PX);
                polyline.setColor(COLOR_WHITE_ARGB);
                polyline.setJointType(JointType.ROUND);
                break;
        }

        polyline.setEndCap(new RoundCap());
//        polyline.setPattern(PATTERN_POLYGON_ALPHA);
//
//        polyline.setWidth(POLYLINE_STROKE_WIDTH_PX);
//        polyline.setColor(COLOR_RED_ARGB);
//        polyline.setJointType(JointType.ROUND);

        }

    private void stylePolygon(Polygon polygon) {
        String type = "";

        if (polygon.getTag() != null){
            type = polygon.getTag().toString();
        }

        List<PatternItem> pattern = null;
        int strokeColor = COLOR_BLACK_ARGB;
        int fillColor = COLOR_WHITE_ARGB;

        switch (type){
            case "alpha":
                pattern = PATTERN_POLYGON_ALPHA;
                strokeColor = COLOR_NO_WATER;

                fillColor = COLOR_NO_WATER;
//                polygon.setStrokePattern(pattern);
//                polygon.setStrokeWidth(POLYGON_STROKE_WIDTH_PX);
                polygon.setStrokeColor(strokeColor);
                break;

        }

        polygon.setFillColor(fillColor);
    }

    /**
     * Gets the current location of the device, and positions the map's camera.
     */
    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = task.getResult();
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(mLastKnownLocation.getLatitude(),
                                            mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                            if (mLastKnownLocation.getLatitude() <  -6.232244 || mLastKnownLocation.getLatitude() > -6.227259
                            || mLastKnownLocation.getLongitude() < 106.608403 || mLastKnownLocation.getLongitude() > 106.615019)
                            {
                                setContentView(R.layout.outofbounds);
                            }
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }


    /**
     * Prompts the user for permission to use the device location.
     */
    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    /**
     * Handles the result of the request for location permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }

    /**
     * Prompts the user to select the current place from a list of likely places, and shows the
     * current place on the map - provided the user has granted location permission.
     */
    private void showCurrentPlace() {
        if (mMap == null) {
            return;
        }

        if (mLocationPermissionGranted) {
            // Get the likely places - that is, the businesses and other points of interest that
            // are the best match for the device's current location.
            @SuppressWarnings("MissingPermission") final
            Task<PlaceLikelihoodBufferResponse> placeResult =
                    mPlaceDetectionClient.getCurrentPlace(null);
            placeResult.addOnCompleteListener
                    (new OnCompleteListener<PlaceLikelihoodBufferResponse>() {
                        @Override
                        public void onComplete(@NonNull Task<PlaceLikelihoodBufferResponse> task) {
                            if (task.isSuccessful() && task.getResult() != null) {
                                PlaceLikelihoodBufferResponse likelyPlaces = task.getResult();

                                // Set the count, handling cases where less than 5 entries are returned.
                                int count;
                                if (likelyPlaces.getCount() < M_MAX_ENTRIES) {
                                    count = likelyPlaces.getCount();
                                } else {
                                    count = M_MAX_ENTRIES;
                                }

                                int i = 0;
                                mLikelyPlaceNames = new String[count];
                                mLikelyPlaceAddresses = new String[count];
                                mLikelyPlaceAttributions = new String[count];
                                mLikelyPlaceLatLngs = new LatLng[count];

                                for (PlaceLikelihood placeLikelihood : likelyPlaces) {
                                    // Build a list of likely places to show the user.
                                    mLikelyPlaceNames[i] = (String) placeLikelihood.getPlace().getName();
                                    mLikelyPlaceAddresses[i] = (String) placeLikelihood.getPlace()
                                            .getAddress();
                                    mLikelyPlaceAttributions[i] = (String) placeLikelihood.getPlace()
                                            .getAttributions();
                                    mLikelyPlaceLatLngs[i] = placeLikelihood.getPlace().getLatLng();

                                    i++;
                                    if (i > (count - 1)) {
                                        break;
                                    }
                                }

                                // Release the place likelihood buffer, to avoid memory leaks.
                                likelyPlaces.release();

                                // Show a dialog offering the user the list of likely places, and add a
                                // marker at the selected place.
                                openPlacesDialog();

                            } else {
                                Log.e(TAG, "Exception: %s", task.getException());
                            }
                        }
                    });
        } else {
            // The user has not granted permission.
            Log.i(TAG, "The user did not grant location permission.");

            // Add a default marker, because the user hasn't selected a place.
            mMap.addMarker(new MarkerOptions()
                    .title(getString(R.string.default_info_title))
                    .position(mDefaultLocation)
                    .snippet(getString(R.string.default_info_snippet)));

            // Prompt the user for permission.
            getLocationPermission();
        }
    }

    /**
     * Displays a form allowing the user to select a place from a list of likely places.
     */
    private void openPlacesDialog() {
        // Ask the user to choose the place where they are now.
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // The "which" argument contains the position of the selected item.
                LatLng markerLatLng = mLikelyPlaceLatLngs[which];
                String markerSnippet = mLikelyPlaceAddresses[which];
                if (mLikelyPlaceAttributions[which] != null) {
                    markerSnippet = markerSnippet + "\n" + mLikelyPlaceAttributions[which];
                }

                // Add a marker for the selected place, with an info window
                // showing information about that place.
                mMap.addMarker(new MarkerOptions()
                        .title(mLikelyPlaceNames[which])
                        .position(markerLatLng)
                        .snippet(markerSnippet));

                // Position the map's camera at the location of the marker.
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerLatLng,
                        DEFAULT_ZOOM));
            }
        };

        // Display the dialog.
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.pick_place)
                .setItems(mLikelyPlaceNames, listener)
                .show();
    }

    /**
     * Updates the map's UI settings based on whether the user has granted location permission.
     */
    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);  //true
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }
}
