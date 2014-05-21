package helper;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.util.Version;

public class SearchHelper implements Helper {
	public boolean convalida(String query){
		if (query.equals(""))
			return false;
		QueryParser parser = new QueryParser(Version.LUCENE_47, "",new StandardAnalyzer(Version.LUCENE_47));
		try{
			parser.parse(query);
		}catch (Exception e){
			return false;
		}
		return true;
	}

}
