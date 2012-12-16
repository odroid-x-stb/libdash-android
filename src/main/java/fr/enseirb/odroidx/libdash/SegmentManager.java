package fr.enseirb.odroidx.libdash;

import java.util.ArrayDeque;

public class SegmentManager {
	
	private final ArrayDeque<String> segments = new ArrayDeque<String>();
	private String baseURL;
	
	public SegmentManager() {
	}
	
	public void setBaseURL(String baseURL) {
		this.baseURL = baseURL;
	}
	
	public String getBaseURL() {
		return baseURL;
	}
	
	public boolean addSegment(String segmentRelativeURL) {
		return segments.add(segmentRelativeURL);
	}
	
	public String getSegment() {
		return segments.getFirst();
	}
}
