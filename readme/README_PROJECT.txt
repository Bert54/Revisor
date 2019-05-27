Author: William Philbert

This README explains how to work on the project.
For help about how to install Revisor, see README_INSTALL.

///////////////////////////////////
////// Summary ////////////////////
///////////////////////////////////

- Project Content
- Requirements
- Maven
- Recommended Workspace
- Commit
- SVN Config
- TortoiseSVN
- Code formating
- Adding a new engine
- Updating the web-site

///////////////////////////////////
////// Project Content ////////////
///////////////////////////////////

+[revisor] (1)
  +[docs] (2)
  |
  +[lib] (3)
  | +[lp_solve_5.5.2.0]
  |
  +[README] (4)
  | +[CodeFormat] (5)
  | | + EclipseCleanUp.xml
  | | + EclipseFormat.xml
  | |
  | +[GlassfishConfigHelp] (6)
  |
  +[revisor] (8)
  | +[revisorEngine]
  | | +[revisorEngine-clc]
  | | +[revisorEngine-core]
  | | +[revisorEngine-pcsfc]
  | | +[revisorEngine-pl]
  | | +[revisorEngine-plak]
  | | +[revisorEngine-qa]
  | | + pom.xml
  | |
  | +[revisorPlatform]
  | | +[revisorPlatform-applet]
  | | +[revisorPlatform-swing]
  | | +[revisorPlatform-webstart]
  | | + pom.xml
  | |
  | +[revisorUpdateTool]
  | |
  | +[revisorWebPlatform]
  | | +[revisorWebPlatform-ear]
  | | +[revisorWebPlatform-ejb]
  | | +[revisorWebPlatform-lib]
  | | +[revisorWebPlatform-web]
  | | + pom.xml
  | |
  | + pom.xml
  |
  +[site-web] (8)
  |
  +[update] (9)

- [revisor] (1) represents the folder [wikitaaable]/Trunk/revisor from the repository.
- [docs] (2) contains some documentation on the project.
- [lib] (3) contains the library "lp_solve" for all OS.
- [README] (4) contains some READMEs (including this file) about how to work on this project.
- [CodeFormat] (5) contains configuration files for Eclipse (and Netbeans) that allow to format Java sources uniformly.
- [GlassfishConfigHelp] (6) contains some help about how to configure a Glassfish server for RevisorWebPlatform.
- [revisor] (7) contains the sources of the projet, organized in a Maven project tree.
- [site-web] (8) contains Revisor web site.
- [update] (9) contains tools to update Revisor web site.

///////////////////////////////////
////// Requirements ///////////////
///////////////////////////////////

To work on this project, you need:
- All requirements indicated in README_INSTALL
- Plus some additionnal requirements:
	- Maven 3 (makes life easier). If you don't use Maven, you will have to copy the contents of "src/main/resouces" folders into "src/main/java/" folders each time there is a change in those folders. If you still want to do this, don't commit those copies!
	- If you use an IDE (ex: Eclipse/Netbeans), you MIGHT need to install some plugins (for Maven). You should check that you have the last version of your IDE and of its Maven plugins.

If you work on revisorWebPlatform, you also need:
- A Glassfish 4 server.
- A Derby database.
- If you use an IDE (ex: Eclipse/Netbeans), you MIGHT need to install some plugins (for Glassfish and Derby).
- For some help about how to configure Glassfish (connection pool, resrource, realm) see the screenshots in [GlassfishConfigHelp] (6).

///////////////////////////////////
////// Maven //////////////////////
///////////////////////////////////

Maven is a powerful tool used to automatically:
- download and import project dependencies recursively
- call some code generation tools like JFlex and Cup
- build project
- run tests
- package project
- install project on local repository
...
All this stuff is configured using a pom.xml file at project root.

Download: http://maven.apache.org/download.cgi

If the project is already created and configured, the only command you need to know is:
+----code----------------------------------------------------------------+
mvn clean install
+------------------------------------------------------------------------+
It will build your project, package it, and install it in the local repository, using the pom.xml configuration.
If you use an IDE, you can use the menu to execute maven commands (ex: in Eclipse, right click your project, run as, Maven install).

If you need to change the pom.xml file (ex: add a new dependency) read the maven tutorials: http://maven.apache.org/users/index.html
If you use an IDE, you may need to update project configuration when you change pom.xml (ex: in Eclipse, right click your project, Maven, Update Project...).

