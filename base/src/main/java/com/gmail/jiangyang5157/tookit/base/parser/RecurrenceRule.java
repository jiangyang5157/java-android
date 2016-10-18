package com.gmail.jiangyang5157.tookit.base.parser;

import java.util.Calendar;
import java.util.HashMap;

/**
 * @author Yang
 * @since 10/18/2016
 * <p>
 * Event recurrence utility functions.
 * <p>
 * This RRULE parser is created by modifying the open sourced code:
 * https://android.googlesource.com/platform/frameworks/opt/calendar/+/ics-mr1/src/com/android/calendarcommon/EventRecurrence.java
 * <p>
 * To use by passing non-null Recurrence string to parse(recur) function.
 * Result will be cached in each field, use data as needed.
 */
public class RecurrenceRule {
    private static String TAG = "RecurrenceRule";

    public int mFreq; // freq2String(freq) returns "Yearly", "Monthly", etc, modified as required
    public String mUntil;
    public int mCount;
    public int mInterval;
    public int mWkst; // day2String(wkst) returns "Sunday", "Monday", etc, modified as required. If not specified, week starts on Monday

    public int[] mBysecond;
    public int mBysecondCount;

    public int[] mByminute;
    public int mByminuteCount;

    public int[] mByhour;
    public int mByhourCount;

    public int[] mByday;
    public int[] mBydayNum;
    public int mBydayCount;

    public int[] mBymonthday;
    public int mBymonthdayCount;

    public int[] mByyearday;
    public int mByyeardayCount;

    public int[] mByweekno;
    public int mByweeknoCount;

    public int[] mBymonth;
    public int mBymonthCount;

    public int[] mBysetpos;
    public int mBysetposCount;

    public static final int SECONDLY = 1;
    public static final int MINUTELY = 2;
    public static final int HOURLY = 3;
    public static final int DAILY = 4;
    public static final int WEEKLY = 5;
    public static final int MONTHLY = 6;
    public static final int YEARLY = 7;

    public static final int SU = 0x00010000;
    public static final int MO = 0x00020000;
    public static final int TU = 0x00040000;
    public static final int WE = 0x00080000;
    public static final int TH = 0x00100000;
    public static final int FR = 0x00200000;
    public static final int SA = 0x00400000;

    /**
     * maps a part string to a parser object
     */
    private static HashMap<String, PartParser> sParsePartMap;

    static {
        sParsePartMap = new HashMap<String, PartParser>();
        sParsePartMap.put("FREQ", new ParseFreq());
        sParsePartMap.put("UNTIL", new ParseUntil());
        sParsePartMap.put("COUNT", new ParseCount());
        sParsePartMap.put("INTERVAL", new ParseInterval());
        sParsePartMap.put("BYSECOND", new ParseBySecond());
        sParsePartMap.put("BYMINUTE", new ParseByMinute());
        sParsePartMap.put("BYHOUR", new ParseByHour());
        sParsePartMap.put("BYDAY", new ParseByDay());
        sParsePartMap.put("BYMONTHDAY", new ParseByMonthDay());
        sParsePartMap.put("BYYEARDAY", new ParseByYearDay());
        sParsePartMap.put("BYWEEKNO", new ParseByWeekNo());
        sParsePartMap.put("BYMONTH", new ParseByMonth());
        sParsePartMap.put("BYSETPOS", new ParseBySetPos());
        sParsePartMap.put("WKST", new ParseWkst());
    }

