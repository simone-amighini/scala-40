package it.simoneamighini.scala40;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import it.simoneamighini.scala40.networking.Server;
import it.simoneamighini.scala40.view.gui.GuiMain;

public class ClientMain {
    public static class Arguments {
        @Parameter(
                names = {"--server-ip", "-i"},
                description = "Changes the IP address to which to connect in order to reach the server."
        )
        public String serverAddress = "localhost";

        @Parameter(
                names = {"--server-port", "-p"},
                description = "Changes the TCP port to which to connect in order to reach the server."
        )
        public String serverPort = Integer.toString(Server.DEFAULT_PORT);

        @Parameter(
                names = {"--help", "-h"},
                description = "Prints a help page.",
                help = true
        )
        public boolean help = false;
    }

    private static Arguments parsedArguments;

    public static Arguments getArguments() {
        return parsedArguments;
    }

    public static void main(String[] args) {
        ClientMain.Arguments arguments = new ClientMain.Arguments();
        JCommander jCommander = JCommander.newBuilder()
                .addObject(arguments)
                .programName("<scala-40-client-program-name>")
                .build();

        try {
            jCommander.parse(args);
        } catch (ParameterException exception) {
            System.err.println("Invalid input arguments.");
            jCommander.usage();
            System.exit(1);
        }

        if (arguments.help) {
            jCommander.usage();
            System.exit(0);
        }

        parsedArguments = arguments;

        // normal execution
        GuiMain.execute();
    }
}
