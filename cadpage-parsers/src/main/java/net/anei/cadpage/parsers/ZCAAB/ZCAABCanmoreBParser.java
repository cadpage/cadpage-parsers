package net.anei.cadpage.parsers.ZCAAB;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeTable;
import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.StandardCodeTable;

public class ZCAABCanmoreBParser extends FieldProgramParser {
  
  public ZCAABCanmoreBParser() {
    super(CITY_LIST, "CANMORE", "AB", 
          "Loc:ADDRCITY! Ty:CODE! Ev:ID! LL:GPS END");
  }
  
  @Override
  public String getFilter() {
    return "disp@calgary.ca>";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("IPS I/Page Notification")) return false;
    int pt = body.indexOf('\n');
    if (pt >= 0) body = body.substring(0,pt).trim();
    return super.parseMsg(body, data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("CODE")) return new MyCodeField();
    return super.getField(name);
  }
  
  private class MyAddressCityField extends Field {

    @Override
    public void parse(String field, Data data) {
      field = field.replace('_', ' ');
      if (field.startsWith("LL(")) {
        parseAddress(StartType.START_OTHER, FLAG_ONLY_CITY | FLAG_ANCHOR_END, field, data);
        data.strAddress = getStart();
      } else {
        Parser p = new Parser(field);
        data.strPlace = p.getLastOptional(": @");
        String apt = p.getLastOptional(',');
        if (apt.isEmpty()) apt = p.getLastOptional(';');
        parseAddress(StartType.START_ADDR, FLAG_ANCHOR_END, p.get(), data);
        data.strApt = append(data.strApt, "-", apt);
      }
    }

    @Override
    public String getFieldNames() {
      return "ADDR CITY APT PLACE";
    }
  }
  
  private class MyCodeField extends Field {
    @Override
    public void parse(String field, Data data) {
      data.strCode = field;
      String call = CALL_CODES.getCodeDescription(data.strCode);
      if (call != null) data.strCall = call;
    }
    
    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }
  
  private static final Pattern PA_PTN = Pattern.compile("\bPA\b");
  
  @Override
  public String adjustMapAddress(String addr) {
    return PA_PTN.matcher(addr).replaceAll("PATH");
  }
  
  private CodeTable CALL_CODES = new StandardCodeTable();

  private static final String[] CITY_LIST = new String[]{
      "BIGHORN",
      "CANMORE",
      "HARVIE HEIGHTS"
  };
}
