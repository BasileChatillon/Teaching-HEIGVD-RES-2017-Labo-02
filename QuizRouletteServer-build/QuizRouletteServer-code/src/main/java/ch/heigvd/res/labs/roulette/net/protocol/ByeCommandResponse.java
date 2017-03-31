
package ch.heigvd.res.labs.roulette.net.protocol;

/**
 *
 * @author Basile Chatillon
 * @author Nicolas Rod
 */
public class ByeCommandResponse {
  private String status;
  private int numberOfStudents;

  public ByeCommandResponse() {
  }

  public ByeCommandResponse(String status, int numberOfStudents) {
    this.status = status;
    this.numberOfStudents = numberOfStudents;
  }
  
  public String getStatus(){
      return status;
  }
  
  public int getNumberOfStrudents(){
      return numberOfStudents;
  }
}
