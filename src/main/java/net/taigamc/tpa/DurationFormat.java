package net.taigamc.tpa;

import java.text.ParseException;
import java.time.Duration;
import java.util.Arrays;
import java.util.regex.Pattern;

public class DurationFormat {

    public static String formatDuration(Duration d) {
        StringBuilder sb = new StringBuilder();
        boolean somePresent = false;
        final long days = d.toDays();
        if (days != 0) {
            somePresent = true;
            sb.append(days);
            sb.append('d');
        }
        final long hours = d.toHours() % 24;
        if (somePresent || hours != 0) {
            somePresent = true;
            sb.append(hours);
            sb.append('h');
        }
        final long minutes = d.toMinutes() % 60;
        if (somePresent || minutes != 0) {
            somePresent = true;
            sb.append(minutes);
            sb.append('m');
        }
        final long seconds = d.toSeconds() % 60;
        sb.append(seconds);
        sb.append('s');
        return sb.toString();
    }

    private static Pattern DURATION_REGEX = Pattern.compile("^((\\d+d\\d+h\\d+m)|(\\d+h\\d+m)|(\\d+m))?\\d+s$");

    public static Duration parseDuration(String str) throws ParseException {
        Duration dur = Duration.ZERO;
        if (!DURATION_REGEX.matcher(str).matches()) throw new ParseException(str, 0);
        String[] split = str.split("[dDhHmMsS]");
        if (split.length == 4) {
            dur = dur.plusDays(Long.parseLong(split[split.length - 4]));
        }
        if (split.length == 3) {
            dur = dur.plusHours(Long.parseLong(split[split.length - 3]));
        }
        if (split.length == 2) {
            dur = dur.plusMinutes(Long.parseLong(split[split.length - 2]));
        }
        dur = dur.plusSeconds(Long.parseLong(split[split.length - 1]));
        return dur;
    }

    private static int readUntilNonDigit(String s, int start) {
        int pos = start;
        while (Character.isDigit(s.charAt(pos))) {
            pos++;
        }
        return pos;
    }

}
