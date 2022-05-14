
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.util.leap.ArrayList;

public class Controleur extends Agent{
	boolean SC_libre = true ;
	ArrayList file_requetes = new ArrayList();
	public void setup() {
		System.out.println(" Agent "+getLocalName());
		addBehaviour(new consulterBoite());	
	}

	public class consulterBoite extends CyclicBehaviour{
		public void action() {
			ACLMessage msgRecu = receive();
			if(msgRecu != null) {
				String msgContenu = msgRecu.getContent();
				if (msgContenu.equals("requete")) {
					System.out.println("le controleur a reçu une "+msgContenu+" de la part de "+msgRecu.getSender().getLocalName() );
					file_requetes.add(msgRecu.getSender().getLocalName());
					if (SC_libre == true ) {
						String nomSite = (String) file_requetes.get(0);
						file_requetes.remove(0);
						ACLMessage msgEnvoi = new ACLMessage(ACLMessage.INFORM);
						msgEnvoi.addReceiver(new AID(nomSite , AID.ISLOCALNAME));
						msgEnvoi.setContent("permission");
						System.out.println("le controleur "+getLocalName()+" a envoyer une "+msgEnvoi.getContent()+" a "+nomSite);
						send(msgEnvoi);
						SC_libre = false ;	
					}	
				}
				else {
					if (msgContenu.equals("retour permission")) {
						System.out.println("le controleur a recu une "+msgContenu+" de la part de "+msgRecu.getSender().getLocalName());
						SC_libre = true ;
						if (file_requetes.size() != 0) {
							String nomSite = (String)file_requetes.get(0);
							file_requetes.remove(0);
							ACLMessage msgEnvoi = new ACLMessage (ACLMessage.INFORM);
							msgEnvoi.addReceiver(new AID(nomSite, AID.ISLOCALNAME));
							msgEnvoi.setContent("permission");
							System.out.println("le controleur a envoyer une "+msgContenu+" a "+nomSite);
							send(msgEnvoi);
							SC_libre = false ;
						}
					}
				}	
			}
		}
	}
}

