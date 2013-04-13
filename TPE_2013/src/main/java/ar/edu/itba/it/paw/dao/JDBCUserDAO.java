package ar.edu.itba.it.paw.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import ar.edu.itba.it.paw.dao.interfaces.UserDAO;
import ar.edu.itba.it.paw.model.User;

public class JDBCUserDAO extends AbstractDAO implements UserDAO {

	private static UserDAO self = null;

	public synchronized static UserDAO getInstance() {

		if (self == null) {

			self = new JDBCUserDAO();
		}

		return self;
	}

	public User login(String username, String password) {

		ResultSet rs = executeQuery(
				"SELECT * FROM users WHERE username = ? AND password = ?",
				username, password);

		try {
			if (!rs.next()) {
				rs.close();
				return null;
			}
			User u = newUser(rs);
			rs.close();
			return u;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public User register(User user) {

		execute("INSERT INTO users (name, surname, mail, username, password) VALUES (?, ?, ?, ?, ?)",
				user.getName(), user.getSurname(), user.getMail(), user.getUsername(), user.getPassword());

		return login(user.getUsername(), user.getPassword());
	}
	
	@Override
	public boolean usernameExists(String username) {

		ResultSet rs = executeQuery("SELECT id FROM users WHERE username = ?", username);

		try {
			if (!rs.next()) {
				rs.close();
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	private User newUser(ResultSet rs) {

		try {
			return new User(rs.getInt("id"), rs.getString("name"),
					rs.getString("surname"), rs.getString("mail"),
					rs.getString("username"), rs.getString("password"));
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public User getSingleUser(int id) {
		ResultSet rs = executeQuery("SELECT * FROM users WHERE id = ?", id);
		try {
			rs.next();
			User u = newUser(rs);
			rs.close();
			return u;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public void update(User user) {

		executeUpdate("UPDATE users SET name = ?, surname = ?, mail = ?, username = ?, password = ?"
				+ "WHERE id = ?", user.getName(), user.getSurname(), user.getMail(), user.getUsername(),
				user.getPassword(), user.getId());
	}
}
