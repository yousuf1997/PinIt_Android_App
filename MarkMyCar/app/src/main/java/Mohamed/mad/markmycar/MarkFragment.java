package Mohamed.mad.markmycar;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;


public class MarkFragment extends Fragment  {
    private TextView textView;
    private DatabaseHelper databaseHelper;
    public static ImageButton marker;



    private View view;

    public MarkFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         view =  inflater.inflate(R.layout.fragment_mark, container, false);
        marker = view.findViewById(R.id.marker);


        marker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                marker.setAlpha(.2f);
                openDialog();
            }
        });
        return view;
    }
    private void openDialog(){
        TitleDialog titleDialog = new TitleDialog();
        titleDialog.show(getFragmentManager(),"Dialog");
    }





}
