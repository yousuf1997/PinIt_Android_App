package Mohamed.mad.markmycar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import androidx.appcompat.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListViewAdapter extends BaseAdapter {
    private DatabaseHelper databaseHelper;
    private Context context_;
    private View current_view;
    private LayoutInflater layoutInflater;
    private List<LocationPoints> locationPointsList;
    private ArrayList<LocationPoints> locationPointsArrayList;
    private   AlertDialog.Builder alert;

    public ListViewAdapter(Context context, List<LocationPoints> locationPointsList) {

        this.context_ = context;
        this.locationPointsList = locationPointsList;
 //       layoutInflater = LayoutInflater.from(context_);
        this.locationPointsArrayList = new ArrayList<LocationPoints>();
       this.locationPointsArrayList.addAll(locationPointsList);
        alert  = new AlertDialog.Builder(context);

        databaseHelper = new DatabaseHelper(context);
    }
    public void setList(List<LocationPoints> locationPointsList){

    }

    public class ViewHolder{
        TextView title, markedDate;
    }
    public void remove(int position) {
        locationPointsList.remove(position);
        //delete database
        databaseHelper.deleteData(locationPointsArrayList.get(position).getTitle());
        notifyDataSetChanged();
    }
    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
         ViewHolder viewHolder;

        Log.i("TAG","GETVIEW");

        if(view == null){

            layoutInflater = (LayoutInflater) context_.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
             view = layoutInflater.inflate(R.layout.list_row,null);
              viewHolder = new ViewHolder();
             viewHolder.title = view.findViewById(R.id.listView_title);
             viewHolder.markedDate = view.findViewById(R.id.markedData);
             view.setTag(viewHolder);
         }else{
             viewHolder = (ViewHolder) view.getTag();
         }
         viewHolder.title.setText(locationPointsList.get(i).getTitle());
        viewHolder.markedDate.setText(locationPointsList.get(i).getMarkedDate());

        //Onclick Listener

      view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //current_view = view;
            //    toast(i);

             viewDialog(view, i);
               //do the intent here
            }
        });
        return view;
    }
    private void toast( int i){

//        Toast.makeText(current_view.getContext(), "You have touched " + locationPointsList.get(i).getTitle(), Toast.LENGTH_LONG).show();
      //  Intent intent = new Intent(current_view.getContext(), LocationMap.class);
        //intent.putExtra("Data", locationPointsList.get(i).getTitle());
         //view.getContext().startActivity(intent);
        // Creates an Intent that will load a map of San Francisco
        Uri gmmIntentUri = Uri.parse("google.navigation:q="+locationPointsList.get(i).getLattitude()+","+locationPointsList.get(i).getLongitude());
       // Log.i("TEST", "**** "+ gmmIntentUri.toString());
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        current_view.getContext().startActivity(mapIntent);


    }

    public void filter(String string){
        string = string.toLowerCase(Locale.getDefault());
        locationPointsList.clear();
        if(string.length() == 0){
            locationPointsList.addAll(locationPointsArrayList);
        }else{
            for(LocationPoints locationPoints : locationPointsArrayList){
                if(locationPoints.getTitle().toLowerCase(Locale.getDefault()).contains(string)){
                    locationPointsList.add(locationPoints);

                }
            }
        }
        notifyDataSetChanged();
    }
    private void viewDialog(View view, final int k){
        current_view = view;
        alert.setTitle("Select").setPositiveButton("Share", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                share(k);
            }
        }).setNegativeButton("Navigate", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
              toast(k);
            }
        }).setNeutralButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                remove(k);
            }
        }).show();
    }
    private void share(int i){
        //http://www.google.com/maps/place/lat,lng
        String link = "http://www.google.com/maps/place/" + locationPointsList.get(i).getLattitude()+","+locationPointsList.get(i).getLongitude();
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, link);
        sendIntent.setType("text/plain");
        current_view.getContext().startActivity(sendIntent);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public Object getItem(int i) {
        return locationPointsList.get(i);
    }

    @Override
    public int getCount() {
        return locationPointsList.size();
    }


}
