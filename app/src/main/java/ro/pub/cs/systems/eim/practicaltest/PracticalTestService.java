package ro.pub.cs.systems.eim.practicaltest;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.SyncStateContract;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Date;
import java.util.Random;

public class PracticalTestService extends Service {

    ProcessingThread pt;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle extras = intent.getExtras();
        int leftNumber = extras.getInt("leftNo");
        int rightNumber = extras.getInt("rightNo");
        pt = new ProcessingThread(getApplicationContext(), leftNumber, rightNumber);
        pt.start();
        return Service.START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        pt.stopThread();
    }


    private class ProcessingThread extends Thread {

        private Context context = null;
        private boolean isRunning = true;

        private double arithmeticMean;
        private double geometricMean;

        private Random random = new Random();

        public ProcessingThread(Context context, int leftNumber, int rightNumber) {
            this.context = context;

            arithmeticMean = (leftNumber + rightNumber) / 2;
            geometricMean = Math.sqrt(leftNumber * rightNumber);
        }

        public void run() {
            Log.d("[processing thread]", "Thread has started");
            while (isRunning) {
                sendMessage();
                sleep();
            }
            Log.d("[processing thread]", "Thread has stopped");
        }

        private void sendMessage() {
            Intent intent = new Intent();
            intent.setAction(Constants.actionTypes[random.nextInt(Constants.actionTypes.length)]);
            intent.putExtra("message", new Date(System.currentTimeMillis()) + " " + arithmeticMean + " " + geometricMean);
            context.sendBroadcast(intent);
        }

        private void sleep() {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        public void stopThread() {
            isRunning = false;
        }
    }
}
