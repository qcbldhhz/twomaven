package xc.ssh.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class HelloAction extends ActionSupport{

	private  String username;
	private String password;
	private String text;
	
	@Override
	public String execute() throws Exception {
		
		System.out.println("搭建好了？"+username);
		
		Map<String,Object > requestMap = (Map<String, Object>) ActionContext.getContext().get("request");
		HttpServletRequest request = ServletActionContext.getRequest();
		 /*  String x = null;
		      x = x.substring(0);*/
		
		Map<String, Object> parameters = ActionContext.getContext().getParameters();
		String object = (String) parameters.get("username");
		
	
		
		
		
		requestMap.put("username", username);
		request.setAttribute("bbb", "aaaaaa");
		return SUCCESS;
	}



	public void setUsername(String username) {
		this.username = username;
	}



	public String getPassword() {
		return password;
	}



	public void setPassword(String password) {
		this.password = password;
	}



	public String getText() {
		return text;
	}



	public void setText(String text) {
		this.text = text;
	}



	public String getUsername() {
		return username;
	}

	
	
	
}
