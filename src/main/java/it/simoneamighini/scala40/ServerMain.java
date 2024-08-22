package it.simoneamighini.scala40;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import it.simoneamighini.scala40.networking.Server;

public class ServerMain {
    public static class Arguments {
        @Parameter(
                names = {"--socket-port", "-p"},
                description = "TCP port to use to listen to connections."
        )
        public int socketPort = Server.DEFAULT_PORT;

        @Parameter(
                names = {"--help", "-h"},
                description = "Prints a help page.",
                help = true
        )
        public boolean help = false;
    }

    public static void main(String[] args) {
        ServerMain.Arguments arguments = new ServerMain.Arguments();
        JCommander jCommander = JCommander.newBuilder()
                .addObject(arguments)
                .programName("<scala-40-server-program-name>")
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

        // normal execution
        Server server = Server.getInstance(arguments.socketPort);

        if (server == null) {
            System.err.println("Server could not be created.");
            System.exit(1);
        }

        if (!(server.start())) {
            System.err.println("Server could not be started.");
        }
    }
}