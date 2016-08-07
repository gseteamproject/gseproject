/**
 * ***************************************************************
 * JADE - Java Agent DEvelopment Framework is a framework to develop
 * multi-agent systems in compliance with the FIPA specifications.
 * Copyright (C) 2000 CSELT S.p.A.
 * 
 * GNU Lesser General Public License
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation,
 * version 2.1 of the License.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA.
 * **************************************************************
 */
package test.common.testerAgentControlOntology;

import jade.content.*;

/**
 * @author Giovanni Caire - TILAB
 */
public class ExecResult implements Concept {
	private int passed;
	private int failed;
	private int skipped;

	public ExecResult() {
	}
	
	public ExecResult(int p, int f, int s) {
		setPassed(p);
		setFailed(f);
		setSkipped(s);
	}
	
	public int getPassed() {
		return passed;
	}
	
	public void setPassed(int p) {
		passed = p;
	}
	
	public int getFailed() {
		return failed;
	}
	
	public void setFailed(int f) {
		failed = f;
	}
	
	public int getSkipped() {
		return skipped;
	}
	
	public void setSkipped(int s) {
		skipped = s;
	}
}


