package ar.edu.itba.it.paw.domain.user;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ar.edu.itba.it.paw.domain.AbstractHibernateRepo;
import ar.edu.itba.it.paw.domain.picture.PictureRepo;


@Repository
public class HibernateUserRepo extends AbstractHibernateRepo implements UserRepo {

	private PictureRepo picRepo;

    @Autowired
    public HibernateUserRepo(SessionFactory sessionFactory, PictureRepo picRepo) {

        super(sessionFactory);
        this.picRepo = picRepo;
    }

    @Override
    public User get(int userid) {

        return get(User.class, userid);
    }

    @Override
    public boolean emailExists(String email) {

        return !find("from User where email = ?", email).isEmpty();
    }

    @Override
    public boolean usernameExists(String username) {
        return !find("from User where username = ?", username).isEmpty();
    }

    @Override
    public User login(String username, String password) {

        List<User> list = find("from User where username = ? and password = ?", username, password);

        if (list.isEmpty()) {
            return null;
        }

        return list.get(0);
    }

    @Override
    public void save(User user) {
    	picRepo.save(user.getAvatar());
        super.save(user);
    }

    @Override
    public List<User> getAll() {
        return find("from User");
    }

	@Override
	public User get(String username) {
		List<User> list = find("from User where username = ?", username);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
	}

	@Override
	public User getByToken(String token) {
		List<User> list = find("from User where token = ?", token);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
	}
}

