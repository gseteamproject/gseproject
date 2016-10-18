package gseproject.experiments;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;

public class TestBlockRobot extends TestAgent{

    /**
     * 
     */
    private static final long serialVersionUID = 3782358699980760583L;

   public TestBlockRobot(){
       this.serviceName = "transporter";
       this.serviceType = "block";
   }
}
