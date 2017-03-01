package com.example.well.xmlparser;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.well.xmlparser.bean.Book;
import com.example.well.xmlparser.parser.Iparser;
import com.example.well.xmlparser.parser.pullparser.PullParser;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "XML";

    private Iparser parser;
    private List<Book> books;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button readBtn = (Button) findViewById(R.id.readBtn);
        Button writeBtn = (Button) findViewById(R.id.writeBtn);

        readBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    InputStream is = getAssets().open("books.xml");
//                    parser = new SaxParser();  //创建SaxBookParser实例
                    parser= new PullParser();
                    books = parser.parser(is);  //解析输入流
                    for (Book book : books) {
                        Log.e(TAG, book.toString());
                    }
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        });
        writeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String xml = parser.serialize(books);  //序列化
                    FileOutputStream fos = openFileOutput("books.xml", Context.MODE_PRIVATE);//如果文件不存在会自己创建然后保存在data/data/包名/fiels目录下
                    fos.write(xml.getBytes("UTF-8"));
                } catch (Exception e) {
//                    Log.e(TAG, e.getMessage());
                }
            }
        });
    }
}
