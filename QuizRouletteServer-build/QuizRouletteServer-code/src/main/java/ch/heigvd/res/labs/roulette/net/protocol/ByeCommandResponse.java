
package ch.heigvd.res.labs.roulette.net.protocol;

/**
 * This class is used to serialize/deserialize the response sent by the server
 * when processing the "Bye" command defined in the protocol specification. The
 * JsonObjectMapper utility class can use this class.
 * 
 * @author Basile Chatillon
 * @author Nicolas Rod
 */
public class ByeCommandResponse {
  private String status;
  private int numberOfCommands;

  public ByeCommandResponse() {
      status = "Not initialized yet";
      numberOfCommands = 0;
  }

  public ByeCommandResponse(String status, int numberOfCommands) {
    this.status = status;
    this.numberOfCommands = numberOfCommands;
  }
  
  public String getStatus(){
      return status;
  }
  
  public int getNumberOfCommands(){
      return numberOfCommands;
  }
}
