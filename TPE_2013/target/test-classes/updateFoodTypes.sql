UPDATE foodtypes SET ammount = (SELECT COUNT(*) FROM restaurants WHERE restaurants.foodTypeId = foodtypes.id);