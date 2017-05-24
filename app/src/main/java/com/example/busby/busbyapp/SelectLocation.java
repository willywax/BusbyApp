package com.example.busby.busbyapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.busby.busbyapp.databinding.SelectLocationBinding;

import Objects.Location;


public class SelectLocation extends AppCompatActivity {
    SelectLocationBinding selectLocationBinding;


    SearchView searchView;

    ListAdapter adapter;

    Location location;
    public String text;
    private int UserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UserID=getIntent().getIntExtra("UserID",0);
        selectLocationBinding = DataBindingUtil.setContentView(this, R.layout.select_location);

        //Dummy data stored in location class
        location = new Location("Edgars","WestGate");
        location.add("Edgars","SandtonCity");
        location.add("Markhams","ClearWater");
        location.add("Markhams","CradleStone");
        location.add("Edgars","NorthGate");
        location.add("Edgars","EastGate");



        adapter = new ListAdapter(location.getLocationArray());
        selectLocationBinding.listView.setAdapter(adapter);

        searchView = (SearchView) findViewById(R.id.search);
        searchView.setActivated(true);
        searchView.setQueryHint("Type your word here");
        searchView.onActionViewExpanded();
        searchView.setIconified(false);
        searchView.clearFocus();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });


        selectLocationBinding.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //sets text in the search view (auto complete)
                text = adapterView.getItemAtPosition(i).toString();
                searchView.setQuery(text, false);

                //finish();
            }
        });
        selectLocationBinding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectLocation.this, New_Post.class);
                intent.putExtra("LocationName", text);
                intent.putExtra("UserID",UserID);
                startActivity(intent);

            }
        });
    }
}
