package examples.timeServer;

import java.util.Date;

import jade.content.AgentAction;

public class SetTime implements AgentAction{
	private static final long serialVersionUID = 2061570814868514924L;
	private Date time;

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}
}
