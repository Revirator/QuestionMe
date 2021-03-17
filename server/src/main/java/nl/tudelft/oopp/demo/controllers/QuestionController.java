package nl.tudelft.oopp.demo.controllers;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import nl.tudelft.oopp.demo.entities.Question;
import nl.tudelft.oopp.demo.entities.Room;
import nl.tudelft.oopp.demo.services.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @GetMapping("/{roomID}") // http://localhost:8080/questions/{roomID}
    @ResponseBody
    public List<Question> getQuestionsByRoom(@PathVariable long roomID) {
        return questionService.getQuestionsByRoom(roomID);
    }

    /** GET mapping.
     * @return a JSON object of an example Question
     */
    @GetMapping("example")   // http://localhost:8080/questions/example
    @ResponseBody               // automatically serialized into JSON
    public Question getExampleQuestion() {
        return new Question(1,
                new Room(LocalDateTime.of(2021, Month.APRIL, 17, 12, 45, 00),
                        "OOP Project", false),
                "What is the basis of the zero subspace?", "Nadine", 55);
    }

    //    @PostMapping   // http://localhost:8080/questions
    //    public void addNewQuestion(@RequestBody Question question) {
    //    questionService.addNewQuestion(question);
    //    }

    @PostMapping   // http://localhost:8080/questions
    public Long addNewQuestion(@RequestBody String payload) {
        return questionService.addNewQuestion(payload);
    }

    @GetMapping("/answered/{roomId}") // http://localhost:8080/questions/answered/{roomId}
    @ResponseBody
    public List<Question> getAnsweredQuestions(@PathVariable long roomId) {
        return questionService.getAnsweredQuestions(roomId);
    }

    @PutMapping("/markAnswered/{questionId}") // http://localhost:8080/questions/markAnswered/{questionId}
    public void markQuestionAsAnswered(@PathVariable long questionId) {
        questionService.markQuestionAsAnswered(questionId);
    }

    @DeleteMapping(path = "{questionId}")   // http://localhost:8080/questions/{questionId}
    public void deleteQuestion(@PathVariable("questionId") Long questionId) {
        questionService.deleteQuestion(questionId);
    }

    @PutMapping(path = "{questionId}")   // http://localhost:8080/questions/{questionId}
    public void updateQuestion(@PathVariable("questionId") Long questionId,
                               @RequestBody String question) {
        questionService.updateQuestion(questionId, question);
    }

    @PutMapping(path = "upvote/{questionId}")  // http://localhost:8080/questions/upvote/{questionId}
    public void upvote(@PathVariable("questionId") Long questionId) {
        questionService.upvote(questionId);
    }

    @PutMapping(path = "deupvote/{questionId}") // http://localhost:8080/questions/deupvote/{questionId}
    public void deUpvote(@PathVariable("questionId") Long questionId) {
        questionService.deUpvote(questionId);
    }
}
