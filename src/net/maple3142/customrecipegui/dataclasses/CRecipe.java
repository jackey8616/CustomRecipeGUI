package net.maple3142.customrecipegui.dataclasses;

import java.util.ArrayList;
import java.util.Map;

public class CRecipe {
	public String name;
	public String[] shape;
	public ArrayList<CIngredient> ingredient = new ArrayList<>();
	public Map<String, Object> result;

	public CRecipe(String name, String[] shape, Map<String, Object> result) {
		super();
		this.name = name;
		this.shape = shape;
		this.result = result;
	}

	public void addIngredient(CIngredient ig) {
		ingredient.add(ig);
	}
}
