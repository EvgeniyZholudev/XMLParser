import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XMLParser {

    public static void main(String[] args) {
        String xmlString = parseXMLToString("src/students.xml");
        System.out.println(xmlString);

        List<Map<String, String>> xmlList = parseXmlToList("src/students.xml");
        System.out.println(xmlList);
    }

    private static String parseXMLToString(String path) {
        // Получаем DOM документ по пути к файлу xml
        Document doc = getDocument(path);
        // Приводим xml документ к нормальному виду
        doc.getDocumentElement().normalize();
        // Получаем список нод с тэгом "Student"
        NodeList students = doc.getElementsByTagName("Student");
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < students.getLength(); i++) {
            Node studentNode = students.item(i);
            if (studentNode.getNodeType() == Node.ELEMENT_NODE) {
                // Если нода является элементом, приводим её к типу Element
                Element studentElement = (Element) studentNode;
                // Получаем аттрибут id и добавляем в строку
                result.append("Student № ").append(studentElement.getAttribute("id")).append("\n");
                // Получаем список нод, находящихся в ноде Student
                NodeList studentProperties = studentElement.getChildNodes();
                for (int j = 0; j < studentProperties.getLength(); j++) {
                    Node studentProperty = studentProperties.item(j);
                    if (studentProperty.getNodeType() != Node.TEXT_NODE) {
                        // Если нода не текстовая, то добавляем её название и значение в строку
                        result.append(studentProperty.getNodeName())
                                .append(":")
                                .append(studentProperty.getChildNodes().item(0).getTextContent())
                                .append("\n");
                    }
                }
                result.append("==========================>\n");
            }
        }
        return result.toString();

    }

    private static Document getDocument(String path) {
        try {
            // Создаем объект, который будет представлять собой xml файл
            File file = new File(path);
            // Создается построитель документа
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            // Создается дерево DOM документа из файла
            return documentBuilder.parse(file);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private static List< Map<String, String>> parseXmlToList(String path) {
        // Получаем DOM документ по пути к файлу xml
        Document doc = getDocument(path);
        // Приводим xml документ к нормальному виду
        doc.getDocumentElement().normalize();
        // Получаем список нод с тэгом "Student"
        NodeList students = doc.getElementsByTagName("Student");
        List< Map<String, String>> result = new ArrayList<>();
        for (int i = 0; i < students.getLength(); i++) {
            Node studentNode = students.item(i);
            if (studentNode.getNodeType() == Node.ELEMENT_NODE) {
                // Если нода является элементом, приводим её к типу Element
                Element studentElement = (Element) studentNode;
                // Создаем мапу, которая будет хранить свойства
                Map<String, String> properties = new HashMap<>();
                // Получаем аттрибут id и добавляем его в мапу свойств
                properties.put("id", studentElement.getAttribute("id"));
                // Получаем список нод, находящихся в ноде Student
                NodeList studentProperties = studentElement.getChildNodes();
                for (int j = 0; j < studentProperties.getLength(); j++) {
                    Node studentProperty = studentProperties.item(j);
                    if (studentProperty.getNodeType() != Node.TEXT_NODE) {
                        // Если нода не текстовая, то добавляем её в мапу
                        properties.put(studentProperty.getNodeName(), studentProperty.getChildNodes().item(0).getTextContent());
                    }
                }
                // Добавляем в список студентов нового с его свойствами
                result.add(properties);
            }
        }
        return result;

    }
}
