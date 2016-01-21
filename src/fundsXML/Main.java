/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fundsXML;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.util.JAXBSource;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.jdom2.JDOMException;
import org.xml.sax.SAXException;

/**
 *
 * @author gP
 */
public class Main {

    FundsXML fundsXML;
    DataSupplierType dataSupplierType;

    /*
    FundsXML.Funds funds;
        FundsXML.Funds.Fund fund;
        FundsXML.DataSupplierList dataSupplierList;
        FundsXML.Funds.Fund.SecurityCodes securityCodes;
        //FundsXML.Funds.Fund.SecurityCodes.CountrySecurityCode countrySecurityCode;
        FundsXML.Funds.Fund processData;

        SecurityCodesType securityCodesType;
        ProcessDataType processDataType;
        PortfolioType portfolioType;
        PortfolioAssetsType portfolioAssetsType;

        //List<PortfolioAssetsType> portfolioAssetsTypeList = new ArrayList<PortfolioAssetsType>();
        List<PortfolioAssetsType> portfolioAssetsTypeList = new ArrayList<>();
     */
    /**
     * @param args the command line arguments
     * @throws javax.xml.bind.PropertyException
     * @throws javax.xml.datatype.DatatypeConfigurationException
     * @throws java.io.IOException
     * @throws org.jdom2.JDOMException
     * @throws java.io.FileNotFoundException
     * @throws org.xml.sax.SAXException
     */
    public static void main(String[] args) throws JAXBException, DatatypeConfigurationException, IOException, JDOMException, FileNotFoundException, SAXException {
        Main m = new Main(args);
        m.begin();
        m.generateXML();
        m.end();
    }

    public Main(String[] args) {
    }

    private void begin() {
    }

    private void end() {
    }

    private void generateXML() throws JAXBException, DatatypeConfigurationException, IOException, JDOMException, FileNotFoundException, SAXException {
        // marshalling setup
        JAXBContext context = JAXBContext.newInstance(FundsXML.class);
        Marshaller m = context.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        // create fundsXML object
        fundsXML = new FundsXML();
        fundsXML.setDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(LocalDate.now().format(DateTimeFormatter.ISO_DATE)));
        dataSupplierType = new DataSupplierType();
        // set data supplier
        //dataSupplierType.setName("Gelion Management GmbH");

        DataSupplierType.Short _short;
        _short = new DataSupplierType.Short();
        _short.setValue("Gelion");
        dataSupplierType.setShort(_short);

        fundsXML.dataSupplier = dataSupplierType;

        // set Unique Delivery IDentification, default is 0
        // @TODO: set id unique
        //fundsXML.setUniqueDeliveryIdentification(1);
        //TODO: XMLgregorianCalendar => String => long: Is there a better way?
        fundsXML.setUniqueDeliveryIdentification(Long.parseLong(DatatypeFactory.newInstance().newXMLGregorianCalendar(LocalDateTime.now().format(DateTimeFormatter.ofPattern("YYYYMMddHHmmss"))).toString()));

        fundsXML.setLanguage("DE");

        JAXBSource source;
        source = new JAXBSource(context, fundsXML);

        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = factory.newSchema(new File("FundsXML3.1.1.xsd"));
        Validator validator = schema.newValidator();
        validator.validate(source);
        System.out.println("validation against FundsXML3.1.1.xsd ok");

        // marshal to console
        m.marshal(fundsXML, System.out);
    }
}
