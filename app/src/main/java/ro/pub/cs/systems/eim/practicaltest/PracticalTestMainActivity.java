package ro.pub.cs.systems.eim.practicaltest;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class PracticalTestMainActivity extends AppCompatActivity {

    private final  static int SECONDARY_ACTIVITY_REQUEST_CODE = 1;
    private static int serviceStatus = Constants.SERVICE_STOPPED;

    private MessageBroadcastReceiver messageBroadcastReceiver = new MessageBroadcastReceiver();
    private class MessageBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(Constants.BROADCAST_RECEIVER_TAG, intent.getStringExtra(Constants.BROADCAST_RECEIVER_EXTRA));
        }
    }

    private IntentFilter intentFilter = new IntentFilter();

    private Button switchActivity;
    private TextView leftText;
    private TextView rightText;
    private Button leftButton;
    private Button rightButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test_main);

        switchActivity = (Button) findViewById(R.id.button_navigate);
        leftText = (TextView) findViewById(R.id.text_left);
        rightText = (TextView) findViewById(R.id.text_right);
        leftButton = (Button) findViewById(R.id.button_left);
        rightButton = (Button) findViewById(R.id.button_right);

        MyListener listener = new MyListener();
        leftButton.setOnClickListener(listener);
        rightButton.setOnClickListener(listener);
        switchActivity.setOnClickListener(listener);

        for (int index = 0; index < Constants.actionTypes.length; index++) {
            intentFilter.addAction(Constants.actionTypes[index]);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(messageBroadcastReceiver, intentFilter);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("camp1", leftText.getText().toString());
        outState.putString("camp2", rightText.getText().toString());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SECONDARY_ACTIVITY_REQUEST_CODE) {
            Toast.makeText(this, "Activity returned with result " + resultCode, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            leftText.setText(savedInstanceState.getString("camp1"));
            rightText.setText(savedInstanceState.getString("camp2"));
        }
    }

    @Override
    protected void onDestroy() {
        Intent intent = new Intent(this, PracticalTestService.class);
        stopService(intent);
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        unregisterReceiver(messageBroadcastReceiver);
        super.onPause();
    }

    private class MyListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Integer intVal1;
            Integer intVal2;
            switch(v.getId()) {
                case R.id.button_left:
                    String leftValue = leftText.getText().toString();
                    intVal1 = Integer.valueOf(leftValue) + 1;
                    leftText.setText(String.valueOf(intVal1));
                    break;
                case R.id.button_right:
                    String rightValue = rightText.getText().toString();
                    intVal2 = Integer.valueOf(rightValue) + 1;
                    rightText.setText(String.valueOf(intVal2));
                    break;
                case R.id.button_navigate:

                    Integer leftVal = Integer.valueOf(leftText.getText().toString());
                    Integer rightVal = Integer.valueOf(rightText.getText().toString());
                    Integer total = leftVal + rightVal;
                    Intent intent = new Intent(PracticalTestMainActivity.this, PracticalTestSecondaryActivity.class);
                    intent.putExtra("total", String.valueOf(total.intValue()));
                    startActivityForResult(intent, SECONDARY_ACTIVITY_REQUEST_CODE);
                    break;
            }
            int left = Integer.parseInt(leftText.getText().toString());
            int right = Integer.parseInt(rightText.getText().toString());
            if (left + right > Constants.NUMBER_OF_CLICKS_THRESHOLD && serviceStatus == Constants.SERVICE_STOPPED) {
                Intent intent = new Intent(getApplicationContext(), PracticalTestService.class);
                intent.putExtra("leftNo", left);
                intent.putExtra("rightNo", right);
                startService(intent);
                serviceStatus = Constants.SERVICE_STARTED;
            }
        }
    }
}
