package xc.ssh.domain;

public class Car {
	private Long id;
	
	private String name;
	private String color;
	
	public Car(){
		System.out.println("其实是调用了无参的构造函数");
	}
	
	
	public Car(String name, String color){
		this.name=name;
		this.color=color;
	}

	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		System.out.println("调用了car的getName");
		return name;
	}
	public void setName(String name) {
		System.out.println("调用了car的setName");
		this.name = name;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}


	@Override
	public String toString() {
		return "Car [id=" + id + ", name=" + name + ", color=" + color + "]";
	}
	
	
	
}