///////////////////////////////////
////// Recommended Workspace //////
///////////////////////////////////

[1] Install
- Install the last version of Maven
- Install the last version of Eclipse IDE for Java EE Developers and check that you have the last version of the Maven plugin.
- To work on revisorPlatform, install a Glassfish 4 server and a Derby database.

[2] Import
- Open Eclipse and choose a workspace
- Right click in the "Project Exlorer" view (on the left)
- Import... > Maven > Existing Maven Projects > Next
- Select the folder: [wikitaaable]/Trunk/revisor/revisor
- Import the project and all its sub-projects

[3] Check
- You should see the following list in the "Project Exlorer" view:
	- revisor
	- revisorEngine
	- revisorEngine-clc
	- revisorEngine-core
	- revisorEngine-pcsfc
	- revisorEngine-pl
	- revisorEngine-plak
	- revisorEngine-qa
	- revisorPlatform
	- revisorPlatform-applet
	- revisorPlatform-swing
	- revisorPlatform-webstart
	- revisorUpdateTool
	- revisorWebPlatform
	- revisorWebPlatform-ear
	- revisorWebPlatform-ejb
	- revisorWebPlatform-lib
	- revisorWebPlatform-web
- Check that all sub-projects were imported (this list may change in the futur)
- If a sub project is missing, or if a new sub-project is added by someone after you did [2], you may need to repeat [2] for that sub project

[4] Resolving "problems"
- First, right click the "revisor" main project > Maven > Update Project... > Select all > Ok. This operation updates Eclipse projects configurations to be coherent with what is written in the "pom.xml" files.
	- You should do this each time a pom.xml file is changed/added.
