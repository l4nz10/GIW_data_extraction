package resource;
/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.spell.Dictionary;
import org.apache.lucene.search.spell.LuceneDictionary;
import org.apache.lucene.search.spell.SpellChecker;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.html.HtmlParser;
import org.apache.tika.sax.BodyContentHandler;
import org.jsoup.Jsoup;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Index all text files under a directory.
 * This is a command-line application demonstrating simple Lucene indexing. Run
 * it with no command-line arguments for usage information.
 */
public class IndexFiles {
	
	
	private static final String INDEX_PATH = "D:\\Program Files\\Apache Software Foundation\\Tomcat 7.0\\webapps\\ProgettoGIW\\lucene\\index\\";
	private static final String DICTIONARY_PATH = "D:\\Program Files\\Apache Software Foundation\\Tomcat 7.0\\webapps\\ProgettoGIW\\lucene\\spellchecker\\";
	private static final String DOCS_PATH = "D:\\Program Files\\Apache Software Foundation\\Tomcat 7.0\\webapps\\ProgettoGIW\\docs\\";
//	private static final String INDEX_PATH = "C:\\Program Files\\Apache Software Foundation\\Tomcat 7.0\\webapps\\webapp\\lucene\\index\\";
//	private static final String DICTIONARY_PATH = "C:\\Program Files\\Apache Software Foundation\\Tomcat 7.0\\webapps\\webapp\\lucene\\spellchecker\\";
//	private static final String DOCS_PATH = "C:\\Program Files\\Apache Software Foundation\\Tomcat 7.0\\webapps\\webapp\\docs\\";
	
	private IndexFiles() {
	}

	/** Index all text files under a directory. 
	 * @throws TikaException 
	 * @throws SAXException */
	public static boolean createIndex() {
		String indexPath = INDEX_PATH;
		String docsPath = DOCS_PATH;
		boolean override = false;
		final File docDir = new File(docsPath);
		if (!docDir.exists() || !docDir.canRead()) {
			return false;
		}
		try {			
			Directory dir = FSDirectory.open(new File(indexPath));
			// :Post-Release-Update-Version.LUCENE_XY:
			Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_47);
			IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_47, analyzer);

			if (override) {
				// Create a new index in the directory, removing any
				// previously indexed documents:
				iwc.setOpenMode(OpenMode.CREATE);
			} else {
				// Add new documents to an existing index:
				iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
			}
			IndexWriter writer = new IndexWriter(dir, iwc);
			indexDocs(writer, docDir);

			// NOTE: if you want to maximize search performance,
			// you can optionally call forceMerge here. This can be
			// a terribly costly operation, so generally it's only
			// worth it when your index is relatively static (ie
			// you're done adding documents to it):
			//
			// writer.forceMerge(1);
			writer.close();
			
