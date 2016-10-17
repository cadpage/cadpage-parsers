package net.anei.cadpage.parsers.TX;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Cy Creek Comm Center
 */
public class TXCyFairParser extends FieldProgramParser {
  
  private static final Pattern MASTER1 = Pattern.compile("(.*?) ([A-Z]{4,5}\\d{10}) (?:(\\d{1,2}[A-Z]\\d{1,2}[A-Z]?) \\d+ )?(.*?)([A-Z]+\\d+[A-Z]?) (\\d\\d/\\d\\d/\\d{4}) (\\d\\d:\\d\\d:\\d\\d)(\\d\\d/\\d\\d/\\d{4} \\d\\d:\\d\\d:\\d\\d)*");
  private static final Pattern MISSING_BLANK_PTN = Pattern.compile("([^/]+ /[^/]+[^ ])(/.*)");
  
  public TXCyFairParser() {
    super("HARRIS", "TX",
           "UNIT CALL ( ADDR/ZSXx PLACE MAP! END" +
                     "| ADDR/Z ( PLACE2 END " +
                              "| PLACE2 X/Z END " +
                              "| X APT PLACE! SUB:CITY! MAP:MAP! UNITS_ASSIGNED:UNIT! RespInfo%EMPTY END ) )");
  }
  
  @Override
  public String getFilter() {
    return "CAD@CYFAIRVFD.ORG";
  }

  @Override
  public String getLocName() {
    return "Cypress-Harris, TX";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    
    Matcher match = MASTER1.matcher(body);
    if (match.matches()) {
      setFieldList("ADDR APT ID CODE CALL DATE TIME");
      parseAddress(match.group(1).trim(), data);
      data.strCallId = match.group(2);
      data.strCode = getOptGroup(match.group(3));
      data.strCall = match.group(4).trim();
      data.strDate = match.group(5);
      data.strTime = match.group(6);
      return true;
    }
    
    match = MISSING_BLANK_PTN.matcher(body);
    if (match.matches()) {
      body = match.group(1) + ' ' + match.group(2);
    }
    return parseFields(body.split(" /"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("PLACE2")) return new MyPlace2Field();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("CITY")) return new MyCityField();
    if (name.equals("MAP")) return new MapField("\\d{3}[A-Z]", true);
    return super.getField(name);
  }
  
  private static final Pattern CODE_PTN = Pattern.compile("^(\\d{1,2}[A-Z]\\d{2}[A-Z]?) +");
  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CODE_PTN.matcher(field);
      if (match.find()) {
        data.strCode = match.group(1);
        field = field.substring(match.end());
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }
  
  private class MyPlace2Field extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      data.defCity = "";
      super.parse(field, data);
    }
  }
  
  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace("No X Street Found", "");
      field = stripFieldStart(field, "/");
      field = stripFieldEnd(field, "/");
      super.parse(field, data);
    }
  }
  
  private class MyCityField extends CityField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf('(');
      if (pt >= 0) {
        data.strSupp = field.substring(pt);
        field = field.substring(0,pt).trim();
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "CITY INFO";
    }
  }
  
  @Override
  public String adjustMapCity(String city) {
    int pt = city.indexOf(" - ");
    if (pt >= 0) city = city.substring(0,pt).trim();
    return convertCodes(city, ADJUST_CITY_TABLE);
  }
  
  private static final Properties ADJUST_CITY_TABLE = buildCodeTable(new String[]{
      "FAIRFIELD",         "",
      "MUELLER CEMETERY",  ""
  });
}
