package resource;
import java.util.LinkedList;
import java.util.List;


public class DocumentInfo {
	private String title,
				   relativePath,
				   highlights;
	private List<DocumentInfo> relatedDocument = new LinkedList<DocumentInfo>();
	
	public DocumentInfo(String title, String path, String highlights) {
		this.setTitle(title);
		this.setRelativePath(path);
		this.setHighlights(highlights);
	}
	
	public boolean hasSimilarDocs(){
		return !relatedDocument.isEmpty();
	}
	
	public DocumentInfo() {
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

	public List<DocumentInfo> getRelatedDocument() {
		return relatedDocument;
	}

	public void setRelatedDocument(List<DocumentInfo> relatedDocument) {
		this.relatedDocument = relatedDocument;
	}
	
}
