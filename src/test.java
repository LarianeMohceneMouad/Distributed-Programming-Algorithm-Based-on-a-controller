
public class test {
	public static void main(String[] args) {
		
		
		
		String[] commande = new String[3];
		String argument = "";
		argument = argument+"controleur:Controleur";
		argument =argument + ";";
		
		argument = argument+"site1:Site(controleur)";
		argument =argument + ";";
		argument = argument+"site2:Site(controleur)";
		argument =argument + ";";
	
		argument = argument+"site3:Site(controleur)";
		argument =argument + ";";
		
		commande[0]="-cp";
		commande[1]="jade.boot";
		commande[2]=argument;
		
		jade.Boot.main(commande);

}
}
