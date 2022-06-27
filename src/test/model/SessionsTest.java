package model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SessionsTest {

    @Test
    void testEmptySessions() {
        Sessions sessions = new Sessions(new ArrayList<Session>());
        assertEquals(0, sessions.getSessions().size());
    }

}
