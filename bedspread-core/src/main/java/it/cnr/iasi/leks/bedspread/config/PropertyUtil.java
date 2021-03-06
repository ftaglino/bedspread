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
package it.cnr.iasi.leks.bedspread.config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 
 * @author gulyx
 *
 */
public class PropertyUtil {

	protected static final Object MUTEX = new Object();
	
	protected static PropertyUtil INSTANCE = null;
	
	private static final String DEFAULT_CONFIG_FILE = "config.properties";

	public static final String KB_ENDPOINT_DEFAULT = "http://dbpedia.org/sparql";
	public static final String KB_ENDPOINT_GRAPH_DEFAULT = "http://dbpedia.org";	
	public static final String DEFAULT_KB_PREDICATES_BLACKLIST_FILE = "configDBpediaKB_predicatesBlackList.txt";
	public static final String KB_PREDICATES_BLACKLIST_FILE = "dbpedia.predicates.blackList";
	
	public static final String INTERACTION_PROTOCOL_ERROR_MESSAGE = "Error : Processing was not completed yet!!";
	
	public static final String CONFIG_FILE_LOCATION_LABEL = "bedspread.config.file.location";
	public static final String KB_LABEL =  "bedspread.kb.class";
	public static final String KB_FILE_LABEL =  "bedspread.kb.file";
	public static final String KB_ENDPOINT_LABEL =  "bedspread.kb.endpoint";
	public static final String KB_ENDPOINT_GRAPH_LABEL =  "bedspread.kb.endpoint.graph";
	public static final String SEMANTIC_SPREAD_LABEL =  "bedspread.semanticspread.class";
//	public static final String TERMINATION_POLICY_LABEL =  "bedspread.terminationpolicy.class";
	public static final String EXECUTION_POLICY_LABEL =  "bedspread.executionpolicy.class";
	public static final String EXECUTION_POLICY_NQUERIES_LABEL =  "bedspread.executionpolicy.nqueries";
	public static final String EXECUTION_POLICY_NODE_DEGREE_BOUND_LABEL =  "bedspread.executionpolicy.nodedegreebound";
	public static final String EXECUTION_POLICY_SCORE_THRESHOLD_LABEL =  "bedspread.executionpolicy.scorethreshold";
	public static final String WEIGHTING_FUNCTION_LABEL =  "bedspread.weightingfunction.class";
	public static final String WEIGHTING_FUNCTOIN_POWER_LABEL = "bedspread.weightingfunction.power";
	public static final String EDGE_WEIGHTING_IC_LABEL = "bedspread.edgeweightingic.class";
	public static final String EDGE_WEIGHTING_IC_MAX_WEIGHT_LABEL = "bedspread.edgeweightingic.maxweight";
	
	public static final String POLICENTRIC_SEMANTIC_SPREAD_SLEEP_LABEL = "bedspread.policentricsemanticspread.sleeptime";
		
	public static final String MAX_CONCURRENT_SPARQL_THREAD_LABEL = "bedspread.sparqlthread.max";

	private Properties properties;
	
	protected PropertyUtil(){
		this.properties = new Properties();
		try {
			String configFileLocation = System.getProperty(CONFIG_FILE_LOCATION_LABEL, DEFAULT_CONFIG_FILE);
			
			ClassLoader classloader = Thread.currentThread().getContextClassLoader();
			InputStream s = classloader.getResourceAsStream(configFileLocation);			
			
			this.properties.load(s);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static PropertyUtil getInstance(){
		synchronized (MUTEX) {
			if ( INSTANCE == null ){
				INSTANCE = new PropertyUtil();
			}			
		}
		return INSTANCE;
	}
	
	public String getProperty(String key){
		  return this.properties.getProperty(key);
	  }

	  public String getProperty(String key, String defalutValue){
		  return this.properties.getProperty(key, defalutValue);
	  }
 

	  public int getProperty(String key, int defalutValue){
		  String v = this.properties.getProperty(key);
		  if (v == null){
			  return defalutValue;
		  }
		  return Integer.parseInt(v);
	  }

	  public double getProperty(String key, double defalutValue){
		  String v = this.properties.getProperty(key);
		  if (v == null){
			  return defalutValue;
		  }
		  return Double.parseDouble(v);
	  }

}
