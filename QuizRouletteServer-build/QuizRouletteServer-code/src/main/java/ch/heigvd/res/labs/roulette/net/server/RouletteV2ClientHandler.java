package ch.heigvd.res.labs.roulette.net.server;

import ch.heigvd.res.labs.roulette.data.EmptyStoreException;
import ch.heigvd.res.labs.roulette.data.IStudentsStore;
import ch.heigvd.res.labs.roulette.data.JsonObjectMapper;
import ch.heigvd.res.labs.roulette.net.protocol.ByeCommandResponse;
import ch.heigvd.res.labs.roulette.net.protocol.InfoCommandResponse;
import ch.heigvd.res.labs.roulette.net.protocol.ListCommandResponse;
import ch.heigvd.res.labs.roulette.net.protocol.LoadCommandResponse;
import ch.heigvd.res.labs.roulette.net.protocol.RandomCommandResponse;
import ch.heigvd.res.labs.roulette.net.protocol.RouletteV2Protocol;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class implements the Roulette protocol (version 2).
 *
 * @author Olivier Liechti
 * @author Basile Chatillon
 * @author Nicolas Rod
 */

public class RouletteV2ClientHandler implements IClientHandler {

    final static Logger LOG = Logger.getLogger(RouletteV2ClientHandler.class.getName());

    private final IStudentsStore store;

    public RouletteV2ClientHandler(IStudentsStore store) {
        this.store = store;
    }

    @Override
    public void handleClientConnection(InputStream is, OutputStream os) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(os));

        writer.println("Hello. Online HELP is available. Will you find it?");
        writer.flush();

        String command;
        int nbCommand = 0;
        boolean done = false;
        while (!done && ((command = reader.readLine()) != null)) {
            nbCommand++;
            LOG.log(Level.INFO, "COMMAND: {0}", command);
            switch (command.toUpperCase()) {
                case RouletteV2Protocol.CMD_RANDOM:
                    RandomCommandResponse rcResponse = new RandomCommandResponse();
                    try {
                        rcResponse.setFullname(store.pickRandomStudent().getFullname());
                    } catch (EmptyStoreException ex) {
                        rcResponse.setError("There is no student, you cannot pick a random one");
                    }
                    writer.println(JsonObjectMapper.toJson(rcResponse));
                    writer.flush();
                    break;
                case RouletteV2Protocol.CMD_HELP:
                    writer.println("Commands: " + Arrays.toString(RouletteV2Protocol.SUPPORTED_COMMANDS));
                    writer.flush();
                    break;

                case RouletteV2Protocol.CMD_INFO:
                    InfoCommandResponse response = new InfoCommandResponse(RouletteV2Protocol.VERSION, store.getNumberOfStudents());
                    writer.println(JsonObjectMapper.toJson(response));
                    writer.flush();
                    break;
                
                case RouletteV2Protocol.CMD_LOAD:
                    writer.println(RouletteV2Protocol.RESPONSE_LOAD_START);
                    writer.flush();
                    LoadCommandResponse responseLoad;
                    // We get the number of students before adding the new ones. So we can calculate the difference
                    int oldNumberOfStudents = store.getNumberOfStudents();
                    
                    // We first try to import the data. If we can, we state "success"
                    try{
                        store.importData(reader);
                        responseLoad = new LoadCommandResponse("success", store.getNumberOfStudents() - oldNumberOfStudents);
                    }
                    catch(IOException e){
                        // If we fail, we state "fail"
                        responseLoad = new LoadCommandResponse("fail", store.getNumberOfStudents() - oldNumberOfStudents);
                    }
                    
                    // We send the answer
                    writer.println(JsonObjectMapper.toJson(responseLoad));               
                    writer.flush();
                    break;

                case RouletteV2Protocol.CMD_BYE:
                    // Creation of the response
                    ByeCommandResponse responseBye = new ByeCommandResponse("success", nbCommand);
                    // We send it
                    writer.println(JsonObjectMapper.toJson(responseBye));
                    writer.flush();
                    done = true;
                    break;

                case RouletteV2Protocol.CMD_CLEAR:
                    // We first clear the store
                    store.clear();
                    // We send the message
                    writer.println(RouletteV2Protocol.RESPONSE_CLEAR_DONE);
                    writer.flush();
                    break;

                case RouletteV2Protocol.CMD_LIST:
                    // Creation of response
                    ListCommandResponse responseList = new ListCommandResponse(store.listStudents());
                    // We send it
                    writer.println(JsonObjectMapper.toJson(responseList));
                    writer.flush();
                    break;

                default:
                    writer.println("Huh? please use HELP if you don't know what commands are available.");
                    writer.flush();
                    break;
            }
            writer.flush();
        }
    }
}
