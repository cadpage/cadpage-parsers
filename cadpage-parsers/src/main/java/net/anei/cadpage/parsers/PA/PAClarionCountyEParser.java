package net.anei.cadpage.parsers.PA;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class PAClarionCountyEParser extends FieldProgramParser {
  
  public PAClarionCountyEParser() {
    super(CITY_LIST, "CLARION COUNTY", "PA", 
          "( LAT:ADDR1 LON:ADDR2 | ADDR/S6 ) CITY? CALL! PLACE/N+? Xstreets:X! X/CS+? NAME? INFO/N+");
    addRoadSuffixTerms("EXT");
  }
  
  @Override
  public String getFilter() {
    return "@oes.clarion.pa.us";
  }
  
  private static final Pattern TRAIL_JUNK_PTN = Pattern.compile("[ \n]+(?:ProQA|Questionnaire:) ");
  private static final Pattern MASTER = Pattern.compile("(.*?) - (.*?), +(.*?)(?:\n([^,].*))?[ \n]+Xstreets: *");
  private static final Pattern MBREAK_PTN = Pattern.compile(" *\n[\n ]*");
  private static final Pattern TRAIL_NAME_PTN = Pattern.compile("(.*) ([A-Z]{5,})", Pattern.CASE_INSENSITIVE);
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("ALERT MESSAGE")) return false;
    if (!body.startsWith("!:")) return false;
    body = body.substring(2).trim();
    
    Matcher match = TRAIL_JUNK_PTN.matcher(body);
    if (match.find()) body = body.substring(0, match.start());
    
    body = body.replace("XStreets:", "Xstreets:");
    
    match = MASTER.matcher(body);
    if (match.lookingAt()) {
      setFieldList("CALL ADDR APT CITY PLACE X NAME INFO");
      data.strCall = match.group(1).trim();
      parseAddress(StartType.START_ADDR, FLAG_RECHECK_APT | FLAG_ANCHOR_END, cleanCity(match.group(2).trim().replace('@',  '/')), data);
      data.strApt = stripFieldStart(data.strApt, "-");
      parseCity(match.group(3).trim(), data);
      data.strPlace = append(data.strPlace, " - ", getOptGroup(match.group(4)));
      
      String left = body.substring(match.end());
      int pt = left.indexOf('\n');
      if (pt >= 0) {
        String cross = left.substring(0,pt).trim();
        if (!cross.equals("Searching Cross Streets...")) data.strCross = cross;
        data.strSupp = MBREAK_PTN.matcher(left.substring(pt).trim()).replaceAll("\n");
      } else {
        match = TRAIL_NAME_PTN.matcher(left);
        if (match.matches()) {
          left = match.group(1).trim();
          data.strName = match.group(2);
        }
        data.strCross = left;
      }
      return true;
    }
    return parseFields(splitFields(body), data);
  }
  
  private static final Pattern DELIM = Pattern.compile(",[ \n]");
  
  private String[] splitFields(String body) {
    List<String> fields = new ArrayList<String>();
    boolean xstreets = false;
    for (String fld : DELIM.split(body)) {
      fld = fld.trim();
      if (fld.startsWith("XStreets:")) xstreets = true;
      if (!xstreets) { 
        for (String fld2 : fld.split(",")) {
          fields.add(fld2.trim());
        }
      } else {
        fields.add(fld);
      }
    }
    return fields.toArray(new String[fields.size()]);
  }

  private static final Pattern CITY_BREAK_PTN = Pattern.compile("(.* (?:BORO|TWP)) (.*)", Pattern.CASE_INSENSITIVE);
  private static final Pattern CITY_BORO_PTN1 = Pattern.compile("Boro\\b *(.*?)", Pattern.CASE_INSENSITIVE);
  private static final Pattern CITY_BORO_PTN2 = Pattern.compile("(.*?) +Boro", Pattern.CASE_INSENSITIVE);

  private void parseCity(String city, Data data) {
    
    Matcher  match = CITY_BREAK_PTN.matcher(city);
    if (match.matches()) {
      data.strCity = cleanCity(match.group(1));
      data.strPlace = match.group(2).trim();
    }
    
    else {
      parseAddress(StartType.START_ADDR, FLAG_ONLY_CITY, city, data);
      if (data.strCity.length() > 0) {
        String left = getLeft();
        match = CITY_BORO_PTN1.matcher(left);
        if (match.matches()) left = match.group(1);
        data.strPlace = left;
      }
      
      else {
        data.strCity = cleanCity(city);
      }
    }
    
    fixCity(data);
  }
  
  private String cleanCity(String city) {
    Matcher match = CITY_BORO_PTN2.matcher(city);
    if (match.matches()) city = match.group(1);
    return city;
  }
  
  private void fixCity(Data data) {
    String city = MISSPELLED_CITY_TABLE.getProperty(data.strCity.toUpperCase());
    if (city != null) data.strCity = city;
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("ADDR1")) return new MyAddress1Field();
    if (name.equals("ADDR2")) return new MyAddress2Field();
    if (name.equals("CITY")) return new MyCityField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("NAME")) return new MyNameField();
    return super.getField(name);
  }
  
  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(',');
      if (pt >= 0) {
        data.strPlace = field.substring(pt+1).trim();
        field = field.substring(0,pt).trim();
      }
      super.parse(field, data);
    }
  }
  
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace('@', '&');
      field = cleanCity(field);
      super.parse(field,  data);
      data.strApt = stripFieldStart(data.strApt, "-");
    }
  }
  
  private class MyAddress1Field extends AddressField {
    @Override
    public void parse(String field, Data data) {
      data.strAddress = "LAT:" + field;
    }
  }
  
  private class MyAddress2Field extends AddressField {
    @Override
    public void parse(String field, Data data) {
      data.strAddress = data.strAddress + ", LON:" + field;
    }
  }
  
  private class MyCityField extends CityField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      if (data.strCity.length() > 0) return false;
      field = cleanCity(field);
      if (field.length() == 0) return true;
      if (!super.checkParse(field, data)) return false;
      fixCity(data);
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      field = cleanCity(field);
      super.parse(field, data);
      fixCity(data);
    }
  }
  
  private class MyCrossField extends CrossField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      if (!isValidAddress(field)) return false;
      parse(field, data);
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (field.equals("Searching Cross Streets...")) return;
      data.strCross = append(data.strCross, ", ", field);
    }
  }
  
  private Pattern NAME_JUNK_PTN = Pattern.compile("[ ,]+$");
  private class MyNameField extends NameField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      if (!field.contains(",")) return false;
      parse(field, data);
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      Matcher match = NAME_JUNK_PTN.matcher(field);
      if (match.find()) field = field.substring(0, match.start());
      super.parse(field, data);
    }
  }
  
  private static final Properties MISSPELLED_CITY_TABLE = buildCodeTable(new String[]{
      "EAST BRANDY",    "East Brady",
      "NEW BETHELEM",   "New Bethlehem"
  }); 
  
  private static final String[] CITY_LIST = new String[]{

      // Boroughs
      "CALLENSBURG",
      "CLARION",
      "EAST BRADY",
      "EAST BRANDY",   // Misspelled
      "EMLENTON",
      "FOXBURG",
      "HAWTHORN",
      "KNOX",
      "NEW BETHLEHEM",
      "NEW BETHELEM",  // Misspelled
      "RIMERSBURG",
      "SHIPPENVILLE",
      "SLIGO",
      "SAINT PETERSBURG",
      "ST PETERSBURG",
      "STRATTANVILLE",

      // Townships
      "ASHLAND TWP",
      "BEAVER TWP",
      "BRADY TWP",
      "CLARION TWP",
      "ELK TWP",
      "FARMINGTON TWP",
      "HIGHLAND TWP",
      "KNOX TWP",
      "LICKING TWP",
      "LIMESTONE TWP",
      "MADISON TWP",
      "MILLCREEK TWP",
      "MONROE TWP",
      "PAINT TWP",
      "PERRY TWP",
      "PINEY TWP",
      "PORTER TWP",
      "REDBANK TWP",
      "RICHLAND TWP",
      "SALEM TWP",
      "TOBY TWP",
      "WASHINGTON TWP",

      // Census-designated places
      "CROWN",
      "LEEPER",
      "MARIANNE",
      "TYLERSBURG",
      "VOWINCKEL",
      
      // Armstrong County
      "HOVEY TWP",
      "PERRY TWP",
      "BRADYS BEND TWP",
      "SUGARCREEK TWP",
      "WASHINGTON TWP",
      "MADISON TWP",
      "MAHONING TWP",
      "REDBANK TWP",
      "PARKER",
      "PARKER CITY",
      "SOUTH BEHTLEHEM",
      
      // Butler County
      "ALLEGHENY TWP",
      "VENANGO TWP",
      "WASHINGTON TWP",
      "PARKER TWP",
      "BRUIN",
      "CHERRY VALLEY",
      "EAU CLAIRE",
      
      // Forest County
      "BARNETT TWP",
      "HARMONY TWP",
      "HICKORY TWP",
      "HOWE TWP",
      "KINGSLEY TWP",
      "TONESTA TWP",
      "TONESTA",
      "GREEN TWP",
      "JENKS TWP",
      
      // Jefferson County
      "BARNETT TWP",
      "ELDRED TWP",
      "UNION TWP",
      "CLOVER TWP",
      "BEAVER TWP",
      "RINGGOLD TWP",
      "CORSICA",
      "SUMMERVILLE",
      
      // Venango County
      "SCRUBGRASS TWP",
      "RICHLAND TWP",
      "ROCKLAND TWP",
      "CRANBERRY TWP",
      "PINEGROVE TWP",
      "EMLENTON"

  };
}
