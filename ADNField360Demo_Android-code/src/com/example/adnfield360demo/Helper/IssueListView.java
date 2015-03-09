package com.example.adnfield360demo.Helper;

public class IssueListView
{
	public String issue_desc;
	public String issue_id;
	
	  public IssueListView(){
	        super();
	    }
	  
	public IssueListView(String _issue_desc,String _issue_id ) {
        super();
        this.issue_desc = _issue_desc;
        this.issue_id = _issue_id; 
        }

	
	@Override
    public String toString() {
        return this.issue_desc;
    }
}
