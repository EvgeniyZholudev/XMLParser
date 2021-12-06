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


/*
Так, короче, смотри
Этот комент потом стереть не забудь)
Я тебе тут накинул парсер в строку, в мапу и в список сущностей.
Если нужен какой-то один, то удали остальные. Друг на друге они не завязаны.
Большую часть я написал комментарии, если что не понятно - говори.
* */


public class XMLParser {

    public static void main(String[] args) {
        String xmlString = parseXMLToString("src/students.xml");
        System.out.println(xmlString);

        Map<String, Map<String, String>> xmlMap = parseXmlToMap("src/students.xml");
        System.out.println(xmlMap);

        List<Student> xmlList = parseXmlToList("src/students.xml");
        System.out.println(xmlList);
    }

    // Метод парсит xml в строку
    private static String parseXMLToString(String path) {
        // Получаем DOM документ по пути к файлу xml
        Document doc = getDocument(path);
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

    // Метод возвращает DOM документ по пути к файлу
    private static Document getDocument(String path) {
        try {
            // Создаем объект, который будет представлять собой xml файл
            File file = new File(path);
            // Создается построитель документа
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            // Создается дерево DOM документа из файла
            Document result = documentBuilder.parse(file);
            // Приводим xml документ к нормальному виду
            result.getDocumentElement().normalize();
            return result;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    // Метод парсит xml в список
    private static List<Student> parseXmlToList(String path) {
        // Получаем DOM документ по пути к файлу xml
        Document doc = getDocument(path);
        // Получаем список нод с тэгом "Student"
        NodeList students = doc.getElementsByTagName("Student");
        List<Student> result = new ArrayList<>();
        for (int i = 0; i < students.getLength(); i++) {
            Node studentNode = students.item(i);
            if (studentNode.getNodeType() == Node.ELEMENT_NODE) {
                // Если нода является элементом, приводим её к типу Element
                Element studentElement = (Element) studentNode;
                Student student = new Student();
                // Получаем аттрибут id и добавляем его в мапу свойств
                student.setId(studentElement.getAttribute("id"));
                // Получаем свойства и проставляем их в сущность Student
                student.setFirstName(getStringProperty(studentElement, "FirstName"));
                student.setSecondName(getStringProperty(studentElement, "SecondName"));
                student.setAge(getIntProperty(studentElement, "Age"));
                student.setUniversity(getStringProperty(studentElement, "University"));
                student.setCourse(getIntProperty(studentElement, "Course"));
                student.setGroup(getStringProperty(studentElement, "Group"));
                // Добавляем в список студентов нового с его свойствами
                result.add(student);
            }
        }
        return result;
    }

    // Метод возвращает строковое значение свойства студента
    private static String getStringProperty(Element studentElement, String propertyName){
        return studentElement.getElementsByTagName(propertyName).item(0).getChildNodes().item(0).getNodeValue();
    }
    // Метод возвращает числовое значение свойства студента
    private static int getIntProperty(Element studentElement, String propertyName){
        return Integer.parseInt(getStringProperty(studentElement, propertyName));
    }

    // Метод парсит xml в мапу
    private static Map<String, Map<String, String>> parseXmlToMap(String path) {
        // Получаем DOM документ по пути к файлу xml
        Document doc = getDocument(path);
        // Получаем список нод с тэгом "Student"
        NodeList students = doc.getElementsByTagName("Student");
        Map<String, Map<String, String>> result = new HashMap<>();
        for (int i = 0; i < students.getLength(); i++) {
            Node studentNode = students.item(i);
            if (studentNode.getNodeType() == Node.ELEMENT_NODE) {
                // Если нода является элементом, приводим её к типу Element
                Element studentElement = (Element) studentNode;
                // Создаем мапу, которая будет хранить свойства
                Map<String, String> properties = new HashMap<>();
                // Получаем аттрибут id
                String id = studentElement.getAttribute("id");
                // Получаем список нод, находящихся в ноде Student
                NodeList studentProperties = studentElement.getChildNodes();
                for (int j = 0; j < studentProperties.getLength(); j++) {
                    Node studentProperty = studentProperties.item(j);
                    if (studentProperty.getNodeType() != Node.TEXT_NODE) {
                        // Если нода не текстовая, то добавляем её в мапу
                        properties.put(studentProperty.getNodeName(), studentProperty.getChildNodes().item(0).getTextContent());
                    }
                }
                // Добавляем в мапу свойства студента по его id
                result.put(id, properties);
            }
        }
        return result;
    }

}
