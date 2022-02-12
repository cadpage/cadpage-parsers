package net.anei.cadpage.parsers.CO;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class COMesaCountyBParser extends FieldProgramParser {

  public COMesaCountyBParser() {
    super(CITY_LIST, "MESA COUNTY", "CO",
          "MARKER? ID! ( Call_Type:CALL! | Fire_Call_Type:CALL! EMS_Call_Type:CALL! ) Address:ADDRCITY/S! Common_Name:PLACE! Closest_Intersection:X! Additional_Location_Info:INFO? Call_Time:DATETIME! Units:UNIT? Narrative:INFO! INFO/N+");
  }

  @Override
  public String getFilter() {
    return "@MESACOUNTY.US,@GJCITY.ORG,@CI.GRANDJCT.CO.US";
  }

  private static final Pattern DELIM = Pattern.compile("\n| +(?<! Fire | EMS )(?=(?:Call Type|Fire Call Type|EMS Call Type|Common Name|Additional Location Info|Units):)");

  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(DELIM.split(body), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("MARKER")) return new SkipField("\\*{3,}[A-Z0-9 ]+\\*{3,}", true);
    if (name.equals("ID")) return new MyIdField();
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("DATETIME")) return new MyDateTimeField();
    return super.getField(name);
  }

  private static final Pattern ID_PTN1 = Pattern.compile("\\d{4}-\\d{8}\\b");
  private static final Pattern ID_PTN2 = Pattern.compile("CFS +(\\d+)");
  private class MyIdField extends IdField {

    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("Inc#") || field.startsWith("INC#")) {
        field = field.substring(4).trim();
        for (String part : field.split(",")) {
          part = part.trim();
          part = stripFieldStart(part, "[");
          part = stripFieldEnd(part, "]");
          Matcher match = ID_PTN1.matcher(part);
          if (match.lookingAt()) {
            data.strCallId = append(data.strCallId, ",", match.group());
          }
        }
      }

      else {
        Matcher match = ID_PTN2.matcher(field);
        if (match.matches()) {
          data.strCallId = match.group(1);
        }

        else abort();
      }
    }
  }

  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      if (data.strCall.length() == 0) {
        data.strCall = field;
      }
      else {
        if (data.strCall.startsWith(field)) return;
        if (field.startsWith(data.strCall)) return;
        data.strCall = data.strCall + "/" + field;
      }
    }
  }

  private static final Pattern ADDR_APT_PTN = Pattern.compile("(.*) APT/BLD\\b *(.*)");
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      String apt = "";
      Matcher match = ADDR_APT_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1).trim();
        apt = match.group(2);
      }
      super.parse(field, data);
      data.strApt = append(data.strApt, "-", apt);
    }

    @Override
    public String getFieldNames() {
      return "ADDR CITY APT";
    }
  }

  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("No Cross Streets Found")) return;
      super.parse(field, data);
    }
  }

  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d\\d?/\\d\\d?/\\d{4}) +(\\d\\d?:\\d\\d:\\d\\d(?: [AP]M)?)");
  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mm:ss aa");
  private class MyDateTimeField extends DateTimeField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_TIME_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strDate = match.group(1);
      String time = match.group(2);
      if (time.endsWith("M")) {
        setTime(TIME_FMT, match.group(2), data);
      } else {
        data.strTime = time;
      }
    }
  }

  @Override
  public String adjustMapAddress(String addr) {
    addr = addr.replace("HWY 6 & 50", "HWY 50");
    addr = addr.replace("MILLER CANYON RANCH RD", "E S 5/10 RD");
    return super.adjustMapAddress(addr);
  }

  private static final String[] CITY_LIST = new String[]{
      "CAMEO",
      "CARPENTER",
      "CLIFTON",
      "COLLBRAN",
      "DE BEQUE",
      "DEBEQUE",
      "FRUITA",
      "FRUITVALE",
      "GATEWAY",
      "GRAND JUNCTION",
      "LOMA",
      "MACK",
      "MESA",
      "MOLINA",
      "ORCHARD MESA",
      "PALISADE",
      "PLATEAU CITY",
      "REDLANDS",
      "WHITEWATER"
  };
}
