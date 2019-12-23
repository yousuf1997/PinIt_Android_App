package Mohamed.mad.markmycar;

import android.database.Cursor;
import android.database.DataSetObserver;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;


public class MyCarFragment extends Fragment  {

   private DatabaseHelper databaseHelper;
   private boolean start = false;
   private TextView textView;
   private View view;
    private SearchView searchView;
   // private ArrayList<LocationPoints> locationPoints = new ArrayList<>();
    private ListView listView;
    private ListViewAdapter listViewAdapter_;
    private DataSetObserver dataSetObserver;

    private Cursor cursor;
    public  ArrayList<LocationPoints> locationPoints = new ArrayList<>();
    public MyCarFragment() {
        // Required empty public constructor
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_my_car, container, false);
        listView = view.findViewById(R.id.location_list);
        databaseHelper = new DatabaseHelper(getContext());
       // alert  = new AlertDialog.Builder(getActivity());
        searchView = view.findViewById(R.id.searchList);
        //populateOpenData();


        //populateArray();

            listViewAdapter_ = new ListViewAdapter(view.getContext(), populateArray());
            listView.setAdapter(listViewAdapter_);

     //   listViewAdapter_.filter("");
       // listView.clearTextFilter();
        /*view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "You have touched inside the thing!!!"  , Toast.LENGTH_LONG).show();
              //  Log.i("T","TOUCHED");
            }
        });*/

       // listViewAdapter_.notifyDataSetChanged();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (s.isEmpty()){
                    listViewAdapter_.filter("");
                    listView.clearTextFilter();
                }else{
                    listViewAdapter_.filter(s);
                }
                return true;
            }
        });
        Log.i("TAG", "The list size is " + populateArray().size());
        //databaseHelper.deleteAllRows();
           /* listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    //Toast.makeText(view.getContext(), "You have touched " + locationPoints.get(i).getTitle(), Toast.LENGTH_LONG).show();
                    goToMap(locationPoints.get(i).getTitle());
                }
            });*/


        return view;

    }
    private void goToMap(String data){
       // Intent intent = new Intent(view.getContext(), MapsActivity.class);
      //  intent.putExtra("Data", data);
        //startActivity(intent);
    }



    public   ArrayList populateArray(){
        locationPoints.clear();
        Log.i("TAG", "Database being called");
        cursor = databaseHelper.getData();
        int count = 0;
        while (cursor.moveToNext()){
            locationPoints.add(new LocationPoints(Double.parseDouble(cursor.getString(4)),
                    Double.parseDouble(cursor.getString(3)),
                    cursor.getString(1),cursor.getString(2)));/*
           //(Double lattitude, Double longitude, String title, String markedDate)
           Log.i("LIST ",
                   0 + " " + cursor.getString(0) + " " +
                           1 + " " + cursor.getString(1) + " " +
                           2 + " " + cursor.getString(2) + " " +
                           3 + " " + cursor.getString(3));*/

            count = count + 1;
        }
        Log.i("TAG", "Inside the populate The list size is " + locationPoints.size());
        return locationPoints;
    }




}
