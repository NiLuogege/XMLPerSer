package com.example.well.xmlparser.parser.saxparser;

import com.example.well.xmlparser.bean.Book;
import com.example.well.xmlparser.parser.Iparser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.DefaultHandler;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

/**
 * Created by ${LuoChen} on 2017/3/1.
 * email:luochen0519@foxmail.com
 */

public class SaxParser implements Iparser {
    @Override
    public List<Book> parser(InputStream is) throws Exception {
        SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();//获得Sax解析器
        ParserHandler handler = new ParserHandler();
        saxParser.parse(is,handler);//解析xml
        return handler.getBooks();//获取结果
    }

    @Override
    public String serialize(List<Book> books) throws Exception {
        SAXTransformerFactory factory = (SAXTransformerFactory) TransformerFactory.newInstance();//取得SAXTransformerFactory实例
        TransformerHandler handler = factory.newTransformerHandler(); //从factory获取TransformerHandler实例
        Transformer transformer = handler.getTransformer(); //从handler获取Transformer实例
        transformer.setOutputProperty(OutputKeys.ENCODING,"UTF-8"); // 设置输出采用的编码方式
        transformer.setOutputProperty(OutputKeys.INDENT,"yes");// 是否自动添加额外的空白
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION,"no");// 是否忽略XML声明

        StringWriter stringWriter = new StringWriter();
        Result result = new StreamResult(stringWriter);
        handler.setResult(result);

        String uri="";//代表命名空间的URI 当URI无值时 须置为空字符串
        String localName=""; //命名空间的本地名称(不包含前缀) 当没有进行命名空间处理时 须置为空字符串  \

        handler.startDocument();
        handler.startElement(uri,localName,"bools",null);

        AttributesImpl attributes = new AttributesImpl();//负责存放元素的属性信息
        char[] chars = null;
        for (Book book : books) {
            attributes.clear();
            attributes.addAttribute(uri,localName,"id","String",String.valueOf(book.getId()));//添加一个名为id的属性(type影响不大,这里设为string)--->不是成对出现的标签

            handler.startElement(uri,localName,"book",attributes); //开始一个book元素 关联上面设定的id属性

            handler.startElement(uri,localName,"name",null);//开始一个name元素 没有属性
            chars=String.valueOf(book.getName()).toCharArray();
            handler.characters(chars,0,chars.length); //设置name的值
            handler.endElement(uri,localName,"name");//设置name元素的文本节点

            handler.startElement(uri, localName, "price", null);//开始一个price元素 没有属性
            chars = String.valueOf(book.getPrice()).toCharArray();
            handler.characters(chars, 0, chars.length);   //设置price元素的文本节点
            handler.endElement(uri, localName, "price");

            handler.endElement(uri, localName, "book");
        }
        handler.endElement(uri, localName, "books");
        handler.endDocument();

        return stringWriter.toString();
    }

    private  class ParserHandler extends DefaultHandler{

        private List<Book> mBooks;
        private StringBuilder mBuilder;
        private Book mBook;

        public List<Book> getBooks(){
            return mBooks;
        }

        @Override
        public void startDocument() throws SAXException {
            super.startDocument();
            mBooks = new ArrayList<>();
            mBuilder = new StringBuilder();
        }

        /**
         * 当遇到起始节点的时候会调用
         * @param uri
         * @param localName
         * @param qName
         * @param attributes
         * @throws SAXException
         */
        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            super.startElement(uri, localName, qName, attributes);
            if(localName.equals("book")){
                mBook = new Book();
            }
            mBuilder.setLength(0);//将字符长度设置为0 以便重新开始读取元素内的字符节点
        }

        /**
         * 在起始节点和结束节点中间调用
         * @param ch
         * @param start
         * @param length
         * @throws SAXException
         */
        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            super.characters(ch, start, length);
            mBuilder.append(ch,start,length);//将读取的字符数组追加到builder中
        }

        /**
         * 当遇到结束节点的时候会调用
         * @param uri
         * @param localName
         * @param qName
         * @throws SAXException
         */
        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            super.endElement(uri, localName, qName);
            if(localName.equals("id")){
                mBook.setId(Integer.parseInt(mBuilder.toString()));
            }else if(localName.equals("name")){
                mBook.setName(mBuilder.toString());
            }else if(localName.equals("price")){
                mBook.setPrice(Float.parseFloat(mBuilder.toString()));
            }else if(localName.equals("book")){
                mBooks.add(mBook);
            }

        }
    }
}
