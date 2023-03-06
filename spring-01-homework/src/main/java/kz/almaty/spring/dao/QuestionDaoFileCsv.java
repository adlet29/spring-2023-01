package kz.almaty.spring.dao;

import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import kz.almaty.spring.dto.QuestionDto;
import kz.almaty.spring.exceptions.FileNameIllegalArgumentException;
import kz.almaty.spring.exceptions.ReaderRuntimeException;
import kz.almaty.spring.model.Option;
import kz.almaty.spring.model.Question;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class QuestionDaoFileCsv implements QuestionDao {

    private final String fileName;

    public QuestionDaoFileCsv(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public List<Question> findAll() {
        List<Question> questions = new ArrayList<>();
        try (BufferedReader bufferedReader = getFileFromResourceAsStreamReader(fileName);
             CSVReader reader = new CSVReader(bufferedReader)) {
            HeaderColumnNameMappingStrategy<QuestionDto> beanStrategy = new HeaderColumnNameMappingStrategy<>();
            beanStrategy.setType(QuestionDto.class);

            CsvToBean<QuestionDto> csvToBean = new CsvToBean<>();
            csvToBean.setCsvReader(reader);
            csvToBean.setMappingStrategy(beanStrategy);
            List<QuestionDto> list = csvToBean.parse();

            for (QuestionDto questionDto : list) {
                List<Option> options = new ArrayList<>();
                options.add( new Option(questionDto.getOption1(), "A", questionDto.getCorrect().equals("option1")));
                options.add( new Option(questionDto.getOption2(), "B", questionDto.getCorrect().equals("option2")));
                options.add( new Option(questionDto.getOption3(), "C", questionDto.getCorrect().equals("option3")));
                options.add( new Option(questionDto.getOption4(), "D", questionDto.getCorrect().equals("option4")));
                questions.add(new Question(questionDto.getQuestion(), options));
            }
        } catch (IOException e) {
            throw new ReaderRuntimeException("problems reading the file!");
        }

        return questions;
    }

    private BufferedReader getFileFromResourceAsStreamReader(String fileName) {

        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);

        if (inputStream == null) {
            throw new FileNameIllegalArgumentException("file not found! " + fileName);
        }

        return new BufferedReader(new InputStreamReader(inputStream));
    }

}
