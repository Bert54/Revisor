package fr.loria.orpailleur.revisor.engine.revisorQA.qa;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fr.loria.orpailleur.revisor.engine.core.utils.files.Resources;

/**
 * QAPBackendRequest is a fancy interface for a Process instance which is used to communicate with the Perl library.
 * To instanciate a QualitativeConstraintNetwork or a QualitativeAlgebra, a QAPBackendRequest instance is required.
 * @author Valmi Dufout-Lussier
 * @author William Philbert
 * @date 01.02.2013
 */
public class QAPBackendRequest {
	
	// Constants :
	
	/**
	 * The file separator for the current OS.
	 */
	public static final String SLASH = System.getProperty("file.separator");
	
	/**
	 * The command used to call Perl.
	 */
	public static final String PERL_COMMAND = "perl";
	
	/**
	 * The name directory containing all Revisor QA perl scripts.
	 */
	public static final String PERL_SCRIPT_DIR = "qa-perl-src";
	
	/**
	 * The name directory containing Revisor QA example data.
	 */
	public static final String EXAMPLE_DATA_DIR = "qa-example-data";
	
	/**
	 * The main Revisor QA perl script.
	 */
	public static final String PERL_SCRIPT = PERL_SCRIPT_DIR + SLASH + "qap-backend.pl";
	
	/**
	 * The default command array passed to Runtime.exec().
	 */
	public static final String[] DEFAULT_CMDARRAY = new String[] {PERL_COMMAND, PERL_SCRIPT};
	
	/**
	 * The default working directory passed to Runtime.exec().
	 */
	public static final File DEFAULT_WORKING_DIRECTORY = Resources.getWorkingDirectoryWithResources(PERL_SCRIPT_DIR, EXAMPLE_DATA_DIR);
	
	/**
	 * How long should we wait for the backend to send its response? (in milliseconds)<br />
	 * 0 = no timeout
	 */
	public static final long DEFAULT_IO_TIMEOUT = 0;
	
	/**
	 * When this is true, all communications between the backend and us are logged onto the standard error stream.
	 */
	private static final boolean DEBUG_COMM = false;
	
	/**
	 * When this is true, the backend's output to its standard error stream is logged onto our standard error stream and proceeding continues
	 * (if the backend crashes, you will have to terminate the main process by hand or wait for a timeout).<br />
	 * When this is false, any error output from the backend throws an exception.
	 */
	private static final boolean DEBUG_BACKEND = false;
	
	// Fields :
	
	private String[] cmdarray;
	private File workingDirectory;
	private long ioTimeout;
	
	private Process backend;
	private BufferedReader err;
	private BufferedReader in;
	private PrintWriter out;
	
	private int lastRevisionDistance;
	
	// Constructors :
	
	public QAPBackendRequest(String[] cmdarray, File workingDirectory, long ioTimeout) throws Exception {
		this.cmdarray = cmdarray;
		this.workingDirectory = workingDirectory;
		this.ioTimeout = ioTimeout;
		this.startBackend();
	}
	
	public QAPBackendRequest(String[] cmdarray, File workingDirectory) throws Exception {
		this(cmdarray, workingDirectory, DEFAULT_IO_TIMEOUT);
	}
	
	public QAPBackendRequest(long ioTimeout) throws Exception {
		this(DEFAULT_CMDARRAY, DEFAULT_WORKING_DIRECTORY, ioTimeout);
	}
	
	public QAPBackendRequest() throws Exception {
		this(DEFAULT_CMDARRAY, DEFAULT_WORKING_DIRECTORY, DEFAULT_IO_TIMEOUT);
	}
	
	// Getters :
	
	public String[] getCmdarray() {
		return this.cmdarray;
	}
	
	public File getWorkingDirectory() {
		return this.workingDirectory;
	}
	
	public long getIoTimeout() {
		return this.ioTimeout;
	}
	
	// Setters :
	
	public void setCmdarray(String[] cmdarray) {
		this.cmdarray = cmdarray;
	}
	
	public void setWorkingDirectory(File workingDirectory) {
		this.workingDirectory = workingDirectory;
	}
	
	public void setIoTimeout(long ioTimeout) {
		this.ioTimeout = ioTimeout;
	}
	
	// Methods :
	
