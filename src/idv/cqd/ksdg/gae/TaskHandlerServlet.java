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
public class TaskHandlerServlet extends HttpServlet {
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		doGet(req, resp);
	}
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		//////////////////////////////////////
		// prepare datastore for use
		//////////////////////////////////////
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();    // init datastore service
		
		//////////////////////////////////////
		// do long job
		//////////////////////////////////////
		
		Date startTime = new Date();
		
		// generate payload
		String payload = ""+Math.random();
		
		// it's been a long time...
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		Date endTime = new Date();

		//////////////////////////////////////
		// save result to datastore
		//////////////////////////////////////
		

		Entity taskRslt = new Entity("TaskRslt");
		
		taskRslt.setProperty("startTime", startTime);
		taskRslt.setProperty("endTime", endTime);
		taskRslt.setProperty("payLoad", payload);

		// store the entity
		ds.put(taskRslt);
		
	}
}
