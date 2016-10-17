package net.anei.cadpage.parsers.CA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Stockton, CA
 */
public class CAStocktonAParser extends FieldProgramParser {
  
  public CAStocktonAParser() {
    super("STOCKTON", "CA",
           "( TIMES_REPORT/R SKIP! CFS_Number:ID! CALL! LOCATION:ADDR! RECEIVED:DATETIME! INFO/N+ "
        +  "| CFS:ID! INCIDENT_#:SKIP RECEIVED:DATETIME DISTRICT_#:MAP MAP? TYPE:CALL! "
        +  "LOCATION:ADDR BUSINESS_NAME:PLACE CROSS_STREETS:X APPARATUS:UNIT "
        +  "( HAZARDS:INFO | PREMISE_HX:INFO ) INFO+ )");
  }
  
  @Override
  public String getFilter() {
    return "StocktonFireCAD@ci.stockton.ca.us";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split("\n+ *"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("TIMES_REPORT")) return new SkipField("RESPONDING UNIT TIMES REPORT", true);
    if (name.equals("MAP")) return new MyMapField(".*DISTRICT.*|", true);
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("UNIT")) return new MyUnitField();
    return super.getField(name);
  }
  
  private class MyMapField extends MapField {
    MyMapField(String p, boolean h) {
      super(p, h);
    }
    
    @Override
    public void parse(String field, Data data) {
      String[] mapSplit = field.trim().split("DISTRICT");
      for (int i=0; i<mapSplit.length; i++) {
        String piece = mapSplit[i].trim();
        if (!piece.equals(""))
          data.strMap = append(data.strMap, "/", piece);
      }
    }
  }
  
  
  private static final Pattern CALL_PATTERN = Pattern.compile("\\b([A-Z0-9]+)\\b(.+)");
  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      field = field.trim();
      Matcher m = CALL_PATTERN.matcher(field);
      if (m.matches()) {
        data.strCode = m.group(1);
        data.strCall = m.group(2).trim();
      }
      else
        data.strCall = field;
    }
    
    @Override
    public String getFieldNames() {
      return "CODE " + super.getFieldNames();
    }
  }
  
  private static final Pattern ADDR_PATTERN = Pattern.compile("(.*?) {2,}(.*)");
  private static final Pattern DIR_SLASH_OF_PTN = Pattern.compile("\\b([NSEW])/([OB])\\b");
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      String apt = "";
      Matcher m = ADDR_PATTERN.matcher(field);
      if (m.matches()) {
        field = m.group(1);
        String tmp = m.group(2);
        if (tmp.contains(" ")) {
          data.strPlace = tmp;
        } else {
          apt = tmp;
        }
      }
      field = DIR_SLASH_OF_PTN.matcher(field).replaceAll("$1$2");
      super.parse(field, data);
      data.strApt = append(data.strApt, "-", apt);
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames()+" APT PLACE";
    }
  }
  
  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      data.strCross = field.replaceAll(" *\\* *", "/");
    }
  }
  
  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      field = field.replaceAll("  +", " ");
      super.parse(field, data);
    }
  }
}
