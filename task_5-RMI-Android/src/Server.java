/*
 * Copyright (c) 2004, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are permitted provided that the following
 * conditions are met:
 *
 * -Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.
 *
 * -Redistribution in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in
 *  the documentation and/or other materials provided with the
 *  distribution.
 *
 * Neither the name of Oracle nor the names of
 * contributors may be used to endorse or promote products derived
 * from this software without specific prior written permission.
 *
 * This software is provided "AS IS," without a warranty of any
 * kind. ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY
 * EXCLUDED. SUN MICROSYSTEMS, INC. ("SUN") AND ITS LICENSORS SHALL
 * NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF
 * USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS
 * DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE FOR
 * ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT,
 * SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF
 * THE USE OF OR INABILITY TO USE THIS SOFTWARE, EVEN IF SUN HAS BEEN
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *
 * You acknowledge that Software is not designed, licensed or
 * intended for use in the design, construction, operation or
 * maintenance of any nuclear facility.
 */



import com.sun.mail.imap.*;

import java.io.IOException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import javax.mail.*;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class Server implements Mail {

    public Server() {}

    public String fetchMail() {
        return "Fredrik is in the house!";
    }

    private void initiateConnection() throws MessagingException, IOException {

        Properties properties = new Properties();
        properties.put("mail.store.protocol", "imap");
        properties.setProperty("mail.imap.host", Constants.IMAP_HOST);
        properties.put("mail.imap.port", "993");
        properties.setProperty("mail.debug", "true");
        properties.put("mail.imap.ssl.enable", "true");
        Session emailSession = Session.getDefaultInstance(properties, null);

        Store store = emailSession.getStore("imap");
        store.connect(Constants.IMAP_HOST,Constants.IMAP_PORT, Constants.USERNAME, Constants.PASSWORD);
        IMAPStore imapStore = (IMAPStore) store;
        IMAPFolder inbox = (IMAPFolder) imapStore.getFolder("INBOX");
        inbox.open(Folder.READ_ONLY);


        Message[] messages = inbox.getMessages();
        Message message = messages[messages.length - 1];

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n");
        stringBuilder.append("From:" + message.getFrom()[0] + "\n");
        stringBuilder.append("Subject: " + message.getSubject() + "\n");
        stringBuilder.append("Content: " + message.getContent().toString() + "\n");

        System.out.println(stringBuilder);




    }

    public static void main(String[] args) {

        try {
            Server server = new Server();

            server.initiateConnection();

//            Mail stub = (Mail) UnicastRemoteObject.exportObject(obj, 0);
//
//            // Bind the remote object's stub in the registry
//            Registry registry = LocateRegistry.createRegistry(1234);
//
//            registry.bind("Mail", stub);
//
//            System.err.println("Server ready");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
