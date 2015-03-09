package com.example.adnfield360demo.Helper;

 	
	public class ProjListView
	{
		public String proj_name;
		public String proj_id;
		private int icon;
		
		  public ProjListView(){
		        super();
		    }
		  
		public ProjListView(String _proj_name,String _proj_id,int icon ) {
	        super();
	        this.proj_name = _proj_name;
	        this.proj_id = _proj_id; 
	        this.icon = icon;
	        }

		public void setproj_name(String _proj_name) {
			this.proj_name = _proj_name;
			}
			public String getproj_name() {
			return proj_name;
			}
			
			public void setIcon(int icon) {
			this.icon = icon;
			}
			public int getIcon() {
			return icon;
			}
			public void setproj_id(String proj_id) {
			this.proj_id = proj_id;
			}
			public String getproj_id() {
			return proj_id;
			}
			
		
		@Override
	    public String toString() {
	        return this.proj_name;
	    }
	}
	
	
 
