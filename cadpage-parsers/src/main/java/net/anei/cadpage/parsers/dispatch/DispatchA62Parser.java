package net.anei.cadpage.parsers.dispatch;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class DispatchA62Parser extends FieldProgramParser {
  
  public DispatchA62Parser(String defCity, String defState) {
    super(defCity, defState, 
          "CALLTYPE:CALL! INCIDENT:ID! COMMONNAME:PLACE! HOUSE:ADDR1! STREET:ADDR2! APT/CROSSSTREET:APT! CITY:CITY! STATE:ST! ZIP:SKIP! CALLER:NAME! CALLBACK#:PHONE! EXT:EMPTY! NARRATIVE:INFO! INFO/CS+ CADNOTES:INFO/N! INFO/CS+");
  }
  
  private static final Pattern MASTER1 = Pattern.compile("([ A-Z]+) (\\d{4}-\\d{6})\n([^\n]*)\n(?:Caller: *(.*)\n)?(?:Callback #: *(.*)\n)?(.*)", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
  private static final Pattern ST_ZIP_PTN = Pattern.compile("(.*), *([A-Z]{2})(?: +\\d{5})?");
  private static final Pattern ADDR_APT_PTN = Pattern.compile("(.*), *(?:APT|ROOM|LOT|) *(\\S*)", Pattern.CASE_INSENSITIVE);
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    Matcher match = MASTER1.matcher(body);
    if (match.matches()) {
      setFieldList("CALL ID PLACE ADDR APT CITY ST NAME PHONE INFO");
      data.strCall = match.group(1).trim();
      data.strCallId = match.group(2);
      String addr = match.group(3).trim();
      data.strName = getOptGroup(match.group(4));
      data.strPhone = getOptGroup(match.group(5));
      data.strSupp = match.group(6).trim();
      
      match = ST_ZIP_PTN.matcher(addr);
      if (match.matches()) {
        addr = match.group(1).trim();
        data.strState = match.group(2);
      }
      int pt = addr.lastIndexOf(',');
      if (pt < 0) return false;
      data.strCity = addr.substring(pt+1).trim();
      addr = addr.substring(0,pt).trim();
      
      String apt = "";
      match = ADDR_APT_PTN.matcher(addr);
      if (match.matches()) {
        addr = match.group(1).trim();
        apt = match.group(2);
      }
      
      pt = addr.lastIndexOf(',');
      if (pt >= 0) {
        data.strPlace = addr.substring(0,pt).trim();
        addr = addr.substring(pt+1).trim();
      }
      
      parseAddress(addr, data);
      data.strApt = append(data.strApt, "-", apt);
      return true;
    }
    
    return parseFields(body.split(","), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR1")) return new MyAddressField(1);
    if (name.equals("ADDR2")) return new MyAddressField(2);
    if (name.equals("APT")) return new MyAptField();
    return super.getField(name);
  }
  
  private class MyAddressField extends AddressField {
    
    private int type;
    
    public MyAddressField(int type) {
      this.type = type;
    }
    
    @Override
    public void parse(String field, Data data) {
     if (type == 1) {
       data.strAddress = field;
     }
     else {
       field = append(data.strAddress, " ", field);
       data.strAddress = "";
       super.parse(field, data);
     }
    }
  }
  
  private static final Pattern APT_PTN = Pattern.compile("\\d+[A-Z]?|(?:APT|LOT|ROOM|RM) *(.*)", Pattern.CASE_INSENSITIVE);
  private class MyAptField extends Field {
    @Override
    public void parse(String field, Data data) {
      Matcher match = APT_PTN.matcher(field);
      if (match.matches()) {
        String apt = match.group(1);
        if (apt == null) apt = field;
        data.strApt = append(data.strApt, "-", apt);
      } else {
        data.strPlace = append(data.strPlace, " - ", field);
      }
    }

    @Override
    public String getFieldNames() {
      return "APT PLACE";
    }
  }
}
