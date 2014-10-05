package ro.zbranca.scoring.data;

import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;


public class TestDatabase {

	public static void main(String[] args) {
		JDBCConnectionPool testPool = DatabaseHelper.getConnectionPool();
		System.out.println(testPool.toString());
		SQLContainer testContainer = DatabaseHelper.getPersonalContainer();
		System.out.println(testContainer.toString());

	}

}
