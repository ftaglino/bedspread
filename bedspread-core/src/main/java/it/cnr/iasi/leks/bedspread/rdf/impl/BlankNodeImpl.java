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
package it.cnr.iasi.leks.bedspread.rdf.impl;

import java.math.BigInteger;
import java.security.SecureRandom;

import it.cnr.iasi.leks.bedspread.rdf.BlankNode;

public class BlankNodeImpl implements BlankNode {

	private String id;
	
	public BlankNodeImpl (){
		this.id = this.randomId();
	}

	public BlankNodeImpl (String id){
		this.id = id;
	}

	public String getResourceID() {
		return this.id;
	}
	
    private String randomId() {
    	SecureRandom random = new SecureRandom();
    	return new BigInteger(130, random).toString(32);
    }

}
