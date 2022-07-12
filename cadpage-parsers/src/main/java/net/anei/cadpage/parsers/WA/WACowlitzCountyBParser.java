package net.anei.cadpage.parsers.WA;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class WACowlitzCountyBParser extends FieldProgramParser {
  
  public WACowlitzCountyBParser() {
    super("COWLITZ COUNTY", "WA", 
          "UPDATE? CALL ADDR CITY! END");
  }

  @Override
  public String getFilter() {
    return "noreply@ispyfire.com";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("iSpyFire Message")) return false;
    return parseFields(body.split("\n"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("UPDATE")) return new SkipField("UPDATED .*|RETONE:");
    if (name.equals("ADDR")) return new MyAddressField();
    return super.getField(name);
  }
  
  private static final Pattern APT_PTN = Pattern.compile("[A-Z]?\\d{1,4}[A-Z]?|[A-Z]");
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      for (String part : field.split(";")) {
        part = part.trim();
        if (data.strAddress.isEmpty()) {
          parseAddress(part, data);
        } else if (APT_PTN.matcher(part).matches()) {
          data.strApt = append(data.strApt, "-", part);
        } else {
          data.strPlace = append(data.strPlace, " - ", part);
        }
      }
    }
    
    @Override
    public String getFieldNames() {
      return "ADDR PLACE APT";
    }
    
  }
}
