package net.anei.cadpage.parsers.NY;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class NYMontgomeryCountyBParser extends FieldProgramParser {
  
  public NYMontgomeryCountyBParser() {
    super(CITY_LIST, "MONTGOMERY COUNTY", "NY", 
          "CALL PLACE ADDR/S X DATETIME INFO GPS EMPTY! END");
  }
  
  @Override
  public String getFilter() {
    return "impactalerts@sheriff.montgomery.ny.us";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!parseFields(body.split(";",-1), data)) return false;
    if (data.strPlace.startsWith("MM ")) {
      data.strAddress = append(data.strAddress, " ", data.strPlace);
      data.strPlace = "";
    }
    return true;
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new MyDateTimeField();
    return super.getField(name);
  }
  
  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d\\d/\\d\\d/\\d{4}) (\\d\\d)(\\d\\d)");
  private class MyDateTimeField extends DateTimeField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_TIME_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strDate = match.group(1);
      data.strTime = match.group(2)+':'+match.group(3);
    }
  }
  
  private static final String[] CITY_LIST = new String[] {
    "AMES",
    "AMSTERDAM",
    "CANAJOHARIE",
    "CHARLESTON",
    "FLORIDA",
    "FONDA",
    "FORT JOHNSON",
    "FORT PLAIN",
    "FULTONVILLE",
    "GLEN",
    "HAGAMAN",
    "MINDEN",
    "MOHAWK",
    "NELLISTON",
    "PALATINE",
    "PALATINE BRIDGE",
    "ROO",
    "ST JOHNSVILLE",
  };

}
