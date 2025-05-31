package net.anei.cadpage.parsers.AR;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class ARFaulknerCountyParser extends FieldProgramParser {

  public ARFaulknerCountyParser() {
    super(AUX_CITY_LIST, "FAULKNER COUNTY", "AR",
          "Incident:EMPTY! Nature:CALL! Address:ADDRCITY! Cross_Streets:X? Priority:PRI! Coordinates:GPS? ID:ID! Units:UNIT! Comments:EMPTY! INFO/N+");
  }

  @Override
  public String getFilter() {
    return "messaging@iamresponding.com";
  }

  private static final Pattern SRC_PTN = Pattern.compile("(.*?) +CAD Incident");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    Matcher match = SRC_PTN.matcher(subject);
    if (!match.matches()) return false;
    data.strSource = match.group(1);
    body = body.replace("\n\t", "\n").replace(": : ", ": ");
    return parseFields(body.split("\n"), data);
  }

  @Override
  public String getProgram() {
    return "SRC " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "message:");
      super.parse(field, data);
    }
  }

  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      super.parse(field, data);
      if (data.strCity.equals("AR")) {
        data.strState = data.strCity;
        data.strCity = "";
        parseAddress(StartType.START_ADDR, FLAG_ANCHOR_END, data.strAddress, data);
      }
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " ST";
    }
  }

  private static final String[] AUX_CITY_LIST = new String[] {
      "PULASKI COUNTY"
  };
}
