/**
 * Copyright (c) 2012 Primum Health IT S.L. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.primum.mobile.device;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.primum.mobile.R;
import com.primum.mobile.activity.ResultActivity;
import com.primum.mobile.exception.TestResultException;
import com.primum.mobile.util.Constants;
import com.primum.mobile.util.HL7MessageGenerator;
import com.signove.health.service.HealthAgentAPI;
import com.signove.health.service.HealthServiceAPI;

public class OximetryDevice extends BaseDevice {

	public OximetryDevice(Context context) {
		super(context);
		testCanceled = false;
		tm = new Handler();
	}

	@Override
	public void performTest() {
		noninConnected = false;
		gotData = false;
		testCanceled = false;
		medicalTestTime = new Date();
		
		testInitialized=true;
		intent= new Intent("com.signove.health.service.HealthService");
		context.startService(intent);
		context.bindService(intent, serviceConnection, 0);
		dialogTimer = new Timer();

        dialogTimer.schedule(new TimerTask() {
			public void run() {
				//This condition is redundant because if the test is canceled the timer is stopped and it wouldn't reach this
				if (!testCanceled) {
					
					dialogTimer.cancel(); // also just top the timer thread, otherwise, you
					testFinished(Device.TEST_RESULT_TIME_OUT);
								// may receive a crash report
				}
			}
		}, 30000); // after 30 seconds (or 30000 miliseconds), the task will be active.
        
	}
	
	@Override
	public String getHL7Message() throws TestResultException{
		checkTextInitialized();

		return HL7MessageGenerator.getPulsioximeterOBX(
				pulsioximeterMacAddress, 
				sdf.format(medicalTestTime),
				oxygenSaturation,
				sdf.format(oxygenSaturationTime),
				pulseRate,
				sdf.format(pulseRateTime));
	}
	
	@Override
	public void printResult(Activity activity, int destLayout) {
		LinearLayout resultLayout = (LinearLayout)activity.findViewById(destLayout);
		View result = activity.getLayoutInflater().inflate(R.layout.result_oximetry, null);
		
		resultLayout.addView(result);

		TextView tx;

		tx = (TextView)activity.findViewById(R.id.txOxySaturation);
		tx.setText(getOxygenSaturation() + "%");

		tx = (TextView)activity.findViewById(R.id.txPulseRate);
		tx.setText(getPulseRate() + " BPM");

		tx= (TextView)activity.findViewById(R.id.txTime);
		tx.setText(formatDate(getMedicalTestTime()));
	}
	
	
	private boolean getTestCanceled() {
        return testCanceled;
    }
	
	
	private void handle_packet_associated(String path, String xml) {}

    private void handle_packet_connected(String path, String dev) {
		pulsioximeterMacAddress = dev;
		dialogTimer.cancel();
		noninConnected = true;
	}

    private void handle_packet_description(String path, String xml) {}

    private void handle_packet_disassociated(String path) {
        if (noninConnected && !gotData && !testCanceled) {
            dialogTimer.cancel();

            testFinished(Device.TEST_RESULT_REMOVED_FINGER);
        }
    }

	private void handle_packet_disconnected(String path) {}

	private Document parse_xml(String xml) {
		Document document = null;

		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			document = db.parse(new ByteArrayInputStream(xml.getBytes("UTF-8")));
		}
        catch (ParserConfigurationException e) {
			Log.w("Antidote", "XML parser error");
		}
        catch (SAXException e) {
			Log.w("Antidote", "SAX exception");
		}
        catch (IOException e) {
			Log.w("Antidote", "IO exception in xml parsing");
		}

		return document;
	}

	private void handle_packet_measurement(String path, String xml) {
		Document d = parse_xml(xml);

		if (d == null) {
			return;
		}

		NodeList datalists = d.getElementsByTagName("data-list");

		for (int i = 0; i < datalists.getLength(); ++i) {
			Log.w("Antidote", "processing datalist " + i);

			Node datalist_node = datalists.item(i);
			NodeList entries = ((Element) datalist_node).getElementsByTagName("entry");

			for (int j = 0; j < entries.getLength(); ++j) {
				Log.w("Antidote", "processing entry " + j);

				boolean ok = false;
				String unit = "";
				String value = "";

				Node entry = entries.item(j);

				// scan immediate children to dodge entry inside another entry
				NodeList entry_children = entry.getChildNodes();

				for (int k = 0; k < entry_children.getLength(); ++k) {
					Node entry_child = entry_children.item(k);

					Log.w("Antidote", "processing entry child " + entry_child.getNodeName());

					if (entry_child.getNodeName().equals("simple")) {
						// simple -> value -> (text)
						NodeList simple = ((Element) entry_child).getElementsByTagName("value");
						Log.w("Antidote", "simple.value count: " + simple.getLength());

						if (simple.getLength() > 0) {
							String text = get_xml_text(simple.item(0));

                            if (text != null) {
								ok = true;
								value = text;
							}
						}
					}
                    else if (entry_child.getNodeName().equals("meta-data")) {
						// meta-data -> meta name=unit
						NodeList metas = ((Element) entry_child).getElementsByTagName("meta");
						Log.w("Antidote", "meta-data.meta count: " + metas.getLength());

                        for (int l = 0; l < metas.getLength(); ++l) {
							Log.w("Antidote", "Processing meta " + l);
							NamedNodeMap attr = metas.item(l).getAttributes();

                            if (attr == null) {
								Log.w("Antidote", "Meta has no attributes");
								continue;
							}

                            Node item = attr.getNamedItem("name");

                            if (item == null) {
								Log.w("Antidote", "Meta has no 'name' attribute");
								continue;
							}

							Log.w("Antidote", "Meta attr 'name' is " + item.getNodeValue());

							if (item.getNodeValue().equals("unit")) {
								Log.w("Antidote", "Processing meta unit");
								String text = get_xml_text(metas.item(l));

                                if (text != null) {
									unit = text;
								}
							}
						}
					}
				}

				if (ok) {
					if (unit != "") {
						if (unit.compareTo("%") == 0) {
							String[] stringParts = value.split("\\.");
							oxygenSaturation = stringParts[0];
							oxygenSaturationTime = new Date();
						}
                        else if (unit.compareTo("bpm") == 0) {
							String[] stringParts=value.split("\\.");
							pulseRate=stringParts[0];
							pulseRateTime = new Date();
						}
					}
					else {
						Log.d("Antidote", "The value of the unit is not set");
					}
				}
			}
		}
		
		if (!testCanceled) {
			gotData = true;
			testFinished(Device.TEST_RESULT_OK);
		}
	}
	
	private String get_xml_text(Node n) {
		String string = null;
		NodeList text = n.getChildNodes();

		for (int l = 0; l < text.getLength(); ++l) {
			Node txt = text.item(l);

			if (txt.getNodeType() == Node.TEXT_NODE) {
				if (string == null) {
					string = "";
				}

				string += txt.getNodeValue();
			}
		}

		return string;
	}

	public String getPulsioximeterMacAddress() {
		return pulsioximeterMacAddress;
	}

	public Date getMedicalTestTime() {
		return medicalTestTime;
	}

	public String getOxygenSaturation() {
		return oxygenSaturation;
	}

	public Date getOxygenSaturationTime() {
		return oxygenSaturationTime;
	}

	public String getPulseRate() {
		return pulseRate;
	}

	public Date getPulseRateTime() {
		return pulseRateTime;
	}


    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.w("HST", "Service connection established");

            // that's how we get the client side of the IPC connection
            api = HealthServiceAPI.Stub.asInterface(service);
            try {
                Log.w("HST", "Configuring...");
                api.ConfigurePassive(agent, specs);
            } catch (RemoteException e) {
                Log.e("HST", "Failed to add listener", e);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.w("HST", "Service connection closed");
        }
    };

    private void RequestConfig(String dev) {
        try {
            Log.w("HST", "Getting configuration ");
            String xmldata = api.GetConfiguration(dev);
            Log.w("HST", "Received configuration");
            Log.w("HST", ".." + xmldata);
        } catch (RemoteException e) {
            Log.w("HST", "Exception (RequestConfig)");
        }
    }

    private void RequestDeviceAttributes(String dev) {
        try {
            Log.w("HST", "Requested device attributes");
            api.RequestDeviceAttributes(dev);
        } catch (RemoteException e) {
            Log.w("HST", "Exception (RequestDeviceAttributes)");
        }
    }

    private HealthAgentAPI.Stub agent = new HealthAgentAPI.Stub() {
        @Override
        public void Connected(String dev, String addr) {
            Log.w("HST", "Connected " + dev);
            Log.w("HST", "..." + addr);
            handle_packet_connected(dev, addr);
        }

        @Override
        public void Associated(String dev, String xmldata) {
            final String idev = dev;
            Log.w("HST", "Associated " + dev);
            Log.w("HST", "...." + xmldata);
            handle_packet_associated(dev, xmldata);

            Runnable req1 = new Runnable() {
                public void run() {
                    RequestConfig(idev);
                }
            };
            Runnable req2 = new Runnable() {
                public void run() {
                    RequestDeviceAttributes(idev);
                }
            };
            tm.postDelayed(req1, 1);
            tm.postDelayed(req2, 500);
        }

        @Override
        public void MeasurementData(String dev, String xmldata) {
            Log.w("HST", "MeasurementData " + dev);
            Log.w("HST", "....." + xmldata);
            handle_packet_measurement(dev, xmldata);
        }

        @Override
        public void DeviceAttributes(String dev, String xmldata) {
            Log.w("HST", "DeviceAttributes " + dev);
            Log.w("HST", ".." + xmldata);
            handle_packet_description(dev, xmldata);
        }

        @Override
        public void Disassociated(String dev) {
            Log.w("HST", "Disassociated " + dev);
            handle_packet_disassociated(dev);
        }

        @Override
        public void Disconnected(String dev) {
            Log.w("HST", "Disconnected " + dev);
            handle_packet_disconnected(dev);
        }
    };
    
    @Override
    public void cancelTest(){
		testCanceled=true;
		dialogTimer.cancel();
		
		((ResultActivity) context).testFinished(Device.TEST_RESULT_FORCED_EXIT);
	
		
	}

    private String pulsioximeterMacAddress = "";
    private Date medicalTestTime;
    private String oxygenSaturation = "";
    private Date oxygenSaturationTime;
    private String pulseRate = "";
    private Date pulseRateTime;
    private Intent intent;
    private int [] specs = {0x1004};
    private Handler tm;
    private HealthServiceAPI api;
    private boolean noninConnected;
    private boolean gotData;
    private boolean testCanceled;
    private Timer dialogTimer;

}
