package com.gradproj.medassistant.medicalassistant;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.internal.NavigationMenuItemView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.gradproj.medassistant.medicalassistant.AlcoholLevelReading.AlcoholLevelReading;
import com.gradproj.medassistant.medicalassistant.BodyPositionReading.BodyPositionReading;
import com.gradproj.medassistant.medicalassistant.ECGReading.ECGReading;
import com.gradproj.medassistant.medicalassistant.Enums.SensorType;
import com.gradproj.medassistant.medicalassistant.GSRReading.GSRReading;
import com.gradproj.medassistant.medicalassistant.HeartRateReading.HeartRateReading;
import com.gradproj.medassistant.medicalassistant.SPOReading.SPOReading;
import com.gradproj.medassistant.medicalassistant.TempratureReading.TemperatureReading;
import com.gradproj.medassistant.medicalassistant.database.DBAdapter;
import com.gradproj.medassistant.medicalassistant.service.BlueToothService;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import static com.gradproj.medassistant.medicalassistant.Enums.SensorType.HEART_RATE_SENSOR;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        HomeFragment.OnFragmentInteractionListener,
        SettingsFragment.OnFragmentInteractionListener,
        LiveTempratureFragment.OnFragmentInteractionListener,
        LiveGSRFragment.OnFragmentInteractionListener,
        LiveHeartRateFragment.OnFragmentInteractionListener,
        LiveAlcoholFragment.OnFragmentInteractionListener,
        LiveSPOFragment.OnFragmentInteractionListener,
        LiveBodyPositionFragment.OnFragmentInteractionListener,
        SymptomsFragment.OnFragmentInteractionListener,
        AboutUsFragment.OnFragmentInteractionListener,
        HelpFragment.OnFragmentInteractionListener
        {

            private static final String TAG = "MAIN_ACTIVITY" ;
            DBAdapter mDbAdapter;

            String fileName = "patient_report.pdf";
            private BluetoothAdapter bluetoothadapter;
            private Intent bluetoothIntent;
            private User loggedInUser;

            private void creatGraph(){
                GraphView graph = (GraphView) findViewById(R.id.graph);
                LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[] {
                        new DataPoint(0, 1),
                        new DataPoint(1, 5),
                        new DataPoint(2, 3),
                        new DataPoint(3, 2),
                        new DataPoint(4, 6)
                });
                graph.addSeries(series);
            }
            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);
                creatGraph();
                String currentUser = getIntent().getStringExtra("userName");
                if(currentUser!=null)
                {
                    List<User> users = User.getUsersList();
                    for (User myUser:users) {
                        if(myUser.getUserName().equals(currentUser))
                        {
                            loggedInUser = myUser;
                            break;
                        }
                    }
                }else
                {
                    NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

                    // get menu from navigationView
                    Menu menu = navigationView.getMenu();
                    MenuItem logoutItem = menu.findItem(R.id.nav_logout);
                    logoutItem.setTitle("Login");
                }
                mDbAdapter = new DBAdapter(this);
                mDbAdapter.open();


                Toolbar toolbar = findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);

                FloatingActionButton fab = findViewById(R.id.fab);
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            createAndDisplayPdf();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                        this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
                drawer.addDrawerListener(toggle);
                toggle.syncState();

                NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                navigationView.setNavigationItemSelectedListener(this);

//                Intent intent = new Intent(this, NotificationService.class);


                //bluetoothEnable();
//                Intent i = new Intent(getApplicationContext(),BluetoothMockService.class);
                Intent i = new Intent(getApplicationContext(),BlueToothService.class);
                startService(i);
                Fragment fragment = null;
                Class fragmentClass = HomeFragment.class;
                try {
                    fragment = (Fragment) fragmentClass.newInstance();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
            }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;
        Class fragmentClass = HomeFragment.class;
        boolean doNothing = false;
        if (id == R.id.nav_anxiety) {
            fragmentClass = LiveGSRFragment.class;
        } else if (id == R.id.nav_blood_oxygen) {
            fragmentClass = LiveSPOFragment.class;
        } else if (id == R.id.nav_body_position) {
            fragmentClass=LiveBodyPositionFragment.class;
        } else if (id == R.id.nav_heart_beat) {
            fragmentClass = LiveHeartRateFragment.class;
        } else if (id == R.id.nav_temperature) {
            fragmentClass = LiveTempratureFragment.class;
        } else if (id == R.id.nav_alcohol_id) {
            fragmentClass = LiveAlcoholFragment.class;
        } else if (id == R.id.nav_ECG) {
            fragmentClass = ECGChartFragment.class;
        }else if(id == R.id.nav_home)
        {
            fragmentClass = HomeFragment.class;
        }else if(id == R.id.nav_help)
        {
            fragmentClass = HelpFragment.class;
        }else if(id == R.id.nav_about_us)
        {
            fragmentClass = AboutUsFragment.class;
        }
        else if(id == R.id.nav_settings)
        {
            fragmentClass = SettingsFragment.class;
        }else if(id==R.id.nav_logout)
        {
            Intent i = new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(i);
            finish();
            return false;
        }
