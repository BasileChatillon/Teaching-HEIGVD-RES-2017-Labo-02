package ch.heigvd.res.labs.roulette.net.client;

import ch.heigvd.res.labs.roulette.data.JsonObjectMapper;
import ch.heigvd.res.labs.roulette.data.Student;
import ch.heigvd.res.labs.roulette.net.protocol.ByeCommandResponse;
import ch.heigvd.res.labs.roulette.net.protocol.InfoCommandResponse;
import ch.heigvd.res.labs.roulette.net.protocol.ListCommandResponse;
import ch.heigvd.res.labs.roulette.net.protocol.LoadCommandResponse;
import ch.heigvd.res.labs.roulette.net.protocol.RouletteV2Protocol;
import java.io.IOException;
import java.util.List;

/**
 * This class implements the client side of the protocol specification (version 2).
 *
 * @author Olivier Liechti
 */
public class RouletteV2ClientImpl extends RouletteV1ClientImpl implements IRouletteV2Client {

    // We keep in attribut the response, so we can use them
    private ByeCommandResponse responseBye = new ByeCommandResponse();
    private LoadCommandResponse responseLoad = new LoadCommandResponse();

    @Override
    public void clearDataStore() throws IOException {
        out.println(RouletteV2Protocol.CMD_CLEAR);
        out.flush();

        // We then read the response, which should be DATASTORE CLEARED
        in.readLine();
    }

    @Override
    public List<Student> listStudents() throws IOException {
        out.println(RouletteV2Protocol.CMD_LIST);
        out.flush();

        return JsonObjectMapper.parseJson(in.readLine(), ListCommandResponse.class).getStudents();
    }

    @Override
    public int getNumberOfStudents() throws IOException {
        // Send the command to the server
        out.println(RouletteV2Protocol.CMD_INFO);
        out.flush();

        // Return the correct info
        return JsonObjectMapper.parseJson(in.readLine(), InfoCommandResponse.class).getNumberOfStudents();
    }

    @Override
    public void disconnect() throws IOException {

        if (!isConnected()){
            return;
        }
        // Send the end command to the server
        out.println(RouletteV2Protocol.CMD_BYE);
        out.flush();
        // get the answer
        responseBye = JsonObjectMapper.parseJson(in.readLine(), ByeCommandResponse.class);

        // Close the BufferedReader, PrintWriter and Socket
        in.close();
        out.close();
        socket.close();
        in = null;
        out = null;
        socket = null;
    }

    /**
     * Method to get the status of the response after a "BYE" command
     * @return the status of the command "BYE"
     */
    public String getByeStatus() {
        return responseBye.getStatus();
    }

    /**
     * Method to get the number of commands of the response after a "BYE" command
     * @return the number of commands done during this session 
     */
    public int getNumberOfCommands() {
        return responseBye.getNumberOfCommands();
    }

    @Override
    public void loadStudents(List<Student> students) throws IOException {
        // Send the command to load students
        out.println(RouletteV2Protocol.CMD_LOAD);
        out.flush();
        in.readLine();

        // Write each student to the server
        for (Student student : students) {
            out.println(student.getFullname());
            out.flush();
        }

        // Send the end of data marker and read the answer
        out.println(RouletteV2Protocol.CMD_LOAD_ENDOFDATA_MARKER);
        out.flush();

        // get the answer of the server
        responseLoad = JsonObjectMapper.parseJson(in.readLine(), LoadCommandResponse.class);
    }
    
    /**
     * Method to get the status of the response after a "LOAD" command
     * @return the status of the command "LOAD"
     */
    public String getLoadStatus() {
        return responseLoad.getStatus();
    }

    /**
     * Method to get the number of commands of the response after a "LOAD" command
     * @return the number of students that we had during the "LOAD" command 
     */
    public int getLoadNbStudents() {
        return responseLoad.getNumberOfNewStudents();
    }
}
