package net.anei.cadpage.parsers.CO;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class COArchuletaCountyParser extends FieldProgramParser {
  
  public COArchuletaCountyParser() {
    super("ARCHULETA COUNTY", "CO",
          "GPS1 GPS2 CALL ADDR ID INFO! INFO+");
  }
  
  @Override
  public String getFilter() {
    return "directorac911@gmail.com,ArchuletaCountyCAD@outlook.com";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split("\\^", -1), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("ID")) return new IdField("#([A-Z]{0,3}\\d\\d-\\d{5,6})", true);
    return super.getField(name);
  }
  
  private static final Pattern ADDR_ST_PTN = Pattern.compile("[A-Z]{2}");
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      String city = p.getLastOptional(',');
      if (ADDR_ST_PTN.matcher(city).matches()) {
        data.strState = city;
        city = p.getLastOptional(',');
      }
      city = city.toUpperCase();
      if (COUNTY_NAME_SET.contains(city)) city += " COUNTY";
      data.strCity = city;
      super.parse(p.get(), data);
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " CITY ST";
    }
  }
  
  private static final Set<String> COUNTY_NAME_SET = new HashSet<String>(Arrays.asList(
      "ARCHULETA",
      "CONEJOS",
      "HINSDALE",
      "LA PLATA",
      "MINERAL",
      "RIO ARRIBA",
      "RIO GRANDE",
      "SAN JUAN"
  )); 
}
