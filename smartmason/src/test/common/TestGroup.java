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

import jade.core.Agent;
import jade.util.leap.*;
import test.common.xml.*;

/**
 Class representing a group of tests (often related to a given 
 functionality) that have to be executed in sequence and 
 require similar configuration, initialization and clean-up operations.
 A <code>TestGroup</code> is executed by a <code>TesterAgent</code>.
 @see test.common.TesterAgent
 @see test.common.Test
 @author Giovanni Caire - TILAB
 @version $Date: December 2003
 */
public class TestGroup implements Serializable{
	private TestDescriptor[] myTests;
	private int cnt;
	private List argumentSpecs = new ArrayList();
	private HashMap args = new HashMap();
	
	/**
	 Creates a <code>TestGroup</code> object including the tests specified
	 in the <code>filename</code> file.
	 */
	public TestGroup(String filename) {
		myTests = XMLManager.getTests(filename);		
		cnt = 0;
	}
	
	/**
	 Specify an optional argument for this TestGroup. If the user
	 does not provide a value for this argument, the default value
	 will be used.
	 */
	public void specifyArgument(String name, String label, String defaultVal) {
		ArgumentSpec a = new ArgumentSpec(name, label, defaultVal);
		a.setValue(defaultVal);
		argumentSpecs.add(a);
	}
	
	/**
	 Specify mandatory argument for this TestGroup. The user will 
	 always have to provide a value for this argument.
	 */
	public void specifyArgument(String name, String label) {
		ArgumentSpec a = new ArgumentSpec(name, label);
		argumentSpecs.add(a);
	}
	
	/**
	 This method retrieves an argument specified at the group level.
	 Developers may use it from the <code>initialize()<code> method
	 to retrieve arguments inserted through 
	 the InsertArgumentsDlg dialog window.
	 */
	protected Object getArgument(String name) {
		return args.get(name);
	}
	
	/**
	 Store a group argument so that it will be accessible by 
	 tests in the group by means of the <code>getGroupArgument()</code>
	 method of the <code>Test</code> class.
	 @see test.common.Test#getGroupArgument(String)
	 */
	protected Object setArgument(String name, Object val) {
		return args.put(name, val);
	}
	
	/**
	 The developer should override this method to perform initializations
	 common to all tests in the group
	 */
	protected void initialize(Agent a) throws TestException {
	}
	
	/**
	 The developer should override this method to perform cleanings
	 common to all tests in the group
	 */
	protected void shutdown(Agent a) {
	}
	
	/**
	 Only called by the TestGroupExecutor to set which tests to execute and
	 which one to skip
	 */
	TestDescriptor[] getDescriptors() {
		return myTests;
	}
	
	/**
	 Only called by the TestGroupExecutor to get the next test to execute
	 */
	Test next() throws SkippedException {		
		String className = null;
		TestDescriptor dsc = null;
		try {
			dsc = myTests[cnt++];
			if (!dsc.getSkip()) {
				Test t = (Test) Class.forName(dsc.getTestClassName()).newInstance();
				t.setGroup(this);
				t.setDescriptor(dsc);
				return t;
			}
			else {
				throw new SkippedException(dsc);
			}
		}
		catch (IndexOutOfBoundsException ioobe) {
			return null;
		}
		catch (SkippedException se) {
			// Re-throw it
			throw se;
		}
		catch (Exception e) {
			throw new SkippedException(dsc, "Can't instantiate test "+dsc.getName()+" ["+e+"]");
		}
	}
	
	/**
	 Only called by the TesterAgent to know what arguments the user 
	 must/can input
	 */
	List getArgumentsSpecification() {
		return argumentSpecs; 
	}
	
	/**
	 Only called by the TesterAgent to set the arguments inputed by 
	 the user
	 */
	void setArguments(List aa) {
		if (aa != null) {
			Iterator it = aa.iterator();
			while (it.hasNext()) {
				ArgumentSpec a = (ArgumentSpec) it.next();
				args.put(a.getName(), a.getValue());
			}
		}
	}
	
	/** 
	 Bring the test group back to the beginning. Only called by
	 the TesterAgent.
	 */
	void reset() {
		cnt = 0;
	}
	
	/** 
	 Return the number of tests in the TestGroup
	 */
	public int size() {
		return myTests.length;  
	}        
}
