package net.anei.cadpage.parsers.NJ;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class NJSussexCountyFParser extends FieldProgramParser {

  public NJSussexCountyFParser() {
    super("SUSSEX COUNTY", "NJ",
          "TIMEDATE! EMPTY ID! Responding_Units:UNIT! Nature:CALL! Address:ADDR! Cross_Streets:X! City:CITY! Zone:MAP! " +
              "Comments:EMPTY! INFO/N+ Directions:DIRS! DIRS+? GPS! END");
  }

  @Override
  public String getFilter() {
    return "FRN-sussexcountynj@email.getrave.com";
  }

  private String directions;

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Sussex County 9-1-1 Center")) return false;
    body = body.replace(" Zone:", "\nZone:");
    directions = "";
    if (!parseFields(body.split("\n"), data)) return false;
    data.strSupp = append(directions, "\n", data.strSupp);
    return true;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("TIMEDATE")) return new TimeDateField("\\d\\d:\\d\\d:\\d\\d \\d\\d/\\d\\d/\\d{4}", true);
    if (name.equals("ID")) return new IdField("INCIDENT # *(.*)", true);
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("DIRS")) return new MyDirectionsField();
    if (name.equals("GPS")) return new MyGPSField();
    return super.getField(name);
  }

  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(';');
      if (pt >= 0) {
        data.strPlace = field.substring(pt+1).trim();
        field = field.substring(0,pt).trim();
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " PLACE";
    }
  }

  private static final Pattern INFO_JUNK_PTN = Pattern.compile("\\d\\d:\\d\\d:\\d\\d +\\d\\d/\\d\\d/\\d{4} .*");

  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (INFO_JUNK_PTN.matcher(field).matches()) return;
      super.parse(field, data);
    }
  }

  private class MyDirectionsField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      directions = append(directions, "\n", field);
    }
  }

  private static final Pattern GPS_PTN = Pattern.compile("Lat= *(.*?) +Lon= *(.*)");
  private class MyGPSField extends GPSField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = GPS_PTN.matcher(field);
      if (!match.matches()) return false;
      super.parse(match.group(1)+','+match.group(2), data);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }
}
