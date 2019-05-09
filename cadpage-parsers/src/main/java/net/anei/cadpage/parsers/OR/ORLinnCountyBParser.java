package net.anei.cadpage.parsers.OR;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;

public class ORLinnCountyBParser extends FieldProgramParser {
  
  public ORLinnCountyBParser() {
    super(CITY_CODES, "LINN COUNTY", "OR", 
          "( CANCEL ADDR/S CITY! INFO/N+ " +
          "| CALL ADDR/Z CITY_CODE INFO/N+ " +
          "| UNIT/Z ENROUTE/R ADDR/S CITY CALL END " +
          "| INFO INFO/Z+? ( PHONE DATETIME ID | DATETIME ID | ID ) " + 
          "| FYI? CALL ( ADDR/S! | PLACE ADDR/S! | EMPTY_PLACE ADDR/S! | ADDR/S! ) APT? REF? " +
          
                // These branches have to be sorted by the number of fixed non-decision fields appear before
                // the first decision field
                // No non decision fields
                "( CITY ( MAP CODEQ? UNITQ? | CODE UNITQ? | PLACE MAP CODEQ? UNITQ? | PLACE CODE UNITQ? | PLACE UNIT | UNITQ? ) " +

                // One non-decision field
                "| PLACE_X CITY MAPQ? CODEQ? UNITQ? " + 

                // Two non-decision fields
                "| X/Z X/Z CITY MAPQ? CODEQ? UNITQ? " +
                "| X/Z X/Z MAP CODEQ? UNITQ? " + 
          
                // Three non-decision fields
                "| PLACE X/Z X/Z CITY MAPQ? CODEQ? UNITQ? " +
                "| X/Z X/Z EMPTY/Z MAP CODEQ? UNITQ? " +
                
                // Four non-decision fields
                "| PLACE X/Z X/Z EMPTY/Z MAP CODEQ? UNITQ? " +
                "| X/Z X/Z EMPTY/Z EMPTY/Z CODE UNITQ? " +
                
                // Five non-decision fields
                "| PLACE X/Z X/Z EMPTY/Z EMPTY/Z CODE UNITQ? " +
                
                // No decision fields (Cross street doesn't count)
                "| X CITY? MAPQ? CODEQ? UNITQ? " +
                "| PLACE MAP CODEQ? UNITQ? | MAP CODEQ? UNITQ? | PLACE CODE UNITQ? | CODE UNITQ? | PLACE UNIT | UNIT? " + 
                ") UNITQ/C+? ( DATETIME " + 
                            "| INFO/Z ( CH/Z EMPTY/Z INFO/Z PH/Z SKIP DATETIME " +
                                     "| CH/Z INFO/Z PH/Z SKIP DATETIME " +
                                     "| INFO/Z PH/Z SKIP DATETIME " +
//                                     "| SKIP DATETIME " + 
                                     "| INFO/Z PH DATETIME " + 
                                     "| PH SKIP? DATETIME " +
                                     "| CH SKIP? DATETIME " +
                                     "| DATETIME " +
                                     "| INFO/Z PH SKIP+ " +
                                     "| PH SKIP+ " +
                                     "| SKIP+ " + 
                                     ") " +
                            ") ID ID2? PRI+? UNIT/C+ " + 
          ")");
    setupCityValues(CITY_CODES);
    setupCities(CITY_LIST);
    removeWords("PLACE");
    addRoadSuffixTerms("LP");
  }

  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom();
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (body.contains(",Enroute,")) return parseFields(body.split(","), data);
    return parseFields(body.split(";"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CITY_CODE")) return new MyCityCodeField();
    if (name.equals("CANCEL")) return new CallField("CANCEL", true);
    if (name.equals("ENROUTE")) return new CallField("Enroute", true);
    if (name.equals("FYI")) return new SkipField("FYI:|Update:");
    if (name.equals("EMPTY_PLACE")) return new MyEmptyPlaceField();
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("APT")) return new AptField("(?:APT|RM|ROOM|LOT)[ #]*(.*)", true);
    if (name.equals("REF")) return new InfoField("\\(S\\).*", true);
    if (name.equals("PLACE_ADDR")) return new MyPlaceAddressField();
    if (name.equals("PLACE_X")) return new MyPlaceCrossField();
    if (name.equals("MAP")) return new MapField("\\d{3,4}|\\d{3}[-A-D]|ALB|CMF|HBRG|LEB|LCJ|LCSO|LYON?|MILL|SODA|SWH|TANG", true);
    if (name.equals("MAPQ")) return new MapField("\\d{3,4}|\\d{3}[-A-D]|ALB|CMF|HBRG|LEB|LCJ|LCSO|LYON?|MILL|SODA|SWH|TANG|", true);
    if (name.equals("CODE")) return new CodeField("(?i)\\d\\d?[A-Z]\\d\\d?[A-Z]?", true);
    if (name.equals("CODEQ")) return new CodeField("(?i)\\d\\d?[A-Z]\\d\\d?[A-Z]?|", true);
    if (name.equals("UNIT")) return new UnitField("(?:\\b(?:[A-Z]+\\d+[A-Z]?|\\d{3}|[A-Z]{1,3}FD|ST[A-Z]|CE|HBRG|MILC|LEB|LYON|ODF|SDIVEL?|SH1ST|SWH|TANG)\\b,?)+", true);
    if (name.equals("UNITQ")) return new UnitField("(?:\\b(?:[A-Z]+\\d+[A-Z]?|\\d{3}|[A-Z]{1,3}FD|ST[A-Z]|CE|HBRG|MILC|LEB|LYON|ODF|SDIVEL?|SH1ST|SWH|TANG)\\b,?)+|", true);
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("CH")) return new ChannelField("F\\d+|OPS", false);
    if (name.equals("PH")) return new PhoneField("(?:541|503|800|818|866|888)\\d{7}|", true);
    if (name.equals("DATETIME")) return new MyDateTimeField();
    if (name.equals("ID")) return new IdField("20\\d{8}");
    if (name.equals("ID2")) return new SkipField("\\d*", true);
    if (name.equals("PRI")) return new SkipField("\\d{0,1}");
    return super.getField(name);
  }
  
  private class MyCityCodeField extends CityField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      if (field.length() > 4) return false;
      String city = CITY_CODES.getProperty(field);
      if (city == null) return false;
      data.strCity = city;
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }
  
  private class MyEmptyPlaceField extends SkipField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      if (field.length() > 0)  return false;
      return !isCity(getRelativeField(+1));
    }
  }
  
  private class MyAddressField extends AddressField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      if (field.startsWith("LL:")) {
        super.parse(field,  data);
        return true;
      }
      Result res = parseAddress(StartType.START_ADDR, FLAG_CHECK_STATUS | FLAG_ANCHOR_END, field);
      if (res.getStatus() < STATUS_INTERSECTION) return false;
      res.getData(data);
      return true;
    }
  }
  
  private class MyPlaceAddressField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      if (data.strAddress.length() == 0 ||
          checkAddress(field) > checkAddress(data.strAddress)) {
        data.strPlace =  data.strAddress;
        data.strAddress = "";
        parseAddress(field, data);
      } else {
        super.parse(field, data);
      }
    }
  }
  
  private class MyPlaceCrossField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      if (checkAddress(field) == STATUS_STREET_NAME) {
        data.strCross = field;
      } else {
        super.parse(field, data);
      }
    }
    
    @Override
    public String getFieldNames() {
      return "PLACE X";
    }
  }
  
  private static final Pattern INFO_DELIM_PTN = Pattern.compile(" *\\[\\d\\d/\\d\\d/\\d\\d .*\\]\\s*|\n");
  private class MyInfoField extends InfoField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      if (!field.contains("\n") && !field.endsWith("]")) return false;
      parse(field, data);
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      for (String line : INFO_DELIM_PTN.split(field)) {
        line = stripFieldStart(line, "[PROQA]");
        if (line.startsWith("Radio Channel:")) {
          data.strChannel = line.substring(14).trim();
        } else {
          data.strSupp = append(data.strSupp, "\n", line);
        }
      }
    }
    
    @Override
    public String getFieldNames() {
      return "CH " + super.getFieldNames();
    }
  }
  
  private static final Pattern DATE_TIME_PTN = Pattern.compile("(?:(\\d\\d/\\d\\d/\\d{4})|(\\d{4}-\\d{2}-\\d{2}))? (\\d\\d:\\d\\d:\\d\\d)(?:\\.\\d+)?");
  private static final Pattern DIGIT_PTN = Pattern.compile("\\d");
  private static final String DATE_TIME_MASK1 = "NN/NN/NNNN NN:NN:NN";
  private static final String DATE_TIME_MASK2 = "NNNN-NN-NN NN:NN:NN.NNN";
  private class MyDateTimeField extends DateTimeField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      field = field.replace(" /", "/").replace("/ ", "/");
      Matcher match = DATE_TIME_PTN.matcher(field);
      if (!match.matches()) return false;
      String date = match.group(1);
      if (date == null) {
        date = match.group(2);
        date = date.substring(5,7)+'/'+date.substring(8,10) + '/' + date.substring(1,4);
      }
      data.strDate = date;
      data.strTime = match.group(3);
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (checkParse(field, data)) return;
      
      if (!isLastField()) {
        field = DIGIT_PTN.matcher(field).replaceAll("N");
        if (!DATE_TIME_MASK1.startsWith(field) &&
            !DATE_TIME_MASK2.startsWith(field)) abort();
      }
    }
  }
  
  @Override
  public String adjustMapCity(String city) {
    if (city.equalsIgnoreCase("FOSTER")) city = "SWEET HOME";
    return city;
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "ALB",  "ALBANY",
      "BROW", "BROWNSVILLE",
      "CASC", "CASCADIA",
      "COR",  "CORVALLIS",
      "FSTR", "FOSTER",
      "GATE", "GATES",
      "HALS", "HALSEY",
      "HBRG", "HARRISBURG",
      "IDAN", "IDAHNA",
      "IND",  "INDEPENDENCE",
      "JEFF", "JEFFERSON",
      "LEB",  "LEBANON",
      "LYON", "LYONS",
      "LYONS","LYONS",
      "MBG",  "MILLERSBURG",
      "MILL", "MILL CITY",
      "SCIO", "SCIO",
      "SHED", "SHEDD",
      "SILV", "SILVERTON",
      "SIS",  "SISTERS",
      "SOD",  "SODAVILLE",
      "SPF",  "SPRINGFIELD",
      "STY",  "STAYTON",
      "SWH",  "SWEET HOME",
      "TANG", "TANGENT"
  });
  
  private static final String[] CITY_LIST = new String[]{
      "MCKENZIE",
      "JUNCTION CITY"
  };
}
