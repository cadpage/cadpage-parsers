package net.anei.cadpage.parsers.VA;

import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

/**
 * Hanover County, VA (B)
 */
public class VAHanoverCountyBParser extends DispatchOSSIParser {
    
  
  public VAHanoverCountyBParser() {
    super(CITY_CODES, "HANOVER COUNTY", "VA",
          "( CANCEL ADDR CITY | ADDR CALL PRI! ( CITY | X/Z CITY | X/Z X/Z CITY ) MAP ID UNIT ) INFO+");
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("PRI")) return new PriorityField("[1-5P]", true);
    if (name.equals("CITY")) return new MyCityField();
    if (name.equals("ID")) return new MyIdField();
    return super.getField(name);
  }
  
  private class MyCityField extends CityField {
    @Override
    public boolean checkParse(String field, Data data) {
      if (isLastField(+1) && field.length() < 2) return true;
      if (field.length() > 4) return false;
      return super.checkParse(field, data);
    }
    
    @Override
    public void parse(String field, Data data) {
      if (field.length() < 2) return;
      if (field.length() > 4) abort();
      super.parse(field, data);
    }
  }
  
  private class MyIdField extends IdField {
    @Override
    public void parse(String field, Data data) {
      if (!NUMERIC.matcher(field).matches()) abort();
      if (field.length() < 12) return;
      if (field.length() > 12) abort();
      super.parse(field, data);
    }
  }
  
  @Override
  public String adjustMapAddress(String address) {
    address = BY_PTN.matcher(address).replaceAll("BYPASS");
    return super.adjustMapAddress(address);
  }
  private static final Pattern BY_PTN = Pattern.compile("\\bBY\\b", Pattern.CASE_INSENSITIVE);
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "AS",   "ASHLAND",
      "ASH",  "ASHLAND",
      "AY",   "AYLETT",
      "AYL",  "AYLETT",
      "AYLE", "AYLETT",
      "BE",   "BEAVERDAM",
      "BEA",  "BEAVERDAM",
      "BEAV", "BEAVERDAM",
      "BG",   "BOWLING GREEN",
      "BU",   "BUMPASS",
      "BUM",  "BUMPASS",
      "BUMP", "BUMPASS",
      "CH",   "CHESTERFIELD",
      "CHE",  "CHESTERFIELD",
      "CHES", "CHESTERFIELD",
      "DA",   "DAWN",
      "DAW",  "DAWN",
      "DAWN", "DAWN",
      "DO",   "DOSWELL",
      "DOS",  "DOSWELL",
      "DOSW", "DOSWELL",
      "GL",   "GLEN ALLEN",
      "GLE",  "GLEN ALLEN",
      "GLEN", "GLEN ALLEN",
      "GO",   "GOOCHLAND",
      "GOC",  "GOOCHLAND",
      "GOCH", "GOOCHLAND",
      "HA",   "HANOVER",
      "HAN",  "HANOVER",
      "HANO", "HANOVER",
      "HE",   "HENRICO",
      "HEN",  "HENRICO",
      "HENR", "HENRICO",
      "KW",   "KING WILLIAM",
      "KWL",  "KING WILLIAM",
      "KWLM", "KING WILLIAM",
      "LO",   "LOUISA",
      "LOU",  "LOUISA",
      "LOUI", "LOUISA",
      "MA",   "MAIDENS",
      "MAI",  "MAIDENS",
      "MAID", "MAIDENS",
      "MA",   "MANQUIN",
      "MAN",  "MANQUIN",
      "MANQ", "MANQUIN",
      "ME",   "MECHANICSVILLE",
      "MEC",  "MECHANICSVILLE",
      "MECH", "MECHANICSVILLE",
      "MI",   "MILFORD",
      "MIL",  "MILFORD",
      "MO",   "MONTPELIER",
      "MON",  "MONTPELIER",
      "MONT", "MONTPELIER",
      "NC",   "NORTH CHESTERFIELD",
      "NCH",  "NORTH CHESTERFIELD",
      "NCHS", "NORTH CHESTERFIELD",
      "NK",   "NEW KENT",
      "NKN",  "NEW KENT",
      "NKNT", "NEW KENT",
      "PB",   "PETERSBURG",
      "PBR",  "PETERSBURG",
      "PBRG", "PETERSBURG",
      "PV",   "PROVIDENCE FORGE",
      "PVF",  "PROVIDENCE FORGE",
      "PVFG", "PROVIDENCE FORGE",
      "RI",   "RICHMOND",
      "RIC",  "RICHMOND",
      "RICH", "RICHMOND",
      "RO",   "ROCKVILLE",
      "ROC",  "ROCKVILLE",
      "ROCK", "ROCKVILLE",
      "RU",   "RUTHER GLEN",
      "RUT",  "RUTHER GLEN",
      "RUTH", "RUTHER GLEN",
      "SA",   "SANDSTON",
      "SAN",  "SANDSTON",
      "SAND", "SANDSTON",
      "SP",   "SPOTSYLVANIA",
      "SPO",  "SPOTSYLVANIA",
      "SPOT", "SPOTSYLVANIA",
      "ST",   "STULDEY",
      "STU",  "STULDEY",
      "STUD", "STULDEY"

  });
}
