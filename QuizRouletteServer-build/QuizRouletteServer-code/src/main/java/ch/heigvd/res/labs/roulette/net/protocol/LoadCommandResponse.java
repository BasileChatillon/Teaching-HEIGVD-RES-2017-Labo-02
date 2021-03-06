
package ch.heigvd.res.labs.roulette.net.protocol;

/**
 * This class is used to serialize/deserialize the response sent by the server
 * when processing the "LOAD" command defined in the protocol specification. The
 * JsonObjectMapper utility class can use this class.
 * 
 * @author Basile Chatillon
 * @author Nicolas Rod
 */
public class LoadCommandResponse {
  private String status;
  private int numberOfNewStudents;

  public LoadCommandResponse() {
      status = "Not initialized yet";
      numberOfNewStudents = 0;
  }

  public LoadCommandResponse(String status, int numberOfNewStudents) {
    this.status = status;
    this.numberOfNewStudents = numberOfNewStudents;
  }
  
  public String getStatus(){
      return status;
  }
  
  public int getNumberOfNewStudents(){
      return numberOfNewStudents;
  }
}
