package com.swip.swipwms.repository;

import com.swip.swipwms.model.User;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static com.fasterxml.jackson.databind.type.LogicalType.DateTime;

public class ActionsRepository {

    public static Path actionsLog = Path.of("/Users/klausgrosser/IdeaProjects/SwipWMS/data/actions.log");

    public static void addToLog(User user, String action) throws IOException {
        Date date = new Date();
        Files.writeString(actionsLog, user.getName() + ". " + action + " " + date.toString() + "\n", StandardOpenOption.APPEND);

    }
}
