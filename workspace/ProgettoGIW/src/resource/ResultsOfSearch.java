package resource;

public class ResultsOfSearch {
	
	private Integer numOfResults;
	private DocumentInfo[][] docResArray;
	private String query;
	private String suggestedQuery;
	private Boolean forcedSearch;
	
	public ResultsOfSearch(String query, Boolean forced) {
		this.query = query;
		this.forcedSearch = forced;
	}
	
	public ResultsOfSearch() {
		this(null, false);
	}
	
	public boolean isForced() {
		return this.forcedSearch;
	}
	
	public boolean thereIsNoResult() {
		return (docResArray == null || docResArray[0][0] == null);				
	}
	
	public boolean isSuggestedSearch() {
		return (suggestedQuery != null);
	}
	
	public DocumentInfo[][] getDocResArray() {
		return docResArray;
	}
	
	public void setDocResArray(DocumentInfo[][] docResArray) {
		this.docResArray = docResArray;
	}
	
	public String getQuery() {
		return query;
	}
	
	public void setQuery(String query) {
		this.query = query;
	}
	
	public String getSuggestedQuery() {
		return suggestedQuery;
	}
	
	public void setSuggestedQuery(String suggestedQuery) {
		this.suggestedQuery = suggestedQuery;
	}

	public Integer getNumOfResults() {
		return numOfResults;
	}

	public void setNumOfResults(Integer numOfResults) {
		this.numOfResults = numOfResults;
	}

	public void setNumOfResults(int length) {
		// TODO Auto-generated method stub
		
	}

	public Boolean getForcedSearch() {
		return forcedSearch;
	}

	public void setForcedSearch(Boolean forcedSearch) {
		this.forcedSearch = forcedSearch;
	}
	
	
}
