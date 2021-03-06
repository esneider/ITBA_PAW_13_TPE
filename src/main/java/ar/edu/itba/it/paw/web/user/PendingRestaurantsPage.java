package ar.edu.itba.it.paw.web.user;

import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PropertyListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import ar.edu.itba.it.paw.domain.EntityModel;
import ar.edu.itba.it.paw.domain.restaurant.Restaurant;
import ar.edu.itba.it.paw.domain.restaurant.RestaurantRepo;
import ar.edu.itba.it.paw.domain.user.User;
import ar.edu.itba.it.paw.web.base.NoSideBarPage;
import ar.edu.itba.it.paw.web.restaurant.RestaurantListPage;
import ar.edu.itba.it.paw.web.restaurant.RestaurantViewPage;

public class PendingRestaurantsPage extends NoSideBarPage {

	/**
	 * 
	 */
	private static final long serialVersionUID = -651147582500585478L;
	@SpringBean
	private RestaurantRepo restRepo;

	public PendingRestaurantsPage() {
		super(true);
		if (!getRestaurantWicketSession().getUser().isAdmin())
			setResponsePage(RestaurantListPage.class);
		@SuppressWarnings("serial")
		IModel<List<Restaurant>> listModel = new LoadableDetachableModel<List<Restaurant>>() {
			@Override
			protected List<Restaurant> load() {
				List<Restaurant> l = restRepo.getPendingRestaurants();
				// Collections.sort(l, new Comparator<Restaurant>() {
				//
				// @Override
				// public int compare(Restaurant r1, Restaurant r2) {
				// return r1.getApplicationDate().compareTo(
				// r2.getApplicationDate());
				// }
				// });
				return l;
			}
		};
		add(new Label("cant", listModel.getObject().size()));
		populatePage(listModel);

	}

	private void populatePage(IModel<List<Restaurant>> listModel) {
		add(new PropertyListView<Restaurant>("restaurants", listModel) {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(final ListItem<Restaurant> item) {
				item.add(new Link<Restaurant>("link", item.getModel()) {
					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick() {
						setResponsePage(new RestaurantViewPage(item.getModel(),
								true));
					}

					
				});

				item.add(new Label("name"));
				item.add(new Label("applicationDate"));
				if (item.getModelObject().getRegisterUser() != null) {
					item.add(new Label("registerUser", new Model<String>(item
							.getModelObject().getRegisterUser().getName()
							+ " - "
							+ item.getModelObject().getRegisterUser()
									.getUsername())));
					final IModel<User> m = new EntityModel<User>(User.class,
							item.getModelObject().getRegisterUser());
					item.add(new Link<User>("ulink", m) {
						/**
						 * 
						 */
						private static final long serialVersionUID = 1L;

						@Override
						public void onClick() {
							setResponsePage(new UserProfilePage(m));
						}
					}.setDefaultModel(m));
				} else {
					item.add(new Label("registerUser", " - ")); // TODO REFACTOR
				}
			}
		});
	}
}
