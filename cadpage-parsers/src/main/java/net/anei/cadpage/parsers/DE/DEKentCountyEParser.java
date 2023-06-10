package net.anei.cadpage.parsers.DE;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Kent County, DE
 */
public class DEKentCountyEParser extends DEKentCountyBaseParser {

  public DEKentCountyEParser() {
    super("KENT COUNTY", "DE",
          "Call_Type:CALL! Address:ADDRCITY/SXa! Dev/Sub:PLACE! Xst's:X");
    setupMultiWordStreets("ALLEY MILL");
  }

  @Override
  public String getFilter() {
    return "CADCentral@State.DE.US";
  }

  @Override
  public boolean parseMsg(String subject, String body, Data data) {

    if (!subject.equals("!")) return false;
    body = body.replace(" Dev/Sub:", "\nDev/Sub:");
    return parseFields(body.split("\n"), data);
  }

  @Override
  protected Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("X")) return new MyCrossField();
    return super.getField(name);
  }

  private static final Pattern CALL_CODE_PTN = Pattern.compile("^(\\d{1,2}[A-Z]\\d{1,2}[A-Z]?) (?:- )? *(.*)");
  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CALL_CODE_PTN.matcher(field);
      if (match.matches()) {
        data.strCode = match.group(1);
        field = match.group(2);
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }

  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {

      int pt = field.lastIndexOf(':');
      if (pt >= 0) {
        data.strPlace = field.substring(pt+1).trim();
        field = field.substring(0,pt).trim();
      }
      field = field.replace('@', '&');
      if (field.startsWith("LAT:")) {
        data.strAddress = field;
      } else {
        super.parse(field, data);
      }
      adjustCityState(data);
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " ST PLACE";
    }
  }

  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("No Cross Streets Found")) return;
      super.parse(field, data);
    }
  }

  @Override
  public String adjustMapAddress(String addr) {
    addr = COR_PTN.matcher(addr).replaceAll("CORNER");
    return super.adjustMapAddress(addr);
  }
  private static final Pattern COR_PTN = Pattern.compile("\\bCOR\\b");
}


