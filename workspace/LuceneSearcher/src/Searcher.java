import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.highlight.TextFragment;
import org.apache.lucene.search.highlight.TokenSources;
import org.apache.lucene.search.spell.SpellChecker;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class Searcher {

	private static final String INDEX_PATH = "C:\\index\\";
	private static final String DICTIONARY_PATH = "C:\\spellchecker\\";
	private static final float SCORE_THRESHOLD = 0.5f;
	private static final String FIELD = "contents";

	private static StandardAnalyzer analyzer;
	private static IndexReader reader;
	private static IndexSearcher searcher;
	private static QueryParser parser;

	public Searcher() throws IOException {
		reader = DirectoryReader.open(FSDirectory.open(new File(INDEX_PATH)));
		searcher = new IndexSearcher(reader);
		analyzer = new StandardAnalyzer(Version.LUCENE_47);
	}

	public static void main(String[] args) throws Exception {
		String queries = null;
		String queryString = null;
		int hitsPerPage = 10;

		reader = DirectoryReader.open(FSDirectory.open(new File(INDEX_PATH)));
		searcher = new IndexSearcher(reader);
		analyzer = new StandardAnalyzer(Version.LUCENE_47);
		parser = new QueryParser(Version.LUCENE_47, FIELD, analyzer);

		BufferedReader in = null;
		in = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));

		while (true) {
			if (queries == null && queryString == null) { // prompt the user
				System.out.println("Enter query: ");
			}

			String line = queryString != null ? queryString : in.readLine();

			if (line == null || line.length() == -1) {
				break;
			}

			line = line.trim();
			if (line.length() == 0) {
				break;
			}

			try {
				Query query = parser.parse(line);
				String readableQuery = query.toString(FIELD);
				System.out.println("Searching...");

				TopDocs results = searcher.search(query, 5 * hitsPerPage);
				ScoreDoc[] hits = results.scoreDocs;
				if (results.scoreDocs.length == 0 || results.getMaxScore() <= SCORE_THRESHOLD) {
					Query newQuery = suggestQuery(query, FIELD);
					TopDocs newResults = searcher.search(newQuery,
							5 * hitsPerPage);
					if (results.totalHits < newResults.totalHits) {
						hits = newResults.scoreDocs;
						query = newQuery;
						readableQuery = newQuery.toString(FIELD);
						
					}
				}
				
				if (hits.length == 0)
					System.out.println("No results found for:\t" + readableQuery);
				else
					System.out.println(hits.length + " results found for:\t" + readableQuery);
				
				createDocumentsResulted(hits, query);
				
				//resultsListerAndNavigator(in, hits, hitsPerPage, highlighter);

			} catch (ParseException e) {
				System.err.println("Incorrect Query");
			}
		}
	}

	private static DocumentResult[] createDocumentsResulted(ScoreDoc[] hits, Query query) throws IOException, InvalidTokenOffsetsException {
		SimpleHTMLFormatter htmlFormatter = new SimpleHTMLFormatter();
		Highlighter highlighter = new Highlighter(htmlFormatter, new QueryScorer(query));
		DocumentResult[] docResArray = new DocumentResult[hits.length];
		
		for (int i = 0; i < hits.length; i++) {
			DocumentResult docRes = new DocumentResult();
			int docId = hits[i].doc;
			Document doc = searcher.doc(docId);				
			String shortPath = doc.get("shortPath");
			if (shortPath != null) {
				docRes.setRelativePath(shortPath);
				String title = doc.get("title");
				if (title != null) {
					docRes.setTitle(title);
				}
				StringBuilder builder = new StringBuilder();
				for (TextFragment frag : getHighlights(docId, highlighter))
					if ((frag != null) && (frag.getScore() > 0)) {
						String snippet = frag.toString().replaceAll("\\s+"," ");
				        builder.append(snippet).append("...");
					}
				if (builder.length() != 0)
					docRes.setHighlights(builder.toString());
			}
			docResArray[i] = docRes;
		}
		return docResArray;
	}

	private static void resultsListerAndNavigator(BufferedReader in, ScoreDoc[] hits, int hitsPerPage, Highlighter highlighter) throws Exception {
		
		int numTotalHits = (hits == null ? 0 : hits.length);

		int start = 0;
		int end = Math.min(numTotalHits, hitsPerPage);

		while (true) {
			end = Math.min(numTotalHits, start + hitsPerPage);
			
			//Ciclo principale che raccoglie il contenuto di ogni documento e lo
			//manda in stampa. Viene usato Highlighter per mostrare snippets di testo.
			for (int i = start; i < end; i++) {
				int docId = hits[i].doc;
				Document doc = searcher.doc(docId);				
				String path = doc.get("path");
				if (path != null) {
					System.out.println((i + 1) + ". " + path);
					String title = doc.get("title");
					if (title != null) {
						System.out.print("	Title: " + doc.get("title"));
					}
					for (TextFragment frag : getHighlights(docId, highlighter)) {
						if ((frag != null) && (frag.getScore() > 0)) {
							String snippet = frag.toString().replaceAll("\\s+"," ");
					        System.out.print(snippet);
							System.out.print("... ");
						}
					}
					System.out.println();
				} else {
					System.out.println((i + 1) + ". " + "No path for this document");
				}
			}

			if (end == 0) {
				break;
			}

			if (numTotalHits >= end) {
				boolean quit = false;
				while (true) {
					System.out.print("Press ");
					if (start - hitsPerPage >= 0) {
						System.out.print("(p)revious page, ");
					}
					if (start + hitsPerPage < numTotalHits) {
						System.out.print("(n)ext page, ");
					}
					System.out.println("(q)uit or enter number to jump to a page.");

					String line = in.readLine();
					if (line.length() == 0 || line.charAt(0) == 'q') {
						quit = true;
						break;
					}
					if (line.charAt(0) == 'p') {
						start = Math.max(0, start - hitsPerPage);
						break;
					} else if (line.charAt(0) == 'n') {
						if (start + hitsPerPage < numTotalHits) {
							start += hitsPerPage;
						}
						break;
					} else {
						int page = Integer.parseInt(line);
						if ((page - 1) * hitsPerPage < numTotalHits) {
							start = (page - 1) * hitsPerPage;
							break;
						} else {
							System.out.println("No such page");
						}
					}
				}
				if (quit)
					break;
				end = Math.min(numTotalHits, start + hitsPerPage);
			}
		}
	}
	
	private static TextFragment[] getHighlights(int docId, Highlighter highlighter) throws IOException, InvalidTokenOffsetsException {
		Document doc = searcher.doc(docId);
		String documentContent = doc.get(FIELD);
		TokenStream tokenStream = TokenSources.getAnyTokenStream(searcher.getIndexReader(), docId, FIELD, analyzer);
		TextFragment[] fragments = highlighter.getBestTextFragments(tokenStream, documentContent, true, 10);
		return fragments;
	}

	// Suggest a possible more optimal query for the search,
	// trying to replace the words inside the query that are not relevant.
	private static Query suggestQuery(Query query, String field) throws ParseException, IOException {

		StringTokenizer tokenizer = new StringTokenizer(query.toString(field));
		String word;
		StringBuilder builder = new StringBuilder();
		String[] modifiers = new String[]  { "+", "-", "\"" };
		
		while (tokenizer.hasMoreTokens()) {
			Character headModifier = null;
			Character tailQuotMark = null;
			word = tokenizer.nextToken();
			if (word.equals("AND") || word.equals("OR") || word.equals("NOT")) {
				builder.append(word);
			} else {
				if (StringUtils.startsWithAny(word, modifiers)) {
					headModifier = word.charAt(0);
					word = word.substring(1, word.length());
				}
				if (word.endsWith("\"")) {
					tailQuotMark = word.charAt(word.length() - 1);
					word = word.substring(0, word.length() - 1);
				}
				word = findMoreRelevantWord(word);
				if (headModifier != null)
					builder.append(headModifier);
				builder.append(word);
				if (tailQuotMark != null)
					builder.append(tailQuotMark);
				builder.append(" ");
			}
		}
		return parser.parse(builder.toString());
	}

	// finds a possible new word more relevant than the original.
	// In case no other word seems to be 'more right', the method returns
	// the original.
	private static String findMoreRelevantWord(String word) throws IOException,
			ParseException {
		String[] suggestions = suggestWords(word, 5, 0.6f);
		if (suggestions.length != 0) {
			for (int i = 0; i < 2 && i < suggestions.length; i++) {
				String newWord = suggestions[i];
				TopDocs resOriginalWord = searcher.search(parser.parse(word),
						10);
				TopDocs resNewWord = searcher.search(parser.parse(newWord), 10);
				if (resOriginalWord.totalHits < resNewWord.totalHits)
					word = newWord;
			}
		}
		return word;
	}

	// suggest a list of words similar to the one passed as parameter.
	// Uses the basic dictionary of Lucene.
	private static String[] suggestWords(String word, int numberOfSuggestions,
			float accuracy) {
		try {
			Directory dictionaryDir = FSDirectory
					.open(new File(DICTIONARY_PATH));
			SpellChecker spellChecker = new SpellChecker(dictionaryDir);
			String[] similarWords = spellChecker.suggestSimilar(word,
					numberOfSuggestions, accuracy);
			spellChecker.close();
			return similarWords;
		} catch (final Exception e) {
			return new String[0];
		}
	}

	// simple method to navigate the results in the console.
	// Also responsible for listing the results.

}