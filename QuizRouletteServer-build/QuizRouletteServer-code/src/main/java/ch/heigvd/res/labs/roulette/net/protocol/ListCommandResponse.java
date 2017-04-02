
package ch.heigvd.res.labs.roulette.net.protocol;

import ch.heigvd.res.labs.roulette.data.Student;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Basile Chatillon
 * @author Nicolas Rod
 */
public class ListCommandResponse {
    private final List<Student> students;
    
    public ListCommandResponse(){
        students = new LinkedList<>();
    }

    public ListCommandResponse(List<Student> s) {
        this.students = new LinkedList<>(s);
    }
    
    public List<Student> getStudents(){
        return students;
    }
}
