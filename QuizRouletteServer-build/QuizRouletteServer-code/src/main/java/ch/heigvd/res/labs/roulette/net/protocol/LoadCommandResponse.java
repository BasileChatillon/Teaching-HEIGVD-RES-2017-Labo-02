
package ch.heigvd.res.labs.roulette.net.protocol;

/**
 *
 * @author Basile Chatillon
 * @author Nicolas Rod
 */
public class LoadCommandResponse {
  private String status;
  private int numberOfStudents;

  public LoadCommandResponse() {
  }

  public LoadCommandResponse(String status, int numberOfStudents) {
    this.status = status;
    this.numberOfStudents = numberOfStudents;
  }
  
  public String getStatus(){
      return status;
  }
  
  public int getNbCommand(){
      return numberOfStudents;
  }
}
