
package ch.heigvd.res.labs.roulette.net.protocol;

/**
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
