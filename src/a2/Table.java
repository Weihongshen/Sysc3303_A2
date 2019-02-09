package a2;

import java.util.ArrayList;

public class Table {
	
	private ArrayList<Ingredient> ingredients;
	private int sandwiches; // NO. of sandwiches.
	private boolean empty = true; // table is empty.

	/**
	 * create a new table with ingredients and sandwiches.
	 */
	public Table() {
		ingredients = new ArrayList<Ingredient>();
		sandwiches = 0;
	}
	
	/**
	 * put ingredients to the table.
	 * @param ingredient
	 */
	public synchronized void put(ArrayList<Ingredient> ingredient) {
		while (!empty) {
			try {
				wait();
			} catch (InterruptedException e) {
				System.exit(1);
			}
		}
		
		for (Ingredient i : ingredient) {
			ingredients.add(i);
		}
		empty = false;
		notifyAll();
	}
	
	public synchronized ArrayList<Ingredient> get(){
		ArrayList<Ingredient> sendIng = new ArrayList<Ingredient>();
		while (empty) {
            try {
                wait();
            } catch (InterruptedException e) {
                return null;
            }
        }
		sendIng.addAll(ingredients);
       	ingredients.clear();
       	sandwiches++;
       	empty=true;
       	notifyAll();
       	return sendIng;
	}
	
	/**
	 * get the number of sandwiches.
	 * @return the number of sandwiches
	 */
	public int getSandwiches() {
		return sandwiches;
	}
	
	/**
	 * check if the table is empty.
	 * @return
	 */
	public boolean isEmpty() {
		return empty;
	}
	
	/**
	 * check if the ingredient list contain the specific ingredient.
	 * @param i ingredient.
	 * @return
	 */
	public boolean hasIng(Ingredient i) {
		return ingredients.contains(i);
	}
}