- When you will get the project for the first time, Eclipse will tell you that some classes are missing (ex: PLConsoleParser).
	- This is normal, because some classes are generated by Jflex and Cup. Those error messages will disappear when you will use Maven to build the project. See step [6] label (#1).
- You may have an error like this : "Plugin execution not covered by lifecycle configuration ..."
	- If the "Update Project..." thing don't resolve this problem, this may indicate that you don't have the last version of Eclipse and/or its Maven plugin.
	- If this error appears after you add a new Maven plugin in a "pom.xml" file, this may indicate that Eclipse can't check the use of that plugin, but it will run anyway.
		- In this case, you will have to tell Eclipse to ignore the problem by adding a new <pluginExecution> tag in the following section of the "revisor" project "pom.xml":

+----code----------------------------------------------------------------+
				<!-- This plugin's configuration is used to store Eclipse m2e settings only. It has no influence on the Maven build itself. -->
				<plugin>
					<groupId>org.eclipse.m2e</groupId>
					<artifactId>lifecycle-mapping</artifactId>
					<version>1.0.0</version>
					<configuration>
						<lifecycleMappingMetadata>
							<pluginExecutions>
								<pluginExecution>
									<pluginExecutionFilter>
										<groupId>org.apache.maven.plugins</groupId>
										<artifactId>maven-dependency-plugin</artifactId>
										<versionRange>[2.8,)</versionRange>
										<goals>
											<goal>copy-dependencies</goal>
											<goal>copy</goal>
										</goals>
									</pluginExecutionFilter>
									<action>
										<ignore />
									</action>
								</pluginExecution>
								<!-- ... -->
							</pluginExecutions>
						</lifecycleMappingMetadata>
					</configuration>
				</plugin>
+------------------------------------------------------------------------+

[5] Code
- Now you can work on the project.
- Don't forget that Java sources go in "src/main/java", while all other sources (image, sound, txt, other language source...) go in "src/main/resources".

[6] Test
- If working on a revisorEngine or revisorPlatform, you can test your code with one of the following methods:
	* Direcly run a class:
		- Right click the class in the "Project Explorer" view
		- Run As > Java Application
		- (This launch configuration is now registered and you can reuse it easily with the run icon on the top.)
	* (#1) Package the project with Maven and run the jar main class
		- This method allows you to test if your code works when packaged in a jar. (important if you call some scripts in your code. ex: revisorQA calls some perl scripts.)
		- Right click the "revisorEngine" project
		- Run As > Maven install
		- A jar is created in the "target" folder of the project (and in your local Maven repositoty)
		- (This launch configuration is now registered and you can reuse it easily with the run icon on the top.)
			- You can duplicate and/or edit this launch configuration.
				- You can rename the launch configuration.
				- You can add "clean " before "install" in the "Goals" field.
		- In the menu: Run > External Tools > External Tools Confiration...
		- Create a new launch configuration
		- In the "Location" field, select your javaw. ex: C:\Program Files\Java\jdk1.7.0_55\bin\javaw.exe
		- In the "Working Directory" field, select the project "target" folder. ex: ${workspace_loc:/revisorEngine-qa/target}
		- In the "Arguments", select your jar. ex: -jar revisorEngine-qa-1.0.0.jar
		- Run
		- (This launch configuration is now registered and you can reuse it easily with the run icon on the top.)
- If working on revisorWebPlatform, you can test your code by deploying the application on Glassfish:
	- Right click the "revisorPlatform-ear" project
	- Run As > Run on Server
	- If it is the first time you deploy the application, you will need to add the glassfish server to the list of registered servers.
	- If you can't find Glassfish in the server list, you may need to install an Eclipse Glassfish plugin (Help > Eclipse Marketplace... > search for "glassfish". At the time this readme is written, the plugin is "Glassfish Tools for Kepler", "Kepler" is the last Eclipse version)
	- To automatically launch the Derby database with Glassfish:
		- Go to Window > Preferences > Glassfish Preferences
		- Check "Start the JavaDB database process when Starting GlassFish Server"
		- In "Java DB Database Location" select ".../workspace/.metadata/.plugins/glassfish.javadb"
	- If you havn't configured your Glassfish server yet, check the [GlassfishConfigHelp] (6) folder

///////////////////////////////////
////// Commit /////////////////////
///////////////////////////////////

IMPORTANT NOTE: Do not commit IDE configuration files and target folders on the repository!
- You should only commit "src" folders and "pom.xml" files.
- You should NOT commit ".settings" folders, ".project" files, ".classpath" files... (these are IDE configuration files).
- You should NOT commit "target" folders (these are generated files, like "bin" folders).

///////////////////////////////////
////// SVN Config /////////////////
///////////////////////////////////

You can use commands like those on the root of your repository (this was already done):
+----code----------------------------------------------------------------+
svn propedit svn:ignore .metadata RemoteSystemsTempFiles Servers
svn propedit svn:global-ignores target bin .project .settings .classpath
+------------------------------------------------------------------------+

///////////////////////////////////
////// TortoiseSVN ////////////////
///////////////////////////////////

If you work with TortoiseSVN on Windows, you can use the menu to add some exceptions:

TortoiseSVN > Properties > New > Other > choose svn:ignore and enter the following property value:
+----code----------------------------------------------------------------+
.metadata
RemoteSystemsTempFiles
Servers
+------------------------------------------------------------------------+

TortoiseSVN > Properties > New > Other > choose svn:global-ignores and enter the following property value:
+----code----------------------------------------------------------------+
target
bin
.project
.settings
.classpath
+------------------------------------------------------------------------+

///////////////////////////////////
////// Code formating /////////////
///////////////////////////////////

The java code of this project was formatted using Eclipse formatter.
The eclipse formatter configuration file is on the repository, so you can import it.
(not only in Eclipse, but also in NetBeans! I don't know for other IDE)

- Import in Eclipse:
	* Window > Preferences > Java > Code Style > Formatter > Import...
	* Select the file "codeFormat/EclipseFormat.xml" from the revisor repository
	* Now you can format your code by pressing Ctrl + Shift + F (configurable)
	* TIP: you can also organize imports with Ctrl + Shift + O (configurable)
	* TIP: this is what I press each time I edit a file: Ctrl(Shift(O F) S)

	* If you want, you can also import the cleanUp configuration:
	* Window > Preferences > Java > Code Style > CleanUp > Import...
	* Select the file "codeFormat/EclipseCleanUp.xml" from the revisor repository

	* You can also go to Window > Preferences > Java > Code Style
	* and check the three checkboxes (this, is, @Override)

- Import in Netbeans:
	* Install the "Eclipse Java Code Formatter" plugin (Tools > Plugins > find and check the plugin > Install)
	
	* Go to Tools > Options > Java tab > Eclipse Formatter tab.
	* Check "Use Eclipse code formatter" and "Format on save", uncheck "Show notifications".
	* In "Configuration file", select the file "codeFormat/EclipseFormat.xml" from the revisor repository.
	
	* Go to Tools > Options > Editor > Formatting.
	* Uncheck "Expand Tabs to Spaces" and select "Off" in "Line Warp".
	
	* Now, the files are automatically formatted each time you save.
	* You can also format manually: Source > Format with Eclipse formatter.


Also, you should always declare blocks explicitly. For example:

+----code----------------------------------------------------------------+
if(condition)
	instruction1;
+------------------------------------------------------------------------+

should be wrote

+----code----------------------------------------------------------------+
if(condition) {
	instruction1;
}
+------------------------------------------------------------------------+

so if, some day, you (or someone else) want to write

+----code----------------------------------------------------------------+
if(condition) {
	instruction1;
	instruction2;
}
+------------------------------------------------------------------------+

you (or he) won't forget the braces.

///////////////////////////////////
////// Adding a new engine ////////
///////////////////////////////////

When you want to add a new engine, look at already existing engines (PL and PLAK for example).
- Look how the Maven "pom.xml" file is writen. Check and complete parent "pom.xml" files.
- Don't forget that Java sources go in "src/main/java", while all other sources (image, sound, txt, other language source...) go in "src/main/resources".
	- ex: for Revisor/QA, the "src/main/resources" folder contains Perl sources, data used by examples, readme files...
	- In "src/main/resources", you should create two folders that will be used by the interpreter of the engine:
		- The "parser" folder will contain ".cup" and ".flex" files.
		- The "resources" folder will contain, among others, text files displayed in the interpreter when the user type "hepl".
			- Why a "resources" folder in "src/main/resources"? Because when the ".jar" is created, all the contents of "src/main/java" and "src/main/resources" are put in the main folder of the ".jar", there is no more separation.
- Your main package should be "fr.loria.orpailleur.revisor.engine.revisorXXX" where "XXX" is the name of your engine, for example: PL, PLAK, CLC, QA, RDFS...
- In your main package, you should have a "RevisorXXX" class and a "RevisorEngineXXX" class.
	- Look how "RevisorPL" and "RevisorEnginePL" are done. "RevisorPL" provide an easy to use static interface, while "RevisorEnginePL" is instantiable.
	- Your engine "RevisorEngineXXX" must implement "RevisorEngine" interface and should extend "AbstractRevisorEngine" class. Your "RevisorXXX" class should instantiate and use your "RevisorEngineXXX" class.
- If you want to create the interpreter for an engine, look at already existing interpreter classes and packages, for example:
	- "AbstractRevisorConsolePL", "RevisorConsolePL", "AbstractRevisorConsolePLAK" and "RevisorConsolePLAK" show how to make extendable interpreters.
	- Also, take a look at "config", "formula" and "instruction" packages to know how to create the classes your interpreter will need.
	- To know how to make a parser with JFlex and Cup:
		- Take a look at "PLConsoleGrammar.cup" and "PLConsoleLexer.jflex" (and their PLAK equivalent), which are in "src/main/resources/parser" (not in "src/main/java").
		- Don't forget to make help files like "RevisorPLGrammar.txt" and "RevisorPLHelp.txt", which are in "src/main/resources/resources".
		- Also don't forget to update the "pom.xml" file.
	- "RevisorPLConsoleMode" and "RevisorPLAKConsoleMode" show how to create a console mode usable in a terminal.
		- Don't forget to make your "RevisorXXXConsoleMode" class the main class of your engine's jar, using Maven "pom.xml".
- To integrate your interpreter in the Swing GUI, you just need to add a line in "RevisorPlatformMainPanel" construtor, next to other engines lines.
	- For example: this.consoles.add(new ConsolePanel<>(new RevisorConsolePL(), this.config));
	- Don't forget to update the "pom.xml" file.

The general advice is to look at what was done for PL and PLAK and do the same for your engine.
You can also find some information in the report in docs/WilliamPhilbert-2014 (in french).

///////////////////////////////////
////// Updating the web site //////
///////////////////////////////////

A tool to update the web-site easily is in development: fr.loria.orpailleur.revisor.update.RevisorUpdateTool
The update procedure is described in the comments in this class.

///////////////////////////////////
