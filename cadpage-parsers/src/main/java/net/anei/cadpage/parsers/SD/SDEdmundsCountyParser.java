package net.anei.cadpage.parsers.SD;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class SDEdmundsCountyParser extends FieldProgramParser {


  public SDEdmundsCountyParser() {
    this("EDMUNDS COUNTY", "SD");
  }

  SDEdmundsCountyParser(String defCity, String defState) {
    super(defCity, defState,
          "CALL ADDRCITYST PLACE! INFO/N+");
  }

  @Override
  public String getAliasCode() {
    return "SDEdmundsCounty";
  }

  @Override
  public String getFilter() {
    return "no-reply@zuercherportal.com";
  }

  private static final Pattern SUBJECT_PTN = Pattern.compile(" - #(\\S+)");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    Matcher match = SUBJECT_PTN.matcher(subject);
    if (match.matches()) data.strCallId = match.group(1);

    return parseFields(body.split(";\\s*"), data);
  }

  @Override
  public String getProgram() {
    return "ID " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITYST")) return new MyAddressCityStateField();
    if (name.equals("PLACE")) return new MyPlaceField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern GPS_PTN = Pattern.compile("[-+]?\\d{2,3}\\.\\d{6,}[, ]+[-+]?\\d{2,3}\\.\\d{6,}\\b");

  private class MyAddressCityStateField extends AddressCityStateField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = GPS_PTN.matcher(field);
      if (match.lookingAt()) {
        String gps = match.group();
        field = field.substring(match.end()).trim();
        if (field.isEmpty()) {
          data.strAddress = gps;
          return;
        } else {
          setGPSLoc(match.group(), data);
        }
      }
      super.parse(field, data);
    }
  }

  private class MyPlaceField extends Field {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      if (GPS_PTN.matcher(field).matches()) {
        setGPSLoc(field, data);
      } else {
        data.strPlace = field;
      }
    }

    @Override
    public String getFieldNames() {
      return "GPS PLACE";
    }
  }


  private static final Pattern INFO_PFX_PTN = Pattern.compile("\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d - *");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      Matcher match = INFO_PFX_PTN.matcher(field);
      if (match.lookingAt()) field = field.substring(match.end());
      super.parse(field,  data);;
    }
  }
}
