package net.anei.cadpage.parsers.IN;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class INLakeCountyParser extends FieldProgramParser {

  public INLakeCountyParser() {
    super(CITY_CODES, "LAKE COUNTY", "IN",
      "SRC MAP CALL STATUS? ADDR UNIT! INFO/N+? ( ID Between:X DATETIME | Between:X DATETIME | DATETIME ) END");
  }
  
  public String getFilter() {
      return "hiplink@lakecountysheriff.com";
    }
  
  protected boolean parseMsg(String subject, String body, Data data) {
    body = body.replace("\nIntersection of:", "\nBetween:");
    return parseFields(body.split("\n"), 2, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("SRC")) return new SourceField("[A-Z]{4}", true);
    if (name.equals("MAP")) return new MapField("[-A-Z]*\\d*[A-Z]?", true);
    if (name.equals("STATUS")) return new SkipField("DISP|PAGED|ENRT|ARRVD");
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("UNIT")) return new MyUnitField();
    if (name.equals("ID")) return new IdField("\\d{2}[A-Z]{1,3}\\d+", true);
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("DATETIME")) return new MyDateTimeField();
    return super.getField(name);
  }
  
  private static final Pattern ADDR_APT_PTN = 
      Pattern.compile("(?:#|APT|RM|ROOM|SUITE|STE|LOT)(?!S) *(.*)|\\d{1,4}[A-Z]?|[A-Z]\\d{0,3}|.* FLOOR", Pattern.CASE_INSENSITIVE);
  
  private class MyAddressField extends AddressField {
    
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      data.strCity = convertCodes(p.getLastOptional(','), CITY_CODES);
      for (String part : p.get().split(";")) {
        part = part.trim();
        if (data.strAddress.length() == 0) {
          parseAddress(part, data);
          continue;
        }
        Matcher match = ADDR_APT_PTN.matcher(part);
        if (match.matches()) {
          String apt = match.group(1);
          if (apt == null) apt = part;
          data.strApt = append(data.strApt, "-", apt);
        } else {
          data.strPlace = append(data.strPlace, " - ", part);
        }
      }
    }
    
    @Override
    public String getFieldNames() {
      return "ADDR APT PLACE CITY";
    }
  }
  
 private class MyUnitField extends UnitField {
   
   @Override
   public void parse(String field, Data data) {
     if (field.equals("PAGED")) return;
     data.strUnit = append(data.strUnit, " ", field);
   }
 }
 
 private class MyCrossField extends CrossField {
   @Override
   public void parse(String field, Data data) {
     field = field.replace("<not found>", "").trim();
     field = stripFieldStart(field, "&");
     field = stripFieldEnd(field, "&");
     super.parse(field, data);
   }
 }
 
 private static final Pattern DATE_TIME_PTN = Pattern.compile("\\d\\d/\\d\\d/\\d{4} \\d\\d:\\d\\d");
 private class MyDateTimeField extends DateTimeField {
   
   public MyDateTimeField() {
     setPattern(DATE_TIME_PTN, false);
   }
   
   @Override
   public void parse(String field, Data data) {
     if (!DATE_TIME_PTN.matcher(field).matches()) return;
     super.parse(field, data);
   }
 }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "CED",  "CEDAR LAKE",
      "CRO",  "CROWN POINT",
      "DYE",  "DYER",
      "GAR",  "GARY",
      "GRI",  "GRIFFITH",
      "HAM",  "HAMMOND",
      "HEB",  "HEBRON",
      "HOB",  "HOBART",
      "HIG",  "HIGHLAND",
      "LKS",  "LAKE STATION",
      "LOW",  "LOWELL",
      "MER",  "MERRILLVILLE",
      "MUN",  "MUNSTER",
      "REN",  "ROSELAWN",
      "SCN",  "SCHNEIDER",
      "SCH",  "SCHERERVILLE",
      "SHL",  "SHELBY",
      "STJ",  "ST JOHN",
      "WHI",  "WHITING"
    });
}
