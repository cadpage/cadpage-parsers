package net.anei.cadpage.parsers.CO;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class COEagleCountyParser extends FieldProgramParser {
  public COEagleCountyParser() {
    super(CITY_TABLE, "EAGLE COUNTY", "CO",
          "ADDR1/Z+? ADDR2/ZS! EVENT:CALL! TIME:TIME! Disp:UNIT!");
  }
  
  @Override
  public String getFilter() {
    return "ipage@vailgov.com";
  }

  private static final Pattern PAMA_PATTERN
    = Pattern.compile("(?:U\\.? *S\\.? +)?HIGHWAY +& +(\\d+)");
  @Override
  public String postAdjustMapAddress(String a) {
    Matcher m=PAMA_PATTERN.matcher(a);
    if (m.matches()) return "US-"+m.group(1);
    return a;
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("IPS I/Page Notification")) return false;
    if (!body.startsWith("Loc:")) return false;
    body = body.substring(4).trim();
    
    return parseFields(body.split("/"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR1")) return new Address1Field();
    if (name.equals("ADDR2")) return new Address2Field();
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d:\\d\\d", true);
    return super.getField(name);
  }
  
  private class Address1Field extends AddressField {
    @Override
    public void parse(String field, Data data) {
      data.strAddress = append(data.strAddress, "/", field);
    }
  }
  
  private static final Pattern SPEC_PATTERN
    = Pattern.compile("(?:alias |@)(.*)");
  private class Address2Field extends AddressField {
    @Override
    public void parse(String field, Data data) {
      field = append(data.strAddress, "/", field);
      data.strAddress = "";
      String place;
      Parser p = new Parser(field);
      while ((place = p.getLastOptional(": ")).length() > 0) {
        parseSpec(place, data);
      }
      String apt = "";
      if (!field.startsWith("LL(")) {
        apt = p.getLastOptional(';');
        apt = append(p.getLastOptional(','), "-", apt);
      }
      field = p.get().replace(" CNTY", " ").trim();
      super.parse(field, data);
      data.strApt = append(data.strApt, "-", apt);
    }
    
    private void parseSpec(String field, Data data) {
      Matcher m=SPEC_PATTERN.matcher(field);
      if (m.matches())
        field = m.group(1).trim();
      data.strPlace = append(field, " - ", data.strPlace);
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames()+" PLACE";
    }
  }

  private static final Pattern CALL_PATTERN
    = Pattern.compile("(.*)- +(\\d{1,2}[A-Z]\\d{1,2}[A-Z]?)");
  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      Matcher m=CALL_PATTERN.matcher(field);
      if (m.matches()) {
        data.strCall=m.group(1).trim();
        data.strCode=m.group(2);
      }
      else
        data.strCall=field;
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames()+" CODE";
    }
  }

  @Override
  public String adjustMapCity(String city) {
    return convertCodes(city, MAP_CITY_TABLE); 
  }
  
  private static final Properties MAP_CITY_TABLE = buildCodeTable(new String[]{
      "ARROWHEAD",    "EDWARDS",
      "CORD",         "STEAMBOAT SPRINGS",
      "LAKE CREEK",   "EDWARDS",
  });
  
  private static final Properties CITY_TABLE = buildCodeTable(new String[]{
      
//      "","GRAND COUNTY",
//      "","SUMMIT COUNTY",
//      "","LAKE COUNTY",
//      "","PITKIN COUNTY",
//      "","GARFIELD COUNTY",
//      "","ROUTT COUNTY",
      "ARW",  "ARROWHEAD",
      "AVON", "AVON",
//      "", "BASALT",
      "BATG", "BACHELOR GULCH",
      "BCK",  "BEAVER CREEK",
      "BON",  "BOND",
      "BUR",  "BURNS",
      "CORD", "CORD",   // ???
      "CORR", "EDWARDS",
      "DOT",  "",
      "EAGL", "EAGLE",
      "EGV",  "EAGLE-VAIL",
      "EDWD", "EDWARDS",
//      "", "EL JEBEL",
      "GYPS", "GYPSUM",
      "LKCK", "LAKE CREEK",
      "MINT", "MINTURN",
      "REDC", "RED CLIFF",
      "VAIL", "VAIL",
      "WOLC", "WOLCOTT",
//      "", "GILMAN",

  });
}
