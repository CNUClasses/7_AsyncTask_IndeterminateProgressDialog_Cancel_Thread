package com.example.keith.deleteme;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static final int NUMBER_UPDATES = 2000;
    private static final int ONE_SECOND = 10;
    private static final String RUNNING_CALC = "Running Calculation for thread ";
    private static final String DONE = "Done with thread ";
    private static final String USER_CANCELED = "User chose to cancel";

    MyTask task;

    private ProgressDialog myProgressDialog;

    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = (TextView)findViewById(R.id.textView);
    }

    public void doCalc(View view) {
        tv.setText(RUNNING_CALC);

        task = new MyTask(this);
        task.execute(NUMBER_UPDATES);

//        runCalcs(NUMBER_UPDATES);
    }


    public void doStopCalc(View view) {
    }


    private void runCalcs(int numberUpdates) {
        for (int i = 0; i <= numberUpdates; i++) {
            try {
                Thread.sleep(ONE_SECOND);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private static class MyTask extends AsyncTask<Integer,Void,Void> {

        private final MainActivity ma;

        public MyTask(MainActivity ma) {
            this.ma = ma;
        }

        @Override
        protected Void doInBackground(Integer... integers) {

            int tot = integers[0];
            for (int i = 0; i <= tot; i++) {
                ma.runCalcs(2);
                if (isCancelled())
                    break;
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ma.progressDialog_start();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            ma.progressDialog_stop();
            ma.tv.setText(DONE );
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            ma.tv.setText(USER_CANCELED);
            ma.progressDialog_stop();
        }
    }

    private void progressDialog_start() {
        myProgressDialog = new ProgressDialog(this);
        myProgressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        task.cancel(true);
                        //myProgressDialog = null;
                    }
                });
        myProgressDialog.setTitle("Please wait");
        myProgressDialog.setMessage("Notice user cannot interact with rest of UI\nincluding starting additional threads");
        myProgressDialog.setCancelable(false);
        myProgressDialog.show();
    }

    private void progressDialog_stop(){
        myProgressDialog.dismiss();
    }

}
