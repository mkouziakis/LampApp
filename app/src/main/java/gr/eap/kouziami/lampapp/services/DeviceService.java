package gr.eap.kouziami.lampapp.services;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import gr.eap.kouziami.lampapp.model.Device;
import gr.eap.kouziami.lampapp.model.DeviceState;
import gr.eap.kouziami.lampapp.model.DeviceStatus;
import gr.eap.kouziami.lampapp.model.ServerResponse;

/**
 * Contains static utility methods
 * for connecting and fetching json data using the provided service.
 *
 * Created by KouziaMi on 21/11/2015.
 */
public class DeviceService {

    static final String HOST = "localhost:3000";

    /**
     *  Invokes get_devices.php
     */
    public static List<Device> readDevices() {
        String url = "http://" + HOST + "/get_devices.php";
        Log.i("DeviceService", "Service url: " + url);

        // Read response as text
        String response = readJSONFeed(url);
        List<Device> devices = new ArrayList<Device>();

        // Use the android provided tools to parse the response
        JSONObject json = null;
        try {
            json = new JSONObject(response);
            checkServerResponse(json);
            JSONArray jsonDevices = json.getJSONArray("devices");
            for (int i = 0; i < jsonDevices.length(); i++) {
                JSONObject jsonDevice = jsonDevices.getJSONObject(i);
                Device device = new Device(jsonDevice.getString("device_id"),
                        jsonDevice.getString("device_name"),
                        jsonDevice.getString("device_type"));
                devices.add(device);
            }
        } catch (ResultException e) {
            // Exception due to an error code returned from the server
            throw e;
        } catch (Exception e) {
            //Exception due to communication or other problems
            throw new DeviceServiceException("Invalid json response: " + response, e);
        }

        return devices;
    }

    /**
     * Invokes get_device_state.php?id=<>
     *
     * @param id
     */
    public static DeviceStatus readDeviceStatus(String id) {
        String url = "http://" + HOST + "get_device_state.php?id=" + id;
        Log.i("DeviceService", "Service url: " + url);
        String response = readJSONFeed(url);

        DeviceStatus status = new DeviceStatus();

        JSONObject json = null;
        try {
            json = new JSONObject(response);
            checkServerResponse(json);
            JSONArray jsonStates = json.getJSONArray("states");
            for (int i = 0; i < jsonStates.length(); i++) {
                JSONObject jsonState = jsonStates.getJSONObject(i);
                DeviceState deviceState = new DeviceState(
                        jsonState.getString("description"),
                        jsonState.getString("state_id"),
                        jsonState.getInt("current_value"),
                        jsonState.getInt("min_value"),
                        jsonState.getInt("max_value"),
                        jsonState.getInt("value_step"));
                status.getStates().put(DeviceStatus.State.valueOf(jsonState.getString("description")), deviceState);
            }
        } catch (ResultException e) {
            throw e;
        } catch (Exception e) {
            throw new DeviceServiceException("Invalid json response: " + response, e);
        }

        return status;
    }

    /**
     * Invokes change_device_state.php?device={0}&state={1}&value={2}
     *
     * @param id
     * @param stateId
     * @param value
     */
    public static void updateDeviceStatus(String id, String stateId, String value) {
        final String template = "http://" + HOST + "/change_device_state.php?device={0}&state={1}&value={2}";
        String url = MessageFormat.format(template, id, stateId, value);
        Log.i("DeviceService", "Service url: " + url);

        String response = readJSONFeed(url);
        JSONObject json = null;
        try {
            json = new JSONObject(response);
            checkServerResponse(json);
        } catch (ResultException e) {
            throw e;
        } catch (Exception e) {
            throw new DeviceServiceException("Invalid json response: " + response, e);
        }
    }

    /**
     * Utilizes HttpURLConnection to access and read content from the given resources
     *
     * @param url
     * @return
     * @throws DeviceServiceException
     */
    private static String readJSONFeed(String url) throws DeviceServiceException {
        HttpURLConnection con = null;
        try {
            URL obj = new URL(url);
            con = (HttpURLConnection) obj.openConnection();

            // Is it always GET? I guess...
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", "Mozilla/5.0");

            int responseCode = con.getResponseCode();
            if (responseCode == 200) {

                //Create a buffer
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                //Keep reading till the end of stream
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                return response.toString();
            } else {
                // HTTP server error
                throw new DeviceServiceException("Problem while invoking service. HTTP error code:" + responseCode);
            }
        }catch (ResultException e) {
            throw e;
        }catch (Exception e) {
            throw new DeviceServiceException("Problem while invoking service", e);
        } finally {
            try {

            } catch(Exception e){
                con.disconnect();
            }
        }
    }

    /**
     * Checks server response. If is not successfull, throw it
     *
     * @param json
     * @throws Exception
     */
    private static void checkServerResponse(JSONObject json) throws Exception {
        ServerResponse serverResponse = readServerResponse(json);
        if (!serverResponse.isSuccess()){
            throw new ResultException(serverResponse.getErrorMessage());
        }
    }

    /**
     * Reads the actual response and creates ServerResponse instance.
     *
     * @param json
     * @throws Exception
     */
    private static ServerResponse readServerResponse(JSONObject json) throws JSONException {
        String result = json.getJSONObject("status").getString("result");
        ServerResponse serverResponse = new ServerResponse(result);
        if (!serverResponse.isSuccess()){
            serverResponse.setErrorMessage(json.getJSONObject("status").getString("reason"));
        }
        return serverResponse;
    }
}