//        else if (id == R.id.nav_send) {
//             doNothing = true;
//        }


        if(!doNothing)
        {
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }
        return true;
    }






    @Override
    protected void onPause() {
        super.onPause();
//        btHelper.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        btHelper.resume();
    }


    // Method for creating a pdf file from text, saving it then opening it for display
    public void createAndDisplayPdf() throws FileNotFoundException, DocumentException {

        if(loggedInUser==null)
        {
            Toast.makeText(getApplicationContext(),"You need to login first",Toast.LENGTH_LONG).show();
            return;
        }
        if(!isStoragePermissionGranted()){
            return;
        }

        Document doc = new Document();

        try {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Dir";

            File dir = new File(path);
            if(!dir.exists())
            {
                dir.mkdirs();
            }

            File file = new File(dir, fileName);
            boolean deleted = file.delete();
            FileOutputStream fOut = new FileOutputStream(file);

            PdfWriter.getInstance(doc, fOut);

            //open the document
            doc.open();
            createPDFHeader(doc);
            createPDFTable(doc,SensorType.TEMPERATURE_SENSOR,"Temperature Readings","Temperature Sensor");
            createPDFTable(doc, HEART_RATE_SENSOR,"Heart Rate Readings","Heart Rate Sensor");
            createPDFTable(doc,SensorType.GSR_SENSOR,"GSR Readings","GSR Sensor");
            createPDFTable(doc,SensorType.ALCOHOL,"Alcohol Level Readings","Alcohol Sensor");
            createPDFTable(doc,SensorType.SPO2_SENSOR,"SPO2 Readings","SPO2 Sensor");
            createPDFTable(doc,SensorType.BODY_POSITION_SENSOR,"Body Position Readings","Body Position Sensor");
            createPDFTable(doc,SensorType.ECG_SENSOR,"ECG Readings","ECG Sensor");


        } catch (DocumentException de) {
            Log.e("PDFCreator", "DocumentException:" + de);
        } catch (IOException e) {
            Log.e("PDFCreator", "ioException:" + e);
        }
        finally {
            doc.close();
        }

        viewPdf(fileName, "Dir");
    }

    private void createPDFHeader(Document doc) throws IOException, DocumentException {
        // get input stream
        InputStream ims = getAssets().open("icon.jpg");
        Bitmap bmp = BitmapFactory.decodeStream(ims);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        Image image = Image.getInstance(stream.toByteArray());
        doc.add(image);
        Paragraph p1 = new Paragraph("Patient Details :");
        p1.add(Chunk.NEWLINE);
//        p1.add(Chunk.NEWLINE);
        p1.add("Patient Name :"+loggedInUser.getName());
        p1.add(Chunk.NEWLINE);
        p1.add("Age :"+loggedInUser.getAge()+" years");
        p1.add(Chunk.NEWLINE);
        p1.add("Sex :"+loggedInUser.getSex());
        p1.add(Chunk.NEWLINE);

        p1.add("Code :"+loggedInUser.getCode());
        p1.add(Chunk.NEWLINE);
//        p1.add(Chunk.NEWLINE);
        p1.add("Ref. by Dr. :"+loggedInUser.getRefDr());
        p1.add(Chunk.NEWLINE);
        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        p1.add("Date :"+currentDateTimeString);
        p1.add(Chunk.NEWLINE);
        p1.add(Chunk.NEWLINE);
        p1.add("Current Values:");
        p1.add(Chunk.NEWLINE);
        p1.add(Chunk.NEWLINE);
        Font paraFont= new Font(Font.FontFamily.COURIER);
        p1.setFont(paraFont);
        PdfPTable myTable = new PdfPTable(3);
        PdfPCell cell1 = new PdfPCell(new Paragraph("Test"));
        PdfPCell cell2 = new PdfPCell(new Paragraph("Observed Value"));
        PdfPCell cell3 = new PdfPCell(new Paragraph("Reference Range"));

        myTable.addCell(cell1);
        myTable.addCell(cell2);
        myTable.addCell(cell3);

        getLastReading(SensorType.TEMPERATURE_SENSOR,myTable);
        getLastReading(SensorType.HEART_RATE_SENSOR,myTable);
        getLastReading(SensorType.SPO2_SENSOR,myTable);
        getLastReading(SensorType.GSR_SENSOR,myTable);
        getLastReading(SensorType.ALCOHOL,myTable);

        doc.add(p1);
        doc.add(myTable);
        doc.add(Chunk.NEXTPAGE);

    }

     private void getLastReading(SensorType sensorType, PdfPTable myTable) {


         Cursor result = mDbAdapter.fetchSensorLastReading(sensorType);
         String reading = "---";
         if (result.moveToFirst()) {
             reading = result.getString(result.getColumnIndex(DBAdapter.KEY_READING_VALUE));

         }
         PdfPCell cell2 = new PdfPCell(new Paragraph(reading));
         Paragraph referenceRange = new Paragraph();
         referenceRange.add(Chunk.NEWLINE);

         Paragraph testName = new Paragraph();

         switch (sensorType) {
             case HEART_RATE_SENSOR :
                 referenceRange.add("Negative:<=60");
                 referenceRange.add(Chunk.NEWLINE);
                 referenceRange.add("Equivocal:61-100");
                 referenceRange.add(Chunk.NEWLINE);
                 referenceRange.add("Positive:>=101");
                 testName.add("Heart Rate");
                     break;
             case TEMPERATURE_SENSOR:
                 referenceRange.add("Negative:<=36°");
                 referenceRange.add(Chunk.NEWLINE);
                 referenceRange.add("Equivocal:36.1°-37.2°");
                 referenceRange.add(Chunk.NEWLINE);
                 referenceRange.add("Positive:>=37.3°");
                 testName.add("Temprature");
                 break;
             case SPO2_SENSOR:
                 referenceRange.add("Negative:<=89 mmHg");
                 referenceRange.add(Chunk.NEWLINE);
                 referenceRange.add("Equivocal:90-100 mmHg");
                 referenceRange.add(Chunk.NEWLINE);
                 referenceRange.add("Positive:>=101 mmHg");
                 testName.add("SPO2");
                 break;
//             case GLUCOSE_SENSOR:
//                 referenceRange.add("Negative:<=69 mg/dl");
//                 referenceRange.add(Chunk.NEWLINE);
//                 referenceRange.add("Equivocal:70-100 mg/dl");
//                 referenceRange.add(Chunk.NEWLINE);
//                 referenceRange.add("Positive:>=101 mg/dl");
//             testName.add("Glucose");
//                 break;
             case GSR_SENSOR:
                 referenceRange.add("Equivocal: <=279");
                 referenceRange.add(Chunk.NEWLINE);
                 referenceRange.add("Spasm of nerves:>=280");
                 testName.add("GSR");
                 break;
             case ALCOHOL:
                 referenceRange.add("Sober: <200 mg/l");
                 referenceRange.add(Chunk.NEWLINE);
                 referenceRange.add("Drunk: >201 mg/l");
                 testName.add("Alcohol");
                 break;
             case BODY_POSITION_SENSOR:
                 referenceRange.add("Prone Position");
                 referenceRange.add(Chunk.NEWLINE);
                 referenceRange.add("Left Lateral Position");
                 referenceRange.add(Chunk.NEWLINE);
                 referenceRange.add("Right Lateral Position");
                 referenceRange.add(Chunk.NEWLINE);
                 referenceRange.add("Supine Position");
                 referenceRange.add(Chunk.NEWLINE);
                 referenceRange.add("Stand or sit Position");
                 testName.add("Patient Position");
                 break;
//             case ECG_SENSOR:
//                 referenceRange.add("Prone Position");
//                 referenceRange.add(Chunk.NEWLINE);
//                 referenceRange.add("Left Lateral Position");
//                 referenceRange.add(Chunk.NEWLINE);
//                 referenceRange.add("Right Lateral Position");
//                 referenceRange.add(Chunk.NEWLINE);
//                 referenceRange.add("Supine Position");
//                 referenceRange.add(Chunk.NEWLINE);
//                 referenceRange.add("Stand or sit Position");
//                 testName.add("Patient Position");
//                 break;



         }
         PdfPCell cell1 = new PdfPCell(testName);
         referenceRange.add(Chunk.NEWLINE);

         PdfPCell cell3 = new PdfPCell(referenceRange);
         myTable.addCell(cell1);
         myTable.addCell(cell2);
         myTable.addCell(cell3);
         result.close();
//         getLastReading(SensorType.TEMPERATURE_SENSOR,myTable);

     }

            // Method for opening a pdf file
    private void viewPdf(String file, String directory) {

        File pdfFile = new File(Environment.getExternalStorageDirectory() + "/" + directory + "/" + file);
        Uri path = Uri.fromFile(pdfFile);

        // Setting the intent for pdf reader
        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
        pdfIntent.setDataAndType(path, "application/pdf");
        pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        try {
            startActivity(pdfIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "Can't read pdf file", Toast.LENGTH_SHORT).show();
        }
    }

