package com.chaoyang805.indexablelistview;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.SectionIndexer;

import com.chaoyang805.indexablelistview.view.IndexableListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<String> mItems;
    private IndexableListView mListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView = (IndexableListView) findViewById(R.id.list_view);
        initDatas();
    }

    /**
     * 1.初始化数据
     * 2.根据section 获取position
     */
    private void initDatas() {
        mItems = new ArrayList<>();

        mItems.add("12345");
        mItems.add("A Diary of a Wimpy Kid 6: Cabin fever");
        mItems.add("B Steve Jobs");
        mItems.add("C Inheritance (The Inheritance cycle)");
        mItems.add("D 11/12/63");
        mItems.add("E The Hunger Games");
        mItems.add("F The LEGO Ideas Book");
        mItems.add("G Explosive Eighteeen: A Stephanie Plum Novel");
        mItems.add("H Catching Fire (The Second Book of the Hunger Games");
        mItems.add("I Elder Scrolls V:Skyrim: Prima Official Game Guide");
        mItems.add("J Death Comes to PemberLey");
        mItems.add("K Diary of a Wimpy Kid 6: Cabin fever");
        mItems.add("L Steve Jobs");
        mItems.add("M Inheritance (The Inheritance cycle)");
        mItems.add("N 11/12/63");
        mItems.add("O The Hunger Games");
        mItems.add("P The LEGO Ideas Book");
        mItems.add("Q Explosive Eighteeen: A Stephanie Plum Novel");
        mItems.add("R Catching Fire (The Second Book of the Hunger Games");
        mItems.add("S Elder Scrolls V:Skyrim: Prima Official Game Guide");
        mItems.add("T Death Comes to PemberLey");

        Collections.sort(mItems);

        ContentAdapter adapter = new ContentAdapter(this, android.R.layout.simple_list_item_1, mItems);

        mListView.setAdapter(adapter);

        mListView.setFastScrollEnabled(true);
    }

    private class ContentAdapter extends ArrayAdapter<String> implements SectionIndexer{

        private String mSection = "#ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        public ContentAdapter(Context context, int resource,List<String> objects) {
            super(context, resource,objects);
        }

        @Override
        public String[] getSections() {
            //A B C D E F G ...
            String[] sections = new String[mSection.length()];

            for (int i = 0; i < sections.length; i++) {
                sections[i] = String.valueOf(mSection.charAt(i));
            }
            return sections;
        }

        @Override
        public int getPositionForSection(int sectionIndex) {
            //从当前的index往前查，直到遇到有对应的item
            for (int i = sectionIndex; i >= 0; i--) {
                for (int j = 0; j < getCount(); j++) {
                    if (i == 0) {
                        //查询数字

                    }else {
                        //查询字母

                    }
                }
            }
            return 0;
        }

        @Override
        public int getSectionForPosition(int position) {
            return 0;
        }
    }
}
