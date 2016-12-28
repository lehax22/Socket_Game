package model_fibbage_xl;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Alex on 18.12.2016.
 */
public class Questions {

    public String question;
    public String answer;
    public boolean unread = true;

    public Questions(String str, String s, boolean b) {
        this.question = str;
        this.answer = s;
        this.unread = b;
    }

    public Questions() {

    }

    public List<Questions> readQuestios(){
        BufferedReader br = null;
        FileReader fr = null;

        List<Questions> q = new ArrayList<>();

        try {

            fr = new FileReader("C:\\Users\\Alex\\IdeaProjects\\FibbageXL\\src\\ques.txt");
            br = new BufferedReader(fr);

            String offer = null;

            while ((offer = br.readLine()) != null) {
                q.add(new Questions(offer, br.readLine(),true));
            }

            return q;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}
