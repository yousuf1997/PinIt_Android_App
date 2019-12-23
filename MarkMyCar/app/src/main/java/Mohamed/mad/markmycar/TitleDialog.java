package Mohamed.mad.markmycar;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class TitleDialog extends AppCompatDialogFragment  {
    private EditText title;
    private TitleDialogListener titleDialogListener;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.AlertDialogTheme);
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.layout_dialog,null);
        title = view.findViewById(R.id.dailogtitle);
        builder.setView(view).setTitle("Title")
                .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //after the dialog is being clicked
                        //this gets the title which will be used to store in SQL database
                        String title_ = title.getText().toString();

                        //this sends the text to markMyfragment
                        titleDialogListener.applyText(title_);
                    }
                });




        return builder.create();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            titleDialogListener = (TitleDialogListener) context;

        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "Implement dialog listener");
        }
    }

    public interface TitleDialogListener{
        void applyText(String title);
    }
}
