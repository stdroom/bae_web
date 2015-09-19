package org.yyb.test;

import java.util.ArrayList;

public class ResultsBean {
	String status = "1";
	UpdateBean results = null;
	
	public ResultsBean(){
		
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public UpdateBean getResults() {
		return results;
	}


	public void setResults(UpdateBean results) {
		this.results = results;
	}

}
