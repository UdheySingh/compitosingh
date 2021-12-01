package com.example;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    ServerSocket server;
    Socket client;
    String stringaRicevuta;
    Integer numeroEstratto;
    BufferedReader in;
    DataOutputStream out;
    ArrayList<Integer> estratti = new ArrayList<Integer>();

    public Socket attendi() {
        try {
            System.out.println("Server partito");
            server = new ServerSocket(6789);
            client = server.accept();
            server.close();
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            out = new DataOutputStream(client.getOutputStream());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Errore durante l'istanza del server!");
            System.exit(1);
        }
        return client;
    }

    public void comunica() {
        try {
            // System.out.println("Connessione effettuata");
            out.writeBytes("Connessione effettuata\n");

            String a = "Dammi il numero estratto";
            for (;;) {
                out.writeBytes(a + "\n");
                a = "Dammi il nuovo numero estratto";
                stringaRicevuta = in.readLine();
                if (stringaRicevuta.equals("FINE")) {
                    client.close();
                    break;
                }
                numeroEstratto = Integer.parseInt(stringaRicevuta);
                inserisci(numeroEstratto);
                Collections.sort(estratti); // ordina i numeri estratti in ordine crescente
                out.writeBytes(numeriEstratti());
                if (controlloVittoria()) {
                    out.writeBytes("VITTORIA!");
                    client.close();
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Errore durante la comunicazione col client!");
            System.exit(1);
        }
    }

    public void inserisci(Integer n) {
        boolean inserito = false;

        for (int i = 0; i < estratti.size(); i++) {
            if (estratti.get(i).equals(n)) {
                inserito = true;
            }
        }

        if (inserito == true) {
            try {
                out.writeBytes("ERRORE: numero gia' presente\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            estratti.add(n);
        }
    }

    public String numeriEstratti() {
        String x = "numeri estratti: ";

        for (int i = 0; i < estratti.size(); i++) {
            if (i == 0) {
                x += estratti.get(i);
            } else {
                x += " - " + estratti.get(i);

            }
        }
        x += "\n";

        return x;
    }

    public boolean controlloVittoria() {
        if (estratti.size() > 4) {
            for (int i = 0; i < estratti.size() - 4; i++) {
                if (estratti.get(i).equals(estratti.get(i + 1) - 1)
                        && estratti.get(i + 1).equals(estratti.get(i + 2) - 1)
                        && estratti.get(i + 2).equals(estratti.get(i + 3) - 1)
                        && estratti.get(i + 3).equals(estratti.get(i + 4) - 1)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.attendi();
        server.comunica();
    }
}