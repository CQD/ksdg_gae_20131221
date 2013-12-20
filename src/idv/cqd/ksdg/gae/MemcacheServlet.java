package idv.cqd.ksdg.gae;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

@SuppressWarnings("serial")
public class MemcacheServlet extends HttpServlet {
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		doGet(req, resp);
	}
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/html");
		PrintWriter pw = resp.getWriter();
		
		//////////////////////////////////////
		// prepare memcache for use
		//////////////////////////////////////
	    MemcacheService mc = MemcacheServiceFactory.getMemcacheService();
		
		
		//////////////////////////////////////
		// Search memcache for data if issued
		//////////////////////////////////////
		if ("search".equals(req.getParameter("search"))) {
			
			String key = req.getParameter("key");
			
			if (mc.contains(key)){
				// if key exists, display it
				String value = (String) mc.get(key);
				pw.println("<p>"+key+" found! Value is: "+value);
			} else {
				// if key not exists, show error message	
				pw.println("<p>"+key+" does not exist!");
			}
			
		}

		//////////////////////////////////////
		// insert/update data to memcache if issued
		//////////////////////////////////////
		if ("save".equals(req.getParameter("save"))) {
			String key = req.getParameter("key");
			String value = req.getParameter("val");
			mc.put(key, value);
			pw.println("<p>"+key+" updated!");
		}
		
		
		//////////////////////////////////////
		// print control form
		//////////////////////////////////////
		pw.println("<form method='post'>key:<input name='key'><br>value:<input name='val'><br><button type='submit' name='save' value='save'>Save</button><br><button type='submit' name='search' value='search'>Search</button></form>");
	}
}
