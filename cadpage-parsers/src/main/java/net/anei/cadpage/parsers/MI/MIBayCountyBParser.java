package net.anei.cadpage.parsers.MI;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MIBayCountyBParser extends FieldProgramParser {

  public MIBayCountyBParser() {
    super("BAY COUNTY", "MI",
          "CALL:CALL! ADDR:ADDRCITY/SXa! INFO:INFO! INFO+ DATE:DATETIME! ( LOC:PLACE! CROSS:X! | PLACE:PLACE! X:X! ) UNIT:UNIT! ID:ID!");
    setupMultiWordStreets("CASS AVENUE");
  }

  @Override
  public String getFilter() {
    return "911@baycounty.net";
  }

  @Override
  public boolean parseMsg(String body, Data data) {
    if (!super.parseFields(body.split("\n"),  8, data)) return false;
    if (data.strPlace.equals(data.strAddress)) data.strPlace = "";
    if (data.strCross.equals("No Cross Streets Found")) data.strCross = "";
    return true;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("DATETIME")) return new MyDateTimeField();
    return super.getField(name);
  }

  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "|");
      data.strSupp = append(data.strSupp, "\n", field);
    }
  }

  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace('@', '&');
      super.parse(field, data);
    }
  }

  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d\\d?/\\d\\d?/\\d{4}) (\\d\\d?:\\d\\d:\\d\\d(?: [AP]M)?)");
  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mm:ss aa");
  private class MyDateTimeField extends DateTimeField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_TIME_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strDate = match.group(1);
      String time =  match.group(2);
      if (time.endsWith("M")) {
        setTime(TIME_FMT, time, data);
      } else {
        data.strTime = time;
      }
    }
  }
}
