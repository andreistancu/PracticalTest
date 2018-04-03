package ro.pub.cs.systems.eim.practicaltest;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PracticalTestSecondaryActivity extends AppCompatActivity{
    private TextView totalText;
    private Button okButton;
    private Button cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test_secondary);

        totalText = (TextView) findViewById(R.id.text_total);
        okButton = (Button) findViewById(R.id.button_ok);
        cancelButton = (Button) findViewById(R.id.button_cancel);

        MyListener listener = new MyListener();
        okButton.setOnClickListener(listener);
        cancelButton.setOnClickListener(listener);

        Intent intent = getIntent();
        if (intent != null) {
            String totalValue = intent.getStringExtra("total");
            totalText.setText(totalValue);
        }
    }

    private class MyListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.button_ok:
                    setResult(RESULT_OK, null);
                    break;
                case R.id.button_cancel:
                    setResult(RESULT_CANCELED, null);
                    break;
            }
            finish();
        }
    }
}
