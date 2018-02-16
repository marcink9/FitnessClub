package sample.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.database.DBConnection;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Marcin on 15.02.2018.
 */
public class RoomDAO {

    private static final String TABLE_NAME = "ROOM";
    private static final String INSERT_STATEMENT = "INSERT INTO " + TABLE_NAME + " VALUES (?,?)";
    private static final String SELECT_BY_ID_STATEMENT = "SELECT * FROM " + TABLE_NAME + " WHERE RoomID = ?";
    private static final String SELECT_ALL_STATEMENT = "SELECT * FROM " + TABLE_NAME;
    private static final String DELETE_ALL_STATEMENT = "DELETE FROM " + TABLE_NAME;

    private RoomDAO() {
    }

    public static void insertRoom(Room room) throws SQLException, ClassNotFoundException {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_STATEMENT)) {
            String roomName = room.getRoomName();
            BigDecimal pricePerHour = room.getPricePerHour();
            statement.setString(1, roomName);
            statement.setBigDecimal(2,pricePerHour);
            statement.execute();
        }
    }

    public static Room selectByID(int id) throws SQLException, ClassNotFoundException {

        Room room = null;
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_BY_ID_STATEMENT)) {
            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) { //magic while
                    room = new Room();
                    room.setRoomID(resultSet.getInt("RoomID"));
                    room.setRoomName(resultSet.getString("RoomName"));
                    room.setPricePerHour(resultSet.getBigDecimal("PricePerHour"));
                }
            }
        }
        return room;
    }

    public static ObservableList<Room> selectAll() throws SQLException, ClassNotFoundException {

        ObservableList<Room> roomList = FXCollections.observableArrayList();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_ALL_STATEMENT)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Room room = new Room();
                    room.setRoomID(resultSet.getInt("RoomID"));
                    room.setRoomName(resultSet.getString("RoomName"));
                    room.setPricePerHour(resultSet.getBigDecimal("PricePerHour"));
                    roomList.add(room);
                }
            }
        }
        return roomList;
    }

    public static void deleteRooms() throws SQLException, ClassNotFoundException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_ALL_STATEMENT)) {
            statement.execute();
        }
    }

}
