/*
 * 	 This file is part of Bedspread, originally promoted and
 *	 developed at CNR-IASI. For more information visit:
 *	 https://github.com/IASI-LEKS/bedspread
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
package it.cnr.iasi.leks.bedspread.tests;


import org.junit.Assert;
import org.junit.Test;

import it.cnr.iasi.leks.bedspread.impl.weights.ic.EdgeWeightingFactory;
import it.cnr.iasi.leks.bedspread.impl.weights.ic.EdgeWeighting_CombIC;
import it.cnr.iasi.leks.bedspread.impl.weights.ic.EdgeWeighting_IC;
import it.cnr.iasi.leks.bedspread.impl.weights.ic.EdgeWeighting_IC_PMI;
import it.cnr.iasi.leks.bedspread.impl.weights.ic.EdgeWeighting_JointIC;
import it.cnr.iasi.leks.bedspread.rdf.impl.RDFFactory;
import it.cnr.iasi.leks.bedspread.rdf.impl.RDFTriple;
import it.cnr.iasi.leks.bedspread.rdf.sparqlImpl.DBpediaKB;
import it.cnr.iasi.leks.debspread.tests.util.weightsIC.EdgeWeighting_CombIC_Test;
import it.cnr.iasi.leks.debspread.tests.util.weightsIC.EdgeWeighting_IC_PMI_Test;
import it.cnr.iasi.leks.debspread.tests.util.weightsIC.EdgeWeighting_IC_Test;
import it.cnr.iasi.leks.debspread.tests.util.weightsIC.EdgeWeighting_JointIC_Test;

public class EdgeWeighting_ICTest {

	private static DBpediaKB kb = DBpediaKB.getInstance();
	
	private static RDFTriple edge_noLiterals = 
			new RDFTriple(
					RDFFactory.getInstance().createURI("http://dbpedia.org/resource/Barack_Obama"), 
					RDFFactory.getInstance().createURI("http://dbpedia.org/ontology/residence"), 
					RDFFactory.getInstance().createURI("http://dbpedia.org/resource/Washington,_D.C.")
					);

	@Test
	public void edgeWeight_IC() {		
		EdgeWeighting_IC ew = EdgeWeightingFactory.getInstance().getEdgeWeighting_IC(kb);
		double result = ew.computeEdgeWeight(edge_noLiterals); 
		System.out.println("EdgeWeighting_IC.computeEdgeWeight="+result);
		Assert.assertTrue(result>0);
	}

	@Test
	public void edgeWeight_jointIC(){
		EdgeWeighting_JointIC ew = EdgeWeightingFactory.getInstance().getEdgeWeighting_JointIC(kb);
		double result = ew.computeEdgeWeight(edge_noLiterals); 
		System.out.println("EdgeWeighting_JointIC.computeEdgeWeight="+result);
		Assert.assertTrue(result>0);
	}

	@Test
	public void edgeWeight_CombIC() {
		EdgeWeighting_CombIC ew = EdgeWeightingFactory.getInstance().getEdgeWeighting_CombIC(kb);
		double result = ew.computeEdgeWeight(edge_noLiterals); 
		System.out.println("EdgeWeighting_CombIC.computeEdgeWeight="+result);
		Assert.assertTrue(result>0);
	}
	
	@Test
	public void edgeWeight_ICplusPMI(){
		EdgeWeighting_IC_PMI ew = EdgeWeightingFactory.getInstance().getEdgeWeighting_IC_PMI(kb);
		double result = ew.computeEdgeWeight(edge_noLiterals); 
		System.out.println("EdgeWeighting_IC_PMI.computeEdgeWeight="+result);
		Assert.assertTrue(result>0);
	}
	
	@Test
	public void predicateProbability() {
		EdgeWeighting_IC_Test ew = new EdgeWeighting_IC_Test(kb);
		double result = ew.predicateProbability(edge_noLiterals.getTriplePredicate());
		System.out.println("EdgeWeighting_IC_Test.predicateProbability="+result);
		Assert.assertTrue(result>0);
	}
	
	@Test
	public void nodeProbability() {
		EdgeWeighting_CombIC_Test ew = new EdgeWeighting_CombIC_Test(kb);
		double result = ew.nodeProbability(edge_noLiterals.getTriplePredicate());
		System.out.println("EdgeWeighting_CombIC_Test.nodeProbability="+result);
		Assert.assertTrue(result>0);
	}

	@Test
	public void nodeProbabilityConditionalToPredicate() {
		EdgeWeighting_JointIC_Test ew = new EdgeWeighting_JointIC_Test(kb);
		double result = ew.nodeProbabilityConditionalToPredicate(edge_noLiterals.getTriplePredicate(), edge_noLiterals.getTripleObject());
		System.out.println("EdgeWeighting_JointIC_Test.nodeProbabilityConditionalToPredicate="+result);
		Assert.assertTrue(result>0);
	}

	@Test
	public void nodeAndPredicateProbability() {
		EdgeWeighting_IC_PMI_Test ew = new EdgeWeighting_IC_PMI_Test(kb);
		double result = ew.nodeAndPredicateProbability(edge_noLiterals.getTriplePredicate(), edge_noLiterals.getTripleObject());
		System.out.println("EdgeWeighting_IC_PMI_Test.nodeAndPredicateProbability="+result);
		Assert.assertTrue(result>0);
	}
	
	@Test
	public void node_IC() {
		EdgeWeighting_CombIC_Test ew = new EdgeWeighting_CombIC_Test(kb);
		double result = ew.node_IC(edge_noLiterals.getTripleObject());
		System.out.println("EdgeWeighting_CombIC_Test.node_IC="+result);
		Assert.assertTrue(result>0);
	}
	
	@Test
	public void nodeConditionalToPredicate_IC() {
		EdgeWeighting_JointIC_Test ew = new EdgeWeighting_JointIC_Test(kb);
		double result = ew.nodeConditionalToPredicate_IC(edge_noLiterals.getTriplePredicate(), edge_noLiterals.getTripleObject());
		System.out.println("EdgeWeighting_JointIC_Test.nodeConditionalToPredicate_IC="+result);
		Assert.assertTrue(result>0);
	}

	@Test
	public void pmi() {
		EdgeWeighting_IC_PMI_Test ew = new EdgeWeighting_IC_PMI_Test(kb);
		double result = ew.pmi(edge_noLiterals.getTriplePredicate(), edge_noLiterals.getTripleObject());
		System.out.println("EdgeWeighting_IC_PMI_Test.pmi="+result);
		Assert.assertTrue(result>0);
	}
	
	@Test
	public void predicate_IC() {
		EdgeWeighting_IC_Test ew = new EdgeWeighting_IC_Test(kb);
		double result = ew.predicate_IC(edge_noLiterals.getTriplePredicate());
		System.out.println("EdgeWeighting_IC_Test.predicate_IC="+result);
		Assert.assertTrue(result>0);
	}
	
}
