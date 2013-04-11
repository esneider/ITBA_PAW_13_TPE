<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<div class=restdetail>
		<h2>${restaurant.name}</h2>
	<ul>
		<li><strong>Direccion:</strong> ${restaurant.address}</li>
		<li><strong>Area:</strong> ${restaurant.area}</li>
		<li><strong>Telefono:</strong> ${restaurant.telephone}</li>
		<br>
		<li><strong>Precio promedio:</strong> ${restaurant.avgprice}</li>
		<li><strong>Tipo de comida:</strong> ${restaurant.foodtype.name}</li>
		<li><strong>Rango de horarios:</strong> ${restaurant.timerange}</li>
		<li><strong>Pagina web:</strong> ${restaurant.website}</li>
		<br>
		<li><strong>Puntaje Promedio:</strong> ${restaurant.avgscore}</li>
		<li><strong>Cantidad de puntuaciones:</strong> ${restaurant.ratings}</li>
	</ul>
	<hr/>
	<h4>Comment this restaurant:</h4>
		<form action='view' method='post'>
			Rating:<br>
			1<input type="radio" name="restaurant_rating" value="1"/>
			2<input type="radio" name="restaurant_rating" value="2"/>
			3<input type="radio" name="restaurant_rating" value="3"/>
			4<input type="radio" name="restaurant_rating" value="4"/>
			5<input type="radio" name="restaurant_rating" value="5"/>
			<br><br>
			Comment:<br>
			<textarea rows="3" cols="50" name="comment"></textarea>
			<br><br>
			<input type='hidden' name='id' value='${restaurant.id}'/>
			<input type='submit' value='Send'/>
		</form>
	<hr/>
<div>