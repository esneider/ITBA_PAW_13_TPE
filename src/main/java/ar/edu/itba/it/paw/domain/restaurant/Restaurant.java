package ar.edu.itba.it.paw.domain.restaurant;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;

import ar.edu.itba.it.paw.domain.AbstractModel;
import ar.edu.itba.it.paw.domain.foodtype.FoodType;
import ar.edu.itba.it.paw.domain.user.User;

@Entity
public class Restaurant extends AbstractModel {

	@ManyToMany
	Set<FoodType> foodtypes;

	@OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
	@Cascade(value=org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	Set<Rating> ratingsList = new HashSet<Rating>();

	@ManyToOne
	private User registerUser;

	private String name, address, area, telephone, website, timerange, state;
	private float avgprice;
	
	private Date applicationDate;

	@Transient
	private java.sql.Date SQLApplicationDate;
	
	public Restaurant() {
	}

	public Restaurant(String name, String address, String area,
			String telephone, String website, String timerange, float avgprice,
			String state, Set<FoodType> foodtypes, User user, Date appDate) {
		this.name = name;
		this.address = address;
		this.area = area;
		this.telephone = telephone;
		this.website = website;
		this.timerange = timerange;
		this.avgprice = avgprice;
		this.foodtypes = foodtypes;
		this.state = state;
		this.registerUser = user;
		this.applicationDate = appDate;
		this.SQLApplicationDate = new java.sql.Date(appDate.getTime());
	}

	public Set<FoodType> getFoodtypes() {
		return foodtypes;
	}

	public String getAddress() {
		return address;
	}

	public String getArea() {
		return area;
	}

	public String getTelephone() {
		return telephone;
	}

	public String getWebsite() {
		return website;
	}

	public String getTimerange() {
		return timerange;
	}

	public float getAvgprice() {
		return avgprice;
	}

	public String getName() {
		return name;
	}

	public Set<Rating> getRatings() {
		return ratingsList;
	}

	public User getRegisterUser() {
		return registerUser;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public void addRating(Rating r) {
		ratingsList.add(r);
	}
	
	public void removeRating(Rating r) {
		ratingsList.remove(r);
	}

	public int getRatingsAmmount() {
		return ratingsList.size();
	}
	
	public Date getApplicationDate() {
		return applicationDate;
	}

	public java.sql.Date getSQLApplicationDate() {
		return SQLApplicationDate;
	}

	public Rating getUserRating(User user) {
		for (Rating r : ratingsList) {
			if (r.getUser().equals(user)) {
				return r;
			}
		}
		return null;
	}

	public float getAvgScore() {
		float avg = 0;
		for (Rating r : ratingsList) {
			avg += r.getScore();
		}
		return ((float)Math.round((avg / ratingsList.size())*100))/100;
	}



}
