package com.oldturok.turok;

public class TurokIRC {
	private int index;
	private String line;
	private String sender;
	private boolean read;

	public TurokIRC(int index, String line, String sender, boolean read) {
		this.index  = index;
		this.line   = line;
		this.sender = sender;
		this.read   = read;
	}

	public int get_index() {
		return index;
	}

	public void set_index(int index) {
		this.index = index;
	}

	public String get_line() {
		return line;
	}

	public void set_line(String line) {
		this.line = line;
	}

	public String get_sender() {
		return sender;
	}

	public void set_sender(String sender) {
		this.sender = sender;
	}

	public boolean is_read() {
		return read;
	}

	public void set_read(boolean read) {
		this.read = read;
	}
}