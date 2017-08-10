package net.maple3142.customrecipegui;

import java.util.ArrayList;
import java.util.Map;

public class CRecipe {
	String name;
	String[] shape;
	ArrayList<CIngredient> ingredient=new ArrayList<>();
	Map<String,Object> result;
	public CRecipe(String name, String[] shape,  Map<String,Object> result) {
		super();
		this.name = name;
		this.shape = shape;
		this.result = result;
	}
	public void addIngredient(CIngredient ig) {
		ingredient.add(ig);
	}
}
