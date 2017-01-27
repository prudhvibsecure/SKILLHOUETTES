package com.sharmastech.skillhouettes.imageloader;

import java.util.Hashtable;

public class Item extends Hashtable {

	public Item(String name) {
		// TODO Auto-generated constructor stub
	}

	public void setAttribute(String name, String value) {
		this.put(name, value);
	}

	public String getAttribute(String key) {
		if (this.get(key) != null) {
			return this.get(key).toString();
		} else {
			return "";
		}
	}

	public int getId() {
		return 0;
	}

	public boolean containKey(String key) {
		return this.containsKey(key) ? true : false;
	}

	public void clear() {
		this.clear();
	}


}
