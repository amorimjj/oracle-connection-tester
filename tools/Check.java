package tools;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Check {

	private static int sleepTime = 5;
	private static int attemptsCount = 0;
	private static int maxAttempts = 30;
	private static String server = "localhost";
	private static String port = "1521";
	private static String db = "XE";
	private static String user = "sys";
	private static String password;

	private static void loadLibrary() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			System.out.println("Oracle JDBC Driver Registered!");
		} catch (ClassNotFoundException e) {
			System.out.println("Where is your Oracle JDBC Driver?");
		}
	}

	private static void sleep() {
		try {
			Thread.sleep(sleepTime * 1000);
		} catch(InterruptedException ex) {
    	Thread.currentThread().interrupt();
		}
	}

	private static Connection getConn() throws SQLException {
		String connUrl = String.format("jdbc:oracle:thin:@%s:%s:%s", server, port, db);
		return DriverManager.getConnection(connUrl, user, password);
	}

	private static boolean isUp() {

		try {
				Statement stmt = getConn().createStatement();
				stmt.executeQuery("select * from dual");
				return true;
			} catch (SQLException e) {
				attemptsCount++;
				System.out.println("Database not ready yet.");
				sleep();
			}

			if ( attemptsCount < maxAttempts )
				return isUp();

			return false;
	}

	private static void validateParam(String param) {
		if ( !Pattern.matches("(user|password|database|server|port|attempts|sleepTime)=\\w+", param) ) {
					System.out.println( "Invalid param: " + param );
					System.out.println("Valid parameters:");
					System.out.println(" user=<username>");
					System.out.println(" password=<password>");
					System.out.println(" database=<database>");
					System.out.println(" server=<server>");
					System.out.println(" port=<port>");
					System.out.println(" attempts=<attempts>");
					System.out.println(" sleepTime=<seconds>");
					System.exit(1);
		}
	}

	private static void setParam(String param) {
		Pattern pattern = Pattern.compile("(?<name>\\w+)=(?<value>\\w+)");
		Matcher matcher = pattern.matcher(param);
		matcher.find();
		String name = matcher.group("name");
		String value = matcher.group("value");

		switch (name) {
			case "user":
				user = value;
				break;
			case "password":
				password = value;
				break;
			case "server":
				server = value;
				break;
			case "port":
				port = value;
				break;
			case "database":
				db = value;
				break;
			case "attempts":
				maxAttempts = Integer.parseInt(value);
			case "sleepTime":
				sleepTime = Integer.parseInt(value);
				break;
			default:
				break;
		}
	}

	private static void getParams(String[] params) {
		for( String param : params ) {
			validateParam(param);
			setParam(param);
		}
	}

	public static void main(String[] argv) {
		getParams(argv);
		System.out.println("-------- Oracle Connection Testing ------");

		boolean isUp = isUp();

		if ( isUp ) {
			System.out.println("-------- Oracle is up and running ------");
		} else {
			System.out.println("-------- Can't connect to oracle after " +
				maxAttempts + " attempts ------");
				System.exit(1);
		}
	}
}
