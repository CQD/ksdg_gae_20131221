package idv.cqd.ksdg.gae;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.SortDirection;

@SuppressWarnings("serial")
public class DatastoreServlet extends HttpServlet {
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		doGet(req, resp);
	}
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/html");
		PrintWriter pw = resp.getWriter();
		
		//////////////////////////////////////
		// prepare datastore for use
		//////////////////////////////////////
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();    // init datastore service
		
		
		//////////////////////////////////////
		// delete all data from datastore if issued
		//////////////////////////////////////
		if ("del".equals(req.getParameter("del"))) {
			
			// get all message keys
			Query q = new Query("Message").setKeysOnly();
			PreparedQuery pq = ds.prepare(q);
			ArrayList<Key> keys = new ArrayList<Key>();

			for (Entity result : pq.asIterable()) {
				keys.add(result.getKey());
			}
			
			// ask datastore to delete entities
			ds.delete(keys);
			
		}

		//////////////////////////////////////
		// insert data to datastore if issued
		//////////////////////////////////////
		if ("create".equals(req.getParameter("create"))) {
			
			// combine entity
			Entity msg = new Entity("Message");
			
			msg.setProperty("title", req.getParameter("title"));
			msg.setProperty("content", req.getParameter("content"));
			msg.setProperty("author", req.getParameter("author"));
			msg.setProperty("createTime", System.currentTimeMillis());

			// store the entity
			ds.put(msg);
			
		}
		
		//////////////////////////////////////
		// display recent data from datastore
		//////////////////////////////////////
		
		// make filter (WHERE clause for SQL)
		Filter filter = new FilterPredicate(
				"createTime",
                FilterOperator.GREATER_THAN,
                System.currentTimeMillis() - (15*60*1000) ); // only query data within 15 min
		
		// prepare the query
		Query q = new Query("Message").setFilter(filter).addSort("createTime", SortDirection.ASCENDING);
		PreparedQuery pq = ds.prepare(q);

		// get the result and print
		for (Entity result : pq.asIterable()) {
			String title = (String) result.getProperty("title");
			String content = (String) result.getProperty("content");
			String author = (String) result.getProperty("author");

			pw.println("<p>["+ title + "]<br>" + content + "<br> - by " + author);
		}
		pw.println("<hr>");

		
		
		//////////////////////////////////////
		// print control form
		//////////////////////////////////////
		
		pw.println("<form method='post'>title:<input name='title'><br>content:<input name='content'><br>author:<input name='author'><br><button type='submit' name='create' value='create'>Create</button></form>");
		pw.println("<form method='post'><button type='submit' name='del' value='del'>Delete All</button></form>");
	}
}
