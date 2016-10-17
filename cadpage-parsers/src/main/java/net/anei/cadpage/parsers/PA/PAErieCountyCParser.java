package net.anei.cadpage.parsers.PA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class PAErieCountyCParser extends FieldProgramParser {
  
  private static final Pattern MARKER = Pattern.compile("^(Belle Valley) / ");
  
  public PAErieCountyCParser() {
    super(PAErieCountyParser.CITY_LIST, "ERIE COUNTY", "PA",
           "ADDR/SC! Apt:APT Bldg:APT XS:X!");
  }
  
  @Override
  public String getFilter() {
    return "messaging@iamresponding.com";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    do {
      if (subject.equals("Belle Valley")) {
        data.strSource = subject;
        break;
      }
      Matcher match = MARKER.matcher(body);
      if (match.find()) {
        data.strSource = match.group(1);
        body = body.substring(match.end()).trim();
        break;
      }
      return false;
    } while (false);
    
    body = body.replace(" Apt ", " Apt: ").replace(" Bldg ", " Bldg: ")
               .replace(" XS ", " XS: ");
    return super.parseMsg(body, data);
  }
  
  private class MyAddressField extends AddressField {
    
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf('>');
      if (pt < 0) abort();
      data.strCode = field.substring(0,pt).trim();
      field = field.substring(pt+1).trim();
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "CODE " + super.getFieldNames();
    }
  }
  
  private class MyAptField extends AptField {
    @Override
    public void parse(String field, Data data) {
      data.strApt = append(field, " - ", data.strApt);
    }
  }
  
  private class MyCrossField extends CrossField {
    
    @Override
    public void parse(String field, Data data) {
      parseAddress(StartType.START_ADDR, FLAG_ONLY_CROSS | FLAG_ONLY_CITY, field, data);
      data.strName = getLeft();
    }
    
    @Override
    public String getFieldNames() {
      return "X CITY NAME";
    }
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("APT")) return new MyAptField();
    if (name.equals("X")) return new MyCrossField();
    return super.getField(name);
  }
  
  @Override
  public String getProgram() {
    return "SRC " + super.getProgram();
  }
}
