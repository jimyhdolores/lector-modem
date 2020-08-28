/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.solmit.lecturamodem;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.TooManyListenersException;

/**
 *
 * @author Jimy Huacho Dolores
 */
public final class SerialComm implements SerialPortEventListener {

    int result = 0;
    static CommPortIdentifier portId = null;
    Enumeration portList = null;
    static InputStream inputStream = null;
    static OutputStream out = null;
    static SerialPort serialPort = null;
    static int srate = 9600;//B-Rate
    String data = null;
    int i = 0;
    String number = "";
    int status = 0;
    public String recvdData = "aa";
    String pswd = "";
    String actualpswd = "";

    public static void main(String args[]) {
        SerialComm obj = new SerialComm();
//                obj.sendMessage("931745158", "mensaje sms solmit");
        obj.leerMennsajes();
    }

    public SerialComm() {
        try {
            if (this.detectarPuerto()) {
                if (this.initPort()) {
                    Thread.sleep(2000);
                }
            }
        } catch (InterruptedException e) {
            System.out.println("Error in SerialComm()-->" + e);
        }
    }

    public boolean detectarPuerto() {

        boolean portFound = false;
        String defaultPort = "COM21";
        try {
            portList = CommPortIdentifier.getPortIdentifiers();

            while (portList.hasMoreElements()) {

                CommPortIdentifier portID = (CommPortIdentifier) portList.nextElement();

                if (portID.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                    if (portID.getName().equals(defaultPort)) {
                        SerialComm.portId = portID;
                        portFound = true;
                        break;
                    }
                }

            }
            if (!portFound) {
                System.out.println("puerto " + defaultPort + " no existe.");
            }
        } catch (Exception e) {
            portFound = false;
        }

        return portFound;

    }

    public boolean initPort() {

        try {
            serialPort = (SerialPort) portId.open("SerialCommApp", 2000);
            inputStream = serialPort.getInputStream();
            out = serialPort.getOutputStream();
            serialPort.addEventListener(this);
            serialPort.notifyOnDataAvailable(true);
            serialPort.setSerialPortParams(srate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
        } catch (PortInUseException | IOException | TooManyListenersException | UnsupportedCommOperationException e) {
            System.out.println("Port in use-->" + e);
            return false;
        }
//
//        try {
//            inputStream = serialPort.getInputStream();
//            out = serialPort.getOutputStream();
//        } catch (IOException e) {
//            System.out.println("IO Error-->" + e);
//        }

//        try {
//            serialPort.addEventListener(this);
//        } catch (TooManyListenersException e) {
//            System.out.println("Too many LIstener-->" + e);
//        }
        serialPort.notifyOnDataAvailable(true);

//        try {
//            serialPort.setSerialPortParams(srate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
////                        serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN | SerialPort.FLOWCONTROL_RTSCTS_OUT);
//        } catch (UnsupportedCommOperationException e) {
//            System.out.println("Error while setting parameters-->" + e);
//        }
        return true;
    }

    @Override
    public synchronized void serialEvent(SerialPortEvent event) {

        switch (event.getEventType()) {
            case SerialPortEvent.DATA_AVAILABLE:

                byte[] readBuffer = new byte[1024];
                int numBytes = 1024;
                data = "";
                this.recvdData = "";
                try {
                    Thread.sleep(100);
                    while (inputStream.available() > 0) {
                        numBytes = inputStream.read(readBuffer);//count of reading data
                        data = data + new String(readBuffer, 0, numBytes);
                        data = data.trim();
                        this.recvdData += data;
                    }
                    if (!this.recvdData.isEmpty() && !this.recvdData.trim().equals("OK")) {
                        System.out.println("****************************************************");
                        System.out.println("LISTA DE MENSAJES");
                        System.out.println("****************************************************");

                        System.out.println(this.recvdData);
                    }

                } catch (Exception e) {
                    System.out.println("Exception in serial event-->" + e);
                }

                break;//break from switch case 1:
        }//end of switch 

    }

    public void sendMessage(String num, String msg) {
        try {
            System.out.println("Sending Message");
            this.recvdData = "";
            String dq = String.valueOf((char) 34);
            String conectAT = "AT";
            String mysms = "AT+CMGS=" + dq + num + dq;

//                        String centralMensaje = "+51195599000";
//                        String SCCA = "AT+CSCA=" + dq + centralMensaje + dq + "," + 145;
//
            out.write(conectAT.getBytes());
            out.write(26);
            out.write(13);
            Thread.sleep(500);

            out.write(mysms.getBytes());
            out.write(13);
            Thread.sleep(500);
            mysms = msg;
            out.write(mysms.getBytes());
            out.write(26);
            out.write(13);
            Thread.sleep(500);
            if (this.recvdData.contains("OK")) {
                return;
            } else if (this.recvdData.contains(">")) {
                out.write(26);
                out.write(13);
                sendMessage(num, msg);
            } else {
                sendMessage(num, msg);
            }
        } catch (IOException | InterruptedException e) {
            System.out.println(e);
        }

    }

    public void leerMennsajes() {
        try {
            this.recvdData = "";
            String dq = String.valueOf((char) 34);
            String todos = "ALL";
            String modoTexto = "AT+CMGF=1";
            String modoPdu = "AT+CMGF=0";
            String mysms = "AT+CMGL= " + dq + todos + dq;

            out.write(modoTexto.getBytes());
            out.write(13);
            Thread.sleep(100);

            out.write(mysms.getBytes());
            out.write(13);
            Thread.sleep(200);

            out.write(modoPdu.getBytes());
            out.write(13);
            Thread.sleep(100);

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
