/**
 * Copyright (C) 2012 Fabien Fleurey <fabien@fleurey.com>
 *
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, Version 3, 29 June 2007;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fr.enseirb.odroidx.libdash;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import android.sax.Element;
import android.sax.EndTextElementListener;
import android.sax.RootElement;
import android.sax.StartElementListener;
import android.util.Log;
import android.util.Xml;

public class SAXParser {

	private static final String TAG = SAXParser.class.getSimpleName();	
	private static final String NAMESPACE = "urn:mpeg:DASH:schema:MPD:2011";
	
	private String mpdURL;
	private SegmentManager segmentManager = new SegmentManager();
	private RootElement root;
	
	public SAXParser(String mpdURL) {
		this.mpdURL = mpdURL;
		root = new RootElement(NAMESPACE, "MPD");
		buildContentHandler();
	}
	
	private void buildContentHandler() {
		// Get Base URL
		Element baseURL = root.getChild(NAMESPACE, "BaseURL");
		baseURL.setEndTextElementListener(new EndTextElementListener() {
			@Override
			public void end(String body) {
				segmentManager.setBaseURL(body);
			}
		});
		final Element representation = root.getChild(NAMESPACE, "Period").getChild(NAMESPACE, "AdaptationSet").getChild(NAMESPACE, "Representation");
		representation.setStartElementListener(new StartElementListener() {
			@Override
			public void start(Attributes attributes) {
				Log.w(TAG, "Start representation!");	
				// Process only the first representation at this time.
				if ("0".equals(attributes.getValue("id"))) {
					Log.w(TAG, "Representation 0");
					// Get the first segment
					Element initialization = representation.getChild(NAMESPACE, "SegmentBase").getChild(NAMESPACE, "Initialization");
					initialization.setStartElementListener(new StartElementListener() {
						@Override
						public void start(Attributes attributes) {
							segmentManager.addSegment(attributes.getValue("sourceURL"));
						}
					});
					// Get the others
					Element segmentURL = representation.getChild(NAMESPACE, "SegmentList").getChild(NAMESPACE, "SegmentURL");
					segmentURL.setStartElementListener(new StartElementListener() {
						@Override
						public void start(Attributes attributes) {
							segmentManager.addSegment(attributes.getValue("media"));	
						}
					});
				}
			}
		});
	}
	
	public void parse() {
		new Thread() {
			@Override
			public void run() {
				try {
					InputStream stream = new URL(mpdURL).openConnection().getInputStream();
					Xml.parse(stream, Xml.Encoding.UTF_8, root.getContentHandler());
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (SAXException e) {
					e.printStackTrace();
				}
				Log.d(TAG, "Base URL: " + segmentManager.getBaseURL());	
				int count = 0;
				while (!segmentManager.isEmpty()) {
					count ++;
					Log.v(TAG, "Segment URL: " + segmentManager.getSegment());
				}
				Log.d(TAG, "Segment number: " + count);
			}
		}.start();
	}
}
