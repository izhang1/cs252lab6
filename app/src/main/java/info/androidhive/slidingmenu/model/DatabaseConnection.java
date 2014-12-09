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
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * Created by Bill on 12/7/2014.
 */
public class DatabaseConnection {
    private Firebase ref;
    private Firebase metricRef;
    long childrenCount;
    boolean done;

    public DatabaseConnection (Firebase ref)  {
        metricRef= ref.child("Metrics");
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


    public ArrayList<String> listMetric ( String metric ) {
        final ArrayList<String> list = new ArrayList<String>();
        Firebase listRef = metricRef.child(metric);
        final Lock _mutex = new ReentrantLock(true);

        _mutex.lock();
        listRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator iter =  dataSnapshot.getChildren().iterator();
                while(iter.hasNext()){
                    String metricValue = iter.next().toString();
                    metricValue = getValue(metricValue);
                    list.add(metricValue);
                    Log.v("Metric Value", metricValue);

                }
                _mutex.unlock();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        return list;
    }

    public ArrayList<String> listMetrics () throws InterruptedException {
        final ArrayList<String> list = new ArrayList<String>();
        final Thread thread = new Thread(){
            public void run(){
                System.out.println("Thread Running");


        metricRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator iter =  dataSnapshot.getChildren().iterator();
                while(iter.hasNext()){
                    String metricName = iter.next().toString();
                    metricName = getKey(metricName);
                    list.add(metricName);
                    Log.v("Metric name", metricName);
                }

            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
            }
        };
        thread.start();
        thread.join();
        Log.v("Metric thread", "done");
        return list;
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
