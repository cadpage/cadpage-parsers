package net.anei.cadpage.parsers.OR;

import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class ORLinnCountyBParser extends FieldProgramParser {
  
  public ORLinnCountyBParser() {
    super(CITY_CODES, "LINN COUNTY", "OR", 
          "( CANCEL ADDR CITY! INFO/N+ " +
          "| FYI? CALL ADDR! REF? ( PLACE X/Z X/Z CITY MAPQ? CODEQ? UNITQ? " + 
                                 "| X/Z X/Z CITY MAPQ? CODEQ? UNITQ? " + 
                                 "| X CITY? MAPQ? CODEQ? UNITQ? " +
                                 "| PLACE CITY MAPQ? CODEQ? UNITQ? " + 
                                 "| ( CITY ( PLACE MAP CODEQ? UNITQ? | MAP CODEQ? UNITQ? | PLACE CODE UNITQ? | CODE UNITQ? | PLACE UNIT | UNITQ? ) " + 
                                   "| PLACE MAP CODEQ? UNITQ? | MAP CODEQ? UNITQ? | PLACE CODE UNITQ? | CODE UNITQ? | PLACE UNIT | UNITQ? " + 
                                   ") " +
                                 ") UNITQ/C+? ( DATETIME " + 
                                           "| INFO ( CH/Z EMPTY NAME PH/Z SKIP DATETIME " +
                                                  "| CH/Z NAME PH/Z SKIP DATETIME " +
                                                  "| NAME PH/Z SKIP DATETIME " +
                                                  "| SKIP DATETIME " + 
                                                  "| NAME PH DATETIME " + 
                                                  "| PH SKIP? DATETIME " +
                                                  "| CH SKIP? DATETIME " +
                                                  "| DATETIME " +
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
    if (name.equals("REF")) return new InfoField("\\(S\\).*", true);
    if (name.equals("MAP")) return new MapField("\\d{4}", true);
    if (name.equals("MAPQ")) return new MapField("\\d{4}|", true);
    if (name.equals("CODE")) return new CodeField("\\d\\d?[A-Z]\\d\\d?[A-Z]?", true);
    if (name.equals("CODEQ")) return new CodeField("\\d\\d?[A-Z]\\d\\d?[A-Z]?|", true);
    if (name.equals("UNIT")) return new UnitField("(?:\\b(?:[A-Z]+\\d+[A-Z]?|\\d{3}|[A-Z]{1,3}FD)\\b,?)+", true);
    if (name.equals("UNITQ")) return new UnitField("(?:\\b(?:[A-Z]+\\d+[A-Z]?|\\d{3}|[A-Z]{1,3}FD)\\b,?)+|", true);
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("CH")) return new ChannelField("F\\d+", false);
    if (name.equals("PH")) return new PhoneField("(?:541|503|800|888)\\d{7}|", true);
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d{4} \\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("ID")) return new IdField("20\\d{8}");
    if (name.equals("ID2")) return new SkipField("\\d*", true);
    if (name.equals("PRI")) return new SkipField("\\d{0,1}");
    return super.getField(name);
  }
  
  private static final Pattern INFO_JUNK_PTN = Pattern.compile(" *\\[\\d\\d/\\d\\d/\\d\\d .*\\](?=\n|$)");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = INFO_JUNK_PTN.matcher(field).replaceAll("");
      super.parse(field, data);
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
