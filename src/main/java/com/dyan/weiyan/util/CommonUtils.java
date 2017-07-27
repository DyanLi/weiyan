package com.dyan.weiyan.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Dyan on 17/7/19.
 */
public class CommonUtils {

    public static class MyUser {
        private int age;
        private String name;
        public MyUser( int age, String name){
            this.age = age;
            this.name = name;
        }

    }

    public static void print (int index, Object obj){
        System.out.println(String.format("{%d}, %s", index, String.valueOf(obj)));
    }

    public static void StringUtil(){
        // a!=null && a
        print(1, StringUtils.isBlank(null));
        print(2, StringUtils.isBlank("\t\n"));
        print(3, StringUtils.isAllBlank("\t","\n"));
        print(4, StringUtils.isAllBlank(new String [] {"\t", "\n", "me"}));

        print(5, StringUtils.compare("hello","hallo"));
        print(6, StringUtils.countMatches("hello","l"));

        print(7, StringUtils.join(new int[] {1,2,3}, '_'));
        print(7, StringUtils.join(new String [] {"a.png", "b.png"}, '|'));
        print(8, StringUtils.remove("hello world", "l"));

        print(9, StringUtils.rightPad("12345",9,'#'));
        print(10, StringUtils.reverse("12345"));

        print(11, StringUtils.isNumeric("123"));
        print(12, StringUtils.deleteWhitespace("sjdfj jsfj jdfj \n \t"));

        print(13, ToStringBuilder.reflectionToString(new MyUser(18,"momo"), ToStringStyle.JSON_STYLE));

    }

    public static void DatetimeUtil() throws ParseException {
        Date date = DateUtils.parseDate("2017/7/7","yyyy/MM/dd","yyyy-MM-dd");
        print(1, date);
        print(2, DateFormatUtils.format(date, "yyyy-MM-dd HH:mm:ss"));

        print(3, DateUtils.addDays(date, 9));
        print(3, date);
        print(4, new Date().after(date));

    }

    public static void HTMLUtil(){
        String html = "<b>hello</b>";
        String escapeHtml = StringEscapeUtils.escapeHtml4(html);
        print(1, escapeHtml);
        print(2, StringEscapeUtils.unescapeHtml4(escapeHtml));
    }

    public static void JSONUtil(){
        String str = "{\"a\":1}";
        JSONObject jsonObject = JSON.parseObject(str);
        jsonObject.put("b", new int[] {1,2,3});
        jsonObject.put("c", new Date());
        jsonObject.put("d", 2.2f);
        jsonObject.put("e", false);
        print(1, jsonObject);

        Map<String, String> strmap = new HashMap<>();
        strmap.put("s1", "s1");
        strmap.put("s2", "s2");
        jsonObject.put("ssm", strmap);
        print(2, jsonObject);

        String str2 = "{\"students\": [\"Jane Thomas\", \"Bob Roberts\", \"Robert Bobert\"]}";
        JSONObject jsonObject2 = JSON.parseObject(str2);
        print(3, jsonObject2);

        print(4, jsonObject2.get("students"));
        print(5, jsonObject.getBoolean("e"));

        jsonObject.put("jsonoj2", jsonObject2);
        print(6, jsonObject);
        print(7, jsonObject.getJSONObject("jsonoj2"));

        JSONArray b = jsonObject.getJSONArray("b");
        b.add(10);
        print(8, b);

        print(9,jsonObject.toJSONString());

    }

