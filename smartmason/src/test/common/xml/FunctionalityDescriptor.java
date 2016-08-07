/*****************************************************************
JADE - Java Agent DEvelopment Framework is a framework to develop 
multi-agent systems in compliance with the FIPA specifications.
Copyright (C) 2000 CSELT S.p.A. 

GNU Lesser General Public License

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation, 
version 2.1 of the License. 

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the
Free Software Foundation, Inc., 59 Temple Place - Suite 330,
Boston, MA  02111-1307, USA.
*****************************************************************/

package test.common.xml;

import jade.util.leap.Serializable;

/**
 * @author Elisabetta Cortese - TiLab
 *
 */
public class FunctionalityDescriptor  implements Serializable {

	private String name = "";
	private String testerClassName ="";
	private String testsListFile;
	private String description ="";
	private boolean skip = false;
	
	/**
	 * Constructor for FunctionalityDescriptor.
	 */
	public FunctionalityDescriptor(String n, boolean s, String c, String d) {
		name = n;
		skip = s;
		testerClassName = c;
		description = d;
	}

	public FunctionalityDescriptor() {
	}
	
	public String getName(){
		return name;
	}

	public boolean getSkip(){
		return skip;
	}
	
	public String getTesterClassName(){
		return testerClassName;
	}

	public String getTestsListFile(){
		return testsListFile;
	}

	public String getDescription(){
		return description;
	}

	public void setName(String n){
		name = n;
	}

	public void setSkip(String s){
		if(s.equalsIgnoreCase("true"))
			skip = true;
		else
			skip = false;
	}
	
	public void setTesterClassName(String c){
		testerClassName = c;
	}

	public void setTestsListFile(String testsListFile){
		this.testsListFile = testsListFile;
	}


	public void setDescription(String d){
		description = d;
	}

	public String toString() {
		return name;
	}
	
	public boolean equals(Object obj) {
		if (obj instanceof FunctionalityDescriptor) {
			return name.equals(((FunctionalityDescriptor) obj).name);
		}
		else {
			return false;
		}
	}
	
	public int hashCode() {
		return name.hashCode();
	}
}
