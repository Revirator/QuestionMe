package nl.tudelft.oopp.demo.services;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;

import javax.transaction.Transactional;

import nl.tudelft.oopp.demo.entities.Room;
import nl.tudelft.oopp.demo.repositories.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    /** Constructor for RoomService.
     * @param roomRepository - retrieves rooms from database.
     */
    @Autowired
    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    /** Called by RoomController.
     * @return a List of rooms.
     */
    public List<Room> getRooms() {
        return roomRepository.findAll();
    }


    /** Called by RoomController.
     * @param id the id of room.
     * @return the room itself.
     */
    public Room getRoomById(long id) {
        return roomRepository.findById(id);
    }


    /** Called by RoomController.
     * @param string new Room object as a string to be stored in the database
     */
    public Room addNewRoom(String string) throws MalformedURLException {
        String[] dataArray = string.split(", ");

        String roomName = dataArray[0];
        LocalDateTime startingTime = LocalDateTime.parse(dataArray[1]);
        boolean active = Boolean.valueOf(dataArray[2]);

        Room updatedRoom = new Room(startingTime, roomName, active);

        roomRepository.save(updatedRoom);

        return updatedRoom;
    }


    /** Updates peopleThinkingLectureIsTooSlow or peopleThinkingLectureIsTooFast ..
     * .. depending on the feedback received.
     * @param url link connected to the room
     * @param feedback feedback to be processed
     */
    @Transactional
    public void updateRoomSpeed(String url,String feedback) {
        try {
            Room room = roomRepository.findFirstByStudentsLink(new URL(url));
            if (feedback.equals("slow")) {
                room.votedTooSlow();
            }
            if (feedback.equals("fast")) {
                room.votedTooFast();
            } else if (feedback.contains("reset")) {
                room.resetVote(feedback);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    /** Updates the status (active/inactive) of a room.
     * @param link the link to a Lecture
     */
    @Transactional
    public void updateRoomStatusByLink(URL link) {
        Room room = null;
        if (link.toString().contains("M")) {
            room = roomRepository.findFirstByModeratorLink(link);
        } else {
            room = roomRepository.findFirstByStudentsLink(link);
        }
        room.end();
    }

    /** Called by RoomController.
     * @param code the code (link?) of room.
     * @return the room itself.
     */
    public Room getRoomByCode(String code) {
        // The next 10 lines are just because we use URL instead of String
        URI uri = null;
        try {
            uri = new URI(code);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        URL url = null;
        try {
            url = uri.toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        // Check if the code is for a student or a moderator (probably will get changed later)
        if (code.contains("M")) {
            return roomRepository.findFirstByModeratorLink(url);
        } else {
            return roomRepository.findFirstByStudentsLink(url);
        }
    }
}