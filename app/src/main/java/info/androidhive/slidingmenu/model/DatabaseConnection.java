package info.androidhive.slidingmenu.model;


import android.provider.ContactsContract;
import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.locks.*;
import java.util.concurrent.CountDownLatch;



/**
 * Created by Bill on 12/7/2014.
 */
public class DatabaseConnection {
    private Firebase ref;
    private Firebase metricRef;
    long childrenCount;
    boolean done;
    ArrayList<String> metricList = new ArrayList<String>();
    ArrayList<String> metricsList = new ArrayList<String>();

    public DatabaseConnection (Firebase ref)  {
        metricRef= ref.child("Metric");
    }

    public DatabaseConnection(Firebase ref, Firebase metricRef){

    }

    public void addMetric(String metric, int value){
        final String finalMetric = metric;
        final int finalValue = value;
        metricRef.child(metric).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                childrenCount = dataSnapshot.getChildrenCount();
                Log.d("DataSnapshot", String.valueOf(childrenCount) );
                metricRef.child(finalMetric).child(String.valueOf(childrenCount+1)).setValue(finalValue);
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }


    public void removeLastValue(String metric){
        final String finalMetric = metric;
        metricRef.child(metric).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                childrenCount = dataSnapshot.getChildrenCount();
                Log.d("DataSnapshot", String.valueOf(childrenCount) );
                metricRef.child(finalMetric).child(String.valueOf(childrenCount)).removeValue();
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void removeSpecificValue(String metric, String key){
        Log.v("Removing key", metric+" : " + key);
        metricRef.child(metric).child(key).removeValue();
    }

    public void removeMetric(String metric){
        Log.v("Removing metric", metric);
        metricRef.child(metric).removeValue();
    }


    public void populateMetric ( String metric ) {
        Firebase listRef = metricRef.child(metric);


        listRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator iter =  dataSnapshot.getChildren().iterator();
                metricList.clear();
                while(iter.hasNext()){
                    String metricValue = iter.next().toString();
                    metricValue = getValue(metricValue);
                    metricList.add(metricValue);
                    Log.v("Metric Value", metricValue);

                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }

        });

    }

    public ArrayList<String> listMetric ( ) {
        return this.metricList;
    }

    public void populateMetrics () {

        metricRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator iter =  dataSnapshot.getChildren().iterator();
                metricsList.clear();
                while(iter.hasNext()){
                    String metricName = iter.next().toString();
                    metricName = getKey(metricName);
                    metricsList.add(metricName);
                    Log.v("Metric name", metricName);
                }

            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public ArrayList<String> listMetrics ( ) {
        return this.metricsList;
    }

    //Helper methods to format returned strings
    public String getValue( String input ) {
        String result = null;
        result = input.substring(input.indexOf("value = ") + 8);
        result = result.split(" ")[0];
        return result;
    }

    public String getKey( String input ) {
        String result = null;
        result = input.substring(input.indexOf("key = ") + 6);
        result = result.split(",")[0];
        return result;
    }

}
