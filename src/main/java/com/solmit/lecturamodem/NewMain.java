/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.solmit.lecturamodem;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;

/**
 *
 * @author PC
 */
public class NewMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException {

        CommPortIdentifier serialPortId;

        Enumeration enumComm;

        enumComm = CommPortIdentifier.getPortIdentifiers();
        String confirmacionAT = "AT\r\n";
        SerialPort serialPort = null;
        OutputStream outputStream = null;

        while (enumComm.hasMoreElements()) {
            serialPortId = (CommPortIdentifier) enumComm.nextElement();
            if (serialPortId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                System.out.println(serialPortId.getName());
                System.out.println("*****************");
//                if (serialPortId.getName().equals("COM3")) {
//                    try {
//                        serialPort = (SerialPort) serialPortId.open("COM3", 2000);
//                    } catch (PortInUseException e) {
//                        System.out.println("err");
//                    }
//                    try {
////                        serialPort.getp
//                        outputStream = serialPort.getOutputStream();
//                    } catch (IOException e) {
//                        System.out.println("error 1 :::::::: " + e.getMessage());
//                    }
//                    try {
//                        serialPort.setSerialPortParams(9600,
//                                SerialPort.DATABITS_8,
//                                SerialPort.STOPBITS_1,
//                                SerialPort.PARITY_NONE);
//                    } catch (UnsupportedCommOperationException e) {
//                        System.out.println("error 2 :::::::: " + e.getMessage());
//                    }
//                    try {
//                        outputStream.write(confirmacionAT.getBytes());
//                        Thread.sleep(3000);
//                        outputStream.flush();
////
//                        outputStream.close();
//                        serialPort.close();
//                    } catch (IOException e) {
//                        System.out.println("error 3 :::::::: " + e.getMessage());
//                    }
//                }
            }
        }
    }

}
