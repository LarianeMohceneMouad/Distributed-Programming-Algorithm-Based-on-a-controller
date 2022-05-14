import jade.core.AID;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.FSMBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.lang.acl.ACLMessage;

public class Site extends Agent{
	String etat = "dehors";
	String nomControleur;
	boolean permission_acquise=false ;
	public void setup() {
		System.out.println("Agent "+getLocalName());
		Object [] args = getArguments();
		if (args != null)
			nomControleur = args[0].toString();
		FSMBehaviour fsm = new FSMBehaviour(this);
		fsm.registerFirstState(new liberer(), "dehors");
		fsm.registerState(new acquirir(), "demandeur");
		fsm.registerState(new enSC(), "dedans");
		fsm.registerTransition("dehors","dehors", 0);
		fsm.registerTransition("dehors","demandeur", 1);
		fsm.registerTransition("demandeur","demandeur", 0);
		fsm.registerTransition("demandeur","dedans", 1);
		fsm.registerDefaultTransition("dedans", "dehors");
		ParallelBehaviour parallel = new ParallelBehaviour(ParallelBehaviour.WHEN_ALL);
		parallel.addSubBehaviour(fsm);
		parallel.addSubBehaviour(new consulterBoite());

		addBehaviour(parallel);
	}
	public class acquirir extends OneShotBehaviour{
		int valTransition ;
		public void action() {
			if (etat.equals("dehors")) {
				etat = "demandeur";
				ACLMessage msgEnvoi = new ACLMessage(ACLMessage.INFORM);
				msgEnvoi.addReceiver(new AID(nomControleur , AID.ISLOCALNAME));
				msgEnvoi.setContent("requete");
				System.out.println("je suis "+getLocalName()+" et j'ai envoyer une "+msgEnvoi.getContent()+" a "+nomControleur);
				send(msgEnvoi);
				valTransition = 0;
			}
			if (permission_acquise == true) {
				valTransition =1;
				etat = "dedans";
			}	
		}
		public int onEnd() {
			return valTransition ;
		}	
	}
	public class liberer extends OneShotBehaviour {
		public void action() {
			if(etat.equals("dedans")) {
				etat = "dehors";
				ACLMessage msgEnvoi = new ACLMessage(ACLMessage.INFORM);
				msgEnvoi.addReceiver(new AID(nomControleur , AID.ISLOCALNAME));
				msgEnvoi.setContent("retour permission");
				send(msgEnvoi);
				permission_acquise = false ;
			}
		}
		public int onEnd() {
			int valTransition = (int) (Math.random() * 2);
			return valTransition ;
		}
	}
	public class enSC extends OneShotBehaviour {
		public void action() {
			for(int i=0;i<5;i++)
				System.out.println("Agent "+getLocalName()+" je suis en SC ");
			block((int)(Math.random() * 1000)); 
		}	
	}
	public class consulterBoite extends CyclicBehaviour{
		public void action() {
			ACLMessage msgRecu = receive();
			if (msgRecu != null) {
				String msgContenu = msgRecu.getContent();
				if(msgContenu.equals("permission")) {
					permission_acquise = true ;
					System.out.println("je suis  "+getLocalName()+" j'ai recu une "+msgContenu+" de la part de "+msgRecu.getSender().getLocalName());

				}
			}
		}

	}
}
