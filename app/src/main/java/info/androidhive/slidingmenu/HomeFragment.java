package info.androidhive.slidingmenu;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;

public class HomeFragment extends Fragment {
	
	public HomeFragment(){}



	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        OnClickListener listnr=new OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent i= new Intent("aFavorite");
                //startActivity(i);
                Intent intent = new Intent(getActivity(), SimpleXYPlotActivity.class);
                startActivity(intent);
            }
        };

        //Button btn = (Button) rootView.findViewById(R.id.testplot);
        //btn.setOnClickListener(listnr);

        return rootView;
    }



    public void graphTest(View view){
        Intent intent = new Intent(getActivity(), SimpleXYPlotActivity.class);
        startActivity(intent);

    }

}
