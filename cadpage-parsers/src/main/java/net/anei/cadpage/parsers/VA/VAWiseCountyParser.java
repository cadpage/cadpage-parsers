package net.anei.cadpage.parsers.VA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

/**
 * Wise County, VA
 */

public class VAWiseCountyParser extends FieldProgramParser {
  public VAWiseCountyParser() {
    this("WISE COUNTY", "VA");
  }
  
  public VAWiseCountyParser(String defCity, String defState) {
    super(defCity, defState,
          "SRC ( UNIT/Z ID TIMES! TIMES+ | CALL ADDR MAP UNIT INFO+ )");
  }
  
  @Override
  public String getAliasCode() {
    return "VAWiseCounty";
  }

  @Override
  public String getFilter() {
    return "pagers@dc911.org";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split("\n"), data);
  }
 
  @Override
  public Field getField(String name) {
    if (name.equals("SRC"))return new SourceField("[A-Z]{3,4}", true);
    if (name.equals("ID")) return new IdField("\\d\\d[A-Z]{3}\\d{4}|\\d+");
    if (name.equals("TIMES")) return new MyTimesField();
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("MAP")) return new MapField("\\d{3}[A-Z]?", true);
    if (name.equals("UNIT")) return new UnitField("[A-Z]{4,6}|ER|[A-Z]{3,4}\\d+", true);
    return super.getField(name);
  }
  
  private class MyTimesField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      data.msgType = MsgType.RUN_REPORT;
      field = field.replace('.', ':');
      data.strSupp = append(data.strSupp, "\n", field);
    }
  }
  
  private static final Pattern ADDR_CITY_ST_PTN = Pattern.compile("(.*),(.*?)(?: (TN))?");
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldEnd(field, "@");
      
      Matcher match = ADDR_CITY_ST_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1).trim();
        data.strCity = match.group(2).trim();
        data.strState = getOptGroup(match.group(3));
      }
      
      else if (field.endsWith("; U")) {
        field = field.substring(0,field.length()-3).trim();
        data.defCity = "";
      }
      super.parse(field,  data);
    }
  }
}
