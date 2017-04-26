package net.anei.cadpage.parsers.OR;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class ORLinnCountyBParser extends FieldProgramParser {
  
  public ORLinnCountyBParser() {
    super(CITY_CODES, "LINN COUNTY", "OR", 
          "( CANCEL ADDR CITY! INFO/N+ " +
          "| FYI? CALL ADDR! APT? REF? ( PLACE_ADDR X/Z X/Z CITY MAPQ? CODEQ? UNITQ? " +
                                      "| PLACE_ADDR X/Z X/Z EMPTY/Z MAP CODEQ? UNITQ? " +
                                      "| PLACE_ADDR X/Z X/Z EMPTY/Z EMPTY/Z CODE UNITQ? " +
                                      "| CITY ( PLACE MAP CODEQ? UNITQ? | MAP CODEQ? UNITQ? | PLACE CODE UNITQ? | CODE UNITQ? | PLACE UNIT | UNITQ? ) " + 
                                      "| X/Z X/Z CITY MAPQ? CODEQ? UNITQ? " + 
                                      "| PLACE_X CITY MAPQ? CODEQ? UNITQ? " + 
                                      "| X/Z X/Z MAP CODEQ? UNITQ? " + 
                                      "| X CITY? MAPQ? CODEQ? UNITQ? " +
                                      "| PLACE MAP CODEQ? UNITQ? | MAP CODEQ? UNITQ? | PLACE CODE UNITQ? | CODE UNITQ? | PLACE UNIT | UNIT? " + 
                                      ") UNITQ/C+? ( DATETIME " + 
                                                  "| INFO ( CH/Z EMPTY/Z NAME PH/Z SKIP DATETIME " +
                                                         "| CH/Z NAME PH/Z SKIP DATETIME " +
                                                         "| NAME PH/Z SKIP DATETIME " +
                                                         "| SKIP DATETIME " + 
                                                         "| NAME PH DATETIME " + 
                                                         "| PH SKIP? DATETIME " +
                                                         "| CH SKIP? DATETIME " +
                                                         "| DATETIME " +
                                                         "| NAME PH SKIP+ " +
                                                         "| PH SKIP+ " +
                                                         "| SKIP+ " + 
                                                         ") " +
                                                  ") ID ID2? PRI+? UNIT/C+ " + 
          ")");
    setupCityValues(CITY_CODES);
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split(";"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CANCEL")) return new CallField("CANCEL", true);
    if (name.equals("FYI")) return new SkipField("FYI:|Update:");
    if (name.equals("APT")) return new AptField("(?:APT|RM|ROOM|LOT)[ #]*(.*)", true);
    if (name.equals("REF")) return new InfoField("\\(S\\).*", true);
    if (name.equals("PLACE_ADDR")) return new MyPlaceAddressField();
    if (name.equals("PLACE_X")) return new MyPlaceCrossField();
    if (name.equals("MAP")) return new MapField("\\d{4}", true);
    if (name.equals("MAPQ")) return new MapField("\\d{4}|", true);
    if (name.equals("CODE")) return new CodeField("(?i)\\d\\d?[A-Z]\\d\\d?[A-Z]?", true);
    if (name.equals("CODEQ")) return new CodeField("(?i)\\d\\d?[A-Z]\\d\\d?[A-Z]?|", true);
    if (name.equals("UNIT")) return new UnitField("(?:\\b(?:[A-Z]+\\d+[A-Z]?|\\d{3}|[A-Z]{1,3}FD|ST[A-Z])\\b,?)+", true);
    if (name.equals("UNITQ")) return new UnitField("(?:\\b(?:[A-Z]+\\d+[A-Z]?|\\d{3}|[A-Z]{1,3}FD|ST[A-Z])\\b,?)+|", true);
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("CH")) return new ChannelField("F\\d+", false);
    if (name.equals("PH")) return new PhoneField("(?:541|503|800|888)\\d{7}|", true);
    if (name.equals("DATETIME")) return new MyDateTimeField();
    if (name.equals("ID")) return new IdField("20\\d{8}");
    if (name.equals("ID2")) return new SkipField("\\d*", true);
    if (name.equals("PRI")) return new SkipField("\\d{0,1}");
    return super.getField(name);
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
  
  private static final Pattern INFO_JUNK_PTN = Pattern.compile(" *\\[\\d\\d/\\d\\d/\\d\\d .*\\](?=\n|$)");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = INFO_JUNK_PTN.matcher(field).replaceAll("");
      super.parse(field, data);
    }
  }
  
  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d\\d/\\d\\d/\\d{4}) (\\d\\d:\\d\\d:\\d\\d)");
  private static final Pattern DIGIT_PTN = Pattern.compile("\\d");
  private static final String DATE_TIME_MASK = "NN/NN/NNNN NN:NN:NN";
  private class MyDateTimeField extends DateTimeField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = DATE_TIME_PTN.matcher(field);
      if (!match.matches()) return false;
      data.strDate = match.group(1);
      data.strTime = match.group(2);
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (checkParse(field, data)) return;
      
      if (!isLastField()) {
        field = DIGIT_PTN.matcher(field).replaceAll("N");
        if (!DATE_TIME_MASK.startsWith(field)) abort();
      }
    }
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
}
