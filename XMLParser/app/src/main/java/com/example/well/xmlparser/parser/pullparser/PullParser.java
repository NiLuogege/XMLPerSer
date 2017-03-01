package com.example.well.xmlparser.parser.pullparser;

import android.util.Xml;

import com.example.well.xmlparser.bean.Book;
import com.example.well.xmlparser.parser.Iparser;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${LuoChen} on 2017/3/1.
 * email:luochen0519@foxmail.com
 */

public class PullParser implements Iparser {

    private ArrayList<Book> mBooks;
    private Book mBook;

    @Override
    public List<Book> parser(InputStream is) throws Exception {
        XmlPullParser parser = Xml.newPullParser(); //由android.util.Xml创建一个XmlPullParser实例
        parser.setInput(is,"UTF-8"); //设置输入流 并指明编码方式

        int eventType = parser.getEventType();
        while (eventType!=XmlPullParser.END_DOCUMENT){
            switch (eventType){
                case XmlPullParser.START_DOCUMENT://是起始节点即创建集合
                    mBooks = new ArrayList<>();
                    break;
                case XmlPullParser.START_TAG:
                    if(parser.getName().equals("book")){
                        mBook = new Book();
                    }else if(parser.getName().equals("id")){
                        eventType=parser.next();
                        mBook.setId(Integer.parseInt(parser.getText()));
                    }else if(parser.getName().equals("name")){
                        eventType=parser.next();
                        mBook.setName(parser.getText());
                    }else if (parser.getName().equals("price")) {
                        eventType = parser.next();
                        mBook.setPrice(Float.parseFloat(parser.getText()));
                    }
                    break;
                case XmlPullParser.END_TAG:
                    if(parser.getName().equals("book")){
                        mBooks.add(mBook);
                        mBook=null;
                    }
                    break;
            }
            eventType=parser.next();
        }
        return mBooks;
    }

    @Override
    public String serialize(List<Book> books) throws Exception {
        XmlSerializer serializer = Xml.newSerializer(); //由android.util.Xml创建一个XmlSerializer实例
        StringWriter writer = new StringWriter();
        serializer.setOutput(writer);   //设置输出方向为writer
        serializer.startDocument("UTF-8", true);
        serializer.startTag("", "books");
        for (Book book : books) {
            serializer.startTag("", "book");
            serializer.attribute("", "id", book.getId() + "");

            serializer.startTag("", "name");
            serializer.text(book.getName());
            serializer.endTag("", "name");

            serializer.startTag("", "price");
            serializer.text(book.getPrice() + "");
            serializer.endTag("", "price");

            serializer.endTag("", "book");
        }
        serializer.endTag("", "books");
        serializer.endDocument();

        return writer.toString();
    }
}
