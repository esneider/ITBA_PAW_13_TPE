package ar.edu.itba.it.paw.web.auth;

import org.apache.wicket.feedback.ContainerFeedbackMessageFilter;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import ar.edu.itba.it.paw.domain.user.User;
import ar.edu.itba.it.paw.domain.user.UserRepo;
import ar.edu.itba.it.paw.utils.Utils;
import ar.edu.itba.it.paw.web.base.NoSideBarPage;
import ar.edu.itba.it.paw.web.restaurant.RestaurantListPage;
import ar.edu.itba.it.paw.web.user.TwoPasswordPanel;

public class ResetPasswordPage extends NoSideBarPage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SpringBean
	private UserRepo userRepo;

	private transient String password;
	private transient String repassword;
	
	public ResetPasswordPage(PageParameters params) {
		super(false);
		final String token = params.get("token").toString();
		if (token == null || token.isEmpty()) {
			setResponsePage(getApplication().getHomePage());
			return;
		}
		
		if (userRepo.getByToken(token) == null) {
			setResponsePage(RestaurantListPage.class);
			return;
		}

		FeedbackPanel panel = new FeedbackPanel("resetFeedback");
		panel.setFilter(new ContainerFeedbackMessageFilter(this));

		add(panel);

		@SuppressWarnings("serial")
		Form<ResetPasswordPage> form = new Form<ResetPasswordPage>("resetForm",
				new CompoundPropertyModel<ResetPasswordPage>(this)) {
			@Override
			protected void onSubmit() {
				password = Utils.normalizeString(password);
				repassword = Utils.normalizeString(repassword);

				if (password.equals("")) {
					error(getString("emptyPassword"));
				}
				if (repassword.equals("")) {
					error(getString("emptyRePassword"));
				}
				if (!password.equals(repassword)) {
					error(getString("passwordsMismatch"));
				}

				if (!hasError()) {
					User currentUser = userRepo.getByToken(token);
					currentUser.setPassword(password);
					currentUser.clearToken();
					setResponsePage(RestaurantListPage.class);
				}

			}
		};

		form.add(new TwoPasswordPanel("passwordPanel"));
		form.add(new Button("reset", new ResourceModel("reset")));

		add(form);
	}
}
