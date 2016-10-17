package net.anei.cadpage.parsers.CA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

  public class CAKernCountyParser extends FieldProgramParser {

    public CAKernCountyParser() {
      super("KERN COUNTY", "CA", 
            "ADDR X! INFO/N+");
      setupCallList(CALL_LIST);
      setupMultiWordStreets(
          "CHINA LAKE",
          "CUMMINGS VALLEY",
          "HORACE MANN",
          "KING ARTHUR",
          "MT VERNON",
          "OFFICE PARK",
          "SANTA MARIA"
      );
    }
    
    @Override
    public String getFilter() {
      return "Dispatch@co.kern.ca.us";
    }
    
    @Override
    public boolean parseMsg(String subject, String body, Data data) {
      if (!subject.equals("!")) return false;
      return parseFields(body.split("\n"), data);
    }

    @Override
    public Field getField(String name) {
      if (name.equals("ADDR")) return new MyAddressField();
      if (name.equals("X")) return new MyCrossField();
      return super.getField(name);
    }
    
    private static final Pattern CALL_ADDR_MAP_PTN = Pattern.compile("([A-Z]+\\d?) ?- (.*) ([A-Z]\\d{2,}(?:-\\d+)?+(?:-[A-Z])?) *(.*)"); 
    private class MyAddressField extends AddressField {
      @Override
      public void parse(String field, Data data) {
        Matcher match = CALL_ADDR_MAP_PTN.matcher(field);
        if (!match.matches()) abort();
        data.strCode = match.group(1);
        field = match.group(2).trim();
        data.strMap = match.group(3);
        data.strPlace = match.group(4);
        
        int pt = field.lastIndexOf(',');
        if (pt < 0) abort();
        data.strCity = field.substring(pt+1).trim();
        field = field.substring(0,pt).trim();
        field = field.replace('@', '&');
        parseAddress(StartType.START_CALL, FLAG_START_FLD_REQ | FLAG_RECHECK_APT | FLAG_ANCHOR_END, field, data);
      }
      
      @Override
      public String getFieldNames() {
        return "CODE CALL ADDR APT CITY MAP PLACE";
      }
    }
    
    private class MyCrossField extends CrossField {
      @Override
      public void parse(String field, Data data) {
        if (field.startsWith("No Cross Streets Found")) {
          field = field.substring(22).trim();
          if (field.length() == 0) return;
          if (!field.startsWith("X ")) {
            data.strPlace = append(data.strPlace, " - ", field);
            return;
          }
          field = field.substring(2).trim();
        }
        String cross = "";
        int pt = field.lastIndexOf(',');
        if (pt >= 0) {
          cross = field.substring(0,pt).trim();
          field = field.substring(pt+1).trim();
        }
        parseAddress(StartType.START_ADDR, FLAG_ONLY_CROSS, field, data);
        data.strCross = append(cross, ", ", data.strCross);
        data.strPlace = append(data.strPlace, " - ", getLeft());
      }
      
      @Override
      public String getFieldNames() {
        return "X PLACE";
      }
    }
    
    private CodeSet CALL_LIST = new CodeSet(
        "AMB ONLY CODE 2",
        "COMMERCIAL FIRE ALARM",
        "HAZARDOUS CONDITION",
        "MEDICAL AID",
        "STRUCTURE FIRE REINFORCED",
        "STRUCTURE FIRE / RESPONSE"
    );
  }
