package ro.zbranca.scoring.data;

import java.sql.SQLException;

import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.connection.SimpleJDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.query.FreeformQuery;
import com.vaadin.data.util.sqlcontainer.query.TableQuery;
import com.vaadin.data.util.sqlcontainer.query.generator.filter.QueryBuilder;
import com.vaadin.data.util.sqlcontainer.query.generator.filter.StringDecorator;


public class DatabaseHelper {
    private static JDBCConnectionPool connectionPool;
    private static SQLContainer personalContainer;
    
    public static final Object[] PERSONAL_NATURAL_COL_ORDER = new Object[] {
		"email", "full_name", "role"
	};
	
	public static final String[] PERSONAL_COL_HEADERS_EN = new String[] {
		"Email", "Name", "Role"
	};
    
    
	public static JDBCConnectionPool getConnectionPool() {
		if (connectionPool == null) {
		try {
			QueryBuilder.setStringDecorator(new StringDecorator("`","`"));
			connectionPool = new SimpleJDBCConnectionPool(
					"com.mysql.jdbc.Driver",
					"jdbc:mysql://localhost:3306/scoring",
					"root", "scr3am",2,5);
			} catch(SQLException e){
			System.out.println("can't get connection container");
			}
		}
		return connectionPool;
	}   
    
    public static SQLContainer getPersonalContainer() {
		if (personalContainer == null) {
			try{
				QueryBuilder.setStringDecorator(new StringDecorator("`","`"));
				TableQuery tableQuery = new TableQuery("personal", getConnectionPool());
				tableQuery.setVersionColumn("version");
				personalContainer = new SQLContainer(tableQuery);
			} catch(SQLException e){
			System.out.println("can't get Personal container");
			}
		}
		return personalContainer;
    }
    
	public static SQLContainer getFreeFormQueryContainer(String sql) {
		QueryBuilder.setStringDecorator(new StringDecorator("`","`"));
		FreeformQuery query = new FreeformQuery(sql, getConnectionPool());
		SQLContainer freeFormQuerySQLContainer;
		try {
			freeFormQuerySQLContainer = new SQLContainer(query);
			return freeFormQuerySQLContainer;
		} catch (SQLException e) {
			System.out.println("sql error:" + sql);
			return null;
		}
	}
}
