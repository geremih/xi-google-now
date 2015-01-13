package googlenow.xi.org.xi_google_now;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class CommandInterpreter{


        public static boolean interpret(Context context, String interceptedCommand, boolean continuous)
        {
            Log.i("XI", interceptedCommand);
            sendResult(context, interceptedCommand);
            return true;
        }

    public static void sendResult(final Context context, final String result) {


        class SendResultTask extends AsyncTask<Void, Void, Void> {


            protected Void doInBackground(Void... results) {

                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
                String url = sharedPrefs.getString("URL", "http://192.168.1.139:9001/api");
                Log.v("XI", "Sending " + result + " to " + url);
                HttpClient httpClient = new DefaultHttpClient();
                // replace with your url
                HttpPost httpPost = new HttpPost(url);


                //Post Data
                List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
                nameValuePair.add(new BasicNameValuePair("result", result));


                //Encoding POST data
                try {
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
                } catch (UnsupportedEncodingException e) {
                    // log exception
                    e.printStackTrace();
                }

                //making POST request.
                try {
                    HttpResponse response = httpClient.execute(httpPost);
                    // write response to log
                    Log.d("Http Post Response:", response.toString());
                } catch (ClientProtocolException e) {
                    // Log exception
                    e.printStackTrace();
                } catch (IOException e) {
                    // Log exception
                    e.printStackTrace();
                }
                return null;
            }
        }

        new SendResultTask().execute();
    }
}
