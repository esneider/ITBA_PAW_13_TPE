<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
 <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<div class=restlist>
<ul>
	<c:choose>
		<c:when test="${fn:length(restaurantList) gt 0}">
			<c:forEach var="restaurant" items="${restaurantList}">
				<li> Restaurant:  <a href="view?id=${restaurant.id}">${restaurant.name}</a> 
				<br>
				Address: ${restaurant.address} <br>
				Area: ${restaurant.area} <br>
				Average Score: ${restaurant.avgScore} (Scored by ${restaurant.ratings} people) </li>
				</br> 
			</c:forEach>
		</c:when>
		<c:otherwise>
			No restaurants found
		</c:otherwise>
	</c:choose>
	

</ul
</ul>
<hr/>
<div>
