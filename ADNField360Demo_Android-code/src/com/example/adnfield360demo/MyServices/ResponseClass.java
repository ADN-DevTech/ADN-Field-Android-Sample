package com.example.adnfield360demo.MyServices;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class ResponseClass {
	
	public class field_login_class {
		
		 @SerializedName("ticket")
		    public String ticket;

	}
	
	 public class field_project_class
	    {
		 @SerializedName("name")	          
	        public String name ;
		 
		 @SerializedName("project_id")
	        public String project_id;  
	    }
	
	 public class field_issue_class
	    {
		 @SerializedName("issue_id")	          
	        public String issue_id ;
		 
		 @SerializedName("description")
	        public String description;		 

		 @SerializedName("issue_type")
	        public String issue_type; 
		 
		 @SerializedName("issue_type_id")
	        public String issue_type_id; 
		 
		 @SerializedName("status")
	        public String status; 
		 
		 @SerializedName("company_id")
	        public String company_id;		 
		
		 @SerializedName("due_date")
	        public String due_date;  		 
		 
		 @SerializedName("created_by")
	        public String created_by;
		 
		 @SerializedName("attachments")
	        public field_attachment[] attachments;   
		 
		 @SerializedName("identifier")
	        public String identifier;    
	    }
	 
	 public class field_update_issue
	    {
		 @SerializedName("id")	          
	        public String id ;
		 
		 @SerializedName("temporary_id")
	        public String temporary_id; 
		 
		 @SerializedName("fields")
	        public List<field_update_issue_field> fields;  
		 
	    }
	 
	 public class field_update_issue_field
	    {
		 @SerializedName("id")	          
	        public String id ;
		 
		 @SerializedName("value")
	        public String value;  
		 
	    }
	 
	 public class field_update_issue_result
	    {
		 @SerializedName("success")	          
	        public String success ;
		 
		 @SerializedName("id")	          
	        public String id ; 

	    }
	 
	 public class field_company
	    {
		 @SerializedName("name")	          
	        public String name ;
		 
		 @SerializedName("id")	          
	        public String id ; 
	    }
	 
	 public class field_attachment
	    {
		 @SerializedName("id")	          
	        public String id ;
		 
		 @SerializedName("filename")	          
	        public String filename ; 
		 
		 @SerializedName("size")	          
	        public String size ;
		 
		 @SerializedName("content_type")	          
	        public String content_type ;
		 
	    }
	 
}

