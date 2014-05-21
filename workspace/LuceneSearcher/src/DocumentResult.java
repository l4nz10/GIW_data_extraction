
public class DocumentResult {
	private String title,
				   relativePath,
				   highlights;
	
	public DocumentResult(String title, String path, String highlights) {
		this.setTitle(title);
		this.setRelativePath(path);
		this.setHighlights(highlights);
	}
	
	public DocumentResult() {
		this(null, null, null);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getRelativePath() {
		return relativePath;
	}

	public void setRelativePath(String relativePath) {
		this.relativePath = relativePath;
	}

	public String getHighlights() {
		return highlights;
	}

	public void setHighlights(String highlights) {
		this.highlights = highlights;
	}
	
}