    public static void XMLUtil() throws DocumentException {

        String xml = //"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<note name=\"dyan\">\n" +
                "  <to>Tove</to>\n" +
                "  <from>Jani</from>\n" +
                "  <heading>Reminder</heading>\n" +
                "  <body>Don't forget me this weekend!</body>\n" +
                "</note>";
        Document doc = DocumentHelper.parseText(xml);
        Element ele = doc.getRootElement();
        print(1, ele.getName());
        print(2, ele.element("from").getData());
        print(3, ele.element("from").getStringValue());
        print(4, ele.attribute("name").getValue());
        print(5, ele.attribute(0).getValue());

        ele.addAttribute("type","user");
        print(6, ele.asXML());

        Element subnode = DocumentHelper.createElement("subnode");
        subnode.setText("\n subnodecontent \t\n");
        ele.add(subnode);
        print(7, ele.asXML());

        ele.remove(ele.element("from"));
        print(8, ele.asXML());

        Iterator it = ele.elementIterator();
        while (it.hasNext()){
            Element e = (Element) it.next();
            print( 9, e.asXML());
        }
    }

    public static StringBuilder printXML(Element ele, int depth, StringBuilder sb) {
        sb.append(StringUtils.rightPad("", depth, '\t') + "<" + ele.getName());
        if (ele.attributeCount() != 0) {
            for (int i = 0; i < ele.attributeCount(); i++) {
                sb.append(" " + ele.attribute(i).getName() + "=" + ele.attribute(i).getValue());
            }
        }
        sb.append(depth + ">");
        if (ele.elements().size() == 0){
            sb.append(ele.getStringValue());
            sb.append("</" + ele.getName() + ">" + "\n");
        } else {
            sb.append("\n");
            depth +=1 ;
            for (int i = 0; i < ele.elements().size(); i++) {
                Element e = (Element) ele.elements().get(i);
                printXML(e, depth, sb);
            }
        }
        if (ele.elements().size() != 0) {
            sb.append(StringUtils.rightPad("", depth -=1, '\t') + "</" + ele.getName() + ">" + "\n");
        }
        return sb;

    }


    public static void XMLUtilHomework() throws DocumentException {
        String str = "<bookstore>\n" +
                "  <book category=\"cooking\">\n" +
                "    <title lang=\"en\">Everyday Italian</title>\n" +
                "    <author>Giada De Laurentiis</author>\n" +
                "    <year>2005</year>\n" +
                "    <price>30.00</price>\n" +
                "  </book>\n" +
                "  <book category=\"children\">\n" +
                "    <title lang=\"en\">Harry Potter</title>\n" +
                "    <author>J K. Rowling</author>\n" +
                "    <year>2005</year>\n" +
                "    <price>29.99</price>\n" +
                "  </book>\n" +
                "  <book category=\"web\">\n" +
                "    <title lang=\"en\">Learning XML</title>\n" +
                "    <author>Erik T. Ray</author>\n" +
                "    <year>2003</year>\n" +
                "    <price>39.95</price>\n" +
                "  </book>\n" +
                "</bookstore>";

        //str = StringUtils.deleteWhitespace(str);
        Document doc = DocumentHelper.parseText(str);
        Element ele = doc.getRootElement();
        StringBuilder sb = new StringBuilder();
        System.out.println(printXML(ele, 0, sb));

    }


    public static void OfficeUtil() throws IOException {
        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet("test");

        Row row = sheet.createRow(3);
        Cell cell = row.createCell(3);
        cell.setCellValue(2.0);
        cell.setCellType(CellType.NUMERIC);

        CellStyle style = workbook.createCellStyle();
        style.setFillBackgroundColor(IndexedColors.GREEN.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderRight(BorderStyle.DASH_DOT);
        style.setLeftBorderColor(IndexedColors.RED.getIndex());
        cell.setCellStyle(style);

        sheet.addMergedRegion(new CellRangeAddress(1,5,8,10));
        workbook.write(new FileOutputStream("my.xls"));

    }

    public static void PDFUtil(){
        //WKTMLTOPDF

    }

    public static void main(String [] args){
        try {
//            StringUtil();
//            DatetimeUtil();
//            HTMLUtil();
//            JSONUtil();
//            XMLUtil();
            XMLUtilHomework();
//            OfficeUtil();
//            PDFUtil();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
