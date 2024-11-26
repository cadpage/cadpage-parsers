
package net.anei.cadpage.parsers.AL;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Jefferson County, AL (C)
 */
public class ALJeffersonCountyCParser extends FieldProgramParser {


  public ALJeffersonCountyCParser() {
    super("JEFFERSON COUNTY", "AL",
          "( CALL ADDRCITY/SXP | ADDRCITY/SXP CALL! ) JUNK X:X? Units:UNIT! Created:DATETIME_ID! Pri_Inc:ID! N:INFO/N+");
  }

  @Override
  public String getFilter() {
    return "FireDesk@jeffcoal911.org";
  }

  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    body = stripFieldStart(body, "/ ! / ");
    return parseFields(body.split("\n"), 5, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("JUNK")) return new SkipField("|@.*", true);
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("DATETIME_ID")) return new MyDateTimeIdField();
    if (name.equals("ID")) return new MyIdField();
    return super.getField(name);
  }

  private static final Pattern ADDR_SRC_PTN = Pattern.compile("(.*)\\(([ \\w]*)\\)");
  private static final Pattern ADDR_APT_PTN = Pattern.compile("\\d[^ ]*|[A-Z]\\d*|(?:APT#?|#) *(.*)");
  private static final Pattern ADDR_MM_PTN = Pattern.compile("/? *MM.*");
  private class MyAddressCityField extends AddressCityField {
    @Override
    public boolean canFail() {
      return true;
    }

    public boolean checkParse(String field, Data data) {
      if (!field.startsWith("@")) return false;
      parse(field, data);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "@");
      Matcher match = ADDR_SRC_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1).trim();
        data.strSource = match.group(2).trim();
      }
      field = stripFieldEnd(field, "()");
      field = field.replace('@', '&');
      super.parse(field, data);

      int pt = data.strCity.indexOf("  ");
      if (pt >= 0) data.strCity = data.strCity.substring(0,pt);

      data.strAddress = data.strAddress.replace(" & MM", " MM");

      if (data.strPlace.length() > 0) {
        match = ADDR_APT_PTN.matcher(data.strPlace);
        if (match.matches()) {
          String apt = match.group(1);
          if (apt == null) apt = data.strPlace;
          data.strApt = append(data.strApt, "-", apt);
          data.strPlace = "";
        }
        else if (ADDR_MM_PTN.matcher(data.strPlace).matches()) {
          data.strAddress = data.strAddress + ' ' + stripFieldStart(data.strPlace, "/");
          data.strPlace = "";
        }
        else if (data.strPlace.startsWith("/") || data.strPlace.startsWith("&")) {
          data.strAddress = data.strAddress + " & " + data.strPlace.substring(1).trim();
          data.strPlace = "";
        }
      }
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " SRC";
    }
  }

  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("No Cross Streets Found")) return;
      field = stripFieldStart(field, "/");
      field = stripFieldEnd(field, "/");
      super.parse(field, data);
    }
  }

  private static final Pattern DATE_TIME_ID_PTN = Pattern.compile("(\\d\\d?/\\d\\d?/\\d{4}) (\\d\\d?:\\d\\d:\\d\\d(?: [AP]M)?) *#(\\d*)");
  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mm:ss aa");
  private class MyDateTimeIdField extends DateTimeField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_TIME_ID_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strDate = match.group(1);
      String time = match.group(2);
      if (time.endsWith("M")) {
        setTime(TIME_FMT, time, data);
      } else {
        data.strTime = time;
      }
      data.strCallId = match.group(3);
    }

    @Override
    public String getFieldNames() {
      return "DATE TIME ID";
    }
  }

  private class MyIdField extends IdField {
    @Override
    public void parse(String field, Data data) {
      data.strCallId = append(data.strCallId, "/", field);
    }
  }
}
