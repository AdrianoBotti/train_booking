import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.logging.*;
import javax.xml.*;
import javax.xml.transform.stream.*;
import javax.xml.validation.*;
import org.xml.sax.*;

public class ServerLogEventiXML { //01
    private static final int portaServerLog = 5555;
    private static final String fileLog = "log.xml";
    private static final String fileSchemaXML = "log.xsd";
    
    private static void validaRecordXML(String recordXML){
        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema;
        try {
            schema = sf.newSchema(new File(fileSchemaXML));
            schema.newValidator().validate(new StreamSource(new StringReader(recordXML)));
         } catch (SAXException | IOException ex) {
            Logger.getLogger(ServerLogEventiXML.class.getName()).log(Level.SEVERE, null, ex);
         }
    }
    
    private static void scriviSuLog(String recordXML){
        try{
            Path path = Paths.get(fileLog);
            Files.write(path, recordXML.getBytes(), StandardOpenOption.APPEND);
        } catch (IOException ex) {
            Logger.getLogger(ServerLogEventiXML.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    public static void main(String[] args){
        while(true){
            try( ServerSocket ss = new ServerSocket(portaServerLog);
                 Socket s = ss.accept();
                 DataInputStream din = new DataInputStream(s.getInputStream())
            ){
                String xml = din.readUTF(); 
                validaRecordXML(xml);
                System.out.println(xml);
                scriviSuLog(xml);
            } catch (IOException ex) {
                Logger.getLogger(ServerLogEventiXML.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}

//01
/*
Applicazione server per la ricezione, validazione e scrittura su file di log
di record inviati dall'applicazione client principale PrenotazioneTreni
*/