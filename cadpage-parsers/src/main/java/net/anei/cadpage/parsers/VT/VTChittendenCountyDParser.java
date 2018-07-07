package net.anei.cadpage.parsers.VT;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class VTChittendenCountyDParser extends FieldProgramParser {
  
  public VTChittendenCountyDParser() {
    this("CHITTENDEN COUNTY");
  }
  
  VTChittendenCountyDParser(String county) {
    super(county, "VT",
          "CALL ADDR EMPTY X GPS! INFO/N+");
  }
  
  @Override
  public String getFilter() {
    return "@alert.active911.com";
  }
  
  @Override
  public String getAliasCode() {
    return "VTChittendenCountyD";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split("\\|"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("GPS")) return new GPSField("Lat:.*? Lon:.*", true);
    return super.getField(name);
  }
  
  private static final Pattern ADDR_APT_PTN = Pattern.compile("(?:APT|UNIT|RM|ROOM|LOT) +(.*)|\\d{1,5}[A-Z]?|[A-Z]", Pattern.CASE_INSENSITIVE);
  private class MyAddressField extends Field {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      data.strCity = p.getLastOptional(':');
      parseAddress(p.get(';'), data);
      while (!p.isEmpty()) {
        String token = p.get(';');
        Matcher match = ADDR_APT_PTN.matcher(token);
        if (match.matches()) {
          String apt = match.group(1);
          if (apt == null) apt = token;
          data.strApt = append(data.strApt, "-", apt);
        } else if (token.startsWith("MM ") || token.startsWith("mm ")) {
          data.strAddress = append(data.strAddress, " ", token);
        } else {
          data.strPlace = append(data.strPlace, " - ", token);
        }
      }
    }
    
    @Override
    public String getFieldNames() {
      return "ADDR PLACE APT CITY";
    }
  }
}
