package a2;

import java.util.ArrayList;

/**
 * Table test creating a table for chef and agent.
 * @author Weihong shen
 *
 */
public class TableTest {

	public static void main(String[] args) {

		Thread agent, pbChef, bChef, jChef;
		Table table;

		table = new Table();

		agent = new Thread(new Agent(table), "Agent");
		pbChef = new Thread(new Chef(table, Ingredient.PEANUT_BUTTER), "Peanut_butter Chef");
		bChef = new Thread(new Chef(table, Ingredient.BREAD), "Bread Chef");
		jChef = new Thread(new Chef(table, Ingredient.JAM), "Jam Chef");
		agent.start();
		pbChef.start();
		bChef.start();
		jChef.start();
		System.out.println(table.getSandwiches());
	}
}

/**
 * class agent, agent has all three ingredients 
 * and random chose two of them to put on table.
 * 
 * @author Weihong Shen
 *
 */
class Agent implements Runnable {

	private Table table;

	public Agent(Table t) {
		table = t;
	}

	/**
	 * return a random ingredient.
	 * 
	 * @return
	 */
	private Ingredient getRanIng() {
		int i = (int) (Math.random() * Ingredient.values().length);
		return Ingredient.values()[i];
	}

	@Override
	public void run() {
		ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
		Ingredient secondIng;
		// Creates 20 sandwiches.
		for (int i = 0; i < 20; i++) {
			ingredients.clear();
			ingredients.add(getRanIng());

			do {
				secondIng = getRanIng();
			} while (ingredients.contains(secondIng));

			ingredients.add(secondIng);

			System.out.print(Thread.currentThread().getName() + " put ingredients: ");
			for (Ingredient ing : ingredients) {
				System.out.print(ing.name() + "  ");
			}
			System.out.println();

			table.put(ingredients);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
			}
		}

		System.exit(1); // End program when all sandwiches have been made.
	}
}

/**
 * Chef class for chef thread.
 * @author Weihong shen
 *
 */
class Chef implements Runnable{

	private Table table;
	private Ingredient ingredient;
	private int sandwiches;
	private ArrayList<Ingredient> ingredients;
	
	/**
	 * create a new chef who has one type of ingredient.
	 * @param t table;
	 * @param i ingredient;
	 */
	
	public Chef(Table t, Ingredient i) {
		table = t;
		ingredient = i;
		sandwiches = 0;
	}
	
	/**
	 * make and eat bread.
	 */
	private void eat() {
		sandwiches++;
    	System.out.println(Thread.currentThread().getName()+ " made sandwich: " + sandwiches + " with ingredient: " + ingredient.name());
       	System.out.println("Overall sandwich number: " + table.getSandwiches() + "\n");
	}
	
	@Override
	public void run() {
		for(;;){
        	synchronized(table){
            	try{
                	while(table.isEmpty()){
                		table.wait();
                	}
                	if(!table.hasIng(ingredient)){
                		ingredients = table.get();
                		table.notifyAll();
                		eat();
                	}else{
                		table.wait();
                	}
            	}catch (InterruptedException e) {
    				e.printStackTrace();
    			}
            }
        }
	}
	
}