			createDictionary(analyzer);
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	/**
	 * Indexes the given file using the given writer, or if a directory is
	 * given, recurses over files and directories found under the given
	 * directory.
	 * 
	 * NOTE: This method indexes one document per input file. This is slow. For
	 * good throughput, put multiple documents into your input file(s). An
	 * example of this is in the benchmark module, which can create "line doc"
	 * files, one document per line, using the <a href=
	 * "../../../../../contrib-benchmark/org/apache/lucene/benchmark/byTask/tasks/WriteLineDocTask.html"
	 * >WriteLineDocTask</a>.
	 * 
	 * @param writer
	 *            Writer to the index where the given file/dir info will be
	 *            stored
	 * @param file
	 *            The file to index, or the directory to recurse into to find
	 *            files to index
	 * @throws IOException
	 *             If there is a low-level I/O error
	 * @throws TikaException 
	 * @throws SAXException 
	 */
	static void indexDocs(IndexWriter writer, File file) throws IOException {
		// do not try to index files that cannot be read
		if (file.canRead()) {
			if (file.isDirectory()) {
				String[] files = file.list();
				// an IO error could occur
				if (files != null) {
					for (int i = 0; i < files.length; i++) {
						indexDocs(writer, new File(file, files[i]));
					}
				}
			} else {
				FileInputStream fis;
				try {
					fis = new FileInputStream(file);
				} catch (FileNotFoundException fnfe) {
					// at least on windows, some temporary files raise this
					// exception with an "access denied" message
					// checking if the file can be read doesn't help
					return;
				}

				try {

					// make a new, empty document
					Document doc = new Document();

					// Add the path of the file as a field named "path". Use a
					// field that is indexed (i.e. searchable), but don't
					// tokenize
					// the field into separate words and don't index term
					// frequency
					// or positional information:
					Field pathField = new StringField("path", file.getPath(), Field.Store.YES);
					doc.add(pathField);
					Field relativePathField = new StringField("shortPath", getRelativePath(file.getPath()), Field.Store.YES);
					doc.add(relativePathField);
					// Add the last modified date of the file a field named
					// "modified".
					// Use a LongField that is indexed (i.e. efficiently
					// filterable with
					// NumericRangeFilter). This indexes to milli-second
					// resolution, which
					// is often too fine. You could instead create a number
					// based on
					// year/month/day/hour/minutes/seconds, down the resolution
					// you require.
					// For example the long value 2011021714 would mean
					// February 17, 2011, 2-3 PM.
					doc.add(new LongField("modified", file.lastModified(), Field.Store.NO));
					
					// Add the contents of the file to a field named "contents".
					// Specify a Reader,
					// so that the text of the file is tokenized and indexed,
					// but not stored.
					// Note that FileReader expects the file to be in UTF-8
					// encoding.
					// If that's not the case searching for special characters
					// will fail.
					if (file.getName().endsWith(".txt"))
						doc.add(new TextField("contents", new BufferedReader(new InputStreamReader(fis, "UTF-8"))));
					if (file.getName().endsWith(".html")) {
						doc.add(new TextField("contents", html2String(fis), Field.Store.YES));
						doc.add(new StringField("title", getHTMLTitle(file), Field.Store.YES));
					}
					if (writer.getConfig().getOpenMode() == OpenMode.CREATE) {
						// New index, so we just add the document (no old
						// document can be there):
						System.out.println("adding " + file);
						writer.addDocument(doc);
					} else {
						// Existing index (an old copy of this document may have
						// been indexed) so
						// we use updateDocument instead to replace the old one
						// matching the exact
						// path, if present:
						System.out.println("updating " + file);
						try {
							writer.updateDocument(new Term("path", file.getPath()), doc);
						} catch (Exception e) {
							// TODO 
							e.printStackTrace();
						}
					}

				} finally {
					fis.close();
				}
			}
		}
	}
	
	private static String getHTMLTitle(File file) throws IOException {
		return Jsoup.parse(file, "UTF-8","").title();
	}

	private static String getRelativePath(String path) {
		int startCut = path.indexOf("docs");
		return path.substring(startCut);
	}

	private static String html2String(FileInputStream fis) {
		BodyContentHandler handler = new BodyContentHandler();
        Metadata metadata = new Metadata();
        try {
			new HtmlParser().parse(fis, handler, metadata, new ParseContext());
		} catch (IOException | SAXException | TikaException e) {
			e.printStackTrace();
		}
        return handler.toString();
	}
	
	private static void createDictionary(Analyzer analyzer) throws IOException {
		Directory dictionaryDir = FSDirectory.open(new File(DICTIONARY_PATH)); 
		Directory indexDir = FSDirectory.open(new File(INDEX_PATH));
		
		IndexReader reader = DirectoryReader.open(indexDir);
		Dictionary dictionary = new LuceneDictionary(reader, "contents");
		IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_47, analyzer);
		
		SpellChecker spellChecker = new SpellChecker(dictionaryDir);
		spellChecker.indexDictionary(dictionary, iwc, false	);
		spellChecker.close();		
	}
}













