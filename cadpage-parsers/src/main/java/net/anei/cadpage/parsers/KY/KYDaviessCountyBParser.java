package net.anei.cadpage.parsers.KY;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class KYDaviessCountyBParser extends FieldProgramParser {

  public KYDaviessCountyBParser() {
    super(KYDaviessCountyParser.CITY_LIST, "DAVIESS COUNTY", "KY",
          "( Call_Time:DATETIME! Call_Type:CALL! Address:ADDRCITY/S6! Common_Name:PLACE! " +
          "| Call_Type:CALL! EMPTY+? ( Common_Name:PLACE! Address:ADDRCITY/S6! Closest_Intersection:X! Incident:ID! " +
                                    "| Location:ADDRCITY/S6! PLACE Near:X! " +
                                    "| Name:PLACE! Phone_Number:PHONE! Caller:NAME! Address:ADDRCITY/S6! Cross_Street:X! Incident:ID! " +
                                    ") " +
          "| Alert_Type:CALL! Call_Type:CALL! Location:PLACE! Address:ADDR! Xstreet:X_CITY! Safety_Alert:ALERT " +
          ") END");
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  private static final Pattern TRAIL_GPS_PTN = Pattern.compile(" +(\\d{2,3}\\.\\d{6,})(-\\d{2,3}\\.\\d{6,})$");
  private static final Pattern NAME_PHONE_NUMBER_PTN = Pattern.compile("Name(.*?)Phone Number::");
  private static final Pattern MISSING_COLON_PTN = Pattern.compile("(?<=Alert Type|Call Type|Common Name|(?<!Preplan )Location|Address|Closest Intersection|Incident|Xstreet|Safety Alert|Cross Street)(?!:)");
  private static final Pattern FLD_BRK_PTN = Pattern.compile("\\s*(?=(?:Call Type|Common Name|Address|Closest Intersection|Incident|Location|Near|Xstreet|Safety Alert|Caller|Cross Street):)|\n");

  @Override
  protected boolean parseMsg(String body, Data data) {
    Matcher match = TRAIL_GPS_PTN.matcher(body);
    if (match.find()) {
      setGPSLoc(match.group(1)+','+match.group(2), data);
      body = body.substring(0,match.start());
    }
    body = body.replace("Incident  Incident Number", "Incident")
               .replace("Incident Number", "Incident")
               .replace("Incident Location", "Location")
               .replace("No Cross Streets Found", "");
    body = NAME_PHONE_NUMBER_PTN.matcher(body).replaceFirst("\nName:$1\nPhone Number:");
    body = MISSING_COLON_PTN.matcher(body).replaceAll(":");
    return parseFields(FLD_BRK_PTN.split(body), data);
  }

  @Override
  public String getProgram() {
    return super.getProgram() + " GPS";
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} +\\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("X_CITY")) return new MyCrossCityField();
    if (name.equals("ALERT")) return new MyAlertField();
    return super.getField(name);
  }

  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      for (String part : field.split(",")) {
        part = part.trim();
        if (!part.isEmpty()) {
          String code = "";
          String call = part;
          if (part.endsWith(")")) {
            int pt = part.indexOf('(');
            if (pt >= 0) {
              code = part.substring(0,pt).trim();
              call = part.substring(pt+1, part.length()-1).trim();
            }
          }
          if (!code.equals(data.strCode)) data.strCode = append(data.strCode, "/", code);
          if (!call.equals(data.strCall)) data.strCall = append(data.strCall, " / ", call);
        }
      }
    }

    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }

  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace('@', '/');
      super.parse(field,  data);
    }
  }

  private class MyCrossCityField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      parseAddress(StartType.START_ADDR, FLAG_ONLY_CROSS | FLAG_ANCHOR_END, field, data);
    }

    @Override
    public String getFieldNames() {
      return "X CITY";
    }
  }

  private class MyAlertField extends AlertField {
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("Building Name:") || "Building Name:".startsWith(field)) return;
      super.parse(field, data);
    }
  }
}
