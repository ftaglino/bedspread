/*
 * 	 This file is part of Bedspread, originally promoted and
 *	 developed at CNR-IASI. For more information visit:
 *	 http://leks.iasi.cnr.it/tools/bedspread
 *	     
 *	 This is free software: you can redistribute it and/or modify
 *	 it under the terms of the GNU General Public License as 
 *	 published by the Free Software Foundation, either version 3 of the 
 *	 License, or (at your option) any later version.
 *	 
 *	 This software is distributed in the hope that it will be useful,
 *	 but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *	 GNU General Public License for more details.
 * 
 *	 You should have received a copy of the GNU General Public License
 *	 along with this source.  If not, see <http://www.gnu.org/licenses/>.
 */
package it.cnr.iasi.leks.bedspread.rdf.sparqlImpl;

import java.security.SecureRandom;
import java.util.Vector;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author ftaglino
 *
 */
public class SPARQLEndpointConnector {
	private String endpointUrl = "";
	
	private static final long TIME_ELAPSED_THRESHOLD = 10000;
	
	private static final long MAX_SLEEP_TIME = 500;
	
	protected final Logger logger = LoggerFactory.getLogger(SPARQLEndpointConnector.class);
	
	private SecureRandom random;
	
	private static final Object MUTEX = new Object();

	public SPARQLEndpointConnector(String endpointUrl) {
		super();
		this.endpointUrl = endpointUrl;
	    this.random = new SecureRandom();
	}

	public String getEndpointUrl() {
		return endpointUrl;
	}

	public void setEndpointUrl(String endpointUrl) {
		this.endpointUrl = endpointUrl;
	}

	/**
	 * Execute a query on the configured Sparql endpoint
	 * @param url The address of the SPARQL endpoint
	 * @param queryString The SPARQL query
	 * @return Vector<QuerySolution> 
	 */	
	public Vector<QuerySolution> execQuery(String queryString) {	
		Vector<QuerySolution> qss = new Vector<QuerySolution>();
		//queryString = queryString.replaceAll("\n", "\\u000D");
		QueryExecution qexec = null;
		
		long ts1 = 0;
		long ts2 = 0;
		
		try {	
			Query query = QueryFactory.create(queryString);
			ts1 = System.currentTimeMillis();
			qexec = QueryExecutionFactory.sparqlService(endpointUrl, query);
			ts2 = System.currentTimeMillis();
			
			ResultSet r = this.executeQuery(qexec);
			while(r.hasNext()){
				qss.add(r.next());
			}
		}
		catch(Exception ex) {
			ts2 = System.currentTimeMillis();
			this.logger.error("{}", queryString +"\n"+ex.getMessage());
		}
		finally {
			long delta = ts2 - ts1;
			this.logQueriesWithLongTimeProcessing(delta, queryString);
			if(qexec!=null)
				qexec.close();
		}
		return qss;
	}
	
	/*
	 * This method has been introduce in order to mitigate 
	 * the simultaneous invocation of the SPARQL endpoint
	 * by that different concurrent threads 
	 */
	private ResultSet executeQuery(QueryExecution qexec) throws Exception{
		ResultSet r = null;
		synchronized (MUTEX) {
			r = qexec.execSelect();	
			
//			this.waitABit();
		}

		return r; 
	}

	private void waitABit() {
		long millis = Math.round(this.random.nextDouble() * MAX_SLEEP_TIME); 
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			this.logger.warn(e.getMessage());
		}
	}

	private void logQueriesWithLongTimeProcessing(long delta, String queryString){
		if (delta >= TIME_ELAPSED_THRESHOLD){
			this.logger.info("Time Elapsed for Query Exec: {} ms, {}", delta, queryString);
		}
	}

}
