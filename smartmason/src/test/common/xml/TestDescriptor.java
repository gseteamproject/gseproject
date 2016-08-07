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

import jade.util.leap.HashMap;
import jade.util.leap.Serializable;

/**
 * @author Elisabetta Cortese - TiLab
 *
 */

// FOR EACH TEST
public class TestDescriptor implements Serializable {
	
	private String name = "";
	private boolean skip = false;
	private String testClass = "";
	private String what = "";
	private String how = "";
	private String passedWhen = "";
	private HashMap args = new HashMap();

	public TestDescriptor(){
	}
		
	public TestDescriptor(String n, boolean s, String c, String w, String h, String p){
		name = n;
		skip = s;
		testClass = c;
		what = w;
		how = h;
		passedWhen = p;
	}
	
	public String getName(){
		return name;
	}

	public boolean getSkip(){
		return skip;
	}
	
	public String getWhat(){
		return what;
	}

	public String getTestClassName(){
		return testClass;
	}
	
	public String getHow(){
		return how;
	}
	
	public String getPassedWhen(){
		return passedWhen;
	}

	public void setName(String n){
		name =n;
	}

	public void setSkip(boolean s){
		skip = s;
	}
	
	public void setSkip(String s){
		if(s.equalsIgnoreCase("true"))
			skip = true;
		else
			skip = false;
	}

	public void setTestClass(String c){
		testClass = c;
	}
	
	public void setWhat(String w){
		what = w;
	}
	
	public void setHow(String h){
		how = h;
	}
	
	public void setPassedWhen(String p){
		passedWhen = p;
	}

	public String getArg(String key) {
		return (String) args.get(key);
	}
	
	public void setArg(String key, String value) {
		args.put(key, value);
	}
}
