package net.anei.cadpage.parsers.dispatch;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.SmartAddressParser;

public class DispatchA83Parser extends SmartAddressParser {

  private CodeSet placeSet;
  private int version;

  public DispatchA83Parser(String[] cityList, String defCity, String defState, int version) {
    this(cityList, null, defCity, defState, version);
  }

  public DispatchA83Parser(String[] cityList, CodeSet placeSet, String defCity, String defState, int version) {
    super(cityList, defCity, defState);
    this.placeSet = placeSet;
    setFieldList("SRC ID CODE DATE TIME CALL ADDR APT CITY NAME GPS INFO");
    this.version = version;
  }

  private static final Pattern RUN_REPORT_PTN = Pattern.compile("(.*?) (\\d\\d?/\\d\\d?/\\d{4}) (\\d\\d?:\\d\\d:\\d\\d [AP]M) (.*?) (Case # \\d+ */.*)");
  private static final Pattern TIMES_BRK_PTN = Pattern.compile(" +/(?=[ A-Za-z]+ - )");

  private static final Pattern SUBJECT_PTN = Pattern.compile("(?:([A-Z]+) )?+(\\d+)");
  private static final Pattern ALERT_MASTER = Pattern.compile("(?:(.*?) +)?Date Recv (\\d\\d?/\\d\\d?/\\d{4}) (\\d\\d?:\\d\\d:\\d\\d(?: [AP]M)?) (.*)");
  private static final Pattern TRAIL_FROM_PTN = Pattern.compile("(.*) From : .* User Id: .*");
  private static final Pattern TRAIL_GPS_PTN = Pattern.compile("(.*) +http://maps.google.com/maps\\?q=(.*)");
  private static final Pattern LEAD_PLACE_PTN = Pattern.compile("(.*?) \\| (.*) \\| (.*)");
  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mm:ss aa");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    Matcher match = SUBJECT_PTN.matcher(subject);
    if (!match.matches()) return false;
    data.strSource = getOptGroup(match.group(1));
    data.strCallId = match.group(2);

    if ((match = RUN_REPORT_PTN.matcher(body)).matches()) {
      setFieldList("SRC ID CALL DATE TIME PLACE ADDR APT CITY INFO");
      data.msgType = MsgType.RUN_REPORT;
      data.strCall = match.group(1);
      data.strDate =  match.group(2);
      setTime(TIME_FMT, match.group(3), data);

      parseAddress(StartType.START_PLACE, match.group(4).trim(), data);
      data.strCall = append(data.strCall, " - ", getLeft());
      if (data.strSupp.equals("None")) data.strSupp = "";
      String times = match.group(5);
      data.strSupp = TIMES_BRK_PTN.matcher(times).replaceAll("\n");

      if (data.strPlace.length() > 0 && data.strAddress.length() == 0) {
        if (placeSet !=  null) {
          String place = placeSet.getCode(data.strPlace);
          if (place != null) {
            data.strAddress = place;
            data.strCall = append(data.strCall, " - ", data.strPlace.substring(place.length()).trim());
          } else {
            data.strAddress = data.strPlace;
          }
        } else {
          data.strAddress = data.strPlace;
        }
        data.strPlace = "";
      }
      return true;
    }

    match = ALERT_MASTER.matcher(body);
    if (match.matches()) {
      setFieldList("SRC ID CALL DATE TIME PLACE APT ADDR CITY NAME INFO GPS");
      String callPfx = getOptGroup(match.group(1));
      data.strDate = match.group(2);
      String time = match.group(3);
      if (time.endsWith("M")) {
        setTime(TIME_FMT, match.group(3), data);
      } else {
        data.strTime = time;
      }
      body = match.group(4).trim();

      StartType st;
      String apt = "";
      if (version == 1) {
        match = TRAIL_GPS_PTN.matcher(body);
        if (match.matches()) {
          body = match.group(1).trim();
          setGPSLoc(match.group(2).replace('+', ' '), data);
        } else {
          data.expectMore = true;
        }
        st = StartType.START_CALL;

      } else {
        match = TRAIL_FROM_PTN.matcher(body);
        if (match.matches()) {
          body = match.group(1).trim();
        } else {
          return false;
        }

        match = LEAD_PLACE_PTN.matcher(body);
        if (match.matches()) {
          data.strPlace = match.group(1).trim();
          apt = match.group(2).trim();
          body = match.group(3).trim();
        }
        st = StartType.START_ADDR;
      }

      parseAddress(st, body, data);
      data.strCall = append(callPfx, " - ", data.strCall);
      data.strApt = append(data.strApt, "-", apt);

      String left = stripFieldStart(getLeft(), data.strAddress);
      left = stripFieldStart(left, data.strCity);
      if (version == 1) {
        data.strName = left;
      } else {
        if (data.strCall.isEmpty()) {
          data.strCall = left;
        } else {
          data.strSupp = left;
        }
      }


      return true;
    }

    return false;
  }
}
