
package com.example.jerry.myodata;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.thalmic.myo.AbstractDeviceListener;
import com.thalmic.myo.Arm;
import com.thalmic.myo.DeviceListener;
import com.thalmic.myo.Hub;
import com.thalmic.myo.Myo;
import com.thalmic.myo.Pose;
import com.thalmic.myo.Quaternion;
import com.thalmic.myo.Vector3;
import com.thalmic.myo.XDirection;
import com.thalmic.myo.scanner.ScanActivity;

import java.util.ArrayList;
import java.util.List;


public class DataActivity extends Activity {

    private TextView txtConnected;
    private TextView txtXData;
    private TextView txtYData;
    private TextView txtZData;
    private TextView txtWData;
    private Button btn;
    private Button btnRecord;
    private ListView txtFix;
    private GraphView graph;

    private TextView txtXAccel;
    private TextView txtYAccel;
    private TextView txtZAccel;

    //good data
    private List<Double> xGood;
    private List<Double> yGood;
    private List<Double> zGood;
    private List<Double> wGood;
    private List<Long> timesGood;

    private List<Double> xPGood;
    private List<Double> yPGood;
    private List<Double> zPGood;
    private List<Double> wPGood;

    private double dRoll; //106.06
    private double dYaw;//-11.53
    private double dPitch;//-111.82

    //user data
    private List<Double> pitch;
    private List<Double> roll;
    private List<Double> yaw;
    private List<Double> w;
    private List<Long> times;
    private List<Double> accelX;
    private List<Double> accelY;
    private List<Double> accelZ;

    private List<Double> pitchPrime;
    private List<Double> rollPrime;
    private List<Double> yawPrime;
    private List<Double> wP;

    private long startTime;
    private boolean recording = false;

    double[] d;

