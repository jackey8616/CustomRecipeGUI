package net.maple3142.customrecipegui;

import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class JsonFileArrayList<T> extends ArrayList<T>{
	transient String file;
	JsonFileArrayList(String file){
		this.file=file;
	}

	private void save(){
		String json=new Gson().toJson(this);
		try {
			PrintWriter pw=new PrintWriter(file);
			pw.write(json);
			pw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	public boolean addNoUpdate(T t) {
		boolean r=super.add(t);
		return r;
	}
	@Override
	public boolean add(T t) {
		boolean r=super.add(t);
		this.save();
		return r;
	}

	@Override
	public boolean remove(Object o) {
		boolean r=super.remove(o);
		this.save();
		return r;
	}
}
