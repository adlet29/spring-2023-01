package kz.almaty.spring.service;

import kz.almaty.spring.dao.QuestionDao;
import kz.almaty.spring.model.Question;

import java.util.List;

public class QuestionServiceImpl implements QuestionService {
    private final IOService ioService;
    private final QuestionDao dao;

    public QuestionServiceImpl(IOService ioService, QuestionDao dao) {
        this.ioService = ioService;
        this.dao = dao;
    }

    @Override
    public void showQuestions() {
        List<Question> questionList =dao.findAll();
        for (Question question : questionList) {
            ioService.outputString(question.getQuestion());
            ioService.outputStringSimple(question.getOption1());
            ioService.outputStringSimple(" ");
            ioService.outputStringSimple(question.getOption2());
            ioService.outputStringSimple(" ");
            ioService.outputStringSimple(question.getOption3());
            ioService.outputStringSimple(" ");
            ioService.outputStringSimple(question.getOption4());
            ioService.outputString("");
        }
    }
}
