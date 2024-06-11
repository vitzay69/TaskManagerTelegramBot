package org.vitzay.dao;

import org.vitzay.entity.Note;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotesDAO {

    private static final String URL = "jdbc:mysql://localhost:3306/note_store";
    private static final String USER = "root";
    private static final String PASSWORD = System.getenv("SQL_PASSWORD");

    public static final String INSERT_QUERY = "INSERT INTO notes(note_name, description, created_date, chat_id) values(?, ?, ?, ?)";

    public Note create(Note note) throws SQLException {
        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_QUERY, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, note.getName());
            preparedStatement.setString(2, note.getDescription());
            Timestamp timestamp = note.getCreationDate();
            preparedStatement.setTimestamp(3, timestamp);
            preparedStatement.setLong(4, note.getChatId());
            preparedStatement.execute();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            generatedKeys.next();
            int id = generatedKeys.getInt(1);
            note.setId(id);
            return note;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Note> getNotes(Long chatId) {
        List<Note> listOfNotes = new ArrayList();

        try {
            Connection connection = getConnection();
            ResultSet resultSet = connection.createStatement().executeQuery("SELECT * FROM notes");

            while (resultSet.next()) {
                Timestamp creationDate = Timestamp.valueOf(resultSet.getTimestamp("created_date").toString());

                if (chatId == resultSet.getLong("chat_id")) {
                    Note note = new Note(resultSet.getInt("id"),
                            resultSet.getString("note_name"),
                            resultSet.getString("description"),
                            creationDate,
                            resultSet.getLong("chat_id"));

                    listOfNotes.add(note);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return listOfNotes;
    }

    public void delete(int id, Long chatId) throws SQLException {
        Connection connection = getConnection();

        try {
            PreparedStatement findNoteStatement = connection.prepareStatement("SELECT chat_id FROM notes WHERE id = ?");
            findNoteStatement.setInt(1, id);

            ResultSet resultSet = findNoteStatement.executeQuery();

            if (resultSet.next()) {
                Long noteChatId = resultSet.getLong("chat_id");

                if (chatId.equals(noteChatId)) {
                    PreparedStatement deleteStatement = connection.prepareStatement("DELETE FROM notes WHERE id = ?");
                    deleteStatement.setInt(1, id);

                    deleteStatement.executeUpdate();
                }
            }
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    public void editName(int id, String text, Long chatId) throws SQLException {
        Connection connection = getConnection();

        try {
            PreparedStatement selectStatement = connection.prepareStatement("SELECT chat_id FROM notes WHERE id = ?");
            selectStatement.setInt(1, id);
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {

                if (chatId.equals(resultSet.getLong("chat_id"))) {
                    PreparedStatement updateStatement = connection.prepareStatement("UPDATE notes SET note_name = ? WHERE id = ?");
                    updateStatement.setString(1, text);
                    updateStatement.setInt(2, id);
                    updateStatement.executeUpdate();
                    updateStatement.close();
                }
            }

            resultSet.close();
            selectStatement.close();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    public void editDescription(int id, String text, Long chatId) throws SQLException {
        Connection connection = getConnection();

        try {
            PreparedStatement selectStatement = connection.prepareStatement("SELECT chat_id FROM notes WHERE id = ?");
            selectStatement.setInt(1, id);
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {

                if (chatId.equals(resultSet.getLong("chat_id"))) {
                    PreparedStatement updateStatement = connection.prepareStatement("UPDATE notes SET description = ? WHERE id = ?");
                    updateStatement.setString(1, text);
                    updateStatement.setInt(2, id);
                    updateStatement.executeUpdate();
                    updateStatement.close();
                }
            }

            resultSet.close();
            selectStatement.close();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
