package net.anei.cadpage.parsers.PA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class PAMontgomeryCountyNParser extends FieldProgramParser {

  public PAMontgomeryCountyNParser() {
    super(PAMontgomeryCountyParser.CITY_CODES, "MONTGOMERY COUNTY", "PA",
          "( Address:ADDR! Cross_Streets:X! Municipality:CITY! Nature:CALL! Map/Box_Plan:MAP! Incident_Number:ID! Dispatch_Time:TIME! Notes:INFO1! INFO1/N+ Trucks:UNIT! END " +
          "| ADDRCITYST_CALL! INFO2/N+ )");
  }

  @Override
  public String getFilter() {
    return "noreply@everbridge.net";
  }

  private static final Pattern START_MARKER_PTN = Pattern.compile("<p> DPS Staff Alert(?: Update)?::<br>|FieldComm has been requested:<br>");

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    Matcher match = START_MARKER_PTN.matcher(body);
    if (!match.find()) return false;
    body = body.substring(match.end());
    int pt = body.indexOf("</p>");
    if (pt < 0) return false;
    body = body.substring(0,pt).trim();
    return super.parseHtmlMsg(subject, body, data);
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("ADDRCITYST_CALL")) return new MyAddressCityStateCallField();
    if (name.equals("INFO1")) return new MyInfo1Field();
    if (name.equals("INFO2")) return new MyInfo2Field();
    return super.getField(name);
  }

  private class MyAddressCityStateCallField extends AddressCityStateField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.lastIndexOf(',');
      if (pt < 0) abort();
      data.strCall = field.substring(pt+1).trim();
      field = field.substring(0,pt).trim();
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " CALL";
    }
  }
  
  private Pattern INFO1_BRK_PTN = Pattern.compile(" +-- +");
  private class MyInfo1Field extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = INFO1_BRK_PTN.matcher(field).replaceAll("\n");
      super.parse(field, data);
    }
  }

  private class MyInfo2Field extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "NOTES:");
      super.parse(field, data);
    }
  }
}
