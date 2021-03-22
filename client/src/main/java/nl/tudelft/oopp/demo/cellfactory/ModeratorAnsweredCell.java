package nl.tudelft.oopp.demo.cellfactory;

import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import nl.tudelft.oopp.demo.controllers.ModeratorRoomController;
import nl.tudelft.oopp.demo.data.Question;

public class ModeratorAnsweredCell extends ListCell<Question> {

    private AnchorPane anchorPane = new AnchorPane();
    private GridPane gridPane = new GridPane();
    private Question question;
    private ObservableList<Question> answered;
    private boolean editingQuestion;
    private boolean editingAnswer;
    private TextField editableQuestion;
    private TextField editableAnswer;
    private ModeratorRoomController mrc;

    /**
     * Constructor for moderator answer cell.
     * @param answered ObservableList of answered questions
     */
    public ModeratorAnsweredCell(ObservableList<Question> answered,
                                 ModeratorRoomController mrc) {
        super();

        this.answered = answered;
        editingAnswer = false;
        editingQuestion = false;
        editableAnswer = new TextField();
        editableQuestion = new TextField();
        this.mrc = mrc;

        // Create visual cell
        createCell();
    }

    /**
     * Creates a cell for answered questions.
     */
    private void createCell() {

        // Add grid pane to anchor pane
        anchorPane.getChildren().add(gridPane);

        // Create all labels with ID
        Label questionLabel = new Label();
        questionLabel.setId("questionLabel");

        Label upVotesLabel = new Label();
        upVotesLabel.setId("upVotesLabel");

        Label ownerLabel = new Label();
        ownerLabel.setId("ownerLabel");

        Label answerLabel = new Label();
        answerLabel.setId("answerLabel");

        // Position labels
        questionLabel.setAlignment(Pos.CENTER_LEFT);
        ownerLabel.setAlignment(Pos.CENTER_LEFT);
        upVotesLabel.setAlignment(Pos.CENTER_RIGHT);
        answerLabel.setAlignment(Pos.CENTER_LEFT);

        // Create buttons
        Button editAnswerButton = new Button("Edit answer");
        Button editQuestionButton = new Button("Edit question");
        Button deleteButton = new Button("Delete");

        // Align buttons
        editAnswerButton.setAlignment(Pos.CENTER_LEFT);
        editQuestionButton.setAlignment(Pos.CENTER_RIGHT);
        deleteButton.setAlignment(Pos.CENTER_RIGHT);

        // Create wrappers
        HBox answerWrapper = new HBox(answerLabel, editAnswerButton);
        HBox questionWrapper = new HBox(questionLabel, editQuestionButton);

        // Set wrapper spacing
        answerWrapper.setSpacing(5);
        questionWrapper.setSpacing(5);

        // Add elements to grid pane
        gridPane.add(ownerLabel, 0, 0);
        gridPane.add(questionWrapper, 0,1);
        gridPane.add(upVotesLabel, 1,0);
        gridPane.add(answerWrapper, 0,2);
        gridPane.add(deleteButton, 1,2);

        // Give background colours
        gridPane.styleProperty().setValue("-fx-background-color: white");
        anchorPane.styleProperty().setValue("-fx-background-color: #E5E5E5");

        // Align grid pane
        gridPane.setAlignment(Pos.CENTER);

        // Set gaps between rows and columns
        gridPane.setVgap(5);
        gridPane.setHgap(5);

        // Align grid pane in anchor pane
        AnchorPane.setTopAnchor(gridPane, 10.0);
        AnchorPane.setLeftAnchor(gridPane, 10.0);
        AnchorPane.setRightAnchor(gridPane, 10.0);
        AnchorPane.setBottomAnchor(gridPane, 10.0);

        // Click events for buttons

        editQuestionButton.setOnAction(event -> {

            if (this.question == null) {
                return;
            }

            questionWrapper.getChildren().clear();

            // User saves changes
            if (editingQuestion) {

                // Send changes to server
                mrc.editQuestion(
                        this.question, editableQuestion.getText());

                questionWrapper.getChildren().addAll(questionLabel, editQuestionButton);
                question.setText(editableQuestion.getText());
                editQuestionButton.setText("Edit question");
                questionLabel.setText(editableQuestion.getText());
                editingQuestion = false;

            } else { // User wants to make changes

                questionWrapper.getChildren().addAll(editableQuestion, editQuestionButton);
                editableQuestion.setText(question.getText());
                editQuestionButton.setText("Save changes");
                editingQuestion = true;

            }
        });

        editAnswerButton.setOnAction(event -> {

            if (this.question == null) {
                return;
            }

            answerWrapper.getChildren().clear();

            // User saves changes
            if (editingAnswer) {

                mrc.setAnswer(this.question, editableAnswer.getText());

                answerWrapper.getChildren().addAll(answerLabel, editAnswerButton);
                question.setAnswer(editableAnswer.getText());
                editAnswerButton.setText("Edit answer");
                answerLabel.setText("Answer: " + editableAnswer.getText());
                editingAnswer = false;

            } else { // User wants to make changes

                answerWrapper.getChildren().addAll(editableAnswer, editAnswerButton);
                editableAnswer.setText(question.getAnswer());
                editAnswerButton.setText("Save changes");
                editingAnswer = true;

            }
        });

        deleteButton.setOnAction(event -> {

            mrc.deleteQuestion(this.question);

            // Remove question from list
            answered.remove(question);

        });

    }

    /**
     * Updates the item in the ListView.
     * @param item updated item
     * @param empty true if empty, false if not
     */
    @Override
    protected void updateItem(Question item, boolean empty) {

        // Update listview
        super.updateItem(item, empty);

        // Empty list item
        if (empty || item == null) {

            setGraphic(null);
            setText("");

        } else { // Non-empty list item

            // Update question object
            this.question = item;

            // Look for number of votes and question and update
            Label upVotesLabel = (Label) gridPane.lookup("#upVotesLabel");
            upVotesLabel.setText(item.getUpvotes() + " votes");

            Label questionLabel = (Label) gridPane.lookup("#questionLabel");

            // Check if exists
            if (questionLabel != null) {
                questionLabel.setText(item.getText());
            }

            Label ownerLabel = (Label) gridPane.lookup("#ownerLabel");
            ownerLabel.setText(item.getOwner());

            Label answerLabel = (Label) gridPane.lookup("#answerLabel");

            // Check if exists
            if (answerLabel != null) {
                answerLabel.setText("Answer: " + item.getAnswer());
            }

            // Show graphic representation
            setGraphic(anchorPane);
        }
    }
}