    private DeviceListener mListener = new AbstractDeviceListener() {

        double Ax = 0,Ay = 0,Az = 0;
        // onConnect() is called whenever a Myo has been connected.
        @Override
        public void onConnect(Myo myo, long timestamp) {
            // Set the text color of the text view to cyan when a Myo connects.
            txtConnected.setText("Connected");
            btn.setEnabled(true);
            btnRecord.setEnabled(true);
        }

        // onDisconnect() is called whenever a Myo has been disconnected.
        @Override
        public void onDisconnect(Myo myo, long timestamp) {
            // Set the text color of the text view to red when a Myo disconnects.
            txtConnected.setText("Disconnected");
            btn.setEnabled(false);
            btnRecord.setEnabled(false);
        }

        // onArmSync() is called whenever Myo has recognized a Sync Gesture after someone has put it on their
        // arm. This lets Myo know which arm it's on and which way it's facing.
        @Override
        public void onArmSync(Myo myo, long timestamp, Arm arm, XDirection xDirection) {
            txtConnected.setText("Arm Synced");
        }

        // onArmUnsync() is called whenever Myo has detected that it was moved from a stable position on a person's arm after
        // it recognized the arm. Typically this happens when someone takes Myo off of their arm, but it can also happen
        // when Myo is moved around on the arm.
        @Override
        public void onArmUnsync(Myo myo, long timestamp) {
            txtConnected.setText("Connected");
        }

        // onUnlock() is called whenever a synced Myo has been unlocked. Under the standard locking
        // policy, that means poses will now be delivered to the listener.
        @Override
        public void onUnlock(Myo myo, long timestamp) {

        }

        // onLock() is called whenever a synced Myo has been locked. Under the standard locking
        // policy, that means poses will no longer be delivered to the listener.
        @Override
        public void onLock(Myo myo, long timestamp) {

        }
        // onOrientationData() is called whenever a Myo provides its current orientation,
        // represented as a quaternion.
        @Override
        public void onOrientationData(Myo myo, long timestamp, Quaternion rotation) {

            txtXData.setText( String.valueOf(180+Math.toDegrees(Quaternion.pitch(rotation))));
            txtYData.setText( String.valueOf(180+Math.toDegrees(Quaternion.yaw(rotation))));
            txtZData.setText( String.valueOf(180+Math.toDegrees(Quaternion.roll(rotation))));
            txtWData.setText( String.valueOf(rotation.w()));
            if((System.currentTimeMillis()-startTime)>10 && recording)
            {

                pitch.add(180+Math.toDegrees(Quaternion.pitch(rotation)));
                roll.add(180+ Math.toDegrees(Quaternion.roll(rotation)));
                yaw.add(180 + Math.toDegrees(Quaternion.yaw(rotation)));
                w.add(rotation.w());
                times.add(System.currentTimeMillis());
                accelX.add(Ax);
                accelY.add(Ay);
                accelZ.add(Az);
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                startTime = System.currentTimeMillis();
            }
        }

        // onPose() is called whenever a Myo provides a new pose.
        @Override
        public void onPose(Myo myo, long timestamp, Pose pose) {
            // Handle the cases of the Pose enumeration, and change the text of the text view
            // based on the pose we receive.


        }

        @Override
        public void onAccelerometerData(Myo myo, long timestamp, Vector3 accel){

            Ax = accel.x();
            Ay = accel.y();
            Az = accel.z();

            txtXAccel.setText(String.valueOf(accel.x()));
            txtYAccel.setText(String.valueOf(accel.y()));
            txtZAccel.setText(String.valueOf(accel.z()));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_activity);

        pitch = new ArrayList<Double>();
        roll = new ArrayList<Double>();
        yaw = new ArrayList<Double>();
        w = new ArrayList<Double>();
        times = new ArrayList<Long>();

        pitchPrime = new ArrayList<Double>();
        rollPrime = new ArrayList<Double>();
        yawPrime = new ArrayList<Double>();
        wP = new ArrayList<Double>();

        txtConnected = (TextView) findViewById(R.id.txtConnect);
        txtXData = (TextView) findViewById(R.id.txtXData);
        txtYData = (TextView) findViewById(R.id.txtYData);
        txtZData = (TextView) findViewById(R.id.txtZData);
        txtWData = (TextView) findViewById(R.id.txtWData);
        txtXAccel = (TextView) findViewById(R.id.txtXAccel);
        txtYAccel = (TextView) findViewById(R.id.txtYAccel);
        txtZAccel = (TextView) findViewById(R.id.txtZAccel);
        btn = (Button) findViewById(R.id.btn);
        btnRecord = (Button) findViewById(R.id.btnRecord);
        btn.setEnabled(false);
        txtFix = (ListView) findViewById(R.id.txtFix);
        graph = (GraphView) findViewById(R.id.graph);

        d = getIntent().getDoubleArrayExtra("aim");

        dPitch = d[0];
        dYaw = d[1];
        dRoll = d[2];

        recording = false;

        accelX = new ArrayList<Double>();
        accelY = new ArrayList<Double>();
        accelZ = new ArrayList<Double>();

        // First, we initialize the Hub singleton with an application identifier.
        Hub hub = Hub.getInstance();
        if (!hub.init(this, getPackageName())) {
            // We can't do anything with the Myo device if the Hub can't be initialized, so exit.
            Toast.makeText(this, "Couldn't initialize Hub", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Next, register for DeviceListener callbacks.
        hub.addListener(mListener);

    }

    private double theRoll = 0;
    private double thePitch = 0;
    private double theYaw = 0;
    private boolean gatherData = false;
    private int counter = 0;

    public void btnClick(View v){
        if(!recording) {
            Log.i("start","start");
            graph.removeAllSeries();
            recording = true;
            btn.setText("Stop");
            startTime = System.currentTimeMillis();
        }
        else {
            btn.setText("Start");
            Log.i("stop","stop");
            recording = false;

 //           for(int i = 0; i <x.size()-1; ++i){
       //         pitchPrime.add((x.get(i+1)-x.get(i)/(times.get(i+1)-times.get(i))));
         //   }

            int startIndex =0, endIndex = times.size()-1;

            for(int i = 0; i < times.size()-1; i++){
                pitchPrime.add((pitch.get(i+1)-pitch.get(i))/(times.get(i+1)-times.get(i)));
                rollPrime.add((roll.get(i+1)-roll.get(i))/(times.get(i+1)-times.get(i)));
                yawPrime.add((yaw.get(i+1)-yaw.get(i))/(times.get(i+1)-times.get(i)));
            }

            while(Math.abs(pitchPrime.get(startIndex)) < 1E-3 || Math.abs(rollPrime.get(startIndex)) < 1E-3  || Math.abs(yawPrime.get(startIndex)) < 1E-3 ){
                if(startIndex == times.size()) {
                    startIndex = 0;
                    break;
                }
                startIndex++;
            }
            while(Math.abs(pitchPrime.get(endIndex-1)) < 1E-3 || Math.abs(rollPrime.get(endIndex-1)) < 1E-3  || Math.abs(yawPrime.get(endIndex-1)) < 1E-3 ){
                if(endIndex == 1) {
                    endIndex = times.size()-1;
                    break;
                }
                endIndex--;
            }

            //trapezoid from startIndexTime to endIndex Time

          /*  double Vx = accelX.get(startIndex);
            for(int i = startIndex+1; i <endIndex; i++)
            {
                Vx += Math.abs(2*accelX.get(i));
            }
            Vx += accelX.get(endIndex);
            Vx *= (times.get(endIndex)-times.get(startIndex))/2000.0; */


            Log.i("Original End", String.valueOf(times.size()));
            Log.i("START",String.valueOf(startIndex));
            Log.i("END",String.valueOf(endIndex));
            for(int i = startIndex; i <= endIndex;++i)
            {
                Log.i("PITCH", String.valueOf(pitch.get(i)));
                Log.i("ROLL", String.valueOf(roll.get(i)));
                Log.i("YAW", String.valueOf(yaw.get(i)));
                if(i!= times.size()-1){
                    Log.i("Pitch Prime", String.valueOf(pitchPrime.get(i)));
                    Log.i("Roll Prime", String.valueOf(rollPrime.get(i)));
                    Log.i("Yaw Prime", String.valueOf(yawPrime.get(i)));
                }
                Log.i("Time", String.valueOf(times.get(i)));
            }

            double userDeltaPitch = pitch.get(endIndex)-pitch.get(startIndex);
            double userDeltaYaw = yaw.get(endIndex)-yaw.get(startIndex);
            double userDeltaRoll = roll.get(endIndex)-roll.get(startIndex);

            if(gatherData)
            {
                theRoll += userDeltaRoll;
                theYaw += userDeltaYaw;
                thePitch += userDeltaPitch;
                counter++;
            }
            Log.i("TIME ELASPED",String.valueOf(times.get(endIndex)-times.get(startIndex)));
            Log.i("Delta Pitch", String.valueOf(userDeltaPitch));
            Log.i("Delta Yaw", String.valueOf(userDeltaYaw));
            Log.i("Delta Roll", String.valueOf(userDeltaRoll));


            //Log.i("Vx",String.valueOf(Vx));

            ArrayList<String> toAdd = new ArrayList<String>();
            if(!gatherData) {
                if (Math.abs(userDeltaYaw - dYaw) > 30) {
                    toAdd.add("You are swinging your arm sideways too much, follow through to your waist");
                }
                if (Math.abs(userDeltaRoll - dRoll) > 30) {
                    toAdd.add("You are not rolling your fingers, snap your wrist after release and point down with your thumb");
                }
                if (Math.abs(dPitch - userDeltaPitch) > 30) {
                    toAdd.add("Your arms are not high enough, raise your elbows at the beginning and follow through all the way down");
                }
            }


            ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this, R.layout.text_layout, toAdd);
            txtFix.setAdapter(listAdapter);

            DataPoint[] userRoll = new DataPoint[endIndex+1-startIndex];
            DataPoint[] userPitch = new DataPoint[endIndex+1-startIndex];
            DataPoint[] userYaw = new DataPoint[endIndex+1-startIndex];

            for(int i = 0; i < endIndex+1-startIndex; i++){
                userRoll[i] = new DataPoint(i+startIndex,roll.get(i+startIndex));
                userPitch[i] = new DataPoint(i+startIndex,pitch.get(i+startIndex));
                userYaw[i] = new DataPoint(i+startIndex,yaw.get(i+startIndex));
            }
            LineGraphSeries<DataPoint> seriesRoll = new LineGraphSeries<>(userRoll);
            LineGraphSeries<DataPoint> seriesPitch = new LineGraphSeries<>(userPitch);
            LineGraphSeries<DataPoint> seriesYaw = new LineGraphSeries<>(userYaw);
            seriesRoll.setColor(Color.BLUE);
            seriesRoll.setTitle("Roll");
            seriesPitch.setColor(Color.RED);
            seriesPitch.setTitle("Pitch");
            seriesYaw.setColor(Color.GREEN);
            seriesYaw.setTitle("Yaw");
            graph.addSeries(seriesRoll);
            graph.addSeries(seriesPitch);
            graph.addSeries(seriesYaw);

            graph.getLegendRenderer().setVisible(true);
           // graph.getLegendRenderer().setWidth(200);
            graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);

            pitch.clear();
            roll.clear();
            yaw.clear();
            w.clear();
            times.clear();

            pitchPrime.clear();
            rollPrime.clear();
            yawPrime.clear();
            wP.clear();
        }

    }

    public void recordClick(View v){
        if(gatherData){
            dPitch = thePitch/(double)counter;
            dYaw = theYaw/(double)counter;
            dRoll = theRoll/(double)counter;
            gatherData = false;
            btnRecord.setText("Record");
        }
       else{
            gatherData = true;
            btnRecord.setText("Recording");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // We don't want any callbacks when the Activity is gone, so unregister the listener.
      //  Hub.getInstance().removeListener(mListener);

        if (isFinishing()) {
            // The Activity is finishing, so shutdown the Hub. This will disconnect from the Myo.
            //Hub.getInstance().shutdown();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_data, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (R.id.action_scan == id) {
            onScanActionSelected();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void onScanActionSelected() {
        // Launch the ScanActivity to scan for Myos to connect to.
        Intent intent = new Intent(this, ScanActivity.class);
        startActivity(intent);
    }
}