    /* values for bit vector that keeps track of what we have already seen */
    private static final int PARSED_FREQ = 1 << 0;
    private static final int PARSED_UNTIL = 1 << 1;
    private static final int PARSED_COUNT = 1 << 2;
    private static final int PARSED_INTERVAL = 1 << 3;
    private static final int PARSED_BYSECOND = 1 << 4;
    private static final int PARSED_BYMINUTE = 1 << 5;
    private static final int PARSED_BYHOUR = 1 << 6;
    private static final int PARSED_BYDAY = 1 << 7;
    private static final int PARSED_BYMONTHDAY = 1 << 8;
    private static final int PARSED_BYYEARDAY = 1 << 9;
    private static final int PARSED_BYWEEKNO = 1 << 10;
    private static final int PARSED_BYMONTH = 1 << 11;
    private static final int PARSED_BYSETPOS = 1 << 12;
    private static final int PARSED_WKST = 1 << 13;

    /**
     * maps a FREQ value to an integer constant
     */
    private static final HashMap<String, Integer> sParseFreqMap = new HashMap<String, Integer>();

    // value type is defined by Recurrence Rule rfc2445 (https://tools.ietf.org/html/rfc2445)
    static {
        sParseFreqMap.put("SECONDLY", SECONDLY);
        sParseFreqMap.put("MINUTELY", MINUTELY);
        sParseFreqMap.put("HOURLY", HOURLY);
        sParseFreqMap.put("DAILY", DAILY);
        sParseFreqMap.put("WEEKLY", WEEKLY);
        sParseFreqMap.put("MONTHLY", MONTHLY);
        sParseFreqMap.put("YEARLY", YEARLY);
    }

    /**
     * maps a two-character weekday string to an integer constant
     */
    private static final HashMap<String, Integer> sParseWeekdayMap = new HashMap<String, Integer>();

    // value type is defined by Recurrence Rule rfc2445 (https://tools.ietf.org/html/rfc2445)
    static {
        sParseWeekdayMap.put("SU", SU);
        sParseWeekdayMap.put("MO", MO);
        sParseWeekdayMap.put("TU", TU);
        sParseWeekdayMap.put("WE", WE);
        sParseWeekdayMap.put("TH", TH);
        sParseWeekdayMap.put("FR", FR);
        sParseWeekdayMap.put("SA", SA);
    }

    /**
     * If set, allow lower-case recurrence rule strings.  Minor performance impact.
     */
    private static final boolean ALLOW_LOWER_CASE = true;
    /**
     * If set, validate the value of UNTIL parts.  Minor performance impact.
     */
    private static final boolean VALIDATE_UNTIL = false;
    /**
     * If set, require that only one of {UNTIL,COUNT} is present.  Breaks compat w/ old parser.
     */
    private static final boolean ONLY_ONE_UNTIL_COUNT = false;

    /**
     * Converts one of the Calendar.SUNDAY constants to the SU, MO, etc.
     * constants.  btw, I think we should switch to those here too, to
     * get rid of this function, if possible.
     */
    public static int calendarDay2Day(int day) {
        switch (day) {
            case Calendar.SUNDAY:
                return SU;
            case Calendar.MONDAY:
                return MO;
            case Calendar.TUESDAY:
                return TU;
            case Calendar.WEDNESDAY:
                return WE;
            case Calendar.THURSDAY:
                return TH;
            case Calendar.FRIDAY:
                return FR;
            case Calendar.SATURDAY:
                return SA;
            default:
                throw new RuntimeException("bad day of week: " + day);
        }
    }

    /**
     * Converts one of the SU, MO, etc. constants to the Calendar.SUNDAY
     * constants.  btw, I think we should switch to those here too, to
     * get rid of this function, if possible.
     */
    public static int day2CalendarDay(int day) {
        switch (day) {
            case SU:
                return Calendar.SUNDAY;
            case MO:
                return Calendar.MONDAY;
            case TU:
                return Calendar.TUESDAY;
            case WE:
                return Calendar.WEDNESDAY;
            case TH:
                return Calendar.THURSDAY;
            case FR:
                return Calendar.FRIDAY;
            case SA:
                return Calendar.SATURDAY;
            default:
                throw new RuntimeException("bad day of week: " + day);
        }
    }

