package ar.edu.itba.it.paw.domain;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;

import ar.edu.itba.it.paw.domain.restaurant.Restaurant;


@Entity
public class FoodType extends AbstractModel implements Comparable<FoodType> {

	private String name;

	@ManyToMany
	private Set<Restaurant> restaurants;
	
	public FoodType() {}

	public FoodType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	public Set<Restaurant> getRestaurants() {
		return restaurants;
	}

	public Integer getAmmount() {
		return restaurants.size();
	}
	
	@Override
	public int compareTo(FoodType other) {

		return name.compareTo(other.name);
	}
}
