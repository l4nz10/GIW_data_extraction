import java.util.LinkedList;
import java.util.List;


public class DocumentResult {
	private String title,
				   relativePath,
				   highlights;
	private List<DocumentResult> relatedDocument = new LinkedList<DocumentResult>();
	
	public DocumentResult(String title, String path, String highlights) {
		this.setTitle(title);
		this.setRelativePath(path);
		this.setHighlights(highlights);
	}
	
	public boolean hasSimilarDocs(){
		return !relatedDocument.isEmpty();
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

	public List<DocumentResult> getRelatedDocument() {
		return relatedDocument;
	}

	public void setRelatedDocument(List<DocumentResult> relatedDocument) {
		this.relatedDocument = relatedDocument;
	}
	
}
