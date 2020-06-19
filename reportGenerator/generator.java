import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class generator {
    private static int width;
    private static int height;
    private static String columnId;
    private static String columnDate;
    private static String columnPerson;
    private static int columnIdWidth;
    private static int columnDateWidth;
    private static int columnPersonWidth;
    private static String tableFormat;

    public static void main(String[] args) {
        //загрузка настроек из XML, инициализация параметров таблицы
        loadXmlSettings(args);
        setTableFormat();

        //чтения файла с данными и создание отчета
        try {
            //FileInputStream fileIn = new FileInputStream("/Users/aleksandrartuskin/Desktop/Simple_test_task (2)/source-data.tsv");
            FileInputStream fileIn = new FileInputStream(System.getProperty("user.dir") + "/" + args[1]);
            BufferedReader readFile = new BufferedReader(new InputStreamReader(fileIn, StandardCharsets.UTF_16));

            //FileOutputStream file = new FileOutputStream("/Users/aleksandrartuskin/Desktop/example-report.txt");
            FileOutputStream file = new FileOutputStream(System.getProperty("user.dir") + "/" + args[2]);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(file, StandardCharsets.UTF_16));

            List<String> pages = getPages(readFile);
            for (String s : pages) {
                writer.write(s);
                writer.newLine();
            }

            readFile.close();
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //получение готового отчета
    public static List<String> getPages(BufferedReader readFile) throws IOException {
        List<String> pages;                         //готовый отчет
        List<String> header;                        //заголовок таблицы
        List<String> dataLine;                      //преобразованная строка с данными
        String borderChar;                          //разделитель листов "~"
        int lineCount;                              //счетчик строк на листе

        //инициализация элементов
        pages = new ArrayList<>();
        String headerString = columnId + "\t" + columnDate + "\t" + columnPerson;
        header = getDataLine(new BufferedReader(new StringReader(headerString)));
        borderChar = "~";
        lineCount = 0;

        //создание заголовка первого листа
        pages.addAll(header);
        lineCount++;
        lineCount++;

        //получение строк с данными, сборка страниц в соответсвии с условиями
        while (readFile.ready()) {
            dataLine = getDataLine(readFile);
            if (lineCount + dataLine.size() < height) {
                for (String dataLines : dataLine) {
                    pages.add(dataLines);
                    lineCount++;
                }
            } else if (lineCount + dataLine.size() == height) {
                pages.addAll(dataLine);
                lineCount = 0;
                pages.add(borderChar);
                pages.addAll(header);
                lineCount++;
                lineCount++;
            } else if (lineCount + dataLine.size() > height) {
                lineCount = 0;
                pages.add(borderChar);
                pages.addAll(header);
                lineCount++;
                lineCount++;
                for (String dataLines : dataLine) {
                    pages.add(dataLines);
                    lineCount++;
                }
            }
        }
        return pages;
    }

    //получение преобразованной строки с данными в соответствии с параметрами
    public static List<String> getDataLine(BufferedReader readFile) throws IOException {
        List<String> dataLine = new ArrayList<>();

        //получение строки и разделение на элементы
        String[] data = readFile.readLine().split("\t");
        String id = data[0].trim();
        String date = data[1].trim();
        String person = data[2].trim();

        //проверка ширины элементов в соответствии с заданной шириной колонок
        //если какой-либо из элементов не помещается в ячейке, то разбиваем строку на подстроки
        if (id.length() > columnIdWidth || date.length() > columnDateWidth || person.length() > columnPersonWidth) {
            List<String> idChanged = getChangedLenth(id, columnIdWidth);
            List<String> dateChanged = getChangedLenth(date, columnDateWidth);
            List<String> personChanged = getChangedLenth(person, columnPersonWidth);

            List<Integer> maxLines = new ArrayList<>();
            maxLines.add(idChanged.size());
            maxLines.add(dateChanged.size());
            maxLines.add(personChanged.size());
            Collections.sort(maxLines);

            for (int i = 0; i < maxLines.get(maxLines.size() - 1); i++) {
                String idCell;
                String dateCell;
                String personCell;
                try {
                    idCell = idChanged.get(i);
                } catch (IndexOutOfBoundsException e) {
                    idCell = "";
                }
                try {
                    dateCell = dateChanged.get(i);
                } catch (IndexOutOfBoundsException e) {
                    dateCell = "";
                }
                try {
                    personCell = personChanged.get(i);
                } catch (IndexOutOfBoundsException e) {
                    personCell = "";
                }

                String subLine = String.format(tableFormat, idCell, dateCell, personCell);
                dataLine.add(subLine);
            }

        } else {
            String line = String.format(tableFormat, id, date, person);
            dataLine.add(line);
        }
        dataLine.add(getBorderLine());
        return dataLine;
    }

    public static List<String> getChangedLenth(String text, int width) {
        //паттерн разделения строки ячейки на подстройки
        String patternString = "(\\w|[а-яА-Я0-9_])*\\W|[^а-яА-Я]*";

        //получение вспомогательного списка всех слов из строки
        List<String> listStart = new ArrayList<>();
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            String ss = text.substring(matcher.start(), matcher.end());
            if (!ss.equals("") && !ss.equals(" ")) {
                listStart.add(ss);
            }
        }

        //разделение слов на подстроки, если они ввыходят за допустимые параметры ширины столбца
        //занесение результата во второй вспомогательный список
        List<String> listTemp = new ArrayList<>();
        for (int i = 0; i < listStart.size(); i++) {
            if (listStart.get(i).trim().length() > width) {
                String workString = listStart.get(i);
                int count = workString.trim().length() / width + 1;
                int currentPos = 0;
                for (int j = 1; j <= count; j++) {
                    if (j == count) {
                        String subString = workString.substring(currentPos);
                        listTemp.add(subString);
                    } else {
                        String subString = workString.substring(currentPos, currentPos + width);
                        listTemp.add(subString);
                        currentPos += width;
                    }
                }
            } else if (listStart.get(i).trim().length() == width) {
                listTemp.add(listStart.get(i).trim());
            } else {
                listTemp.add(listStart.get(i));
            }
        }

        //сборка готового списка с подстроками ячейки в соответствии с заданной шириной столбца
        List<String> listFinal = new ArrayList<>();
        StringBuilder line = new StringBuilder();
        for (String stringTemp : listTemp) {
            if (line.length() + stringTemp.length() < width) {
                line.append(stringTemp);
            } else if (line.length() + stringTemp.trim().length() == width) {
                line.append(stringTemp);
                listFinal.add(line.toString());
                line = new StringBuilder();
            } else if (line.length() + stringTemp.length() == width) {
                line.append(stringTemp);
                listFinal.add(line.toString());
                line = new StringBuilder();
            }else if (line.length() + stringTemp.length() > width) {
                listFinal.add(line.toString());
                line = new StringBuilder("" + stringTemp);
            }
        }
        if (!line.toString().equals("")) {
            listFinal.add(line.toString());
        }

        return listFinal;
    }

    //получение разделителя "-----" в соответствии с заданной шириной
    public static String getBorderLine() {
        StringBuilder separator = new StringBuilder();
        for (int i = 0; i < width; i++) {
            separator.append("-");
        }
        return separator.toString();
    }

    //загрузка настроек форматирования таблицы из XML файла
    public static void loadXmlSettings(String[] args) {
        try {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            //Document document = documentBuilder.parse("/Users/aleksandrartuskin/Desktop/Simple_test_task (2)/settings.xml");
            Document document = documentBuilder.parse(System.getProperty("user.dir") + "/" + args[0]);
            Node root = document.getDocumentElement();
            NodeList sections = root.getChildNodes();
            Node page = sections.item(1);
            Node columns = sections.item(3);

            //получение параметров страницы: width, height
            NodeList pageParams = page.getChildNodes();

            //получение параметров первого столбца: title, width
            NodeList column1 = columns.getChildNodes();
            NodeList column1Params = column1.item(1).getChildNodes();

            //получение параметров второго столбца: title, width
            NodeList column2 = columns.getChildNodes();
            NodeList column2Params = column2.item(3).getChildNodes();

            //получение параметров третьего столбца: title, width
            NodeList column3 = columns.getChildNodes();
            NodeList column3Params = column3.item(5).getChildNodes();

            //инициализация параметров таблицы
            width = Integer.parseInt(pageParams.item(1).getTextContent());
            height = Integer.parseInt(pageParams.item(3).getTextContent());
            columnId = column1Params.item(1).getTextContent();
            columnDate = column2Params.item(1).getTextContent();
            columnPerson = column3Params.item(1).getTextContent();
            columnIdWidth = Integer.parseInt(column1Params.item(3).getTextContent());
            columnDateWidth = Integer.parseInt(column2Params.item(3).getTextContent());
            columnPersonWidth = Integer.parseInt(column3Params.item(3).getTextContent());
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            ex.printStackTrace(System.out);
        }
    }

    //инициализация форматирования таблицы
    public static void setTableFormat() {
        String columnNumb = "%-" + columnIdWidth + "." + columnIdWidth + "s";
        String columnDate = "%-" + columnDateWidth + "." + columnDateWidth + "s";
        String columnFio = "%-" + columnPersonWidth + "." + columnPersonWidth + "s";
        tableFormat = "| " + columnNumb + " | " + columnDate + " | " + columnFio + " |";
    }
}
