/*
 * 	 This file is part of Bedspread, originally promoted and
 *	 developed at CNR-IASI. For more information visit:
 *	 https://github.com/IASI-LEKS/bedspread
 *	     
 *	 This is free software: you can redistribute it and/or modify
 *	 it under the terms of the GNU Lesser General Public License as 
 *	 published by the Free Software Foundation, either version 3 of the 
 *	 License, or (at your option) any later version.
 *	 
 *	 This software is distributed in the hope that it will be useful,
 *	 but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *	 GNU Lesser General Public License for more details.
 * 
 *	 You should have received a copy of the GNU Lesser General Public License
 *	 along with this source.  If not, see <http://www.gnu.org/licenses/>.
 */
package it.cnr.iasi.leks.bedspread;

import java.io.IOException;
import java.io.Writer;
import java.lang.Runnable;
import java.util.Set;

import it.cnr.iasi.leks.bedspread.policies.TerminationPolicy;
import it.cnr.iasi.leks.bedspread.rdf.AnyResource;
import it.cnr.iasi.leks.bedspread.rdf.KnowledgeBase;
import it.cnr.iasi.leks.bedspread.util.SetOfNodesFactory;

/**
 * 
 * @author gulyx
 *
 */
public abstract class AbstractSemanticSpread implements Runnable{
	private Node origin;
	private TerminationPolicy term;
	private ComputationStatus status;
	
	private Set<Node> activatedNodes;
	private Set<Node> currentlyActiveNodes;
	private Set<Node> forthcomingActiveNodes;	
	private Set<Node> justProcessedForthcomingActiveNodes;
	
	private SetOfNodesFactory setOfNodesFactory;
	
	protected KnowledgeBase kb;

	public AbstractSemanticSpread(Node origin, KnowledgeBase kb, TerminationPolicy term){
		this.origin = origin;
		this.origin.updateScore(1);
		
		this.kb = kb;
		this.term = term;
		this.status = ComputationStatus.NotStarted;
		
		this.setOfNodesFactory = SetOfNodesFactory.getInstance();		

		this.currentlyActiveNodes = this.setOfNodesFactory.getSetOfNodesInstance();		
		this.activatedNodes = this.setOfNodesFactory.getNaviteSetOfNodesInstance();
		this.forthcomingActiveNodes = this.setOfNodesFactory.getSetOfNodesInstance();
		this.justProcessedForthcomingActiveNodes = this.setOfNodesFactory.getSetOfNodesInstance();		
	}
		
	public Node getOrigin(){
		return this.origin;
	}
	
	public ComputationStatus getComputationStatus() {
		return this.status;
	}
	
	private void refreshInternalState(){
		this.currentlyActiveNodes.clear();		
		this.activatedNodes.clear();
		this.forthcomingActiveNodes.clear();
		this.justProcessedForthcomingActiveNodes.clear();

		this.currentlyActiveNodes.add(this.origin);
	}
	
	public void run (){
		this.status = ComputationStatus.Running;
		this.refreshInternalState();
		while (!term.wasMet()){
			this.justProcessedForthcomingActiveNodes.clear();
			for (Node node : currentlyActiveNodes) {
				this.extractForthcomingActiveNodes(node);
				
				for (Node newNode : this.forthcomingActiveNodes) {
					Double newScore = this.computeScore(node, newNode);
					newNode.updateScore(newScore);
// Note that elements already present are not doubled in "tempActiveNodes" according to : java.util.Set				
					this.justProcessedForthcomingActiveNodes.add(newNode);
				}
			}
			
			for (Node node : this.currentlyActiveNodes) {
				this.activatedNodes.add(node);
			}						
			this.currentlyActiveNodes.clear();			
			for (Node tmpNode : this.justProcessedForthcomingActiveNodes) {
				this.currentlyActiveNodes.add(tmpNode);
			}
			this.filterCurrenltyActiveNode();
		}
		this.status = ComputationStatus.Completed;
	}
	
	private void extractForthcomingActiveNodes(Node node) {
		this.forthcomingActiveNodes.clear();
		for (AnyResource neighbor : this.kb.getNeighborhood(node.getResource())) {
			Node neighborNode = new Node(neighbor);
			if ((! this.activatedNodes.contains(neighborNode)) && (! this.currentlyActiveNodes.contains(neighborNode))){
// Note that elements already present are not doubled in "forthcomingActiveNodes" according to : java.util.Set				
				this.forthcomingActiveNodes.add(neighborNode);
			}	
		}
		
	}

	protected Set<Node> getActiveNodes(){
		Set<Node> n = this.setOfNodesFactory.getSetOfNodesInstance();
		n.addAll(this.activatedNodes);
		n.addAll(this.currentlyActiveNodes);
		
		return n;
	}

	protected Set<Node> getAllActiveNodes(){
		Set<Node> n = this.getActiveNodes();
		
		n.addAll(this.justProcessedForthcomingActiveNodes);
		
		return n;
	}
	
	protected abstract double computeScore(Node spreadingNode, Node targetNode); 
	protected abstract void filterCurrenltyActiveNode();

	
	public abstract void flushData (Writer out) throws IOException; 
}