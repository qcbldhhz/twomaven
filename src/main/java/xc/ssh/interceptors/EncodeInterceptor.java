package xc.ssh.interceptors;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class EncodeInterceptor extends  AbstractInterceptor {
	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		
		HttpServletRequest request = ServletActionContext.getRequest();
		Map<String, String[]> parameterMap = request.getParameterMap();
		
		for (String []  strArr  : parameterMap.values()) {
		/*	for (String str : strArr) {
				str= new String(str.getBytes("ISO-8859-1"),"UTF-8");  
				System.out.println(str);
			}*/
			for(int i=0;i<strArr.length;i++){
				strArr[i]=new String(strArr[i].getBytes("ISO-8859-1"),"UTF-8");  
			}
		}

		String invoke = invocation.invoke();
		System.out.println("拦截器返回的是什么？"+invoke);
		return invoke;
	}

}