	private void startBackend() throws Exception {
		this.backend = Runtime.getRuntime().exec(this.cmdarray, null, this.workingDirectory);
		this.err = new BufferedReader(new InputStreamReader(this.backend.getErrorStream()));
		this.in = new BufferedReader(new InputStreamReader(this.backend.getInputStream()));
		this.out = new PrintWriter(this.backend.getOutputStream());
		this.makeQuery("hello");
	}
	
	private void stopBackend() {
		this.backend.destroy();
	}
	
	@Override
	protected void finalize() {
		this.stopBackend();
	}
	
	private String makeQuery(String query) throws IOException {
		this.out.println(query);
		this.out.flush();
		
		if(DEBUG_COMM) {
			System.err.println("Sent: " + query);
		}
		
		String response = null;
		boolean wentonce = false;
		boolean keepgoing = DEBUG_BACKEND;
		
		while(!wentonce || keepgoing) {
			wentonce = true;
			long startTime = System.currentTimeMillis();
			
			while((!this.in.ready()) && (!this.err.ready()) && (this.ioTimeout <= 0 || (System.currentTimeMillis() - startTime < this.ioTimeout))) {
				// Wait until in or err is ready to be read, or timeout.
			}
			
			if(this.in.ready()) {
				response = this.in.readLine();
				
				if(DEBUG_COMM) {
					System.err.println("Recv: " + response);
				}
				
				keepgoing = false;
			}
			else if(this.err.ready()) {
				if(DEBUG_BACKEND) {
					System.err.println("Backend stderr message: " + this.err.readLine());
				}
				else {
					throw new IOException("Backend sent error message: " + this.err.readLine());
				}
			}
			else {
				this.backend.destroy(); // added 21.05.2013: the process was going on in the background after timing out
				throw new IOException("Request timed out (request started at " + startTime + ", it is now " + System.currentTimeMillis() + ", and the timeout limit is " + this.ioTimeout);
			}
		}
		
		return response;
	}
	
	public String createQCN(String file, String algebra) throws IOException {
		String query = "new " + file + " " + algebra;
		String objnr = this.makeQuery(query);
		return objnr;
	}
	
	public List<String> createQCN_PC(String file, String algebra) throws IOException {
		String query = "new_pc " + file + " " + algebra;
		String response_str = this.makeQuery(query);
		List<String> objnrs = Arrays.asList(response_str.split(" "));
		return objnrs;
	}
	
	public String createQCNfromString(String formula, String algebra) throws IOException {
		String query = "new_from_string " + algebra + " " + formula;
		String objnr = this.makeQuery(query);
		return objnr;
	}
	
	public List<String> reviseQCN(String psi, String mu) throws Exception {
		String query = "revise " + psi + " " + mu;
		String response_str = this.makeQuery(query);
		List<String> response = Arrays.asList(response_str.split(" "));
		return response;
	}
	
	public List<String> reviseExhaustivelyQCN(String psi, String mu) throws IOException, NumberFormatException {
		String query = "revise_exhaustive_with_distance " + psi + " " + mu;
		String response_str = this.makeQuery(query);
		List<String> response = new ArrayList<>(Arrays.asList(response_str.split(" ")));
		this.lastRevisionDistance = Integer.parseInt(response.remove(0));
		return response;
	}
	
	public List<String> reviseExhaustivelyQCNd(String psi, String mu, int max_distance) throws IOException, NumberFormatException {
		String query = "revise_exhaustive_with_distance " + psi + " " + mu + " " + max_distance;
		String response_str = this.makeQuery(query);
		List<String> response = new ArrayList<>(Arrays.asList(response_str.split(" ")));
		this.lastRevisionDistance = Integer.parseInt(response.remove(0));
		return response;
	}
	
	public String QCNtoString(String qcn) throws IOException {
		String query = "string " + qcn;
		return this.makeQuery(query);
	}
	
	public String conjoinQCN(String qcn1, String qcn2) throws IOException {
		String query = "conjoin " + qcn1 + " " + qcn2;
		return this.makeQuery(query);
	}
	
	public String abstractQCN(String qcn, int index, String variable) throws IOException {
		String query = String.format("abstract %s %s %d", qcn, variable, index);
		return this.makeQuery(query);
	}
	
	public String refineQCN(String qcn, int index, String variable, String qcn1, String qcn2) throws IOException {
		String query = String.format("refine %s %s %d %s %s", qcn, variable, index, qcn1, qcn2);
		return this.makeQuery(query);
	}
	
	public int getLastRevisionDistance() {
		return this.lastRevisionDistance;
	}
	
}
