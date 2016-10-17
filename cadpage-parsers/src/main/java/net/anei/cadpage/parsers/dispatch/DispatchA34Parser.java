package net.anei.cadpage.parsers.dispatch;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Golden, CO
 */
public class DispatchA34Parser extends FieldProgramParser {
  
  private static final Pattern ENDLINE = Pattern.compile("\\s*\n");
  private static final Pattern DELIM = Pattern.compile("\\*\n");
  
  public DispatchA34Parser(String defCity, String defState) {
    super(defCity, defState,
          "Call#:ID! Time:TIME! Type_of_call:CALL! Location:ADDR/SXa! Cross_streets:X? Map_page:MAP? Units:UNIT? Narrative:INFO");
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    body = ENDLINE.matcher(body).replaceAll("\n");
    
    int pt = body.indexOf("\n\n\n\n");
    if (pt >= 0) body = body.substring(0,pt);
    
    return parseFields(DELIM.split(body), 6, data);
  }
  
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      String[] parts = field.split(",");
      if (parts.length > 3) abort();
      int ndx = 0;
      if (parts.length == 3) data.strPlace = parts[ndx++].trim();
      super.parse(parts[ndx++].trim(), data);
      if (ndx < parts.length) data.strCity = parts[ndx].trim();
      
      if (data.strApt.contains("MM") || data.strApt.equals("HWY")) {
        data.strAddress = data.strAddress + " " + data.strApt;
        data.strApt = "";
      }
    }
    
    @Override
    public String getFieldNames() {
      return "PLACE ADDR APT CITY";
    }
  }
  
  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("/")) return;
      super.parse(field, data);
    }
  }
  
  private class MyMapField extends MapField {
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("MAP ")) field = field.substring(4).trim();
      super.parse(field, data);
    }
  }
  
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      data.strSupp = append(data.strSupp, "\n", field);
    }
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("MAP")) return new MyMapField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
}
