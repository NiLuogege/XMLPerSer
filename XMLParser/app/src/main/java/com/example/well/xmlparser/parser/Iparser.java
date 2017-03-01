package com.example.well.xmlparser.parser;

import com.example.well.xmlparser.bean.Book;

import java.io.InputStream;
import java.util.List;

/**
 * Created by Well on 2017/3/1.
 */

public interface Iparser {
    /**
     *解析输入流,得到Book对象集合
     * @param is
     * @return
     * @throws Exception
     */
    List<Book> parser(InputStream is) throws Exception;

    /**
     * 将Book集合对象序列化到xml中
     * @param books
     * @return 返回xml类型的字符串
     * @throws Exception
     */
    String serialize(List<Book> books) throws Exception;
}
