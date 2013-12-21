package idv.cqd.ksdg.gae;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.QueueStatistics;
import com.google.appengine.api.taskqueue.TaskOptions;

@SuppressWarnings("serial")
public class TaskQueueServlet extends HttpServlet {
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		doGet(req, resp);
	}
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/html");
		PrintWriter pw = resp.getWriter();
		
		//////////////////////////////////////
		// prepare task queue and datastore for use
		//////////////////////////////////////
		Queue queue = QueueFactory.getQueue("default");
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();    // init datastore service
		
		
		//////////////////////////////////////
		// add task to queue if issued
		//////////////////////////////////////
		if ("add".equals(req.getParameter("add"))) {
			queue.add(TaskOptions.Builder.withUrl("/_queue/addLongJob/"));
		}

		//////////////////////////////////////
		// display task result
		//////////////////////////////////////
		
		// prepare the query
		Query q = new Query("TaskRslt").addSort("startTime", SortDirection.ASCENDING);
		PreparedQuery pq = ds.prepare(q);

		// get the result and print
		for (Entity result : pq.asIterable()) {
			Date startTime = (Date) result.getProperty("startTime");
			Date endTime = (Date) result.getProperty("endTime");
			String payLoad = (String) result.getProperty("payLoad");

			pw.println("<p>Start: "+ startTime + ", end: " + endTime + ", payload:" + payLoad);
		}
		pw.println("<hr>");

		
		
		pw.println("<form method='post'>key:<button type='submit' name='add' value='add'>Add Job</button><br><button type='submit' name='refresh' value='refresh'>Refresh Page</button></form>");
	}
}
