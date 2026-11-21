package net.anei.cadpage.parsers.PA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class PAWestmorelandCountyBParser extends FieldProgramParser {

  public PAWestmorelandCountyBParser() {
    super("WESTMORELAND COUNTY", "PA",
          "Agencies:SRC! Call_Type:CALL! ID_URL! Received_On:SKIP! Location:ADDRCITYST! Dispatch_Notes:INFO/N! INFO/N+ Units:UNIT END");
  }

  @Override
  public String getFilter() {
    return "Pathfinder-Administration@codysystems.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Dispatched Call Information")) return false;
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ID_URL")) return new MyIdURLField();
    if (name.equals("ADDRCITYST")) return new MyAddressCityStateField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern ID_URL_PTN = Pattern.compile("CFS-(\\d\\d-\\d{6}) \\((https:.*)\\)");
  private class MyIdURLField extends Field {
    @Override
    public void parse(String field, Data data) {
      Matcher match = ID_URL_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strCallId = match.group(1);
      data.strInfoURL = match.group(2).trim();
    }

    @Override
    public String getFieldNames() {
      return "ID URL";
    }
  }

  private class MyAddressCityStateField extends AddressCityStateField {
    @Override
    public void parse(String field, Data data) {
      if (field.endsWith(")")) {
        int pt = field.indexOf('(');
        if (pt >= 0) {
          data.strPlace = field.substring(pt+1, field.length()-1).trim();
          field = field.substring(0,pt).trim();
        }
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " PLACE";
    }
  }

  private static final Pattern INFO_HDR_PTN = Pattern.compile("\\[\\d\\d:\\d\\d:\\d\\d\\] [A-Za-z ]+: *");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = INFO_HDR_PTN.matcher(field);
      if (match.lookingAt()) field = field.substring(match.end());
      super.parse(field, data);
    }
  }
}
