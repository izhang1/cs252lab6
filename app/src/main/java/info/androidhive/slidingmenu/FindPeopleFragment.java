package info.androidhive.slidingmenu;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.firebase.client.Firebase;

import java.lang.reflect.Array;
import java.util.ArrayList;

import info.androidhive.slidingmenu.model.DatabaseConnection;

public class FindPeopleFragment extends Fragment {
	
	public FindPeopleFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_find_people, container, false);
        Firebase.setAndroidContext(rootView.getContext());
        Firebase ref = new Firebase("https://lab6.firebaseio.com/User/");
        final DatabaseConnection data = new DatabaseConnection(ref);

        Button metricButton = (Button)rootView.findViewById(R.id.metricButton);

        ListView metricView = (ListView) rootView.findViewById(R.id.listView);
        ArrayList<String> metricList = data.listMetrics();

        metricButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> metricList = data.listMetrics();
                Log.v("Metric List", metricList.toString());
            }
        });
        return rootView;
    }
}
