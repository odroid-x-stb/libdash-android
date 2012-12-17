package fr.enseirb.odroidx.libdash;

import java.io.InputStream;

public class Segment {

	private int id;
	private int startByte;
	private int endByte;
	private InputStream content;
	
	public Segment(int id, int startByte, int endByte) {
		this.id = id;
		this.startByte = startByte;
		this.endByte = endByte;
	}

	public InputStream getContent() {
		return content;
	}

	public void setContent(InputStream content) {
		this.content = content;
	}

	public int getId() {
		return id;
	}

	public int getStartByte() {
		return startByte;
	}

	public int getEndByte() {
		return endByte;
	}
}
