package info.androidhive.slidingmenu;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.firebase.client.Firebase;

import info.androidhive.slidingmenu.model.DatabaseConnection;

public class HomeFragment extends Fragment {
	
	public HomeFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        Firebase.setAndroidContext(rootView.getContext());
        Firebase ref = new Firebase("https://lab6.firebaseio.com/User/");
        Button button = (Button) rootView.findViewById(R.id.button);

        final DatabaseConnection data = new DatabaseConnection(ref);

        button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                data.addMetric("Bench", 185 );
            }
        });
        return rootView;
    }
}
