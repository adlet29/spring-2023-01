package kz.almaty.spring.dao;

import kz.almaty.spring.model.Option;
import kz.almaty.spring.model.Question;
import kz.almaty.spring.util.ScannerUtil;
import kz.almaty.spring.exceptions.FileIndexOutOfBoundsException;
import kz.almaty.spring.exceptions.FileNullPointerException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class QuestionDaoFileCsv implements QuestionDao {

    private final String name;

    public QuestionDaoFileCsv(String resourceName) {
        this.name = resourceName;
    }

    @Override
    public List<Question> findAll() {
        List<Question> questionList = new ArrayList<>();
        ScannerUtil scannerUtil = new ScannerUtil();
        try {
            List<List<String>> listList = scannerUtil.getRecords(this.name);
            String header = getHeaderLine(listList);
            String[] headerArr = header.split(";");
            int j = 0;
            listList.remove(0);
            for (List<String> list : listList) {
                List<Option> answerList = new ArrayList<>();
                for (String value : list) {
                    String[] arr = value.split(";");
                    if (j + 1 == Integer.parseInt(arr[0])) {
                        answerList.add(new Option(arr[3], headerArr[3], arr[1].equals(headerArr[3])));
                        answerList.add(new Option(arr[4], headerArr[4], arr[1].equals(headerArr[4])));
                        answerList.add(new Option(arr[5], headerArr[5], arr[1].equals(headerArr[5])));
                        answerList.add(new Option(arr[6], headerArr[6], arr[1].equals(headerArr[6])));
                        Question question = new Question(arr[2], answerList);
                        questionList.add(question);
                    } else {
                        throw new FileIndexOutOfBoundsException("Given file item index is out of range");
                    }
                }
                j++;
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return questionList;
    }


    private String getHeaderLine(List<List<String>> listList) {
        if (listList.isEmpty() || listList.get(0).isEmpty()) {
            throw new FileNullPointerException("File content is empty");
        }
        return listList.get(0).get(0);
    }

}
