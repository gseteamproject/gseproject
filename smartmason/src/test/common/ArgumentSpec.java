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

package test.common;

import jade.util.leap.Serializable;

/**
   Class representing the specification for a generic argument of a 
   test group
   @author Giovanni Caire - TILAB
 */
public class ArgumentSpec implements Serializable {
	private String name;
	private String label;
	private String value;
	private String defaultValue;
	private boolean mandatory;
	
	ArgumentSpec(String n, String l, String v) {
		name = n;
		label = l;
		value = v;
		defaultValue = v;
		mandatory = false;
	}
	
	ArgumentSpec(String n, String l) {
		name = n;
		label = l;
		value = null;
		defaultValue = null;
		mandatory = true;
	}
		
	public String getName() {
		return name;
	}
	
	public String getLabel() {
		return label;
	}
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String v) {
		value = v;
	}
	
	public String getDefaultValue() {
		return defaultValue;
	}
	
	public boolean isMandatory() {
		return mandatory;
	}
}
	
