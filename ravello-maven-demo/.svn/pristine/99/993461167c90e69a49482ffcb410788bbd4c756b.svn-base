package com.ravello.maven.demo;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public final class Hello extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		try {
			String dbServer = getServletConfig().getInitParameter("db.server");
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://" + dbServer + ":3306/maven_demo";
			Connection conn = DriverManager.getConnection(url, "dev",
					"ravellodev");
			Statement stmt = conn.createStatement();
			String query = "select message from demo_tbl where id =1;";
			ResultSet resultSet = stmt.executeQuery(query);
			resultSet.first();
			response.setContentType("text/html");
			PrintWriter writer = response.getWriter();
			writer.println("<html>");
			writer.println("<body><div id=testmsg><b>");
			writer.println(resultSet.getString("message"));
			writer.println("</b></div></body>");
			writer.println("</html>");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}