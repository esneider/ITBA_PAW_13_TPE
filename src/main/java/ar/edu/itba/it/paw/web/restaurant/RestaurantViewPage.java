package ar.edu.itba.it.paw.web.restaurant;

import java.io.Serializable;
import java.util.List;

import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.spring.injection.annot.SpringBean;

import ar.edu.itba.it.paw.domain.EntityModel;
import ar.edu.itba.it.paw.domain.restaurant.Restaurant;
import ar.edu.itba.it.paw.domain.restaurant.RestaurantRepo;
import ar.edu.itba.it.paw.domain.restaurant.RestaurantState;
import ar.edu.itba.it.paw.domain.user.User;
import ar.edu.itba.it.paw.utils.Utils;
import ar.edu.itba.it.paw.web.application.RestaurantApplication;
import ar.edu.itba.it.paw.web.base.SideBarPage;
import ar.edu.itba.it.paw.web.user.CommentPanel;
import ar.edu.itba.it.paw.web.user.UserProfilePage;

public class RestaurantViewPage extends SideBarPage {

	private static final long serialVersionUID = 1094753744913503034L;
	@SpringBean
	private RestaurantRepo restRepo;

	private transient IModel<Restaurant> restaurantModel;

	@SuppressWarnings({"serial"})
	public RestaurantViewPage(final IModel<Restaurant> restaurantModel,
			boolean isPending) {

		super(null, false);
		setDefaultModel(new CompoundPropertyModel<Restaurant>(restaurantModel));
		this.restaurantModel = restaurantModel;
		/*
		 * Basic fields
		 */
		add(new Label("name"));
		add(new Image("imghigh", RestaurantApplication.HIGHLIGHTED_ICON)
				.setVisible(restaurantModel.getObject().isHighlighted()));
		add(new Label("address"));
		add(new Label("area"));
		add(new Label("telephone"));
		add(new Label("avgprice", new Model<String>("$"
				+ Utils.function((double) restaurantModel.getObject()
						.getAvgprice()))));
		add(new Label("timerange"));
		add(new Label("website"));
		add(new Label("avgScore"));

		add(new Label("scoredBy", new StringResourceModel("scoredBy",
				new Model<Serializable>(restaurantModel.getObject()
						.getRatingsAmmount()))));

		add(new FoodTypesPanel("foodtypesPanel", restaurantModel));

		add(new CommentPanel("commentPanel", restaurantModel));

		add(new CommentRestaurantPanel("addCommentPanel", restaurantModel));
		
		/*
		 * Adding Register user
		 */
		if (restaurantModel.getObject().getRegisterUser() != null) {
			add(new Link<User>("registerUser", new EntityModel<User>(User.class,
					restaurantModel.getObject().getRegisterUser())) {

				@Override
				public void onClick() {
					setResponsePage(new UserProfilePage(getModel()));
				}
				
				@Override
				public IModel<?> getBody() {
					return getModel();
				}
			});
		} else {
			add(new Label("registerUser", " - "));
		}

		/*
		 * Google Maps
		 */
		add(new WebMarkupContainer("googlemap")
				.add(new AttributeAppender("data-address", new Model<String>(
						restaurantModel.getObject().getAddress())))
				.add(new AttributeAppender(
						"data-description",
						new Model<String>(restaurantModel.getObject().getName())))
				.setVisible(!isPending));

		/*
		 * Recommended restaurants
		 */
		IModel<List<Restaurant>> listModel = new LoadableDetachableModel<List<Restaurant>>() {
			@Override
			protected List<Restaurant> load() {
				if (getRestaurantWicketSession().isSignedIn()) {
					return restRepo.getRecommendedRestaurants(
							restaurantModel.getObject(),
							getRestaurantWicketSession().getUser());
				} else {
					return restRepo.getRecommendedRestaurants(restaurantModel
							.getObject());
				}
			}
		};

		WebMarkupContainer a = new WebMarkupContainer("caretaFix");
		a.setVisible(!isPending);
		add(a);
		add(new SimpleRestaurantListPanel("recommended", listModel, false)
				.setVisible(!isPending));

		/*
		 * Access Restaurant ViewPage Count
		 */
		if (!isPending)
			add(new Label("accessCount"));
		else
			add(new Label("accessCount", new Model<String>("N/A")));

		/*
		 * Pending requests
		 */
		add(new AdminPendingRequestsPanel("adminPendingResquests",
				restaurantModel) {
			@Override
			public boolean isVisible() {
				if (!getRestaurantWicketSession().isSignedIn())
					return false;
				User currentUser = getRestaurantWicketSession().getUser();
				return currentUser.isAdmin()
						&& restaurantModel.getObject().getState()
								.equals(RestaurantState.Pending);
			}
		});

	}

	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);

		response.render(JavaScriptHeaderItem
				.forUrl("https://maps.google.com/maps/api/js?sensor=false"));
		response.render(JavaScriptHeaderItem
				.forReference(new JavaScriptResourceReference(
						RestaurantApplication.class, "maps.js")));
	}

	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();
		restaurantModel.getObject().setNewAccess();
	}
}
