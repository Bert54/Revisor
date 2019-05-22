package fr.loria.orpailleur.revisor.update;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JTextField;

/**
 * @author William Philbert
 */
public class RevisorUpdateTool extends ScriptFrame {
	
	// Constants :
	
	private static final long serialVersionUID = 1L;
	
	// Fields :
	private final JButton chooseFileButton;
	private final JTextField fileField;
	private final JTextField versionField;
	
	private String repositoryDir;
	private String version;
	private String updateDir;
	private String tmpDir;
	private String libDir;
	
	// Constructors :
	
	public RevisorUpdateTool() {
		super("Revisor Update Tool");
		
		// TODO - ajouter les composants.
		
		this.fileField = new JTextField();
		this.versionField = new JTextField();
		this.chooseFileButton = new JButton("Browse");
		
		this.chooseFileButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
			}
			
		});
	}
	
	// Methods :
	
	@Override
	protected String startButtonText() {
		return "Update web site";
	}
	
	@Override
	protected String closeButtonText() {
		return "Close";
	}
	
	@Override
	protected String successMessage() {
		return "Upadate complete! You can close this window.";
	}
	
	@Override
	protected String failureMessage() {
		return "Update failed :(";
	}
	
	@Override
	protected boolean checkParameters() {
		this.repositoryDir = this.fileField.getText();
		this.version = this.versionField.getText();
		
		// TODO - check the values loaded from the fields and update the interface (fields and buttons enabled/visible).
		
		return false;
	}
	
	@Override
	protected void initValues() {
		this.updateDir = this.repositoryDir + "update" + SLASH;
		this.tmpDir = this.updateDir + "tmp" + SLASH;
		this.libDir = this.tmpDir;
		
		// TODO - init other values...
	}
	
	@Override
	protected void execute() throws Exception {
		// TODO - écrire le code correspondant à la description.
		
		// # Interface :
		
		// Au dessus de la console, il y a deux champs "repository folder" et "version".
		// Il y a également un bouton à côté du champs "repository folder" pour utiliser un filechooser.
		// Ces champs et ce bouton peuvent être utilisé tant que l'utilsateur n'a pas lancé le processus de mise à jour.
		// Une fois le processus de mise à jour lancé, les champs ne sont plus modifiables et les bouton ne sont plus clickables.
		
		// En dessous des champs, il y a un bouton pour lancer la mise à jour.
		// Quand ce bouton est cliqué, le contenu des champs est verifié.
		// Si le contenu des champs est valide, la mise à jour commence, sinon un pop-up est afficher pour expliquer le problème.
		// Les veifications à effetuer sont les suivantes :
		//  - Le dossier choisit existe et contient les dossier "revisor", "update" et "site-web".
		//  - Il n'exite pas déjà un zip pour la version choisie dans "site-web/download".
		//    ->Demander une confirmation à l'utilisateur s'il existe déjà un zip pour cette version.
		
		// # Processus de mise à jour :
		
		// Creer si necessaire un dossier "tmp" dans le dossier "update" et le nettoyer.
		
		// Deziper l'archive produite par Maven pour le projet revisorPlatform-webstart dans le dossier "tmp".
		// Supprimer le jar de lpsolve.
		// Renommer le jar de cup-runtime.
		// Corriger le fichier jnlp (cup-runtime).
		// Nettoyer le dossier "site-web/java-web-start" et copier le nouveau contenu.
		// Nettoyer le dossier "tmp".
		
		// Créer un dossier "revisor-X.X.X" dans le dossier "tmp" en fonction de la version entrée par l'utilisateur.
		// Créer les dossier src et bin dans le dossier "revisor-X.X.X".
		// Copier le dossier lib du dépot dans le dossier "revisor-X.X.X".
		// Copier le fichier "README/README_INSTALL.txt" dans le dossier "revisor-X.X.X".
		
		// Copier les jar générés par Maven pour le projet revisorPlatform-swing dans le dossier "bin".
		// Supprimer le jar de lpsolve.
		// Copier le contenu de "update/StuffToCopyInBinFolder" dans le dossier "bin".
		
		// Copier le dossier "revisor" contenant l'arbre des projets Maven dans le dossier "src".
		// Supprimer tout ce qui n'est pas des sources (ne garder que les dossier "src" et les fichier "pom.xml").
		
		// Créer un zip du dossier "revisor-X.X.X" et le mettre dans le dossier "site-web/download".
		
		// Dire à l'utilisateur qu'il n'a plus qu'à :
		//  - modifier la page de download pour ajouter à lien vers le nouveau zip.
		//  - faire un svn add et un commit.
		//  - se connecter à labs et faire un svn update.
	}
	
	// Main :
	
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		ScriptFrame.initLocalAndLookAndFeel();
		new RevisorUpdateTool();
	}
	
}
