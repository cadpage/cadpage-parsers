package net.anei.cadpage.parsers.MI;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MISaginawCountyDParser extends FieldProgramParser {

  public MISaginawCountyDParser() {
    super(MISaginawCountyBParser.CITY_CODES, "SAGINAW COUNTY", "MI",
          "Location:ADDR! Apartment:APT! Location_Name:PLACE! ( Cross_Steets:X! | Cross_Streets:X! ) Inc_Type:CALL! M/C:EMPTY! All_Comments:INFO! INFO/N+ " +
              "Units_Assigned:UNIT! City/Twp:CITY! A/S/B:MAP! Latitude:GPS1! Longitude:GPS2! Incident_Number:ID! Phone:PHONE! Now_Time:DATETIME!");

  }

  @Override
  public String getFilter() {
    return "777";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Saginaw Twp Fire")) return false;
    data.strSource = subject;
    int pt = body.indexOf("\nApartment:");
    if (pt < 0) return false;
    body = body.substring(0,pt).replace("\n", "") + body.substring(pt);
    return parseFields(body.split("\n"), data);
  }

  @Override
  public String getProgram() {
    return "SRC " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("DATETIME")) return new MyDateTimeField();
    return super.getField(name);
  }

  private static final Pattern INFO_HDR_PTN = Pattern.compile("\\d{4}/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d\\b *");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      for (String line : field.split("\\|")) {
        line = line.trim();
        Matcher match = INFO_HDR_PTN.matcher(line);
        if (match.lookingAt()) line = line.substring(match.end());
        data.strSupp = append(data.strSupp, "\n", line);
      }
    }
  }

  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d{4})(\\d{2})(\\d{2}) +(\\d\\d:\\d\\d:\\d\\d)");
  private class MyDateTimeField extends DateTimeField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_TIME_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strDate = match.group(2)+'/'+match.group(3)+'/'+match.group(1);
      data.strTime = match.group(3);
    }
  }
}
