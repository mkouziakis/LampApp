package gr.eap.kouziami.lampapp.services;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;

/**
 * Subclass of AsynchTask, we will use this to access the service.
 * Put here all the boilerplate code related to progress dialogs, error dialogs etc etc ....
 *
 * Created by KouziaMi on 22/11/2015.
 */

public class DeviceServiceExecutor extends AsyncTask<DeviceTask, DeviceTask, DeviceTask> {

    private boolean executionSuccessfull = false;
    private Activity callerActivity;
    private ProgressDialog dialog;

    /**
     * Construct and get a few params
     *
     * @param callerActivity
     * @param progressMessage
     */
    public DeviceServiceExecutor(Activity callerActivity, String progressMessage) {
        this.callerActivity = callerActivity;
        dialog =  new ProgressDialog(callerActivity);
        dialog.setMessage(progressMessage);

    }

    /**
     * Before execution
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //show the progress dialog now.
        dialog.setIndeterminate(true);
        dialog.show();
    }

    /**
     * Actual execution, runs in a worker thread
     */
    @Override
    protected DeviceTask doInBackground(DeviceTask... deviceTasks) {
        DeviceTask deviceTask = deviceTasks[0];
        try {
            // Do the invocation
            deviceTask.execute();
            executionSuccessfull = true;
        } catch (final DeviceServiceException e) {
            //If there is an error use the ui thread to notify the user
            Log.e(DeviceServiceExecutor.class.getName(), "Problem while invoking service", e);
            callerActivity.runOnUiThread(new Runnable() {
                public void run() {
                    AlertDialog.Builder builder = new AlertDialog.Builder(callerActivity);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.setMessage(e.getMessage());
                    dialog.show();
                }
            });
        }
        return deviceTask;
    }

    /**
     * After execution
     */
    @Override
    protected void onPostExecute(DeviceTask deviceTask) {
        super.onPostExecute(deviceTask);
        if (executionSuccessfull){
            deviceTask.postSuccess();
        } else {
            deviceTask.postError();
        }
        deviceTask.postDefault();
        // Time to hide progress bar
        dialog.dismiss();
    }
}