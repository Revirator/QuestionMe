package nl.tudelft.oopp.demo.controllers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import nl.tudelft.oopp.demo.entities.Question;
import nl.tudelft.oopp.demo.entities.Quote;
import nl.tudelft.oopp.demo.services.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("questions")
public class QuestionController {


    private final QuestionService questionService;

    @Autowired
    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping   // http://localhost:8080/questions
    public List<Question> getQuestions() {
        return questionService.getQuestions();
    }


    @GetMapping("example")   // http://localhost:8080/questions/example
    @ResponseBody               // automatically serialized into JSON
    public Question getExampleQuestion() {
        return new Question(1, 1,  "What is the basis of the zero subspace?", "Nadine");
    }

    @PostMapping   // http://localhost:8080/questions
    public void addNewQuestion(@RequestBody Question question) {
        questionService.addNewQuestion(question);
    }

    @DeleteMapping(path = "{questionId}")   // http://localhost:8080/questions/{questionId} --> EXAMPLE: http://localhost:8080/questions/2
    public void deleteQuestion(@PathVariable("questionId") Long questionId) {
        questionService.deleteQuestion(questionId);
    }

    @PutMapping(path = "{questionId}")   // http://localhost:8080/questions/{questionId}?question=new question? --> EXAMPLE: http://localhost:8080/questions/6?question=Can I refrain from what I said before?
    public void updateQuestion(
            @PathVariable("questionId") Long questionId,
            @RequestParam String question
    ) {
        questionService.updateQuestion(questionId, question);
    }
}