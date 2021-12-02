package net.anei.cadpage.parsers.KY;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class KYDaviessCountyBParser extends FieldProgramParser {

  public KYDaviessCountyBParser() {
    super("DAVIESS COUNTY", "KY",
          "( Call_Time:DATETIME! Call_Type:CALL! Address:ADDRCITY/S6! Common_Name:PLACE! " +
          "| Call_Type:CALL! EMPTY+? ( Common_Name:PLACE! Address:ADDRCITY/S6! Closest_Intersection:X! Incident:ID! " +
                                    "| Location:ADDRCITY/S6! PLACE Near:X! " +
                                    ") " +
          ") END");
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  private static final Pattern TRAIL_GPS_PTN = Pattern.compile(" +(\\d{2,3}\\.\\d{6,})(-\\d{2,3}\\.\\d{6,})$");
  private static final Pattern MISSING_COLON_PTN = Pattern.compile("(?<=Common Name|Address|Closest Intersection|Incident)(?!:)");
  private static final Pattern FLD_BRK_PTN = Pattern.compile("\\s*(?=Call Type:|Common Name|Address|Closest Intersection|Incident|Location|Near)|\n");

  @Override
  protected boolean parseMsg(String body, Data data) {
    Matcher match = TRAIL_GPS_PTN.matcher(body);
    if (match.find()) {
      setGPSLoc(match.group(1)+','+match.group(2), data);
      body = body.substring(0,match.start());
    }
    body = body.replace("Incident  Incident Number", "Incident").replace("Incident Number", "Incident");
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
}
