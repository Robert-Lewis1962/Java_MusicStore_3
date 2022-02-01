package music.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import music.models.Product;

public class ProductIO {

	public static List<Product> getProducts() {
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection connection = pool.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;

		String query = "SELECT * FROM product";

		try {
			ps = connection.prepareStatement(query);
			rs = ps.executeQuery();
			List<Product> products = new ArrayList<Product>();

			while (rs.next()) {
				Product item = new Product(rs.getString(1), rs.getString(2), rs.getDouble(3));
				products.add(item);
			}
			return products;
		} catch (Exception e) {
			System.out.println(e);
			return null;
		} finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closePreparedStatement(ps);
			pool.freeConnection(connection);
		}
	}

	public static Product getProduct(String productCode) {
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection connection = pool.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;

		String query = "SELECT code, description, cost FROM product WHERE code = ?";

		try {
			ps = connection.prepareStatement(query);
			ps.setString(1, productCode);
			rs = ps.executeQuery();

			if (rs.next()) {
				Product item = new Product(rs.getString(1), rs.getString(2), rs.getDouble(3));
				return item;
			}
			return null;
		} catch (SQLException e) {
			System.out.println(e);
			return null;
		} finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closePreparedStatement(ps);
			pool.freeConnection(connection);
		}

	}

	public static void insertProduct(Product product) {
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection connection = pool.getConnection();
		PreparedStatement ps = null;

		String query = "INSERT INTO product (code, description, cost) VALUES (?,?,?)";

		try {
			ps = connection.prepareStatement(query);
			ps.setString(1, product.getCode());
			ps.setString(2, product.getDescription());
			ps.setDouble(3, product.getPrice());
			ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e);
		} finally {
			DBUtil.closePreparedStatement(ps);
			pool.freeConnection(connection);
		}
	}

	public static void updateProduct(Product product) {
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection connection = pool.getConnection();
		PreparedStatement ps = null;

		String query = "UPDATE product SET description = ?, cost = ? WHERE code = ?";

		try {
			ps = connection.prepareStatement(query);

			ps.setString(1, product.getDescription());
			ps.setDouble(2, product.getPrice());
			ps.setString(3, product.getCode());
			ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e);
		} finally {
			DBUtil.closePreparedStatement(ps);
			pool.freeConnection(connection);
		}
	}

	public static void deleteProduct(String code) {
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection connection = pool.getConnection();
		PreparedStatement ps = null;

		String query = "DELETE FROM product WHERE code = ?";

		try {
			ps = connection.prepareStatement(query);
			ps.setString(1, code);
			ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e);
		} finally {
			DBUtil.closePreparedStatement(ps);
			pool.freeConnection(connection);
		}
	}
}
