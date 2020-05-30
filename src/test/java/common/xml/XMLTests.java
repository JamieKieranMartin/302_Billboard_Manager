package common.xml;

import common.models.Billboard;
import common.utils.XML;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class XMLTests {
    Billboard billboard = Billboard.Random(0);
    static String picture ="";

    @BeforeAll
    public static void getPic() throws Exception {
        picture = new String(Files.readAllBytes(Paths.get("./src/test/java/common/xml/picture.txt")));
    }
    @Test
    public void TestEmptyBillboard() throws Exception {
        billboard.message = null;
        billboard.picture = null;
        billboard.information = null;
        billboard.backgroundColor = "#000000";

        String xml = XML.toXML(billboard);
        assertEquals(xml, "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><billboard background=\"#000000\"/>");
    }

    @Test
    public void TestOnlyMessage() throws Exception {
        billboard.message = "Hello!";
        billboard.messageColor = "#FFFFFF";
        billboard.picture = null;
        billboard.information = null;
        billboard.backgroundColor = "#000000";

        String xml = XML.toXML(billboard);
        assertEquals(xml, "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><billboard background=\"#000000\"><message colour=\"#FFFFFF\">Hello!</message></billboard>");
    }

    @Test
    public  void TestNormalBillboard() throws Exception {
        billboard.message = "Hello!";
        billboard.backgroundColor="#ca26ac";
        billboard.informationColor="f756c5";
        billboard.messageColor = "#FFFFFF";
        billboard.information = "HiHello!";

        String xml = XML.toXML(billboard);
        assertEquals(xml,"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><billboard background=\"#ca26ac\"><message colour=\"#FFFFFF\">Hello!</message><information colour=\"f756c5\">HiHello!</information></billboard>");
    }

    @Test
    public void TestOnlyInformation() throws Exception {
        billboard.message = "Hello!";
        billboard.messageColor = "#FFFFFF";
        billboard.picture = null;
        billboard.information = null;
        billboard.backgroundColor = "#000000";

        String xml = XML.toXML(billboard);
        assertEquals(xml, "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><billboard background=\"#000000\"><message colour=\"#FFFFFF\">Hello!</message></billboard>");
    }

    @Test
    public void TestBillboardNameWithMessage() throws Exception {
        billboard.name = "newBillboard";
        billboard.message = "What!!!";
        billboard.messageColor = "#FFFFFF";
        billboard.picture = null;
        billboard.information = null;
        billboard.backgroundColor = "#000000";

        String xml = XML.toXML(billboard);
        assertEquals(xml, "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><billboard background=\"#000000\"><message colour=\"#FFFFFF\">What!!!</message></billboard>");
    }

    @Test
    public void TestMessageAndInformation() throws Exception {
        billboard.message = "What!!!";
        billboard.messageColor = "#FFFFFF";
        billboard.picture = null;
        billboard.information = "THE";
        billboard.informationColor = "#c6299c";
        billboard.backgroundColor = "#000000";

        String xml = XML.toXML(billboard);
        assertEquals(xml, "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><billboard background=\"#000000\"><message colour=\"#FFFFFF\">What!!!</message><information colour=\"#c6299c\">THE</information></billboard>");
    }

    @Test
    public void TestOnlyPicture() throws Exception {
        billboard.picture = picture;
        billboard.message = null;
        billboard.information = null;
        billboard.backgroundColor = "#000000";

        String xml = XML.toXML(billboard);
        String result = XML.readFile("./src/test/java/common/xml/billboard.xml");
        result = result.replace("\n","");
        assertEquals(result,xml);
    }

    @Test
    public void TestNormalfromXML() throws Exception {
        billboard.message = "What!!!";
        billboard.messageColor = "#FFFFFF";
        billboard.picture = null;
        billboard.information = null;
        billboard.backgroundColor = "#000000";
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><billboard background=\"#000000\"><message colour=\"#FFFFFF\">What!!!</message></billboard>";
        Billboard result = XML.fromXML(xml,billboard);
        assertEquals(result,billboard);
    }

    @Test
    public void TestFromXMLWithOnlyMessage() throws Exception {
        billboard.message = "I like it";
        billboard.messageColor = "#FFFFFF";
        billboard.picture = null;
        billboard.information = null;
        billboard.backgroundColor = "#000000";
        String xml ="<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><billboard background=\"#000000\"><message colour=\"#FFFFFF\">I like it</message></billboard>";
        Billboard result = XML.fromXML(xml,  billboard);
        assertEquals(result, billboard);
    }

    @Test
    public void TestFromXMLWithOnlyInfo() throws Exception {
        billboard.message = null;
        billboard.messageColor = "#FFFFFF";
        billboard.picture = null;
        billboard.information = "I'm twenty five";
        billboard.backgroundColor = "#000000";
        String xml ="<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><billboard background=\"#000000\"><message colour=\"#FFFFFF\">I'm twenty five</message></billboard>";
        Billboard result = XML.fromXML(xml,  billboard);
        assertEquals(result, billboard);
    }

    @Test
    public void TestFromXMLWithNameAndInfoAndColors() throws Exception{
        billboard.message = "I like it";
        billboard.messageColor = "#323ca8";
        billboard.picture = null;
        billboard.information = "I'm truly fine";
        billboard.informationColor = "#a8a032";
        billboard.backgroundColor = "#a532ad";
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><billboard background=\"#a532ad\"><message colour=\"#323ca8\">I like it</message><information colour=\"#a8a032\">I'm truly fine</information></billboard>";
        Billboard result = XML.fromXML(xml,billboard);
        assertEquals(result,billboard);
    }

    @Test
    public void TestFromXMLWithOnlyPicture() throws Exception{
        billboard.picture = picture;
        billboard.message = null;
        billboard.information = null;
        billboard.backgroundColor = "#000000";

        String xml = XML.readFile("./src/test/java/common/xml/billboard.xml");;
        Billboard result = XML.fromXML(xml,billboard);
        assertEquals(result,billboard);
    }

    @Test
    public void TestFullBillboard() throws Exception{
        billboard.message = "I like it";
        billboard.messageColor = "#323ca8";
        billboard.picture = picture;
        billboard.information = "I'm truly fine";
        billboard.informationColor = "#a8a032";
        billboard.backgroundColor = "#a532ad";
        String xml = XML.toXML(billboard);
        String result = XML.readFile("./src/test/java/common/xml/billboard1.xml");
        result = result.replace("\n","");
        assertEquals(result,xml);
    }

    @Test
    public void TestFullInfoFromXML() throws Exception{
        billboard.message = "I like it";
        billboard.messageColor = "#323ca8";
        billboard.picture = picture;
        billboard.information = "I'm truly fine";
        billboard.informationColor = "#a8a032";
        billboard.backgroundColor = "#a532ad";

        String xml = XML.readFile("./src/test/java/common/xml/billboard1.xml");;
        Billboard result = XML.fromXML(xml,billboard);
        assertEquals(billboard,result);
    }
}
