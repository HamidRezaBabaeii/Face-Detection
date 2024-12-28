package com.yaa.mqqt;
import static com.yaa.mqqt.logs.listUpdater;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.FileOutputStream;
import java.io.IOException;

public class MqttService {

    String MQTT_SERVER = "ws://test.mosquitto.org:8080";
    String CLIENT_NAME = "Golabi";
    String MQTT_TOPIC = "testmqttshaf";
    Context context;
    MqttAndroidClient mqttAndroidClient;

    MqttService(Context context) throws MqttException {
        this.context = context;

        mqttAndroidClient = new MqttAndroidClient(context, MQTT_SERVER, CLIENT_NAME);
        mqttAndroidClient.setCallback(mqttCallbackExtended);

        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setCleanSession(false);

        mqttAndroidClient.connect(mqttConnectOptions, new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                Log.d("Mqtt", "Connected");
                subscribeToTopic();  // Call subscribeToTopic() only after successful connection
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                Log.d("Mqtt", "Connection failed: " + exception.toString());
            }
        });
    }

    MqttCallbackExtended mqttCallbackExtended = new MqttCallbackExtended() {
        @Override
        public void connectComplete(boolean reconnect, String serverURI) {
            Log.d("Mqtt", "Connected");
        }

        @Override
        public void connectionLost(Throwable cause) {
            Log.d("Mqtt", "Connection lost: " + cause.toString());
        }

        @Override
        public void messageArrived(String topic, MqttMessage message) {
            Log.d("Mqtt", topic + "(messageArrived): " + message);
            if( message.toString().split(",").length <= 1)
                return;
            String receivedData = message.toString();
            saveDataToFile(receivedData);
            listUpdater(receivedData);
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {
            Log.d("Mqtt", "Delivery complete");
        }
    };

    IMqttActionListener iMqttActionListener = new IMqttActionListener() {
        @Override
        public void onSuccess(IMqttToken asyncActionToken) {
            Log.d("Mqtt", "Subscription successful");
        }

        @Override
        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
            Log.d("Mqtt", "Subscription failed: " + exception.toString());
        }
    };

    IMqttMessageListener iMqttMessageListener = (topic, message) -> {

    };

    public void subscribeToTopic() {
        try {
            mqttAndroidClient.subscribe(MQTT_TOPIC, 0, null, iMqttActionListener);

            // Uncomment if you want to use the IMqttMessageListener
            // mqttAndroidClient.subscribe(MQTT_TOPIC, 0, iMqttMessageListener);

        } catch (MqttException ex) {
            Log.e("Mqtt", "Exception whilst subscribing", ex);
        }
    }

    public void publishMessage(String publishMessage) {
        try {
            MqttMessage message = new MqttMessage();
            message.setPayload(publishMessage.getBytes());
            mqttAndroidClient.publish(MQTT_TOPIC, message);
            if (!mqttAndroidClient.isConnected()) {
                Log.d("Mqtt", "Client not connected");
            }
        } catch (MqttException e) {
            Log.e("Mqtt", "Error Publishing: " + e.getMessage(), e);
        }
    }

    private void saveDataToFile(String data) {
        try {
            FileOutputStream fos = context.openFileOutput("data.txt", Context.MODE_APPEND);
            fos.write((data + "\n").getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