/*
    public void sendNotification()
    {
        Carousal carousal = Carousal.with(this).beginTransaction();
        carousal.setContentTitle("Your Title Here").setContentText("Your content description here");


        CarousalItem cItem = new CarousalItem("Item Id here", "Item Title", "Item Content","Picture Url");

        //Additionally we can set a type to it. It is useful if we are showing more than one type
        //of data in carousal. so that we know, where to go when an item is clicked.
//        cItem.setType(TYPE_QUOTE);

        //Now add item to the carousal
        carousal.addCarousalItem(cItem);

        carousal.setBigContentTitle("Quotes from everywhere!").setBigContentText("Notice these random quotes from around the world");

        carousal.setOtherRegionClickable(true);  //by default it is false

        carousal.buildCarousal();

    }


            public class CarousalItemClickReceiver extends BroadcastReceiver {

                @Override
                public void onReceive(Context context, Intent intent) {
                    Bundle bundle = intent.getExtras();
                    if (bundle != null) {  //meaning some item is clicked
                        //Get the carousal item that is clicked . Use the same key.
                        CarousalItem item = bundle.getParcelable(Carousal.CAROUSAL_ITEM_CLICKED_KEY);
                        if (item != null) {
                            //Now we need to know where to redirect event
                            String id = item.getId();
                            //Now start an intent or anything else from here

                        } else {  //Meaning other region is clicked and isOtherRegionClick is set to true.
                            //Again handle by anything suitable here.
                            Toast.makeText(context, "Other region clicked", Toast.LENGTH_LONG).show();
                        }

                    }
                }
            }
            */
    public void createPDFTable(Document doc,SensorType sensorType,String tableName,String sensorName) throws DocumentException {
        Paragraph p1 = new Paragraph(tableName);
        p1.add(Chunk.NEWLINE);
        p1.add(Chunk.NEWLINE);
        Font paraFont= new Font(Font.FontFamily.COURIER);
        p1.setFont(paraFont);
        PdfPTable myTable = new PdfPTable(3);
        PdfPCell cell1 = new PdfPCell(new Paragraph("Date"));
        PdfPCell cell2 = new PdfPCell(new Paragraph("Sensor Name"));
        PdfPCell cell3 = new PdfPCell(new Paragraph("Reading"));

        myTable.addCell(cell1);
        myTable.addCell(cell2);
        myTable.addCell(cell3);

        Cursor result = mDbAdapter.fetchAllSensorReadings(sensorType);

        if (result.moveToFirst()) {
            do {
                String id = result.getString(result.getColumnIndex(DBAdapter.KEY_ROW_ID));
                String reading = result.getString(result.getColumnIndex(DBAdapter.KEY_READING_VALUE));
                String date = result.getString(result.getColumnIndex(DBAdapter.KEY_READING_DATE));

                PdfPCell cellDate = new PdfPCell(new Paragraph(date));
                //PdfPCell cellSensorName = new PdfPCell(new Paragraph("Sensor Name"));
                PdfPCell cellSensorName = new PdfPCell(new Paragraph(sensorName));
                PdfPCell cellReading = new PdfPCell(new Paragraph(reading));

                myTable.addCell(cellDate);
                myTable.addCell(cellSensorName);
                myTable.addCell(cellReading);
                // do what ever you want here
            } while (result.moveToNext());
        }

        //add paragraph to document
        doc.add(p1);

        doc.add(myTable);
        doc.add(Chunk.NEXTPAGE);
        result.close();


    }

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted");
                return true;
            } else {

                Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted");
            return true;
        }
    }

            @Override
            public void onFragmentInteraction(Uri uri) {

            }

            public void bluetoothEnable(){

                bluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(bluetoothIntent, 1);

            }

            public void bsluetoothDisable(){

                bluetoothadapter.disable();

            }

            @Override
            protected void onActivityResult(int requestCode, int resultCode, Intent data) {
                super.onActivityResult(requestCode, resultCode, data);

                bluetoothadapter = BluetoothAdapter.getDefaultAdapter();
                Intent intent = new Intent(this, BluetoothMockService.class);
                startService(intent);

            }

            @Override
            public void onDestroy() {
                super.onDestroy();
                if(mDbAdapter!=null)
                {
                    mDbAdapter.close();
                }
            }
        }
