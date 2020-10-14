package net.anei.cadpage.parsers.KY;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class KYKentonCountyAParser extends FieldProgramParser {
  
  private static final Pattern DELIM = Pattern.compile(" ;(?= )");
  
  public KYKentonCountyAParser() {
    super("KENTON COUNTY", "KY",
          "CALL PLACE ADDR1 ADDR1 ADDR1 ADDR1 ADDR2 EMPTY CITY APT ID INFO ADDR3 ADDR3 ADDR3 ADDR3 ADDR3 X! UNIT");
  }
  
  @Override
  public String getFilter() {
    return "@kentoncounty.org";
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("From KCECC")) return false;
    if (body.endsWith(";")) body += ' ';
    return parseFields(DELIM.split(body, -1), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR1")) return new MyAddressField(1);
    if (name.equals("ADDR2")) return new MyAddressField(2);
    if (name.equals("ADDR3")) return new MyAddressField(3);
    if (name.equals("CITY")) return new MyCityField();
    if (name.equals("ID")) return new IdField("\\d{4}-\\d{8}|", true);
    if (name.equals("X")) return new MyCrossField();
    return super.getField(name);
  }
  
  private static final Pattern ADDR_CITY_ST_PTN = Pattern.compile("(.*),([ A-Z]+?)(?: ([A-Z]{2}))?");
  private class MyAddressField extends AddressField {
    
    private int type;
    
    public MyAddressField(int type) {
      this.type = type;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (field.length() != 0) {
        if (type == 3 && data.strAddress.length() > 0 && !data.strAddress.contains("&")) {
          data.strAddress += " &";
        }
        data.strAddress = append(data.strAddress, " ", field);
      }
      
      if (type == 2) {
        Matcher match = ADDR_CITY_ST_PTN.matcher(data.strAddress);
        if (match.matches()) {
          data.strAddress = match.group(1).trim();
          data.strCity = match.group(2).trim();
          data.strState = getOptGroup(match.group(3));
        }
      }
    }
    
    @Override
    public String getFieldNames() {
      return "ADDR APT CITY ST";
    }
  }
  
  private class MyCityField extends CityField {
    @Override
    public void parse(String field, Data data) {
      if (field.length() == 0) return;
      super.parse(field, data);
    }
  }
  
  private static final Pattern CROSS_PTN = Pattern.compile("[ ,A-Z0-9]+ / [ ,A-Z0-9]+");
  private class MyCrossField extends CrossField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      if (field.length() == 0) return true;
      if (field.equals("No Cross Streets Found")) return true;
      if (!CROSS_PTN.matcher(field).matches()) return false;
      super.parse(field, data);
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }
}
