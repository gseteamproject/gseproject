package examples;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class PongAgent extends Agent {
	private static final long serialVersionUID = 3663966406239393054L;

	protected void setup() {
		addBehaviour(new CyclicBehaviour(this) {
			private static final long serialVersionUID = -1912882200351395625L;

			public void action() {
				ACLMessage msg = receive();
				if (msg != null) {
					// вывод на экран локального имени агента и полученного
					// сообщения
					System.out.println(" - " + myAgent.getLocalName() + msg.getContent());
					ACLMessage reply = msg.createReply();
					reply.setPerformative(ACLMessage.INFORM); // устанавливаем
																// перформатив
																// сообщения
					reply.setContent("Pong"); // содержимое сообщения
					send(reply); // отправляем сообщение
				}
				block();
			}
		});
	}
}