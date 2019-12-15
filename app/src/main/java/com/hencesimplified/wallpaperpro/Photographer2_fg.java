package com.hencesimplified.wallpaperpro;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Photographer2_fg extends Fragment {

    private List<sample_photos> listPhotos;
    private RecyclerView myrv;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private RecyclerViewAdapter myAdap;
    private TextView link1;

    public Photographer2_fg() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root= inflater.inflate(R.layout.fragment_photographer2_fg, container, false);

        firebaseDatabase=FirebaseDatabase.getInstance();
        listPhotos=new ArrayList<>();
        myrv=root.findViewById(R.id.photographer2_rec_id);
        link1=root.findViewById(R.id.link1);
        myrv.setHasFixedSize(true);
        myrv.setLayoutManager(new GridLayoutManager(getContext(),3));
        myAdap = new RecyclerViewAdapter(getContext(),listPhotos);


        SharedPreferences pref = getContext().getSharedPreferences("ProPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("ProPage",4);
        editor.apply();


        databaseReference = firebaseDatabase.getReference("photographer2");


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    sample_photos photos = postSnapshot.getValue(sample_photos.class);
                    listPhotos.add(photos);
                }

                myrv.setAdapter(myAdap);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        link1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alert_dia1=new AlertDialog.Builder(getContext()).create();
                alert_dia1.setTitle("View Photographer?");
                alert_dia1.setMessage("Do you want to view the photographer's profile in Instagram?");

                alert_dia1.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/rennies_photography/")));
                    }
                });
                alert_dia1.setButton(AlertDialog.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alert_dia1.show();

            }
        });

        return root;
    }

}
