package net.anei.cadpage.parsers.NY;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class NYRocklandCountyCParser extends FieldProgramParser {

  private static Pattern MARKER = Pattern.compile("(?:/ )?ACR# +(\\d+) / +");

  public NYRocklandCountyCParser() {
    super("ROCKLAND COUNTY", "NY",
        "Addr:ADDR! X_St:X Name:NAME Phone:PHONE Comp:CALL? DATETIME? ");
  }

  @Override
  public String getFilter() {
    return "dispatch@hatzolohems.org,Dispatch,cad@hatzolohems.org";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    do {
      if (subject.startsWith("ACR# ")) {;
        data.strCallId = subject.substring(5).trim();
        break;
      }

      Matcher match = MARKER.matcher(body);
      if (match.lookingAt()) {
        data.strCallId = match.group(1);
        body = body.substring(match.end());
        break;
      }

    } while (false);

    if (body.startsWith("Add:")) body = "Addr:" + body.substring(4);
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("DATETIME")) return new MyDateTimeField();
    return super.getField(name);
  }

  private static final Pattern ADDR_APT_PTN = Pattern.compile("(?:APT|RM|ROOM|LOT|#) *(.*)|[A-Z]?\\d+[A-Z]?|[A-Z]", Pattern.CASE_INSENSITIVE);
  private class MyAddressField extends AddressField {

    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      super.parse(p.get(','), data);
      String city = p.get();
      Matcher match = ADDR_APT_PTN.matcher(city);
      if (match.matches()) {
        String apt = match.group(1);
        if (apt == null) apt = city;
        data.strApt = append(data.strApt, "-", apt);
      } else {
        data.strCity = city;
      }
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " CITY";
    }
  }

  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("Between ")) {
        field = field.substring(8).trim();
      } else if (field.startsWith("Near the intersection of ")) {
        field = "Near " + field.substring(25).trim();
      }
      super.parse(field, data);
    }
  }

  @Override
  public String getProgram() {
    return "ID " + super.getProgram();
  }

  private static final Pattern DATE_TIME_PTN =  Pattern.compile("(\\d\\d?/\\d\\d?/\\d{4}) - (\\d\\d?:\\d\\d [AP]M)");
  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mm aa");
  private class MyDateTimeField extends DateTimeField {

    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = DATE_TIME_PTN.matcher(field);
      if (!match.matches()) return false;
      data.strDate = match.group(1);
      setTime(TIME_FMT, match.group(2), data);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }
}
