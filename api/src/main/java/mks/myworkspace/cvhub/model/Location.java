package mks.myworkspace.cvhub.model;
public class Location {
    private String name;
    private int code;
    private String division_type;
	public Location(String name, int code, String division_type) {
		super();
		this.name = name;
		this.code = code;
		this.division_type = division_type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getDivision_type() {
		return division_type;
	}
	public void setDivision_type(String division_type) {
		this.division_type = division_type;
	}
    
}