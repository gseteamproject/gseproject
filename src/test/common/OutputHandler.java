package test.common;

public interface OutputHandler {
	void handleOutput(String instanceName, String msg);
	void handleTermination(int exitValue);
}
