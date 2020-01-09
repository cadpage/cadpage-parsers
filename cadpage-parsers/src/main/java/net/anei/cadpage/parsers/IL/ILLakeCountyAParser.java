package net.anei.cadpage.parsers.IL;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class ILLakeCountyAParser extends FieldProgramParser {
  
  public ILLakeCountyAParser() {
    super("LAKE COUNTY", "IL",
          "Incident:ID! Nat:CALL! Loc:ADDRCITY! Grid:MAP! Tac:CH! Trucks:UNIT! Notes:INFO!");
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    body = body.replace('\0', '\n');
    return super.parseMsg(body, data);
  }
  
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    return super.getField(name);
  }
  
  private static final Pattern ADDR_CITY_APT_PTN = Pattern.compile("(.*?)-(?:UNINC. )?([A-Z][A-Z ]+)(?:[- ]+(\\d.*))?");
  
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldEnd(field, "-");
      if (field.length() == 0) return;
      String apt = "";
      Matcher match = ADDR_CITY_APT_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1).trim();
        data.strCity = match.group(2);
        apt = getOptGroup(match.group(3));
      }
      parseAddress(field, data);
      data.strApt = append(data.strApt, "-", apt);
    }
  }
}
