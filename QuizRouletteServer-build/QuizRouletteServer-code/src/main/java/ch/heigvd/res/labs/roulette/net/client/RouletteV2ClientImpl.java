package ch.heigvd.res.labs.roulette.net.client;

import ch.heigvd.res.labs.roulette.data.JsonObjectMapper;
import ch.heigvd.res.labs.roulette.data.Student;
import ch.heigvd.res.labs.roulette.data.StudentsList;
import ch.heigvd.res.labs.roulette.net.protocol.ByeCommandResponse;
import ch.heigvd.res.labs.roulette.net.protocol.InfoCommandResponse;
import ch.heigvd.res.labs.roulette.net.protocol.ListCommandResponse;
import ch.heigvd.res.labs.roulette.net.protocol.LoadCommandResponse;
import ch.heigvd.res.labs.roulette.net.protocol.RouletteV1Protocol;
import ch.heigvd.res.labs.roulette.net.protocol.RouletteV2Protocol;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * This class implements the client side of the protocol specification (version 2).
 *
 * @author Olivier Liechti
 */
public class RouletteV2ClientImpl extends RouletteV1ClientImpl implements IRouletteV2Client {

    private ByeCommandResponse responseBye = new ByeCommandResponse();
    private LoadCommandResponse responseLoad = new LoadCommandResponse();

    @Override
    public void clearDataStore() throws IOException {
        out.println(RouletteV2Protocol.CMD_CLEAR);
        out.flush();

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

    public String getByeStatus() {
        return responseBye.getStatus();
    }

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

    public String getLoadStatus() {
        return responseLoad.getStatus();
    }

    public int getLoadNbStudents() {
        return responseLoad.getNumberOfNewStudents();
    }
}
