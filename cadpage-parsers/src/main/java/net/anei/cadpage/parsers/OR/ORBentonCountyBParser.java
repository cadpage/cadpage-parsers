package net.anei.cadpage.parsers.OR;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;

public class ORBentonCountyBParser extends ORBentonCountyBaseParser {
  
  public ORBentonCountyBParser() {
    this("BENTON COUNTY");
  }
  
  public ORBentonCountyBParser(String defCity) {
    super(defCity, 
          "( CANCEL ADDR/S CITY! INFO/N+ " +
          "| CALL ADDR/Z CITY_CODE INFO/N+ " +
          "| UNIT/Z ENROUTE/R ADDR/S CITY CALL END " +
          "| INFO INFO/Z+? ( PHONE DATETIME ID! | DATETIME ID! | ID! ) " + 
          "| SELECT/FIXED CALL PLACE ADDR/SZ X/Z X/Z CITY MAPQ CODEQ UNITQ UNITQ/C INFO PLACE PHONE DATETIME ID ID2! END " +
          
          // The rest of this handles obsolete ugly pages where almost all of the fields were optional
          "| FYI? CALL ( ADDR/S! | PLACE ADDR/S! | EMPTY_PLACE ADDR/S! | ADDR/S! ) APT? REF? " +
          
                // These branches have to be sorted by the number of fixed non-decision fields appear before
                // the first decision field
                // No non decision fields
                "( CITY ( MAPX CODEQ? UNITQ? | CODEX UNITQ? | PLACE MAPX CODEQ? UNITQ? | PLACE CODEX UNITQ? | PLACE UNITX | UNITQ? ) " +

                // One non-decision field
                "| PLACE_X CITY MAPQ? CODEQ? UNITQ? " + 

                // Two non-decision fields
                "| X/Z X/Z CITY MAPQ? CODEQ? UNITQ? " +
                "| X/Z X/Z MAPX CODEQ? UNITQ? " + 
          
                // Three non-decision fields
                "| PLACE X/Z X/Z CITY MAPQ? CODEQ? UNITQ? " +
                "| X/Z X/Z EMPTY/Z MAPX CODEQ? UNITQ? " +
                
                // Four non-decision fields
                "| PLACE X/Z X/Z EMPTY/Z MAPX CODEQ? UNITQ? " +
                "| X/Z X/Z EMPTY/Z EMPTY/Z CODEX UNITQ? " +
                
                // Five non-decision fields
                "| PLACE X/Z X/Z EMPTY/Z EMPTY/Z CODEX UNITQ? " +
                
                // No decision fields (Cross street doesn't count)
                "| X CITY? MAPQ? CODEQ? UNITQ? " +
                "| PLACE MAPX CODEQ? UNITQ? | MAPX CODEQ? UNITQ? | PLACE CODEX UNITQ? | CODEX UNITQ? | PLACE UNITX | UNITX? " + 
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
                            ") ID ID2? PRI+? UNITX/C+ " + 
          ")");
  }

  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom();
  }
  
  @Override
  public String getAliasCode() {
    return "ORBentonCountyB";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (body.contains(",Enroute,")) return parseFields(body.split(","), data);
    
    // This used to be a mess of hard to recognize optional fields.  Now that it
    // has been cleaned up and is a nice fixed format, we have to figure out whether
    // the new fixed position logic can be used or not.
    String[] flds = body.split(";", -1);
    setSelectValue(flds.length == 16 && isCity(flds[5]) ? "FIXED" : "");
    if (!parseFields(body.split(";", -1), data)) return false;
    fixAddress(data);
    return true;
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CITY_CODE")) return new MyCityCodeField();
    if (name.equals("CANCEL")) return new CallField("CANCEL", true);
    if (name.equals("ENROUTE")) return new CallField("Enroute", true);
    if (name.equals("FYI")) return new SkipField("FYI:|Update:");
    if (name.equals("EMPTY_PLACE")) return new MyEmptyPlaceField();
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("APT")) return new AptField("(?:APT|RM|ROOM|LOT)[ #]*(.*)", false);
    if (name.equals("REF")) return new InfoField("\\(S\\).*", false);
    if (name.equals("PLACE")) return new MyPlaceField();
    if (name.equals("PLACE_X")) return new MyPlaceCrossField();
    if (name.equals("MAPX")) return new MapField("\\d{3,4}|\\d{3}[-A-D]|ALB|CMF|HALS|HBRG|LEB|LCJ|LCSO|LYON?|MILB|MILL|SODA|SWH|TANG", true);
    if (name.equals("MAPQ")) return new MapField("\\d{3,4}|\\d{3}[-A-D]|ALB|CMF|HALS|HBRG|LEB|LCJ|LCSO|LYON?|MILB|MILL|SODA|SWH|TANG|", true);
    if (name.equals("CODEX")) return new CodeField("(?i)\\d\\d?[A-Z]\\d\\d?[A-Z]?", true);
    if (name.equals("CODEQ")) return new CodeField("(?i)\\d\\d?[A-Z]\\d\\d?[A-Z]?|", true);
    if (name.equals("UNITX")) return new UnitField("[A-Z]+\\d+[A-Z]{0,2}|\\d{3}|[A-Z]{1,3}FD|ST[A-Z]|ADAIR|ADF|ALS|ALSEA|BLF|CE|DET|JEF|HALS|HBRG|KVF|LEB|LYON|MFD|MILB|MILC|MONROE|ODFS?|PHILO|NE|NW|SDIVEL?|SH1ST|SWH|TANG|[A-Z0-9]+,[A-Z0-9,]+", true);
    if (name.equals("UNITQ")) return new UnitField("[A-Z]+\\d+[A-Z]{0,2}|\\d{3}|[A-Z]{1,3}FD|ST[A-Z]|ADAIR|ADF|ALS|ALSEA|BLF|CE|DET|JEF|HALS|HBRG|KVF|LEB|LYON|MFD|MILB|MILC|MONROE|ODFS?|PHILO|NE|NW|SDIVEL?|SH1ST|SWH|TANG|[A-Z0-9]+,[A-Z0-9,]+|", true);
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
      String city = cvtCityCode(field);
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
  
  private class MyPlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      String addr = getRelativeField(+1);
      if (addr.startsWith(field)) return;
      super.parse(field, data);
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
}
