
package ch.heigvd.res.labs.roulette.net.protocol;

/**
 *
 * @author Basile Chatillon
 * @author Nicolas Rod
 */
public class ByeCommandResponse {
  private String status;
  private int nbCommand;

  public ByeCommandResponse() {
  }

  public ByeCommandResponse(String status, int numberOfStudents) {
    this.status = status;
    this.nbCommand = numberOfStudents;
  }
  
  public String getStatus(){
      return status;
  }
  
  public int getNbCommand(){
      return nbCommand;
  }
}
