package nl.tudelft.oopp.demo.entities;

public class Student extends User {

    public Student(String username, Room room) {
        super(username, room);
    }

    @Override
    public String toString() {
        return "Student " + super.getNickname() + " in room " + super.getRoom().getRoomId();
    }
}