    /**
     * Converts one of the internal day constants (SU, MO, etc.) to the
     * two-letter string representing that constant.
     *
     * @param day one the internal constants SU, MO, etc.
     * @return the string for the day
     * @throws IllegalArgumentException Thrown if the day argument is not one of
     *                                  the defined day constants.
     */
    public static String day2String(int day) {
        switch (day) {
            case SU:
                return "Sunday";
            case MO:
                return "Monday";
            case TU:
                return "Tuesday";
            case WE:
                return "Wednesday";
            case TH:
                return "Thursday";
            case FR:
                return "Friday";
            case SA:
                return "Saturday";
            default:
                throw new IllegalArgumentException("bad day argument: " + day);
        }
    }

    /**
     * @param freq
     * @return the string for the FREQ
     */
    public static String freq2String(int freq) {
        switch (freq) {
            case SECONDLY:
                return "Secondly";
            case MINUTELY:
                return "Minutely";
            case HOURLY:
                return "Hourly";
            case DAILY:
                return "Daily";
            case WEEKLY:
                return "Weekly";
            case MONTHLY:
                return "Monthly";
            case YEARLY:
                return "Yearly";
            default:
                throw new IllegalArgumentException("bad freq argument: " + freq);
        }
    }

    private static void appendNumbers(StringBuilder s, String label,
                                      int count, int[] values) {
        if (count > 0) {
            s.append(label);
            count--;
            for (int i = 0; i < count; i++) {
                s.append(values[i]);
                s.append(",");
            }
            s.append(values[count]);
        }
    }

    private void appendByDay(StringBuilder s, int i) {
        int n = this.mBydayNum[i];
        if (n != 0) {
            s.append(n);
        }
        String str = day2String(this.mByday[i]);
        s.append(str);
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();

        s.append("FREQ=");
        s.append(freq2String(this.mFreq));
        if (this.mUntil != null) {
            s.append(";UNTIL=");
            s.append(mUntil);
        }
        if (this.mCount != 0) {
            s.append(";COUNT=");
            s.append(this.mCount);
        }
        if (this.mInterval != 0) {
            s.append(";INTERVAL=");
            s.append(this.mInterval);
        }
        if (this.mWkst != 0) {
            s.append(";WKST=");
            s.append(day2String(this.mWkst));
        }
        appendNumbers(s, ";BYSECOND=", this.mBysecondCount, this.mBysecond);
        appendNumbers(s, ";BYMINUTE=", this.mByminuteCount, this.mByminute);
        appendNumbers(s, ";BYSECOND=", this.mByhourCount, this.mByhour);
        // day
        int count = this.mBydayCount;
        if (count > 0) {
            s.append(";BYDAY=");
            count--;
            for (int i = 0; i < count; i++) {
                appendByDay(s, i);
                s.append(",");
            }
            appendByDay(s, count);
        }
        appendNumbers(s, ";BYMONTHDAY=", this.mBymonthdayCount, this.mBymonthday);
        appendNumbers(s, ";BYYEARDAY=", this.mByyeardayCount, this.mByyearday);
        appendNumbers(s, ";BYWEEKNO=", this.mByweeknoCount, this.mByweekno);
        appendNumbers(s, ";BYMONTH=", this.mBymonthCount, this.mBymonth);
        appendNumbers(s, ";BYSETPOS=", this.mBysetposCount, this.mBysetpos);
        return s.toString();
    }

