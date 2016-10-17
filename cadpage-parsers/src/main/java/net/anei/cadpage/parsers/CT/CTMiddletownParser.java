package net.anei.cadpage.parsers.CT;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Middletown, CT
 */
public class CTMiddletownParser extends FieldProgramParser {
  
  public CTMiddletownParser() {
    super(CITY_LIST, "MIDDLETOWN","CT",
          "UNIT CALL ADDR/S! PLACE INFO+");
  }
  
  @Override 
  public String getFilter() {
    return "911@middletownct.gov,911@cityofmiddletown.com";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("CAD System Message")) return false;
    return parseFields(body.split("\n"), 3, data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    return super.getField(name);
  }
  
  private static final Pattern ADDR_APT_PTN = Pattern.compile("(.*) , Apt\\. (.*)");
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = ADDR_APT_PTN.matcher(field);
      if (match.matches()) {
        parseAddress(match.group(1).trim(), data);
        parseAddress(StartType.START_OTHER, FLAG_ONLY_CITY | FLAG_ANCHOR_END, match.group(2).trim(), data);
        data.strApt = append(data.strApt, "-", getStart());
      } else {
        super.parse(field, data);
      }
    }
  }
  
  private static final String[] CITY_LIST = new String[]{
    "CROMWELL",
    "MIDDLETOWN"
  };
}
