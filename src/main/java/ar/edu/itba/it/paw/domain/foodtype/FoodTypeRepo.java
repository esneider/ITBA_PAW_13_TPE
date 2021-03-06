package ar.edu.itba.it.paw.domain.foodtype;

import java.util.List;


public interface FoodTypeRepo {

    /**
     * Returns a list of all FoodTypes
     */
    public List<FoodType> getAll();

    /**
     * Returns a single FoodType
     */
    public FoodType get(int id);
    
    /**
     * Checks if food type's name already exists
     */
    public boolean foodTypeExists(String name);
}

