package net.anei.cadpage.parsers.CO;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class COPitkinCountyParser extends FieldProgramParser {
  
  public COPitkinCountyParser() {
    super(CITY_CODES, "PITKIN COUNTY", "CO",
           "SRC ( UNIT ID TIME! TIME+ | CALL ADDR SRC UNIT! INFO+ )");
  }
  
  @Override
  public String getFilter() {
    return "textpage@pitpage.org";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    return parseFields(body.split("\n"), data);
  }
  
  
  private static final Pattern APT_PTN = Pattern.compile("(?:APT|ROOM|RM|SUITE|STE|LOT|UNIT) *(.*)|\\d+", Pattern.CASE_INSENSITIVE);
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      data.strCity = convertCodes(p.getLastOptional(','), CITY_CODES);
      if (data.strCity.length() == 0) abort();
      String p2 = p.getLastOptional(';');
      String p1 = p.getLastOptional(';');
      if (p1.equals(data.strSource)) p1 = "";
      if (p1.length() > 0) {
        data.strPlace = p1;
        Matcher match = APT_PTN.matcher(p2);
        if (match.matches()) {
          String p3 = match.group(1);
          if (p3 != null) p2 = p3;
        }
        data.strApt = append(data.strApt, "-", p2);
      } else if (p2.length() > 0) {
        Matcher match = APT_PTN.matcher(p2);
        if (match.matches()) {
          String p3 = match.group(1);
          if (p3 != null) p2 = p3;
          data.strApt = append(data.strApt, "-", p2);
        } else {
          data.strPlace = p2;
        }
      }
      parseAddress(p.get(), data);
    }
    
    @Override
    public String getFieldNames() {
      return "ADDR PLACE APT CITY";
    }
    
  }
  
  private static final Pattern SRC_PTN = Pattern.compile("[A-Z]{2,3}");
  private class MySourceField extends SourceField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals(data.strSource)) return;
      if (!SRC_PTN.matcher(field).matches()) abort();
      data.strSource = append(data.strSource, "-", field);
    }
  }
  
  private class MyTimeField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      data.strCall = "RUN REPORT";
      data.strSupp = append(data.strSupp, "\n", field);
    }
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("SRC")) return new MySourceField();
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("ID")) return new IdField("\\d\\d[A-Z]{2}\\d{5}|\\d{4}", true);
    if (name.equals("TIME")) return new MyTimeField();
    return super.getField(name);
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "ASP",  "ASPEN",
      "BAS",  "BASALT",
      "CAR",  "CARBONDALE",
      "EL",   "EL JEBEL",
      "GWS",  "GLENWOOD SPRINGS",
      "MER",  "MEREDITH",
      "PT",   "PITKIN COUNTY",
      "RD",   "REDSTONE",
      "SNO",  "SNOWMASS",
      "SMV",  "SNOWMASS VILLAGE",
      "TVL",  "THOMASVILLE",
      "WDY",  "WOODY CREEK",

  });
}
