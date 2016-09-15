package org.ghost;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SwapActivity extends AppCompatActivity implements
        FirstFragment.OnFragmentInteractionListener,
        SecondFragment.OnFragmentInteractionListener,
        OnClickListener{

    private FragmentManager fragmentManager;
    private Fragment firstFrag;
    private Fragment secondFrag;
    private Button swapFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swap);

        findViews();

        fragmentManager = getSupportFragmentManager();

        firstFrag = FirstFragment.newInstance();
        secondFrag = SecondFragment.newInstance();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragmentContainer, firstFrag, "firstFrag");
        fragmentTransaction.commit();
    }

    private void findViews() {
        swapFrag = (Button) findViewById(R.id.swapFrag);
        swapFrag.setOnClickListener(this);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.swapFrag:
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragmentContainer, secondFrag, "secondFrag");
//        fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
        }
    }
}
