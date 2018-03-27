package xc.take.show;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lol.redScarf.base.logistics.kuaidi100.KuaiDi100ReaderExpressJson;
import lol.redScarf.base.logistics.kuaidi100.util.KuaiDi100Express;
import xc.take.util.Express;
import xc.take.util.ReaderExpressJson;

public class ShowExpress extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
/*		Map<String, List<Express>> map = ReaderExpressJson.getAll();
				System.out.println(map);
		request.setAttribute("map", map);
		request.getRequestDispatcher("/WEB-INF/index.jsp").forward(request, response);*/
		
		Map<String, List<KuaiDi100Express>> map = KuaiDi100ReaderExpressJson.getAll();
		System.out.println(map);
		request.setAttribute("map", map);
		request.getRequestDispatcher("/WEB-INF/index.jsp").forward(request, response);
	}

	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		this.doGet(request, response);
	}

}
