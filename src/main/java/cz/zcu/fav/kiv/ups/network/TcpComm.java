package cz.zcu.fav.kiv.ups.network;


import java.io.*;
import java.net.*;
import java.util.Timer;
import java.util.TimerTask;

public class TcpComm implements Comm, Runnable {

    private static int CHECKSUM = 138;

    private CommObserver m_observer;
    OutputStream m_output;
    Timer timer;
    TimerTask tt;
    int packetCounter = 0;

    public TcpComm() {
       timer = new Timer();
        tt = new TimerTask() {
            @Override
            public void run() {
               sledujSit();
            }};
        timer.schedule(tt, 1000, 5000);
    }

    private void sledujSit() {
      //  System.out.println("Sleduji pakety");
        if (packetCounter > 5) {
            try {
                System.err.println("Network closed");
                m_observer.processData("Client was disconnected.");
                m_output.close();
                timer.cancel();
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void send(String data) {
        assert (data != null);
        assert (m_output != null);

        byte [] res = new byte[data.length()+3];
        int i = 0;
        res[i++] = 1;
        res[i++] = 2;
        int checksum_res = 0;
        byte temp;
        for(;i<data.length()+2;i++) {
            temp = data.getBytes()[i-2];
            res[i] = temp;
            checksum_res = (checksum_res + temp) % CHECKSUM;
        }
        res[0] = (byte)checksum_res;
        res[i] = 3;


        try {
            packetCounter++;
            m_output.write(res);
            m_output.flush();

        } catch (IOException e) {
            System.err.println("Caught IOException: " + e.getMessage());
        }
    }


    public void registerObserver(CommObserver observer) {
        m_observer = observer;
    }


    public void run() {

        try {

            InetAddress address = InetAddress.getByName("10.10.100.14");
            int port = 10000;
            Socket socket = new Socket(address, port);
            m_output = socket.getOutputStream();
            InputStream input = socket.getInputStream();
//            BufferedInputStream input = (BufferedInputStream) socket.getInputStream();
//            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            while (true) {
/*
                String line = in.readLine();
                if (line != null) {
                    m_observer.processData(line);
                }
*/
                byte[] buffer = new byte[1024];
                int count = input.read(buffer);
                if (count > 0)
                {
                    String data = new String(buffer, 0, count);
                    if (m_observer != null)
                    {
                        /*
                        if (timer != null){
                            System.out.println("Reset timer.");
                            timer.cancel();
                            timer.purge();
                            tt.cancel();
                        }
                        */
                        packetCounter--;
                        m_observer.processData(data);
                    }
                }else {
                    try {
                        System.err.println("Network closed");
                        m_observer.processData("Server was disconnected.");
                        m_output.close();
                        timer.cancel();
                        break;
                    }catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        } catch (IOException e) {
            System.err.println("Caught IOException: " + e.getMessage());
            m_observer.processData("Client was disconnected.");

        }
    }

    //---------------------------------------------------------

    public void start() {
        (new Thread(this)).start();
    }
}