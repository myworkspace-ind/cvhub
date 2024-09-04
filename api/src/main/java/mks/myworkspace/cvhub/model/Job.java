package mks.myworkspace.cvhub.model;
public class Job {
        private Long id;
        private String title;
        private Location location;
        private String industry;
		public Job(Long id, String title, Location location, String industry) {
			super();
			this.id = id;
			this.title = title;
			this.location = location;
			this.industry = industry;
		}
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public Location getLocation() {
			return location;
		}
		public void setLocation(Location location) {
			this.location = location;
		}
		public String getIndustry() {
			return industry;
		}
		public void setIndustry(String industry) {
			this.industry = industry;
		}
        
        
}