    /**
     * Determines whether two integer arrays contain identical elements.
     * <p>
     * The native implementation over-allocated the arrays (and may have stuff left over from
     * a previous run), so we can't just check the arrays -- the separately-maintained count
     * field also matters.  We assume that a null array will have a count of zero, and that the
     * array can hold as many elements as the associated count indicates.
     * <p>
     * TODO: replace this with Arrays.equals() when the old parser goes away.
     */
    private static boolean arraysEqual(int[] array1, int count1, int[] array2, int count2) {
        if (count1 != count2) {
            return false;
        }
        for (int i = 0; i < count1; i++) {
            if (array1[i] != array2[i])
                return false;
        }
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof RecurrenceRule)) {
            return false;
        }
        RecurrenceRule er = (RecurrenceRule) obj;
        return mFreq == er.mFreq &&
                (mUntil == null ? er.mUntil == null : mUntil.equals(er.mUntil)) &&
                mCount == er.mCount &&
                mInterval == er.mInterval &&
                mWkst == er.mWkst &&
                arraysEqual(mBysecond, mBysecondCount, er.mBysecond, er.mBysecondCount) &&
                arraysEqual(mByminute, mByminuteCount, er.mByminute, er.mByminuteCount) &&
                arraysEqual(mByhour, mByhourCount, er.mByhour, er.mByhourCount) &&
                arraysEqual(mByday, mBydayCount, er.mByday, er.mBydayCount) &&
                arraysEqual(mBydayNum, mBydayCount, er.mBydayNum, er.mBydayCount) &&
                arraysEqual(mBymonthday, mBymonthdayCount, er.mBymonthday, er.mBymonthdayCount) &&
                arraysEqual(mByyearday, mByyeardayCount, er.mByyearday, er.mByyeardayCount) &&
                arraysEqual(mByweekno, mByweeknoCount, er.mByweekno, er.mByweeknoCount) &&
                arraysEqual(mBymonth, mBymonthCount, er.mBymonth, er.mBymonthCount) &&
                arraysEqual(mBysetpos, mBysetposCount, er.mBysetpos, er.mBysetposCount);
    }

    @Override
    public int hashCode() {
        // We overrode equals, so we must override hashCode().  Nobody seems to need this though.
        throw new UnsupportedOperationException();
    }

    /**
     * parses FREQ={SECONDLY,MINUTELY,...}
     */
    private static class ParseFreq extends PartParser {
        @Override
        public int parsePart(String value, RecurrenceRule er) {
            Integer freq = sParseFreqMap.get(value);
            if (freq == null) {
                throw new InvalidFormatException("Invalid FREQ value: " + value);
            }
            er.mFreq = freq;
            return PARSED_FREQ;
        }
    }

    public boolean repeatsOnEveryWeekDay() {
        if (this.mFreq != WEEKLY) {
            return false;
        }
        int count = this.mBydayCount;
        if (count != 5) {
            return false;
        }
        for (int i = 0; i < count; i++) {
            int day = mByday[i];
            if (day == SU || day == SA) {
                return false;
            }
        }
        return true;
    }

    /**
     * Determines whether this rule specifies a simple monthly rule by weekday, such as
     * "FREQ=MONTHLY;BYDAY=3TU" (the 3rd Tuesday of every month).
     * <p>
     * Negative days, e.g. "FREQ=MONTHLY;BYDAY=-1TU" (the last Tuesday of every month),
     * will cause "false" to be returned.
     * <p>
     * Rules that fire every week, such as "FREQ=MONTHLY;BYDAY=TU" (every Tuesday of every
     * month) will cause "false" to be returned.  (Note these are usually expressed as
     * WEEKLY rules, and hence are uncommon.)
     *
     * @return true if this rule is of the appropriate form
     */
    public boolean repeatsMonthlyOnDayCount() {
        if (this.mFreq != MONTHLY) {
            return false;
        }
        if (mBydayCount != 1 || mBymonthdayCount != 0) {
            return false;
        }
        if (mBydayNum[0] <= 0) {
            return false;
        }
        return true;
    }

    /**
     * Resets parser-modified fields to their initial state.  Does not alter mStartDate.
     * <p>
     * The original parser always set all of the "count" fields, "wkst", and "until",
     * essentially allowing the same object to be used multiple times by calling parse().
     * It's unclear whether this behavior was intentional.  For now, be paranoid and
     * preserve the existing behavior by resetting the fields.
     * <p>
     * We don't need to touch the integer arrays; they will either be ignored or
     * overwritten.  The "mStartDate" field is not set by the parser, so we ignore it here.
     */
    private void resetFields() {
        mUntil = null;
        mFreq = mCount = mInterval
                = mBysecondCount = mByminuteCount = mByhourCount
                = mBydayCount = mBymonthdayCount = mByyeardayCount
                = mByweeknoCount = mBymonthCount = mBysetposCount = 0;
    }

    /**
     * Parses an rfc2445 (https://tools.ietf.org/html/rfc2445) recurrence rule string into its component pieces.
     * Attempting to parse malformed input will result in an RecurrenceRule.InvalidFormatException.
     *
     * @param recur The recurrence rule to parse (in un-folded form).
     */
    public void parse(String recur) {
        /*
         * From RFC 2445 section 4.3.10:
         *
         * recur = "FREQ"=freq *(
         *       ; either UNTIL or COUNT may appear in a 'recur',
         *       ; but UNTIL and COUNT MUST NOT occur in the same 'recur'
         *
         *       ( ";" "UNTIL" "=" enddate ) /
         *       ( ";" "COUNT" "=" 1*DIGIT ) /
         *
         *       ; the rest of these keywords are optional,
         *       ; but MUST NOT occur more than once
         *
         *       ( ";" "INTERVAL" "=" 1*DIGIT )          /
         *       ( ";" "BYSECOND" "=" byseclist )        /
         *       ( ";" "BYMINUTE" "=" byminlist )        /
         *       ( ";" "BYHOUR" "=" byhrlist )           /
         *       ( ";" "BYDAY" "=" bywdaylist )          /
         *       ( ";" "BYMONTHDAY" "=" bymodaylist )    /
         *       ( ";" "BYYEARDAY" "=" byyrdaylist )     /
         *       ( ";" "BYWEEKNO" "=" bywknolist )       /
         *       ( ";" "BYMONTH" "=" bymolist )          /
         *       ( ";" "BYSETPOS" "=" bysplist )         /
         *       ( ";" "WKST" "=" weekday )              /
         *       ( ";" x-name "=" text )
         *       )
         *
         *  The rule parts are not ordered in any particular sequence.
         *
         * Examples:
         *   FREQ=MONTHLY;INTERVAL=2;COUNT=10;BYDAY=1SU,-1SU
         *   FREQ=YEARLY;INTERVAL=4;BYMONTH=11;BYDAY=TU;BYMONTHDAY=2,3,4,5,6,7,8
         *
         * Strategy:
         * (1) Split the string at ';' boundaries to get an array of rule "parts".
         * (2) For each part, find substrings for left/right sides of '=' (name/value).
         * (3) Call a <name>-specific parsing function to parse the <value> into an
         *     output field.
         *
         * By keeping track of which names we've seen in a bit vector, we can verify the
         * constraints indicated above (FREQ appears first, none of them appear more than once --
         * though x-[name] would require special treatment), and we have either UNTIL or COUNT
         * but not both.
         *
         * In general, RFC 2445 property names (e.g. "FREQ") and enumerations ("TU") must
         * be handled in a case-insensitive fashion, but case may be significant for other
         * properties.  We don't have any case-sensitive values in RRULE, except possibly
         * for the custom "X-" properties, but we ignore those anyway.  Thus, we can trivially
         * convert the entire string to upper case and then use simple comparisons.
         *
         * Differences from previous version:
         * - allows lower-case property and enumeration values [optional]
         * - enforces that FREQ appears first
         * - enforces that only one of UNTIL and COUNT may be specified
         * - allows (but ignores) X-* parts
         * - improved validation on various values (e.g. UNTIL timestamps)
         * - error messages are more specific
         *
         * TODO: enforce additional constraints listed in RFC 5545, notably the "N/A" entries
         * in section 3.3.10.  For example, if FREQ=WEEKLY, we should reject a rule that
         * includes a BYMONTHDAY part.
         */
        /* TODO: replace with "if (freq != 0) throw" if nothing requires this */
        resetFields();
        if (recur == null) {
            return;
        }
        int parseFlags = 0;
        String[] parts;
        if (ALLOW_LOWER_CASE) {
            parts = recur.toUpperCase().split(";");
        } else {
            parts = recur.split(";");
        }
        for (String part : parts) {
            int equalIndex = part.indexOf('=');
            if (equalIndex <= 0) {
                /* no '=' or no LHS */
                throw new InvalidFormatException("Missing LHS in " + part);
            }
            String lhs = part.substring(0, equalIndex);
            String rhs = part.substring(equalIndex + 1);
            if (rhs.length() == 0) {
                throw new InvalidFormatException("Missing RHS in " + part);
            }
            /*
             * In lieu of a "switch" statement that allows string arguments, we use a
             * map from strings to parsing functions.
             */
            PartParser parser = sParsePartMap.get(lhs);
            if (parser == null) {
                if (lhs.startsWith("X-")) {
                    //Log.d(TAG, "Ignoring custom part " + lhs);
                    continue;
                }
                throw new InvalidFormatException("Couldn't find parser for " + lhs);
            } else {
                int flag = parser.parsePart(rhs, this);
                if ((parseFlags & flag) != 0) {
                    throw new InvalidFormatException("Part " + lhs + " was specified twice");
                }
                parseFlags |= flag;
            }
        }
        // If not specified, week starts on Monday.
        if ((parseFlags & PARSED_WKST) == 0) {
            mWkst = MO;
        }
        // FREQ is mandatory.
        if ((parseFlags & PARSED_FREQ) == 0) {
            throw new InvalidFormatException("Must specify a FREQ value");
        }
        // Can't have both UNTIL and COUNT.
        if ((parseFlags & (PARSED_UNTIL | PARSED_COUNT)) == (PARSED_UNTIL | PARSED_COUNT)) {
            if (ONLY_ONE_UNTIL_COUNT) {
                throw new InvalidFormatException("Must not specify both UNTIL and COUNT: " + recur);
            } else {
                System.out.println(TAG + "Warning: rrule has both UNTIL and COUNT: " + recur);
            }
        }
    }

    /**
     * parses UNTIL=enddate, e.g. "19970829T021400"
     */
    private static class ParseUntil extends PartParser {
        @Override
        public int parsePart(String value, RecurrenceRule er) {
            if (VALIDATE_UNTIL) {
                // TODO: 10/18/2016
            }
            er.mUntil = value;
            return PARSED_UNTIL;
        }
    }

    /**
     * parses COUNT=[non-negative-integer]
     */
    private static class ParseCount extends PartParser {
        @Override
        public int parsePart(String value, RecurrenceRule er) {
            er.mCount = parseIntRange(value, Integer.MIN_VALUE, Integer.MAX_VALUE, true);
            if (er.mCount < 0) {
                System.out.println(TAG + "Invalid Count. Forcing COUNT to 1 from " + value);
                er.mCount = 1; // invalid count. assume one time recurrence.
            }
            return PARSED_COUNT;
        }
    }

    /**
     * parses INTERVAL=[non-negative-integer]
     */
    private static class ParseInterval extends PartParser {
        @Override
        public int parsePart(String value, RecurrenceRule er) {
            er.mInterval = parseIntRange(value, Integer.MIN_VALUE, Integer.MAX_VALUE, true);
            if (er.mInterval < 1) {
                System.out.println(TAG + "Invalid Interval. Forcing INTERVAL to 1 from " + value);
                er.mInterval = 1;
            }
            return PARSED_INTERVAL;
        }
    }

    /**
     * parses BYSECOND=byseclist
     */
    private static class ParseBySecond extends PartParser {
        @Override
        public int parsePart(String value, RecurrenceRule er) {
            int[] bysecond = parseNumberList(value, 0, 59, true);
            er.mBysecond = bysecond;
            er.mBysecondCount = bysecond.length;
            return PARSED_BYSECOND;
        }
    }

    /**
     * parses BYMINUTE=byminlist
     */
    private static class ParseByMinute extends PartParser {
        @Override
        public int parsePart(String value, RecurrenceRule er) {
            int[] byminute = parseNumberList(value, 0, 59, true);
            er.mByminute = byminute;
            er.mByminuteCount = byminute.length;
            return PARSED_BYMINUTE;
        }
    }

    /**
     * parses BYHOUR=byhrlist
     */
    private static class ParseByHour extends PartParser {
        @Override
        public int parsePart(String value, RecurrenceRule er) {
            int[] byhour = parseNumberList(value, 0, 23, true);
            er.mByhour = byhour;
            er.mByhourCount = byhour.length;
            return PARSED_BYHOUR;
        }
    }

    /**
     * parses BYDAY=bywdaylist, e.g. "1SU,-1SU"
     */
    private static class ParseByDay extends PartParser {
        @Override
        public int parsePart(String value, RecurrenceRule er) {
            int[] byday;
            int[] bydayNum;
            int bydayCount;
            if (value.indexOf(",") < 0) {
                /* only one entry, skip split() overhead */
                bydayCount = 1;
                byday = new int[1];
                bydayNum = new int[1];
                parseWday(value, byday, bydayNum, 0);
            } else {
                String[] wdays = value.split(",");
                int len = wdays.length;
                bydayCount = len;
                byday = new int[len];
                bydayNum = new int[len];
                for (int i = 0; i < len; i++) {
                    parseWday(wdays[i], byday, bydayNum, i);
                }
            }
            er.mByday = byday;
            er.mBydayNum = bydayNum;
            er.mBydayCount = bydayCount;
            return PARSED_BYDAY;
        }

        /**
         * parses [int]weekday, putting the pieces into parallel array entries
         */
        private static void parseWday(String str, int[] byday, int[] bydayNum, int index) {
            int wdayStrStart = str.length() - 2;
            String wdayStr;
            if (wdayStrStart > 0) {
                /* number is included; parse it out and advance to weekday */
                String numPart = str.substring(0, wdayStrStart);
                int num = parseIntRange(numPart, -53, 53, false);
                bydayNum[index] = num;
                wdayStr = str.substring(wdayStrStart);
            } else {
                /* just the weekday string */
                wdayStr = str;
            }
            Integer wday = sParseWeekdayMap.get(wdayStr);
            if (wday == null) {
                throw new InvalidFormatException("Invalid BYDAY value: " + str);
            }
            byday[index] = wday;
        }
    }

    /**
     * parses BYMONTHDAY=bymodaylist
     */
    private static class ParseByMonthDay extends PartParser {
        @Override
        public int parsePart(String value, RecurrenceRule er) {
            int[] bymonthday = parseNumberList(value, -31, 31, false);
            er.mBymonthday = bymonthday;
            er.mBymonthdayCount = bymonthday.length;
            return PARSED_BYMONTHDAY;
        }
    }

    /**
     * parses BYYEARDAY=byyrdaylist
     */
    private static class ParseByYearDay extends PartParser {
        @Override
        public int parsePart(String value, RecurrenceRule er) {
            int[] byyearday = parseNumberList(value, -366, 366, false);
            er.mByyearday = byyearday;
            er.mByyeardayCount = byyearday.length;
            return PARSED_BYYEARDAY;
        }
    }

    /**
     * parses BYWEEKNO=bywknolist
     */
    private static class ParseByWeekNo extends PartParser {
        @Override
        public int parsePart(String value, RecurrenceRule er) {
            int[] byweekno = parseNumberList(value, -53, 53, false);
            er.mByweekno = byweekno;
            er.mByweeknoCount = byweekno.length;
            return PARSED_BYWEEKNO;
        }
    }

    /**
     * parses BYMONTH=bymolist
     */
    private static class ParseByMonth extends PartParser {
        @Override
        public int parsePart(String value, RecurrenceRule er) {
            int[] bymonth = parseNumberList(value, 1, 12, false);
            er.mBymonth = bymonth;
            er.mBymonthCount = bymonth.length;
            return PARSED_BYMONTH;
        }
    }

    /**
     * parses BYSETPOS=bysplist
     */
    private static class ParseBySetPos extends PartParser {
        @Override
        public int parsePart(String value, RecurrenceRule er) {
            int[] bysetpos = parseNumberList(value, Integer.MIN_VALUE, Integer.MAX_VALUE, true);
            er.mBysetpos = bysetpos;
            er.mBysetposCount = bysetpos.length;
            return PARSED_BYSETPOS;
        }
    }

    /**
     * parses WKST={SU,MO,...}
     */
    private static class ParseWkst extends PartParser {
        @Override
        public int parsePart(String value, RecurrenceRule er) {
            Integer wkst = sParseWeekdayMap.get(value);
            if (wkst == null) {
                throw new InvalidFormatException("Invalid WKST value: " + value);
            }
            er.mWkst = wkst;
            return PARSED_WKST;
        }
    }

    /**
     * Base class for the RRULE part parsers.
     */
    abstract static class PartParser {
        /**
         * Parses a single part.
         *
         * @param value The right-hand-side of the part.
         * @param er    The RecurrenceRule into which the result is stored.
         * @return A bit value indicating which part was parsed.
         */
        public abstract int parsePart(String value, RecurrenceRule er);

        /**
         * Parses an integer, with range-checking.
         *
         * @param str       The string to parse.
         * @param minVal    Minimum allowed value.
         * @param maxVal    Maximum allowed value.
         * @param allowZero Is 0 allowed?
         * @return The parsed value.
         */
        public static int parseIntRange(String str, int minVal, int maxVal, boolean allowZero) {
            try {
                if (str.charAt(0) == '+') {
                    // Integer.parseInt does not allow a leading '+', so skip it manually.
                    str = str.substring(1);
                }
                int val = Integer.parseInt(str);
                if (val < minVal || val > maxVal || (val == 0 && !allowZero)) {
                    throw new InvalidFormatException("Integer value out of range: " + str);
                }
                return val;
            } catch (NumberFormatException nfe) {
                throw new InvalidFormatException("Invalid integer value: " + str);
            }
        }

        /**
         * Parses a comma-separated list of integers, with range-checking.
         *
         * @param listStr   The string to parse.
         * @param minVal    Minimum allowed value.
         * @param maxVal    Maximum allowed value.
         * @param allowZero Is 0 allowed?
         * @return A new array with values, sized to hold the exact number of elements.
         */
        public static int[] parseNumberList(String listStr, int minVal, int maxVal,
                                            boolean allowZero) {
            int[] values;
            if (listStr.indexOf(",") < 0) {
                // Common case: only one entry, skip split() overhead.
                values = new int[1];
                values[0] = parseIntRange(listStr, minVal, maxVal, allowZero);
            } else {
                String[] valueStrs = listStr.split(",");
                int len = valueStrs.length;
                values = new int[len];
                for (int i = 0; i < len; i++) {
                    values[i] = parseIntRange(valueStrs[i], minVal, maxVal, allowZero);
                }
            }
            return values;
        }
    }

    /**
     * Thrown when a recurrence string provided can not be parsed according
     * to RFC2445.
     */
    public static class InvalidFormatException extends RuntimeException {
        InvalidFormatException(String s) {
            super(s);
        }
    }

    public static void main(String[] args) {
        RecurrenceRule rrule = new RecurrenceRule();
        rrule.parse("FREQ=MONTHLY;INTERVAL=1;BYMONTHDAY=1,15");
        String s = rrule.toString();
        System.out.println(TAG + ": " + s);
    }
}