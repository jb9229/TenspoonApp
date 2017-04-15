package com.ts.tsadmobtest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.ts.tsadmobtest.react.TSReactActivity;
//import com.ts.tsadmobtest.react.TSReactActivity;


public class MainActivity extends AppCompatActivity {

    private Switch adSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Intent reactRootIntent  =   new Intent(this, TSReactActivity.class);

        adSwitch    =   (Switch) findViewById(R.id.adSwitch);

        adSwitch.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    Intent intent = new Intent(MainActivity.this, ScreenService.class);
                    startService(intent);

                    startActivity(reactRootIntent);

                }else
                {
                    Intent intent = new Intent(MainActivity.this, ScreenService.class);
                    stopService(intent);
                }
            }
        });

    }
}
